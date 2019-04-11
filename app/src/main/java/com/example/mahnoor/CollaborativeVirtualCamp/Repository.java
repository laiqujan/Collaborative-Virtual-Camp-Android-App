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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;




/**
 * A simple {@link Fragment} subclass.
 */
public class Repository extends Fragment {



    EditText editText;
    Button button;
    AlertDialog.Builder builder;
    ListView listView;
    ArrayList<String> values= new ArrayList<String>();
    String length;
    ArrayAdapter<String> adapter;

    public Repository() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_repository, container, false);

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        getActivity().setTitle("Repositories");
        //editText = (EditText)getActivity().findViewById(R.id.editText);
        //button   = (Button) getActivity().findViewById(R.id.button);
        listView = (ListView)getActivity().findViewById(R.id.respository_list_view);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               FileFragment filefragment = new FileFragment();
                String Repository = listView.getItemAtPosition(position).toString();
                UserDetails.Repository = Repository;
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.frag_fram,filefragment).commit();

            }
        });




        getRepositoryList();
        /* button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editText.getText().toString();
                createRepository(name);
            }
        } ); */
    }

    private void getRepositoryList() {
        String server_url = "http://w2academy.com/androidGetRepositories.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                int count = 0;
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    while (count<jsonArray.length())
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(count);
                        values.add(jsonObject.getString("Repositories"));

                        count++;
                    }
                    adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,
                            android.R.id.text1,values);
                    listView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
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
                Map<String, String> param = new HashMap<String, String>();
                param.put("teamName", UserDetails.TeamName);
                return param;
            }
        };

        Singleton.getmSingleton(getContext()).addRequest(stringRequest);

    }

    /* private void createRepository(final String name) {
        builder  = new AlertDialog.Builder(getContext());
        String rep_url = "http://w2academy.com/androidCreateRepository.php";
        StringRequest stringrequest = new StringRequest(Request.Method.POST, rep_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(response.toString().equals("1")){
                            builder.setTitle("Success");
                            display("repository created successfully");
                            Repository repository = new Repository();
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            fragmentManager.beginTransaction().replace(R.id.frag_fram,repository).commit();

                        }
                        else{
                            builder.setTitle("Failed");
                            display("Repository already exists, try new name");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getContext(), error.toString(),Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("teamName",UserDetails.TeamName);
                params.put("RepositoryName",name);
                return params;
            }
        };

        Singleton.getmSingleton(getContext()).addRequest(stringrequest);
    } */

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
