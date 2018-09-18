package business;

import dataaccess.OrderDataAccess;
import model.Order;
import model.Product;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Class representing business logic for a Order.
 *
 * @see BusinessClass
 * @see Order
 */
public class OrderBusiness extends BusinessClass<Order> {

    public OrderBusiness() {
        super(Order.class, new OrderDataAccess());
    }

    /**
     * Adds new order to the database
     *
     * @param newOrder the order to be added
     * @return the added order
     */
    @Override
    public Order addNew(Order newOrder) {
        ProductBusiness productBusiness = new ProductBusiness();

        for (Product p : newOrder.getProducts()) {
            Product currDBProd = productBusiness.findById(p.getId());//get the product from database
            currDBProd.setQuantity(currDBProd.getQuantity() - p.getQuantity());//set product new quantity
            productBusiness.update(currDBProd);//update product new quantity
        }

        dataAccess.insert(newOrder);//add the order in the database

        try {
            File file = new File("bills/Order #" + newOrder.getId() + ".txt");//generate bill file
            file.getParentFile().mkdirs();//generate bills directory if not existent
            Files.write(Paths.get(file.getPath()), newOrder.toString().getBytes());//write to file
        } catch (IOException e) {
            e.printStackTrace();
        }

        return newOrder;
    }
}
