package ph.com.jeffreyvcabrera.stlukesdev.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

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

import ph.com.jeffreyvcabrera.stlukesdev.R;
import ph.com.jeffreyvcabrera.stlukesdev.adapters.ActionsListAdapter;
import ph.com.jeffreyvcabrera.stlukesdev.interfaces.AsyncTaskListener;
import ph.com.jeffreyvcabrera.stlukesdev.models.ActionsBody;
import ph.com.jeffreyvcabrera.stlukesdev.utils.API;
import ph.com.jeffreyvcabrera.stlukesdev.utils.Settings;

public class AddDiagnosis extends AppCompatActivity implements AsyncTaskListener{

    EditText content;
    Button save;
    ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_diagnosis);

        listview = (ListView) findViewById(R.id.listEdit);
        save = (Button) findViewById(R.id.save_diagnosis);
        content = (EditText) findViewById(R.id.content_diagnosis_et);

        new ListActionsAPI(this, "/api_patients/listaction/"+getIntent().getExtras().getInt("id")+"/"+2).execute();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp = AddDiagnosis.this.getSharedPreferences("USERINFO",0);
                Integer id = sp.getInt("id", 0);
                if (content.getText().toString().trim().equals("")) {
                    Toast.makeText(AddDiagnosis.this, "Cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    new API(AddDiagnosis.this, AddDiagnosis.this).execute("POST", "/api_patients/adddiagnosis/" + content.getText() + "/" + getIntent().getExtras().getInt("id") + "/" + id);
                }
            }
        });
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
                    String message = jObj.getString("msg");
                    Toast.makeText(this, "Diagnosis Added", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(this, PatientNotes.class);
                    Bundle bundle = new Bundle();
                    intent.putExtras(getBundle(bundle));
                    startActivity(intent);
                    finish();
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
        bundle.putInt("status", getIntent().getExtras().getInt("status"));
        bundle.putInt("change_dressing", getIntent().getExtras().getInt("change_dressing"));
        bundle.putInt("priority", getIntent().getExtras().getInt("priority"));
        bundle.putInt("post_op", getIntent().getExtras().getInt("post_op"));
        bundle.putInt("trach_care", getIntent().getExtras().getInt("trach_care"));

        return bundle;
    }

    class ListActionsAPI extends AsyncTask<String, Void, String> {
        Context act;
        ProgressDialog pd;
        String api_url;

        public ListActionsAPI(Context act, String api_url) {
            this.act = act;
            this.api_url = api_url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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
                    final ArrayList<ActionsBody> sampleModel = new ArrayList<>();

                    JSONArray activities_data = jObj.optJSONArray("data");
                    for(int x=0; x<activities_data.length();x++){
                        JSONObject a = activities_data.getJSONObject(x);

                        ActionsBody cma1 = new ActionsBody();

                        cma1.setContent(a.getString("content"));
                        cma1.setDate_posted(a.getString("date_created"));
                        cma1.setPhysician(a.getString("lastname"));
                        sampleModel.add(cma1);

                    }

                    ActionsListAdapter actionsListAdapter = new ActionsListAdapter(act, sampleModel);
                    listview.setAdapter(actionsListAdapter);

                } else {

                    String message = jObj.getString("error");
                    if (message.equals("No Records Found")) {

                    } else {
                    }

                }
            } catch (JSONException e) {
                Log.e("error", "Error parsing data" + e.toString());
            }
        }
    }

    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (!doubleBackToExitPressedOnce) {
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this,"Press back again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        } else {
            Intent intent = new Intent(this, PatientNotes.class);
            Bundle bundle = new Bundle();
            intent.putExtras(getBundle(bundle));
            startActivity(intent);
            finish();
        }
    }
}
