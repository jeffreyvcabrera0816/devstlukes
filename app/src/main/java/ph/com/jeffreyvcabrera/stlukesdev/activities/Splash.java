package ph.com.jeffreyvcabrera.stlukesdev.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import org.json.JSONObject;

import ph.com.jeffreyvcabrera.stlukesdev.R;
import ph.com.jeffreyvcabrera.stlukesdev.interfaces.AsyncTaskListener;
import ph.com.jeffreyvcabrera.stlukesdev.models.UserModel;
import ph.com.jeffreyvcabrera.stlukesdev.utils.API;
import ph.com.jeffreyvcabrera.stlukesdev.utils.SharedPrefManager;
import ph.com.jeffreyvcabrera.stlukesdev.utils.SplashAPI;
import ph.com.jeffreyvcabrera.stlukesdev.utils.ValidationUtil;

/**
 * Created by Jeffrey on 9/4/2017.
 */

public class Splash extends Activity implements AsyncTaskListener{

    private static int SPLASH_TIME_OUT = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        SharedPrefManager spm = new SharedPrefManager(this);
        final String uemail = spm.getUser().getEmail();
        final String upw = spm.getUser().getMd5_password();
        final String umac = spm.getUser().getMac_address();
        final Boolean isLogged = spm.getUser().isLogged_in();

        if (isLogged) {
            if (ValidationUtil.isNetworkAvailable(Splash.this)) {
                initAPI(uemail, upw, umac);
            } else {
                ValidationUtil.showNoInternetAlert(Splash.this);
            }
        } else {
            if (ValidationUtil.isNetworkAvailable(Splash.this)) {

                new Handler().postDelayed(new Runnable() {

         /*
          * Showing splash screen with a timer. This will be useful when you
          * want to show case your app logo / company
          */

                    @Override
                    public void run() {
                        Intent i = new Intent(Splash.this, Login.class);
                        startActivity(i);
                        finish();
                    }
                }, SPLASH_TIME_OUT);

            } else {
                ValidationUtil.showNoInternetAlert(Splash.this);
            }

        }



    }

    private void initAPI(String email, String password, String mac) {
        new SplashAPI(this, this).execute("POST", "/api_users/login/" + email + "/" + password + "/" + mac);
    }

    @Override
    public void onTaskComplete(String result) {
        try {
            JSONObject jObj = new JSONObject(result);
            boolean success = jObj.getBoolean("success");

            if (success) {

                UserModel um = new UserModel();
                JSONObject activity_data = jObj.optJSONObject("user_data");

                Integer id = activity_data.optInt("id");
                Integer role = activity_data.optInt("role");
                Integer duty = activity_data.optInt("duty");
                Integer gender = activity_data.optInt("gender");
                String email = activity_data.optString("email");
                String firstname = activity_data.optString("firstname");
                String middlename = activity_data.optString("middlename");
                String lastname = activity_data.optString("lastname");
                String mobile = activity_data.optString("mobile");
                String mac_address = activity_data.optString("mac_address");
                String md5_password = activity_data.optString("password");
                String image = activity_data.optString("image");
                String date_added = activity_data.optString("date_added");
                String last_login = activity_data.optString("last_login");

                um.setId(id);
                um.setDuty(duty);
                um.setRole(role);
                um.setGender(gender);
                um.setEmail(email);
                um.setFirstname(firstname);
                um.setMiddlename(middlename);
                um.setLastname(lastname);
                um.setMobile(mobile);
                um.setMac_address(mac_address);
                um.setMd5_password(md5_password);
                um.setImage(image);
                um.setDate_added(date_added);
                um.setLast_login(last_login);
                um.setLogged_in(true);

                SharedPrefManager sm = new SharedPrefManager(this);
                sm.saveUser(um);

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Intent i = new Intent(Splash.this, Login.class);
                startActivity(i);
                finish();
            }
        } catch (Exception e) {

            String message = "An error occured";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }
}
