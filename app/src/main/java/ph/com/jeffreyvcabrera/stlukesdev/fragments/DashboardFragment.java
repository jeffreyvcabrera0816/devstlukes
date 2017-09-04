package ph.com.jeffreyvcabrera.stlukesdev.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ph.com.jeffreyvcabrera.stlukesdev.R;
import ph.com.jeffreyvcabrera.stlukesdev.activities.PatientsList;
import ph.com.jeffreyvcabrera.stlukesdev.adapters.ExpandableListAdapter;
import ph.com.jeffreyvcabrera.stlukesdev.interfaces.AsyncTaskListener;
import ph.com.jeffreyvcabrera.stlukesdev.utils.API;

/**
 * Created by Jeffrey on 2/21/2017.
 */

public class DashboardFragment extends Fragment implements AsyncTaskListener, SwipeRefreshLayout.OnRefreshListener {

    private ExpandableListView listView;
    ExpandableListAdapter expandableListAdapter;
    private List<String> listDataHeader;
    private HashMap<String,List<String>> listHash;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.primary_fragment, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeDashboard);
        swipeRefreshLayout.setOnRefreshListener(this);
        listView = (ExpandableListView)v.findViewById(R.id.lvExp);
        initData();
//        listAdapter = new DashboardExpandableListAdapter(getActivity(),listDataHeader,listHash);


        new API(getActivity(), DashboardFragment.this).execute("POST", "/api_patients/dailycensus/");

        return v;
    }

    private void initData() {

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

            try {
                jObj = new JSONObject(result);
                boolean success = jObj.getBoolean("success");

                if (success) {

                    List<String> announcements = new ArrayList<>();
                    List<String> daily_census = new ArrayList<>();
                    List<String> duty_team = new ArrayList<>();
                    List<String> incoming_duty_team = new ArrayList<>();
                    List<String> from_duty_team = new ArrayList<>();

                    listDataHeader = new ArrayList<>();
                    listHash = new HashMap<>();
                    listDataHeader.add("Announcements");
                    listDataHeader.add("On Duty");
                    listDataHeader.add("Pre Duty");
                    listDataHeader.add("From Duty");
                    listDataHeader.add("Daily Census");
                    daily_census.add("#");

                    JSONArray statuses = jObj.optJSONArray("statuses");
                    Log.d("response", statuses.toString());

                    for (int x = 0; x < statuses.length(); x++) {
                        JSONObject a = statuses.getJSONObject(x);


                        if (x == 3) {
                            daily_census.add(a.getString("inpatient"));
                        } else if (x == 4) {
                            daily_census.add(a.getString("discharged"));
                        } else if (x == 6) {
                            daily_census.add(a.getString("mortality"));
                        } else if (x == 7) {
                            daily_census.add(a.getString("signout"));
                        } else {
                            daily_census.add(a.getString("status_count"));
                        }


                    }

                    JSONArray duty = jObj.optJSONArray("on_duty");
                    Log.d("response", duty.toString());
                    for (int x = 0; x < duty.length(); x++) {
                        JSONObject a = duty.getJSONObject(x);

                        if (a.getString("gender").equals("1")) {
                            duty_team.add("Dr. "+a.getString("lastname")+" ("+ a.getString("mobile")+")");
                        } else {
                            duty_team.add("Dra. "+a.getString("lastname")+" ("+ a.getString("mobile")+")");
                        }
                    }

                    JSONArray incomingduty = jObj.optJSONArray("incoming_duty");
                    Log.d("response", incomingduty.toString());
                    for (int x = 0; x < incomingduty.length(); x++) {
                        JSONObject a = incomingduty.getJSONObject(x);

                        if (a.getString("gender").equals("1")) {
                            incoming_duty_team.add("Dr. "+a.getString("lastname")+" ("+ a.getString("mobile")+")");
                        } else {
                            incoming_duty_team.add("Dra. "+a.getString("lastname")+" ("+ a.getString("mobile")+")");
                        }
                    }

                    JSONArray fromduty = jObj.optJSONArray("from_duty");
                    Log.d("response", fromduty.toString());
                    for (int x = 0; x < fromduty.length(); x++) {
                        JSONObject a = fromduty.getJSONObject(x);

                        if (a.getString("gender").equals("1")) {
                            from_duty_team.add("Dr. "+a.getString("lastname")+" ("+ a.getString("mobile")+")");
                        } else {
                            from_duty_team.add("Dra. "+a.getString("lastname")+" ("+ a.getString("mobile")+")");
                        }
                    }

                    JSONArray announcement = jObj.optJSONArray("announcements");
                    Log.d("response", announcement.toString());
                    for (int x = 0; x < announcement.length(); x++) {
                        JSONObject a = announcement.getJSONObject(x);

                        announcements.add(a.getString("announcement"));
                    }

                    listHash.put(listDataHeader.get(0),announcements);
                    listHash.put(listDataHeader.get(1),duty_team);
                    listHash.put(listDataHeader.get(2),incoming_duty_team);
                    listHash.put(listDataHeader.get(3),from_duty_team);
                    listHash.put(listDataHeader.get(4),daily_census);

                    expandableListAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listHash);
                    listView.setAdapter(expandableListAdapter);
                    listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

                        @Override
                        public boolean onChildClick(ExpandableListView parent, View v,int groupPosition, int childPosition, long id) {
//                            if (groupPosition == 3 && childPosition == 0) {
                            if (groupPosition == 4) {
                                Intent intent = new Intent(getActivity(), PatientsList.class);
                                getActivity().startActivity(intent);
                            }


                            return true;
                        }
                    });

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

    @Override
    public void onRefresh() {
        new API(getActivity(), DashboardFragment.this).execute("POST", "/api_patients/dailycensus/");
    }
}
