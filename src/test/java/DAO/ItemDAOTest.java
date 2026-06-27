package DAO;

import ConnectDatabase.DBConnect;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import static org.junit.jupiter.api.Assertions.*;

public class ItemDAOTest {

    @Test
    public void testItemPrice_MustBePositive() {
        boolean validPrices = true;
        String sql = "SELECT price, name FROM Item";

        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                double price = rs.getDouble("price");
                if (price <= 0) {
                    validPrices = false;
                    System.out.println("CẢNH BÁO: Món [" + rs.getString("name") + "] đang để giá sai: " + price);
                }
            }
        } catch (Exception e) {
            fail("Lỗi kết nối SQL khi kiểm tra giá món ăn");
        }

        assertTrue(validPrices, "Tất cả các món ăn trong thực đơn bắt buộc phải có giá lớn hơn 0!");
    }

    @Test
    public void testItemMenuId_IdNotNull() {
        boolean hasOrphanItems = false;
        String sql = "SELECT item_id, name FROM Item WHERE menu_id IS NULL OR menu_id = ''";

        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                hasOrphanItems = true;
                System.out.println("LỖI: Món [" + rs.getString("name") + "] không thuộc danh mục nào!");
            }
        } catch (Exception e) {
            fail("Lỗi truy vấn danh mục món ăn");
        }

        assertFalse(hasOrphanItems, "Nghiêm trọng: Phát hiện món ăn bị mồ côi, không được gán vào mã danh mục (menu_id)!");
    }

    @Test
    public void testItemAvailability_FlagValid() {
        boolean flagValid = true;
        String sql = "SELECT is_available, name FROM Item";

        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int stockCount = rs.getInt("is_available");
                if (stockCount < 0) {
                    flagValid = false;
                    System.out.println("LỖI: Món [" + rs.getString("name") + "] có số lượng tồn kho bị âm: " + stockCount);
                }
            }
        } catch (Exception e) {
            fail("Lỗi cấu trúc dữ liệu số lượng món ăn");
        }

        assertTrue(flagValid, "Số lượng món ăn khả dụng còn lại trong kho không được phép là một số âm!");
    }
}
