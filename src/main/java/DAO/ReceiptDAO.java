package DAO;

import ConnectDatabase.DBConnect;
import View.CardReceipt.ReceiptItem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ReceiptDAO {

    public void insertReceiptFromOrder(String orderId) {
        String sqlCheck = "SELECT COUNT(*) FROM Receipt WHERE order_id = ?";
        String sqlGetOrder = "SELECT order_status FROM Orders WHERE order_id = ?";
        
        try (Connection con = DBConnect.getConnection()) {
            try (PreparedStatement psCheck = con.prepareStatement(sqlCheck)) {
                psCheck.setString(1, orderId);
                ResultSet rsCheck = psCheck.executeQuery();
                if (rsCheck.next() && rsCheck.getInt(1) > 0) return; 
            }

            try (PreparedStatement psOrder = con.prepareStatement(sqlGetOrder)) {
                psOrder.setString(1, orderId);
                ResultSet rsOrder = psOrder.executeQuery();
                if (rsOrder.next()) {
                    String status = rsOrder.getString("order_status").trim().toLowerCase();
                    if (!status.equals("finish") && !status.equals("hoàn thành") && !status.equals("completed")) {
                        return;
                    }
                }
            }

            double totalAmount = 0;
            String sqlSum = "SELECT SUM(quantity * price_at_order) FROM OrderDetails " +
                            "WHERE order_id = ? AND item_status NOT IN ('Canceled', 'Đã hủy')";
            try (PreparedStatement psSum = con.prepareStatement(sqlSum)) {
                psSum.setString(1, orderId);
                ResultSet rsSum = psSum.executeQuery();
                if (rsSum.next()) {
                    totalAmount = rsSum.getDouble(1);
                }
            }

            String receiptId = "RC" + (10000 + new java.util.Random().nextInt(90000));

            String sqlInsert = "INSERT INTO Receipt (receipt_id, order_id, total_amount, status, payment_date) VALUES (?, ?, ?, 'UNPAID', GETDATE())";
            try (PreparedStatement psInsert = con.prepareStatement(sqlInsert)) {
                psInsert.setString(1, receiptId);
                psInsert.setString(2, orderId);
                psInsert.setDouble(3, totalAmount);
                psInsert.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<ReceiptItem> getReceiptItems(String orderId) {
        List<ReceiptItem> list = new ArrayList<>();
        String sql = "SELECT item_name, quantity, price_at_order FROM OrderDetails " +
                     "WHERE order_id = ? AND item_status NOT IN ('Canceled', 'Đã hủy')";
        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, orderId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new ReceiptItem(
                    rs.getString("item_name"),
                    rs.getInt("quantity"),
                    rs.getDouble("price_at_order")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}