package com.example.douwe.douwe_pset3;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;


public class orderPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_page);
        findViewById(R.id.button3).setOnClickListener(new HandleClick());

        try {
            TextView textViewer = (TextView) findViewById(R.id.textView4);

            SharedPreferences prefs = getSharedPreferences("filename", MODE_PRIVATE);
            String teststring = prefs.getString("name", "Nothing on order yet, go get some.");


            textViewer.setText(teststring.replace('`',' '));

            textViewer = (TextView) findViewById(R.id.textView);
            ArrayList<String> myList = new ArrayList<String>(Arrays.asList(teststring.split("`")));

            // can't order when there are no products.
            if(myList.size() > 1) {
                Button butt = (Button) findViewById(R.id.button3);
                butt.setVisibility(View.VISIBLE);
            } else {
                Button butt = (Button) findViewById(R.id.button3);
                butt.setVisibility(View.INVISIBLE);
            }

            int amount_prods = 0;
            for(int i = 0; i < myList.size(); i+=2) {
                amount_prods += Integer.parseInt(myList.get(i));
            }
                textViewer.setText("Amount of products: " + Integer.toString(amount_prods));
        }catch (Exception e){
            System.out.println("error3: " + e.toString());
        }
    }

    private class HandleClick implements View.OnClickListener {
        public void onClick(View arg0) {
            getString("https:///www.resto.mprog.nl/order");
            SharedPreferences prefs = getSharedPreferences("filename", MODE_PRIVATE);
            SharedPreferences.Editor prefsEditor = prefs.edit();
            prefsEditor.remove("name");
            prefsEditor.apply();
            prefsEditor.commit();
            TextView confirm = (TextView) findViewById(R.id.textView5);
            confirm.setText("Order has been placed.");
        }
    }



    public String getString(String url){
        RequestQueue queue = Volley.newRequestQueue(this);
        url = "https://resto.mprog.nl/order";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        parseResponse(response);
                        try {
                            parseResponse(response);
                        }
                        catch (Exception e) {
                            parseResponse("");
                            System.out.println(e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.toString());
            }
        });

        queue.add(stringRequest);
        return "no";
    }

    public void parseResponse(String response){
        String time;
        try{
            JSONObject prep_time = new JSONObject(response);
            time = "Order will be delivered in: " + Integer.toString(prep_time.getInt("preparation_time")) + " minutes";
        } catch (Exception e) {
            System.out.println(e.toString());
            time = "Due to an error the food will never be delivered.";
        }
        TextView view = findViewById(R.id.textView5);
        view.setText(time);
        Button butt = (Button) findViewById(R.id.button3);
        butt.setVisibility(View.INVISIBLE);
    }



}
