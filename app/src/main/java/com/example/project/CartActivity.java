package com.example.project;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.Adapter.CartAdapter;
import com.example.project.Model.Dish;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends Activity {
    private List<Dish> list = new ArrayList<>();
    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private Button btn_back, btn_calculator;
    private Bundle myBundle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        btn_back = findViewById(R.id.btn_back);
        myBundle = getIntent().getBundleExtra("myPackage");


        list = (List<Dish>) myBundle.getSerializable("list");
        if (list == null) {
            list = new ArrayList<>();
        }
        recyclerView = findViewById(R.id.cart_recycler_view);
        adapter = new CartAdapter(this, list);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}
