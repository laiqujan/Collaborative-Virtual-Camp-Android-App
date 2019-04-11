package com.example.mahnoor.CollaborativeVirtualCamp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.net.Uri;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
//import com.example.rizwan.katrinakaif1.R;

public class ForgotPassword extends AppCompatActivity {
    EditText Email;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final String randomPassword=getSaltString();
        Email   = (EditText) findViewById(R.id.email);
         button=(Button)findViewById(R.id.button) ;
         button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String em=Email.getText().toString();
               if(em=="" || (!em.contains("@")) || (!em.contains(".com"))){
                   Toast.makeText(getApplicationContext(),"Please enter correct email",Toast.LENGTH_LONG).show();
               }
               else{
                   saveData(Email.getText().toString(),randomPassword);
               }

           }
       });
    }
    private void saveData(final String email, final String password) {
        String url = "http://w2academy.com/androidResetPassword.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String code = response.toString();
                        if(code.equals("1")){
                          // sendEmail(email,password);
                            Toast.makeText(getApplicationContext(),"New password has been emailed to you.",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(ForgotPassword.this,Login.class);
                            startActivity(intent);
                        }else {
                            Toast.makeText(getApplicationContext(),"Failed to reset Password. Please try later",Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("email", email);
                param.put("password", password);
                return param;
            }
        };
        Singleton.getmSingleton(this).addRequest(stringRequest);
    }
    protected void sendEmail(String email,String password) {
       // Log.i("Send email", "");
        String[] TO = {email};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Password reset");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Please login using this password."+password);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
           // Log.i("Finished sending email...", "");
        } catch (android.content.ActivityNotFoundException ex) {
          //  Toast.makeText(MainActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    protected String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 8) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }
}
