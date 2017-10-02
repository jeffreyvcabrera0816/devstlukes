package ph.com.jeffreyvcabrera.stlukesdev.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
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
import ph.com.jeffreyvcabrera.stlukesdev.models.PhysiciansModel;
import ph.com.jeffreyvcabrera.stlukesdev.utils.Settings;


/**
 * Created by Jeffrey on 2/19/2017.
 */

public class PhysiciansListAdapter extends BaseAdapter{
    Context c;
    ArrayList<PhysiciansModel> pm;
    private LayoutInflater inflater;
    Integer cur_stat, patient_id;

    public PhysiciansListAdapter(Context c, ArrayList<PhysiciansModel> pm, Integer patient_id) {
        this.c = c;
        this.pm = pm;
        this.patient_id = patient_id;

    }

    @Override
    public int getCount() {
        return pm.size();
    }

    @Override
    public Object getItem(int i) {
        return pm.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        View v;
        final ViewHolder holder;

        if (view == null) {
            inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.add_physicians_row, viewGroup, false);
            holder = new ViewHolder(v);
            v.setTag(holder);
        } else {
            v = view;
            holder = (ViewHolder) v.getTag();
        }

        holder.name.setText(pm.get(i).getFirstname() + " " + pm.get(i).getLastname());

        holder.designation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater li = LayoutInflater.from(c);
                View promptsView = li.inflate(R.layout.change_status_view, null);
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(c);
                alertDialogBuilder.setView(promptsView);

                final AlertDialog alertDialog = alertDialogBuilder.create();

                final Spinner status_array = (Spinner) promptsView.findViewById(R.id.status_array);
                ArrayAdapter<String> adapter;
                final String[] list = {"None", "@", "®"};

                adapter = new ArrayAdapter<String>(c, android.R.layout.simple_spinner_item, list);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                status_array.setAdapter(adapter);
//                status_array.setSelection(cur_stat);

                status_array.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                        Log.d("status" , String.valueOf(arg2));

                        if (arg2 == 1) {
                            holder.designation.setImageResource(R.mipmap.arroba);
                            holder.reference = "1";
                            alertDialog.dismiss();
                        } else if (arg2 == 2) {
                            holder.designation.setImageResource(R.mipmap.registered);
                            holder.reference = "2";
                            alertDialog.dismiss();
                        } else {
                            holder.designation.setImageResource(R.mipmap.doctor);
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub
                    }
                });

                alertDialog.show();
            }
        });

        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new APInoDialog(c, "/api_patients/addpatientphysician/"+patient_id+"/"+pm.get(i).getId()+"/"+holder.reference).execute();
                holder.btnAdd.setImageResource(R.mipmap.checked);
                holder.btnAdd.setEnabled(false);
                holder.designation.setEnabled(false);
            }
        });


        return v;
    }

    class ViewHolder {

        TextView name;
        ImageView btnAdd, designation;
        String reference;

        ViewHolder(View view) {

            reference = "0";
            name = (TextView) view.findViewById(R.id.fullname);
            designation = (ImageView) view.findViewById(R.id.designation);
            btnAdd = (ImageView) view.findViewById(R.id.btn_add);
        }
    }

    class APInoDialog extends AsyncTask<String, Void, String> {
        Context act;
        ProgressDialog pd;
        String api_url;

        public APInoDialog(Context act, String api_url) {
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

                } else {
                    String message = jObj.getString("error");
                    Toast.makeText(act, "Error", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                Log.e("error", "Error parsing data" + e.toString());
            }
        }
    }

}
