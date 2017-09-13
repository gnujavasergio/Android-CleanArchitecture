package bo.com.syscode.kamaleon.data.products.datasource.cloud;

import android.os.Handler;
import com.google.common.collect.Lists;

import bo.com.syscode.kamaleon.products.domain.criteria.ProductCriteria;
import bo.com.syscode.kamaleon.products.domain.model.Product;

import java.util.HashMap;
import java.util.LinkedHashMap;


/**
 * Fuente de datos relacionada al servidor remoto
 */
public class CloudProductsDataSource implements ICloudProductsDataSource {

    private static final long LATENCY = 2000;

    private static HashMap<String, Product> API_DATA;
    static {
        API_DATA = new LinkedHashMap<>();
        for (int i = 0; i < 100; i++) {
            addProduct(43, "Producto " + (i + 1),
                    "file:///android_asset/mock-product.png");
        }
    }

    private static void addProduct(float price, String name, String imageUrl) {
        Product newProduct = new Product(price, name, imageUrl);
        API_DATA.put(newProduct.getCode(), newProduct);
    }

    public CloudProductsDataSource() {

    }


    @Override
    public void getProducts(final ProductServiceCallback callback,
                            ProductCriteria criteria) {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                callback.onLoaded(Lists.newArrayList(API_DATA.values()));
            }
        }, LATENCY);
    }

}
