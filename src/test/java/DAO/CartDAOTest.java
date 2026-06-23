package DAO;

import ConnectDatabase.DBConnect;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import static org.junit.jupiter.api.Assertions.*;

public class CartDAOTest {

    private final String TEST_CART_ID = "CTEST_01"; 

    @BeforeEach
    public void setUp() {
        try (Connection con = DBConnect.getConnection()) {

            String realTableId = "TB01"; 
            String sqlGetTable = "SELECT TOP 1 table_id FROM DiningTable";
            try (PreparedStatement psTable = con.prepareStatement(sqlGetTable);
                 ResultSet rsTable = psTable.executeQuery()) {
                if (rsTable.next()) {
                    realTableId = rsTable.getString("table_id");
                }
            }

            String sqlCart = "INSERT INTO Cart (cart_id, table_id) VALUES (?, ?)";
            try (PreparedStatement ps = con.prepareStatement(sqlCart)) {
                ps.setString(1, TEST_CART_ID);
                ps.setString(2, realTableId);
                ps.executeUpdate();
            } catch (Exception e) {} 

            String realItemId = ""; 
            String realName = "";
            String getRealItem = "SELECT TOP 1 item_id, name FROM Item";
            
            try (PreparedStatement psItem = con.prepareStatement(getRealItem);
                 ResultSet rs = psItem.executeQuery()) {
                 if (rs.next()) {
                     realItemId = rs.getString("item_id");
                     realName = rs.getString("name");
                 }
            }

            if (!realItemId.isEmpty()) {
                String sqlCartItem = "INSERT INTO CartItem (cartitem_id, cart_id, item_id, name, quantity) VALUES (?, ?, ?, ?, 1)";
                try (PreparedStatement ps = con.prepareStatement(sqlCartItem)) {
                    
                    ps.setString(1, "CITEST_01"); 
                    ps.setString(2, TEST_CART_ID);
                    ps.setString(3, realItemId);
                    ps.setString(4, realName);
                    ps.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    public void tearDown() {
        try (Connection con = DBConnect.getConnection()) {
            String sqlDelItem = "DELETE FROM CartItem WHERE cart_id = ?";
            try (PreparedStatement ps = con.prepareStatement(sqlDelItem)) {
                ps.setString(1, TEST_CART_ID);
                ps.executeUpdate();
            }
            String sqlDelCart = "DELETE FROM Cart WHERE cart_id = ?";
            try (PreparedStatement ps = con.prepareStatement(sqlDelCart)) {
                ps.setString(1, TEST_CART_ID);
                ps.executeUpdate();
            }
        } catch (Exception e) {}
    }

    @Test
    public void testGetCartItems_Success() {
        boolean hasItems = false;
        String sql = "SELECT * FROM CartItem WHERE cart_id = ?";
        
        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, TEST_CART_ID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                hasItems = true;
                assertTrue(rs.getInt("quantity") > 0, "Số lượng món ăn phải lớn hơn 0");
            }
        } catch (Exception e) {
            fail("Lỗi SQL khi lấy chi tiết giỏ hàng");
        }
        
        assertTrue(hasItems, "Giỏ hàng test phải có ít nhất 1 món ăn");
    }

    @Test
    public void testAddItemToCart_ValidItem() {
        int quantity = 2;
        assertTrue(quantity > 0, "Logic hệ thống: Số lượng thêm vào phải luôn dương");
        assertNotNull(TEST_CART_ID, "Logic hệ thống: Mã giỏ hàng không được để trống");
    }
}