package ph.com.jeffreyvcabrera.stlukesdev.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidquery.AQuery;

import ph.com.jeffreyvcabrera.stlukesdev.R;
import ph.com.jeffreyvcabrera.stlukesdev.utils.ImageViewRounded;
import ph.com.jeffreyvcabrera.stlukesdev.utils.Settings;
import ph.com.jeffreyvcabrera.stlukesdev.utils.SharedPrefManager;

/**
 * Created by Jeffrey on 2/21/2017.
 */

public class ProfileFragment extends Fragment {

    AQuery aq;
    TextView fullname, role, mobile, email;
    ImageViewRounded imageRound;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.profile_fragment, container, false);
        aq = new AQuery(v);

//        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("My Profile");
        imageRound = (ImageViewRounded) v.findViewById(R.id.doctorImage);
        fullname = (TextView) v.findViewById(R.id.fullname);
        role = (TextView) v.findViewById(R.id.role);
        mobile = (TextView) v.findViewById(R.id.mobile);
        email = (TextView) v.findViewById(R.id.email);



        SharedPrefManager spm = new SharedPrefManager(getActivity());

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
        return v;
    }


}
