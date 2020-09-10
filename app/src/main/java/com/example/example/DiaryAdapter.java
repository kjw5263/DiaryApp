package com.example.example;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class DiaryAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Diary> items;
    int item_layout;

    public DiaryAdapter(Context context,int item_layout, ArrayList<Diary> items) {
        this.context = context;
        this.item_layout = item_layout;
        this.items = items;
    }

    //리스트뷰가 몇개의 아이템을 가지고있는지
    @Override
    public int getCount() {
        return items.size();
    }

    //현재 어떤 아이템인지
    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    //현재 어떤 포지션인지
    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(item_layout, null);
        }

        TextView textview_title = view.findViewById(R.id.textView1);
        TextView textview_contents = view.findViewById(R.id.textView2);

        textview_title.setText(items.get(i).getTitle());
        textview_contents.setText(items.get(i).getContents());

        return view;
    }
}
