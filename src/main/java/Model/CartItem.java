package Model;

public class CartItem {
    private String cartId;
    private String itemId;
    private String itemName; 
    private String imgUrl;   
    private int quantity;
    private double price;
    private int maxStock;

    public CartItem() {
    }

    public CartItem(String cartId,String itemId, String itemName, String imgUrl, int quantity, double price) {
        this.cartId = cartId;
        this.itemId = itemId;
        this.itemName = itemName;
        this.imgUrl = imgUrl;
        this.quantity = quantity;
        this.price = price;
    }

    public double getTotalPrice() {
        return this.quantity * this.price;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    
    public int getMaxStock() {
        return maxStock;
    }

    public void setMaxStock(int maxStock) {
        this.maxStock = maxStock;
    }
}