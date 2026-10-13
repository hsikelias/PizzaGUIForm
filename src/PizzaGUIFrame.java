
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class PizzaGUIFrame extends JFrame {

    private JRadioButton thin, regular, deepDish;
    private ButtonGroup crustGroup;
    private JComboBox<String> sizeBox;
    private JCheckBox[] toppings;
    private JTextArea receiptArea;

    private final String[] sizes = {"Small", "Medium", "Large", "Super"};
    private final double[] prices = {8.00, 12.00, 16.00, 20.00};
    private final String[] toppingNames = {
            "Pepperoni", "Mushrooms", "Extra Cheese",
            "Sausage", "Onions", "Pineapple"
    };

    public PizzaGUIFrame() {
        setTitle("Hot Pizza Order");
        setSize(550, 620);
        setLayout(new BorderLayout(10, 10));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        JPanel topPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        topPanel.add(createCrustPanel());
        topPanel.add(createSizePanel());

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(createToppingsPanel(), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.add(createReceiptPanel(), BorderLayout.CENTER);
        bottomPanel.add(createButtonPanel(), BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.NORTH);
        add(bottomPanel, BorderLayout.CENTER);

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                quitProgram();
            }
        });
    }

    private JPanel createCrustPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 1));
        panel.setBorder(new TitledBorder("Crust Type"));

        thin = new JRadioButton("Thin");
        regular = new JRadioButton("Regular");
        deepDish = new JRadioButton("Deep-dish");

        crustGroup = new ButtonGroup();
        crustGroup.add(thin);
        crustGroup.add(regular);
        crustGroup.add(deepDish);

        panel.add(thin);
        panel.add(regular);
        panel.add(deepDish);

        return panel;
    }

    private JPanel createSizePanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBorder(new TitledBorder("Pizza Size"));

        sizeBox = new JComboBox<>(sizes);
        sizeBox.setSelectedIndex(-1);
        panel.add(sizeBox);

        return panel;
    }

    private JPanel createToppingsPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.setBorder(new TitledBorder("Toppings ($1 each)"));

        toppings = new JCheckBox[toppingNames.length];

        for (int i = 0; i < toppingNames.length; i++) {
            toppings[i] = new JCheckBox(toppingNames[i]);
            panel.add(toppings[i]);
        }

        return panel;
    }

    private JPanel createReceiptPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new TitledBorder("Order Receipt"));

        receiptArea = new JTextArea(12, 40);
        receiptArea.setEditable(false);
        receiptArea.setFont(new Font("Monospaced", Font.PLAIN, 13));

        panel.add(new JScrollPane(receiptArea));
        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout());

        JButton order = new JButton("Order");
        JButton clear = new JButton("Clear");
        JButton quit = new JButton("Quit");

        order.addActionListener(e -> placeOrder());
        clear.addActionListener(e -> clearForm());
        quit.addActionListener(e -> quitProgram());

        panel.add(order);
        panel.add(clear);
        panel.add(quit);

        return panel;
    }

    private void placeOrder() {
        String crust = "";

        if (thin.isSelected()) crust = "Thin";
        else if (regular.isSelected()) crust = "Regular";
        else if (deepDish.isSelected()) crust = "Deep-dish";

        int sizeIndex = sizeBox.getSelectedIndex();

        if (crust.isEmpty() || sizeIndex == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a crust and pizza size.");
            return;
        }

        int toppingCount = 0;
        StringBuilder toppingList = new StringBuilder();

        for (JCheckBox topping : toppings) {
            if (topping.isSelected()) {
                toppingCount++;
                toppingList.append(String.format(
                        "%-25s $1.00%n", topping.getText()));
            }
        }

        if (toppingCount == 0) {
            JOptionPane.showMessageDialog(this,
                    "Please select at least one topping.");
            return;
        }

        double subtotal = prices[sizeIndex] + toppingCount;
        double tax = subtotal * 0.07;
        double total = subtotal + tax;

        StringBuilder receipt = new StringBuilder();
        receipt.append("================================\n");
        receipt.append("          PIZZA ORDER\n");
        receipt.append("================================\n");

        receipt.append(String.format("%-25s $%.2f%n",
                crust + " - " + sizes[sizeIndex], prices[sizeIndex]));

        receipt.append(toppingList);
        receipt.append("\n");

        receipt.append(String.format("%-25s $%.2f%n",
                "Sub-total:", subtotal));
        receipt.append(String.format("%-25s $%.2f%n",
                "Tax:", tax));

        receipt.append("--------------------------------\n");

        receipt.append(String.format("%-25s $%.2f%n",
                "Total:", total));

        receipt.append("================================\n");

        receiptArea.setText(receipt.toString());
    }

    private void clearForm() {
        crustGroup.clearSelection();
        sizeBox.setSelectedIndex(-1);

        for (JCheckBox topping : toppings) {
            topping.setSelected(false);
        }
        receiptArea.setText("");
    }

    private void quitProgram() {
        int answer = JOptionPane.showConfirmDialog(
                this,
                "sure you want to quit?",
                "Exit",
                JOptionPane.YES_NO_OPTION
        );
        if (answer == JOptionPane.YES_OPTION) {
            dispose();
        }
    }
}
