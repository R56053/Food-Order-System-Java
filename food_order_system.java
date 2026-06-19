package food_order_systems;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

public class food_order_system extends JFrame {

    // Custom Neon Cyberpunk RGB Palette
    private static final Color COLOR_BG = new Color(18, 18, 20);         // Deep Onyx Black
    private static final Color COLOR_CARD = new Color(28, 28, 32);       // Dark Mode Cards
    private static final Color COLOR_TEXT = new Color(240, 240, 245);    // Ice White Text
    private static final Color COLOR_ACCENT = new Color(187, 134, 252);  // Neon Purple Accent
    private static final Color COLOR_GREEN = new Color(0, 230, 118);     // Electric Green
    private static final Color COLOR_RED = new Color(255, 23, 68);       // Cyber Red
    private static final Color COLOR_BLUE = new Color(0, 176, 255);      // Neon Blue
    private static final Color COLOR_RECEIPT_BG = new Color(10, 10, 12);  // Matte Black Screen

    private final String[] itemNames = {
        "Student Tiffin (Monthly)", "Office Lunch Plan (Weekly)", "Family Feast Bundle",
        "Chicken Tikka (Piece)", "Seekh Kabab (4 Pcs)", "Malai Boti Plate",
        "Zinger Burger", "Chicken Shami Burger", "Loaded Fries (Large)",
        "Extra Whole-Wheat Roti", "Premium Mint Raita Bowl", "Fresh Garden Salad",
        "Shahi Kheer Cup", "Suji Ka Halwa (Plate)", "Gulab Jamun (Pair)"
    };

    private final double[] itemPrices = {
        8500, 2800, 4500, 280, 420, 480, 350, 220, 290, 40, 90, 120, 180, 250, 160
    };

    private JCheckBox[] checkBoxes;
    private JTextField[] qtyFields;
    private JTextField txtSubTotal, txtDelivery, txtTotal;
    private JTextArea txtReceipt;

    public food_order_system() {
        setTitle("Pakistani Cloud Kitchen & Delivery Subscription System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 800);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        checkBoxes = new JCheckBox[itemNames.length];
        qtyFields = new JTextField[itemNames.length];

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(COLOR_BG);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel menuPanel = new JPanel(new GridLayout(3, 2, 15, 15));
        menuPanel.setBackground(COLOR_BG);
        
        menuPanel.add(createCategoryPanel("Meal Plan Subscriptions", 0, 2));
        menuPanel.add(createCategoryPanel("BBQ Grill Station", 3, 5)); 
        menuPanel.add(createCategoryPanel("Fast Food Corners", 6, 8));
        menuPanel.add(createCategoryPanel("Meal Add-ons & Sides", 9, 11));
        menuPanel.add(createCategoryPanel("Traditional Sweet Treats", 12, 14));
        
        JPanel emptyPanel = new JPanel();
        emptyPanel.setBackground(COLOR_BG);
        menuPanel.add(emptyPanel);

        JPanel billingPanel = new JPanel(new BorderLayout(15, 15));
        billingPanel.setBackground(COLOR_BG);
        billingPanel.setPreferredSize(new Dimension(420, 0));

        txtReceipt = new JTextArea();
        txtReceipt.setFont(new Font("Monospaced", Font.BOLD, 13));
        txtReceipt.setBackground(COLOR_RECEIPT_BG);
        txtReceipt.setForeground(COLOR_GREEN); 
        txtReceipt.setEditable(false);
        txtReceipt.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        JScrollPane receiptScroll = new JScrollPane(txtReceipt);
        receiptScroll.setBorder(BorderFactory.createLineBorder(COLOR_CARD, 2));
        billingPanel.add(receiptScroll, BorderLayout.CENTER);

        JPanel calcPanel = new JPanel(new GridBagLayout());
        calcPanel.setBackground(COLOR_CARD);
        calcPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(COLOR_ACCENT), "Subscription Cost Matrix", 
                TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 13), COLOR_ACCENT));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 10, 8, 10);

        txtSubTotal = createReadOnlyField(calcPanel, "Plan SubTotal:", 0, gbc);
        txtDelivery = createReadOnlyField(calcPanel, "Logistics / Delivery fee:", 1, gbc);
        txtTotal = createReadOnlyField(calcPanel, "Grand Total:", 2, gbc);

        JPanel btnPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        btnPanel.setBackground(COLOR_BG);
        
        JButton btnTotal = createStyledButton("Generate Bill", COLOR_GREEN);
        JButton btnUpdate = createStyledButton("Update Tariffs", COLOR_BLUE);
        JButton btnReset = createStyledButton("Clear Fields", COLOR_ACCENT);
        JButton btnExit = createStyledButton("Exit Portal", COLOR_RED);

        btnTotal.addActionListener(this::calculateTotal);
        btnUpdate.addActionListener(this::updateItemPrice);
        btnReset.addActionListener(this::resetForm);
        btnExit.addActionListener(e -> verifyExit());

        btnPanel.add(btnTotal);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnReset);
        btnPanel.add(btnExit);

        JPanel southPanel = new JPanel(new BorderLayout(10, 10));
        southPanel.setBackground(COLOR_BG);
        southPanel.add(calcPanel, BorderLayout.CENTER);
        southPanel.add(btnPanel, BorderLayout.SOUTH);

        billingPanel.add(southPanel, BorderLayout.SOUTH);
        mainPanel.add(menuPanel, BorderLayout.CENTER);
        mainPanel.add(billingPanel, BorderLayout.EAST);
        add(mainPanel);
    }

    private JPanel createCategoryPanel(String title, int startIdx, int endIdx) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COLOR_CARD);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(COLOR_BG, 1), title, 
                TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 14), COLOR_ACCENT));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;
        for (int i = startIdx; i <= endIdx; i++) {
            checkBoxes[i] = new JCheckBox(itemNames[i] + " - Rs." + (int)itemPrices[i]);
            checkBoxes[i].setBackground(COLOR_CARD);
            checkBoxes[i].setForeground(COLOR_TEXT);
            checkBoxes[i].setFont(new Font("Arial", Font.PLAIN, 12)); 

            qtyFields[i] = new JTextField("0", 4);
            qtyFields[i].setHorizontalAlignment(JTextField.CENTER);
            qtyFields[i].setEnabled(false);
            qtyFields[i].setBackground(COLOR_BG);
            qtyFields[i].setForeground(COLOR_TEXT);
            qtyFields[i].setCaretColor(COLOR_TEXT);
            qtyFields[i].setBorder(BorderFactory.createLineBorder(COLOR_BG));

            final int index = i;
            checkBoxes[i].addActionListener(e -> {
                boolean selected = checkBoxes[index].isSelected();
                qtyFields[index].setEnabled(selected);
                qtyFields[index].setText(selected ? "1" : "0");
                qtyFields[index].setBackground(selected ? Color.WHITE : COLOR_BG);
                qtyFields[index].setForeground(selected ? Color.BLACK : COLOR_TEXT);
            });

            gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 1.0;
            panel.add(checkBoxes[i], gbc);
            gbc.gridx = 1; gbc.weightx = 0.0;
            panel.add(qtyFields[i], gbc);
            row++;
        }
        return panel;
    }

    private JTextField createReadOnlyField(JPanel container, String labelText, int row, GridBagConstraints gbc) {
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.3;
        JLabel lbl = new JLabel(labelText);
        lbl.setForeground(COLOR_TEXT);
        container.add(lbl, gbc);

        JTextField field = new JTextField("PKR 0.00", 12);
        field.setEditable(false);
        field.setBackground(COLOR_BG);
        field.setForeground(COLOR_GREEN);
        field.setFont(new Font("Arial", Font.BOLD, 12));
        field.setHorizontalAlignment(JTextField.RIGHT);
        field.setBorder(BorderFactory.createLineBorder(COLOR_BG));
        
        gbc.gridx = 1; gbc.weightx = 0.7;
        container.add(field, gbc);
        return field;
    }

    private static JButton createStyledButton(String text, Color bgRGB) {
        JButton btn = new JButton(text);
        btn.setBackground(bgRGB);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        return btn;
    }

    private void updateItemPrice(ActionEvent evt) {
        String selectedItem = (String) JOptionPane.showInputDialog(this, "Select an item to modify:", "Update Plan Rates", 
                JOptionPane.QUESTION_MESSAGE, null, itemNames, itemNames[0]);
        if (selectedItem != null) {
            for (int i = 0; i < itemNames.length; i++) {
                if (itemNames[i].equals(selectedItem)) {
                    String input = JOptionPane.showInputDialog(this, "New rate for " + selectedItem + ":");
                    try {
                        double newPrice = Double.parseDouble(input);
                        itemPrices[i] = newPrice;
                        checkBoxes[i].setText(itemNames[i] + " - Rs." + (int)newPrice);
                    } catch (Exception e) { JOptionPane.showMessageDialog(this, "Invalid Input"); }
                    break;
                }
            }
        }
    }

    private void calculateTotal(ActionEvent evt) {
        double subTotal = 0;
        StringBuilder sb = new StringBuilder("PAKISTAN KITCHEN LOGISTICS\n==========================================\n");
        for (int i = 0; i < itemNames.length; i++) {
            if (checkBoxes[i].isSelected()) {
                int qty = Integer.parseInt(qtyFields[i].getText());
                double cost = qty * itemPrices[i];
                subTotal += cost;
                sb.append(String.format("%-24s %-4d Rs.%.2f\n", itemNames[i], qty, cost));
            }
        }
        double total = subTotal + (subTotal > 0 ? 250 : 0);
        txtSubTotal.setText("PKR " + subTotal);
        txtTotal.setText("PKR " + total);
        txtReceipt.setText(sb.toString() + "==========================================\nGrand Total: PKR " + total);
    }

    private void resetForm(ActionEvent evt) {
        for (int i = 0; i < itemNames.length; i++) {
            checkBoxes[i].setSelected(false);
            qtyFields[i].setText("0");
            qtyFields[i].setEnabled(false);
        }
        txtSubTotal.setText("PKR 0.00");
        txtTotal.setText("PKR 0.00");
        txtReceipt.setText("");
    }

    private void verifyExit() {
        if (JOptionPane.showConfirmDialog(this, "Exit system?") == JOptionPane.YES_OPTION) System.exit(0);
    }

    private static class LoginDialog extends JDialog {
        private final JTextField txtUser;
        private final JPasswordField txtPass;
        private boolean authenticated = false;

        public LoginDialog(Frame parent) {
            super(parent, "System Access", true);
            setLayout(new FlowLayout());
            txtUser = new JTextField(12);
            txtPass = new JPasswordField(12);
            JButton btnLogin = new JButton("Login");
            btnLogin.addActionListener(e -> {
                if (txtUser.getText().equals("admin") && new String(txtPass.getPassword()).equals("1234")) {
                    authenticated = true;
                    dispose();
                } else JOptionPane.showMessageDialog(this, "Denied");
            });
            add(new JLabel("User:")); add(txtUser);
            add(new JLabel("Pass:")); add(txtPass);
            add(btnLogin);
            pack();
            setLocationRelativeTo(parent);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            food_order_system frame = new food_order_system();
            LoginDialog login = new LoginDialog(frame);
            login.setVisible(true);
            if (login.authenticated) frame.setVisible(true);
        });
    }
}