package Model;

import java.sql.Timestamp;

public class Receipt {
    private String receiptId;
    private String orderId;
    private double totalAmount;
    private String status;
    private Timestamp paymentDate;

    public Receipt() {
    }

    public Receipt(String receiptId, String orderId, double totalAmount, String status, Timestamp paymentDate) {
        this.receiptId = receiptId;
        this.orderId = orderId;
        this.totalAmount = totalAmount;
        this.status = status;
        this.paymentDate = paymentDate;
    }


    public String getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(String receiptId) {
        this.receiptId = receiptId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Timestamp paymentDate) {
        this.paymentDate = paymentDate;
    }
}