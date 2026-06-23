package DAO;

import ConnectDatabase.DBConnect;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import static org.junit.jupiter.api.Assertions.*;

public class ReceiptDAOTest {

    private String autoFetchedReceiptId;

    @BeforeEach
    public void setUp() {
        String sql = "SELECT TOP 1 receipt_id FROM Receipt";
        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                autoFetchedReceiptId = rs.getString("receipt_id");
            }
        } catch (Exception e) {}
    }

    @Test
    public void testLoadReceipt_Success() {
        assertNotNull(autoFetchedReceiptId, "Database chưa có Hóa đơn nào! Hãy test nấu xong 1 đơn hàng trước.");
        
        double totalAmount = -1;
        String sql = "SELECT total_amount FROM Receipt WHERE receipt_id = ?";
        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, autoFetchedReceiptId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                totalAmount = rs.getDouble("total_amount");
            }
        } catch (Exception e) {
            fail("Lỗi SQL");
        }

        assertTrue(totalAmount >= 0, "Lỗi dữ liệu: Tổng tiền hóa đơn bị âm!");
    }

    @Test
    public void testReceiptHasValidOrderId() {
        assertNotNull(autoFetchedReceiptId, "Thiếu dữ liệu Hóa đơn để test");
        
        String linkedOrderId = null;
        String sql = "SELECT order_id FROM Receipt WHERE receipt_id = ?";
        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, autoFetchedReceiptId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                linkedOrderId = rs.getString("order_id");
            }
        } catch (Exception e) {
            fail("Lỗi truy xuất Hóa đơn");
        }
        
        assertNotNull(linkedOrderId, "Lỗi nghiêm trọng: Hóa đơn bị mồ côi (Không liên kết với Đơn hàng nào)");
        assertTrue(linkedOrderId.startsWith("O"), "Mã đơn hàng liên kết sai định dạng (Phải bắt đầu bằng chữ O)");
    }
}