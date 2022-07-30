package com.shuvo.ttit.qrcodetester.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shuvo.ttit.qrcodetester.R;

import java.util.List;
import java.util.Map;

import static com.shuvo.ttit.qrcodetester.TreeMenu.expandableListView;
import static com.shuvo.ttit.qrcodetester.TreeMenu.fromPicture;

public class CustomExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> listTitle;
    private Map<String,List<String>> listItem;

    public CustomExpandableListAdapter(Context context, List<String> listTitle, Map<String, List<String>> listItem) {
        this.context = context;
        this.listTitle = listTitle;
        this.listItem = listItem;
    }

    @Override
    public int getGroupCount() {
        return listTitle.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return listItem.size();
    }

    @Override
    public Object getGroup(int i) {
        return listTitle.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return listItem.get(listTitle.get(i)).get(i1);
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
        String  title = (String) getGroup(i);
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_group,null);
        }
        TextView textView = view.findViewById(R.id.listTitle);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setText(title);
        ImageView imageView = view.findViewById(R.id.arrow_of_group);
        if (expandableListView.isGroupExpanded(i)) {
            imageView.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24);
        }else {
            imageView.setImageResource(R.drawable.ic_baseline_arrow_right_24);
        }
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fromPicture = true;
                if (expandableListView.isGroupExpanded(i)) {
                    expandableListView.collapseGroup(i);
                    imageView.setImageResource(R.drawable.ic_baseline_arrow_right_24);
                }
                else {
                    expandableListView.expandGroup(i);
                    imageView.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24);
                }


            }
        });
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        String  title = (String) getChild(i,i1);
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item,null);
        }
        TextView textView = view.findViewById(R.id.expandabledListItem);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setText(title);
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
