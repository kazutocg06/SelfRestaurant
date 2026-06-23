package DAO;

import ConnectDatabase.DBConnect;
import Model.OrderDetails;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailsDAO {

    public List<OrderDetails> getOrderDetailsByOrderId(String orderId) {
        List<OrderDetails> list = new ArrayList<>();
        String sql = "SELECT orderdetail_id, order_id, item_id, item_name, quantity, price_at_order, item_status "
                   + "FROM OrderDetails WHERE order_id = ?";

        try (Connection con = DBConnect.getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderDetails detail = new OrderDetails(
                        rs.getString("orderdetail_id"),
                        rs.getString("order_id"),
                        rs.getString("item_id"),
                        rs.getString("item_name"),
                        rs.getInt("quantity"),
                        rs.getFloat("price_at_order"),
                        rs.getString("item_status")
                    );
                    list.add(detail);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean cancelSingleItem(String orderDetailId) {
        String sql = "UPDATE OrderDetails SET item_status = N'Canceled' WHERE orderdetail_id = ?";
        
        try (Connection con = DBConnect.getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, orderDetailId);
            return ps.executeUpdate() > 0; 
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean cancelWholeOrder(String orderId) {
        String sqlOrder = "UPDATE Orders SET order_status = 'Canceled' WHERE order_id = ?";
        String sqlDetails = "UPDATE OrderDetails SET item_status = N'Canceled' WHERE order_id = ?";
       
        try (Connection con = DBConnect.getConnection()) {
            con.setAutoCommit(false); 
            
            try (PreparedStatement psOrder = con.prepareStatement(sqlOrder);
                 PreparedStatement psDetails = con.prepareStatement(sqlDetails)) {
                
                psOrder.setString(1, orderId);
                psOrder.executeUpdate();
                
                psDetails.setString(1, orderId);
                psDetails.executeUpdate();
                
                con.commit(); 
                return true;
                
            } catch (SQLException ex) {
                con.rollback(); 
                ex.printStackTrace();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updateOrderStatus(String orderId, String newStatus) {
        String sql = "UPDATE Orders SET order_status = ? WHERE order_id = ?";
        
        try (Connection con = DBConnect.getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, newStatus);
            ps.setString(2, orderId);
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}