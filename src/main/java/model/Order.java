package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Class used to represent an order.
 * An order has a customer and a list of products.
 */
public class Order {
    private int id;
    private Customer customer;
    private List<Product> products;


    public Order() {
        customer = null;
        products = new ArrayList<Product>() {
            @Override
            public String toString() {
                return getStringRepresentationSoldProducts();
            }
        };

        id = 0;
    }


    public void addProduct(Product product) {
        products.add(product);
    }

    public List<Product> getProducts() {
        return products;
    }

    /**
     * Calculates and returns the total order value.
     *
     * @return the total order value
     */
    public int getTotalValue() {
        if (products.size() == 0) return 0;
        return products.stream().mapToInt(x -> x.getQuantity() * x.getPrice()).sum();
    }

    /**
     * Makes a string representation for sold products.
     *
     * @return the string representation of sold products
     */
    public String getStringRepresentationSoldProducts() {
        if (products == null) return "";

        StringBuilder res = new StringBuilder();

        for (Product p : products) {
            res.append("  " + p.getName() + " --- " + p.getQuantity() + " * " + p.getPrice() + "\n");
        }

        return res.toString();
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder("");
        res.append("Order #" + id + "\n");
        res.append("Customer: " + customer.getName() + "\n\n");
        res.append("Products:\n\n");
        res.append(getStringRepresentationSoldProducts());
        res.append("\nTotal: " + getTotalValue());

        return res.toString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
