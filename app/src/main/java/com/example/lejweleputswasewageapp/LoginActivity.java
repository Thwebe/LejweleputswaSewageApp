package com.example.lejweleputswasewageapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.local.UserIdStorageFactory;

public class LoginActivity extends AppCompatActivity {
    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;
    EditText etUsername,etPassword,etRestEmail;
    Button btnLogin,btnRegister;
    TextView tvReset;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.login);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);
        btnRegister = findViewById(R.id.btnRegister);
        btnLogin=findViewById(R.id.btnLogin);
        etUsername=findViewById(R.id.etUsername);
        etPassword=findViewById(R.id.etPassword);
        tvReset=findViewById(R.id.tvReset);

        tvLoad.setText("Busy authenticating user please wait..");
        showProgress(true);
        Backendless.UserService.isValidLogin(new AsyncCallback<Boolean>() {
            @Override
            public void handleResponse(Boolean response) {
                if(response)
                {
                    tvLoad.setText("Successfully authenticated. Signing you in...");



                    String userObjectId= UserIdStorageFactory.instance().getStorage().get();
                    Backendless.Data.of(BackendlessUser.class).findById(userObjectId, new AsyncCallback<BackendlessUser>() {
                        @Override
                        public void handleResponse(BackendlessUser response) {
                            PersonApplication.user=response;
                            startActivity(new Intent(LoginActivity.this,MainScreen.class) );
                            LoginActivity.this.finish();
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            showProgress(false);
                            Toast.makeText(LoginActivity.this,"Error: "+fault.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });

                }
                else
                    {
                        showProgress(false);
                    }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
              showProgress(false);
              Toast.makeText(LoginActivity.this,"Error:" +fault.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,com.example.lejweleputswasewageapp.Register.class);
                startActivity(intent);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(etUsername.getText().toString().trim().isEmpty()||etPassword.getText().toString().trim().isEmpty())
               {
                   Toast.makeText(LoginActivity.this,"Please enter all fields",Toast.LENGTH_LONG).show();
               }
               else if(etUsername.getText().toString().trim().equals("admin@gmail.com")&&etPassword.getText().toString().equals("123"))
               {

                   tvLoad.setText("Signing you in please wait ");
                   showProgress(true);

                   Backendless.UserService.login(etUsername.getText().toString().trim(), etPassword.getText().toString().trim(), new AsyncCallback<BackendlessUser>() {
                       @Override
                       public void handleResponse(BackendlessUser response) {
                           Toast.makeText(LoginActivity.this,"Successfully signed in!",Toast.LENGTH_LONG).show();
                           startActivity(new Intent(LoginActivity.this,Admin.class));
                           LoginActivity.this.finish();
                       }

                       @Override
                       public void handleFault(BackendlessFault fault) {
                           Toast.makeText(LoginActivity.this,"Error: " + fault.getMessage(),Toast.LENGTH_LONG).show();
                           showProgress(false);
                       }
                   });
               }
               else
                   {
                       String email=etUsername.getText().toString().trim();
                       String password=etPassword.getText().toString().trim();

                       tvLoad.setText("Signing you in please wait... ");
                       showProgress(true);

                       Backendless.UserService.login(email, password, new AsyncCallback<BackendlessUser>() {
                           @Override
                           public void handleResponse(BackendlessUser response) {
                               PersonApplication.user=response;
                            Toast.makeText(LoginActivity.this,"Successfully signed in!",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(LoginActivity.this,MainScreen.class));
                            LoginActivity.this.finish();

                           }

                           @Override
                           public void handleFault(BackendlessFault fault) {
                               Toast.makeText(LoginActivity.this,"Error: " + fault.getMessage(),Toast.LENGTH_LONG).show();
                               showProgress(false);
                           }
                       }, true);

                   }
            }
        });

        tvReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog =new AlertDialog.Builder(LoginActivity.this);
                dialog.setTitle("Password Reset");
                dialog.setMessage("Please enter your email address so that a link can be sent to you to reset your "+
                        " password.  A link will be sent to your email to reset your password.");
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_view,null);

                dialog.setView(dialogView);
                etRestEmail=dialogView.findViewById(R.id.etResetEmail);

                dialog.setPositiveButton("RESET", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(etRestEmail.getText().toString().trim().isEmpty())
                        {
                            Toast.makeText(LoginActivity.this,"Please enter an email to rester passowrd",Toast.LENGTH_LONG).show();

                        }
                        else
                            {
                                tvLoad.setText(R.string.link);
                                showProgress(true);
                                String email=etRestEmail.getText().toString().trim();
                                Backendless.UserService.restorePassword(email, new AsyncCallback<Void>() {
                                    @Override
                                    public void handleResponse(Void response) {
                                        showProgress(false);
                                        Toast.makeText(LoginActivity.this, "Reset link sent to email address", Toast.LENGTH_SHORT).show();

                                    }

                                    @Override
                                    public void handleFault(BackendlessFault fault) {
                                    showProgress(false);
                                    Toast.makeText(LoginActivity.this,"Error: "+fault.getMessage(),Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                    }
                });

                dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                dialog.show();
            }
        });

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