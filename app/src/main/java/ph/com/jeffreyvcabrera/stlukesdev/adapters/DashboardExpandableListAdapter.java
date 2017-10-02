package ph.com.jeffreyvcabrera.stlukesdev.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ph.com.jeffreyvcabrera.stlukesdev.R;
import ph.com.jeffreyvcabrera.stlukesdev.fragments.DashboardFragment;

/**
 * Created by reale on 21/11/2016.
 */

public class DashboardExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<String> listDataHeader;
    private HashMap<String,List<String>> listHashMap;

    public DashboardExpandableListAdapter(Context context, List<String> listDataHeader, HashMap<String, List<String>> listHashMap) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listHashMap = listHashMap;
    }

    @Override
    public int getGroupCount() {
        return listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int i) {

        return listHashMap.get(listDataHeader.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return listDataHeader.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return listHashMap.get(listDataHeader.get(i)).get(i1); // i = Group Item , i1 = ChildItem
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        String headerTitle = (String)getGroup(i);

        View v;
        final ViewHolder holder;

        if(view == null)
        {
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.list_group,null);
            holder = new ViewHolder(v);
            v.setTag(holder);
        } else {
            v = view;
            holder = (ViewHolder) v.getTag();
        }


        holder.lblListHeader.setTypeface(null, Typeface.BOLD);
        holder.lblListHeader.setText(headerTitle);
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        final String childText = (String)getChild(i,i1);

        View v;
        final ViewHolder holder;

        if(view == null)
        {
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.list_item,null);
            holder = new ViewHolder(v);
            v.setTag(holder);
        } else {
            v = view;
            holder = (ViewHolder) v.getTag();
        }

        holder.txtListChild.setText(childText);
        return view;
    }

    class ViewHolder {

        TextView txtListChild;
        TextView lblListHeader;

        ViewHolder(View view) {
            lblListHeader = (TextView)view.findViewById(R.id.lblListHeader);
            txtListChild = (TextView) view.findViewById(R.id.fullname);
        }
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
