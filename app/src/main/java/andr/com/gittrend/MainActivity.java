package andr.com.gittrend;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity {

    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    EditText editText;
    private ArrayList<ExampleItem> mExampleList;
    private ExampleAdapter mAdapter;
    private ProgressBar pgsBar;
    private TextView noText;
    private Button noButton;
    private dbhelper DbHelper;
    private user User;
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS =0 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        pgsBar = (ProgressBar) findViewById(R.id.pBar);
        noText = (TextView) findViewById(R.id.nointernttext);
        noButton = (Button) findViewById(R.id.nointernetbutton);
        noButton.setVisibility(View.GONE);
        pgsBar.setVisibility(View.GONE);
        noText.setVisibility(View.GONE);

        DbHelper = new dbhelper(this);
        List<user> Users = DbHelper.getAllUser();
        Log.d("sdd", "userlength:"+Users.size()+" ");
        Log.d("users", "loadreps: "+Users+"");
        // init SwipeRefreshLayout and ListView
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.simpleSwipeRefreshLayout);
        // get the reference of RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        // set a LinearLayoutManager with default vertical orientation
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        mExampleList = new ArrayList<>();
        if (checkAndRequestPermissions()==true) {
            pgsBar.setVisibility(VISIBLE);

            if (isNetworkConnected() != false) {
                noButton.setVisibility(View.GONE);
                noText.setVisibility(View.GONE);
                loadreps();

            } else {
                if(Users.size() == 0){
                    noButton.setVisibility(VISIBLE);
                    pgsBar.setVisibility(View.GONE);
                    noText.setVisibility(VISIBLE);}
                else{
                    for(final user cn : Users){
                        mExampleList.add(new ExampleItem(cn.getAvatar(),cn.getName(),cn.getLanguage(),cn.getDescp(),cn.getStars()));

                    }
                    mAdapter = new ExampleAdapter(mExampleList);
                    recyclerView.setAdapter(mAdapter);
                    pgsBar.setVisibility(View.GONE);
                }

            }
        }
        else{
            Toast.makeText(getApplicationContext(), "no " +"no network", Toast.LENGTH_SHORT).show();

        }
        editText = findViewById(R.id.editTextSearch);
        //adding a TextChangedListener
        //to call a method whenever there is some change on the EditText
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //after the change calling the method and passing the search input
                filter(editable.toString());
            }
        });

        // implement setOnRefreshListener event on SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // cancel the Visual indication of a refresh
                mExampleList.clear();
                mAdapter = new ExampleAdapter(mExampleList);
                recyclerView.setAdapter(mAdapter);
                pgsBar.setVisibility(VISIBLE);
                loadreps();
                swipeRefreshLayout.setRefreshing(false);

            }
        });
    }
    private void filter(String text) {
        ArrayList<ExampleItem> filteredList = new ArrayList<>();
        for (ExampleItem item : mExampleList) {
            if (item.getText1().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        mAdapter.filterList(filteredList);
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
    public void loadrepss(View view){
        pgsBar.setVisibility(VISIBLE);
        loadreps();
    }
    private  boolean checkAndRequestPermissions() {
        int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return true;
        }
       return true;
    }
    void loadreps(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://private-anon-45bb4aa8eb-githubtrendingapi.apiary-mock.com/repositories";

// Request a string response from the provided URL.
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest
                (Request.Method.GET, url, (String) null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        for(Integer i=0; i<response.length(); i++){
                            try {
                                JSONObject sys  = response.getJSONObject(i);
                                mExampleList.add(new ExampleItem(sys.getString("avatar"),sys.getString("name"),sys.getString("language"),sys.getString("description"),sys.getString("stars")));

                                if(DbHelper.checkUser(sys.getString("name")) == false) {
                                    User = new user();

                                    String name=sys.getString("name");
                                    String avatar=sys.getString("avatar");
                                    String language=sys.getString("language");
                                    String descp=sys.getString("description");
                                    String stars=sys.getString("stars");
                                    User.setName(name);
                                    User.setAvatar(avatar);
                                    User.setLanguage(language);
                                    User.setDescp(descp);
                                    User.setStars(stars);
                                    DbHelper.addUser(User);


                                }
                                else {

                                    user UpdateUser = new user();
                                String avatar=sys.getString("avatar");
                                String language=sys.getString("language");
                                String descp=sys.getString("description");
                                String stars=sys.getString("stars");
                                UpdateUser.setName(UpdateUser.getName());
                                UpdateUser.setId(UpdateUser.getId());
                                UpdateUser.setAvatar(avatar);
                                UpdateUser.setLanguage(language);
                                UpdateUser.setDescp(descp);
                                UpdateUser.setStars(stars);
                                DbHelper.updateUser(UpdateUser);}
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        Log.d("ff", "onResponse: "+mExampleList+"");
                        pgsBar.setVisibility(View.GONE);
                        noButton.setVisibility(View.GONE);
                        noText.setVisibility(View.GONE);
                        mAdapter = new ExampleAdapter(mExampleList);
                        recyclerView.setAdapter(mAdapter);
                       // set the Adapter to RecyclerView
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(getApplicationContext(), "url3 is: " +error, Toast.LENGTH_SHORT).show();
                        noButton.setVisibility(VISIBLE);
                        pgsBar.setVisibility(View.GONE);
                        noText.setVisibility(VISIBLE);
                    }
                });

// Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);

    }



}