package com.banan.model.responses;

import com.banan.model.entities.Product;

/**
 * Created by andre on 26.09.2017.
 */

public class ProductDetailResponse {


    private Product product;

    public ProductDetailResponse(Product product) {
        this.product = product;
    }

    public Product getProduct() {
        return product;
    }
}
