package Model;

import java.util.Date;

public class Cart {
    private String cartId;
    private String status;
  
    public Cart() {
    }

    public Cart(String cartId, String status) {
        this.cartId = cartId;
        this.status = status;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    
}