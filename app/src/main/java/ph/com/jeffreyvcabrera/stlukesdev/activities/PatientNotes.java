package ph.com.jeffreyvcabrera.stlukesdev.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import ph.com.jeffreyvcabrera.stlukesdev.R;
import ph.com.jeffreyvcabrera.stlukesdev.adapters.ActionsAdapter;
import ph.com.jeffreyvcabrera.stlukesdev.adapters.ActionsNeededAdapter;
import ph.com.jeffreyvcabrera.stlukesdev.adapters.PatientsListAdapter;
import ph.com.jeffreyvcabrera.stlukesdev.interfaces.AsyncTaskListener;
import ph.com.jeffreyvcabrera.stlukesdev.models.ActionsBody;
import ph.com.jeffreyvcabrera.stlukesdev.models.ActionsHeader;
import ph.com.jeffreyvcabrera.stlukesdev.models.ActionsNeededModel;
import ph.com.jeffreyvcabrera.stlukesdev.models.DetailInfo;
import ph.com.jeffreyvcabrera.stlukesdev.models.HeaderInfo;
import ph.com.jeffreyvcabrera.stlukesdev.utils.API;
import ph.com.jeffreyvcabrera.stlukesdev.utils.Settings;

import static ph.com.jeffreyvcabrera.stlukesdev.R.id.toolbar;

public class PatientNotes extends AppCompatActivity implements AsyncTaskListener {

    private LinkedHashMap<String, ActionsHeader> myDepartments = new LinkedHashMap<String, ActionsHeader>();
    private ArrayList<ActionsHeader> deptList = new ArrayList<ActionsHeader>();
    Toolbar toolbar;
    Animation button_animation;
    private ActionsAdapter listAdapter;
    private ExpandableListView myList;
    TextView name, age, physician, status, room, change_status;
    Integer cur_stat;
    ImageView image;
    private AQuery aq;
    ImageView stat1, stat2, stat3, stat4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actions_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(),R.mipmap.letter_i_yellow);

        load();

    }

    public void load() {
        this.aq = new AQuery(this);

        myList = (ExpandableListView) findViewById(R.id.actionsExp);
        final ViewGroup headerView = (ViewGroup)getLayoutInflater().inflate(R.layout.patient_notes, myList,false);
        name = (TextView) headerView.findViewById(R.id.name);
        age = (TextView) headerView.findViewById(R.id.age);
        physician = (TextView) headerView.findViewById(R.id.physician);
        status = (TextView) headerView.findViewById(R.id.status);
        room = (TextView) headerView.findViewById(R.id.room);
        image = (ImageView) headerView.findViewById(R.id.image);
        change_status = (TextView) headerView.findViewById(R.id.change_status);
        stat1 = (ImageView) headerView.findViewById(R.id.stat1);
        stat2 = (ImageView) headerView.findViewById(R.id.stat2);
        stat3 = (ImageView) headerView.findViewById(R.id.stat3);
        stat4 = (ImageView) headerView.findViewById(R.id.stat4);
        button_animation = AnimationUtils.loadAnimation(PatientNotes.this, R.anim.button_animate);

        myList.addHeaderView(headerView);
        new API(this, this).execute("POST", "/api_patients/actionsNeeded/"+getIntent().getExtras().getInt("id"));

        String imgaq = Settings.local_url + "/assets/img/images/patients/" + getIntent().getExtras().getString("image");
        if ((getIntent().getExtras().getString("image")).equals("")) {
            image.setImageResource(R.mipmap.person_placeholder);
        } else {
            aq.id(R.id.image).image(imgaq, false, true);
        }


        name.setText(getIntent().getExtras().getString("lastname") + ", " + getIntent().getExtras().getString("firstname"));
        String gender = "";
        if (getIntent().getExtras().getInt("gender") == 1) {
            gender = "M";
        } else {
            gender = "F";
        }
        age.setText(getIntent().getExtras().getString("age") + " " + gender);

        status.setText("Status: "+statusName(getIntent().getExtras().getInt("status")));
        room.setText(getIntent().getExtras().getString("room"));

        if (getIntent().getExtras().getString("physicians").equals("")) {
            physician.setText("Physician/s: Not Assigned");
        } else {
            physician.setText("Physician/s: "+getIntent().getExtras().getString("physicians"));
        }


        if (getIntent().getExtras().getInt("change_dressing") == 1) {
            stat1.setImageResource(R.mipmap.triangle_yellow);
        } else {
            stat1.setImageResource(R.mipmap.triangle_black);
        }

        if (getIntent().getExtras().getInt("priority") == 1) {
            stat2.setImageResource(R.mipmap.star_yellow);
        } else {
            stat2.setImageResource(R.mipmap.star_black);
        }

        if (getIntent().getExtras().getInt("post_op") == 1) {
            stat3.setImageResource(R.mipmap.letter_p_yellow);
        } else {
            stat3.setImageResource(R.mipmap.letter_p_black);
        }

        if (getIntent().getExtras().getInt("trach_care") == 1) {
            stat4.setImageResource(R.mipmap.letter_i_yellow);
        } else {
            stat4.setImageResource(R.mipmap.letter_i_black);
        }

        stat1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stat1.startAnimation(button_animation);
                if (getIntent().getExtras().getInt("change_dressing") == 1) {
                    new changeStatusAPI(PatientNotes.this, "/api_patients/changestatus/"+1+"/"+getIntent().getExtras().getInt("id")+"/"+0).execute();
                } else {
                    new changeStatusAPI(PatientNotes.this, "/api_patients/changestatus/"+1+"/"+getIntent().getExtras().getInt("id")+"/"+1).execute();
                }
            }
        });

        stat2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stat2.startAnimation(button_animation);
                if (getIntent().getExtras().getInt("priority") == 1) {
                    new changeStatusAPI(PatientNotes.this, "/api_patients/changestatus/"+2+"/"+getIntent().getExtras().getInt("id")+"/"+0).execute();
                } else {
                    new changeStatusAPI(PatientNotes.this, "/api_patients/changestatus/"+2+"/"+getIntent().getExtras().getInt("id")+"/"+1).execute();
                }
            }
        });

        stat3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stat3.startAnimation(button_animation);
                if (getIntent().getExtras().getInt("post_op") == 1) {
                    new changeStatusAPI(PatientNotes.this, "/api_patients/changestatus/"+3+"/"+getIntent().getExtras().getInt("id")+"/"+0).execute();
                } else {
                    new changeStatusAPI(PatientNotes.this, "/api_patients/changestatus/"+3+"/"+getIntent().getExtras().getInt("id")+"/"+1).execute();
                }
            }
        });

        stat4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stat4.startAnimation(button_animation);
                if (getIntent().getExtras().getInt("trach_care") == 1) {
                    new changeStatusAPI(PatientNotes.this, "/api_patients/changestatus/"+4+"/"+getIntent().getExtras().getInt("id")+"/"+0).execute();
                } else {
                    new changeStatusAPI(PatientNotes.this, "/api_patients/changestatus/"+4+"/"+getIntent().getExtras().getInt("id")+"/"+1).execute();
                }
            }
        });

        cur_stat = getIntent().getExtras().getInt("status");
        change_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater li = LayoutInflater.from(PatientNotes.this);
                View promptsView = li.inflate(R.layout.change_status_view, null);
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PatientNotes.this);
                alertDialogBuilder.setView(promptsView);

                final AlertDialog alertDialog = alertDialogBuilder.create();

                final Spinner status_array = (Spinner) promptsView.findViewById(R.id.status_array);
                ArrayAdapter<String> adapter;
                final String[] list = {"Admission", "Referral", "Surgical", "In-Patient", "Discharge", "Emergency", "Mortalities", "Sign Out", "Fall", "Medication Error", "Morbidities", "Sentinel Events"};

                adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, list);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                status_array.setAdapter(adapter);
                status_array.setSelection(cur_stat-1);

                status_array.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                        if (!String.valueOf(cur_stat-1).equals(String.valueOf(arg2))) {
                            cur_stat = arg2+1;
                            new changeStatusAPI2(PatientNotes.this, "/api_patients/changestat/"+cur_stat+"/"+getIntent().getExtras().getInt("id")).execute();
                            alertDialog.dismiss();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub
                    }
                });

                alertDialog.show();
            }
        });

    }

    private String statusName(Integer status) {
        String department = "";
        if (status == 1) {
            department = "Admission";
        } else if (status == 2) {
            department = "Referral";
        } else if (status == 3) {
            department = "Surgical";
        } else if (status == 4) {
            department = "In-Patient";
        } else if (status == 5) {
            department = "Discharge";
        } else if (status == 6) {
            department = "Emergency";
        } else if (status == 7) {
            department = "Mortalities";
        } else if (status == 8) {
            department = "Sign Out";
        } else if (status == 9) {
            department = "Fall";
        } else if (status == 10) {
            department = "Medication Error";
        } else if (status == 11) {
            department = "Morbidities";
        } else if (status == 12) {
            department = "Sentinel Event";
        }

        return department;
    }

    private int addProduct(String status, String content, String date_created, String name){

        int groupPosition = 0;

        //check the hash map if the group already exists
        ActionsHeader headerInfo = myDepartments.get(status);
        //add the group if doesn't exists
        if(headerInfo == null){
            headerInfo = new ActionsHeader();
            headerInfo.setName(status);
            myDepartments.put(status, headerInfo);
            deptList.add(headerInfo);
        }

        //get the children for the group
        ArrayList<ActionsBody> actionsList = headerInfo.getActionsList();
        //size of the children list
        int listSize = actionsList.size();
        //add to the counter
        listSize++;

        //create a new child and add that to the group
        ActionsBody detailInfo = new ActionsBody();
        detailInfo.setContent(content);
        detailInfo.setDate_posted(date_created);
        detailInfo.setPhysician(name);

        actionsList.add(detailInfo);
        headerInfo.setActionsList(actionsList);

        //find the group position inside the list
        groupPosition = deptList.indexOf(headerInfo);
        return groupPosition;
    }

    @Override
    public void onTaskComplete(String result) {
        JSONObject jObj = null;
        if (result == "error") {

        } else {

            try {
                jObj = new JSONObject(result);
                boolean success = jObj.getBoolean("success");

                if (success) {
//                    JSONArray physicians = jObj.optJSONArray("physicians");
                    JSONArray actions_needed = jObj.optJSONArray("actions_needed");
                    JSONArray diagnosis = jObj.optJSONArray("diagnosis");
                    JSONArray notes = jObj.optJSONArray("notes");
                    JSONArray surgical_procedures = jObj.optJSONArray("surgical_procedures");

                    for (int x = 0; x < actions_needed.length(); x++) {
                        JSONObject a = actions_needed.getJSONObject(x);
                        addProduct("Actions Needed",a.getString("content"), a.getString("date_created"), a.getString("firstname")+" "+a.getString("lastname"));

                    }

                    for (int x = 0; x < diagnosis.length(); x++) {
                        JSONObject a = diagnosis.getJSONObject(x);
                        addProduct("Diagnosis",a.getString("content"), a.getString("date_created"), a.getString("firstname")+" "+a.getString("lastname"));

                    }

                    for (int x = 0; x < notes.length(); x++) {
                        JSONObject a = notes.getJSONObject(x);
                        addProduct("Notes",a.getString("content"), a.getString("date_created"), a.getString("firstname")+" "+a.getString("lastname"));

                    }

                    for (int x = 0; x < surgical_procedures.length(); x++) {
                        JSONObject a = surgical_procedures.getJSONObject(x);
                        addProduct("Surgical Procedures",a.getString("content"), a.getString("date_created"), a.getString("firstname")+" "+a.getString("lastname"));

                    }

                    listAdapter = new ActionsAdapter(PatientNotes.this, deptList);
                    myList.setAdapter(listAdapter);

                } else {


                    String message = jObj.getString("error");
                    if (message.equals("No Records Found")) {

                        Toast.makeText(this, "No Activity Found", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add_action) {
            Intent intent = new Intent(this, AddAction.class);
            Bundle bundle = new Bundle();
            intent.putExtras(getBundle(bundle));
            startActivity(intent);
            finish();
        }

        if (id == R.id.add_diagnosis) {
            Intent intent = new Intent(this, AddDiagnosis.class);
            Bundle bundle = new Bundle();
            intent.putExtras(getBundle(bundle));
            startActivity(intent);
            finish();
        }

        if (id == R.id.add_note) {
            Intent intent = new Intent(this, AddNotes.class);
            Bundle bundle = new Bundle();
            intent.putExtras(getBundle(bundle));
            startActivity(intent);
            finish();
        }

        if (id == R.id.add_procedure) {
            Intent intent = new Intent(this, AddSurgicalProcedure.class);
            Bundle bundle = new Bundle();
            intent.putExtras(getBundle(bundle));
            startActivity(intent);
            finish();
        }

        if (id == R.id.patient_edit) {
            Intent intent = new Intent(this, PatientEdit.class);
            Bundle bundle = new Bundle();
            intent.putExtras(getBundle(bundle));
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private Bundle getBundle(Bundle bundle) {

        bundle.putString("firstname", getIntent().getExtras().getString("firstname"));
        bundle.putString("middlename", getIntent().getExtras().getString("middlename"));
        bundle.putString("lastname", getIntent().getExtras().getString("lastname"));
        bundle.putString("age", getIntent().getExtras().getString("age"));
        bundle.putInt("gender", getIntent().getExtras().getInt("gender"));
        bundle.putString("room", getIntent().getExtras().getString("room"));
        bundle.putString("image", getIntent().getExtras().getString("image"));
        bundle.putString("physicians", getIntent().getExtras().getString("physicians"));
        bundle.putString("date_admitted", getIntent().getExtras().getString("date_admitted"));
        bundle.putString("date_released", getIntent().getExtras().getString("date_released"));
        bundle.putInt("id", getIntent().getExtras().getInt("id"));
        bundle.putInt("status", cur_stat);
        bundle.putInt("change_dressing", getIntent().getExtras().getInt("change_dressing"));
        bundle.putInt("priority", getIntent().getExtras().getInt("priority"));
        bundle.putInt("post_op", getIntent().getExtras().getInt("post_op"));
        bundle.putInt("trach_care", getIntent().getExtras().getInt("trach_care"));

        return bundle;
    }

    class changeStatusAPI extends AsyncTask<String, Void, String> {
        Context act;
        ProgressDialog pd;
        String api_url;

        public changeStatusAPI(Context act, String api_url) {
            this.act = act;
            this.api_url = api_url;
        }

        @Override
        protected String doInBackground(String... params) {

            HttpClient httpclient;
            HttpPost httppost;
            HttpResponse response;
            HttpEntity entity;
            InputStream isr = null;
            String result = "";
            String test_url = api_url + "/";
            String url = Settings.local_url + test_url;

            try {
                httpclient = new DefaultHttpClient();
                httppost = new HttpPost(url);
                response = httpclient.execute(httppost);
                entity = response.getEntity();
                isr = entity.getContent();
            } catch (Exception e) {
                Log.e("log_tag", "Error in http connection " + e.toString());
            }

            //convert response to string
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(isr, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                isr.close();

                result = sb.toString();
            } catch (Exception e) {
                Log.e("log_tag", "Error converting result" + e.toString());
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);
            if (pd != null) {
                pd.dismiss();
            }

            try {
                JSONObject jObj = new JSONObject(result);
                boolean status = jObj.getBoolean("success");
                String service = jObj.getString("service");

                if (status) {
                    Toast.makeText(act, "Status Changed", Toast.LENGTH_SHORT).show();
                } else {
                    String message = jObj.getString("error");
                    Toast.makeText(act, "Error", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                Log.e("error", "Error parsing data" + e.toString());
            }
        }
    }

    class changeStatusAPI2 extends AsyncTask<String, Void, String> {
        Context act;
        ProgressDialog pd;
        String api_url;

        public changeStatusAPI2(Context act, String api_url) {
            this.act = act;
            this.api_url = api_url;
        }

        @Override
        protected String doInBackground(String... params) {

            HttpClient httpclient;
            HttpPost httppost;
            HttpResponse response;
            HttpEntity entity;
            InputStream isr = null;
            String result = "";
            String test_url = api_url + "/";
            String url = Settings.local_url + test_url;

            try {
                httpclient = new DefaultHttpClient();
                httppost = new HttpPost(url);
                response = httpclient.execute(httppost);
                entity = response.getEntity();
                isr = entity.getContent();
            } catch (Exception e) {
                Log.e("log_tag", "Error in http connection " + e.toString());
            }

            //convert response to string
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(isr, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                isr.close();

                result = sb.toString();
            } catch (Exception e) {
                Log.e("log_tag", "Error converting result" + e.toString());
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);
            if (pd != null) {
                pd.dismiss();
            }

            try {
                JSONObject jObj = new JSONObject(result);
                boolean success = jObj.getBoolean("success");
                String service = jObj.getString("service");

                if (success) {
                    Toast.makeText(act, "Status Changed", Toast.LENGTH_SHORT).show();
                    status.setText("Status: "+statusName(cur_stat));
                } else {
                    String message = jObj.getString("error");
                    Toast.makeText(act, "Error", Toast.LENGTH_LONG).show();


                }
            } catch (JSONException e) {
                Log.e("error", "Error parsing data" + e.toString());
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PatientNotes.this, PatientsList.class);
        Bundle bundle = new Bundle();
        intent.putExtras(getBundle(bundle));
        startActivity(intent);
        finish();
    }
}
