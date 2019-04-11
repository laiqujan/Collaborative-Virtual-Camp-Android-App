package com.example.mahnoor.CollaborativeVirtualCamp;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyAccountFragment extends Fragment {


    public MyAccountFragment() {
        // Required empty public constructor
    }

    EditText Name,Contact,Profession,Password;
    TextView Email;
    Button button;
    AlertDialog.Builder builder;
    String email,name,contact,profession,password;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_my_account, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        builder  = new AlertDialog.Builder(getContext());
        Email       = (TextView)getActivity().findViewById(R.id.email_id);
        Name        = (EditText)getActivity().findViewById(R.id.name);
        Contact     = (EditText)getActivity().findViewById(R.id.contact);
        Profession  = (EditText)getActivity().findViewById(R.id.profession);
        Password    = (EditText)getActivity().findViewById(R.id.password);

        button      = (Button)getActivity().findViewById(R.id.update);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = Email.getText().toString();
                name = Name.getText().toString();
                contact = Contact.getText().toString();
                profession = Profession.getText().toString();
                password = Password.getText().toString();
                boolean flage = check(email, name, contact, profession, password);
                if (flage){
                    saveData(email, name, contact, profession, password);
                }else{
                    builder.setTitle("Failed");
                    display("Fields can not be empty");
                }
            }
        });

        String url = "http://w2academy.com/androidGetInfo.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        int count = 0;
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            while (count<jsonArray.length())
                            {
                                JSONObject jsonObject = jsonArray.getJSONObject(count);
                                Email.setText(jsonObject.getString("email"));
                                Name.setText(jsonObject.getString("name"));
                                Contact.setText(jsonObject.getString("contactNo"));
                                Profession.setText(jsonObject.getString("profession"));
                                Password.setText(jsonObject.getString("password"));

                                count++;
                            }
                        }catch (Exception e){
                            display(e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                display(error.toString());
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("email",UserDetails.Username);
                return params;
            }
        };
        Singleton.getmSingleton(getContext()).addRequest(stringRequest);
    }

    private void saveData(final String email, final String name, final String contact, final  String profession,final String password) {
        String url = "http://w2academy.com/androidsaveUpdatedInfo.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String code = response.toString();
                        if(code.equals("1")){
                            getActivity().setTitle("My Account");
                            MyAccountFragment myAccountFragment = new MyAccountFragment();
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            fragmentManager.beginTransaction().replace(R.id.frag_fram,myAccountFragment).commit();
                        }else {
                            builder.setTitle("Failed");
                            display("can not update detail for now, try again later");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {
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
        Singleton.getmSingleton(getContext()).addRequest(stringRequest);
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
}
