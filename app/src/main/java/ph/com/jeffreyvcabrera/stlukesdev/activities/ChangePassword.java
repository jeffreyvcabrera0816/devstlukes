package ph.com.jeffreyvcabrera.stlukesdev.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import ph.com.jeffreyvcabrera.stlukesdev.R;
import ph.com.jeffreyvcabrera.stlukesdev.adapters.PatientsListSortAdapter;
import ph.com.jeffreyvcabrera.stlukesdev.interfaces.AsyncTaskListener;
import ph.com.jeffreyvcabrera.stlukesdev.models.DetailInfo;
import ph.com.jeffreyvcabrera.stlukesdev.utils.API;
import ph.com.jeffreyvcabrera.stlukesdev.utils.Settings;
import ph.com.jeffreyvcabrera.stlukesdev.utils.SharedPrefManager;

/**
 * Created by Jeffrey on 9/6/2017.
 */

public class ChangePassword extends AppCompatActivity implements AsyncTaskListener {
    ScrollView mRootLayout;
    Button change_pass_button;
    EditText et_old_pass, et_new_pass, et_confirm_pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SharedPrefManager spm = new SharedPrefManager(this);
        final Integer id = spm.getUser().getId();
        final String old_pw = spm.getUser().getMd5_password();

        et_old_pass = (EditText) findViewById(R.id.et_old_pass);
        et_new_pass = (EditText) findViewById(R.id.et_new_pass);
        et_confirm_pass = (EditText) findViewById(R.id.et_confirm_pass);
        change_pass_button = (Button) findViewById(R.id.change_pass_button);
//        Toast.makeText(ChangePassword.this, old_pw, Toast.LENGTH_SHORT).show();
        change_pass_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPass = et_old_pass.getText().toString().trim();
                String newPass = et_new_pass.getText().toString().trim();
                String newPassConfirm = et_confirm_pass.getText().toString().trim();

                if (oldPass.isEmpty() || newPass.isEmpty() || newPassConfirm.isEmpty()) {
                    Toast.makeText(ChangePassword.this, "All fields are required", Toast.LENGTH_SHORT).show();
                }
                if (!(md5(oldPass)).equals(old_pw)) {
                    Toast.makeText(ChangePassword.this, md5(oldPass) +"\n"+old_pw, Toast.LENGTH_SHORT).show();
                } else {
                    if (newPass.equals(newPassConfirm)) {
                        new API(ChangePassword.this, ChangePassword.this).execute("POST", "/api_users/changepassword/"+id+"/"+md5(newPass));
                    } else {
                        Toast.makeText(ChangePassword.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_other, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so longf
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
//                NavUtils.navigateUpFromSameTask(this);
                onBackPressed();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTaskComplete(String result) {
        JSONObject jObj = null;
        if (result == "error") {
            Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
        } else {

            final ArrayList<DetailInfo> detailInfo = new ArrayList<>();
            try {
                jObj = new JSONObject(result);
                boolean success = jObj.getBoolean("success");

                if (success) {

                    Intent in = new Intent(ChangePassword.this, MainActivity.class);
                    startActivity(in);
                    Toast.makeText(this, "Your Password has been changed", Toast.LENGTH_LONG).show();
                    finish();

                } else {

                    String message = jObj.getString("error");
                    if (message.equals("No Records Found")) {

                        Toast.makeText(this, "No Data", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class ChangePass extends AsyncTask<String, Void, String> {
        Activity act;
        ProgressDialog pd;
        String api_url;

        public ChangePass (Activity act, String api_url) {
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
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(act);
            pd.setMessage("Loading...");
            pd.setCancelable(false);
            pd.show();
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
                String  service = jObj.getString("service");
                if (status) {
                    Intent in = new Intent(ChangePassword.this, MainActivity.class);
                    startActivity(in);
                    Toast.makeText(act, "Your Password has been changed", Toast.LENGTH_LONG).show();
                    finish();
                }
                else {
                    String message = jObj.getString("error");
                    Toast.makeText(act.getApplicationContext(), message, Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                Log.e("error", "Error parsing data" + e.toString());
            }
        }
    }

    private static final String md5(final String password) {
        try {

            MessageDigest digest = MessageDigest
                    .getInstance("MD5");
            digest.update(password.getBytes());
            byte messageDigest[] = digest.digest();

            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
