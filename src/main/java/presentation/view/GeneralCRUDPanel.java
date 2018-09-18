package presentation.view;

import business.BusinessClass;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * A generic JPanel with 5 tabs: create, view, update, delete, filter.
 * It generates the panels by using java reflection framework.
 * Uses a businessClass for getting the data.
 *
 * @author Vasile Lup
 * @see BusinessClass
 */
public class GeneralCRUDPanel<T> extends JPanel {

    protected BusinessClass<T> businessClass;
    protected Class<T> type;
    protected JPanel createPanel;
    protected JPanel readPanel;
    protected JPanel updatePanel;
    protected JPanel deletePanel;
    protected JPanel filterPanel;

    private boolean withUpdate;
    private boolean withDelete;
    private boolean withFilter;

    public GeneralCRUDPanel(BusinessClass<T> businessClass, boolean withUpdate, boolean withDelete, boolean withFilter) {
        this.businessClass = businessClass;
        this.type = businessClass.getType();
        this.withUpdate = withUpdate;
        this.withDelete = withDelete;
        this.withFilter = withFilter;

        createPanel = new JPanel();
        readPanel = new JPanel();
        updatePanel = new JPanel();
        deletePanel = new JPanel();
        filterPanel = new JPanel();

        updatePanel();

        JTabbedPane p = new JTabbedPane();

        p.addTab("Create", createPanel);
        p.addTab("View", readPanel);
        if (withUpdate) p.addTab("Update", updatePanel);
        if (withDelete) p.addTab("Delete", deletePanel);
        if (withFilter) p.addTab("Filter", filterPanel);

        setLayout(new BorderLayout());
        add(p, BorderLayout.CENTER);
    }

    public GeneralCRUDPanel(BusinessClass<T> businessClass) {
        this(businessClass, true, true, true);
    }

    /**
     * Updates all panels with data from the business class.
     */
    protected void updatePanel() {

        makeCreatePanel();
        makeReadPanel();
        if (withDelete) makeDeletePanel();
        if (withUpdate) makeUpdatePanel();
        if (withFilter) makeFilterPanel();

        revalidate();
        repaint();
    }

    protected void makeCreatePanel() {
        createPanel.removeAll();
        createPanel.setLayout(new GridLayout(0, 2));

        List<String> fieldNames = getFieldNames();
        List<JTextField> fieldContents = new ArrayList<>(fieldNames.size());


        fieldNames.forEach(x -> {
            if (x.equals("id")) return;
            createPanel.add(new JLabel(x));
            fieldContents.add(new JTextField());
            createPanel.add(fieldContents.get(fieldContents.size() - 1));
        });

        JButton button = new JButton("Add " + type.getSimpleName());
        createPanel.add(new JLabel(""));
        createPanel.add(button);

        button.addActionListener(x -> {

            int i = -1;
            try {
                T instance = type.newInstance();
                for (Field field : type.getDeclaredFields()) {

                    if (field.getName().equals("id")) continue;
                    ++i;
                    Object value = fieldContents.get(i).getText();
                    if (field.getType().getSimpleName().equals("int"))
                        value = Integer.parseInt(fieldContents.get(i).getText());

                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), type);
                    Method method = propertyDescriptor.getWriteMethod();
                    method.invoke(instance, value);
                }

                businessClass.addNew(instance);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid integer input", "ERROR", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                e.printStackTrace();
            }

            updatePanel();
        });

        createPanel.revalidate();
        createPanel.repaint();
    }

    protected void makeReadPanel() {
        readPanel.removeAll();
        readPanel.setLayout(new BorderLayout());

        JTable table = createTable(businessClass.findAll());
        table.setEnabled(false);
        readPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        readPanel.revalidate();
        readPanel.repaint();
    }

    protected void makeFilterPanel() {
        filterPanel.removeAll();

        JComboBox fieldComboBox = new JComboBox<>(getFieldNames().toArray(new String[0]));
        JComboBox operatorComboBox = new JComboBox<>(new String[]{"=", "<", ">", "!=", "<=", ">="});

        JButton button = new JButton("Filter");
        JTextField text = new JTextField(15);
        JTable table = new JTable();

        button.addActionListener(x -> {
            table.setModel(createTable(businessClass.findByField((String) fieldComboBox.getSelectedItem(), (String) operatorComboBox.getSelectedItem(), text.getText())).getModel());
            filterPanel.add(new JLabel("Total: " + table.getRowCount()), BorderLayout.SOUTH);
            filterPanel.revalidate();
            filterPanel.repaint();
        });

        JPanel p = new JPanel();
        p.setLayout(new FlowLayout());
        p.add(new JLabel("Field: "));
        p.add(fieldComboBox);
        p.add(operatorComboBox);
        p.add(text);
        p.add(button);

        filterPanel.setLayout(new BorderLayout());
        filterPanel.add(p, BorderLayout.NORTH);
        filterPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        filterPanel.revalidate();
        filterPanel.repaint();
    }

    protected void makeDeletePanel() {
        deletePanel.removeAll();
        deletePanel.setLayout(new BorderLayout());

        JTable table = createTable(businessClass.findAll());
        table.setEnabled(true);

        JButton button = new JButton("Delete");
        deletePanel.add(button, BorderLayout.NORTH);
        deletePanel.add(new JScrollPane(table), BorderLayout.CENTER);

        button.addActionListener(x -> {
            if (table.getSelectedRow() < 0) return;
            businessClass.deleteById(Integer.parseInt((String) table.getModel().getValueAt(table.getSelectedRow(), 0)));
            updatePanel();
        });

        deletePanel.revalidate();
        deletePanel.repaint();
    }

    protected void makeUpdatePanel() {
        updatePanel.removeAll();
        updatePanel.setLayout(new BorderLayout());

        JTable table = createTable(businessClass.findAll());
        table.setEnabled(true);

        JButton button = new JButton("Update");
        updatePanel.add(button, BorderLayout.NORTH);
        updatePanel.add(new JScrollPane(table), BorderLayout.CENTER);

        button.addActionListener(x -> {
            for (int k = 0; k < table.getModel().getRowCount(); ++k) {
                try {
                    int i = -1;
                    T instance = type.newInstance();
                    for (Field field : type.getDeclaredFields()) {
                        ++i;
                        Object value = table.getModel().getValueAt(k, i).toString();
                        if (field.getType().getSimpleName().equals("int")) value = Integer.parseInt((String) value);

                        PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), type);
                        Method method = propertyDescriptor.getWriteMethod();
                        method.invoke(instance, value);
                    }

                    businessClass.update(instance);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            updatePanel();
        });

        updatePanel.revalidate();
        updatePanel.repaint();
    }

    /**
     * @return a list with all the field names of an object of type T
     */
    protected List<String> getFieldNames() {
        List<String> res = new ArrayList<>();

        for (Field field : type.getDeclaredFields()) {
            res.add(field.getName());
        }

        return res;
    }

    /**
     * Return a table populated with objects from the objects parameter.
     * The header of the table is generated using reflection framework.
     * @param objects the objects to be represented
     * @return a JTable populated with the objects
     */
    protected JTable createTable(List<T> objects) {
        if (objects == null || objects.size() == 0) return new JTable();//empty list

        List<String> columNames = new ArrayList<>();
        List<List<String>> tableContents = new ArrayList<>();

        for (Field field : objects.get(0).getClass().getDeclaredFields()) {
            columNames.add(field.getName());//make the table header
        }

        for (Object obj : objects) {
            List<String> curr = new ArrayList<>();

            for (Field field : obj.getClass().getDeclaredFields()) {
                field.setAccessible(true);//read private fields also
                try {
                    curr.add(field.get(obj).toString());//get field content
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            tableContents.add(curr);
        }

        String[][] tableContentsArr = new String[tableContents.size()][columNames.size()];
        String[] columnNamesArr = new String[columNames.size()];

        for (int i = 0; i < columnNamesArr.length; ++i) columnNamesArr[i] = columNames.get(i);
        for (int i = 0; i < tableContentsArr.length; ++i)
            for (int j = 0; j < tableContentsArr[i].length; ++j)
                tableContentsArr[i][j] = tableContents.get(i).get(j);//transform tableContents to an array


        return new JTable(tableContentsArr, columnNamesArr);//return the JTable
    }
}
