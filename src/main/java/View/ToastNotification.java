package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class ToastNotification extends JPanel {
    private String message;
    private int targetY = 30; 
    private int currentY;
 
    private int toastWidth = 400;
    private int toastHeight = 90;

    private Timer animationTimer; 
    private Timer progressTimer;  
    
    private int lifeTime = 3500; 
    private int currentLife = 3500; 
    private JFrame parentFrame;

    public ToastNotification(JFrame parent, String message) {
        this.parentFrame = parent;
        this.message = message;
        
        setOpaque(false);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(15, 30, 25, 30)); 
        
        String timeNow = new SimpleDateFormat("HH:mm").format(new Date());
        JLabel lblMsg = new JLabel("<html>" + message + " <br><span style='font-size:12px; color:#e0e0e0;'>Lúc " + timeNow + "</span></html>");
        lblMsg.setForeground(Color.WHITE);
        lblMsg.setFont(new Font("Arial", Font.BOLD, 16));
        
        JLabel lblIcon = new JLabel(""); 
        lblIcon.setFont(new Font("Arial", Font.PLAIN, 24));
        lblIcon.setForeground(Color.WHITE);
        
        add(lblIcon, BorderLayout.WEST);
        add(lblMsg, BorderLayout.CENTER);
        
        currentY = -toastHeight;
        int x = (parent.getWidth() - toastWidth) / 2;
        setBounds(x, currentY, toastWidth, toastHeight);
        
        parent.getLayeredPane().add(this, JLayeredPane.POPUP_LAYER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2.setColor(new Color(40, 167, 69, 240)); 
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
        
        g2.setColor(new Color(255, 255, 255, 100));
        g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 20, 20);
        
        g2.setColor(new Color(255, 255, 255, 180)); 
        int barWidth = (int) ((currentLife / (double) lifeTime) * (getWidth() - 24)); 
        g2.fillRoundRect(12, getHeight() - 10, Math.max(0, barWidth), 4, 4, 4);
        
        g2.dispose();
    }

    public void showToast() {
        animationTimer = new Timer(10, e -> {
            if (currentY < targetY) {
                currentY += 5; 
                setLocation(getX(), currentY);
            } else {
                animationTimer.stop();
            
                progressTimer = new Timer(15, evt -> {
                    currentLife -= 15;
                    repaint(); 
                    
                    if (currentLife <= 0) {
                        progressTimer.stop();
                        hideToast(); 
                    }
                });
                progressTimer.start();
            }
        });
        animationTimer.start();
    }

    private void hideToast() {
        animationTimer = new Timer(10, e -> {
            if (currentY > -toastHeight) {
                currentY -= 5;
                setLocation(getX(), currentY);
            } else {
                animationTimer.stop();
                
                parentFrame.getLayeredPane().remove(ToastNotification.this);
                parentFrame.getLayeredPane().repaint();
            }
        });
        animationTimer.start();
    }
    
    public static void show(JFrame parent, String message) {
        new ToastNotification(parent, message).showToast();
    }
}