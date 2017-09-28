package com.banan.ui.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.banan.App;
import com.banan.R;
import com.banan.model.entities.Product;

import org.greenrobot.eventbus.EventBus;

import io.objectbox.Box;

/**
 * contains db initialization for future use, EventBus registering methods and error handling
 */
public class BaseActivity extends AppCompatActivity {

    protected Box<Product> productBox;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBox();
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    protected void showAlert(String title, String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                }).show();
    }

    private void initBox() {
        productBox = ((App) getApplicationContext()).getBoxStore().boxFor(Product.class);
    }

    protected void showOfflineToast() {
        Toast.makeText(this, R.string.offline_toast, Toast.LENGTH_SHORT).show();
    }
}
