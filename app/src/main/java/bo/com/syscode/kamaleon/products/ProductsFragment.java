package bo.com.syscode.kamaleon.products;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import bo.com.syscode.kamaleon.R;
import bo.com.syscode.kamaleon.di.DependencyProvider;
import bo.com.syscode.kamaleon.products.domain.model.Product;
import bo.com.syscode.kamaleon.products.ProductsAdapter.ProductItemListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragmento para mostrar la lista de productos
 */
public class ProductsFragment extends Fragment
        implements ProductsMvp.View {

    private RecyclerView mProductsList;
    private ProductsAdapter mProductsAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private View mEmptyView;
    private ProductItemListener mItemListener = new ProductItemListener() {
        @Override
        public void onProductClick(Product clickedProduct) {
            // Aquí lanzarías la pantalla de detalle del producto
        }
    };

    private ProductsPresenter mProductsPresenter;


    public ProductsFragment() {
        // Required empty public constructor
    }

    public static ProductsFragment newInstance() {
        return new ProductsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProductsAdapter = new ProductsAdapter(new ArrayList<Product>(0), mItemListener);
        mProductsPresenter = new ProductsPresenter(
                DependencyProvider.provideProductsRepository(getActivity()),
                this);

        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_products, container, false);

        // Referencias UI
        mProductsList = (RecyclerView) root.findViewById(R.id.products_list);
        mEmptyView = root.findViewById(R.id.noProducts);
        mSwipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.refresh_layout);

        // Setup
        setUpProductsList();
        setUptRefreshLayout();

        if (savedInstanceState != null) {
            hideList(false);
        }

        return root;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState == null) {
            mProductsPresenter.loadProducts(false);
        }
    }

    private void setUpProductsList() {
        mProductsList.setAdapter(mProductsAdapter);
        mProductsList.setHasFixedSize(true);

        final LinearLayoutManager layoutManager =
                (LinearLayoutManager) mProductsList.getLayoutManager();

        // Se agrega escucha de scroll infinito.
        mProductsList.addOnScrollListener(
                new InfinityScrollListener(mProductsAdapter, layoutManager) {
                    @Override
                    public void onLoadMore() {
                        mProductsPresenter.loadProducts(false);
                    }
                });
    }

    private void setUptRefreshLayout() {
        mSwipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mProductsPresenter.loadProducts(true);
            }
        });
    }


    @Override
    public void showProducts(List<Product> products) {

        mProductsAdapter.replaceData(products);

        hideList(false);
    }

    @Override
    public void showLoadingState(final boolean show) {
        if (getView() == null) {
            return;
        }

        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(show);
            }
        });
    }

    @Override
    public void showEmptyState() {
        hideList(true);
    }

    private void hideList(boolean hide) {
        mProductsList.setVisibility(hide ? View.GONE : View.VISIBLE);
        mEmptyView.setVisibility(hide ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showProductsError(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void showProductsPage(List<Product> products) {
        mProductsAdapter.addData(products);
        hideList(false);
    }

    @Override
    public void showLoadMoreIndicator(boolean show) {
        if (!show) {
            mProductsAdapter.dataFinishedLoading();
        } else {
            mProductsAdapter.dataStartedLoading();
        }
    }

    @Override
    public void allowMoreData(boolean allow) {
        mProductsAdapter.setMoreData(allow);
    }


}
