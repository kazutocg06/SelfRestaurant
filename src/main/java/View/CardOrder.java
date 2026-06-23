package View;

import DAO.OrderDetailsDAO;
import Model.OrderDetails;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.*;

public class CardOrder extends JPanel {

    public CardOrder(String orderId, String time, List<OrderDetails> items, double ignoredTotal, String status, Controller.OrderController controller) {
        this.setOpaque(false);
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(510, 250));
        this.setMaximumSize(new Dimension(510, 250));
        this.setMinimumSize(new Dimension(510, 250));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        double realTotal = 0;
        for (OrderDetails item : items) {
            String itStatus = (item.getItemStatus() != null) ? item.getItemStatus().trim().toLowerCase() : "";
            if (!itStatus.equals("canceled") && !itStatus.equals("đã hủy")) {
                realTotal += (item.getQuantity() * item.getPriceAtOrder());
            }
        }

        String safeStatus = (status != null) ? status.trim().toLowerCase() : "";
        String displayStatus = (status != null) ? status.trim().toUpperCase() : ""; 
        Color statusColor = Color.GRAY;
        boolean isOrderLocked = false; 

        switch (safeStatus) {
            case "pending": case "chuẩn bị": case "chu?n b?":
                displayStatus = "PENDING";
                statusColor = new Color(140, 146, 153); 
                break;
            case "cooking": case "đang nấu":
                displayStatus = "COOKING";
                statusColor = new Color(253, 126, 20); 
                break;
            case "done": case "hoàn thành": case "completed": case "finish":
                displayStatus = "FINISH";
                statusColor = new Color(143, 207, 127); 
                isOrderLocked = true; 
                break;
            case "canceled": case "đã hủy":
                displayStatus = "CANCELED";
                statusColor = new Color(220, 53, 69); 
                isOrderLocked = true; 
                break;
        }

        boolean canCancelWholeOrder = !isOrderLocked;
        if (canCancelWholeOrder) {
            for (OrderDetails item : items) {
                String itStatus = (item.getItemStatus() != null) ? item.getItemStatus().trim().toLowerCase() : "";
                if (!itStatus.equals("pending") && !itStatus.equals("chuẩn bị") && !itStatus.equals("chu?n b?") && 
                    !itStatus.equals("canceled") && !itStatus.equals("đã hủy")) {
                    canCancelWholeOrder = false;
                    break;
                }
            }
        }

        final Color finalBorderColor = statusColor;

        JPanel innerCard = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24); 
                
                g2.setColor(finalBorderColor);
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
        JLabel lblOrderId = new JLabel("ĐƠN " + orderId + "  ");
        lblOrderId.setFont(new Font("Arial", Font.BOLD, 18));
        JLabel lblTime = new JLabel("(" + time + ")");
        lblTime.setFont(new Font("Arial", Font.ITALIC, 13));
        lblTime.setForeground(Color.GRAY);
        pnlTitle.add(lblOrderId);
        pnlTitle.add(lblTime);

        JLabel lblStatusTag = new JLabel(displayStatus, SwingConstants.CENTER) {
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
        lblStatusTag.setBackground(statusColor); 
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

        for (int i = 0; i < items.size(); i++) {
            OrderDetails item = items.get(i);
            JPanel rowItem = new JPanel(new BorderLayout());
            rowItem.setOpaque(false);
            
            if (i < items.size() - 1) {
                rowItem.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 235, 240)), 
                    BorderFactory.createEmptyBorder(8, 0, 8, 0)
                ));
            } else {
                rowItem.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0)); 
            }

            String itStatus = (item.getItemStatus() != null) ? item.getItemStatus().trim().toLowerCase() : "";
            boolean isItemCanceled = itStatus.equals("canceled") || itStatus.equals("đã hủy");
            boolean isItemCooking = itStatus.equals("cooking") || itStatus.equals("đang nấu");
            boolean isItemFinish = itStatus.equals("finish") || itStatus.equals("hoàn thành") || itStatus.equals("completed");
            boolean isItemPending = itStatus.equals("pending") || itStatus.equals("chuẩn bị") || itStatus.equals("chu?n b?");

            String itemNameDisplay = item.getQuantity() + "x   " + item.getItemName();
            JLabel lblItemInfo = new JLabel();
            lblItemInfo.setFont(new Font("Arial", Font.PLAIN, 15));

            if (isItemCanceled) {
                itemNameDisplay += " (Đã hủy)"; 
                lblItemInfo.setForeground(Color.GRAY);
            } else if (isItemCooking) {
                itemNameDisplay += " (Đang nấu...)"; 
                lblItemInfo.setForeground(new Color(253, 126, 20)); 
            } else if (isItemFinish) {
                itemNameDisplay += " (Hoàn thành)"; 
                lblItemInfo.setForeground(new Color(143, 207, 127)); 
            }

            lblItemInfo.setText(itemNameDisplay);
            rowItem.add(lblItemInfo, BorderLayout.WEST);

            if (!isOrderLocked && !isItemCanceled && isItemPending) {
                JButton btnCancelItem = new JButton("[Hủy món]"); 
                btnCancelItem.setFont(new Font("Arial", Font.PLAIN, 12));
                btnCancelItem.setForeground(new Color(220, 53, 69));
                btnCancelItem.setContentAreaFilled(false);
                btnCancelItem.setBorderPainted(false);
                btnCancelItem.setFocusPainted(false);
                btnCancelItem.setCursor(new Cursor(Cursor.HAND_CURSOR));
                
                btnCancelItem.addActionListener(e -> {
                    int choice = JOptionPane.showConfirmDialog(this, "Xác nhận hủy món: " + item.getItemName() + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                    if (choice == JOptionPane.YES_OPTION) {
                        OrderDetailsDAO dao = new OrderDetailsDAO();
                        if (dao.cancelSingleItem(item.getOrderDetailId())) {
                            if (controller != null) controller.loadOrdersToUI();
                        }
                    }
                });
                rowItem.add(btnCancelItem, BorderLayout.EAST);
            }
            pnlItemsContainer.add(rowItem);
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

                JScrollPane outerScroll = (JScrollPane) SwingUtilities.getAncestorOfClass(JScrollPane.class, CardOrder.this);
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

        JLabel lblTotal = new JLabel(String.format("Tổng: %,.0f đ", realTotal)); 
        lblTotal.setFont(new Font("Arial", Font.BOLD, 16));
        pnlFooter.add(lblTotal, BorderLayout.WEST);

        if (canCancelWholeOrder) {
            JButton btnCancelOrder = new JButton("HỦY CẢ ĐƠN"); 
            btnCancelOrder.setFont(new Font("Arial", Font.BOLD, 12));
            btnCancelOrder.setForeground(Color.WHITE);
            btnCancelOrder.setBackground(new Color(220, 53, 69));
            btnCancelOrder.setFocusPainted(false);
            btnCancelOrder.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
            
            btnCancelOrder.addActionListener(e -> {
                int choice = JOptionPane.showConfirmDialog(this, "Bạn chắc chắn muốn hủy toàn bộ đơn " + orderId + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    OrderDetailsDAO dao = new OrderDetailsDAO();
                    if (dao.cancelWholeOrder(orderId)) {
                        if (controller != null) controller.loadOrdersToUI(); 
                    }
                }
            });
            pnlFooter.add(btnCancelOrder, BorderLayout.EAST);
        }

        innerCard.add(pnlFooter, BorderLayout.SOUTH);
        this.add(innerCard, BorderLayout.CENTER);
    }
}