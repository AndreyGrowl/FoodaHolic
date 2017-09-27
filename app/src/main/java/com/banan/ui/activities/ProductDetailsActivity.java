package com.banan.ui.activities;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.banan.R;
import com.banan.api.ProductsApi;
import com.banan.model.entities.Product;
import com.banan.model.responses.ErrorResponse;
import com.banan.model.responses.ProductDetailResponse;
import com.banan.util.Constants;
import com.banan.util.PicassoCache;
import com.squareup.picasso.Callback;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductDetailsActivity extends BaseActivity {

    private Product product;

    @BindView(R.id.image_product)
    ImageView imageProduct;

    @BindView(R.id.tv_price)
    TextView textViewPrice;

    @BindView(R.id.tv_name)
    TextView textViewName;

    @BindView(R.id.tv_description)
    TextView textViewDescription;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private boolean isProductLoaded = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        fillViews();

        if (isNetworkConnected()) {
            ProductsApi.INSTANCE.getProductById(product.getProductId());
        } else {
            hideProgress();
        }
    }

    protected void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    @Subscribe
    public void onEventError(ErrorResponse errorResponse) {
        showAlert(errorResponse.getMessage(), getString(R.string.error_details));
        hideProgress();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Subscribe
    public void onEventProductDetails(ProductDetailResponse response) {
        product.setDescription(response.getProduct().getDescription());
        textViewDescription.setText(product.getDescription());
        productBox.put(product);

        isProductLoaded = true;
        hideProgress();
    }

    private void fillViews() {
        supportPostponeEnterTransition();
        Bundle extras = getIntent().getExtras();
        product = extras.getParcelable(Constants.EXTRA_ITEM);
        imageProduct.setTransitionName(extras.getString(Constants.EXTRA_TRANSITION_NAME_IMAGE));
        textViewName.setTransitionName(extras.getString(Constants.EXTRA_TRANSITION_NAME_NAME));
        textViewPrice.setTransitionName(extras.getString(Constants.EXTRA_TRANSITION_NAME_PRICE));

        textViewPrice.setText(product.getPrice());
        textViewName.setText(product.getName());
        textViewDescription.setText(product.getDescription());

        PicassoCache.getPicassoInstance(this).load(product.getImagePath()).noFade().into(imageProduct, new Callback() {
            @Override
            public void onSuccess() {
                supportStartPostponedEnterTransition();
            }

            @Override
            public void onError() {
                supportStartPostponedEnterTransition();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (isProductLoaded) {
            Intent intent = new Intent();
            intent.putExtra(Constants.EXTRA_ITEM, product);
            setResult(RESULT_OK, intent);

            //TODO handle activity result
        }
        super.onBackPressed();
    }
}
