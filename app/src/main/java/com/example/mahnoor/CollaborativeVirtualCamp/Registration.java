package com.example.mahnoor.CollaborativeVirtualCamp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class Registration extends AppCompatActivity {

    Toolbar toolbar;
    EditText Email,Name,Contact,Profession,Password;
    Button button;
    AlertDialog.Builder builder;
    String server_url = "http://w2academy.com/androidRegistration.php";
    String email,name,contact,profession,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        toolbar = (Toolbar) findViewById(R.id.toolbar_register);
        toolbar.setTitle("Register");
        toolbar.setTitleTextColor(Color.WHITE);

        builder  = new AlertDialog.Builder(Registration.this);
        Email       = (EditText) findViewById(R.id.email);
        Name        = (EditText) findViewById(R.id.name);
        Contact     = (EditText) findViewById(R.id.contact);
        Profession  = (EditText) findViewById(R.id.profession);
        Password    = (EditText) findViewById(R.id.password);

        button      = (Button) findViewById(R.id.signup);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = Email.getText().toString();
                name = Name.getText().toString();
                contact = Contact.getText().toString();
                profession = Profession.getText().toString();
                password = Password.getText().toString();



                boolean flage = check(email, name, contact, profession, password);
                flage=isValidEmailAddress(email);

                if (flage) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    String code = response.toString();
                                    if (code.equals("1"))
                                    {
                                        builder.setTitle("Registration successful");
                                        display("User Registered Successfully. Press OK to Login");
                                        Intent intent = new Intent(Registration.this,Login.class);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        builder.setTitle("Registration Error");
                                        display(response.toString());
                                    }

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> param = new HashMap<String, String>();
                            param.put("email", email);
                            param.put("name", name);
                            param.put("contact", contact);
                            param.put("profession", profession);
                            param.put("password", password);
                            return param;
                        }
                    };
                    Singleton.getmSingleton(Registration.this).addRequest(stringRequest);
                }
                else
                {
                    builder.setTitle("Registration Failure");
                    display("Some data may be lost or user already exists.Try again Later");
                }
            }

        });



    }

    public boolean check(String Email,String Name,String Contact,String Profession,String Password)
    {
        if(Email.equals("")||Name.equals("")||Contact.equals("")||Profession.equals("")||Password.equals(""))
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

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }
}
