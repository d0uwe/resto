package com.example.douwe.douwe_pset3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class foodsies extends AppCompatActivity {
    JSONArray arr3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodsies);

        String pressed_button = getIntent().getExtras().getString("knoppie");
        String url = "https://resto.mprog.nl/menu?category=" + pressed_button;
        getString(url);

    }

    public String getString(String url){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String result;
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.

                        parseResponse(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);

        return "failed";
    }

    void parseResponse(String response){
        try{

            JSONObject obj = new JSONObject(response);
            JSONArray arr = obj.getJSONArray("items");

            // for listview
            ArrayList<String> arr2 = new ArrayList<>();
            System.out.println(arr);
            System.out.println("legte" + Integer.toString(arr.length()));

            // Get all foodsies and add them to arr2, for the view.
            for (int i = 0; i < arr.length(); i++)
            {
                System.out.println("wtf mate");
                JSONObject entry = arr.getJSONObject(i);
                System.out.println("hey" + entry.getString("name"));

                String post_id = entry.getString("name");
                System.out.println("checkcheck: " + post_id);
                arr2.add(post_id);
            }
            ArrayAdapter<String> list = new ArrayAdapter<>(foodsies.this, android.R.layout.simple_list_item_1, arr2);
            final ListView listlylistview = (ListView) findViewById(R.id.ListView2);

            // make a final array...
            arr3 = arr;
            listlylistview.setOnItemClickListener(new HandleClick2());
            listlylistview.setAdapter(list);
        } catch (Exception e){

        }

    }

    private class HandleClick2 implements AdapterView.OnItemClickListener {
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent(foodsies.this, foodpage.class);
            try{
                intent.putExtra("entry", arr3.getJSONObject(i).toString());
            } catch (Exception e){
                intent.putExtra("entry", "failed");
            }
            startActivity(intent);
        }
    }
}
