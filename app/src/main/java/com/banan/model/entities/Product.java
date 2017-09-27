package com.banan.model.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.banan.util.NumericTypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class Product implements Parcelable {

    @Id(assignable = true)
    @JsonAdapter(NumericTypeAdapter.class)
    @SerializedName("product_id")
    private long productId;
    @SerializedName("name")
    private String name;
    @SerializedName("price")
    private String price;
    @SerializedName("image")
    private String imagePath;
    @SerializedName("description")
    private String description;

    public Product() {
    }

    private Product(Parcel in) {
        productId = in.readLong();
        name = in.readString();
        price = in.readString();
        imagePath = in.readString();
        description = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(productId);
        parcel.writeString(name);
        parcel.writeString(price);
        parcel.writeString(imagePath);
        parcel.writeString(description);
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}
