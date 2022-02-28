package com.example.lejweleputswasewageapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ListAdapter;

import java.util.List;






public class NotificationAdapter extends ArrayAdapter<Person> {

    public interface ItemClicked
    {
        void onItemClicked(int index);
    }

    private Context context;
    private List<Person> notifications;
    ItemClicked activity;



    public NotificationAdapter(Context context, List<Person> list)
    {
        super(context,R.layout.notification_layout,list);
        this.context=context;
        notifications=list;
        activity=(ItemClicked) context;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater=(LayoutInflater)  context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView=inflater.inflate(R.layout.notification_layout,parent,false);

        TextView tvName=convertView.findViewById(R.id.tvName);
        TextView tvEmail=convertView.findViewById(R.id.tvEmail);
        TextView tvNumber=convertView.findViewById(R.id.tvNumber);
        TextView tvStat=convertView.findViewById(R.id.tvStat);


        tvEmail.setText(notifications.get(position).getEmail());
        tvName.setText(notifications.get(position).getName());
        tvStat.setText(notifications.get(position).getStatus());
        tvNumber.setText(notifications.get(position).getNumber());



        return convertView;


    }


}
