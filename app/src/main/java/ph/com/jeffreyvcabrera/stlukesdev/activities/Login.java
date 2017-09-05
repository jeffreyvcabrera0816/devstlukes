package ph.com.jeffreyvcabrera.stlukesdev.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

import ph.com.jeffreyvcabrera.stlukesdev.R;
import ph.com.jeffreyvcabrera.stlukesdev.interfaces.AsyncTaskListener;
import ph.com.jeffreyvcabrera.stlukesdev.models.UserModel;
import ph.com.jeffreyvcabrera.stlukesdev.utils.API;
import ph.com.jeffreyvcabrera.stlukesdev.utils.Md5;
import ph.com.jeffreyvcabrera.stlukesdev.utils.SharedPrefManager;
import ph.com.jeffreyvcabrera.stlukesdev.utils.ValidationUtil;

public class Login extends AppCompatActivity implements AsyncTaskListener {

    EditText edtEmail;
    EditText edtPass;
    TextInputLayout mEmailLayout, mPasswordLayout;
    Button sign_in_button;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private void init() {
        edtEmail = (EditText) findViewById(R.id.email);
        edtPass = (EditText) findViewById(R.id.password);
        sign_in_button = (Button) findViewById(R.id.sign_in_button);

        sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isDataValid();
//                Toast.makeText(Login.this, "test", Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void isDataValid() {
        // TODO Auto-generated method stub
        boolean isValid = ValidationUtil.isEmailDataValid(edtEmail, this,
                mEmailLayout);
        if (isValid) {
            isValid = ValidationUtil.isPasswordDataValid(edtPass, this,
                    mPasswordLayout);
            if (isValid) {
                if (ValidationUtil.isNetworkAvailable(this)) {
                    new API(this, this).execute("POST", "/api_users/login/" + edtEmail.getText() + "/" + Md5.md5(String.valueOf(edtPass.getText())) + "/" + getMac());

                } else {
                    ValidationUtil.showNoInternetAlert(this);
                }
            }
        }
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
                String message = jObj.getString("error");
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {

            String message = "An error occured";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    public static String getMac() {
        try {
            String interfaceName = "wlan0";
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                if (!intf.getName().equalsIgnoreCase(interfaceName)) {
                    continue;
                }

                byte[] mac = intf.getHardwareAddress();
                if (mac == null) {
                    return "";
                }

                StringBuilder buf = new StringBuilder();
                for (byte aMac : mac) {
                    buf.append(String.format("%02X:", aMac));
                }
                if (buf.length() > 0) {
                    buf.deleteCharAt(buf.length() - 1);
                }
                return buf.toString();
            }
        } catch (Exception ex) {
        } // for now eat exceptions
        return "";

    }
}
