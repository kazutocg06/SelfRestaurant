package Controller;

import ConnectDatabase.DBConnect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class KitchenSimulator {
    private static KitchenSimulator instance;
    private Map<String, Long> transitionTimes = new HashMap<>();
    private Random rand = new Random();
    private OrderController currentController; // Lưu controller để ra lệnh refresh màn hình
    private boolean isRunning = false;

    private KitchenSimulator() {}

    // Dùng Singleton để đảm bảo chỉ có duy nhất 1 "Nhà bếp" hoạt động
    public static KitchenSimulator getInstance() {
        if (instance == null) {
            instance = new KitchenSimulator();
        }
        return instance;
    }

    public void setOrderController(OrderController controller) {
        this.currentController = controller;
    }

    public void startSimulation() {
        if (isRunning) return;
        isRunning = true;

        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(2000); // Quét Database mỗi 2 giây
                    boolean uiNeedsRefresh = simulateCooking();
                    
                    // Nếu có món nấu xong và đang mở tab Đơn hàng -> Cập nhật UI
                    if (uiNeedsRefresh && currentController != null) {
                        javax.swing.SwingUtilities.invokeLater(() -> {
                            currentController.loadOrdersToUI();
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.setDaemon(true); // Thread tự động chết khi tắt phần mềm
        thread.start();
    }

    // ==========================================================
    // GIỮ NGUYÊN 100% HÀM SIMULATE COOKING THEO YÊU CẦU CỦA BẠN
    // ==========================================================
    private boolean simulateCooking() {
        boolean updated = false;
        long currentTime = System.currentTimeMillis();

        // Chỉ quét các món đang Pending/Cooking của những đơn chưa bị Hủy trong ngày hôm nay
        String sqlGetItems = "SELECT od.orderdetail_id, od.order_id, od.item_status " +
                             "FROM OrderDetails od " +
                             "JOIN Orders o ON od.order_id = o.order_id " +
                             "WHERE (od.item_status = 'Pending' OR od.item_status = 'Cooking') " +
                             "AND o.order_status != 'Canceled' " +
                             "AND CAST(o.created_at AS DATE) = CAST(GETDATE() AS DATE)";

        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sqlGetItems);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String detailId = rs.getString("orderdetail_id");
                String orderId = rs.getString("order_id");
                String status = rs.getString("item_status").trim();

                if (!transitionTimes.containsKey(detailId)) {
                    // Nếu món mới vào, bấm giờ random từ 10s đến 15s (10000ms - 15000ms)
                    long delay = 15000 + rand.nextInt(10000); 
                    transitionTimes.put(detailId, currentTime + delay);
                } else {
                    // Nếu thời gian đếm ngược đã kết thúc
                    if (currentTime >= transitionTimes.get(detailId)) {
                        // Pending -> Cooking, Cooking -> Finish
                        String newStatus = status.equalsIgnoreCase("Pending") ? "Cooking" : "Finish";
                        updateItemStatusDB(detailId, newStatus);
                        transitionTimes.remove(detailId); // Xóa để bấm giờ cho Phase tiếp theo
                        checkAndUpdateOrderStatus(orderId); // Cập nhật lại trạng thái cha
                        updated = true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return updated;
    }

    private void updateItemStatusDB(String detailId, String newStatus) {
        String sql = "UPDATE OrderDetails SET item_status = ? WHERE orderdetail_id = ?";
        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setString(2, detailId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();}
    }

    // ==========================================================
    // CHỈNH SỬA LẠI LOGIC CHỐT TRẠNG THÁI & HIỂN THỊ THÔNG BÁO
    // ==========================================================
    private void checkAndUpdateOrderStatus(String orderId) {
        String sql = "SELECT item_status FROM OrderDetails WHERE order_id = ?";
        
        boolean hasPending = false;
        boolean hasCooking = false;
        boolean hasFinish = false;
        int validItems = 0;

        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, orderId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                String st = rs.getString("item_status").trim().toLowerCase();
                if (!st.equals("canceled") && !st.equals("đã hủy")) {
                    validItems++;
                    if (st.equals("pending") || st.equals("chuẩn bị")) {
                        hasPending = true;
                    } else if (st.equals("cooking") || st.equals("đang nấu")) {
                        hasCooking = true;
                    } else if (st.equals("finish") || st.equals("hoàn thành")) {
                        hasFinish = true;
                    }
                }
            }
            if (validItems == 0) return; // Mọi món đều bị hủy lẻ

            // LOGIC NGHIỆP VỤ MỚI: Tính toán trạng thái tổng chuẩn xác nhất
            String newOrderStatus = null;
            if (!hasPending && !hasCooking && hasFinish) {
                newOrderStatus = "Finish"; // 100% các món đã nấu xong
            } else if (hasCooking || (hasPending && hasFinish)) {
                newOrderStatus = "Cooking"; // Có món đang nấu, HOẶC nấu xong một nửa nhưng vẫn còn món đang đợi
            }

            if (newOrderStatus != null) {
                String updateSql = "UPDATE Orders SET order_status = ? WHERE order_id = ? AND order_status != 'Canceled' AND order_status != ?";
                try (PreparedStatement updatePs = con.prepareStatement(updateSql)) {
                    updatePs.setString(1, newOrderStatus);
                    updatePs.setString(2, orderId);
                    updatePs.setString(3, newOrderStatus);
                    
                    int rowsUpdated = updatePs.executeUpdate();
                    
                    // CHỈ GỌI BIÊN LAI VÀ THÔNG BÁO KHI ĐƠN THỰC SỰ ĐƯỢC CHỐT SANG FINISH THÀNH CÔNG
                    if (rowsUpdated > 0 && newOrderStatus.equalsIgnoreCase("Finish")) {
                        new DAO.ReceiptDAO().insertReceiptFromOrder(orderId);
                        
                        // HIỂN THỊ THÔNG BÁO TOAST TRƯỢT XUỐNG
                        javax.swing.SwingUtilities.invokeLater(() -> {
                            javax.swing.JFrame mainFrame = null;
                            for (java.awt.Window window : java.awt.Window.getWindows()) {
                                if (window instanceof javax.swing.JFrame) {
                                    mainFrame = (javax.swing.JFrame) window;
                                    break;
                                }
                            }
                            if (mainFrame != null) {
                                View.ToastNotification.show(mainFrame, "Đơn hàng " + orderId + " đã sẵn sàng!");
                            }
                        });
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}