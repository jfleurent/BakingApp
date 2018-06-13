package com.example.jeffr.bakingapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.jeffr.bakingapp.R;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    ;private Context context;
    private final List<String> itemList;
    private HashMap<String,List<String>> listHashMap;

    public ExpandableListAdapter(Context context, List<String> itemList, HashMap<String, List<String>> listHashMap) {
        this.context = context;
        this.itemList = itemList;
        this.listHashMap = listHashMap;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {

        return listHashMap.get(itemList.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return listHashMap.get(itemList.get(groupPosition)).size();
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
                             final ViewGroup parent) {
        final String itemString = (String)getChild(groupPosition,childPosition);
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.expandable_list_item,null);
        }
        TextView textView = convertView.findViewById(R.id.expandable_list_item_textview);
        textView.setText(itemString);
        return convertView;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return itemList.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return itemList.size();
    }

    @Override
    public long getGroupId(final int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View theConvertView, ViewGroup parent) {
        String header = itemList.get(groupPosition);

        if(theConvertView == null){
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            theConvertView = inflater.inflate(R.layout.expandable_list_header,null);
        }
        TextView textView = theConvertView.findViewById(R.id.expandable_list_header_textview);
        textView.setText(header);
        return theConvertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

}
