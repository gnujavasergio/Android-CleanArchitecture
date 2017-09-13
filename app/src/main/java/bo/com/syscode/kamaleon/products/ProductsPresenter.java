package bo.com.syscode.kamaleon.products;

import bo.com.syscode.kamaleon.products.domain.model.Product;
import bo.com.syscode.kamaleon.data.products.ProductsRepository;
import bo.com.syscode.kamaleon.products.domain.criteria.PagingProductCriteria;
import bo.com.syscode.kamaleon.products.domain.criteria.ProductCriteria;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Presentador que escucha los eventos de la UI y luego presenta los resultados a la vista
 */
public class ProductsPresenter implements ProductsMvp.Presenter {

    private final ProductsRepository mProductsRepository;
    private final ProductsMvp.View mProductsView;

    public static final int PRODUCTS_LIMIT = 20;

    private boolean isFirstLoad = true;
    private int mCurrentPage = 1;

    public ProductsPresenter(ProductsRepository productsRepository,
                             ProductsMvp.View productsView) {
        mProductsRepository = checkNotNull(productsRepository);
        mProductsView = checkNotNull(productsView);
    }

    @Override
    public void loadProducts(final boolean reload) {
        final boolean reallyReload = reload || isFirstLoad;

        if (reallyReload) {
            mProductsView.showLoadingState(true);
            mProductsRepository.refreshProducts();
            mCurrentPage = 1;
        } else {
            mProductsView.showLoadMoreIndicator(true);
            mCurrentPage++;
        }


        // Ahora, preparamos el criterio de paginación
        ProductCriteria criteria = new PagingProductCriteria(mCurrentPage, PRODUCTS_LIMIT);

        mProductsRepository.getProducts(
                new ProductsRepository.GetProductsCallback() {
                    @Override
                    public void onProductsLoaded(List<Product> products) {
                        mProductsView.showLoadingState(false);
                        processProducts(products, reallyReload);

                        // Ahora si, ya no es el primer refresco
                        isFirstLoad = false;
                    }

                    @Override
                    public void onDataNotAvailable(String error) {
                        mProductsView.showLoadingState(false);
                        mProductsView.showLoadMoreIndicator(false);
                        mProductsView.showProductsError(error);
                    }
                },
                criteria);

    }

    private void processProducts(List<Product> products, boolean reload) {
        if (products.isEmpty()) {
            if (reload) {
                mProductsView.showEmptyState();
            } else {
                mProductsView.showLoadMoreIndicator(false);
            }
            mProductsView.allowMoreData(false);
        } else {

            if (reload) {
                mProductsView.showProducts(products);
            } else {
                mProductsView.showLoadMoreIndicator(false);
                mProductsView.showProductsPage(products);
            }

            mProductsView.allowMoreData(true);
        }
    }
}
