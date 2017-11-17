package com.example.douwe.douwe_pset3;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class foodpage extends AppCompatActivity {
    JSONObject foodItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // todo: async task
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodpage);
        findViewById(R.id.button).setOnClickListener(new HandleClick2());


        String retrievied_string = getIntent().getExtras().getString("entry");

        try{
            foodItem = new JSONObject(retrievied_string);
            TextView view1 = (TextView) findViewById(R.id.textView2);
            view1.setText(foodItem.getString("name"));
            view1 = (TextView) findViewById(R.id.textView3);
            view1.setText(foodItem.getString("description"));
            String url = foodItem.getString("image_url");
            System.out.println("this url though: " + url);

            Button butt = (Button) findViewById(R.id.button);
            butt.setText(butt.getText() + " for: " + Integer.toString(foodItem.getInt("price")));

            ImageView viewer = (ImageView) findViewById(R.id.imageView2);
            viewer.setImageBitmap(drawable_from_url(url));

            SharedPreferences getter = getSharedPreferences("filename", MODE_PRIVATE);
            String teststring = getter.getString("name", "");
            butt = (Button) findViewById(R.id.button5);
            butt.setVisibility(View.INVISIBLE);
            if(teststring.contains(foodItem.getString("name"))) {
                butt = (Button) findViewById(R.id.button5);
                butt.setVisibility(View.VISIBLE);
                butt.setOnClickListener(new HandleClick3());
            } else {
                butt = (Button) findViewById(R.id.button5);
                butt.setVisibility(View.INVISIBLE);
            }




        } catch (Exception e) {
            System.out.println("error2: " + e.toString());

            System.out.println("i failed :c me sorry");
        }
    }



    Bitmap drawable_from_url(String url) throws java.net.MalformedURLException, java.io.IOException {

        HttpURLConnection connection = (HttpURLConnection)new URL(url) .openConnection();
        connection.setRequestProperty("User-agent","Mozilla/4.0");

        connection.connect();
        InputStream input = connection.getInputStream();

        return BitmapFactory.decodeStream(input);
    }

    private class HandleClick2 implements View.OnClickListener {
        public void onClick(View view) {
            SharedPreferences getter = getSharedPreferences("filename", MODE_PRIVATE);
            int totalPrice = getter.getInt("price", 0);
            String teststring = getter.getString("name", "");
            ArrayList<String> myList = new ArrayList<String>(Arrays.asList(teststring.split("`")));

            // contains title of dish
            TextView viewer = findViewById(R.id.textView2);
            String new_dish = viewer.getText().toString() + "\n";
            String newString = "";
            boolean found = false;
            for(int i = 1; i < myList.size(); i+=2){
                System.out.println("found dish: " + myList.get(i) + " new dish: " + new_dish);
                if (myList.get(i).equals(new_dish)) {
                    found = true;
                    newString = newString + Integer.toString(Integer.parseInt(myList.get(i -1)) + 1)  + "`" + myList.get(i) + "`";
                } else {
                    newString = newString + myList.get(i -1) + "`" + myList.get(i) + "`";
                }
            }
            if (found == false){
                newString = newString + "1" + "`" + new_dish;
            }
            SharedPreferences prefs = getSharedPreferences("filename", MODE_PRIVATE);
            SharedPreferences.Editor prefsEditor = prefs.edit();
            prefsEditor.putString("name", newString);
            try{
                prefsEditor.putInt("price", totalPrice + foodItem.getInt("price"));
            } catch (Exception e) {
                prefsEditor.putInt("price", totalPrice);

            }
            prefsEditor.apply();
            prefsEditor.commit();
        }
    }

    private class HandleClick3 implements View.OnClickListener {
        public void onClick(View view) {
            SharedPreferences getter = getSharedPreferences("filename", MODE_PRIVATE);
            int totalPrice = getter.getInt("price", 0);
            String teststring = getter.getString("name", "");
            ArrayList<String> myList = new ArrayList<String>(Arrays.asList(teststring.split("`")));

            // contains title of dish
            TextView viewer = findViewById(R.id.textView2);
            String new_dish = viewer.getText().toString() + "\n";
            String newString = "";
            boolean found = false;
            for(int i = 1; i < myList.size(); i+=2){
                System.out.println("found dish: " + myList.get(i) + " new dish: " + new_dish);
                if (myList.get(i).equals(new_dish)) {
                    found = true;
                    if(!(Integer.parseInt(myList.get(i -1)) == 1)){
                        newString = newString + Integer.toString(Integer.parseInt(myList.get(i -1)) - 1)  + "`" + myList.get(i) + "`";
                    }
                }
            }

            SharedPreferences prefs = getSharedPreferences("filename", MODE_PRIVATE);
            SharedPreferences.Editor prefsEditor = prefs.edit();
            prefsEditor.putString("name", newString);
            try{
                if(found){
                    prefsEditor.putInt("price", totalPrice - foodItem.getInt("price"));
                }
            } catch (Exception e) {
                prefsEditor.putInt("price", totalPrice);

            }
            prefsEditor.apply();
            prefsEditor.commit();
        }
    }
}
