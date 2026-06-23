package View;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.*;

public class CardReceipt extends JPanel {

    public static class ReceiptItem {
        public String name;
        public int quantity;
        public double price;

        public ReceiptItem(String name, int quantity, double price) {
            this.name = name;
            this.quantity = quantity;
            this.price = price;
        }
    }

    public CardReceipt(String receiptId, String orderId, String time, List<ReceiptItem> items, double totalAmount, String status) {
        this.setOpaque(false);
        this.setLayout(new BorderLayout());
        
        this.setPreferredSize(new Dimension(510, 250));
        this.setMaximumSize(new Dimension(510, 250));
        this.setMinimumSize(new Dimension(510, 250));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String safeStatus = (status != null) ? status.trim().toUpperCase() : "UNPAID";
        Color borderStatusColor;
        if (safeStatus.equals("PAID") || safeStatus.equals("ĐÃ THANH TOÁN")) {
            safeStatus = "PAID";
            borderStatusColor = new Color(40, 167, 69); 
        } else {
            safeStatus = "UNPAID";
            borderStatusColor = new Color(140, 146, 153); 
        }

        JPanel innerCard = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24); 
                
                g2.setColor(borderStatusColor);
                g2.setStroke(new BasicStroke(2.0f)); 
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 24, 24);
                
                g2.dispose();
            }
        };
        innerCard.setOpaque(false);
        innerCard.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setOpaque(false);
        pnlHeader.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));

        JPanel pnlTitle = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlTitle.setOpaque(false);
        JLabel lblId = new JLabel("ĐƠN " + receiptId + "  ");
        lblId.setFont(new Font("Arial", Font.BOLD, 18));
        JLabel lblTime = new JLabel("(" + time + ")");
        lblTime.setFont(new Font("Arial", Font.ITALIC, 13));
        lblTime.setForeground(Color.GRAY);
        pnlTitle.add(lblId);
        pnlTitle.add(lblTime);

        JLabel lblStatusTag = new JLabel(safeStatus, SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        lblStatusTag.setOpaque(false);
        lblStatusTag.setFont(new Font("Arial", Font.BOLD, 12));
        lblStatusTag.setForeground(Color.WHITE);
        lblStatusTag.setBackground(borderStatusColor); 
        lblStatusTag.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));

        pnlHeader.add(pnlTitle, BorderLayout.WEST);
        pnlHeader.add(lblStatusTag, BorderLayout.EAST);
        innerCard.add(pnlHeader, BorderLayout.NORTH);
        JPanel pnlItemsContainer = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(248, 249, 250)); 
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.setColor(new Color(235, 238, 240));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
                g2.dispose();
            }
        };
        pnlItemsContainer.setLayout(new BoxLayout(pnlItemsContainer, BoxLayout.Y_AXIS));
        pnlItemsContainer.setOpaque(false);
        pnlItemsContainer.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        int maxShow = 3; 
        int limit = Math.min(items.size(), maxShow);

        for (int i = 0; i < limit; i++) {
            ReceiptItem item = items.get(i);
            JPanel rowItem = new JPanel(new BorderLayout());
            rowItem.setOpaque(false);
            
            if (i < limit - 1 || items.size() > maxShow) {
                rowItem.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 235, 240)), 
                    BorderFactory.createEmptyBorder(8, 0, 8, 0)
                ));
            } else {
                rowItem.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0)); 
            }

            JLabel lblItemInfo = new JLabel(item.quantity + "x   " + item.name);
            lblItemInfo.setFont(new Font("Arial", Font.PLAIN, 15));
            rowItem.add(lblItemInfo, BorderLayout.WEST);

            pnlItemsContainer.add(rowItem);
        }

        if (items.size() > maxShow) {
            JPanel rowMore = new JPanel(new BorderLayout());
            rowMore.setOpaque(false);
            rowMore.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
            JLabel lblMore = new JLabel("... và " + (items.size() - maxShow) + " món khác");
            lblMore.setFont(new Font("Arial", Font.ITALIC, 13));
            lblMore.setForeground(Color.GRAY);
            rowMore.add(lblMore, BorderLayout.WEST);
            pnlItemsContainer.add(rowMore);
        }

        JScrollPane scrollItems = new JScrollPane(pnlItemsContainer);
        scrollItems.setPreferredSize(new Dimension(10, 125)); 
        scrollItems.setOpaque(false);
        scrollItems.getViewport().setOpaque(false);
        scrollItems.setBorder(BorderFactory.createEmptyBorder()); 
        scrollItems.setViewportBorder(BorderFactory.createEmptyBorder()); 
        scrollItems.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scrollItems.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        
        MouseAdapter firewallAdapter = new MouseAdapter() {
            private int startY;
            
            @Override
            public void mousePressed(MouseEvent e) {
                startY = e.getYOnScreen(); 
                
                JScrollPane outerScroll = (JScrollPane) SwingUtilities.getAncestorOfClass(JScrollPane.class, CardReceipt.this);
                if (outerScroll != null) {
                    Component view = outerScroll.getViewport().getView();
                    if (view != null) {
                        MouseEvent fakeRelease = new MouseEvent(
                            view, MouseEvent.MOUSE_RELEASED, 
                            e.getWhen(), 0, e.getX(), e.getY(), 0, false
                        );
                        view.dispatchEvent(fakeRelease);
                    }
                }
            }
            
            @Override
            public void mouseDragged(MouseEvent e) {
                JViewport viewPort = scrollItems.getViewport();
                if (viewPort != null && pnlItemsContainer.getHeight() > viewPort.getHeight()) {
                    int deltaY = startY - e.getYOnScreen();
                    Point viewPos = viewPort.getViewPosition();
                    int newY = viewPos.y + deltaY;
                    
                    int maxY = pnlItemsContainer.getHeight() - viewPort.getHeight();
                    if (newY < 0) newY = 0;
                    if (newY > maxY) newY = maxY;
                    
                    viewPort.setViewPosition(new Point(viewPos.x, newY));
                    startY = e.getYOnScreen(); 
                }
            }
        };
        
        pnlItemsContainer.addMouseListener(firewallAdapter);
        pnlItemsContainer.addMouseMotionListener(firewallAdapter);
        scrollItems.getViewport().addMouseListener(firewallAdapter);
        scrollItems.getViewport().addMouseMotionListener(firewallAdapter);

        for (Component comp : pnlItemsContainer.getComponents()) {
            comp.addMouseListener(firewallAdapter);
            comp.addMouseMotionListener(firewallAdapter);
        }

        innerCard.add(scrollItems, BorderLayout.CENTER);
        JPanel pnlFooter = new JPanel(new BorderLayout());
        pnlFooter.setOpaque(false);
        pnlFooter.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        JLabel lblTotal = new JLabel(String.format("Tổng: %,.0f đ", totalAmount)); 
        lblTotal.setFont(new Font("Arial", Font.BOLD, 16));
        pnlFooter.add(lblTotal, BorderLayout.WEST);

        JButton btnDetail = new JButton("XEM CHI TIẾT"); 
        btnDetail.setFont(new Font("Arial", Font.BOLD, 12));
        btnDetail.setForeground(Color.WHITE);
        btnDetail.setBackground(new Color(255, 102, 51)); 
        btnDetail.setFocusPainted(false);
        btnDetail.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        btnDetail.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        final String finalSafeStatus = safeStatus;
        btnDetail.addActionListener(e -> {
            JPanel pnlBill = new JPanel();
            pnlBill.setLayout(new BoxLayout(pnlBill, BoxLayout.Y_AXIS));
            pnlBill.setOpaque(false); 
            pnlBill.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

            JLabel lblShopName = new JLabel("KIOSK TỰ PHỤC VỤ");
            lblShopName.setFont(new Font("Arial", Font.BOLD, 22));
            lblShopName.setForeground(new Color(255, 102, 51)); 
            lblShopName.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel lblBillTitle = new JLabel("HÓA ĐƠN THANH TOÁN");
            lblBillTitle.setFont(new Font("Arial", Font.BOLD, 16));
            lblBillTitle.setForeground(new Color(50, 50, 50));
            lblBillTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel lblBillId = new JLabel("Mã Đơn: #" + receiptId);
            lblBillId.setFont(new Font("Arial", Font.PLAIN, 14));
            lblBillId.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel lblDate = new JLabel("Ngày: " + time);
            lblDate.setFont(new Font("Arial", Font.PLAIN, 14));
            lblDate.setAlignmentX(Component.CENTER_ALIGNMENT);

            pnlBill.add(lblShopName);
            pnlBill.add(Box.createVerticalStrut(8));
            pnlBill.add(lblBillTitle);
            pnlBill.add(Box.createVerticalStrut(12));
            pnlBill.add(lblBillId);
            pnlBill.add(lblDate);
            pnlBill.add(Box.createVerticalStrut(15));

            JSeparator sep1 = new JSeparator();
            sep1.setForeground(new Color(220, 220, 220));
            pnlBill.add(sep1);
            pnlBill.add(Box.createVerticalStrut(15));

            JPanel pnlItems = new JPanel(new GridLayout(0, 1, 0, 10));
            pnlItems.setOpaque(false); 
            for (ReceiptItem it : items) {
                JPanel row = new JPanel(new BorderLayout());
                row.setOpaque(false);

                JLabel lblName = new JLabel(it.quantity + "x   " + it.name);
                lblName.setFont(new Font("Arial", Font.PLAIN, 15));
                lblName.setForeground(new Color(40, 40, 40));

                JLabel lblPrice = new JLabel(String.format("%,.0f đ", it.price * it.quantity));
                lblPrice.setFont(new Font("Arial", Font.BOLD, 15));
                lblPrice.setForeground(new Color(80, 80, 80));

                row.add(lblName, BorderLayout.WEST);
                row.add(lblPrice, BorderLayout.EAST);
                pnlItems.add(row);
            }
            pnlBill.add(pnlItems);
            pnlBill.add(Box.createVerticalStrut(15));

            JSeparator sep2 = new JSeparator();
            sep2.setForeground(new Color(220, 220, 220));
            pnlBill.add(sep2);
            pnlBill.add(Box.createVerticalStrut(15));

            JPanel pnlTotal = new JPanel(new BorderLayout());
            pnlTotal.setOpaque(false); 

            JLabel lblTotalText = new JLabel("TỔNG CỘNG:");
            lblTotalText.setFont(new Font("Arial", Font.BOLD, 16));

            JLabel lblTotalValue = new JLabel(String.format("%,.0f VNĐ", totalAmount));
            lblTotalValue.setFont(new Font("Arial", Font.BOLD, 22));
            lblTotalValue.setForeground(new Color(220, 53, 69)); 

            pnlTotal.add(lblTotalText, BorderLayout.WEST);
            pnlTotal.add(lblTotalValue, BorderLayout.EAST);

            pnlBill.add(pnlTotal);
            pnlBill.add(Box.createVerticalStrut(20));

            JLabel lblStatus = new JLabel("TRẠNG THÁI: " + finalSafeStatus);
            lblStatus.setFont(new Font("Arial", Font.BOLD, 16));
            lblStatus.setForeground(finalSafeStatus.equals("PAID") ? new Color(40, 167, 69) : new Color(140, 146, 153));
            lblStatus.setAlignmentX(Component.CENTER_ALIGNMENT);
            pnlBill.add(lblStatus);

            java.awt.Window window = javax.swing.SwingUtilities.getWindowAncestor(this);
            JDialog customDialog = new JDialog((Frame) window, true);
            customDialog.setUndecorated(true); 
            
            customDialog.setSize(window.getSize());
            customDialog.setLocationRelativeTo(window);
            customDialog.setBackground(new Color(0, 0, 0, 120)); 
            
            JPanel backdrop = new JPanel(new GridBagLayout());
            backdrop.setOpaque(false);
            
            backdrop.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mousePressed(java.awt.event.MouseEvent e) {
                    customDialog.dispose();
                }
            });

            JPanel wrapper = new JPanel(new BorderLayout()) {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    g2.setColor(Color.WHITE); 
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
                    
                    g2.setColor(new Color(255, 102, 51)); 
                    g2.setStroke(new BasicStroke(3.0f));
                    g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 24, 24);
                    
                    g2.dispose();
                }
            };
            wrapper.setOpaque(false);
            wrapper.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            wrapper.setPreferredSize(new Dimension(420, 520)); 
            
            wrapper.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mousePressed(java.awt.event.MouseEvent e) {
                    e.consume(); 
                }
            });

            JScrollPane scrollDialog = new JScrollPane(pnlBill);
            scrollDialog.setBorder(null);
            scrollDialog.setOpaque(false);
            scrollDialog.getViewport().setOpaque(false);
            
            scrollDialog.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
            scrollDialog.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            scrollDialog.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE); 
            
            Controller.KineticScroller.setup(scrollDialog);

            wrapper.add(scrollDialog, BorderLayout.CENTER);
            
            backdrop.add(wrapper);

            customDialog.setContentPane(backdrop);
            customDialog.setVisible(true);
        });
        
        pnlFooter.add(btnDetail, BorderLayout.EAST);
        innerCard.add(pnlFooter, BorderLayout.SOUTH);
        
        this.add(innerCard, BorderLayout.CENTER);
    }
}