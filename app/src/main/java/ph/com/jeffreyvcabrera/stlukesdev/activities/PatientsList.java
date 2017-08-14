package ph.com.jeffreyvcabrera.stlukesdev.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
//import android.widget.SearchView;
import android.widget.Toast;
import android.support.v7.widget.SearchView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import ph.com.jeffreyvcabrera.stlukesdev.R;
import ph.com.jeffreyvcabrera.stlukesdev.adapters.PatientsListAdapter;
import ph.com.jeffreyvcabrera.stlukesdev.interfaces.AsyncTaskListener;
import ph.com.jeffreyvcabrera.stlukesdev.models.DetailInfo;
import ph.com.jeffreyvcabrera.stlukesdev.models.HeaderInfo;
import ph.com.jeffreyvcabrera.stlukesdev.utils.API;
import ph.com.jeffreyvcabrera.stlukesdev.utils.SharedPrefManager;

/**
 * Created by Jeffrey on 6/5/2017.
 */

public class PatientsList extends AppCompatActivity implements AsyncTaskListener, SwipeRefreshLayout.OnRefreshListener {

    private LinkedHashMap<String, HeaderInfo> myDepartments = new LinkedHashMap<String, HeaderInfo>();
    private ArrayList<HeaderInfo> deptList = new ArrayList<HeaderInfo>();

    private PatientsListAdapter listAdapter;
    private ExpandableListView myList;
    private SwipeRefreshLayout swipeRefreshLayout;
    Integer id, role;
    SearchView searchView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patients_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Patients");
        myList = (ExpandableListView) findViewById(R.id.myList);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipePatients);
        swipeRefreshLayout.setOnRefreshListener(this);

        SharedPrefManager spm = new SharedPrefManager(this);

        id = spm.getUser().getId();
        role = spm.getUser().getRole();

        new API(this, this).execute("POST", "/api_patients/list/id/"+role+"/"+id);
    }

    //here we maintain our products in various departments
    private int addProduct(Integer status_type, String status, String firstname, String middlename, String lastname, String age, Integer gender, String room, String date_admitted, String date_released, String diagnosis, Integer id, Integer change_dressing, Integer priority, Integer post_op, Integer trach_care, String image, String physician){

        int groupPosition = 0;

        //check the hash map if the group already exists
        HeaderInfo headerInfo = myDepartments.get(status);
        //add the group if doesn't exists
        if(headerInfo == null){
            headerInfo = new HeaderInfo();
            headerInfo.setName(status);
            myDepartments.put(status, headerInfo);
            deptList.add(headerInfo);
        }

        //get the children for the group
        ArrayList<DetailInfo> productList = headerInfo.getProductList();
        //size of the children list
        int listSize = productList.size();
        //add to the counter
        listSize++;

        //create a new child and add that to the group
        DetailInfo detailInfo = new DetailInfo();
        detailInfo.setId(id);
        detailInfo.setFirstname(firstname);
        detailInfo.setMiddlename(middlename);
        detailInfo.setLastname(lastname);
        detailInfo.setAge(age);
        detailInfo.setGender(gender);
        detailInfo.setRoom(room);
        detailInfo.setImage(image);
        detailInfo.setPhysician(physician);
        detailInfo.setDate_admitted(date_admitted);
        detailInfo.setDate_released(date_released);
        detailInfo.setChange_dressing(change_dressing);
        detailInfo.setPriority(priority);
        detailInfo.setPost_op(post_op);
        detailInfo.setTrach_care(trach_care);
        detailInfo.setDate_released(date_released);
        detailInfo.setStatus(status_type);
        detailInfo.setDiagnosis(diagnosis);

        productList.add(detailInfo);
        headerInfo.setProductList(productList);

        groupPosition = deptList.indexOf(headerInfo);
        return groupPosition;
    }

    private String headerName(int status) {
        String department = "";
        if (status == 1) {
            department = "Admission/s";
        } else if (status == 2) {
            department = "Referral/s";
        } else if (status == 3) {
            department = "Surgical/s";
        } else if (status == 4) {
            department = "In-Patient/s";
        } else if (status == 5) {
            department = "Discharge/s";
        } else if (status == 6) {
            department = "Emergency/s";
        } else if (status == 7) {
            department = "Mortalities/s";
        } else if (status == 8) {
            department = "Sign Out/s";
        } else if (status == 9) {
            department = "Fall/s";
        } else if (status == 10) {
            department = "Medication Error/s";
        } else if (status == 11) {
            department = "Morbidities/s";
        } else if (status == 12) {
            department = "Sentinel Events/s";
        } else if (status == 100) {
            department = "In-Patient/s";
        } else if (status == 200) {
            department = "Discharge/s";
        } else if (status == 300) {
            department = "Mortalities/s";
        } else if (status == 400) {
            department = "Sign Out/s";
        }

        return department;
    }

    @Override
    public void onTaskComplete(String result) {
        JSONObject jObj = null;
        if (result == "error") {
            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }
        } else {
            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }

            try {
                jObj = new JSONObject(result);
                boolean success = jObj.getBoolean("success");

                if (success) {

                    JSONArray data = jObj.optJSONArray("data");
                    Log.d("response", data.toString());
                    for (int x = 0; x < data.length(); x++) {
                        JSONObject a = data.getJSONObject(x);
                        if (a.getInt("status") != 4 && a.getInt("status") != 5 && a.getInt("status") != 7 && a.getInt("status") != 8 ) {
                            addProduct(a.getInt("status"), headerName(a.getInt("status")), a.getString("firstname"), a.getString("middlename"), a.getString("lastname"), a.getString("age"), a.getInt("gender"), a.getString("room"), a.getString("date_admitted"), a.getString("date_released"), (a.isNull("diagnosis")? "":a.getString("diagnosis")), a.getInt("id"), a.getInt("change_dressing"), a.getInt("priority"), a.getInt("post_op"), a.getInt("trach_care"), a.getString("image"), (a.isNull("physicians")? "":a.getString("physicians")));
                        }
                    }

                    for (int x = 0; x < data.length(); x++) {
                        JSONObject a = data.getJSONObject(x);

                        addProduct(a.getInt("status"), headerName(a.getInt("status_table")),a.getString("firstname"), a.getString("middlename"), a.getString("lastname"), a.getString("age"), a.getInt("gender"), a.getString("room"), a.getString("date_admitted"), a.getString("date_released"), (a.isNull("diagnosis")? "":a.getString("diagnosis")), a.getInt("id"), a.getInt("change_dressing"), a.getInt("priority"), a.getInt("post_op"), a.getInt("trach_care"), a.getString("image"), (a.isNull("physicians")? "":a.getString("physicians")));

                    }

                    listAdapter = new PatientsListAdapter(this, deptList);
                    myList.setAdapter(listAdapter);

                } else {

                    if (swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    String message = jObj.getString("error");
                    if (message.equals("No Records Found")) {

                        Toast.makeText(this, "No Activity Found", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                    }
                    myDepartments.clear();
                    deptList.clear();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
                menu.setGroupVisible(R.id.main_menu_group, false);
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                menu.setGroupVisible(R.id.main_menu_group, true);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                myDepartments.clear();
                deptList.clear();
                listAdapter.notifyDataSetChanged();
//                Toast.makeText(PatientsList.this, "test", Toast.LENGTH_SHORT).show();
                new API(PatientsList.this, PatientsList.this).execute("POST", "/api_patients/search/"+query+"/"+role+"/"+id);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void setItemsVisibility(Menu menu, MenuItem exception, boolean visible) {
        for (int i=0; i<menu.size(); ++i) {
            MenuItem item = menu.getItem(i);
            if (item != exception) item.setVisible(visible);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.sort_room) {
            myDepartments.clear();
            deptList.clear();
            new API(this, this).execute("POST", "/api_patients/list/room/"+role+"/"+id);
        }

        if (id == R.id.sort_name) {
            myDepartments.clear();
            deptList.clear();
            new API(this, this).execute("POST", "/api_patients/list/lastname/"+role+"/"+id);
        }

        if (id == R.id.sort_status) {
            myDepartments.clear();
            deptList.clear();
            new API(this, this).execute("POST", "/api_patients/list/status/"+role+"/"+id);
        }

        if (id == R.id.action_add) {
            Intent intent = new Intent(PatientsList.this, PatientAdd.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        myDepartments.clear();
        deptList.clear();
        new API(this, this).execute("POST", "/api_patients/list/id/"+role+"/"+id);

    }
}
