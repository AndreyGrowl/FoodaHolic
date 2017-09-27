package com.banan.model.responses;

import com.banan.model.entities.Product;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by andre on 26.09.2017.
 */

public class ProductsResponse {

    @SerializedName("products")
    private List<Product> products;

    public ProductsResponse(List<Product> products) {
        this.products = products;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
