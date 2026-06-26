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
    private OrderController currentController; 
    private boolean isRunning = false;

    private KitchenSimulator() {}

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
                    Thread.sleep(2000); 
                    boolean uiNeedsRefresh = simulateCooking();
                    
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
        thread.setDaemon(true);
        thread.start();
    }

    private boolean simulateCooking() {
        boolean updated = false;
        long currentTime = System.currentTimeMillis();

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
                    long delay = 15000 + rand.nextInt(10000); 
                    transitionTimes.put(detailId, currentTime + delay);
                } else {
                    if (currentTime >= transitionTimes.get(detailId)) {
                        String newStatus = status.equalsIgnoreCase("Pending") ? "Cooking" : "Finish";
                        updateItemStatusDB(detailId, newStatus);
                        transitionTimes.remove(detailId);
                        checkAndUpdateOrderStatus(orderId);
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
            if (validItems == 0) return;

            String newOrderStatus = null;
            if (!hasPending && !hasCooking && hasFinish) {
                newOrderStatus = "Finish";
            } else if (hasCooking || (hasPending && hasFinish)) {
                newOrderStatus = "Cooking";
            }

            if (newOrderStatus != null) {
                String updateSql = "UPDATE Orders SET order_status = ? WHERE order_id = ? AND order_status != 'Canceled' AND order_status != ?";
                try (PreparedStatement updatePs = con.prepareStatement(updateSql)) {
                    updatePs.setString(1, newOrderStatus);
                    updatePs.setString(2, orderId);
                    updatePs.setString(3, newOrderStatus);
                    
                    int rowsUpdated = updatePs.executeUpdate();
                    
                    if (rowsUpdated > 0 && newOrderStatus.equalsIgnoreCase("Finish")) {
                        new DAO.ReceiptDAO().insertReceiptFromOrder(orderId);
                        
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