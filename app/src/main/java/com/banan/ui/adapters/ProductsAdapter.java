package com.banan.ui.adapters;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.banan.R;
import com.banan.model.entities.Product;
import com.banan.util.PicassoCache;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {

    private static final String TAG = ProductsAdapter.class.getSimpleName();

    public static final String NAME_IMAGE = "image";
    public static final String NAME_PRICE = "price";
    public static final String NAME_TITLE = "title";

    private List<Product> data;
    private OnProductClickListener onProductClickListener;

    public interface OnProductClickListener {
        void onProductClicked(Product product, View itemView);
    }

    public ProductsAdapter(ArrayList<Product> data, OnProductClickListener onProductClickListener) {
        this.data = data;
        this.onProductClickListener = onProductClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Product item = data.get(position);
        holder.textViewName.setText(item.getName());
        holder.textViewPrice.setText(item.getPrice());
        holder.itemView.setOnClickListener(view -> onProductClickListener.onProductClicked(item, holder.itemView));
        holder.itemView.setTag(position);
        PicassoCache.getPicassoInstance(holder.itemView.getContext())
                .load(data.get(position).getImagePath())
                .error(R.drawable.ic_image)
                .into(holder.image);

        ViewCompat.setTransitionName(holder.image, NAME_IMAGE);
        ViewCompat.setTransitionName(holder.textViewName, NAME_TITLE);
        ViewCompat.setTransitionName(holder.textViewPrice, NAME_PRICE);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void addData(List<Product> newData) {
        final int itemListSize = newData.size();
        data.addAll(newData);
        notifyItemRangeInserted(data.size() - itemListSize, itemListSize);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image_product)
        ImageView image;
        @BindView(R.id.tv_name)
        TextView textViewName;
        @BindView(R.id.tv_price)
        TextView textViewPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
