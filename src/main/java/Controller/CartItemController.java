package Controller;

import DAO.CartDAO;
import DAO.CartItemDAO;
import DAO.OrderDAO;
import Model.CartItem;
import View.CardCartItem;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class CartItemController {
    
    private CartDAO cartDAO;
    private CartItemDAO cartItemDAO;
    private String currentCartId = null; 
    
    private JPanel jPanelCartGrid; 
    private JPanel innerCartPanel; 
    private JPanel jPanelOrder;
    
    private JLabel lblOrderId;     
    private JLabel lblTotalUniqueItems;  
    private JLabel lblTotalQuantity;  
    private JLabel lblGrandTotal;  

    public CartItemController(JPanel jPanelCartGrid) {
        this(jPanelCartGrid, null, null, null, null, null); 
    }
    
    public CartItemController(JPanel jPanelCartGrid, JPanel jPanelOrder, JLabel lblOrderId, JLabel lblTotalUniqueItems, JLabel lblTotalQuantity, JLabel lblGrandTotal) {
        this.cartDAO = new CartDAO();
        this.cartItemDAO = new CartItemDAO();
        this.jPanelCartGrid = jPanelCartGrid;
        this.jPanelOrder = jPanelOrder; 
        
        this.lblOrderId = lblOrderId;       
        this.lblTotalUniqueItems = lblTotalUniqueItems; 
        this.lblTotalQuantity = lblTotalQuantity; 
        this.lblGrandTotal = lblGrandTotal; 
        
        if (this.jPanelCartGrid != null) {
            this.jPanelCartGrid.setLayout(new BorderLayout());
            this.jPanelCartGrid.setOpaque(false);
            this.innerCartPanel = new JPanel() {
                @Override
                public Dimension getPreferredSize() {
                    Dimension d = super.getPreferredSize();
                    d.width = 740;
                    return d;
                }
                
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    g2.setColor(Color.WHITE);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                    
                    g2.setColor(new Color(210, 215, 220));
                    g2.setStroke(new java.awt.BasicStroke(2.0f));
                    g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 20, 20);
                    g2.dispose();
                }
            };
            this.innerCartPanel.setOpaque(false);
            this.innerCartPanel.setLayout(new BorderLayout()); 
            this.jPanelCartGrid.add(this.innerCartPanel, BorderLayout.CENTER);
        }
        
        this.currentCartId = cartDAO.createNewCart();
        if (this.lblOrderId != null && currentCartId != null) {
            this.lblOrderId.setText("#" + currentCartId); 
        }
        loadCartToUI();
    }

    public void addToCart(String itemId, String itemName, double price) {
        if (currentCartId == null) return;
        boolean success = cartItemDAO.addOrUpdateItem(currentCartId, itemId, itemName, 1);
        if (success) loadCartToUI(); 
    }

    public void increaseQuantity(String itemId) {
        if (currentCartId == null) return;
        cartItemDAO.updateQuantity(currentCartId, itemId, 1);
        loadCartToUI();
    }

    public void decreaseQuantity(String itemId) {
        if (currentCartId == null) return;
        cartItemDAO.updateQuantity(currentCartId, itemId, -1);
        loadCartToUI();
    }

    public void removeItem(String itemId) {
        if (currentCartId == null) return;
        cartItemDAO.removeItem(currentCartId, itemId);
        loadCartToUI();
    }

    public void loadCartToUI() {
        if (currentCartId == null || innerCartPanel == null) return;
        
        innerCartPanel.removeAll();

        List<CartItem> listItems = cartItemDAO.getCartItems(currentCartId);
        
        double grandTotal = 0;
        int totalQuantity = 0;
        int totalUniqueItems = listItems.size();
        
        if (listItems.isEmpty()) {
            JLabel lblEmpty = new JLabel("Chưa có món nào trong giỏ");
            lblEmpty.setFont(new Font("Arial", Font.BOLD, 18));
            lblEmpty.setForeground(new Color(180, 180, 180));
            lblEmpty.setHorizontalAlignment(SwingConstants.CENTER);
            
            innerCartPanel.add(lblEmpty, BorderLayout.CENTER);
            
        } else {
            JPanel listPanel = new JPanel();
            listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
            listPanel.setOpaque(false);        
            JPanel safeAreaPanel = new JPanel(new BorderLayout());
            safeAreaPanel.setOpaque(false);
            safeAreaPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 15, 60, 15));
            
            for (CartItem item : listItems) {
                grandTotal += item.getTotalPrice(); 
                totalQuantity += item.getQuantity();   
                
                CardCartItem card = new CardCartItem(item, this);
                JPanel rowWrapper = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 0));
                rowWrapper.setOpaque(false);
                rowWrapper.add(card);
                
                listPanel.add(rowWrapper);
                listPanel.add(Box.createVerticalStrut(15)); 
            }
            safeAreaPanel.add(listPanel, BorderLayout.NORTH);           
            innerCartPanel.add(safeAreaPanel, BorderLayout.NORTH);
        }
        
        if (lblTotalUniqueItems != null) lblTotalUniqueItems.setText(String.valueOf(totalUniqueItems));
        if (lblTotalQuantity != null) lblTotalQuantity.setText(String.valueOf(totalQuantity));
        if (lblGrandTotal != null) lblGrandTotal.setText(String.format("%,.0f VNĐ", grandTotal));      
        jPanelCartGrid.revalidate();
        jPanelCartGrid.repaint();
        
        javax.swing.SwingUtilities.invokeLater(() -> {
            java.awt.Container parent = jPanelCartGrid.getParent();
            if (parent instanceof javax.swing.JViewport) {
                parent.revalidate();
                parent.repaint();
            }
        });
    }

    public boolean confirmOrder() {
        if (currentCartId == null) return false;
        List<CartItem> items = cartItemDAO.getCartItems(currentCartId);
        
        if (items.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(null, "Giỏ hàng trống! Hãy thêm món trước khi đặt.");
            return false;
        }

        double grandTotal = 0;
        for (CartItem item : items) {
            grandTotal += item.getTotalPrice();
        }

        OrderDAO orderDAO = new OrderDAO();
        String newOrderId = orderDAO.placeOrder(currentCartId, grandTotal);

        if (newOrderId != null) {
            javax.swing.JOptionPane.showMessageDialog(null, "Đặt hàng thành công! Mã đơn:" + newOrderId);
            
            if (this.jPanelOrder != null) {
                OrderController orderController = new OrderController(this.jPanelOrder);
                orderController.loadOrdersToUI();
            }

            this.currentCartId = cartDAO.createNewCart();
            if (this.lblOrderId != null) {
                this.lblOrderId.setText("#" + currentCartId); 
            }
            loadCartToUI();
            return true;
        }
        return false;
    }

    public String getCurrentCartId() {
        return currentCartId;
    }
}