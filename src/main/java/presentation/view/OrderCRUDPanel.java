package presentation.view;

import business.BusinessClass;
import business.CustomerBusiness;
import business.ProductBusiness;
import model.Customer;
import model.Order;
import model.Product;

import javax.swing.*;
import java.awt.*;

/**
 * GeneralCRUDPanel adapted for an order.
 *
 * @author Vasile Lup
 * @see GeneralCRUDPanel
 */
public class OrderCRUDPanel extends GeneralCRUDPanel<Order> {

    private MainFrame view;

    public OrderCRUDPanel(BusinessClass<Order> businessClass, MainFrame view) {
        super(businessClass, false, false, false);
        this.view = view;
    }

    @Override
    protected void makeCreatePanel() {
        createPanel.removeAll();
        createPanel.setLayout(new BorderLayout());

        JPanel customerPanel = new JPanel();
        customerPanel.setLayout(new FlowLayout());
        customerPanel.add(new JLabel("Customer: "));

        JComboBox customerComboBox = new JComboBox<>(new CustomerBusiness().findAll().toArray(new Customer[0]));
        customerPanel.add(customerComboBox);

        JPanel productPanel = new JPanel();
        productPanel.setLayout(new FlowLayout());
        productPanel.add(new JLabel("Product: "));

        JComboBox productComboBox = new JComboBox<>(new ProductBusiness().findAll().toArray(new Product[0]));
        productPanel.add(productComboBox);

        productPanel.add(new JLabel("Quantity: "));
        JTextField quantityTextField = new JTextField(10);
        productPanel.add(quantityTextField);

        JButton addProductButton = new JButton("Add");
        JButton doneOrderButton = new JButton("Done");
        JTextArea orderOutput = new JTextArea();

        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.add(customerPanel);
        p.add(productPanel);
        JPanel p3 = new JPanel(new FlowLayout());
        p3.add(addProductButton);
        p.add(p3);
        createPanel.add(p, BorderLayout.NORTH);
        createPanel.add(new JScrollPane(orderOutput), BorderLayout.CENTER);
        JPanel p2 = new JPanel(new FlowLayout());
        p2.add(doneOrderButton);
        createPanel.add(p2, BorderLayout.SOUTH);

        Order currOrder = new Order();

        addProductButton.addActionListener(x -> {
            Product prod = (Product) productComboBox.getSelectedItem();

            int quantity = 0;

            try {
                quantity = Integer.parseInt(quantityTextField.getText());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Bad quantity input", "ERROR", JOptionPane.ERROR_MESSAGE);
            }

            if (quantity > prod.getQuantity()) {
                JOptionPane.showMessageDialog(this, "Item is currently under stock");
            }

            prod.setQuantity(quantity);
            currOrder.addProduct(prod);

            currOrder.setCustomer((Customer) customerComboBox.getSelectedItem());
            orderOutput.setText(currOrder.toString());

            productComboBox.removeItem(prod);
        });

        doneOrderButton.addActionListener(x -> {
            if (currOrder.getProducts().size() <= 0) {
                JOptionPane.showMessageDialog(this, "Can not add empty order");
                return;
            }

            businessClass.addNew(currOrder);
            view.updateAllFrames();
        });

        createPanel.revalidate();
        createPanel.repaint();
    }

    @Override
    protected void makeReadPanel() {
        readPanel.removeAll();
        readPanel.setLayout(new BorderLayout());

        StringBuilder text = new StringBuilder();
        for (Order o : businessClass.findAll()) {
            text.append(o.toString());
            text.append("\n*******************************************\n");
        }

        JTextArea textArea = new JTextArea(text.toString());
        readPanel.add(new JScrollPane(textArea), BorderLayout.CENTER);

        readPanel.setPreferredSize(new Dimension(400, 400));
        readPanel.revalidate();
        readPanel.repaint();
    }
}
