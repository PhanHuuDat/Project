package com.example.project;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.project.Adapter.DishAdapter;
import com.example.project.Model.Dish;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 135;
    private List<Dish> list = new ArrayList<>();
    private RecyclerView recyclerView;
    private DishAdapter adapter;
    private boolean isLogin;
    private Button btn_gotoCart;
    private Intent intentToCart, intentToLogin;
    private Bundle bundleToCart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intentToCart = new Intent(MainActivity.this, CartActivity.class);
        bundleToCart = new Bundle();
        checkLogin();

        btn_gotoCart = findViewById(R.id.btn_back);

        prepareMovieData();
        recyclerView = findViewById(R.id.main_recycler_view);
        adapter = new DishAdapter(this, list, bundleToCart);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        btn_gotoCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentToCart.putExtra("myPackage",bundleToCart);
                startActivityForResult(intentToCart,REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        bundleToCart = data.getBundleExtra("callBack");
    }

    private void checkLogin() {
        isLogin = getIntent().getBooleanExtra("isLogin", false);
        if (!isLogin) {
            intentToLogin = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intentToLogin);
        }
    }

    private void prepareMovieData() {

        Dish dish = new Dish("Cơm sườn", "Cơm", 30000, R.drawable.com_suon);
        list.add(dish);

        dish = new Dish("Cơm chiên", "Cơm", 40000, R.drawable.com_chien);
        list.add(dish);

        dish = new Dish("Mì quảng", "Món bún", 30000, R.drawable.mi_quang);
        list.add(dish);

        dish = new Dish("Phở", "Món bún", 35000, R.drawable.pho);
        list.add(dish);

        dish = new Dish("Bún bò Huế", "Món bún", 35000, R.drawable.bun_bo_hue);
        list.add(dish);

        dish = new Dish("Bò kho", "Khác", 35000, R.drawable.bo_kho);
        list.add(dish);
    }


}