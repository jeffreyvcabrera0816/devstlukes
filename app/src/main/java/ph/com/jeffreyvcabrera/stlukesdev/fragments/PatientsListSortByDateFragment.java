package ph.com.jeffreyvcabrera.stlukesdev.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ph.com.jeffreyvcabrera.stlukesdev.R;
import ph.com.jeffreyvcabrera.stlukesdev.adapters.PatientsListSortAdapter;
import ph.com.jeffreyvcabrera.stlukesdev.interfaces.AsyncTaskListener;
import ph.com.jeffreyvcabrera.stlukesdev.models.DetailInfo;
import ph.com.jeffreyvcabrera.stlukesdev.utils.API;
import ph.com.jeffreyvcabrera.stlukesdev.utils.SharedPrefManager;

/**
 * Created by Jeffrey on 9/5/2017.
 */

public class PatientsListSortByDateFragment extends android.support.v4.app.Fragment implements AsyncTaskListener, SwipeRefreshLayout.OnRefreshListener {

    ListView myList;
    private SwipeRefreshLayout swipeRefreshLayout;
    Integer id, role;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.patients_list_sort_fragment,null);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipePatients);
        swipeRefreshLayout.setOnRefreshListener(this);
        myList = (ListView) view.findViewById(R.id.listview_patients);
        SharedPrefManager spm = new SharedPrefManager(getActivity());

        id = spm.getUser().getId();
        role = spm.getUser().getRole();

        new API(getActivity(), this).execute("POST", "/api_patients/list/date_admitted/"+role+"/"+id);

        return view;
    }

    @Override
    public void onRefresh() {
        new API(getActivity(), this).execute("POST", "/api_patients/list/date_admitted/"+role+"/"+id);

    }

    @Override
    public void onTaskComplete(String result) {
        JSONObject jObj = null;
        if (result == "error") {
            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }
        } else {
            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }
            final ArrayList<DetailInfo> detailInfo = new ArrayList<>();
            try {
                jObj = new JSONObject(result);
                boolean success = jObj.getBoolean("success");

                if (success) {

                    JSONArray data = jObj.optJSONArray("data");
                    Log.d("response", data.toString());
                    for (int x = 0; x < data.length(); x++) {
                        JSONObject a = data.getJSONObject(x);

                        DetailInfo nm = new DetailInfo();
                        nm.setId(a.getInt("id"));
                        nm.setStatus(a.getInt("status"));
                        nm.setFirstname(a.getString("firstname"));
                        nm.setMiddlename(a.getString("middlename"));
                        nm.setLastname(a.getString("lastname"));
                        nm.setAge(a.getString("age"));
                        nm.setGender(a.getInt("gender"));
                        nm.setRoom(a.getString("room"));
                        nm.setDate_admitted(a.getString("date_admitted"));
                        nm.setDate_released(a.getString("date_released"));
                        nm.setDiagnosis(a.isNull("diagnosis")? "":a.getString("diagnosis"));
                        nm.setChange_dressing(a.getInt("change_dressing"));
                        nm.setPriority(a.getInt("priority"));
                        nm.setPost_op(a.getInt("post_op"));
                        nm.setTrach_care(a.getInt("trach_care"));
                        nm.setImage(a.getString("image"));
                        nm.setPhysician(a.isNull("physicians")? "":a.getString("physicians"));

                        detailInfo.add(nm);
                    }

                    PatientsListSortAdapter listAdapter = new PatientsListSortAdapter(getActivity(), detailInfo);
                    myList.setAdapter(listAdapter);
                    listAdapter.notifyDataSetChanged();

                } else {

                    if (swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    String message = jObj.getString("error");
                    if (message.equals("No Records Found")) {

                        Toast.makeText(getActivity(), "No Activity Found", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void setItemsVisibility(Menu menu, MenuItem exception, boolean visible) {
        for (int i=0; i<menu.size(); ++i) {
            MenuItem item = menu.getItem(i);
            if (item != exception) item.setVisible(visible);
        }
    }
}
