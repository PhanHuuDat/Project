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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
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
    private RelativeLayout btnSearch;
    private RelativeLayout btnOrders;
    private RelativeLayout btnProfile;

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
        btnSearch = findViewById(R.id.btn_search);
        btnOrders = findViewById(R.id.btn_orders);
        btnProfile = findViewById(R.id.btn_profile);
        dishes = new ArrayList<>();
        //init db
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Dishes");
//        prepareDishesData();
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
        addEvent();
    }

    private void goToLogin() {
        Intent myIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(myIntent);
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    private void getDishes() {
        dishes.clear();
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                pbLoad.setVisibility(View.GONE);
                dishes.add(snapshot.getValue(Dish.class));
                adapter.filter("all");
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                pbLoad.setVisibility(View.GONE);
                adapter.filter("all");
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                pbLoad.setVisibility(View.GONE);
                adapter.filter("all");
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                pbLoad.setVisibility(View.GONE);
                adapter.filter("all");
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addEvent() {
        //navigate to search
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this, "Btn cart clicked", Toast.LENGTH_SHORT).show();
                // opening a new activity for adding a course.
                Intent myIntent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(myIntent);
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });
        //navigate to cart list
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this, "Btn cart clicked", Toast.LENGTH_SHORT).show();
                // opening a new activity for adding a course.
                Intent myIntent = new Intent(MainActivity.this, CartActivity.class);
                startActivity(myIntent);
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });

        //reload this
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(myIntent);
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });

        //navigate to order list
        btnOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, OrdersActivity.class);
                startActivity(myIntent);
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });

        //navigate to profile
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(myIntent);
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });

    }

    private void prepareDishesData() {
        List<Dish> uploadList = new ArrayList<>();
        //String id, String name, String category, String price, String foodImg)
        Dish dish1 = new Dish("1", "Cơm gà", "Rice", "20000", "https://cdn.tgdd.vn/2020/07/CookRecipe/GalleryStep/thanh-pham-421.jpg" );
        Dish dish2 = new Dish("2", "Cơm sườn", "Rice", "25000", "https://cdn.beptruong.edu.vn/wp-content/uploads/2018/06/cach-uop-thit-nuong-com-tam.jpg" );

        Dish dish3 = new Dish("3", "Bún riêu", "Noodle", "24000", "https://cdn.tgdd.vn/2020/07/CookRecipe/GalleryStep/thanh-pham-421.jpg" );
        Dish dish4 = new Dish("4", "Bún thịt nướng", "Noodle", "22000", "https://cdn.tgdd.vn/2020/07/CookRecipe/GalleryStep/thanh-pham-421.jpg" );

        Dish dish5 = new Dish("5", "Phở bò", "Noodle", "30000", "https://i.ytimg.com/vi/0Z__e-gagx4/maxresdefault.jpg" );

        Dish dish6 = new Dish("6", "Bò nướng", "Steak", "250000", "https://khoaikhau.com/wp-content/uploads/2020/09/mon-beefsteak-da-chuan-bi-xong-tai-mot-steakhouse.jpg" );

        Dish dish7 = new Dish("7", "Ramen", "Noodle", "45000", "https://glebekitchen.com/wp-content/uploads/2017/04/tonkotsuramenfront.jpg\n" );
        Dish dish8 = new Dish("8", "Cháo lòng", "Soup", "30000", "https://images.foody.vn/res/g25/245020/prof/s576x330/foody-mobile-foody-chao-long-hai--594-636014051026334282.jpg" );
        uploadList.add(dish1);
        uploadList.add(dish2);
        uploadList.add(dish3);
        uploadList.add(dish4);
        uploadList.add(dish5);
        uploadList.add(dish6);
        uploadList.add(dish7);
        uploadList.add(dish8);
        databaseReference.setValue(uploadList);
    }

    @Override
    public void onItemClick(int position) {
        Intent myIntent = new Intent(this, DetailActivity.class);
        Bundle myBundle = new Bundle();
        myBundle.putString("dishID", dishes.get(position).getId());
        myIntent.putExtra("myPacket", myBundle);
        startActivity(myIntent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

}