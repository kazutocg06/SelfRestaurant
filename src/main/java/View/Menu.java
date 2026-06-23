/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package View;

import Controller.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

/**
 *
 * @author LOQ
 */
public class Menu extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Menu.class.getName());

    private Controller.MenuController menuController;
    private Controller.SearchSuggestionHandler searchHandler;
    private Controller.CartItemController cartItemController;

    public Menu() {

        initComponents();

        this.setSize(1200, 680); //kích thước cứng cho cửa sổ Kiosk
        this.setResizable(false); // Khóa không cho tự co giãn
        this.setLocationRelativeTo(null); // Hiển thị ở chính giữa màn hình

        this.getContentPane().setBackground(new java.awt.Color(255, 102, 51));
        jPanelDynamic.setOpaque(false);

        jButtonMenu.setOpaque(false);
        jButtonMenu.setContentAreaFilled(false);
        jButtonMenu.setBorderPainted(false);
        jButtonMenu.setFocusPainted(false);

        // ==========================================================
        // 1. CẤU HÌNH CARDLAYOUT CHO CÁC TIÊU ĐỀ
        // ==========================================================
        jPanelDynamic.add(createTitlePanel("Giỏ hàng của bạn"), "cartCard");
        jPanelDynamic.add(createTitlePanel("Đơn hàng đang xử lý"), "orderCard");
        jPanelDynamic.add(createTitlePanel("Lịch sử hóa đơn"), "receiptCard");

        // ==========================================================
        // 2. CẤU HÌNH DANH SÁCH MÓN ĂN (Chống xé hình)
        // ==========================================================
        jScrollPane1.getViewport().setOpaque(true);
        jScrollPane1.getViewport().setBackground(new Color(235, 238, 240));
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        // Ẩn vĩnh viễn thanh cuộn dọc và ngang của Giỏ hàng (Cho đồng bộ giao diện Kiosk)
        jScrollPaneCart.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollPaneCart.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        // ==========================================================
        // 3. LẮP RÁP THẺ "TÓM TẮT ĐƠN HÀNG" VÀO KHUNG jPanelOrderSummary
        // ==========================================================
        // Xóa sạch nền xám và đồ cũ của khung kéo thả
        jPanelOrderSummary.setOpaque(false);
        jPanelOrderSummary.setLayout(new BorderLayout());
        jPanelOrderSummary.removeAll();

        // Lấy thẻ chúng ta đã thiết kế ra
        View.CardOrderSummary orderSummaryCard = new View.CardOrderSummary();

        // Bọc vào Wrapper để ép nó lên trên cùng, tránh bị dãn ra giữa màn hình
        jPanelOrderSummary.add(orderSummaryCard, BorderLayout.CENTER);

        // Tăng lề phải lên 60px để bảng Summary lùi vào trong, không dính sát mép phải
        // ==========================================================
        // 4. KHỞI TẠO LƯỚI GIỎ HÀNG
        // ==========================================================
        JPanel jPanelCartGrid = new JPanel();
        jScrollPaneCart.setViewportView(jPanelCartGrid);

        // ==========================================================
        // 5. KHỞI TẠO CONTROLLER VÀ ĐỔ DỮ LIỆU
        // ==========================================================
        // Truyền lưới và 4 cái nhãn từ thẻ tóm tắt vào Controller
        cartItemController = new Controller.CartItemController(
                jPanelCartGrid,
                jPanelOrder,
                orderSummaryCard.getLblOrderId(),
                orderSummaryCard.getLblTotalUniqueItems(),
                orderSummaryCard.getLblTotalQuantity(),
                orderSummaryCard.getLblGrandTotal()
        );

        menuController = new Controller.MenuController(jPanelFoodList, cartItemController);
        menuController.loadAll();

        Controller.KineticScroller.setup(jScrollPane1);
        Controller.KineticScroller.setup(jScrollPaneCart);
        jScrollPane1.getViewport().setScrollMode(javax.swing.JViewport.SIMPLE_SCROLL_MODE);
        jScrollPaneCart.getViewport().setScrollMode(javax.swing.JViewport.SIMPLE_SCROLL_MODE);
        jScrollOrder.getViewport().setScrollMode(javax.swing.JViewport.SIMPLE_SCROLL_MODE);

        jScrollPaneCart.setOpaque(false);
        jScrollPaneCart.getViewport().setOpaque(false);
        jScrollPaneCart.setBorder(null);
        searchHandler = new Controller.SearchSuggestionHandler(jTextField1, menuController);
        jTextField1.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                jTextField1ActionPerformed(null);
            }
        });

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                if (cartItemController != null && cartItemController.getCurrentCartId() != null) {
                    DAO.CartDAO dao = new DAO.CartDAO();
                    dao.deleteCartAndItems(cartItemController.getCurrentCartId());
                }
            }
        }));
        Controller.KitchenSimulator.getInstance().startSimulation();
        fitImageToButton(jButtonCart, "D:/DoAnPTTKTHTTT/SelfRestaurant/src/main/resources/images/cart.png");
        fitImageToButton(jButtonOrder, "D:/DoAnPTTKTHTTT/SelfRestaurant/src/main/resources/images/bell.png");
    }

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jPanelHeading = new javax.swing.JPanel();
        jPanelFunction = new javax.swing.JPanel();
        jButtonMenu = new javax.swing.JButton();
        jButtonCart = new javax.swing.JButton();
        jButtonOrder = new javax.swing.JButton();
        jButtonReciept = new javax.swing.JButton();
        jPanelDynamic = new javax.swing.JPanel();
        jPanelDyMenu = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jButtonTim = new javax.swing.JButton();
        jButtonXoa = new javax.swing.JButton();
        jPanelDyCart = new javax.swing.JPanel();
        jPanelDyOrder = new javax.swing.JPanel();
        jPanelDyReciept = new javax.swing.JPanel();
        jPanelContent = new javax.swing.JPanel();
        jPanelMenu = new javax.swing.JPanel();
        jButtonSToanBo = new javax.swing.JButton();
        jButtonSMonChinh = new javax.swing.JButton();
        jButtonSDoUong = new javax.swing.JButton();
        jButtonSTrangMieng = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanelFoodList = new javax.swing.JPanel();
        jPanelCart = new javax.swing.JPanel();
        jScrollPaneCart = new javax.swing.JScrollPane();
        jPanelCartGrid = new javax.swing.JPanel();
        jButtonXacNhanOrder = new javax.swing.JButton();
        jPanelOrderSummary = new javax.swing.JPanel();
        jPanelOrder = new javax.swing.JPanel();
        jScrollOrder = new javax.swing.JScrollPane();
        jPanelOrderFood = new javax.swing.JPanel();
        jPanelReciept = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(253, 126, 20));

        jPanelHeading.setBackground(new java.awt.Color(255, 102, 51));

        jPanelFunction.setBackground(new java.awt.Color(255, 102, 51));

        jButtonMenu.setIcon(new javax.swing.ImageIcon("D:\\DoAnPTTKTHTTT\\SelfRestaurant\\src\\main\\resources\\images\\icon-menu.png")); // NOI18N
        jButtonMenu.setBorderPainted(false);
        jButtonMenu.setContentAreaFilled(false);
        jButtonMenu.setFocusPainted(false);
        jButtonMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMenuActionPerformed(evt);
            }
        });

        jButtonCart.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cart.png"))); // NOI18N
        jButtonCart.setContentAreaFilled(false);
        jButtonCart.setFocusPainted(false);
        jButtonCart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCartActionPerformed(evt);
            }
        });

        jButtonOrder.setIcon(new javax.swing.ImageIcon(getClass().getResource("/bell.png"))); // NOI18N
        jButtonOrder.setContentAreaFilled(false);
        jButtonOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOrderActionPerformed(evt);
            }
        });

        jButtonReciept.setIcon(new javax.swing.ImageIcon(getClass().getResource("/receipt.png"))); // NOI18N
        jButtonReciept.setBorderPainted(false);
        jButtonReciept.setContentAreaFilled(false);
        jButtonReciept.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRecieptActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelFunctionLayout = new javax.swing.GroupLayout(jPanelFunction);
        jPanelFunction.setLayout(jPanelFunctionLayout);
        jPanelFunctionLayout.setHorizontalGroup(
            jPanelFunctionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelFunctionLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jButtonMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addComponent(jButtonCart, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(jButtonOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(jButtonReciept, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26))
        );
        jPanelFunctionLayout.setVerticalGroup(
            jPanelFunctionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelFunctionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelFunctionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButtonMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonReciept, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonCart, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonOrder, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanelDynamic.setLayout(new java.awt.CardLayout());

        jPanelDyMenu.setBackground(new java.awt.Color(255, 102, 51));

        jTextField1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jTextField1.setPreferredSize(new java.awt.Dimension(10, 10));
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jButtonTim.setText("Tìm");
        jButtonTim.setBorderPainted(false);
        jButtonTim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonTimActionPerformed(evt);
            }
        });

        jButtonXoa.setText("Xóa");
        jButtonXoa.setBorderPainted(false);
        jButtonXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonXoaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelDyMenuLayout = new javax.swing.GroupLayout(jPanelDyMenu);
        jPanelDyMenu.setLayout(jPanelDyMenuLayout);
        jPanelDyMenuLayout.setHorizontalGroup(
            jPanelDyMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDyMenuLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 417, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButtonXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonTim, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );
        jPanelDyMenuLayout.setVerticalGroup(
            jPanelDyMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDyMenuLayout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(jPanelDyMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonXoa, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                    .addComponent(jButtonTim, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jPanelDynamic.add(jPanelDyMenu, "card2");

        javax.swing.GroupLayout jPanelDyCartLayout = new javax.swing.GroupLayout(jPanelDyCart);
        jPanelDyCart.setLayout(jPanelDyCartLayout);
        jPanelDyCartLayout.setHorizontalGroup(
            jPanelDyCartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 580, Short.MAX_VALUE)
        );
        jPanelDyCartLayout.setVerticalGroup(
            jPanelDyCartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 64, Short.MAX_VALUE)
        );

        jPanelDynamic.add(jPanelDyCart, "card3");

        jPanelDyOrder.setBackground(new java.awt.Color(255, 102, 0));

        javax.swing.GroupLayout jPanelDyOrderLayout = new javax.swing.GroupLayout(jPanelDyOrder);
        jPanelDyOrder.setLayout(jPanelDyOrderLayout);
        jPanelDyOrderLayout.setHorizontalGroup(
            jPanelDyOrderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 580, Short.MAX_VALUE)
        );
        jPanelDyOrderLayout.setVerticalGroup(
            jPanelDyOrderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 64, Short.MAX_VALUE)
        );

        jPanelDynamic.add(jPanelDyOrder, "card4");

        javax.swing.GroupLayout jPanelDyRecieptLayout = new javax.swing.GroupLayout(jPanelDyReciept);
        jPanelDyReciept.setLayout(jPanelDyRecieptLayout);
        jPanelDyRecieptLayout.setHorizontalGroup(
            jPanelDyRecieptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 580, Short.MAX_VALUE)
        );
        jPanelDyRecieptLayout.setVerticalGroup(
            jPanelDyRecieptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 64, Short.MAX_VALUE)
        );

        jPanelDynamic.add(jPanelDyReciept, "card5");

        javax.swing.GroupLayout jPanelHeadingLayout = new javax.swing.GroupLayout(jPanelHeading);
        jPanelHeading.setLayout(jPanelHeadingLayout);
        jPanelHeadingLayout.setHorizontalGroup(
            jPanelHeadingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelHeadingLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelDynamic, javax.swing.GroupLayout.PREFERRED_SIZE, 580, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanelFunction, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanelHeadingLayout.setVerticalGroup(
            jPanelHeadingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelHeadingLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelHeadingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanelDynamic, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanelFunction, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanelContent.setBackground(new java.awt.Color(248, 249, 250));
        jPanelContent.setLayout(new java.awt.CardLayout());

        jPanelMenu.setBackground(new java.awt.Color(255, 102, 51));

        jButtonSToanBo.setText("Toàn bộ");
        jButtonSToanBo.setBorderPainted(false);
        jButtonSToanBo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSToanBoActionPerformed(evt);
            }
        });

        jButtonSMonChinh.setText("Món chính");
        jButtonSMonChinh.setBorderPainted(false);
        jButtonSMonChinh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSMonChinhActionPerformed(evt);
            }
        });

        jButtonSDoUong.setText("Đồ uống");
        jButtonSDoUong.setBorderPainted(false);
        jButtonSDoUong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSDoUongActionPerformed(evt);
            }
        });

        jButtonSTrangMieng.setText("Tráng miệng");
        jButtonSTrangMieng.setBorderPainted(false);
        jButtonSTrangMieng.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSTrangMiengActionPerformed(evt);
            }
        });

        jScrollPane1.setBorder(null);

        jPanelFoodList.setLayout(new java.awt.GridLayout(0, 4, 20, 20));
        jScrollPane1.setViewportView(jPanelFoodList);

        javax.swing.GroupLayout jPanelMenuLayout = new javax.swing.GroupLayout(jPanelMenu);
        jPanelMenu.setLayout(jPanelMenuLayout);
        jPanelMenuLayout.setHorizontalGroup(
            jPanelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelMenuLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jButtonSToanBo)
                .addGap(18, 18, 18)
                .addComponent(jButtonSMonChinh)
                .addGap(18, 18, 18)
                .addComponent(jButtonSDoUong)
                .addGap(18, 18, 18)
                .addComponent(jButtonSTrangMieng)
                .addContainerGap(781, Short.MAX_VALUE))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanelMenuLayout.setVerticalGroup(
            jPanelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelMenuLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonSToanBo)
                    .addComponent(jButtonSMonChinh)
                    .addComponent(jButtonSDoUong)
                    .addComponent(jButtonSTrangMieng))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 493, Short.MAX_VALUE))
        );

        jPanelContent.add(jPanelMenu, "card2");

        jPanelCart.setBackground(new java.awt.Color(255, 102, 51));

        jPanelCartGrid.setMinimumSize(new java.awt.Dimension(20, 0));
        jPanelCartGrid.setLayout(new java.awt.GridLayout(1, 0, 20, 20));
        jScrollPaneCart.setViewportView(jPanelCartGrid);

        jButtonXacNhanOrder.setText("Xác Nhận Đặt Đơn");
        jButtonXacNhanOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonXacNhanOrderActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelOrderSummaryLayout = new javax.swing.GroupLayout(jPanelOrderSummary);
        jPanelOrderSummary.setLayout(jPanelOrderSummaryLayout);
        jPanelOrderSummaryLayout.setHorizontalGroup(
            jPanelOrderSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 368, Short.MAX_VALUE)
        );
        jPanelOrderSummaryLayout.setVerticalGroup(
            jPanelOrderSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 403, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanelCartLayout = new javax.swing.GroupLayout(jPanelCart);
        jPanelCart.setLayout(jPanelCartLayout);
        jPanelCartLayout.setHorizontalGroup(
            jPanelCartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCartLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPaneCart, javax.swing.GroupLayout.PREFERRED_SIZE, 747, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                .addGroup(jPanelCartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelCartLayout.createSequentialGroup()
                        .addComponent(jButtonXacNhanOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(88, 88, 88))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelCartLayout.createSequentialGroup()
                        .addComponent(jPanelOrderSummary, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29))))
        );
        jPanelCartLayout.setVerticalGroup(
            jPanelCartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCartLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelCartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelCartLayout.createSequentialGroup()
                        .addComponent(jPanelOrderSummary, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 66, Short.MAX_VALUE)
                        .addComponent(jButtonXacNhanOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(16, 16, 16))
                    .addGroup(jPanelCartLayout.createSequentialGroup()
                        .addComponent(jScrollPaneCart)
                        .addContainerGap())))
        );

        jPanelContent.add(jPanelCart, "card3");

        jPanelOrderFood.setLayout(new java.awt.GridLayout(0, 2, 20, 20));
        jScrollOrder.setViewportView(jPanelOrderFood);

        javax.swing.GroupLayout jPanelOrderLayout = new javax.swing.GroupLayout(jPanelOrder);
        jPanelOrder.setLayout(jPanelOrderLayout);
        jPanelOrderLayout.setHorizontalGroup(
            jPanelOrderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelOrderLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollOrder, javax.swing.GroupLayout.DEFAULT_SIZE, 1172, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanelOrderLayout.setVerticalGroup(
            jPanelOrderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelOrderLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollOrder, javax.swing.GroupLayout.DEFAULT_SIZE, 522, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanelContent.add(jPanelOrder, "card4");

        javax.swing.GroupLayout jPanelRecieptLayout = new javax.swing.GroupLayout(jPanelReciept);
        jPanelReciept.setLayout(jPanelRecieptLayout);
        jPanelRecieptLayout.setHorizontalGroup(
            jPanelRecieptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1184, Short.MAX_VALUE)
        );
        jPanelRecieptLayout.setVerticalGroup(
            jPanelRecieptLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 534, Short.MAX_VALUE)
        );

        jPanelContent.add(jPanelReciept, "card5");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelHeading, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(92, 92, 92))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelContent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanelHeading, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanelContent, javax.swing.GroupLayout.PREFERRED_SIZE, 534, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addGap(0, 1, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonSToanBoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSToanBoActionPerformed
        menuController.loadAll();
    }//GEN-LAST:event_jButtonSToanBoActionPerformed

    private void jButtonSTrangMiengActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSTrangMiengActionPerformed
        menuController.loadByCategory("trangMieng");
    }//GEN-LAST:event_jButtonSTrangMiengActionPerformed

    private void jButtonSDoUongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSDoUongActionPerformed
        menuController.loadByCategory("doUong");
    }//GEN-LAST:event_jButtonSDoUongActionPerformed

    private void jButtonSMonChinhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSMonChinhActionPerformed
        menuController.loadByCategory("monChinh");
    }//GEN-LAST:event_jButtonSMonChinhActionPerformed

    private void jButtonMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMenuActionPerformed
        jPanelContent.removeAll();
        jPanelContent.add(jPanelMenu);
        jPanelContent.repaint();
        jPanelContent.revalidate();

        switchHeaderCard("card2");
    }//GEN-LAST:event_jButtonMenuActionPerformed

    private void jButtonOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOrderActionPerformed
        jPanelContent.removeAll();
        jPanelContent.add(jPanelOrder);
        jPanelContent.repaint();
        jPanelContent.revalidate();

        switchHeaderCard("orderCard");
        if (searchHandler != null) {
            searchHandler.hidePopup();
        }
        Controller.OrderController orderController = new Controller.OrderController(jPanelOrder);
        orderController.loadOrdersToUI();
        Controller.KitchenSimulator.getInstance().setOrderController(orderController);
    }//GEN-LAST:event_jButtonOrderActionPerformed

    private void jButtonRecieptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRecieptActionPerformed
        jPanelContent.removeAll();
        jPanelContent.add(jPanelReciept);
        jPanelContent.repaint();
        jPanelContent.revalidate();

        switchHeaderCard("receiptCard");
        if (searchHandler != null) {
            searchHandler.hidePopup();
        }

        Controller.ReceiptController receiptController = new Controller.ReceiptController(jPanelReciept);
        receiptController.loadReceiptsToUI();
    }//GEN-LAST:event_jButtonRecieptActionPerformed

    private void jButtonTimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonTimActionPerformed
        searchHandler.hidePopup(); 
        menuController.searchGlobal(jTextField1.getText());
    }//GEN-LAST:event_jButtonTimActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        if (searchHandler != null) {
            searchHandler.hidePopup();
        }

        VirtualKeyboardDialog vk = new VirtualKeyboardDialog(this, "Tìm kiếm món ăn (VD: Trà sữa, Bạc xỉu...)", jTextField1.getText());
        vk.setVisible(true);

        String searchKeyword = vk.getResult();

        vk.dispose();

        if (searchKeyword != null) {
            jTextField1.setText(searchKeyword);
            jTextField1.requestFocusInWindow();
            if (searchHandler != null) {
                searchHandler.onChangeSearch(searchKeyword);
                searchHandler.showPopup();
            }
        }
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jButtonXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonXoaActionPerformed
        jTextField1.setText(""); 
        menuController.clearSearch();
    }//GEN-LAST:event_jButtonXoaActionPerformed

    private void jButtonXacNhanOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonXacNhanOrderActionPerformed
        if (cartItemController != null) {
            cartItemController.confirmOrder();
        }
    }//GEN-LAST:event_jButtonXacNhanOrderActionPerformed

    private void jButtonCartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCartActionPerformed
        jPanelContent.removeAll();
        jPanelContent.add(jPanelCart);
        jPanelContent.repaint();
        jPanelContent.revalidate();

        switchHeaderCard("cartCard");
        if (searchHandler != null)
            searchHandler.hidePopup();
    }//GEN-LAST:event_jButtonCartActionPerformed

    private void switchHeaderCard(String cardName) {
        java.awt.CardLayout cl = (java.awt.CardLayout) (jPanelDynamic.getLayout());
        cl.show(jPanelDynamic, cardName);
    }

    private javax.swing.JPanel createTitlePanel(String title) {
        javax.swing.JPanel panel = new javax.swing.JPanel(new java.awt.BorderLayout());

        panel.setOpaque(false);
        panel.setBackground(new java.awt.Color(0, 0, 0, 0));

        javax.swing.JLabel label = new javax.swing.JLabel(title);
        label.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 22));
        label.setForeground(java.awt.Color.WHITE); 
        label.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 15, 0, 0));

        panel.add(label, java.awt.BorderLayout.WEST);
        return panel;
    }

    public void fitImageToButton(javax.swing.JButton button, String imagePath) {
        int width = button.getWidth() > 0 ? button.getWidth() : button.getPreferredSize().width;
        int height = button.getHeight() > 0 ? button.getHeight() : button.getPreferredSize().height;

        if (width <= 0) {
            width = 47;
        }
        if (height <= 0) {
            height = 52;
        }

        try {
            java.awt.Image imgOriginal = new javax.swing.ImageIcon(imagePath).getImage();

            if (imgOriginal != null && imgOriginal.getWidth(null) > 0) {
                java.awt.image.BufferedImage scaledImg = new java.awt.image.BufferedImage(
                        width, height, java.awt.image.BufferedImage.TYPE_INT_ARGB
                );

                Graphics2D g2d = scaledImg.createGraphics();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                g2d.drawImage(imgOriginal, 0, 0, width, height, null);
                g2d.dispose();

                button.setIcon(new javax.swing.ImageIcon(scaledImg));
            }
        } catch (Exception e) {
            System.out.println("Lỗi tải ảnh: " + e.getMessage());
        }

        button.setMargin(new java.awt.Insets(0, 0, 0, 0));
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
    }

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new Menu().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCart;
    private javax.swing.JButton jButtonMenu;
    private javax.swing.JButton jButtonOrder;
    private javax.swing.JButton jButtonReciept;
    private javax.swing.JButton jButtonSDoUong;
    private javax.swing.JButton jButtonSMonChinh;
    private javax.swing.JButton jButtonSToanBo;
    private javax.swing.JButton jButtonSTrangMieng;
    private javax.swing.JButton jButtonTim;
    private javax.swing.JButton jButtonXacNhanOrder;
    private javax.swing.JButton jButtonXoa;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanelCart;
    private javax.swing.JPanel jPanelCartGrid;
    private javax.swing.JPanel jPanelContent;
    private javax.swing.JPanel jPanelDyCart;
    private javax.swing.JPanel jPanelDyMenu;
    private javax.swing.JPanel jPanelDyOrder;
    private javax.swing.JPanel jPanelDyReciept;
    private javax.swing.JPanel jPanelDynamic;
    private javax.swing.JPanel jPanelFoodList;
    private javax.swing.JPanel jPanelFunction;
    private javax.swing.JPanel jPanelHeading;
    private javax.swing.JPanel jPanelMenu;
    private javax.swing.JPanel jPanelOrder;
    private javax.swing.JPanel jPanelOrderFood;
    private javax.swing.JPanel jPanelOrderSummary;
    private javax.swing.JPanel jPanelReciept;
    private javax.swing.JScrollPane jScrollOrder;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPaneCart;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
