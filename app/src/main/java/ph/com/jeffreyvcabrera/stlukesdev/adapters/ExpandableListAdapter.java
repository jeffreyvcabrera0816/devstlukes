package ph.com.jeffreyvcabrera.stlukesdev.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import ph.com.jeffreyvcabrera.stlukesdev.R;

/**
 * Created by reale on 21/11/2016.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<String> listDataHeader;
    private HashMap<String,List<String>> listHashMap;

    public ExpandableListAdapter(Context context, List<String> listDataHeader, HashMap<String, List<String>> listHashMap) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listHashMap = listHashMap;
    }

    @Override
    public int getChildTypeCount () {
        return 4;
    }

    @Override
    public int getChildType (int groupPosition, int childPosition) {

        return groupPosition;
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
        if(view == null)
        {
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_group,null);
            ExpandableListView mExpandableListView = (ExpandableListView) viewGroup;
            mExpandableListView.expandGroup(0);
            mExpandableListView.expandGroup(1);
            mExpandableListView.expandGroup(2);
            mExpandableListView.expandGroup(3);
        }
        TextView lblListHeader = (TextView)view.findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        final String childText = (String)getChild(i,i1);
        int childType = getChildType(i, i1);
        if(view == null)
        {
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (childType == 0) {
                view = inflater.inflate(R.layout.list_item,null);
            } else if (childType == 1) {
                view = inflater.inflate(R.layout.list_item,null);
            } else if (childType == 2) {
                view = inflater.inflate(R.layout.list_item,null);
            } else if (childType == 3) {
                view = inflater.inflate(R.layout.daily_census_item_list,null);
            }
        }

        TextView status_name = (TextView)view.findViewById(R.id.title);
        TextView txtListChild = (TextView)view.findViewById(R.id.lblListItem);
        txtListChild.setText(childText);

        if (childType == 3) {
            if (i1 == 0) {
                status_name.setText("View All");
                status_name.setAllCaps(true);
            } else if (i1 == 1) {
                status_name.setText("Admission/s");
            } else if (i1 == 2) {
                status_name.setText("Referral/s");
            } else if (i1 == 3) {
                status_name.setText("Surgical/s");
            } else if (i1 == 4) {
                status_name.setText("Total In Patient/s");
            } else if (i1 == 5) {
                status_name.setText("Discharge/s");
            } else if (i1 == 6) {
                status_name.setText("Emergency/s");
            } else if (i1 == 7) {
                status_name.setText("Mortalities/s");
            } else if (i1 == 8) {
                status_name.setText("Sign Out/s");
            } else if (i1 == 9) {
                status_name.setText("Fall/s");
            } else if (i1 == 10) {
                status_name.setText("Medication Error/s");
            } else if (i1 == 11) {
                status_name.setText("Morbidities/s");
            } else if (i1 == 12) {
                status_name.setText("Sentinel Events/s");
            }
        }

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
