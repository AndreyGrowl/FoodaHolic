package com.banan.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.banan.R;
import com.banan.api.ProductsApi;
import com.banan.model.entities.Product;
import com.banan.model.responses.ErrorResponse;
import com.banan.model.responses.ProductsResponse;
import com.banan.ui.adapters.ProductsAdapter;
import com.banan.util.Constants;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.objectbox.android.AndroidScheduler;

public class MainActivity extends BaseActivity implements ProductsAdapter.OnProductClickListener {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.tv_nothing)
    TextView textViewNothing;

    private ProductsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initList();
        loadData();
    }

    private void loadData() {
        productBox.query().build().subscribe().on(AndroidScheduler.mainThread()).observer(data -> {
            if (data.size() > 0) {
                hideProgress();
                adapter.addData(data);
            } else {
                ProductsApi.INSTANCE.getProducts();
            }
        });
    }

    private void initList() {
        adapter = new ProductsAdapter(new ArrayList<>(), this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onProductClicked(Product product, View itemView) {
        ImageView imageView = itemView.findViewById(R.id.image_product);
        TextView textViewName = itemView.findViewById(R.id.tv_name);
        TextView textViewPrice = itemView.findViewById(R.id.tv_price);

        String transitionImage = ViewCompat.getTransitionName(imageView);
        String transitionName = ViewCompat.getTransitionName(textViewName);
        String transitionPrice = ViewCompat.getTransitionName(textViewPrice);

        Intent intent = new Intent(this, ProductDetailsActivity.class);
        intent.putExtra(Constants.EXTRA_ITEM, product);
        intent.putExtra(Constants.EXTRA_TRANSITION_NAME_IMAGE, transitionImage);
        intent.putExtra(Constants.EXTRA_TRANSITION_NAME_NAME, transitionName);
        intent.putExtra(Constants.EXTRA_TRANSITION_NAME_PRICE, transitionPrice);

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                Pair.create(imageView, transitionImage),
                Pair.create(textViewName, transitionName),
                Pair.create(textViewPrice, transitionPrice));

        startActivity(intent, options.toBundle());
    }

    @Subscribe
    public void onEventProducts(ProductsResponse productsResponse) {
        hideProgress();
        adapter.addData(productsResponse.getProducts());
        productBox.put(productsResponse.getProducts());
    }

    @Subscribe
    public void onEventError(ErrorResponse errorResponse) {
        showAlert(getString(R.string.app_error), getString(R.string.error_details));
        hideProgress();
        showNothingToShowView();
    }

    @Override
    public void onDestroy() {
        //not sure if it's right way to control compositeSub
        ProductsApi.INSTANCE.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onStart() {
        ProductsApi.INSTANCE.onStart();
        super.onStart();
    }

    protected void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    protected void showNothingToShowView() {
        if (adapter.getItemCount() == 0) {
            textViewNothing.setVisibility(View.VISIBLE);
        }
    }
}
