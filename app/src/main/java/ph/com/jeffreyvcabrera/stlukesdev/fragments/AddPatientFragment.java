package ph.com.jeffreyvcabrera.stlukesdev.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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
import java.util.List;

import ph.com.jeffreyvcabrera.stlukesdev.R;
import ph.com.jeffreyvcabrera.stlukesdev.activities.PatientsList;
import ph.com.jeffreyvcabrera.stlukesdev.adapters.PatientsListAdapter;
import ph.com.jeffreyvcabrera.stlukesdev.interfaces.AsyncTaskListener;
import ph.com.jeffreyvcabrera.stlukesdev.utils.API;
import ph.com.jeffreyvcabrera.stlukesdev.utils.Settings;

/**
 * Created by Jeffrey on 2/21/2017.
 */

public class AddPatientFragment extends Fragment {

    EditText firstname, middlename, lastname, room, age;
    Button add_button;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_patient_fragment, container, false);

        firstname = (EditText) v.findViewById(R.id.firstname);
        middlename = (EditText) v.findViewById(R.id.middlename);
        lastname = (EditText) v.findViewById(R.id.lastname);
        room = (EditText) v.findViewById(R.id.room);
        age = (EditText) v.findViewById(R.id.age);
        add_button = (Button) v.findViewById(R.id.add_button);
        final Spinner gender = (Spinner) v.findViewById(R.id.gender);
        List<String> categories = new ArrayList<String>();
        categories.add("Male");
        categories.add("Female");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        gender.setAdapter(dataAdapter);

        // Spinner click listener
        gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fname;
                String mname;
                String lname;
                String rm;
                String nt;
                String gend;

                fname = firstname.getText().toString();
                mname = middlename.getText().toString();
                lname = lastname.getText().toString();
                rm = room.getText().toString();
                nt = age.getText().toString();

                if (fname.equals("") || lname.equals("") || rm.equals("")) {
                    Toast.makeText(getActivity(), "Please fill the required fields", Toast.LENGTH_SHORT).show();
                } else {
                    new APIAdd(getActivity(), "/api_patients/addpatient/"+fname+"/"+mname+"/"+lname+"/"+rm+"/"+nt).execute();
                }

            }
        });

        return v;
    }

    class APIAdd extends AsyncTask<String, Void, String> {
        Context act;
        ProgressDialog progressDialog;
        String api_url;

        public APIAdd(Context act, String api_url) {
            this.act = act;
            this.api_url = api_url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(act);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }

            ConnectivityManager cm =
                    (ConnectivityManager)act.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
            if (!isConnected) {
                Toast.makeText(act, "No Internet Connection", Toast.LENGTH_LONG).show();
            }

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
            String url = Settings.local_url + api_url;

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
            if (progressDialog != null) {
                progressDialog.dismiss();
            }

            try {
                JSONObject jObj = new JSONObject(result);
                boolean status = jObj.getBoolean("success");
//                String service = jObj.getString("service");

                if (status) {
                    Toast.makeText(act, "Patient Added", Toast.LENGTH_SHORT).show();
                    fragmentManager = getFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.containerView, new DashboardFragment()).commit();
                    Intent intent = new Intent(act, PatientsList.class);
                    act.startActivity(intent);

                } else {
                    Toast.makeText(act, "An Error Ocurred", Toast.LENGTH_LONG).show();


                }
            } catch (JSONException e) {
                Log.e("error", "Error parsing data" + e.toString());
            }
        }
    }
}
