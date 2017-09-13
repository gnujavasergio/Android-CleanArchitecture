package bo.com.syscode.kamaleon.data.products.datasource.memory;

import com.google.common.collect.Lists;
import bo.com.syscode.kamaleon.products.domain.model.Product;
import bo.com.syscode.kamaleon.products.domain.criteria.ProductCriteria;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Implementación concreta de la fuente de datos en memoria
 */
public class MemoryProductsDataSource implements IMemoryProductsDataSource {
    private static HashMap<String, Product> mCachedProducts;

    @Override
    public List<Product> find(ProductCriteria criteria) {

        ArrayList<Product> products =
                Lists.newArrayList(mCachedProducts.values());
        return criteria.match(products);
    }

    @Override
    public void save(Product product) {
        if (mCachedProducts == null) {
            mCachedProducts = new LinkedHashMap<>();
        }
        mCachedProducts.put(product.getCode(), product);
    }


    @Override
    public void deleteAll() {
        if (mCachedProducts == null) {
            mCachedProducts = new LinkedHashMap<>();
        }
        mCachedProducts.clear();
    }


    @Override
    public boolean mapIsNull() {
        return mCachedProducts == null;
    }
}
