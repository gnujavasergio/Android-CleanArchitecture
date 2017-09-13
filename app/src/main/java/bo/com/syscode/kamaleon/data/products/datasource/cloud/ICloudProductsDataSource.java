package bo.com.syscode.kamaleon.data.products.datasource.cloud;

import bo.com.syscode.kamaleon.products.domain.model.Product;
import bo.com.syscode.kamaleon.products.domain.criteria.ProductCriteria;

import java.util.List;

/**
 * Interfaz de comunicación con el repositorio para la fuente de datos remota
 */
public interface ICloudProductsDataSource {

    interface ProductServiceCallback {

        void onLoaded(List<Product> products);

        void onError(String error);

    }

    void getProducts(ProductServiceCallback callback, ProductCriteria criteria);

}
