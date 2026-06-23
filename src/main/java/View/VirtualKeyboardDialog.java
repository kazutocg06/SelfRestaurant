package View;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class VirtualKeyboardDialog extends JDialog {
    private String result = null;
    private JTextArea txtInput;
    
    private boolean isCaps = false; 
    private List<JButton> letterButtons = new ArrayList<>(); 

    public VirtualKeyboardDialog(Frame parent, String title, String initialText) {
        super(parent, title, true); 
        
        setUndecorated(true); 
        if (parent != null) {
            setBounds(parent.getBounds()); 
        } else {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            setBounds(0, 0, screenSize.width, screenSize.height);
        }
        setBackground(new Color(0, 0, 0, 140));

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2));
        mainPanel.setPreferredSize(new Dimension(850, 480)); 

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(245, 245, 245));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 5));
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 15));
        lblTitle.setForeground(Color.DARK_GRAY);
        headerPanel.add(lblTitle, BorderLayout.WEST);
        
        JButton btnClose = new JButton("X");
        btnClose.setFont(new Font("Arial", Font.BOLD, 16));
        btnClose.setForeground(Color.WHITE);
        btnClose.setBackground(new Color(220, 53, 69));
        btnClose.setFocusPainted(false);
        btnClose.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        btnClose.addActionListener(e -> {
            result = null;
            dispose();
        });
        headerPanel.add(btnClose, BorderLayout.EAST);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));
        topPanel.setOpaque(false);
        
        txtInput = new JTextArea(3, 20);
        txtInput.setFont(new Font("Arial", Font.BOLD, 22));
        txtInput.setLineWrap(true);
        txtInput.setWrapStyleWord(true);
        if (initialText != null) txtInput.setText(initialText);
        
        JScrollPane scrollPane = new JScrollPane(txtInput);
        topPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel keyboardPanel = new JPanel();
        keyboardPanel.setLayout(new BoxLayout(keyboardPanel, BoxLayout.Y_AXIS));
        keyboardPanel.setOpaque(false);
        keyboardPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));

        String[][] keys = {
            {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "<- Xóa"},
            {"q", "w", "e", "r", "t", "y", "u", "i", "o", "p"},
            {"a", "s", "d", "f", "g", "h", "j", "k", "l"},
            {"CAPS", "z", "x", "c", "v", "b", "n", "m", "Xong"}
        };

        for (String[] row : keys) {
            JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
            rowPanel.setOpaque(false);
            
            for (String key : row) {
                JButton btn = new JButton(key);
                btn.setFont(new Font("Arial", Font.BOLD, 20));
                btn.setFocusPainted(false);
                
                if (key.equals("<- Xóa")) { 
                    btn.setPreferredSize(new Dimension(100, 55));
                    btn.setBackground(new Color(220, 53, 69));
                    btn.setForeground(Color.WHITE);
                    btn.addActionListener(e -> {
                        String text = txtInput.getText();
                        if (text.length() > 0) txtInput.setText(text.substring(0, text.length() - 1));
                    });
                } 
                else if (key.equals("CAPS")) {
                    btn.setPreferredSize(new Dimension(100, 55));
                    btn.setBackground(new Color(100, 149, 237)); 
                    btn.setForeground(Color.WHITE);
                    btn.addActionListener(e -> toggleCaps());
                }
                else if (key.equals("Xong")) {
                    btn.setPreferredSize(new Dimension(100, 55));
                    btn.setBackground(new Color(40, 167, 69));
                    btn.setForeground(Color.WHITE);
                    btn.addActionListener(e -> { 
                        result = txtInput.getText(); 
                        dispose(); 
                    });
                }
                else {
                    btn.setPreferredSize(new Dimension(60, 55));
                    btn.setBackground(new Color(240, 240, 240));
                    if (key.matches("[a-zA-Z]")) letterButtons.add(btn);
                    btn.addActionListener(e -> processTelex(btn.getText()));
                }
                rowPanel.add(btn);
            }
            keyboardPanel.add(rowPanel);
        }

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        bottomPanel.setOpaque(false);
        JButton btnSpace = new JButton("");
        btnSpace.setPreferredSize(new Dimension(450, 55));
        btnSpace.setFont(new Font("Arial", Font.BOLD, 18));
        btnSpace.addActionListener(e -> txtInput.append(" "));
        bottomPanel.add(btnSpace);
        keyboardPanel.add(bottomPanel);

        JPanel centerWrapper = new JPanel(new BorderLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.add(topPanel, BorderLayout.NORTH);
        centerWrapper.add(keyboardPanel, BorderLayout.CENTER);
        mainPanel.add(centerWrapper, BorderLayout.CENTER);

        JPanel overlayPanel = new JPanel(new GridBagLayout()); 
        overlayPanel.setOpaque(false);
        overlayPanel.add(mainPanel); 

        overlayPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                result = null; 
                dispose(); 
            }
        });

        mainPanel.addMouseListener(new MouseAdapter() {});

        setContentPane(overlayPanel);
        updateButtonLabels();
    }

    private void toggleCaps() {
        isCaps = !isCaps;
        updateButtonLabels();
    }

    private void updateButtonLabels() {
        for (JButton btn : letterButtons) {
            String text = btn.getText();
            btn.setText(isCaps ? text.toUpperCase() : text.toLowerCase());
        }
    }

    private void processTelex(String c) {
        String txt = txtInput.getText();
        if (txt.isEmpty()) {
            txtInput.append(c);
            return;
        }
        
        char lastChar = txt.charAt(txt.length() - 1);
        String combo = "" + lastChar + c.toLowerCase();
        char replace = 0;
        
        switch (combo) {
            case "aa": replace = 'â'; break; case "aw": replace = 'ă'; break;
            case "ee": replace = 'ê'; break; case "oo": replace = 'ô'; break;
            case "ow": replace = 'ơ'; break; case "uw": replace = 'ư'; break;
            case "w": replace = 'ư'; break;  case "dd": replace = 'đ'; break;
            
            case "as": replace = 'á'; break; case "af": replace = 'à'; break;
            case "ar": replace = 'ả'; break; case "ax": replace = 'ã'; break; case "aj": replace = 'ạ'; break;
            case "âs": replace = 'ấ'; break; case "âf": replace = 'ầ'; break;
            case "âr": replace = 'ẩ'; break; case "âx": replace = 'ẫ'; break; case "âj": replace = 'ậ'; break;
            case "ăs": replace = 'ắ'; break; case "ăf": replace = 'ằ'; break;
            case "ăr": replace = 'ẳ'; break; case "ăx": replace = 'ẵ'; break; case "ăj": replace = 'ặ'; break;
            
            case "es": replace = 'é'; break; case "ef": replace = 'è'; break;
            case "er": replace = 'ẻ'; break; case "ex": replace = 'ẽ'; break; case "ej": replace = 'ẹ'; break;
            case "ês": replace = 'ế'; break; case "êf": replace = 'ề'; break;
            case "êr": replace = 'ể'; break; case "êx": replace = 'ễ'; break; case "êj": replace = 'ệ'; break;
            
            case "is": replace = 'í'; break; case "if": replace = 'ì'; break;
            case "ir": replace = 'ỉ'; break; case "ix": replace = 'ĩ'; break; case "ij": replace = 'ị'; break;
            
            case "os": replace = 'ó'; break; case "of": replace = 'ò'; break;
            case "or": replace = 'ỏ'; break; case "ox": replace = 'õ'; break; case "oj": replace = 'ọ'; break;
            case "ôs": replace = 'ố'; break; case "ôf": replace = 'ồ'; break;
            case "ôr": replace = 'ổ'; break; case "ôx": replace = 'ỗ'; break; case "ôj": replace = 'ộ'; break;
            case "ơs": replace = 'ớ'; break; case "ơf": replace = 'ờ'; break;
            case "ơr": replace = 'ở'; break; case "ơx": replace = 'ỡ'; break; case "ơj": replace = 'ợ'; break;
            
            case "us": replace = 'ú'; break; case "uf": replace = 'ù'; break;
            case "ur": replace = 'ủ'; break; case "ux": replace = 'ũ'; break; case "uj": replace = 'ụ'; break;
            case "ưs": replace = 'ứ'; break; case "ưf": replace = 'ừ'; break;
            case "ưr": replace = 'ử'; break; case "ưx": replace = 'ữ'; break; case "ưj": replace = 'ự'; break;
            
            case "ys": replace = 'ý'; break; case "yf": replace = 'ỳ'; break;
            case "yr": replace = 'ỷ'; break; case "yx": replace = 'ỹ'; break; case "yj": replace = 'ỵ'; break;
        }

        if (replace != 0) {
            if (Character.isUpperCase(lastChar)) replace = Character.toUpperCase(replace);
            txtInput.setText(txt.substring(0, txt.length() - 1) + replace);
        } else {
            txtInput.append(c);
        }
    }

    public String getResult() {
        return result;
    }
}