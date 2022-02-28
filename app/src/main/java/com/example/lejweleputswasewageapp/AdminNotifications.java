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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import java.util.List;

public class AdminNotifications extends AppCompatActivity implements NotificationsAdapter.ItemClicked{
    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;
    private static final int SmsNotification =1;
    RecyclerView RvList;
    RecyclerView.Adapter Myadapter;
    RecyclerView.LayoutManager layoutManager;

  //  NotificationAdapter adapter;
  //  ListView lvList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_notifications);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);
        RvList=findViewById(R.id.RvList);
        RvList.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        RvList.setLayoutManager(layoutManager);
        //lvList=findViewById(R.id.lvList);

        showProgress(true);
        tvLoad.setText("Getting Clients please wait...");

        DataQueryBuilder dataQueryBuilder= DataQueryBuilder.create();
        dataQueryBuilder.setGroupBy("name");

        Backendless.Persistence.of(Person.class).find(dataQueryBuilder, new AsyncCallback<List<Person>>() {
            @Override
            public void handleResponse(List<Person> response) {
        //    PersonApplication.personList=response;
            Myadapter=new NotificationsAdapter(AdminNotifications.this,response);
            RvList.setAdapter(Myadapter);
            showProgress(false);



            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(AdminNotifications.this,"Error:"+fault.getMessage(),Toast.LENGTH_LONG).show();
                showProgress(false);
            }
        });


    }

    public void onItemClicked(int index) {
        Intent intent= new Intent(AdminNotifications.this,SmsActivity.class);
        intent.putExtra("index",index);
        startActivityForResult(intent,SmsNotification);

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
