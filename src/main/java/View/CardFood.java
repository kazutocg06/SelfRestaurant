package View;

import Controller.CartItemController;
import Model.Item;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class CardFood extends JPanel {
    
    private Item item;
    private CartItemController cartItemController; 

    public CardFood(Item item, CartItemController cartItemController) {
        this.item = item;
        this.cartItemController = cartItemController;
        
        this.setOpaque(false); 
        this.setPreferredSize(new Dimension(220, 310)); 
        this.setLayout(new BorderLayout());
        this.setBackground(Color.WHITE);

        JPanel pnlImageDynamic = new JPanel() {
            private Image originalImg = new ImageIcon("D:/DoAnPTTKTHTTT/SelfRestaurant/src/main/java/images/" + item.getImgUrl()).getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (originalImg != null) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

                    int targetW = getWidth();
                    int targetH = getHeight();

                    double scaleX = (double) targetW / originalImg.getWidth(null);
                    double scaleY = (double) targetH / originalImg.getHeight(null);
                    double scale = Math.max(scaleX, scaleY); 

                    int scaledW = (int) (originalImg.getWidth(null) * scale);
                    int scaledH = (int) (originalImg.getHeight(null) * scale);

                    int x = (targetW - scaledW) / 2;
                    int y = (targetH - scaledH) / 2;

                    java.awt.geom.Area clipShape = new java.awt.geom.Area(
                        new java.awt.geom.RoundRectangle2D.Float(2, 2, targetW - 4, targetH, 14, 14)
                    );
                    
                    clipShape.add(new java.awt.geom.Area(
                        new java.awt.geom.Rectangle2D.Float(2, targetH / 2.0f, targetW - 4, targetH / 2.0f)
                    ));

                    g2d.clip(clipShape);

                    g2d.drawImage(originalImg, x, y, scaledW, scaledH, null);
                    g2d.dispose(); 
                }
            }
        };
        pnlImageDynamic.setPreferredSize(new Dimension(220, 160)); 
        pnlImageDynamic.setOpaque(false);

        JPanel panelContent = new JPanel();
        panelContent.setOpaque(false); 
        panelContent.setLayout(new GridLayout(4, 1, 0, 5)); 
        panelContent.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); 
        
        JLabel lblName = new JLabel(item.getName());
        lblName.setFont(new Font("Arial", Font.BOLD, 14));
        
        String desc = item.getDescription() != null ? item.getDescription() : "";
        JLabel lblDesc = new JLabel(desc);
        lblDesc.setFont(new Font("Arial", Font.PLAIN, 12));
        lblDesc.setForeground(Color.GRAY);
        
        JLabel lblPrice = new JLabel(String.format("%,.0f VNĐ", item.getPrice()));
        lblPrice.setFont(new Font("Arial", Font.BOLD, 14));
        lblPrice.setForeground(new Color(220, 53, 69)); 
        
        JButton btnAdd = new JButton("Thêm vào giỏ");
        btnAdd.setBackground(new Color(255, 102, 51)); 
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFocusPainted(false); 
        btnAdd.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if (cartItemController != null) {
                    cartItemController.addToCart(item.getItemId(), item.getName(), item.getPrice());
                }
            }
        });
        
        panelContent.add(lblName);
        panelContent.add(lblDesc);
        panelContent.add(lblPrice);
        panelContent.add(btnAdd);

        this.add(pnlImageDynamic, BorderLayout.NORTH); 
        this.add(panelContent, BorderLayout.CENTER);   
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
        
        g2.dispose();
    }

    @Override
    protected void paintChildren(Graphics g) {
        super.paintChildren(g); 
        
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2.setColor(new Color(255, 102, 51)); 
        g2.setStroke(new java.awt.BasicStroke(2.0f)); 
        
        g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 16, 16);
        
        g2.dispose();
    }
}