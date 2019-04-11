package com.example.mahnoor.CollaborativeVirtualCamp;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class Tasks extends Fragment {

    String[] header = {"Title","Status"};

    String[][] twodArray;
    TableView<String[]> tableView;
    String server_url = "http://w2academy.com/androidTasks.php";
    AlertDialog.Builder builder;
    ArrayList<String> values= new ArrayList<String>();



    public Tasks() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tasks2, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle("Tasks");

        tableView = (TableView<String[]>)getActivity().findViewById(R.id.tableView);
        tableView.setHeaderBackgroundColor(Color.parseColor("#B2BEB5"));
        tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(getActivity(),header));


        StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            int count = 0;
                            JSONArray jsonArray = new JSONArray(response);
                            twodArray = new String[jsonArray.length()][2];

                            while(count<jsonArray.length())
                            {
                                JSONObject jsonObject = jsonArray.getJSONObject(count);
                                values.add(jsonObject.getString("Title"));
                                //values.add(jsonObject.getString("Description"));
                                values.add(jsonObject.getString("Status"));
                                String[] arr=new String[3];
                                arr[0]= jsonObject.getString("Title").toString();
                               // arr[1]=jsonObject.getString("Description").toString();
                                arr[1]= jsonObject.getString("Status").toString();
                                for(int col=0;col<2;col++){
                                    twodArray[count][col]=arr[col];
                                }
                                count++;
                            }
                            //IDHAR LIKHNA HAI CONVERSION KA CODE. Is 'values' arraylist ko 2D me krna
                            // hai and neechay wali line me wo array deni hai 'values' ki jagah
                            //jsonArray.length() for number of rows reurned by server

                            tableView.setDataAdapter(new SimpleTableDataAdapter(getActivity(), twodArray));

                        }catch (Exception e)
                        {
                            Toast.makeText(getContext(),e.toString(),Toast.LENGTH_LONG).show();
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
                params.put("teamName",UserDetails.TeamName);
                return params;
            }
        };
        Singleton.getmSingleton(getContext()).addRequest(stringRequest);

    }
}