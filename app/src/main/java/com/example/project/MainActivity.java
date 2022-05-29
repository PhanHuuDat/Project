package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.project.Adapter.DishAdapter;
import com.example.project.Model.Dish;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Dish> list = new ArrayList<>();
    private RecyclerView recyclerView;
    private DishAdapter adapter;
    private boolean isLogin;
    private Button btn_gotoCart;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isLogin = getIntent().getBooleanExtra("isLogin", false);

        btn_gotoCart = findViewById(R.id.btn_back);
        btn_gotoCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(MainActivity.this,CartActivity.class);
                startActivity(intent);
            }
        });

        if(!isLogin){
            intent = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
        }

        prepareMovieData();
        recyclerView = findViewById(R.id.main_recycler_view);
        adapter = new DishAdapter(this, list);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);


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