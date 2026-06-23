package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import ConnectDatabase.DBConnect;
import java.util.Random;

public class CartDAO {

    public String createNewCart() {
        String cartId = "C" + (new Random().nextInt(90000) + 10000); 
        String sql = "INSERT INTO Cart (cart_id, table_id) VALUES (?, 'TB01')"; 

        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, cartId);
            if (ps.executeUpdate() > 0) {
                return cartId; 
            }
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(null, 
                "Hệ thống không thể tạo Giỏ hàng mới!\nVui lòng kiểm tra kết nối SQL hoặc dữ liệu bàn mặc định.\nChi tiết: " + e.getMessage(), 
                "Lỗi Khởi Tạo Giỏ (SQL Error)", 
                javax.swing.JOptionPane.ERROR_MESSAGE);
            
            e.printStackTrace();
            return null;
        }
        return null;
    }
    
    public void deleteCartAndItems(String cartId) {
        String sqlDeleteItems = "DELETE FROM CartItem WHERE cart_id  = ?";
        String sqlDeleteCart = "DELETE FROM Cart WHERE cart_id = ?";
        try (java.sql.Connection con = DBConnect.getConnection(); 
             java.sql.PreparedStatement psItem = con.prepareStatement(sqlDeleteItems);
             java.sql.PreparedStatement psCart = con.prepareStatement(sqlDeleteCart)) {

             psItem.setString(1, cartId);
             psItem.executeUpdate();

             psCart.setString(1, cartId);
             psCart.executeUpdate();
             
             System.out.println("Đã dọn dẹp sạch sẽ giỏ hàng #" + cartId + " khỏi Database!");
             
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

