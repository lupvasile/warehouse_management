package dataaccess;

import model.Order;
import model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Access data from database for an object of type Order.
 *
 * @see GenericDataAccess
 * @see Order
 */
public class OrderDataAccess extends GenericDataAccess<Order> {

    private Connection connection;

    @Override
    public Order insert(Order order) {

        try {
            makeUpdate("INSERT INTO `order` (`idCustomer`) VALUES ('" + order.getCustomer().getId() + "')");
            connection = ConnectionFactory.getConnection();

            ResultSet rs = executeSelectStatement("SELECT id from `order` as a left join orderitems on a.id=orderitems.orderId where orderId is NULL");
            rs.next();
            order.setId(rs.getInt(1));

            for (Product p : order.getProducts()) {
                makeUpdate("INSERT INTO `orderitems` (`orderId`, `productId`, `quantity`) VALUES ('" + order.getId() + "', '" + p.getId() + "', '" + p.getQuantity() + "')");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.close(connection);
        }

        return order;
    }

    @Override
    public List<Order> findAll() {
        List<Order> list = new ArrayList<>();

        connection = ConnectionFactory.getConnection();

        ResultSet rs = executeSelectStatement("SELECT DISTINCT id FROM `" + type.getSimpleName() + "`");

        try {
            List<Integer> orderIds = new ArrayList<>();
            while (rs.next())
                orderIds.add(rs.getInt(1));

            for (int id : orderIds) {
                list.add(findById(id));
            }

            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.close(connection);
        }

        return null;
    }

    @Override
    public Order findById(int id) {
        Order order = new Order();
        order.setId(id);

        try {
            if (connection.isClosed()) connection = ConnectionFactory.getConnection();

            ResultSet rs = executeSelectStatement("SELECT idCustomer FROM `" + type.getSimpleName() + "` WHERE id=" + id);
            rs.next();
            int idCustomer = rs.getInt(1);

            order.setCustomer(new CustomerDataAccess().findById(idCustomer));

            rs = executeSelectStatement("SELECT * FROM orderItems where orderId=" + id);
            while (rs.next()) {
                int idProduct = rs.getInt("productId");
                int quantity = rs.getInt("quantity");
                Product p = new ProductDataAccess().findById(idProduct);
                p.setQuantity(quantity);

                order.addProduct(p);
            }

            return order;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return order;
    }

    private ResultSet executeSelectStatement(String query) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            return resultSet;
        } catch (SQLException e) {
            //LOGGER.log(Level.WARNING, type.getName() + "DAO:findAll " + e.getMessage());
        }

        return null;
    }
}
