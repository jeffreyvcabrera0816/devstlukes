package ph.com.jeffreyvcabrera.stlukesdev.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.androidquery.AQuery;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ph.com.jeffreyvcabrera.stlukesdev.R;
import ph.com.jeffreyvcabrera.stlukesdev.interfaces.AsyncTaskListener;
import ph.com.jeffreyvcabrera.stlukesdev.utils.API;
import ph.com.jeffreyvcabrera.stlukesdev.utils.MySingleton;
import ph.com.jeffreyvcabrera.stlukesdev.utils.Settings;

public class PatientAdd extends AppCompatActivity implements AsyncTaskListener {

    EditText firstname, middlename, lastname, room, age;
    Button add_button;
    Integer id, tgender;
    Spinner spinner;
    String tfirstname, tmiddlename, tlastname, tage, troom;
    RadioGroup radioSexGroup;
    RadioButton radioSexButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_add);

        firstname = (EditText) findViewById(R.id.firstname);
        middlename = (EditText) findViewById(R.id.middlename);
        lastname = (EditText) findViewById(R.id.lastname);
        room = (EditText) findViewById(R.id.room);
        age = (EditText) findViewById(R.id.age);
        add_button = (Button) findViewById(R.id.btnAdd);
        radioSexGroup = (RadioGroup) findViewById(R.id.radioSex);

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tfirstname = firstname.getText().toString().trim();
                tmiddlename = "";
                tlastname = lastname.getText().toString().trim();
                troom = room.getText().toString().trim();
                tage = age.getText().toString().trim();

                int selectedId = radioSexGroup.getCheckedRadioButtonId();
                radioSexButton = (RadioButton) findViewById(selectedId);

                if (radioSexButton.getText().equals("male")) {
                    tgender = 1;
                } else {
                    tgender = 2;
                }

                if (tfirstname.equals("") || tlastname.equals("") || troom.equals("") || tage.equals("")) {
                    Toast.makeText(PatientAdd.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    if (tmiddlename.equals("")) {
                        tmiddlename = "Unknown";
                    }
                    new API(PatientAdd.this, PatientAdd.this).execute("POST", "/api_patients/addpatient/" + tfirstname + "/" + tmiddlename + "/" + tlastname + "/" + troom + "/" + tage + "/" + tgender);
                }

            }
        });
    }

    @Override
    public void onTaskComplete(String result) {

        try {
            JSONObject jObj = new JSONObject(result);
            boolean status = jObj.getBoolean("success");


            if (status) {
                Integer patient_id = jObj.getInt("data");
                Toast.makeText(PatientAdd.this, "New Patient Added", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PatientAdd.this, ImageAdd.class);
                Bundle bundle = new Bundle();
                bundle.putInt("new_id", patient_id);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();

            } else {
                Toast.makeText(PatientAdd.this, "An Error Occured", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            Log.e("error", "Error parsing data" + e.toString());
        }
    }

    public void cancel(View view) {
        Toast.makeText(PatientAdd.this, "Canceled", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(PatientAdd.this, PatientsList.class);
        startActivity(intent);
        finish();
    }

    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
//        Toast.makeText(PatientAdd.this, "Press back again to exit", Toast.LENGTH_SHORT).show();
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
            Intent intent = new Intent(PatientAdd.this, PatientsList.class);
            startActivity(intent);
            finish();
        }

    }
}

