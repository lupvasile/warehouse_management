package model;

/**
 * Class used to represent a Customer.
 *
 * @author Vasile Lup
 */
public class Customer {

    private int id;
    private String name;
    private int age;
    private String email;
    private String phone;

    public Customer(int id, String name, int age, String email, String phone) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.email = email;
        this.phone = phone;
    }

    /**
     * Short constructor of the class Customer.
     * Age is set as 0, phone number is null.
     *
     * @param id    the id of the customer
     * @param name  name of the customer
     * @param email the customer's email
     */
    public Customer(int id, String name, String email) {
        this(id, name, 0, email, null);
    }

    public Customer() {
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "id=" + id +
                "\n name='" + name + '\'' +
                "\n age=" + age +
                "\n email='" + email + '\'' +
                "\n phone='" + phone + '\'';
    }
}
