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
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {
private List<Location> locations;
private String ivUrl;
ItemClicked activity;
Context context;

//Bitmap bitmap;
public interface ItemClicked
{
    void onItemClicked(int index);
}

public LocationAdapter(Context context, List<Location> list,String ivList)
{
    activity=(ItemClicked) context;
    locations=list;
    this.ivUrl = ivUrl;
}

 public class ViewHolder extends RecyclerView.ViewHolder
{
    TextView tvUserEmail,tvCountry,tvLocality,tvAddress,tvComment,Longitude,Latitude;
    ImageView ivImage;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        tvUserEmail=itemView.findViewById(R.id.tvUserEmail);
        tvCountry=itemView.findViewById(R.id.tvCountry);
        tvLocality=itemView.findViewById(R.id.tvLocality);
        tvAddress=itemView.findViewById(R.id.tvAddress);
        ivImage=itemView.findViewById(R.id.ivImage);
        tvComment=itemView.findViewById(R.id.tvComment);
        Longitude=itemView.findViewById(R.id.Longitude);
        Latitude=itemView.findViewById(R.id.Latitude);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                activity.onItemClicked(locations.indexOf((Location) view.getTag()));
            }
        });



    }
}


    @NonNull
    @Override
    public LocationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

    View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_locationitems,parent,false);
    return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationAdapter.ViewHolder holder, int position) {

    holder.itemView.setTag(locations.get(position));

        DataQueryBuilder dataQueryBuilder=DataQueryBuilder.create();
        Backendless.Persistence.of(Location.class).find(dataQueryBuilder, new AsyncCallback<List<Location>>() {
            @Override
            public void handleResponse(List<Location> response) {

                //holder.tvAddress.setText(address);
            }

            @Override
            public void handleFault(BackendlessFault fault) {

            }
        });

    String comment=locations.get(position).getComment();
    String address=locations.get(position).getAddress();
    holder.tvUserEmail.setText(locations.get(position).getUserEmail());
    holder.tvAddress.setText(address);
    holder.tvLocality.setText("Locality:  "+locations.get(position).getLocality());
    holder.tvCountry.setText("Country:       South Africa");
    holder.ivImage.setImageBitmap(getBitmapFromURL(ivUrl));
    holder.tvComment.setText("Complaint:         " + comment);
    holder.Longitude.setText(""+locations.get(position).getLongitude());
    holder.Latitude.setText(""+locations.get(position).getLatitude());

       //  holder.ivImage.setImageBitmap(bitmap);
      //  String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
      //   String Email = PersonApplication.user.getEmail();
      //    String imageFileName = Email + timeStamp + ".jPEG";
      //   Picasso.with(context).load("https://greatfield.backendless.app/api/files/Images/Okay%40gmail.com20211127_111020.jPEG").into(holder.ivImage);
     //   holder.ivImage.setImageBitmap();


    }

    @Override
    public int getItemCount() {

    return locations.size();
    }
    public static Bitmap getBitmapFromURL(String imgUrl) {
        try {
            URL url = new URL(imgUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);

            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }
}
