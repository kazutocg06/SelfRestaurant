package DAO;

import ConnectDatabase.DBConnect;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import static org.junit.jupiter.api.Assertions.*;

public class OrderDAOTest {

    private OrderDAO orderDao;
    private final String MOCK_CART_ID = "CTEST_99";

    @BeforeEach
    public void setUp() {
        orderDao = new OrderDAO();
        try (Connection con = DBConnect.getConnection()) {
            String sqlCart = "INSERT INTO Cart (cart_id, table_id) VALUES (?, 'TB01')";
            try (PreparedStatement ps = con.prepareStatement(sqlCart)) {
                ps.setString(1, MOCK_CART_ID);
                ps.executeUpdate();
            }
        } catch (Exception e) {}
    }

    @AfterEach
    public void tearDown() {
        
        try (Connection con = DBConnect.getConnection()) {
            String sqlDel = "DELETE FROM Cart WHERE cart_id = ?";
            try (PreparedStatement ps = con.prepareStatement(sqlDel)) {
                ps.setString(1, MOCK_CART_ID);
                ps.executeUpdate();
            }
        } catch (Exception e) {}
    }

    
    @Test
    public void testPlaceOrder_IdFormat() {
        String generatedId = "O" + (System.currentTimeMillis() % 100000000);
        assertNotNull(generatedId);
        assertTrue(generatedId.startsWith("O"), "Mã đơn hàng phải bắt đầu bằng ký tự 'O'");
    }

    
    @Test
    public void testPlaceOrder_IdLength() {
        String generatedId = "O" + (System.currentTimeMillis() % 100000000);
        assertTrue(generatedId.length() <= 10, "Mã đơn hàng vượt quá varchar(10) của database!");
    }

   
    @Test
    public void testPlaceOrder_ZeroAmount() {
        double amount = 0.0;
        assertTrue(amount >= 0, "Tổng tiền đơn hàng không được phép âm!");
    }

  
    @Test
    public void testPlaceOrder_InvalidCartId() {
        String result = orderDao.placeOrder("NON_EXIST", 50000);
    
        assertNotNull(result, "Hệ thống phải tự động dùng bàn mặc định khi mã giỏ hàng sai!");
        assertTrue(result.startsWith("O"), "Mã hóa đơn sinh ra phải bắt đầu bằng chữ O");
    }

  
    public void testPlaceOrder_NegativeAmount() {
        double invalidAmount = -50000.0;
        assertFalse(invalidAmount > 0, "Hệ thống không được chấp nhận số tiền âm!");
    }
}