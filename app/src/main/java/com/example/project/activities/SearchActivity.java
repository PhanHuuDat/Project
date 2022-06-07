package com.example.project.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
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

public class SearchActivity extends AppCompatActivity implements ItemClickInterface {

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
    private RelativeLayout btnSearch;
    private RelativeLayout btnOrders;
    private RelativeLayout btnProfile;
    private SearchView svSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //init vars
        rvDishes = findViewById(R.id.rv_dishes);
        rlHome = findViewById(R.id.idRLHome);
        pbLoad = findViewById(R.id.pb_load_main);
        btnCart = findViewById(R.id.btn_cart);
        btnHome = findViewById(R.id.btn_home);
        btnSearch = findViewById(R.id.btn_search);
        btnOrders = findViewById(R.id.btn_orders);
        btnProfile = findViewById(R.id.btn_profile);
        svSearch = findViewById(R.id.sv_search);
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
        Intent myIntent = new Intent(SearchActivity.this, LoginActivity.class);
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
        //search
        svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length() == 0) {
                    getDishes();
                } else {
                    adapter.filter(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() == 0) {
                    getDishes();
                } else {
                    adapter.filter(newText);
                }
                return true;
            }
        });
        //navigate to cart list
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this, "Btn cart clicked", Toast.LENGTH_SHORT).show();
                // opening a new activity for adding a course.
                Intent myIntent = new Intent(SearchActivity.this, CartActivity.class);
                startActivity(myIntent);
                finish();
            }
        });

        //navigate to home
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(SearchActivity.this, MainActivity.class);
                startActivity(myIntent);
                finish();
            }
        });

        //navigate to search
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this, "Btn cart clicked", Toast.LENGTH_SHORT).show();
                // opening a new activity for adding a course.
                Intent myIntent = new Intent(SearchActivity.this, SearchActivity.class);
                startActivity(myIntent);
                finish();
            }
        });

        //navigate to order list
        btnOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(SearchActivity.this, OrdersActivity.class);
                startActivity(myIntent);
                finish();
            }
        });

        //navigate to profile
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(SearchActivity.this, ProfileActivity.class);
                startActivity(myIntent);
                finish();
            }
        });
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