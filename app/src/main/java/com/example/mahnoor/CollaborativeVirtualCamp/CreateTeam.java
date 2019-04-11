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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateTeam extends Fragment {


    public CreateTeam() {
        // Required empty public constructor
    }
    TextView textView;
    EditText teamName,category,description;
    String teamname,cat,desc;
    Button button;
    AlertDialog.Builder builder;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_team, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        builder  = new AlertDialog.Builder(getContext());

        textView = (TextView)getActivity().findViewById(R.id.email_id);
        textView.setText(UserDetails.Username.toString());
        button = (Button) getActivity().findViewById(R.id.create);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                teamName = (EditText)getActivity().findViewById(R.id.teamName);
                teamname = teamName.getText().toString();
                category = (EditText)getActivity().findViewById(R.id.category);
                cat      = category.getText().toString();
                description= (EditText)getActivity().findViewById(R.id.description);
                desc    = description.getText().toString();
                if(check(teamname,cat,desc)) {
                    saveData(teamname,cat,desc);
                }else {
                    display("Fill all the fields");
                }
            }
        });

    }

    public boolean check(String teamname,String cat, String desc)
    {
        if(teamname.equals("")||cat.equals("")||desc.equals(""))
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public void saveData(final String teamname, final String cat, final String desc)
    {
        String url = "http://w2academy.com/androidCreateTeam.php";
        builder  = new AlertDialog.Builder(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String code = response.toString();
                        if(code.equals("1")){
                            builder.setTitle("Success");
                            display("Team created successfull");
                            getActivity().setTitle("My Teams List");
                            MyTeams myTeams = new MyTeams();
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            fragmentManager.beginTransaction().replace(R.id.frag_fram, myTeams).commit();
                        }
                        else if(code.equals("0")){
                            builder.setTitle("Failed, try again later");
                            display("Team already exists, try another name");
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),error.toString(),Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("email",UserDetails.Username);
                params.put("teamName",teamname);
                params.put("category",cat);
                params.put("description",desc);
                return params;
            }
        };
        Singleton.getmSingleton(getContext()).addRequest(stringRequest);
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
