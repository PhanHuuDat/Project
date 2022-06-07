package com.example.project.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.project.R;
import com.example.project.adapters.DishAdapter;
import com.example.project.adapters.OrderAdapter;
import com.example.project.interfaces.ItemClickInterface;
import com.example.project.interfaces.ItemLongClickInterface;
import com.example.project.models.CartItem;
import com.example.project.models.Dish;
import com.example.project.models.Order;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;

public class OrdersActivity extends AppCompatActivity implements ItemClickInterface {

    private FloatingActionButton btnCart;
    private RecyclerView rvOrder;
    private RelativeLayout btnHome;
    private RelativeLayout btnSearch;
    private RelativeLayout btnOrders;
    private RelativeLayout btnProfile;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private ArrayList<Order> orders;
    private RelativeLayout rlHome;
    private ProgressBar pbLoad;
    private OrderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        //init vars
        btnCart = findViewById(R.id.btn_cart);
        rvOrder = findViewById(R.id.rv_orders);
        orders = new ArrayList<>();
        rlHome = findViewById(R.id.idRLHome);
        pbLoad = findViewById(R.id.pb_load_main);
        btnHome = findViewById(R.id.btn_home);
        btnSearch = findViewById(R.id.btn_search);
        btnOrders = findViewById(R.id.btn_orders);
        btnProfile = findViewById(R.id.btn_profile);

        //init db
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Orders");
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            // No user is signed in
            goToLogin();
        }

        //init adapter
        adapter = new OrderAdapter(this, orders, this::onItemClick);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvOrder.setLayoutManager(mLayoutManager);
        rvOrder.setItemAnimator(new DefaultItemAnimator());
        rvOrder.setAdapter(adapter);

        //get orders
        getOrders();
        addEvent();
    }

    private void goToLogin() {
        Intent myIntent = new Intent(OrdersActivity.this, LoginActivity.class);
        startActivity(myIntent);
        finish();
    }

    private void getOrders() {
        orders.clear();
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                pbLoad.setVisibility(View.GONE);
                Order reveiveOrder = snapshot.getValue(Order.class);
                if (reveiveOrder.getUid().equals(mAuth.getUid())) {
                    orders.add(reveiveOrder);
                    adapter.notifyDataSetChanged();
                }
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
                Intent myIntent = new Intent(OrdersActivity.this, CartActivity.class);
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
                Intent myIntent = new Intent(OrdersActivity.this, SearchActivity.class);
                startActivity(myIntent);
                finish();
            }
        });

        //navigate to home
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(OrdersActivity.this, MainActivity.class);
                startActivity(myIntent);
                finish();
            }
        });

        //reload this
        btnOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(OrdersActivity.this, OrdersActivity.class);
                startActivity(myIntent);
                finish();
            }
        });

        //navigate to profile
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(OrdersActivity.this, ProfileActivity.class);
                startActivity(myIntent);
                finish();
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Intent myIntent = new Intent(this, OrderDetailActivity.class);
        Bundle myBundle = new Bundle();
        myBundle.putString("OID", orders.get(position).getOid());
        myIntent.putExtra("myPacket", myBundle);
        startActivity(myIntent);
    }

}