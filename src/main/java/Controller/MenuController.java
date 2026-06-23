package Controller;

import DAO.ItemDAO;
import Model.Item;
import View.CardFood;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class MenuController {
    
    private ItemDAO itemDAO;
    private JPanel jPanelFoodList;
    private CartItemController cartItemController; 
    
    private String currentCategory = "ALL"; 
    private String currentKeyword = "";

    public MenuController(JPanel jPanelFoodList, CartItemController cartItemController) {
        this.itemDAO = new ItemDAO();
        this.jPanelFoodList = jPanelFoodList;
        this.cartItemController = cartItemController; 
    }

    public void loadAll() {
        this.currentCategory = "ALL";
        this.currentKeyword = "";
        loadData();
    }

    public void searchGlobal(String keyword) {
        this.currentKeyword = keyword.trim();
        this.currentCategory = "ALL"; 
        loadData();
    }

    public void loadByCategory(String menuId) {
        this.currentCategory = menuId;
        loadData();
    }
    
    public void clearSearch() {
        this.currentKeyword = "";
        loadData();
    }

    private void loadData() {
        List<Item> list = itemDAO.searchItems(currentKeyword, currentCategory);
        renderToView(list);
    }

    private void renderToView(List<Item> list) {
        jPanelFoodList.removeAll(); 
        
        // ==========================================================
        // THAY ĐỔI LAYOUT CỦA PANEL GỐC THÀNH BORDER_LAYOUT
        // ==========================================================
        jPanelFoodList.setLayout(new BorderLayout());
        jPanelFoodList.setOpaque(false);
        
        if (list.isEmpty()) {
            JLabel lblEmpty = new JLabel("Không tìm thấy món");
            lblEmpty.setFont(new Font("Arial", Font.BOLD, 18));
            lblEmpty.setForeground(Color.GRAY);
            lblEmpty.setHorizontalAlignment(SwingConstants.CENTER);
            jPanelFoodList.add(lblEmpty, BorderLayout.CENTER);
        } else {
            // ==========================================================
            // TẠO MỘT LỚP BỌC (WRAPPER) ĐỂ BẢO VỆ KÍCH THƯỚC CỦA THẺ
            // ==========================================================
            JPanel gridPanel = new JPanel(new GridLayout(0, 4, 20, 20));
            gridPanel.setOpaque(false);
            
            // Padding lùi vào 20px ở cả 4 viền để thẻ không đâm vào vách
            gridPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));
            
            for (Item item : list) {
                gridPanel.add(new CardFood(item, cartItemController)); 
            }
            
            // TUYỆT CHIÊU: Gắn lưới vào NÓC (NORTH) của khu vực chứa. 
            // Hành động này triệt tiêu hoàn toàn sự bóp méo của ScrollPane!
            jPanelFoodList.add(gridPanel, BorderLayout.NORTH);
        }
        
        jPanelFoodList.revalidate();
        jPanelFoodList.repaint();
    }
}