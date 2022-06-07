package com.example.project.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.NumberPicker;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.adapters.CartAdapter;
import com.example.project.interfaces.ItemClickInterface;
import com.example.project.interfaces.ItemLongClickInterface;
import com.example.project.models.Cart;
import com.example.project.models.CartItem;
import com.example.project.models.Dish;
import com.example.project.models.Order;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderDetailActivity extends AppCompatActivity implements ItemClickInterface, ItemLongClickInterface {

    private RecyclerView rvCart;
    private TextView tvTotal, tvStatus;
    private RelativeLayout btnHome;
    private RelativeLayout btnSearch;
    private RelativeLayout btnOrders;
    private RelativeLayout btnProfile;
    private RelativeLayout rlHome;
    private ProgressBar pbLoad;
    private CartAdapter adapter;
    private RelativeLayout btnBack;


    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    private List<CartItem> cartItems;
    private String currentOID;
    private String currentStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        Intent callerIntent = getIntent();
        Bundle packetFromCaller = callerIntent.getBundleExtra("myPacket");
        currentOID = packetFromCaller.getString("OID");
        //init vars
        tvTotal = findViewById(R.id.tv_total);
        tvStatus = findViewById(R.id.tv_status);
        rvCart = findViewById(R.id.rv_dishes);
        rlHome = findViewById(R.id.idRLHome);
        pbLoad = findViewById(R.id.pb_load_main);
        cartItems = new ArrayList<>();
        btnHome = findViewById(R.id.btn_home);
        btnSearch = findViewById(R.id.btn_search);
        btnOrders = findViewById(R.id.btn_orders);
        btnProfile = findViewById(R.id.btn_profile);
        cartItems = new ArrayList<>();
        btnBack = findViewById(R.id.btn_back);

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
        adapter = new CartAdapter(this, cartItems, this::onItemClick, this::onItemLongClick);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvCart.setLayoutManager(mLayoutManager);
        rvCart.setItemAnimator(new DefaultItemAnimator());
        rvCart.setAdapter(adapter);
        getOrder();
        pbLoad.setVisibility(View.GONE);

        //set ev
        setEvent();
    }

    private void goToLogin() {
        Intent myIntent = new Intent(OrderDetailActivity.this, LoginActivity.class);
        startActivity(myIntent);
        finish();
    }

    private void setEvent() {
        //go back to whatever it was
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //navigate to home
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(OrderDetailActivity.this, MainActivity.class);
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
                Intent myIntent = new Intent(OrderDetailActivity.this, SearchActivity.class);
                startActivity(myIntent);
                finish();
            }
        });

        //navigate to order list
        btnOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToOrderList();
            }
        });

        //navigate to profile
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(OrderDetailActivity.this, ProfileActivity.class);
                startActivity(myIntent);
                finish();
            }
        });
    }

    //get cart
    private void getOrder() {
        databaseReference = firebaseDatabase.getReference("Orders");
        databaseReference.child(currentOID).child("orderItems").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Checking if the value exists
                if (snapshot.exists()) {
                    for (DataSnapshot sn : snapshot.getChildren()) {
                        cartItems.add(sn.getValue(CartItem.class));
                    }
                } else {
                    Toast.makeText(OrderDetailActivity.this, "Data does not exist", Toast.LENGTH_SHORT).show();
                }
                updateInfo();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        databaseReference.child(currentOID).child("status").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    currentStatus = snapshot.getValue(String.class);
                } else {
                    Toast.makeText(OrderDetailActivity.this, "Data does not exist", Toast.LENGTH_SHORT).show();
                }
                updateInfo();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateInfo() {
        long total = 0;
        for (CartItem c : cartItems) {
            total += c.getTotal();
        }
        tvStatus.setText("Status: " + currentStatus);
        tvTotal.setText("Total: " + total);
    }


    private void goToOrderList() {
//        Toast.makeText(CartActivity.this, dishes.get(position).getId(), Toast.LENGTH_SHORT).show();
        Intent myIntent = new Intent(this, OrdersActivity.class);
        startActivity(myIntent);
        finish();
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public boolean onItemLongClick(int position) {
//        Toast.makeText(CartActivity.this, cartItems.get(position).getDishID(), Toast.LENGTH_SHORT).show();
        PopupMenu popup = new PopupMenu(OrderDetailActivity.this, rvCart.getChildAt(position));
        //inflating menu from xml resource
        popup.inflate(R.menu.option_menu_od);
        //adding click listener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.mn_detail:
                        Intent myIntent = new Intent(OrderDetailActivity.this, DetailActivity.class);
                        Bundle myBundle = new Bundle();
                        myBundle.putString("dishID", cartItems.get(position).getDishID());
                        myIntent.putExtra("myPacket", myBundle);
                        startActivity(myIntent);
                        break;
                }
                return false;
            }
        });
        //displaying the popup
        popup.show();
        return true;
    }
}