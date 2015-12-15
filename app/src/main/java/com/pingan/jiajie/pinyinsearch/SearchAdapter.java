package com.pingan.jiajie.pinyinsearch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by jiajie on 15/12/8.
 */
public class SearchAdapter extends BaseAdapter {
    List<Contact> mlist;
    Context mContext;

    public SearchAdapter(List<Contact> mlist, Context mContext) {
        this.mlist = mlist;
        this.mContext = mContext;
    }

    public void setData(List<Contact> mlist) {
        this.mlist = mlist;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Contact bean = mlist.get(position);
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.search_list_item, parent, false);
        }
        TextView name = ViewHolder.get(view, R.id.name_tv);
        TextView number = ViewHolder.get(view, R.id.iphone_tv);
        name.setText(bean.getName());
        number.setText(bean.getNumber());
        return view;
    }


}
