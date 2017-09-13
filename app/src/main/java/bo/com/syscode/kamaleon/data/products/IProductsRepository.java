package bo.com.syscode.kamaleon.data.products;

import bo.com.syscode.kamaleon.products.domain.model.Product;
import bo.com.syscode.kamaleon.products.domain.criteria.ProductCriteria;

import java.util.List;

/**
 * Repositorio de productos
 */
public interface IProductsRepository {
    interface GetProductsCallback {

        void onProductsLoaded(List<Product> products);

        void onDataNotAvailable(String error);
    }

    void getProducts(GetProductsCallback callback, ProductCriteria criteria);

    void refreshProducts();
}
