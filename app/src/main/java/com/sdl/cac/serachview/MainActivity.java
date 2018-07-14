package com.sdl.cac.serachview;

import android.content.Intent;
import android.content.Context;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.*;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.ArrayList;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.document.Table;
import com.amazonaws.mobileconnectors.dynamodbv2.document.UpdateItemOperationConfig;
import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Document;
import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Primitive;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import com.sdl.cac.serachview.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;



public class MainActivity extends AppCompatActivity {

    ListView listView;
    ListViewAdapter adapter;
    String[] title;
    String[] description;
    String[] URLC;



    private static String url = "http://gist.githubusercontent.com/sdlcsblr/6fc9137b4cbfe6ebcf6c1494f6c48263/raw/3732a4920ac9b6c61183d6a65bb4c742fc8be36c/";
    int[] icon, temp;
    ArrayList<Model> arrayList = new ArrayList<Model>();
    ArrayList<String> listItems = new ArrayList<String>();
    private static final String TAG_CUSTOMER = "customer";
    private static final String TAG_CNAME = "cname";
    private static final String TAG_SNAME = "sname";
    private static final String TAG_AURL = "aurl";
    private static final String TAG_TYPE = "type";
    JSONArray user = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {




        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            Toast.makeText(MainActivity.this, "You are connected to Internet", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "You are not connected to Internet", Toast.LENGTH_SHORT).show();
            SystemClock.sleep(3000);
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("WorldServer Customers List");

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);


       icon = new int[]{R.drawable.sdla, R.drawable.onpremise, R.drawable.aws,R.drawable.other};
       temp = new int[]{R.drawable.sdla};


        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            try {


                // Creating new JSON Parser

                JSONParser jParser = new JSONParser();
                JSONObject json = jParser.getJSONFromUrl(url);
                user = json.getJSONArray(TAG_CUSTOMER);



                // Getting JSON from URL


                String[] title = new String[user.length()];
                String[] description = new String[user.length()];
                String[] URLC = new String[user.length()];
                String[] type = new String[user.length()];

                for (int i = 0; i < user.length(); i++) {
                    JSONObject c = user.getJSONObject(i);



                    title[i] = c.getString(TAG_CNAME);
                    description[i] = c.getString(TAG_SNAME);
                    URLC[i] = c.getString(TAG_AURL);
                    type[i]= c.getString(TAG_TYPE);
                    if(type[i].equalsIgnoreCase("on demand"))
                        temp[0]=icon[0];
                    else if(type[i].equalsIgnoreCase("on premise"))
                        temp[0]=icon[1];
                    else if(type[i].equalsIgnoreCase("AWS"))
                        temp[0]=icon[2];
                    else
                        temp[0]=icon[3];

                    Model model = new Model(title[i], description[i], URLC[i], temp[0]);
                    //bind all strings in an array
                    arrayList.add(model);


                }

            } catch (JSONException e) {
                // TODO Auto-generated catch block


                e.printStackTrace();
            }
        }
        else
        {
            Toast.makeText(MainActivity.this, "You are not connected to Internet WS Monitor Exiting...", Toast.LENGTH_LONG).show();
            //SystemClock.sleep(3000);
            finish();
            moveTaskToBack(true);

        }

        listView = findViewById(R.id.listView);





        //pass results to listViewAdapter class
        adapter = new ListViewAdapter(this, arrayList);

        //bind the adapter to the listview
        listView.setAdapter(adapter);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView)myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (TextUtils.isEmpty(s)){
                    adapter.filter("");
                    listView.clearTextFilter();
                }
                else {
                    adapter.filter(s);
                }
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id==R.id.action_settings){
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}

