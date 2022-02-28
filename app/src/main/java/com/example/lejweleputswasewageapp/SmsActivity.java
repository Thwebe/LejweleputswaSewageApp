package com.example.lejweleputswasewageapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class SmsActivity extends AppCompatActivity {

    EditText etMessage,etTel;
    Button btnSendSMS;
    int index;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        etMessage=findViewById(R.id.etMessage);
        etTel=findViewById(R.id.etTel);
        btnSendSMS=findViewById(R.id.btnSendSMS);
        index = getIntent().getIntExtra("index", -1);



        btnSendSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    // Construct data
                    String apiKey = "apikey=" + "MzQ2ZDY3NWEzNDQxNDM1MzVhNmQ1NDc1Nzg1MTMxMzQ=";
                    String message = "message=" + etMessage.getText().toString();
                    String sender = "sender=" + "TXTCL";
                    String numbers = "numbers=" + etTel.getText().toString();


                    //Send data
                    HttpURLConnection conn = (HttpURLConnection) new URL("https://api.txtlocal.com/send/?").openConnection();
                    String data = apiKey + numbers + sender + message;
                    conn.setDoOutput(true);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
                    conn.getOutputStream().write(data.getBytes(StandardCharsets.UTF_8));
                    final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line;
                    while ((line = rd.readLine()) != null) {
                        Toast.makeText(getApplicationContext(), "the message is" + line, Toast.LENGTH_SHORT).show();

                    }
                    rd.close();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Message Not Sent" + e, Toast.LENGTH_SHORT).show();

                }
            }

        });
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
}