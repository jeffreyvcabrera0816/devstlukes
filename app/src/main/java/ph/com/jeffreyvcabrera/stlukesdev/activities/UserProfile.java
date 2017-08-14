package ph.com.jeffreyvcabrera.stlukesdev.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.androidquery.AQuery;

import ph.com.jeffreyvcabrera.stlukesdev.R;
import ph.com.jeffreyvcabrera.stlukesdev.utils.ImageViewRounded;
import ph.com.jeffreyvcabrera.stlukesdev.utils.Settings;
import ph.com.jeffreyvcabrera.stlukesdev.utils.SharedPrefManager;

public class UserProfile extends AppCompatActivity {

    AQuery aq;
    EditText fullname, role, mobile, email;
    ImageViewRounded imageRound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        aq = new AQuery(this);

        getSupportActionBar().setTitle("My Profile");
        imageRound = (ImageViewRounded) findViewById(R.id.doctorImage);
        fullname = (EditText) findViewById(R.id.fullname);
        role = (EditText) findViewById(R.id.role);
        mobile = (EditText) findViewById(R.id.mobile);
        email = (EditText) findViewById(R.id.email);

        SharedPrefManager spm = new SharedPrefManager(this);

        Integer id = spm.getUser().getId();
        Integer position = spm.getUser().getRole();
        String image = spm.getUser().getImage();
        String firstname = spm.getUser().getFirstname();
        String lastname = spm.getUser().getLastname();
        String em = spm.getUser().getEmail();
        String mob = spm.getUser().getMobile();
        String[] roles = {"Admin", "Consultant", "Resident"};
        String imgaq = Settings.local_url + "/assets/img/images/physicians/" + image;

        if (image.equals("")) {
            imageRound.setImageResource(R.mipmap.person_placeholder);
        } else {
            aq.id(imageRound).image(imgaq, true, true);
        }

        fullname.setText(firstname+" "+lastname);
        mobile.setText(mob);
        email.setText(em);
        role.setText(roles[position]);
    }
}
