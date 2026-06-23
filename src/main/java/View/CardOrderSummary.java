package View;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

public class CardOrderSummary extends JPanel {
    
    private JLabel lblOrderId;
    private JLabel lblTotalUniqueItems;
    private JLabel lblTotalQuantity;
    private JLabel lblGrandTotal;

    public CardOrderSummary() {
        this.setOpaque(false);
        this.setLayout(new BorderLayout()); 
        
        this.setPreferredSize(new Dimension(340, 400));

        JPanel innerCard = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
       
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                
                g2.setColor(new Color(210, 215, 220));
                g2.setStroke(new BasicStroke(2.0f));
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 15, 15);
                
                g2.dispose();
            }
        };
        
        innerCard.setLayout(new BoxLayout(innerCard, BoxLayout.Y_AXIS));
        innerCard.setOpaque(false);
        
        innerCard.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        Font fontTitle = new Font("Arial", Font.BOLD, 18);
        Font fontNormal = new Font("Arial", Font.PLAIN, 16);
        Font fontValue = new Font("Arial", Font.BOLD, 16);

        JPanel pnlTop = new JPanel(new BorderLayout());
        pnlTop.setOpaque(false);
        pnlTop.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30)); 
        
        JLabel lblTitleOrder = new JLabel("Mã giỏ:");
        lblTitleOrder.setFont(fontTitle);
        lblOrderId = new JLabel("#---");
        lblOrderId.setFont(fontTitle);
        lblOrderId.setForeground(Color.GRAY);
        pnlTop.add(lblTitleOrder, BorderLayout.WEST);
        pnlTop.add(lblOrderId, BorderLayout.EAST);
        
        innerCard.add(pnlTop);
        innerCard.add(Box.createVerticalStrut(15));
        
        JSeparator sep1 = new JSeparator();
        sep1.setForeground(new Color(200, 200, 200));
        sep1.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        innerCard.add(sep1);
        innerCard.add(Box.createVerticalStrut(15));

        JPanel pnlMiddle = new JPanel(new GridLayout(2, 1, 0, 15));
        pnlMiddle.setOpaque(false);
        pnlMiddle.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60)); 

        JPanel pnlUniqueItems = new JPanel(new BorderLayout());
        pnlUniqueItems.setOpaque(false);
        JLabel lblTitleUnique = new JLabel("Tổng số món:");
        lblTitleUnique.setFont(fontNormal);
        lblTotalUniqueItems = new JLabel("0");
        lblTotalUniqueItems.setFont(fontValue);
        pnlUniqueItems.add(lblTitleUnique, BorderLayout.WEST);
        pnlUniqueItems.add(lblTotalUniqueItems, BorderLayout.EAST);
        pnlMiddle.add(pnlUniqueItems);

        JPanel pnlQuantity = new JPanel(new BorderLayout());
        pnlQuantity.setOpaque(false);
        JLabel lblTitleQty = new JLabel("Tổng số lượng:");
        lblTitleQty.setFont(fontNormal);
        lblTotalQuantity = new JLabel("0");
        lblTotalQuantity.setFont(fontValue);
        pnlQuantity.add(lblTitleQty, BorderLayout.WEST);
        pnlQuantity.add(lblTotalQuantity, BorderLayout.EAST);
        pnlMiddle.add(pnlQuantity);
        
        innerCard.add(pnlMiddle);
        innerCard.add(Box.createVerticalStrut(15));
        
        JSeparator sep2 = new JSeparator();
        sep2.setForeground(new Color(200, 200, 200));
        sep2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        innerCard.add(sep2);
        
        innerCard.add(Box.createVerticalGlue());

        JPanel pnlBottom = new JPanel(new BorderLayout());
        pnlBottom.setOpaque(false);
        pnlBottom.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        
        JLabel lblTitleTotal = new JLabel("Tổng:");
        lblTitleTotal.setFont(new Font("Arial", Font.BOLD, 18));
        
        lblGrandTotal = new JLabel("0 VNĐ");
        lblGrandTotal.setFont(new Font("Arial", Font.BOLD, 22)); 
        lblGrandTotal.setForeground(new Color(220, 53, 69)); 
        
        pnlBottom.add(lblTitleTotal, BorderLayout.WEST);
        pnlBottom.add(lblGrandTotal, BorderLayout.EAST); 
        
        innerCard.add(pnlBottom);

        this.add(innerCard, BorderLayout.CENTER);
    }

    public JLabel getLblOrderId() { return lblOrderId; }
    public JLabel getLblTotalUniqueItems() { return lblTotalUniqueItems; }
    public JLabel getLblTotalQuantity() { return lblTotalQuantity; }
    public JLabel getLblGrandTotal() { return lblGrandTotal; }
}