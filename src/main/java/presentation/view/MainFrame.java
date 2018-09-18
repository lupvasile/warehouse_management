package presentation.view;

import business.CustomerBusiness;
import business.OrderBusiness;
import business.ProductBusiness;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Main JFrame, containing the buttons used to open customer, product and order windows.
 *
 * @author Vasile Lup
 */
public class MainFrame extends JFrame {

    private GeneralCRUDPanel customerPanel;
    private GeneralCRUDPanel productPanel;
    private GeneralCRUDPanel orderPanel;
    private JFrame customerFrame, productFrame, orderFrame;
    private JButton customerButton;
    private JButton productButton;
    private JButton orderButton;

    public MainFrame() {
        increaseUIFont(3);
        UIManager.put("Table.font", new Font("Dialog", Font.PLAIN, 13));

        setTitle("Order Management");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocation(400, 300);
        //setMinimumSize(new Dimension(600, 500));

        customerButton = new JButton("Manage customers");
        productButton = new JButton("Manage products");
        orderButton = new JButton("Manage orders");


        setLayout(new FlowLayout());
        add(customerButton);
        add(productButton);
        add(orderButton);

        customerPanel = new GeneralCRUDPanel<>(new CustomerBusiness());
        productPanel = new GeneralCRUDPanel<>(new ProductBusiness());
        orderPanel = new OrderCRUDPanel(new OrderBusiness(), this);

        customerFrame = new FramePanel(customerPanel, "Customer manager", 200, 400, false);
        productFrame = new FramePanel(productPanel, "Product manager", 700, 400, false);
        orderFrame = new FramePanel(orderPanel, "Order manager", 700, 400, false);

        pack();
    }

    /**
     * Increases the font for all the UI components with the given parameter.
     *
     * @param increase the size with which all the component font size is increased
     */
    public static void increaseUIFont(int increase) {
        java.util.Enumeration keys = UIManager.getDefaults().keys();//get all standard font keys
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();//get the key ex: Table.font
            Object value = UIManager.get(key);//get current font for key
            if (value instanceof javax.swing.plaf.FontUIResource)//check if key is a font resource
            {
                //increase the font
                UIManager.put(key, new javax.swing.plaf.FontUIResource(((FontUIResource) value).getFontName(), Font.PLAIN, ((FontUIResource) value).getSize() + increase));
            }
        }
    }

    public void updateAllFrames() {
        customerPanel.updatePanel();
        productPanel.updatePanel();
        orderPanel.updatePanel();
    }

    public void setCustomerFrameVisibility(boolean visibility) {
        customerFrame.setVisible(visibility);
    }

    public void setOrderFrameVisibility(boolean visibility) {
        orderFrame.dispose();
        orderPanel = new OrderCRUDPanel(new OrderBusiness(), this);
        orderFrame = new FramePanel(orderPanel, "Order manager", 700, 400, false);
        orderFrame.setVisible(visibility);
    }

    public void setProductFrameVisibility(boolean visibility) {
        productFrame.setVisible(visibility);
    }

    public void addListeners(ActionListener customerButtonListener, ActionListener productButtonListener, ActionListener orderButtonListener) {
        customerButton.addActionListener(customerButtonListener);
        productButton.addActionListener(productButtonListener);
        orderButton.addActionListener(orderButtonListener);
    }

    public void showError(String errorMsg) {
        JOptionPane.showMessageDialog(this, errorMsg, "ERROR", JOptionPane.ERROR_MESSAGE);
    }

}
