package ph.com.jeffreyvcabrera.stlukesdev.activities;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;

import ph.com.jeffreyvcabrera.stlukesdev.fragments.AddPatientFragment;
import ph.com.jeffreyvcabrera.stlukesdev.fragments.DashboardFragment;
import ph.com.jeffreyvcabrera.stlukesdev.fragments.PrimaryFragment;
import ph.com.jeffreyvcabrera.stlukesdev.R;
import ph.com.jeffreyvcabrera.stlukesdev.fragments.ProfileFragment;
import ph.com.jeffreyvcabrera.stlukesdev.utils.Settings;
import ph.com.jeffreyvcabrera.stlukesdev.utils.SharedPrefManager;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    private AQuery aq;
    NavigationView navigationView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.getSupportActionBar().setTitle("Dashboard");
        Intent detailsIntent = new Intent(this, MainActivity.class);

// Use TaskStackBuilder to build the back stack and get the PendingIntent
        PendingIntent pendingIntent =
                TaskStackBuilder.create(this)
                        // add all of DetailsActivity's parents to the stack,
                        // followed by DetailsActivity itself
                        .addNextIntentWithParentStack(detailsIntent)
                        .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentIntent(pendingIntent);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);

        toggle.syncState();

        SharedPrefManager spm = new SharedPrefManager(this);
        Integer id = spm.getUser().getId();
        Integer role = spm.getUser().getRole();
        String image = spm.getUser().getImage();
        String firstname = spm.getUser().getFirstname();
        String middlename = spm.getUser().getMiddlename();
        String lastname = spm.getUser().getLastname();
        String email = spm.getUser().getEmail();
        String mobile = spm.getUser().getMobile();

        id = spm.getUser().getId();
        role = spm.getUser().getRole();

        this.aq = new AQuery(this);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setActivated(false);
        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);
//        ImageView userImage = (ImageView) headerView.findViewById(R.id.userImage);
        TextView name = (TextView) headerView.findViewById(R.id.physician_name);
        TextView position = (TextView) headerView.findViewById(R.id.role);
        String[] roles = {"Admin", "Consultant", "Resident"};
        name.setText(firstname+" "+lastname);
        position.setText(roles[role]);
        String imgaq = Settings.local_url + "/assets/img/images/physicians/" + image;
//        if (image.equals("")) {
//            userImage.setImageResource(R.mipmap.person_placeholder);
//        } else {
//            aq.id(userImage).image(imgaq, false, true);
//        }


        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.containerView, new DashboardFragment()).commit();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Intent intent = new Intent(this, UserProfile.class);
            startActivity(intent);

        } else if (id == R.id.list_patients) {
            Intent intent = new Intent(this, PatientsList.class);
            startActivity(intent);

        } else if (id == R.id.nav_new_patient) {
            Intent intent = new Intent(this, PatientAdd.class);
            startActivity(intent);

        } else if (id == R.id.nav_logout) {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            MainActivity.this.getSupportActionBar().setTitle("Dashboard");
            android.app.FragmentManager fm = getFragmentManager();
            if (fm.getBackStackEntryCount() > 0) {

                fm.popBackStack(null, fm.POP_BACK_STACK_INCLUSIVE);
            } else {
                super.onBackPressed();
            }
        }
    }
}
