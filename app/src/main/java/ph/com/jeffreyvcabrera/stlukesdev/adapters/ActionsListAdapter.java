package ph.com.jeffreyvcabrera.stlukesdev.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ph.com.jeffreyvcabrera.stlukesdev.R;
import ph.com.jeffreyvcabrera.stlukesdev.models.ActionsBody;

/**
 * Created by Jeffrey on 6/19/2017.
 */

public class ActionsListAdapter extends BaseAdapter {
    Context c;
    ArrayList<ActionsBody> model;

    public ActionsListAdapter(Context c, ArrayList<ActionsBody> model) {
        this.c = c;
        this.model = model;
    }

    @Override
    public int getCount() {
        return model.size();
    }

    @Override
    public Object getItem(int position) {
        return model.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v;
        if(convertView==null){

            LayoutInflater li = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.actions_needed_row,parent,false);

        }else v=convertView;

        TextView content = (TextView) v.findViewById(R.id.content);
        TextView date_created = (TextView) v.findViewById(R.id.date_created);
        TextView who_created = (TextView) v.findViewById(R.id.who_created);

        content.setText(model.get(position).getContent());
        date_created.setText("\nDate Created: "+model.get(position).getDate_posted());
        who_created.setText("Created By: Dr. "+model.get(position).getPhysician());

        return v;
    }
}
