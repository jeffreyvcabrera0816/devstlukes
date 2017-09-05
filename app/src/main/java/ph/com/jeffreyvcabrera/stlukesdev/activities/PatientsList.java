package ph.com.jeffreyvcabrera.stlukesdev.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
//import android.widget.SearchView;
import android.support.v7.widget.SearchView;


import java.util.ArrayList;
import java.util.LinkedHashMap;

import ph.com.jeffreyvcabrera.stlukesdev.R;
import ph.com.jeffreyvcabrera.stlukesdev.fragments.PatientsListDefaultFragment;
import ph.com.jeffreyvcabrera.stlukesdev.fragments.PatientsListSearchFragment;
import ph.com.jeffreyvcabrera.stlukesdev.fragments.PatientsListSortByDateFragment;
import ph.com.jeffreyvcabrera.stlukesdev.fragments.PatientsListSortByNameFragment;
import ph.com.jeffreyvcabrera.stlukesdev.fragments.PatientsListSortByRoomFragment;
import ph.com.jeffreyvcabrera.stlukesdev.fragments.PatientsListSortByStatusFragment;
import ph.com.jeffreyvcabrera.stlukesdev.models.HeaderInfo;

/**
 * Created by Jeffrey on 6/5/2017.
 */

public class PatientsList extends AppCompatActivity {

    private LinkedHashMap<String, HeaderInfo> myDepartments = new LinkedHashMap<String, HeaderInfo>();
    private ArrayList<HeaderInfo> deptList = new ArrayList<HeaderInfo>();

    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patients_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Patients");

        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.containerView,new PatientsListDefaultFragment()).commit();

    }



    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
                menu.setGroupVisible(R.id.main_menu_group, false);
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                menu.setGroupVisible(R.id.main_menu_group, true);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                Bundle args = new Bundle();
                args.putString("query", query);
                Fragment detail = new PatientsListSearchFragment();
                detail.setArguments(args);
                FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.containerView, detail).commit();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.sort_room) {
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.containerView,new PatientsListSortByRoomFragment()).commit();
        }

        if (id == R.id.sort_name) {
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.containerView,new PatientsListSortByNameFragment()).commit();
        }

        if (id == R.id.sort_status) {
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.containerView,new PatientsListDefaultFragment()).commit();
        }

        if (id == R.id.sort_date) {
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.containerView,new PatientsListSortByDateFragment()).commit();
        }

        if (id == R.id.action_add) {
            Intent intent = new Intent(PatientsList.this, PatientAdd.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }


}
