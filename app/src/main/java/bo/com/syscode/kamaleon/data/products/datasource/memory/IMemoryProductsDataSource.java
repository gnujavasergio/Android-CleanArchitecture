package bo.com.syscode.kamaleon.data.products.datasource.memory;

import bo.com.syscode.kamaleon.products.domain.model.Product;
import bo.com.syscode.kamaleon.products.domain.criteria.ProductCriteria;

import java.util.List;

/**
 * Interfaz para fuente de datos en memoria
 */
public interface IMemoryProductsDataSource {
    List<Product> find(ProductCriteria criteria);

    void save(Product product);

    void deleteAll();

    boolean mapIsNull();
}
