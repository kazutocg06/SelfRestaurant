package DAO;

import Model.CartItem;
import ConnectDatabase.DBConnect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CartItemDAO {
    private void showOutOfStockPopup(String message) {
        java.awt.Window activeWindow = null;
        for (java.awt.Window window : java.awt.Window.getWindows()) {
            if (window.isShowing() && window instanceof javax.swing.JFrame) {
                activeWindow = window;
                break;
            }
        }

        javax.swing.JDialog customDialog = new javax.swing.JDialog((java.awt.Frame) activeWindow, true);
        customDialog.setUndecorated(true);
        
        if (activeWindow != null) {
            customDialog.setSize(activeWindow.getSize());
            customDialog.setLocationRelativeTo(activeWindow);
        } else {
            customDialog.setSize(1200, 680);
            customDialog.setLocationRelativeTo(null);
        }

        customDialog.setBackground(new java.awt.Color(0, 0, 0, 120)); 

        javax.swing.JPanel backdrop = new javax.swing.JPanel(new java.awt.GridBagLayout());
        backdrop.setOpaque(false);

        backdrop.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                customDialog.dispose();
            }
        });

        javax.swing.JPanel wrapper = new javax.swing.JPanel(new java.awt.BorderLayout()) {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                super.paintComponent(g);
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(java.awt.Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
                
                g2.setColor(new java.awt.Color(255, 102, 51));
                g2.setStroke(new java.awt.BasicStroke(3.0f));
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 24, 24);
                g2.dispose();
            }
        };
        wrapper.setOpaque(false);
        wrapper.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));
        wrapper.setPreferredSize(new java.awt.Dimension(450, 200));

        wrapper.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                e.consume(); 
            }
        });


        javax.swing.JLabel lblTitle = new javax.swing.JLabel("THÔNG BÁO", javax.swing.SwingConstants.CENTER);
        lblTitle.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 22));
        lblTitle.setForeground(new java.awt.Color(255, 102, 51));
        wrapper.add(lblTitle, java.awt.BorderLayout.NORTH);

        javax.swing.JLabel lblMsg = new javax.swing.JLabel("<html><div style='text-align: center; width: 350px;'>" + message + "</div></html>", javax.swing.SwingConstants.CENTER);
        lblMsg.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 18));
        lblMsg.setForeground(new java.awt.Color(50, 50, 50));
        wrapper.add(lblMsg, java.awt.BorderLayout.CENTER);

        javax.swing.JLabel lblHint = new javax.swing.JLabel("(Chạm ra ngoài để đóng)", javax.swing.SwingConstants.CENTER);
        lblHint.setFont(new java.awt.Font("Arial", java.awt.Font.ITALIC, 13));
        lblHint.setForeground(java.awt.Color.GRAY);
        wrapper.add(lblHint, java.awt.BorderLayout.SOUTH);

        backdrop.add(wrapper);
        customDialog.setContentPane(backdrop);
        customDialog.setVisible(true);
    }


    public boolean addOrUpdateItem(String cartId, String itemId, String itemName, int quantityToAdd) {
        try (Connection con = DBConnect.getConnection()) {
            
            String checkStockSql = "SELECT is_available FROM Item WHERE item_id = ?";
            try (PreparedStatement psStock = con.prepareStatement(checkStockSql)) {
                psStock.setString(1, itemId);
                ResultSet rsStock = psStock.executeQuery();
                if (rsStock.next()) {
                    int currentStock = rsStock.getInt("is_available");
                    if (currentStock < quantityToAdd) {
                        
                        if (currentStock == 0) {
                            showOutOfStockPopup("Món <b>" + itemName + "</b> đã hết khẩu phần!");
                        } else {
                            showOutOfStockPopup("Món <b>" + itemName + "</b> chỉ còn " + currentStock + " phần!");
                        }
                        return false; 
                    }
                } else {
                    return false;
                }
            }
            
            String deductStockSql = "UPDATE Item SET is_available = is_available - ? WHERE item_id = ?";
            try (PreparedStatement psDeduct = con.prepareStatement(deductStockSql)) {
                psDeduct.setInt(1, quantityToAdd);
                psDeduct.setString(2, itemId);
                psDeduct.executeUpdate(); 
            }

            String checkSql = "SELECT quantity FROM CartItem WHERE cart_id = ? AND item_id = ?";
            try (PreparedStatement checkPs = con.prepareStatement(checkSql)) {
                checkPs.setString(1, cartId);
                checkPs.setString(2, itemId);
                ResultSet rs = checkPs.executeQuery();
                
                if (rs.next()) {
                    int currentQty = rs.getInt("quantity");
                    String updateSql = "UPDATE CartItem SET quantity = ? WHERE cart_id = ? AND item_id = ?";
                    try (PreparedStatement updatePs = con.prepareStatement(updateSql)) {
                        updatePs.setInt(1, currentQty + quantityToAdd);
                        updatePs.setString(2, cartId);
                        updatePs.setString(3, itemId);
                        return updatePs.executeUpdate() > 0;
                    }
                } else {
                    String cartItemId = "CI" + (new Random().nextInt(90000) + 10000);
                    String insertSql = "INSERT INTO CartItem (cartitem_id, cart_id, item_id, name, quantity, note) VALUES (?, ?, ?, ?, ?, '')";
                    try (PreparedStatement insertPs = con.prepareStatement(insertSql)) {
                        insertPs.setString(1, cartItemId);
                        insertPs.setString(2, cartId);
                        insertPs.setString(3, itemId);
                        insertPs.setString(4, itemName);
                        insertPs.setInt(5, quantityToAdd);
                        return insertPs.executeUpdate() > 0;
                    }
                }
            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<CartItem> getCartItems(String cartId) {
        List<CartItem> list = new ArrayList<>();
        String sql = "SELECT ci.cart_id, ci.item_id, ci.name, i.img_url, ci.quantity, i.price, i.is_available " +
                     "FROM CartItem ci " +
                     "JOIN Item i ON ci.item_id = i.item_id " +
                     "WHERE ci.cart_id = ?";
                     
        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cartId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                CartItem item = new CartItem();
                item.setCartId(rs.getString("cart_id"));
                item.setItemId(rs.getString("item_id"));
                item.setItemName(rs.getString("name"));     
                item.setImgUrl(rs.getString("img_url"));    
                item.setQuantity(rs.getInt("quantity"));
                item.setPrice(rs.getDouble("price")); 
                item.setMaxStock(rs.getInt("is_available")); 
                list.add(item);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public boolean removeItem(String cartId, String itemId) {
        try (Connection con = DBConnect.getConnection()) {
            int qtyInCart = 0;
            String getQtySql = "SELECT quantity FROM CartItem WHERE cart_id = ? AND item_id = ?";
            try (PreparedStatement psQty = con.prepareStatement(getQtySql)) {
                psQty.setString(1, cartId);
                psQty.setString(2, itemId);
                ResultSet rs = psQty.executeQuery();
                if (rs.next()) qtyInCart = rs.getInt("quantity");
            }

            String sql = "DELETE FROM CartItem WHERE cart_id = ? AND item_id = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, cartId);
                ps.setString(2, itemId);
                if (ps.executeUpdate() > 0) {
                    String restoreStock = "UPDATE Item SET is_available = is_available + ? WHERE item_id = ?";
                    try (PreparedStatement psRestore = con.prepareStatement(restoreStock)) {
                        psRestore.setInt(1, qtyInCart);
                        psRestore.setString(2, itemId);
                        psRestore.executeUpdate();
                    }
                    return true;
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }
    
    public boolean updateQuantity(String cartId, String itemId, int change) {
        try (Connection con = DBConnect.getConnection()) {
            if (change > 0) {
                String checkStock = "SELECT i.is_available, ci.name FROM Item i JOIN CartItem ci ON i.item_id = ci.item_id WHERE ci.cart_id = ? AND ci.item_id = ?";
                try (PreparedStatement psStock = con.prepareStatement(checkStock)) {
                    psStock.setString(1, cartId);
                    psStock.setString(2, itemId);
                    ResultSet rs = psStock.executeQuery();
                    if (rs.next() && rs.getInt("is_available") < change) {
                        
                        showOutOfStockPopup("Món <b>" + rs.getString("name") + "</b> đã hết khẩu phần!");
                        return false; 
                    }
                }
            }

            String sql = "UPDATE CartItem SET quantity = quantity + ? WHERE cart_id = ? AND item_id = ? AND (quantity + ?) > 0";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, change);
                ps.setString(2, cartId);
                ps.setString(3, itemId);
                ps.setInt(4, change);
                int affected = ps.executeUpdate();
                
                if (affected > 0) {
                    String updateStock = "UPDATE Item SET is_available = is_available - ? WHERE item_id = ?";
                    try (PreparedStatement psUpdate = con.prepareStatement(updateStock)) {
                        psUpdate.setInt(1, change);
                        psUpdate.setString(2, itemId);
                        psUpdate.executeUpdate();
                    }
                    return true;
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }
}