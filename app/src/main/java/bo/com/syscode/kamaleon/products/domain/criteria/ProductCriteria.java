package bo.com.syscode.kamaleon.products.domain.criteria;

import bo.com.syscode.kamaleon.products.domain.model.Product;

import java.util.List;

/**
 * Patrón de especificación para los productos
 */
public interface ProductCriteria {
    List<Product> match(List<Product> products);
}
