package ph.com.jeffreyvcabrera.stlukesdev.adapters;

/**
 * Created by Jeffrey on 6/5/2017.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import ph.com.jeffreyvcabrera.stlukesdev.R;
import ph.com.jeffreyvcabrera.stlukesdev.activities.PatientNotes;
import ph.com.jeffreyvcabrera.stlukesdev.models.ActionsBody;
import ph.com.jeffreyvcabrera.stlukesdev.models.ActionsHeader;
import ph.com.jeffreyvcabrera.stlukesdev.models.DetailInfo;
import ph.com.jeffreyvcabrera.stlukesdev.models.HeaderInfo;
import ph.com.jeffreyvcabrera.stlukesdev.utils.Settings;

public class ActionsAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<ActionsHeader> deptList;
    LinearLayout list_row;

    public ActionsAdapter(Context context, ArrayList<ActionsHeader> deptList) {
        this.context = context;
        this.deptList = deptList;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<ActionsBody> productList = deptList.get(groupPosition).getActionsList();
        return productList.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View view, ViewGroup parent) {

        final ActionsBody detailInfo = (ActionsBody) getChild(groupPosition, childPosition);
        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.actions_needed_row, null);
        }

        TextView content = (TextView) view.findViewById(R.id.content);
        TextView date_created = (TextView) view.findViewById(R.id.date_created);
        TextView who_created = (TextView) view.findViewById(R.id.who_created);

        content.setText(detailInfo.getContent());
        date_created.setText("\nDate Created: "+detailInfo.getDate_posted());
        who_created.setText("Created By: Dr. "+detailInfo.getPhysician());

        return view;
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        ArrayList<ActionsBody> productList = deptList.get(groupPosition).getActionsList();
        return productList.size();

    }

    @Override
    public Object getGroup(int groupPosition) {
        return deptList.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return deptList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isLastChild, View view,
                             ViewGroup parent) {

        ActionsHeader headerInfo = (ActionsHeader) getGroup(groupPosition);
        if (view == null) {
            LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inf.inflate(R.layout.group_heading, null);
        }

        TextView heading = (TextView) view.findViewById(R.id.heading);
        TextView count = (TextView) view.findViewById(R.id.childrenCount);

        heading.setText(headerInfo.getName().trim());
        return view;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }



}