package View;

import Controller.CartItemController; 
import Model.CartItem;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class CardCartItem extends JPanel {

    private CartItem item;
    private CartItemController controller; 

    public CardCartItem(CartItem item, CartItemController controller) {
        this.item = item;
        this.controller = controller;

        this.setLayout(new BorderLayout()); 
        this.setOpaque(false);
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); 
        
        this.setPreferredSize(new Dimension(700, 135));
        this.setMaximumSize(new Dimension(700, 135));
        this.setMinimumSize(new Dimension(700, 135)); 
        this.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel pnlImg = new JPanel(new BorderLayout());
        pnlImg.setOpaque(false);
        pnlImg.setPreferredSize(new Dimension(150, 100)); 
        
        JPanel pnlImgDraw = new JPanel() {
            Image imgOriginal = new ImageIcon("D:/DoAnPTTKTHTTT/SelfRestaurant/src/main/java/images/" + item.getImgUrl()).getImage();
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int w = getWidth(); int h = getHeight();

                if (imgOriginal != null && imgOriginal.getWidth(null) > 0) {
                    java.awt.image.BufferedImage maskImage = new java.awt.image.BufferedImage(w, h, java.awt.image.BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g2d = maskImage.createGraphics();
                    
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    g2d.setColor(Color.WHITE);
                    g2d.fillRoundRect(0, 0, w, h, 12, 12);

                    g2d.setComposite(java.awt.AlphaComposite.SrcIn);
            
                    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                    double scale = Math.max((double) w / imgOriginal.getWidth(null), (double) h / imgOriginal.getHeight(null)); 
                    int scaledW = (int) (imgOriginal.getWidth(null) * scale);
                    int scaledH = (int) (imgOriginal.getHeight(null) * scale);
                    
                    g2d.drawImage(imgOriginal, (w - scaledW) / 2, (h - scaledH) / 2, scaledW, scaledH, null);
                    g2d.dispose();

                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                    g2.drawImage(maskImage, 0, 0, null);

                }
            }
        };
        pnlImgDraw.setOpaque(false);
        pnlImg.add(pnlImgDraw, BorderLayout.CENTER);
        this.add(pnlImg, BorderLayout.WEST); 

        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setOpaque(false);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 0)); 
        
        JLabel nameLabel = new JLabel(item.getItemName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        nameLabel.setVerticalAlignment(SwingConstants.TOP);
        infoPanel.add(nameLabel, BorderLayout.NORTH);

        JPanel pricePanel = new JPanel(new GridLayout(2, 1));
        pricePanel.setOpaque(false);
        
        JLabel unitPrice = new JLabel(String.format("Đơn giá: %,.0f đ", item.getPrice()));
        unitPrice.setFont(new Font("Arial", Font.PLAIN, 13));
        unitPrice.setForeground(Color.GRAY);
        
        JLabel totalValue = new JLabel(String.format("Tổng: %,.0f đ", item.getTotalPrice()));
        totalValue.setFont(new Font("Arial", Font.BOLD, 16));
        totalValue.setForeground(new Color(220, 53, 69)); 
        
        pricePanel.add(unitPrice);
        pricePanel.add(totalValue);
        
        infoPanel.add(pricePanel, BorderLayout.SOUTH);
        this.add(infoPanel, BorderLayout.CENTER); 


        JPanel actionPanel = new JPanel(new BorderLayout());
        actionPanel.setOpaque(false);
        actionPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JPanel topActionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        topActionPanel.setOpaque(false);

        JButton btnMinus = new JButton("-");
        btnMinus.setPreferredSize(new Dimension(40, 28)); 
        btnMinus.setFont(new Font("Arial", Font.BOLD, 16)); 
        btnMinus.setMargin(new java.awt.Insets(0, 0, 0, 0)); 
        btnMinus.setFocusPainted(false);
        btnMinus.addActionListener(e -> controller.decreaseQuantity(item.getItemId()));
        
        JLabel lblQtyVal = new JLabel(String.valueOf(item.getQuantity()), SwingConstants.CENTER);
        lblQtyVal.setFont(new Font("Arial", Font.BOLD, 15));
        lblQtyVal.setPreferredSize(new Dimension(30, 28)); 
        
        JButton btnPlus = new JButton("+");
        btnPlus.setPreferredSize(new Dimension(40, 28)); 
        btnPlus.setFont(new Font("Arial", Font.BOLD, 16)); 
        btnPlus.setMargin(new java.awt.Insets(0, 0, 0, 0)); 
        btnPlus.setFocusPainted(false);
        
        if (item.getQuantity() >= item.getMaxStock()) {
            btnPlus.setEnabled(false);
            btnPlus.setForeground(Color.LIGHT_GRAY); 
            btnPlus.setToolTipText("Món này chỉ còn " + item.getMaxStock() + " phần!"); 
        } else {
            btnPlus.addActionListener(e -> controller.increaseQuantity(item.getItemId()));
        }
        
        JButton btnDelete = new JButton("X");
        btnDelete.setBackground(new Color(220, 53, 69)); 
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setPreferredSize(new Dimension(40, 28)); 
        btnDelete.setFont(new Font("Arial", Font.BOLD, 14));
        btnDelete.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnDelete.setFocusPainted(false);
        btnDelete.setBorder(BorderFactory.createEmptyBorder()); 
        btnDelete.addActionListener(e -> controller.removeItem(item.getItemId()));
        
        topActionPanel.add(btnMinus);
        topActionPanel.add(lblQtyVal);
        topActionPanel.add(btnPlus);
        topActionPanel.add(Box.createHorizontalStrut(10)); 
        topActionPanel.add(btnDelete);

        actionPanel.add(topActionPanel, BorderLayout.NORTH);

        JPanel bottomActionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        bottomActionPanel.setOpaque(false);
        
        JButton btnNote = new JButton("Ghi chú");
        btnNote.setPreferredSize(new Dimension(80, 26));
        btnNote.setFont(new Font("Arial", Font.PLAIN, 12));
        btnNote.setFocusPainted(false);
        btnNote.addActionListener(e -> {
            java.awt.Window window = javax.swing.SwingUtilities.getWindowAncestor(this);
            java.awt.Frame parentFrame = (window instanceof java.awt.Frame) ? (java.awt.Frame) window : null;
            VirtualKeyboardDialog vk = new VirtualKeyboardDialog(parentFrame, "Nhập ghi chú (VD: Không cay, ít đá...)", "");
            vk.setVisible(true); 
            String note = vk.getResult();
            if (note != null && !note.trim().isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(this, "Ghi chú cho món [" + item.getItemName() + "]: " + note);
            }
        });
        
        bottomActionPanel.add(btnNote);
        actionPanel.add(bottomActionPanel, BorderLayout.SOUTH); 
        this.add(actionPanel, BorderLayout.EAST); 
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
        
        g2.setColor(new Color(255, 102, 51));
        g2.setStroke(new BasicStroke(2.0f));
        g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 16, 16);
        
        g2.dispose();
    }
}