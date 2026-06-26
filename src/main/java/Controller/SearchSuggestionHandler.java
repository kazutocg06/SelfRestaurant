package Controller;

import DAO.ItemDAO;
import Model.Item;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.AWTEvent;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;

public class SearchSuggestionHandler {

    private JTextField txtSearch;
    private MenuController menuController;
    private ItemDAO dao;
    
    private JPopupMenu searchPopup;
    private JPanel searchContainer;
    private Timer liveSearchTimer;
    private List<Item> lastResults;

    public SearchSuggestionHandler(JTextField txtSearch, MenuController menuController) {
        this.txtSearch = txtSearch;
        this.menuController = menuController;
        this.dao = new ItemDAO();
        
        initPopupUI();   
        initListeners(); 
    }

    private void initPopupUI() {
        searchPopup = new JPopupMenu();
        searchPopup.setLightWeightPopupEnabled(false); 
        searchPopup.setFocusable(false);
        searchPopup.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        searchContainer = new JPanel();
        searchContainer.setLayout(new BoxLayout(searchContainer, BoxLayout.Y_AXIS));
        searchContainer.setBackground(Color.WHITE);

        JScrollPane searchScroll = new JScrollPane(searchContainer);
        searchScroll.setBorder(null);
        searchScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        searchScroll.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));      
        Controller.KineticScroller.setup(searchScroll);

        searchPopup.add(searchScroll);
    }

    private void initListeners() {
        liveSearchTimer = new Timer(300, e -> performSearch());
        liveSearchTimer.setRepeats(false);

        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { liveSearchTimer.restart(); }
            @Override public void removeUpdate(DocumentEvent e) { liveSearchTimer.restart(); }
            @Override public void changedUpdate(DocumentEvent e) { liveSearchTimer.restart(); }
        });
        
        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
            @Override
            public void eventDispatched(AWTEvent event) {
                if (event.getID() == java.awt.event.MouseEvent.MOUSE_WHEEL) {
                    if (searchPopup != null && searchPopup.isVisible()) {
                        java.awt.event.MouseEvent me = (java.awt.event.MouseEvent) event;
                        if (javax.swing.SwingUtilities.isDescendingFrom(me.getComponent(), searchPopup)) {
                            return; 
                        }
                        hidePopup(); 
                    }
                }
            }
        }, AWTEvent.MOUSE_WHEEL_EVENT_MASK);
    }
    private void performSearch() {
        String kw = txtSearch.getText().trim();
        
        if (kw.isEmpty()) {
            hidePopup();
            return;
        }

        lastResults = dao.searchItems(kw, "ALL");
        searchContainer.removeAll(); 

        if (lastResults == null || lastResults.isEmpty()) {
            hidePopup();
        } else {
            int itemHeight = 70; 
            int popupWidth = txtSearch.getWidth();
            int imgWidth = (int) (popupWidth * 0.3); 

            for (Item item : lastResults) {
                JPanel row = new JPanel(new BorderLayout());
                row.setPreferredSize(new Dimension(popupWidth, itemHeight));
                row.setMaximumSize(new Dimension(popupWidth, itemHeight));
                row.setBackground(Color.WHITE);
                row.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)));

                JPanel pnlImg = new JPanel(new BorderLayout());
                pnlImg.setOpaque(false); 
                pnlImg.setPreferredSize(new Dimension(imgWidth, itemHeight));
                pnlImg.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 5)); 
                
                JPanel pnlImgDraw = new JPanel() {
                    Image imgOriginal = new ImageIcon("D:/DoAnPTTKTHTTT/SelfRestaurant/src/main/java/images/" + item.getImgUrl()).getImage();

                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        int targetW = getWidth();
                        int targetH = getHeight();

                        if (imgOriginal != null && imgOriginal.getWidth(null) > 0) {
                            java.awt.image.BufferedImage maskImage = new java.awt.image.BufferedImage(targetW, targetH, java.awt.image.BufferedImage.TYPE_INT_ARGB);
                            Graphics2D g2d = maskImage.createGraphics();
                            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                            g2d.setColor(Color.WHITE);
                            g2d.fillRoundRect(0, 0, targetW, targetH, 8, 8);

                            g2d.setComposite(java.awt.AlphaComposite.SrcIn);
                            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

                            double scaleX = (double) targetW / imgOriginal.getWidth(null);
                            double scaleY = (double) targetH / imgOriginal.getHeight(null);
                            double scale = Math.max(scaleX, scaleY); 

                            int scaledW = (int) (imgOriginal.getWidth(null) * scale);
                            int scaledH = (int) (imgOriginal.getHeight(null) * scale);

                            int x = (targetW - scaledW) / 2;
                            int y = (targetH - scaledH) / 2;

                            g2d.drawImage(imgOriginal, x, y, scaledW, scaledH, null);
                            g2d.dispose(); 

                            g.drawImage(maskImage, 0, 0, null);
                        }
                    }
                };
                pnlImgDraw.setOpaque(false); 
                pnlImg.add(pnlImgDraw, BorderLayout.CENTER);

                JLabel lblName = new JLabel("   " + item.getName());
                lblName.setFont(new Font("Arial", Font.BOLD, 14));

                row.add(pnlImg, BorderLayout.WEST); 
                row.add(lblName, BorderLayout.CENTER); 
                row.addMouseListener(new java.awt.event.MouseAdapter() {
                    private int startYOnScreen;

                    @Override
                    public void mousePressed(java.awt.event.MouseEvent e) {
                        startYOnScreen = e.getLocationOnScreen().y;
                    }

                    @Override
                    public void mouseReleased(java.awt.event.MouseEvent e) {
                        int deltaY = Math.abs(e.getLocationOnScreen().y - startYOnScreen);
                        if (deltaY < 5) {
                            if (e.getComponent().contains(e.getPoint())) {
                                txtSearch.setText(item.getName());
                                hidePopup();
                                menuController.searchGlobal(item.getName());
                            }
                        }
                    }
                });
                
                searchContainer.add(row);
            }

            searchContainer.revalidate();
            searchContainer.repaint();
        }
    }
    public void onChangeSearch(String text) {
        if (liveSearchTimer != null && liveSearchTimer.isRunning()) {
            liveSearchTimer.stop();
        }
        performSearch(); 
    }
    public void showPopup() {
        if (lastResults != null && !lastResults.isEmpty() && !txtSearch.getText().trim().isEmpty()) {
            int itemHeight = 70; 
            int popupWidth = txtSearch.getWidth();
            int showCount = Math.min(lastResults.size(), 3);
            
            searchPopup.setPopupSize(popupWidth, showCount * itemHeight);
            searchPopup.show(txtSearch, 0, txtSearch.getHeight());
            txtSearch.requestFocus();
        }
    }
    
    public void hidePopup() {
        if (liveSearchTimer != null && liveSearchTimer.isRunning()) {
            liveSearchTimer.stop();
        }
        if (searchPopup != null && searchPopup.isVisible()) {
            searchPopup.setVisible(false);
        }
    }
}