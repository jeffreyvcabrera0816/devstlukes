package ph.com.jeffreyvcabrera.stlukesdev.adapters;

/**
 * Created by Jeffrey on 6/5/2017.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;

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
import ph.com.jeffreyvcabrera.stlukesdev.activities.PatientsList;
import ph.com.jeffreyvcabrera.stlukesdev.models.DetailInfo;
import ph.com.jeffreyvcabrera.stlukesdev.models.HeaderInfo;
import ph.com.jeffreyvcabrera.stlukesdev.utils.Settings;

public class PatientsListAdapter extends BaseExpandableListAdapter implements Filterable {
     Animation button_animation;
    ImageView stat1, stat2, stat3, stat4;
    private Context context;
    private ArrayList<HeaderInfo> deptList;
    LinearLayout list_row;

    public PatientsListAdapter(Context context, ArrayList<HeaderInfo> deptList) {
        this.context = context;
        this.deptList = deptList;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<DetailInfo> productList = deptList.get(groupPosition).getProductList();
        return productList.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View view, ViewGroup parent) {

        final DetailInfo detailInfo = (DetailInfo) getChild(groupPosition, childPosition);
        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.child_row, null);
        }

        button_animation = AnimationUtils.loadAnimation(context, R.anim.button_animate);

        list_row = (LinearLayout) view.findViewById(R.id.patients_list_row);
        TextView name = (TextView) view.findViewById(R.id.name);
        TextView age = (TextView) view.findViewById(R.id.age);
        TextView room = (TextView) view.findViewById(R.id.room);
        TextView latest_diagnosis = (TextView) view.findViewById(R.id.latest_diagnosis);
        TextView physician = (TextView) view.findViewById(R.id.physician);

        name.setText(detailInfo.getLastname()+", "+detailInfo.getFirstname());
            String gender = "";
        if (detailInfo.getGender() == 1) {
            gender = "M";
        } else {
            gender = "F";
        }
        age.setText(detailInfo.getAge()+ gender);
        room.setText(detailInfo.getRoom());

        if (detailInfo.getPhysician().equals("")) {
            physician.setText("Physician/s: Not Assigned");
        } else {
            physician.setText("Physician/s: "+detailInfo.getPhysician());
        }

        if (detailInfo.getDiagnosis().equals("")) {
            latest_diagnosis.setText("Latest Diagnosis: No Record");
        } else {
            latest_diagnosis.setText("Latest Diagnosis: "+detailInfo.getDiagnosis());
        }

        stat1 = (ImageView) view.findViewById(R.id.stat1);
        stat2 = (ImageView) view.findViewById(R.id.stat2);
        stat3 = (ImageView) view.findViewById(R.id.stat3);
        stat4 = (ImageView) view.findViewById(R.id.stat4);

        if (detailInfo.getChange_dressing().equals(1)) {
            stat1.setImageResource(R.mipmap.triangle_yellow);
        } else {
            stat1.setImageResource(R.mipmap.triangle_black);
        }

        if (detailInfo.getPriority().equals(1)) {
            stat2.setImageResource(R.mipmap.star_yellow);
        } else {
            stat2.setImageResource(R.mipmap.star_black);
        }

        if (detailInfo.getPost_op().equals(1)) {
            stat3.setImageResource(R.mipmap.letter_p_yellow);
        } else {
            stat3.setImageResource(R.mipmap.letter_p_black);
        }

        if (detailInfo.getTrach_care().equals(1)) {
            stat4.setImageResource(R.mipmap.letter_i_yellow);
        } else {
            stat4.setImageResource(R.mipmap.letter_i_black);
        }

        stat1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageView imageView1 = (ImageView) view.findViewById(R.id.stat1);
                imageView1.startAnimation(button_animation);
                if (detailInfo.getChange_dressing() == 1) {
                    new changeStatusAPI(context, "/api_patients/changestatus/"+1+"/"+detailInfo.getId()+"/"+0).execute();
                } else {
                    new changeStatusAPI(context, "/api_patients/changestatus/"+1+"/"+detailInfo.getId()+"/"+1).execute();
                }
            }
        });

        stat2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageView imageView2 = (ImageView) view.findViewById(R.id.stat2);
                imageView2.startAnimation(button_animation);
                if (detailInfo.getPriority() == 1) {
                    new changeStatusAPI(context, "/api_patients/changestatus/"+2+"/"+detailInfo.getId()+"/"+0).execute();
                } else {
                    new changeStatusAPI(context, "/api_patients/changestatus/"+2+"/"+detailInfo.getId()+"/"+1).execute();
                }
            }
        });

        stat3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageView imageView3 = (ImageView) view.findViewById(R.id.stat3);
                imageView3.startAnimation(button_animation);
                if (detailInfo.getPost_op() == 1) {
                    new changeStatusAPI(context, "/api_patients/changestatus/"+3+"/"+detailInfo.getId()+"/"+0).execute();
                } else {
                    new changeStatusAPI(context, "/api_patients/changestatus/"+3+"/"+detailInfo.getId()+"/"+1).execute();
                }
            }
        });

        stat4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageView imageView4 = (ImageView) view.findViewById(R.id.stat4);
                imageView4.startAnimation(button_animation);
                if (detailInfo.getTrach_care() == 1) {
                    new changeStatusAPI(context, "/api_patients/changestatus/"+4+"/"+detailInfo.getId()+"/"+0).execute();
                } else {
                    new changeStatusAPI(context, "/api_patients/changestatus/"+4+"/"+detailInfo.getId()+"/"+1).execute();
                }
            }
        });

        list_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PatientNotes.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle bundle = new Bundle();
                bundle.putString("firstname", detailInfo.getFirstname());
                bundle.putString("middlename", detailInfo.getMiddlename());
                bundle.putString("lastname", detailInfo.getLastname());
                bundle.putString("age", detailInfo.getAge());
                bundle.putInt("gender", detailInfo.getGender());
                bundle.putString("room", detailInfo.getRoom());
                bundle.putString("image", detailInfo.getImage());
                bundle.putString("physicians", detailInfo.getPhysician());
                bundle.putString("date_admitted", detailInfo.getDate_admitted());
                bundle.putString("date_released", detailInfo.getDate_released());
                bundle.putInt("id", detailInfo.getId());
                bundle.putInt("status", detailInfo.getStatus());
                bundle.putInt("change_dressing", detailInfo.getChange_dressing());
                bundle.putInt("priority", detailInfo.getPriority());
                bundle.putInt("post_op", detailInfo.getPost_op());
                bundle.putInt("trach_care", detailInfo.getTrach_care());

                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });


        return view;
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    class changeStatusAPI extends AsyncTask<String, Void, String> {
        Context act;
        ProgressDialog pd;
        String api_url;

        public changeStatusAPI(Context act, String api_url) {
            this.act = act;
            this.api_url = api_url;
        }

        @Override
        protected String doInBackground(String... params) {

            HttpClient httpclient;
            HttpPost httppost;
            HttpResponse response;
            HttpEntity entity;
            InputStream isr = null;
            String result = "";
            String test_url = api_url + "/";
            String url = Settings.local_url + test_url;

            try {
                httpclient = new DefaultHttpClient();
                httppost = new HttpPost(url);
                response = httpclient.execute(httppost);
                entity = response.getEntity();
                isr = entity.getContent();
            } catch (Exception e) {
                Log.e("log_tag", "Error in http connection " + e.toString());
            }

            //convert response to string
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(isr, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                isr.close();

                result = sb.toString();
            } catch (Exception e) {
                Log.e("log_tag", "Error converting result" + e.toString());
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);
            if (pd != null) {
                pd.dismiss();
            }

            try {
                JSONObject jObj = new JSONObject(result);
                boolean status = jObj.getBoolean("success");
                String service = jObj.getString("service");

                if (status) {
                    Toast.makeText(act, "Status Changed", Toast.LENGTH_SHORT).show();
                } else {
                    String message = jObj.getString("error");
                    Toast.makeText(act, "Error", Toast.LENGTH_LONG).show();


                }
            } catch (JSONException e) {
                Log.e("error", "Error parsing data" + e.toString());
            }
        }
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        ArrayList<DetailInfo> productList = deptList.get(groupPosition).getProductList();
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

        HeaderInfo headerInfo = (HeaderInfo) getGroup(groupPosition);
        if (view == null) {
            LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inf.inflate(R.layout.group_heading, null);
        }

        TextView heading = (TextView) view.findViewById(R.id.heading);
        TextView count = (TextView) view.findViewById(R.id.childrenCount);

        heading.setText(headerInfo.getName().trim());
        count.setText(String.valueOf(getChildrenCount(groupPosition)));
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