package com.example.lejweleputswasewageapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.files.BackendlessFile;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainScreen extends AppCompatActivity {
    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;


    private static final int CameraRequestCode = 1;
    private static final int LocationRequestCode = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    String currentPhotoPath;
    FusedLocationProviderClient fusedLocationClient;


    EditText EtComment;
    TextView tvWelcome;
    Button btnLocation, btnPicture;
    ImageView ivPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivityscreen);





        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);
        tvWelcome = findViewById(R.id.tvWelcome);
        btnLocation = findViewById(R.id.btnLocation);
        ivPic = findViewById(R.id.ivPic);
        btnPicture = findViewById(R.id.btnPicture);
        EtComment = findViewById(R.id.ETcoment);

        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_Navigation);

        bottomNavigationView.setSelectedItemId(R.id.home);

        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.about:
                        startActivity(new Intent(MainScreen.this,About.class));
                        overridePendingTransition(0,0);
                        break;
                    case R.id.notifications:
                        startActivity(new Intent(MainScreen.this,notification.class));
                        overridePendingTransition(0,0);
                        break;
                    case R.id.progressreport:
                        startActivity(new Intent(MainScreen.this,Progress_Report.class));
                        overridePendingTransition(0,0);
                        break;
                    case R.id.home:
                        break;

                }
            }
        });



        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        tvWelcome.setText("Welcome, " + PersonApplication.user.getProperty("name"));

        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = PersonApplication.user.getProperty("name").toString();


                Person person = new Person();
                person.setNumber("0727923669");
                person.setPassword(person.getPassword());
                person.setEmail(PersonApplication.user.getEmail());
                person.setName(name);
                person.setUserEmail(PersonApplication.user.getEmail());
                person.setComment(EtComment.getText().toString().trim());
                person.setStatus("Pending");


                if (EtComment.getText().toString().isEmpty()) {
                    Toast.makeText(MainScreen.this, "Please enter a comment", Toast.LENGTH_LONG).show();
                } else if (ContextCompat.checkSelfPermission(MainScreen.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainScreen.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, LocationRequestCode);
                    //getLocation();
                    tvLoad.setText("saving data please wait..");
                    showProgress(true);
                    Backendless.Persistence.save(person, new AsyncCallback<Person>() {
                        @Override
                        public void handleResponse(Person response) {
                            EtComment.setText(null);
                            person.setNumber(null);
                            person.setEmail(null);
                            person.setName(null);
                            person.setPassword(null);

                            tvLoad.setText("Data Saved..........");
                            showProgress(false);

                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Toast.makeText(MainScreen.this, "Error" + fault.getMessage(), Toast.LENGTH_LONG).show();
                            showProgress(false);
                        }
                    });


                } else {



                }


            }
        });

        btnPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainScreen.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainScreen.this, new String[]{Manifest.permission.CAMERA}, CameraRequestCode);
                } else {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

                    Toast.makeText(MainScreen.this, "Thank you permission granted", Toast.LENGTH_LONG).show();

                }
            }


        });


    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            return;
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ivPic.setImageBitmap(imageBitmap);



            String Email = PersonApplication.user.getEmail();

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = Email + timeStamp + ".jPEG";
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            try {
                File ivPic = File.createTempFile(
                        imageFileName,  /* prefix */
                        ".jpg",         /* suffix */
                        storageDir      /* directory */
                );
                currentPhotoPath = ivPic.getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Backendless.Files.Android.upload(imageBitmap, Bitmap.CompressFormat.JPEG, 100, imageFileName, "Images", new AsyncCallback<BackendlessFile>() {
                @Override
                public void handleResponse(BackendlessFile response) {

                    Toast.makeText(MainScreen.this, "Succesfully uploaded", Toast.LENGTH_LONG).show();




                }

                @Override
                public void handleFault(BackendlessFault fault) {
                    Toast.makeText(MainScreen.this, "Error" + fault.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });


        }


    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CameraRequestCode) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainScreen.this, "permission granted", Toast.LENGTH_LONG).show();

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.

                    return;
                }
                fusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {

                        Location location = task.getResult();
                        if (location != null) {
                            Geocoder geocoder = new Geocoder(MainScreen.this, Locale.getDefault());


                            try {
                                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                                com.example.lejweleputswasewageapp.Location location1 = new com.example.lejweleputswasewageapp.Location();
                                //Initiallize Longitude,latitude,CountryName,Locality
                                location1.setLongitude(addresses.get(0).getLongitude());
                                location1.setLatitude(addresses.get(0).getLatitude());
                                location1.setLocality(addresses.get(0).getLocality());
                                location1.setCountryName(addresses.get(0).getCountryName());
                                location1.setAddress(addresses.get(0).getAddressLine(0));
                                location1.setUserEmail(PersonApplication.user.getEmail());
                                location1.setComment(EtComment.getText().toString().trim());

                                tvLoad.setText("Sending Location Please wait..");
                                showProgress(true);

                                Backendless.Persistence.save(location1, new AsyncCallback<com.example.lejweleputswasewageapp.Location>() {
                                    @Override
                                    public void handleResponse(com.example.lejweleputswasewageapp.Location response) {
                                        Toast.makeText(MainScreen.this, "Location Successfully sent", Toast.LENGTH_LONG).show();

                                        showProgress(false);
                                    }

                                    @Override
                                    public void handleFault(BackendlessFault fault) {

                                        Toast.makeText(MainScreen.this, "Error: " + fault.getMessage(), Toast.LENGTH_LONG).show();
                                        showProgress(false);
                                    }
                                });

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    //If the user clicked on deny
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainScreen.this, Manifest.permission.CAMERA))
                {
                    AlertDialog.Builder alertDialog= new AlertDialog.Builder(this);
                    alertDialog.setTitle("Important permission required!");
                    alertDialog.setMessage("This permission is important for the admin to see the significance of the picture. Please allow the permision and take a picture");

                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainScreen.this, new String [] {Manifest.permission.CAMERA},CameraRequestCode);

                        }
                    });

                    alertDialog.setNegativeButton("No Thanks", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(MainScreen.this,"Cannot use this feature unless you allow it",Toast.LENGTH_LONG).show();
                        }
                    });

                    alertDialog.show();
                }
                else
                {
                    Toast.makeText(MainScreen.this,"We will never show the feature to you again",Toast.LENGTH_LONG).show();

                }

            }

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId())
    {
        case R.id.logout:
            tvLoad.setText("Busy signing you out..please wait...");
            showProgress(true);

            Backendless.UserService.logout(new AsyncCallback<Void>() {
                @Override
                public void handleResponse(Void response) {

                    tvLoad.setText("User signed out successfully!");

                    startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                    MainScreen.this.finish();

                }

                @Override
                public void handleFault(BackendlessFault fault) {
                    showProgress(false);
                    Toast.makeText(MainScreen.this,"Ërror: "+ fault.getMessage(),Toast.LENGTH_LONG).show();
                }
            });

    }
        return super.onOptionsItemSelected(item);
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