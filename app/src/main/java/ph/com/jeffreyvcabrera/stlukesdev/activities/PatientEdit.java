package ph.com.jeffreyvcabrera.stlukesdev.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.view.LayoutInflater;
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
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
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
import java.util.zip.Inflater;

import ph.com.jeffreyvcabrera.stlukesdev.R;
import ph.com.jeffreyvcabrera.stlukesdev.fragments.AddPatientFragment;
import ph.com.jeffreyvcabrera.stlukesdev.fragments.DashboardFragment;
import ph.com.jeffreyvcabrera.stlukesdev.interfaces.AsyncTaskListener;
import ph.com.jeffreyvcabrera.stlukesdev.utils.API;
import ph.com.jeffreyvcabrera.stlukesdev.utils.MySingleton;
import ph.com.jeffreyvcabrera.stlukesdev.utils.Settings;

public class PatientEdit extends AppCompatActivity implements AsyncTaskListener {

    private AQuery aq;
    private static final int ACTIVITY_START_CAMERA_APP = 0;
    private static final int REQUEST_EXTERNAL_STORAGE_RESULT = 0;
    private ImageView mPhotoCapturedImageView;
    private String mImageFileLocation = "";
    Bitmap photoReducedSizeBitmp = null;
    String imageFileName = "";
    EditText firstname, middlename, lastname, room, age;
    Button add_button;
    Integer id, tgender, getGender;
    Spinner spinner;
    String tfirstname, tmiddlename, tlastname, tage, troom, timage, fname, mname, lname, rm, nt;
    RadioGroup radioSexGroup;
    RadioButton radioSexButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_edit);

        this.aq = new AQuery(this);
        id = getIntent().getExtras().getInt("id");
        tfirstname = getIntent().getExtras().getString("firstname");
        tmiddlename = getIntent().getExtras().getString("middlename");
        tlastname = getIntent().getExtras().getString("lastname");
        tage = getIntent().getExtras().getString("age");
        tgender = getIntent().getExtras().getInt("gender");
        troom = getIntent().getExtras().getString("room");
        timage = getIntent().getExtras().getString("image");

        mPhotoCapturedImageView = (ImageView) findViewById(R.id.PhotoCaptured);
        firstname = (EditText) findViewById(R.id.firstname);
        middlename = (EditText) findViewById(R.id.middlename);
        lastname = (EditText) findViewById(R.id.lastname);
        room = (EditText) findViewById(R.id.room);
        age = (EditText) findViewById(R.id.age);
        add_button = (Button) findViewById(R.id.btnAdd);
        radioSexGroup = (RadioGroup) findViewById(R.id.radioSex);

        if (tgender == 1) {
            radioSexGroup.check(R.id.male);
        } else {
            radioSexGroup.check(R.id.female);
        }

        firstname.setText(tfirstname);
        lastname.setText(tlastname);
        room.setText(troom);
        age.setText(tage);

        String imgaq = Settings.local_url + "/assets/img/images/patients/" + timage;

        if (timage.equals("")) {
            mPhotoCapturedImageView.setImageResource(R.mipmap.person_placeholder);
        } else {
            imageFileName = timage;
            aq.id(R.id.PhotoCaptured).image(imgaq, true, true);
        }

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tfirstname = firstname.getText().toString();
                tmiddlename = "";
                tlastname = lastname.getText().toString();
                troom = room.getText().toString();
                tage = age.getText().toString();

                if (tfirstname.equals("") || tlastname.equals("") || troom.equals("")) {
                    Toast.makeText(PatientEdit.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    if (tmiddlename.equals("")) {
                        tmiddlename = "Unknown";
                    }
                    new API(PatientEdit.this, PatientEdit.this).execute("POST", "/api_patients/editPatient/" + id + "/" + tfirstname + "/" + tmiddlename + "/" + tlastname + "/" + troom + "/" + tage + "/" + tgender);
                }

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void process(View view) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            callCameraApp();
        } else {
            if (shouldShowRequestPermissionRationale(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "External storage permission required to save image", Toast.LENGTH_SHORT).show();
            }
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE_RESULT);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_EXTERNAL_STORAGE_RESULT) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callCameraApp();
            } else {
                Toast.makeText(this, "External write permission has not been granted, cannot save images", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void callCameraApp() {
        Intent callCameraApplicationIntent = new Intent();
        callCameraApplicationIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

        File photoFile = null;
        try {
            photoFile = createImageFile();

        } catch (IOException e) {
            e.printStackTrace();
        }
        String authorities = getApplicationContext().getPackageName() + ".fileprovider";
        Uri imageUri = FileProvider.getUriForFile(this, authorities, photoFile);
        callCameraApplicationIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(callCameraApplicationIntent, ACTIVITY_START_CAMERA_APP);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ACTIVITY_START_CAMERA_APP && resultCode == RESULT_OK) {

            rotateImage(setReducedImageSize(), true);
        }

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFile = "IMAGE_" + timeStamp;
        imageFileName = imageFile + ".jpg";
        String folder_main = "Patients";
        File f = new File(Environment.getExternalStorageDirectory(), folder_main);
        if (!f.exists()) {
            f.mkdirs();
        }
        File storageDirectory = Environment.getExternalStoragePublicDirectory(folder_main);
        File image = File.createTempFile(imageFile, ".jpg", storageDirectory);
        mImageFileLocation = image.getAbsolutePath();
        return image;
    }

    private Bitmap setReducedImageSize() {
        int targetImageViewWidth = mPhotoCapturedImageView.getWidth();
        int targetImageViewHeight = mPhotoCapturedImageView.getHeight();

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mImageFileLocation, bmOptions);
        int cameraImageWidth = bmOptions.outWidth;
        int cameraImageHeight = bmOptions.outHeight;

        int scaleFactor = Math.min(cameraImageWidth / targetImageViewWidth, cameraImageHeight / targetImageViewHeight);
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inJustDecodeBounds = false;

        photoReducedSizeBitmp = BitmapFactory.decodeFile(mImageFileLocation, bmOptions);
        return photoReducedSizeBitmp;

    }

    private void rotateImage(Bitmap bitmap, Boolean uploaded) {
        ExifInterface exifInterface = null;
        try {
            exifInterface = new ExifInterface(mImageFileLocation);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, exifInterface.ORIENTATION_UNDEFINED);
        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            default:
        }

        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        mPhotoCapturedImageView.setImageBitmap(rotatedBitmap);

    }

    private void uploadImage() {
        String url = Settings.local_url + "/api_patients/upload_image/patients";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jObj = null;
                        try {
                            jObj = new JSONObject(response);
                            boolean status = jObj.getBoolean("success");
                            if (status) {
                                Toast.makeText(PatientEdit.this, "Patient Profile Updated", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(PatientEdit.this, PatientNotes.class);
                                Bundle bundle = new Bundle();
                                intent.putExtras(getBundle(bundle));
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(PatientEdit.this, "False", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", error.toString());
                Toast.makeText(PatientEdit.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("filename", imageFileName);
                params.put("image", imageToString(photoReducedSizeBitmp));
                params.put("id", String.valueOf(id));
                return params;
            }
        };
        MySingleton.getInstance(PatientEdit.this).addToRequestQueue(stringRequest);
    }

    private String imageToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 35, byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes, Base64.DEFAULT);
    }

    @Override
    public void onTaskComplete(String result) {

        try {
            JSONObject jObj = new JSONObject(result);
            boolean status = jObj.getBoolean("success");

            if (status) {

                if (photoReducedSizeBitmp != null) {
                    uploadImage();
                } else {
                    Toast.makeText(PatientEdit.this, "Patient Profile Updated", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PatientEdit.this, PatientNotes.class);
                    Bundle bundle = new Bundle();
                    intent.putExtras(getBundle(bundle));
                    startActivity(intent);
                    finish();
                }

            } else {
                if (photoReducedSizeBitmp != null) {
                    uploadImage();
                } else {
                    Toast.makeText(PatientEdit.this, "No Changes", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PatientEdit.this, PatientNotes.class);
                    Bundle bundle = new Bundle();
                    intent.putExtras(getBundle(bundle));
                    startActivity(intent);
                    finish();
                }

            }
        } catch (JSONException e) {
            Log.e("error", "Error parsing data" + e.toString());
        }
    }

    public void cancel(View view) {
        Toast.makeText(PatientEdit.this, "Canceled", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(PatientEdit.this, PatientNotes.class);
        Bundle bundle = new Bundle();
        intent.putExtras(getBundle(bundle));
        startActivity(intent);
        finish();
    }

    private Bundle getBundle(Bundle bundle) {

        bundle.putString("firstname", tfirstname);
        bundle.putString("middlename", tmiddlename);
        bundle.putString("lastname", tlastname);
        bundle.putString("age", tage);
        bundle.putInt("gender", tgender);
        bundle.putString("room", troom);
        if (photoReducedSizeBitmp != null) {
            bundle.putString("image", imageFileName);
        } else {
            bundle.putString("image", getIntent().getExtras().getString("image"));
        }
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
            Intent intent = new Intent(PatientEdit.this, PatientNotes.class);
            Bundle bundle = new Bundle();
            intent.putExtras(getBundle(bundle));
            startActivity(intent);
            finish();
        }

    }
}

