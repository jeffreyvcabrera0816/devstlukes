package ph.com.jeffreyvcabrera.stlukesdev.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.androidquery.AQuery;

import java.util.ArrayList;
import java.util.zip.Inflater;

import ph.com.jeffreyvcabrera.stlukesdev.R;
import ph.com.jeffreyvcabrera.stlukesdev.models.ActionsNeededModel;

/**
 * Created by Jeffrey on 6/13/2017.
 */

public class ActionsNeededAdapter extends BaseAdapter {

    LayoutInflater inflater;
    Context c;
    ArrayList<ActionsNeededModel> m;

    public ActionsNeededAdapter(Context c, ArrayList<ActionsNeededModel> m) {
        this.c = c;
        this.m = m;
    }

    @Override
    public int getCount() {
        return m.size();
    }

    @Override
    public Object getItem(int i) {
        return m.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        if (view == null) {
            inflater= (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.actions_needed_row, viewGroup, false);
        } else {
            v = view;
        }

        TextView content = (TextView) v.findViewById(R.id.content);
        TextView date_created = (TextView) v.findViewById(R.id.date_created);
        TextView who_created = (TextView) v.findViewById(R.id.who_created);

        content.setText(m.get(i).getContent());
        date_created.setText("Date: "+m.get(i).getDate_posted());
        who_created.setText("Created by: Dr "+ m.get(i).getPhysician());


        return v;
    }
}
