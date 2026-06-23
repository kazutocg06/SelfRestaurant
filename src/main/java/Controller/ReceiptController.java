package Controller;

import ConnectDatabase.DBConnect;
import View.CardReceipt;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

public class ReceiptController {
    
    private JPanel targetPanel;

    public ReceiptController(JPanel targetPanel) {
        this.targetPanel = targetPanel;
    }

    public void loadReceiptsToUI() {
        if (targetPanel == null) return;

        // 1. Dọn dẹp panel đích
        targetPanel.removeAll();
        targetPanel.setLayout(new BorderLayout());
        targetPanel.setOpaque(false);

        JPanel gridReceipt = new JPanel(new GridLayout(0, 2, 20, 20));
        gridReceipt.setOpaque(false);
        gridReceipt.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ==========================================================
        // CHIẾN THUẬT 1 CÂU SQL: JOIN 2 BẢNG ĐỂ LẤY HẾT DỮ LIỆU TRONG 1 NỐT NHẠC
        // ==========================================================
        String sql = "SELECT r.receipt_id, r.order_id, r.total_amount, r.status, o.created_at AS payment_date, "
                   + "od.item_id, od.item_name, od.quantity, od.price_at_order "
                   + "FROM Receipt r "
                   + "JOIN Orders o ON r.order_id = o.order_id "
                   + "LEFT JOIN OrderDetails od ON r.order_id = od.order_id "
                   + "ORDER BY o.created_at DESC";

        // Dùng Map để nhóm các món ăn vào đúng Hóa đơn trên bộ nhớ RAM
        Map<String, Object[]> receiptDataMap = new LinkedHashMap<>();
        Map<String, List<CardReceipt.ReceiptItem>> receiptItemsMap = new LinkedHashMap<>();

        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String rcId = rs.getString("receipt_id");

                // Nếu là hóa đơn mới xuất hiện lần đầu trong vòng lặp, khởi tạo thông tin chung
                if (!receiptDataMap.containsKey(rcId)) {
                    String ordId = rs.getString("order_id");
                    double total = rs.getDouble("total_amount");
                    String st = rs.getString("status");
                    Timestamp payDate = rs.getTimestamp("payment_date");

                    String timeStr = (payDate != null) ? new SimpleDateFormat("dd/MM/yyyy HH:mm").format(payDate) : "--/--/---- --:--";
                    
                    receiptDataMap.put(rcId, new Object[]{ordId, timeStr, total, st});
                    receiptItemsMap.put(rcId, new ArrayList<>());
                }

                // Đọc thông tin món ăn và nhét trực tiếp vào danh sách trên RAM
                String itemId = rs.getString("item_id");
                if (itemId != null) {
                    // Tạo đối tượng ReceiptItem từ kết quả JOIN
                    CardReceipt.ReceiptItem item = new CardReceipt.ReceiptItem(
                        rs.getString("item_name"),
                        rs.getInt("quantity"),
                        rs.getDouble("price_at_order")
                    );
                    receiptItemsMap.get(rcId).add(item);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // ==========================================================
        // DUYỆT RAM ĐỂ ĐỔ DỮ LIỆU LÊN GIAO DIỆN (KHÔNG CHẠY SQL TẠI ĐÂY)
        // ==========================================================
        for (String rcId : receiptDataMap.keySet()) {
            Object[] info = receiptDataMap.get(rcId);
            List<CardReceipt.ReceiptItem> items = receiptItemsMap.get(rcId);

            String ordId = (String) info[0];
            String timeStr = (String) info[1];
            double total = (Double) info[2];
            String st = (String) info[3];

            // Nạp thẻ vào lưới đồ họa
            gridReceipt.add(new CardReceipt(rcId, ordId, timeStr, items, total, st));
        }

        // 4. Xử lý trường hợp trống hoặc hiển thị thanh cuộn
        if (gridReceipt.getComponentCount() == 0) {
            JLabel lblEmpty = new JLabel("Chưa có hóa đơn nào được xuất", SwingConstants.CENTER);
            lblEmpty.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 18));
            lblEmpty.setForeground(new Color(180, 180, 180));
            targetPanel.add(lblEmpty, BorderLayout.CENTER);
        } else {
            JPanel wrapperPanel = new JPanel(new BorderLayout());
            wrapperPanel.setOpaque(false);
            wrapperPanel.add(gridReceipt, BorderLayout.NORTH);

            JScrollPane scrollReceipt = new JScrollPane(wrapperPanel);
            scrollReceipt.setOpaque(false);
            scrollReceipt.getViewport().setOpaque(false);
            scrollReceipt.setBorder(null);
            scrollReceipt.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
            scrollReceipt.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            
            scrollReceipt.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);

            KineticScroller.setup(scrollReceipt);
            targetPanel.add(scrollReceipt, BorderLayout.CENTER);
        }

        // 5. Làm mới lại giao diện
        targetPanel.revalidate();
        targetPanel.repaint();
    }
}