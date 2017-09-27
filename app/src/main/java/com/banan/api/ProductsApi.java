package com.banan.api;


import android.accounts.NetworkErrorException;

import com.banan.App;
import com.banan.R;
import com.banan.model.responses.ErrorResponse;
import com.banan.model.responses.ProductDetailResponse;
import com.banan.util.Constants;
import com.banan.util.DisposableInterface;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import org.greenrobot.eventbus.EventBus;

import java.net.UnknownHostException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public enum ProductsApi implements ProductsApiInterface, DisposableInterface {

    INSTANCE {
        private RequestsInterface api = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Constants.BASE_URL)
                .client(new OkHttpClient.Builder().build())
                .build().create(RequestsInterface.class);

        private CompositeDisposable compositeDisposable = new CompositeDisposable();

        @Override
        public void getProducts() {
            compositeDisposable.add(api.getProducts()
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(productsResponse -> EventBus.getDefault().post(productsResponse), this::handleError));
        }

        @Override
        public void getProductById(long id) {
            compositeDisposable.add(api.getProductById(id)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(product -> EventBus.getDefault().post(new ProductDetailResponse(product)), this::handleError));
        }

        private void handleError(Throwable throwable) {
            ErrorResponse errorResponse = new ErrorResponse();
            if (throwable instanceof UnknownHostException || throwable instanceof NetworkErrorException) {
                errorResponse.setMessage(App.getContext().getResources().getString(R.string.error_network));
            } else if (throwable instanceof HttpException) {
                errorResponse.setMessage(((HttpException) throwable).message());
            } else {
                errorResponse.setMessage(throwable.getMessage());
            }
            EventBus.getDefault().post(errorResponse);
            throwable.printStackTrace();
        }

        @Override
        public void onDestroy() {
            compositeDisposable.clear();
        }

        @Override
        public void onStart() {
            compositeDisposable = new CompositeDisposable();
        }

    }
}

