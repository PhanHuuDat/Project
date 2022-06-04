package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class DashboardScreen extends AppCompatActivity {
    Spinner spinner_food, spinner_categories;
    ImageView img_shopping;
    int food[] = {R.drawable.bo_kho, R.drawable.bun_bo_hue, R.drawable.com_chien, R.drawable.mi_quang, R.drawable.pho, R.drawable.com_suon};
    String[] categories = {"Rice", "Noodles", "Drinks", "Ice-cream", "Snakes"};
    int icon[] = {R.drawable.rice, R.drawable.noodles, R.drawable.soft_drink, R.drawable.icecream, R.drawable.snaks};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_dashboard_screen);
        anhxa();

        img_shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent productIntent = new Intent(DashboardScreen.this, MainActivity.class);
                startActivityForResult(productIntent, 0);
            }
        });
        // spinner hình ảnh show lên trên (các loại món ăn )
        CustomeSpiner customeSpiner = new CustomeSpiner(getApplicationContext(), food);
        spinner_food.setAdapter(customeSpiner);
        // spinner loại đồ ăn (categories)
        CustomeSpinerCategories customeSpinerCategories = new CustomeSpinerCategories(getApplicationContext(), categories, icon);
        spinner_categories.setAdapter(customeSpinerCategories);

        spinner_categories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(), categories[i],Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }


    public void anhxa() {
        img_shopping = (ImageView) findViewById(R.id.img_shopping);
        spinner_food = (Spinner) findViewById(R.id.spiner_image);
        spinner_categories = (Spinner) findViewById(R.id.spiner_categories);
    }
}