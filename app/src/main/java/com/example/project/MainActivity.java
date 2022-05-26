package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Dish> list = new ArrayList<>();
    private RecyclerView recyclerView;
    private DishAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prepareMovieData();
        recyclerView = findViewById(R.id.recycler_view);
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