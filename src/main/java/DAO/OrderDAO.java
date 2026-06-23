package DAO;

import ConnectDatabase.DBConnect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderDAO {

    public String placeOrder(String cartId, double totalAmount) {
        
        String orderId = "O" + (System.currentTimeMillis() % 100000000); 

        String sqlGetTable = "SELECT table_id FROM Cart WHERE cart_id = ?";
        String sqlInsertOrder = "INSERT INTO Orders (order_id, table_id, total_amount, order_status, created_at) VALUES (?, ?, ?, N'Chuẩn bị', GETDATE())";
        String sqlGetCartItems = "SELECT ci.item_id, ci.name, ci.quantity, i.price FROM CartItem ci JOIN Item i ON ci.item_id = i.item_id WHERE ci.cart_id = ?";
        String sqlInsertDetail = "INSERT INTO OrderDetails (orderdetail_id, order_id, item_id, item_name, quantity, price_at_order, item_status) VALUES (?, ?, ?, ?, ?, ?, 'Pending')";
        String sqlDeleteCartItem = "DELETE FROM CartItem WHERE cart_id = ?";
        String sqlDeleteCart = "DELETE FROM Cart WHERE cart_id = ?";

        try (Connection con = DBConnect.getConnection()) {
            
            con.setAutoCommit(false); 

            try {
                String tableId = "TB01"; 
                try (PreparedStatement psTable = con.prepareStatement(sqlGetTable)) {
                    psTable.setString(1, cartId);
                    try (ResultSet rs = psTable.executeQuery()) {
                        if (rs.next()) tableId = rs.getString("table_id");
                    }
                }

                try (PreparedStatement psOrder = con.prepareStatement(sqlInsertOrder)) {
                    psOrder.setString(1, orderId);
                    psOrder.setString(2, tableId);
                    psOrder.setDouble(3, totalAmount);
                    psOrder.executeUpdate();
                }

                try (PreparedStatement psGetItems = con.prepareStatement(sqlGetCartItems);
                     PreparedStatement psInsertDetail = con.prepareStatement(sqlInsertDetail)) {

                    psGetItems.setString(1, cartId);
                    try (ResultSet rsItems = psGetItems.executeQuery()) {
                        int count = 1;
                        while (rsItems.next()) {
                            String detailId = "OD" + ((System.currentTimeMillis() % 1000000) + count++);
                            
                            psInsertDetail.setString(1, detailId);
                            psInsertDetail.setString(2, orderId);
                            psInsertDetail.setString(3, rsItems.getString("item_id"));
                            psInsertDetail.setString(4, rsItems.getString("name"));
                            psInsertDetail.setInt(5, rsItems.getInt("quantity"));
                            psInsertDetail.setDouble(6, rsItems.getDouble("price"));
                            psInsertDetail.executeUpdate();
                        }
                    }
                }

                try (PreparedStatement psDelItem = con.prepareStatement(sqlDeleteCartItem)) {
                    psDelItem.setString(1, cartId);
                    psDelItem.executeUpdate();
                }
                try (PreparedStatement psDelCart = con.prepareStatement(sqlDeleteCart)) {
                    psDelCart.setString(1, cartId);
                    psDelCart.executeUpdate();
                }

                con.commit(); 
                return orderId;

            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        } catch (java.sql.SQLException e) {
            javax.swing.JOptionPane.showMessageDialog(null, 
                "Lỗi kết nối hoặc vi phạm ràng buộc CSDL:\n" + e.getMessage(), 
                "Lỗi Chốt Đơn (SQL Error)", 
                javax.swing.JOptionPane.ERROR_MESSAGE);
            
            e.printStackTrace();
            return null;
        }
    }
}