package com.example.mahnoor.CollaborativeVirtualCamp;

import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class Login extends AppCompatActivity {

   Toolbar toolbar;
    Button button;
    TextView textView;
    TextView forgotPassword;
    EditText Email,Password;
    String server_url = "http://w2academy.com/androidLogin.php";
    String username,password;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        toolbar = (Toolbar) findViewById(R.id.toolbar_login);
        toolbar.setTitle("Login");
        toolbar.setTitleTextColor(Color.WHITE);

        button   = (Button) findViewById(R.id.login);
        Email    = (EditText) findViewById(R.id.email);
        Password = (EditText) findViewById(R.id.password);
        builder  = new AlertDialog.Builder(Login.this);
        forgotPassword = (TextView) findViewById(R.id.textView5);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,ForgotPassword.class);
                startActivity(intent);
            }
        });
        textView = (TextView) findViewById(R.id.textView4);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,Registration.class);
                startActivity(intent);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username = Email.getText().toString();
                password = Password.getText().toString();
                boolean flag = check(username,password);
                if(flag)
                {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    String code = response.toString();
                                    if (code.equals("1"))
                                    {
                                        UserDetails.Username = username.toString();
                                        Intent intent = new Intent(Login.this,MainActivity.class);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        builder.setTitle("Login error");
                                        display("Invalid email or password");
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
                            Map<String,String> params = new HashMap<String, String>();
                            params.put("user_name",username);
                            params.put("password",password);
                            return params;
                        }
                    };
                    Singleton.getmSingleton(Login.this).addRequest(stringRequest);

                }
                else
                {
                    builder.setTitle("Empty field");
                    display("Please enter the data into the fields");
                }



            }
        });

    }


    public boolean check(String user, String pass)
    {
        if(user.equals("") || pass.equals(""))
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public void display(String message)
    {
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                   Email.setText("");
                    Password.setText("");
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    @Override
    public void onBackPressed() {
        // Handle logout here
        UserDetails.Username = null;
        Intent intent = new Intent(this,Login.class);
        startActivity(intent);
        //System.exit(0);
    }
}
