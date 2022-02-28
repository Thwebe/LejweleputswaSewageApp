package com.example.lejweleputswasewageapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import java.util.List;

public class ViewLocations extends AppCompatActivity implements LocationAdapter.ItemClicked {

    private static final int REQUESTCODE_LOCATION_info = 1;
    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;
    private String ivurl;

    RecyclerView rvList;
    RecyclerView.Adapter myadapter;
    RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_locations);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);

        rvList=findViewById(R.id.rvList);
        rvList.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        rvList.setLayoutManager(layoutManager);


        DataQueryBuilder dataQueryBuilder= DataQueryBuilder.create();
        //dataQueryBuilder.setGroupBy("userEmail");

        showProgress(true);
        tvLoad.setText("Retrieving client locations please wait...");

        Backendless.Persistence.of(Location.class).find(dataQueryBuilder, new AsyncCallback<List<Location>>() {
            @Override
            public void handleResponse(List<Location> response) {
                PersonApplication.userLocations=response;
                myadapter=new LocationAdapter(ViewLocations.this,response,ivurl);
                rvList.setAdapter(myadapter);



                showProgress(false);

            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(ViewLocations.this,"Error "+fault.getMessage(),Toast.LENGTH_LONG).show();
                showProgress(false);
            }
        });



    }

    @Override
    public void onItemClicked(int index) {
        Intent intent= new Intent(ViewLocations.this,MapsActivity.class);
        intent.putExtra("index",index);
        startActivityForResult(intent,REQUESTCODE_LOCATION_info);

    }
    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });

            tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoad.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }

    }
}