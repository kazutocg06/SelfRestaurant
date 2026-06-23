package Model;

public class OrderDetails {
    private String orderDetailId;
    private String orderId;
    private String itemId;
    private String itemName;
    private int quantity;
    private double priceAtOrder;
    private String itemStatus;
    
    public OrderDetails() {
    }

    public OrderDetails(String orderDetailId, String orderId, String itemId, String itemName, int quantity, double priceAtOrder, String itemStatus) {
        this.orderDetailId = orderDetailId;
        this.orderId = orderId;
        this.itemId = itemId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.priceAtOrder = priceAtOrder;
        this.itemStatus = itemStatus;
    }

    public OrderDetails(String itemName, int quantity) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.itemStatus = "Pending"; 
    }

    public String getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(String orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPriceAtOrder() {
        return priceAtOrder;
    }

    public void setPriceAtOrder(double priceAtOrder) {
        this.priceAtOrder = priceAtOrder;
    }

    public String getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(String itemStatus) {
        this.itemStatus = itemStatus;
    }
    
    public double getTotalPrice() {
        return this.quantity * this.priceAtOrder;
    }
}