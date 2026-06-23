package DAO;

import ConnectDatabase.DBConnect;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import static org.junit.jupiter.api.Assertions.*;

public class MenuDAOTest {

    @Test
    public void testLoadAllItems_ReturnsData() {
        int itemCount = 0;
        String sql = "SELECT COUNT(*) AS total FROM Item"; 
        
        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                itemCount = rs.getInt("total");
            }
        } catch (Exception e) {
            fail("Lỗi kết nối SQL khi tải Menu");
        }
        
        assertTrue(itemCount > 0, "Menu hiện tại đang trống! Hãy thêm món ăn vào DB.");
    }

    @Test
    public void testSearchItems_ValidKeyword() {
        String keyword = "Cà phê"; 
        boolean found = false;

        String sql = "SELECT * FROM Item WHERE name LIKE ?";
        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                found = true;
                String itemName = rs.getString("name");
                assertTrue(itemName.toLowerCase().contains("cà phê"), "Kết quả trả về không khớp với từ khóa tìm kiếm");
            }
        } catch (Exception e) {
            fail("Lỗi SQL khi tìm kiếm");
        }
    }
}