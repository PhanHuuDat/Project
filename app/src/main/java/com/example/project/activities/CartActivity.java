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
import com.example.project.models.Order;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CartActivity extends AppCompatActivity implements ItemClickInterface, ItemLongClickInterface {

    private FloatingActionButton btnRemove;
    private RecyclerView rvCart;
    private TextView tvTotal;
    private FloatingActionButton btnSummary;
    private RelativeLayout btnHome;
    private RelativeLayout btnSearch;
    private RelativeLayout btnOrders;
    private RelativeLayout btnProfile;
    private RelativeLayout rlHome;
    private ProgressBar pbLoad;
    private CartAdapter adapter;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    private List<CartItem> cartItems;
    private Cart cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        //init vars
        tvTotal = findViewById(R.id.tv_total);
        rvCart = findViewById(R.id.rv_dishes);
        rlHome = findViewById(R.id.idRLHome);
        pbLoad = findViewById(R.id.pb_load_main);
        btnRemove = findViewById(R.id.btn_remove);
        cartItems = new ArrayList<>();
        btnSummary = findViewById(R.id.btn_summary);
        btnHome = findViewById(R.id.btn_home);
        btnSearch = findViewById(R.id.btn_search);
        btnOrders = findViewById(R.id.btn_orders);
        btnProfile = findViewById(R.id.btn_profile);
        cartItems = new ArrayList<>();

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
        getCart();
        pbLoad.setVisibility(View.GONE);

        //set ev
        setEvent();
    }

    private void goToLogin() {
        Intent myIntent = new Intent(CartActivity.this, LoginActivity.class);
        startActivity(myIntent);
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    private void setEvent() {
        // submit order
        btnSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                builder.setMessage("Confirm order?");
                builder.setCancelable(true);

                builder.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (cart.getCartItems().size() != 0) {
                                    Order newOrder = new Order();
                                    newOrder.setStatus("pending");
                                    newOrder.setOid(generateOID());
                                    newOrder.setOrderItems(cart.getCartItems());
                                    newOrder.setUid(cart.getUid());
                                    newOrder.setPrice(cart.getTotal());
                                    databaseReference = firebaseDatabase.getReference("Orders");
                                    databaseReference.child(newOrder.getOid()).setValue(newOrder);
                                    databaseReference = firebaseDatabase.getReference("Carts");
                                    databaseReference.child(cart.getCid()).removeValue();
                                    goToOrderList();
                                } else {
//                                    Toast.makeText(CartActivity.this, "Cart is empty", Toast.LENGTH_SHORT).show();
                                    Snackbar.make(rvCart, "Cart is empty!", Snackbar.LENGTH_SHORT).show();
                                    dialog.cancel();
                                }

                            }
                        });

                builder.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        //navigate to home
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(CartActivity.this, MainActivity.class);
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
                Intent myIntent = new Intent(CartActivity.this, SearchActivity.class);
                startActivity(myIntent);
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
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
                Intent myIntent = new Intent(CartActivity.this, ProfileActivity.class);
                startActivity(myIntent);
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });

        //delete items
        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < cartItems.size(); i++) {
                    View itemView = rvCart.getChildAt(i);
                    CheckBox ckCheck = itemView.findViewById(R.id.ck_select);
                    if (ckCheck.isChecked()) {
                        cart.removeFromCart(cartItems.get(i).getDishID());
                        ckCheck.setChecked(false);
                        //adapter.notifyDataSetChanged();
                    }
                }
                databaseReference = firebaseDatabase.getReference("Carts");
//                databaseReference.child(cart.getCid()).removeValue();
                databaseReference.child(cart.getCid()).setValue(cart);
                updateInfo();
            }
        });
        //show context menu

    }

    private String generateOID() {
        StringBuffer stringBuffer = new StringBuffer();
        Date now = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmm");
        simpleDateFormat.format(now, stringBuffer, new FieldPosition(0));
        return stringBuffer.toString();
    }

    private void goToOrderList() {
//        Toast.makeText(CartActivity.this, dishes.get(position).getId(), Toast.LENGTH_SHORT).show();
        Intent myIntent = new Intent(this, OrdersActivity.class);
        startActivity(myIntent);
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
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
                cartItems.clear();
                for (CartItem c : cart.getCartItems()) {
                    cartItems.add(c);
                }
                updateInfo();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(CartActivity.this, "Error", Toast.LENGTH_LONG).show();
                Snackbar.make(rvCart, "Error!", Snackbar.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void saveCart() {
        databaseReference = firebaseDatabase.getReference("Carts");
        databaseReference.child(cart.getCid()).setValue(cart);
    }

    private void updateInfo() {
        tvTotal.setText("Total: " + cart.getTotal());
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public boolean onItemLongClick(int position) {
//        Toast.makeText(CartActivity.this, cartItems.get(position).getDishID(), Toast.LENGTH_SHORT).show();
        PopupMenu popup = new PopupMenu(CartActivity.this, rvCart.getChildAt(position));
        //inflating menu from xml resource
        popup.inflate(R.menu.option_menu);
        //adding click listener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.mn_detail:
                        Intent myIntent = new Intent(CartActivity.this, DetailActivity.class);
                        Bundle myBundle = new Bundle();
                        myBundle.putString("dishID", cartItems.get(position).getDishID());
                        myIntent.putExtra("myPacket", myBundle);
                        startActivity(myIntent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                        break;
                    case R.id.mn_edit:
//                        Toast.makeText(CartActivity.this, "mn edit", Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                        final View view = CartActivity.this.getLayoutInflater().inflate(R.layout.dialog_picker, null);
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
                                            long singlePrice = Long.valueOf(cartItems.get(position).getSinglePrice());
                                            long totalPrice = singlePrice * amount;
                                            cart.editCartItem(cartItems.get(position).getDishID(), amount, singlePrice, totalPrice, cartItems.get(position).getItemImg());
                                            databaseReference = firebaseDatabase.getReference("Carts");
                                            databaseReference.child(cart.getCid()).setValue(cart);
                                            updateInfo();
//                                            Toast.makeText(CartActivity.this,
//                                                    "Added to cart", Toast.LENGTH_SHORT).show();
                                            Snackbar.make(rvCart, "Added to cart!", Snackbar.LENGTH_SHORT).show();

                                        } catch (Exception e) {
//                                            Toast.makeText(CartActivity.this,
//                                                    e.getMessage(), Toast.LENGTH_SHORT).show();
//                                            System.out.println(e.getMessage());
                                            Snackbar.make(rvCart, e.getMessage(), Snackbar.LENGTH_SHORT).show();
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
                        break;
                    case R.id.mn_remove:
                        cart.removeFromCart(cartItems.get(position).getDishID());
                        databaseReference = firebaseDatabase.getReference("Carts");
                        databaseReference.child(cart.getCid()).setValue(cart);
                        updateInfo();
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