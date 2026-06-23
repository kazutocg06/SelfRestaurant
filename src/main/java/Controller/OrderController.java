package Controller;

import ConnectDatabase.*;
import Model.OrderDetails;
import View.CardOrder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

public class OrderController {
    
    private JPanel jPanelOrder;

    public OrderController(JPanel jPanelOrder) {
        this.jPanelOrder = jPanelOrder;
    }

    public void loadOrdersToUI() {
        if (jPanelOrder == null) return;
        
        jPanelOrder.removeAll();
        jPanelOrder.setLayout(new BorderLayout());
        jPanelOrder.setOpaque(false);

        JPanel gridPanel = new JPanel(new GridLayout(0, 2, 15, 15));
        gridPanel.setOpaque(false);

      
        String sql = "SELECT o.order_id, o.created_at, o.total_amount, o.order_status, "
                   + "od.orderdetail_id, od.item_id, od.item_name, od.quantity, od.price_at_order, od.item_status "
                   + "FROM Orders o "
                   + "LEFT JOIN OrderDetails od ON o.order_id = od.order_id " 
                   + "WHERE CAST(o.created_at AS DATE) = CAST(GETDATE() AS DATE) " 
                   + "ORDER BY o.created_at DESC";

     
        Map<String, Object[]> orderDataMap = new LinkedHashMap<>();
        Map<String, List<OrderDetails>> orderItemsMap = new LinkedHashMap<>();

        try (Connection con = ConnectDatabase.DBConnect.getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
             
            while (rs.next()) {
                String orderId = rs.getString("order_id");
                
               
                if (!orderDataMap.containsKey(orderId)) {
                    java.sql.Timestamp ts = rs.getTimestamp("created_at");
                    String timeStr = (ts != null) ? new java.text.SimpleDateFormat("HH:mm").format(ts) : "--:--";
                    orderDataMap.put(orderId, new Object[]{timeStr, rs.getDouble("total_amount"), rs.getString("order_status")});
                    orderItemsMap.put(orderId, new ArrayList<>());
                }
                
           
                String detailId = rs.getString("orderdetail_id");
                if (detailId != null) { 
                    OrderDetails detail = new OrderDetails(
                        detailId, 
                        orderId, 
                        rs.getString("item_id"),
                        rs.getString("item_name"), 
                        rs.getInt("quantity"),
                        rs.getDouble("price_at_order"), 
                        rs.getString("item_status")
                    );
                    orderItemsMap.get(orderId).add(detail);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // ==========================================================
        // DUYỆT BỘ NHỚ RAM ĐỂ VẼ GIAO DIỆN LÊN MÀN HÌNH
        // ==========================================================
        for (String orderId : orderDataMap.keySet()) {
            Object[] info = orderDataMap.get(orderId);
            List<OrderDetails> items = orderItemsMap.get(orderId);
            
            // Ép kiểu dữ liệu lấy từ Map ra
            String timeStr = (String) info[0];
            double total = (Double) info[1];
            String status = (String) info[2];

            // Truyền 'this' để thẻ có thể tự Load lại lưới khi bấm Hủy đơn
            CardOrder card = new CardOrder(orderId, timeStr, items, total, status, this);
            gridPanel.add(card);
        }

        // ==========================================================
        // XỬ LÝ THANH CUỘN VÀ TRƯỜNG HỢP TRỐNG
        // ==========================================================
       if (gridPanel.getComponentCount() == 0) {
            JLabel lblEmpty = new JLabel("Chưa có đơn hàng nào hôm nay", SwingConstants.CENTER);
            lblEmpty.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 18));
            lblEmpty.setForeground(new Color(180, 180, 180));
            jPanelOrder.add(lblEmpty, BorderLayout.CENTER);
            
        } else {
            // CHIẾN THUẬT: TẠO MỘT CÁI KHUNG BỌC (WRAPPER) ĐỂ NEO LƯỚI LÊN TRÊN CÙNG
            JPanel wrapperPanel = new JPanel(new BorderLayout());
            wrapperPanel.setOpaque(false);
            
            // Nhét gridPanel vào phía Bắc (NORTH) của khung bọc. 
            // Việc này ép cái lưới phải dính sát lên trên cùng, không được chảy xệ xuống dưới.
            wrapperPanel.add(gridPanel, BorderLayout.NORTH); 

            // Bỏ cái khung bọc (wrapperPanel) vào thanh cuộn thay vì bỏ trực tiếp gridPanel
            JScrollPane scrollMain = new JScrollPane(wrapperPanel); 
            
            scrollMain.setOpaque(false);
            scrollMain.getViewport().setOpaque(false);
            scrollMain.setBorder(BorderFactory.createEmptyBorder()); // Trị viền xám
            scrollMain.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
            scrollMain.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            
            Controller.KineticScroller.setup(scrollMain);
            jPanelOrder.add(scrollMain, BorderLayout.CENTER);
        }

        jPanelOrder.revalidate();
        jPanelOrder.repaint();
    }
}