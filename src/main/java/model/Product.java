package model;

/**
 * Class used to represent a product.
 */
public class Product {
    private int id;
    private String name;
    private int quantity;
    private int price;
    private String manufacturer;

    public Product(int id, String name, int quantity, int price, String manufacturer) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.manufacturer = manufacturer;
    }

    public Product(int id, String name, int quantity, int price) {
        this(id, name, quantity, price, null);
    }

    public Product() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    @Override
    public String toString() {
        return "id=" + id +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", manufacturer='" + manufacturer + '\'';
    }
}
