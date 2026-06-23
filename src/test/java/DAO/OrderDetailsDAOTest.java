package DAO;

import ConnectDatabase.DBConnect;
import Model.OrderDetails;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class OrderDetailsDAOTest {
    
    private OrderDetailsDAO dao;
    private String autoFetchedOrderId; 

    @BeforeEach
    public void setUp() {
        dao = new OrderDetailsDAO();
        autoFetchedOrderId = getARealOrderIdFromDB();
    }

    @AfterEach
    public void tearDown() {
    }

    private String getARealOrderIdFromDB() {
        String sql = "SELECT TOP 1 order_id FROM OrderDetails"; 
        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getString("order_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; 
    }

    @Test
    public void testGetOrderDetailsByOrderId_Success() {
        assertNotNull(autoFetchedOrderId, "CẢNH BÁO: Database của bạn đang trống trơn, hãy vào App đặt thử 1 đơn rồi mới chạy Test!");
        List<OrderDetails> resultList = dao.getOrderDetailsByOrderId(autoFetchedOrderId);
        assertNotNull(resultList, "Danh sách trả về không được phép Null");
        assertFalse(resultList.isEmpty(), "Đơn hàng này phải có ít nhất 1 món ăn");
        assertEquals(autoFetchedOrderId, resultList.get(0).getOrderId(), "Mã đơn hàng bị lệch dữ liệu!");
    }

    @Test
    public void testGetOrderDetailsByOrderId_NotFound() {
        String fakeOrderId = "O_GHOST_000"; 

        List<OrderDetails> resultList = dao.getOrderDetailsByOrderId(fakeOrderId);

        assertNotNull(resultList, "Ngay cả khi không tìm thấy, hàm phải trả về List rỗng chứ không được Null");
        assertTrue(resultList.isEmpty(), "Danh sách phải trống vì mã đơn hàng không tồn tại");
    }

    @Test
    public void testUpdateOrderStatus_Valid() {
        assertNotNull(autoFetchedOrderId, "CẢNH BÁO: Database của bạn đang trống trơn!");   
        String targetStatus = "Finish"; 
        boolean isSuccess = dao.updateOrderStatus(autoFetchedOrderId, targetStatus);
        assertTrue(isSuccess, "Cập nhật trạng thái thất bại do lỗi SQL hoặc sai mã đơn");
    }
}