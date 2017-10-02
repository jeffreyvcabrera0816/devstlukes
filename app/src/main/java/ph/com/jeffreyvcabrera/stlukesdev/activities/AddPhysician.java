package ph.com.jeffreyvcabrera.stlukesdev.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ph.com.jeffreyvcabrera.stlukesdev.R;
import ph.com.jeffreyvcabrera.stlukesdev.adapters.PhysiciansListAdapter;
import ph.com.jeffreyvcabrera.stlukesdev.interfaces.AsyncTaskListener;
import ph.com.jeffreyvcabrera.stlukesdev.models.PhysiciansModel;
import ph.com.jeffreyvcabrera.stlukesdev.utils.API;

/**
 * Created by Jeffrey on 9/25/2017.
 */

public class AddPhysician extends AppCompatActivity implements AsyncTaskListener {

    ListView listView;
    PhysiciansListAdapter listAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_physician);

        getSupportActionBar().setTitle("Add Physician");

        listView = (ListView) findViewById(R.id.physicians_list);
        init();
    }

    public void init() {
        new API(this, this).execute("POST", "/api_physicians/listconsultants");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_done, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_done) {
            Intent intent = new Intent(AddPhysician.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTaskComplete(String result) {
        Log.d("objectresult", result);
        JSONObject jObj = null;
        if (result == "error") {

        } else {

            final ArrayList<PhysiciansModel> physiciansModel = new ArrayList<>();
            try {
                jObj = new JSONObject(result);
                boolean success = jObj.getBoolean("success");

                if (success) {

                    JSONArray data = jObj.optJSONArray("data");
                    Log.d("response", data.toString());
                    for (int x = 0; x < data.length(); x++) {
                        JSONObject a = data.getJSONObject(x);

                        PhysiciansModel pm = new PhysiciansModel();
                        pm.setId(a.getInt("id"));
                        pm.setFirstname(a.getString("firstname"));
                        pm.setMiddlename(a.getString("middlename"));
                        pm.setLastname(a.getString("lastname"));
                        pm.setGender(a.getInt("gender"));

                        physiciansModel.add(pm);
                    }

                    listAdapter = new PhysiciansListAdapter(this, physiciansModel, getIntent().getExtras().getInt("new_id"));
                    listView.setAdapter(listAdapter);

                } else {



                    String message = jObj.getString("error");
                    if (message.equals("No Records Found")) {

                        Toast.makeText(this, "No Activity Found", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
