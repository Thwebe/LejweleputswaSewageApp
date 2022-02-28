package com.example.lejweleputswasewageapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder> {
private List<Person>  notifications;

ItemClicked activity;
Context context;


public interface ItemClicked
{
    void onItemClicked(int index);
}

public NotificationsAdapter(Context context, List<Person> list)
{
    activity=(ItemClicked) context;
    notifications=list;

}

 public class ViewHolder extends RecyclerView.ViewHolder
{
    TextView tvName,tvEmail,tvStat,tvNumber;


    public ViewHolder(@NonNull View itemView) {
        super(itemView);
         tvName=itemView.findViewById(R.id.tvName);
         tvEmail=itemView.findViewById(R.id.tvEmail);
         tvNumber=itemView.findViewById(R.id.tvNumber);
         tvStat=itemView.findViewById(R.id.tvStat);


        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                activity.onItemClicked(notifications.indexOf((Person) view.getTag()));
            }
        });



    }
}


    @NonNull
    @Override
    public NotificationsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

    View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_layout,parent,false);
    return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationsAdapter.ViewHolder holder, int position) {

    holder.itemView.setTag(notifications.get(position));

        DataQueryBuilder dataQueryBuilder=DataQueryBuilder.create();
        Backendless.Persistence.of(Person.class).find(dataQueryBuilder, new AsyncCallback<List<Person>>() {
            @Override
            public void handleResponse(List<Person> response) {

                //holder.tvAddress.setText(address);
            }

            @Override
            public void handleFault(BackendlessFault fault) {

            }
        });

      holder.tvEmail.setText(notifications.get(position).getEmail());
      holder.tvName.setText(notifications.get(position).getName());
      holder.tvStat.setText(notifications.get(position).getStatus());
      holder.tvNumber.setText(notifications.get(position).getNumber());




    }

    @Override
    public int getItemCount() {

    return notifications.size();
    }


}

