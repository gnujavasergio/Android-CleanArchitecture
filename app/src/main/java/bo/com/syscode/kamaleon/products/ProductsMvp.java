package bo.com.syscode.kamaleon.products;

import bo.com.syscode.kamaleon.products.domain.model.Product;

import java.util.List;

/**
 * Creado por Hermosa Programación.
 */
public interface ProductsMvp {
    interface View {
        void showProducts(List<Product> products);

        void showLoadingState(boolean show);

        void showEmptyState();

        void showProductsError(String msg);

        void showProductsPage(List<Product> products);

        void showLoadMoreIndicator(boolean show);

        void allowMoreData(boolean show);
    }

    interface Presenter {
        void loadProducts(boolean reload);
    }
}
