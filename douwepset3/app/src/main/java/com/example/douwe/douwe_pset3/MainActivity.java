package com.example.douwe.douwe_pset3;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.SystemClock;
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

public class MainActivity extends AppCompatActivity {
    ListView listlylistview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button2).setOnClickListener(new HandleClick());


        String url = "http://pindakaas.ga/resto/categories.php";
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
            TextView mTextView = (TextView) findViewById(R.id.text);
            mTextView.setText("Welcome to the restaurant, look around and order." );

            JSONObject obj = new JSONObject(response);
            JSONArray arr = obj.getJSONArray("categories");

            // for listview
            ArrayList<String> arr2 = new ArrayList<>();

            for (int i = 0; i < arr.length(); i++)
            {
                String post_id = arr.getString(i);
                System.out.println("checkcheck: " + post_id);
                arr2.add(post_id);
            }
            ArrayAdapter<String> list = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, arr2);
            listlylistview = (ListView) findViewById(R.id.ListView);
            listlylistview.setOnItemClickListener(new HandleClick2());
            listlylistview.setAdapter(list);
        } catch (Exception e){

        }

    }
    private class HandleClick2 implements AdapterView.OnItemClickListener {
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent(MainActivity.this, foodsies.class);
            intent.putExtra("knoppie", listlylistview.getItemAtPosition(i).toString());
            startActivity(intent);
        }
    }

    private class HandleClick implements View.OnClickListener {
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this, orderPage.class);
            startActivity(intent);
        }
    }
}
