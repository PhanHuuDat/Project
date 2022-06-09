package com.example.project.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.project.R;
import com.example.project.models.Cart;
import com.example.project.models.Dish;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    private String currentDID;
    private ImageView ivFoodImg;
    private TextView tvFoodName;
    private TextView tvFoodCat;
    private TextView tvFoodPrice;
    private FloatingActionButton btnAdd;
    private FloatingActionButton btnCart;
    private ProgressBar pbLoad;
    private RelativeLayout btnHome;
    private RelativeLayout btnSearch;
    private RelativeLayout btnOrders;
    private RelativeLayout btnProfile;
    private RelativeLayout btnBack;
    private CardView cvMain;

    private Cart cart;
    private Dish currentD;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //init db
        firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            // No user is signed in
            goToLogin();
        }

        //init vars
        mapping();
        pbLoad.setVisibility(View.GONE);
        addEvent();
    }

    private void goToLogin() {
        Intent myIntent = new Intent(DetailActivity.this, LoginActivity.class);
        startActivity(myIntent);
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    public void addEvent() {
        //open form to add this item to cart
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPickerDialog();
            }
        });

        //navigate to cart list
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(DetailActivity.this, CartActivity.class);
                startActivity(myIntent);
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });

        //go back to whatever it was
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //navigate to main
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(DetailActivity.this, MainActivity.class);
                startActivity(myIntent);
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });

        //navigate to search
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this, "Btn cart clicked", Toast.LENGTH_SHORT).show();
                // opening a new activity for adding a course.
                Intent myIntent = new Intent(DetailActivity.this, SearchActivity.class);
                startActivity(myIntent);
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });

        //navigate to order
        btnOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(DetailActivity.this, OrdersActivity.class);
                startActivity(myIntent);
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });

        //navigate to profile
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(DetailActivity.this, ProfileActivity.class);
                startActivity(myIntent);
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });

    }

    private void showPickerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View view = this.getLayoutInflater().inflate(R.layout.dialog_picker, null);
        builder.setView(view);
        builder.setTitle("How many do you want to add?");
        final NumberPicker picker = (NumberPicker) view.findViewById(R.id.picker);
        picker.setMinValue(1);
        picker.setMaxValue(100);
        picker.setValue(1);
        builder
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        int a = 0;
                        try {
                            int amount = picker.getValue();
                            long singlePrice = Long.valueOf(currentD.getPrice());
                            long totalPrice = singlePrice * amount;
                            cart.addToCart(currentDID, amount, singlePrice, totalPrice, currentD.getFoodImg());
//                            cart.setTotal(cart.getTotal() + Long.valueOf(currentD.getPrice()) * amount);
                            saveCart();
//                            Toast.makeText(DetailActivity.this,
//                                    "Added to cart", Toast.LENGTH_SHORT).show();
                            Snackbar.make(cvMain, "Added to cart", Snackbar.LENGTH_SHORT).show();

                        } catch (Exception e) {
                            Toast.makeText(DetailActivity.this,
                                    e.getMessage(), Toast.LENGTH_SHORT).show();
                            Snackbar.make(cvMain, e.getMessage(), Snackbar.LENGTH_SHORT).show();
//                            System.out.println(e.getMessage());
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                       dialog.cancel();
                    }
                });
        builder.create().show();
    }

    private void mapping() {
        Intent callerIntent = getIntent();
        Bundle packetFromCaller = callerIntent.getBundleExtra("myPacket");
        currentDID = packetFromCaller.getString("dishID");
        ivFoodImg = findViewById(R.id.img_avatar);
        tvFoodName = findViewById(R.id.tv_dish_name);
        pbLoad = findViewById(R.id.pb_load_main);
        tvFoodCat = findViewById(R.id.tv_dish_cat);
        tvFoodPrice = findViewById(R.id.tv_dish_price);
        btnAdd = findViewById(R.id.btn_add);
        btnCart = findViewById(R.id.btn_cart);
        btnHome = findViewById(R.id.btn_home);
        btnSearch = findViewById(R.id.btn_search);
        btnOrders = findViewById(R.id.btn_orders);
        btnProfile = findViewById(R.id.btn_profile);
        btnBack = findViewById(R.id.btn_back);
        cvMain = findViewById(R.id.card_detail);
        findCurrentDish(currentDID);
        getCart();
    }

    //    find current selected dish
    private void findCurrentDish(String currentDID) {
        databaseReference = firebaseDatabase.getReference("Dishes");
        int fid = Integer.valueOf(currentDID) - 1;
        databaseReference.child(String.valueOf(fid)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Checking if the value exists
                boolean isEmpty = true;
                if (snapshot.exists()) {
                    Dish d = snapshot.getValue(Dish.class);
                    // checking if the name searched is available and adding it to the array list
                    if (d.getId().equals(currentDID)) {
                        currentD = d;
                        isEmpty = false;
                    }
                    Picasso.get().load(currentD.getFoodImg()).into(ivFoodImg);
                    tvFoodName.setText(currentD.getName());
                    tvFoodCat.setText(currentD.getCategory());
                    tvFoodPrice.setText(currentD.getPrice()+ " VND");
                }
                if (isEmpty) {
//                    Toast.makeText(DetailActivity.this, "Data does not exist", Toast.LENGTH_SHORT).show();
                    Snackbar.make(cvMain, "Data does not exist", Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(DetailActivity.this, "Error", Toast.LENGTH_LONG).show();
                Snackbar.make(cvMain, "Error!", Snackbar.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    //get cart
    private void getCart() {
        databaseReference = firebaseDatabase.getReference("Carts");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Checking if the value exists
                boolean isFound = false;
                if (snapshot.exists()) {
                    // looping through the values
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Cart tempCart = ds.getValue(Cart.class);
                        // checking if the name searched is available and adding it to the array list
                        if (tempCart.getUid().equals(mAuth.getCurrentUser().getUid())) {
                            cart = tempCart;
                            isFound = true;
                            break;
                        }
                    }
                }
                if (!isFound) {
                    //create a new cart
                    cart = new Cart("Cart" + String.valueOf(snapshot.getChildrenCount() + 1),
                            mAuth.getCurrentUser().getUid(),
                            new ArrayList<>(), 0);
                    saveCart();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(DetailActivity.this, "Error", Toast.LENGTH_LONG).show();
                Snackbar.make(cvMain, "Error!!", Snackbar.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void saveCart() {
        databaseReference = firebaseDatabase.getReference("Carts");
        databaseReference.child(cart.getCid()).setValue(cart);
    }
}