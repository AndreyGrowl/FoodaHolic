package com.banan.api;


import com.banan.model.entities.Product;
import com.banan.model.responses.ProductsResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RequestsInterface {

//    https://s3-eu-west-1.amazonaws.com/developer-application-test/cart/list
//    https://s3-eu-west-1.amazonaws.com/developer-application-test/cart/{product_id}/detail

    @GET("cart/list")
    Observable<ProductsResponse> getProducts();

    @GET("cart/{product_id}/detail")
    Observable<Product> getProductById(@Path("product_id") long id);
}
