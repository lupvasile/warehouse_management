package business;

import dataaccess.ProductDataAccess;
import model.Product;

/**
 * Class representing business logic for a Product.
 *
 * @see BusinessClass
 * @see Product
 */
public class ProductBusiness extends BusinessClass<Product> {

    public ProductBusiness() {
        super(Product.class, new ProductDataAccess());
    }

}
