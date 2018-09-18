package business;

import dataaccess.CustomerDataAccess;
import model.Customer;

/**
 * Class representing business logic for a Customer.
 *
 * @see BusinessClass
 * @see Customer
 */
public class CustomerBusiness extends BusinessClass<Customer> {

    public CustomerBusiness(){
        super(Customer.class,new CustomerDataAccess());
    }

}
