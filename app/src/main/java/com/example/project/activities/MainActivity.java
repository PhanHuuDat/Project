package com.example.project.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.adapters.DishAdapter;
import com.example.project.interfaces.ItemClickInterface;
import com.example.project.models.Dish;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Locale;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity implements ItemClickInterface {

    private FloatingActionButton btnCart;
    private RecyclerView rvDishes;
    private DishAdapter adapter;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private ProgressBar pbLoad;
    private ArrayList<Dish> dishes;
    private RelativeLayout rlHome;
    private RelativeLayout btnHome;
    private RelativeLayout btnOrders;
    private RelativeLayout btnProfile;
    private EditText edtSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init vars
        rvDishes = findViewById(R.id.rv_dishes);
        rlHome = findViewById(R.id.idRLHome);
        pbLoad = findViewById(R.id.pb_load_main);
        btnCart = findViewById(R.id.btn_cart);
        btnHome = findViewById(R.id.btn_home);
        btnOrders = findViewById(R.id.btn_orders);
        btnProfile = findViewById(R.id.btn_profile);
        edtSearch = findViewById(R.id.edt_search);
        dishes = new ArrayList<>();

        //init db
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Dishes");
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            // No user is signed in
            goToLogin();
        }

        //init adapter
        adapter = new DishAdapter(this, dishes, this::onItemClick);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvDishes.setLayoutManager(mLayoutManager);
        rvDishes.setItemAnimator(new DefaultItemAnimator());
        rvDishes.setAdapter(adapter);
        //get dishes
        getDishes();

//        prepareDishesData();
        addEvent();
    }
    private void goToLogin() {
        Intent myIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(myIntent);
        finish();
    }

    private void getDishes() {
        dishes.clear();
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                pbLoad.setVisibility(View.GONE);
                dishes.add(snapshot.getValue(Dish.class));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                pbLoad.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                pbLoad.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                pbLoad.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addEvent() {
        //navigate to cart list
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this, "Btn cart clicked", Toast.LENGTH_SHORT).show();
                // opening a new activity for adding a course.
                Intent myIntent = new Intent(MainActivity.this, CartActivity.class);
                startActivity(myIntent);
                finish();
            }
        });

        //reload this
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(myIntent);
                finish();
            }
        });

        //navigate to order list
        btnOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, OrdersActivity.class);
                startActivity(myIntent);
                finish();
            }
        });

        //navigate to profile
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(myIntent);
                finish();
            }
        });

        //search input
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchKey = edtSearch.getText().toString();
                if (searchKey.length() > 0) {
                    searchDishes(searchKey);
                } else {
                    getDishes();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void searchDishes(String searchKey) {
        dishes.removeIf(dish -> !dish.getName().toLowerCase().contains(searchKey.toLowerCase()));
        adapter.notifyDataSetChanged();
    }

    private void prepareDishesData() {
        Dish dish = new Dish("1", "Cơm sườn", "Cơm", "30000", "https://cdn.beptruong.edu.vn/wp-content/uploads/2018/06/cach-uop-thit-nuong-com-tam.jpg");
        Dish dish1 = new Dish("2", "Cơm sườn2", "Cơm", "31000", "https://cdn.beptruong.edu.vn/wp-content/uploads/2018/06/cach-uop-thit-nuong-com-tam.jpg");
        Dish dish2 = new Dish("3", "Cơm sườn3", "Cơm", "32000", "https://cdn.beptruong.edu.vn/wp-content/uploads/2018/06/cach-uop-thit-nuong-com-tam.jpg");
        Dish dish3 = new Dish("4", "Cơm sườn4", "Cơm", "33000", "https://cdn.beptruong.edu.vn/wp-content/uploads/2018/06/cach-uop-thit-nuong-com-tam.jpg");
        dishes.add(dish);
        dishes.add(dish1);
        dishes.add(dish2);
        dishes.add(dish3);
    }

    @Override
    public void onItemClick(int position) {
//        displayBottomSheet(dishes.get(position));
        //Toast.makeText(MainActivity.this, dishes.get(position).getId(), Toast.LENGTH_SHORT).show();
        Intent myIntent = new Intent(this, DetailActivity.class);
        Bundle myBundle = new Bundle();
        myBundle.putString("dishID", dishes.get(position).getId());
        myIntent.putExtra("myPacket", myBundle);
        startActivity(myIntent);
    }

}