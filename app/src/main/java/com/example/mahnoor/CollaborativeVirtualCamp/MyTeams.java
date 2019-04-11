package com.example.mahnoor.CollaborativeVirtualCamp;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
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

public class MyTeams extends Fragment {

    private String Team_url = "http://w2academy.com/androidGetMyAllTeams.php";
    AlertDialog.Builder builder;
    ListView listView;
    ArrayList<String> values= new ArrayList<String>();
    String length;
    ArrayAdapter<String> adapter;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listView = (ListView)getActivity().findViewById(R.id.all_teams);
        addOnClickListener();
        builder  = new AlertDialog.Builder(getContext());

        StringRequest jsonArrayRequest = new StringRequest(Request.Method.POST, Team_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        int count = 0;
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            while (count<jsonArray.length())
                            {
                                JSONObject jsonObject = jsonArray.getJSONObject(count);
                                values.add(jsonObject.getString("Team_Name"));

                                count++;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,
                                android.R.id.text1,values);
                        listView.setAdapter(adapter);
                        registerForContextMenu(listView);

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
                params.put("email",UserDetails.Username);
                return params;
            }
        };

        Singleton.getmSingleton(getContext()).addRequest(jsonArrayRequest);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.context_teams,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo adapterContextMenuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId())
        {
            case R.id.delete_team:
                String teamtodelet = (listView.getItemAtPosition(adapterContextMenuInfo.position)).toString();
                DeleteTeam(teamtodelet,UserDetails.Username,values,adapter,adapterContextMenuInfo);
                break;
            case R.id.teammates:
                String teammembers = (listView.getItemAtPosition(adapterContextMenuInfo.position)).toString();
                Teammates(teammembers,values,adapter,adapterContextMenuInfo);
                break;
            case R.id.repository:
                Repository repository = new Repository();
                String TeamName = (listView.getItemAtPosition(adapterContextMenuInfo.position)).toString();
                UserDetails.TeamName = TeamName;
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.frag_fram,repository).commit();
                break;
            case R.id.task:
                Tasks task = new Tasks();
                UserDetails.TeamName =(listView.getItemAtPosition(adapterContextMenuInfo.position)).toString();
                FragmentManager taskfragmentManager = getActivity().getSupportFragmentManager();
                taskfragmentManager.beginTransaction().replace(R.id.frag_fram,task).commit();
                break;
            /*case R.id.files:
                Repository rrepository = new Repository();
               // String tTeamName = (listView.getItemAtPosition(adapterContextMenuInfo.position)).toString();
                UserDetails.TeamName = (listView.getItemAtPosition(adapterContextMenuInfo.position)).toString();;
                FragmentManager ffragmentManager = getActivity().getSupportFragmentManager();
                ffragmentManager.beginTransaction().replace(R.id.frag_fram,rrepository).commit();
                break; */
            case R.id.invite_member:
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.invite_member, null);
                final TextView enterEmail = (TextView) alertLayout.findViewById(R.id.textView);
                final EditText email = (EditText) alertLayout.findViewById(R.id.email);
                final Button invite = (Button) alertLayout.findViewById(R.id.invite);


                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle("Invite member");
                // this is set the view from XML inside AlertDialog
                alert.setView(alertLayout);
                // allow cancel of AlertDialog on click of back button and outside touch
                alert.setCancelable(true);
              //  alert.setNegativeButton("Cancel", null);
                alert.show();
                invite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String username = email.getText().toString();

                        if(username=="" || (!username.contains("@")) || (!username.contains(".com"))){
                            Toast.makeText(getContext(),"Please Enter Correct Email",Toast.LENGTH_LONG).show();
                        }
                        else{
                            sendInvitation(email.getText().toString());
                        }
                    }

                });
                break;
            case R.id.add_task:
                LayoutInflater addTaskInflater = getActivity().getLayoutInflater();
                View addTaskAlertLayout = addTaskInflater.inflate(R.layout.add_task, null);
                final TextView titleView = (TextView) addTaskAlertLayout.findViewById(R.id.textView2);
                final TextView descriptionView = (TextView) addTaskAlertLayout.findViewById(R.id.textView3);
                final EditText title = (EditText) addTaskAlertLayout.findViewById(R.id.title);
                final EditText description = (EditText) addTaskAlertLayout.findViewById(R.id.description);

                final Button add = (Button) addTaskAlertLayout.findViewById(R.id.add);

                String taskTitle = title.getText().toString();
                String taskDescription = description.getText().toString();

                AlertDialog.Builder addTaskAlert = new AlertDialog.Builder(getContext());
                addTaskAlert.setTitle("Add Task");
                // this is set the view from XML inside AlertDialog
                addTaskAlert.setView(addTaskAlertLayout);
                // allow cancel of AlertDialog on click of back button and outside touch
                addTaskAlert.setCancelable(true);
              //  addTaskAlert.setNegativeButton("Cancel", null);
                addTaskAlert.show();
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String taskTitle = title.getText().toString();
                        String taskDescription = description.getText().toString();
                        if(taskTitle==" " || taskDescription==" "){
                            Toast.makeText(getContext(),"Please enter the required information",Toast.LENGTH_LONG).show();
                        }
                        else{
                            addTask(title.getText().toString(), description.getText().toString());
                        }
                    }

                });
                break;
        }

        return super.onContextItemSelected(item);
    }

    public void Teammates(final String selectedItem, final ArrayList values,final ArrayAdapter adapter, final AdapterView.AdapterContextMenuInfo adapterContextMenuInfo)
    {
        values.clear();
        getActivity().setTitle(selectedItem+ "Teams Mates");
        String server_url = "http://w2academy.com/androidTeammates.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                int count = 0;
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    while (count<jsonArray.length())
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(count);
                        values.add(jsonObject.getString("Team_Member"));

                        count++;
                    }
                    adapter.notifyDataSetChanged();
                    unregisterForContextMenu(listView);
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
                param.put("teamName",selectedItem);
                return param;
            }
        };

        Singleton.getmSingleton(getContext()).addRequest(stringRequest);


    }
    private void DeleteTeam(final String selectedItem, final String UserEmail,final ArrayList values,final ArrayAdapter adapter, final AdapterView.AdapterContextMenuInfo adapterContextMenuInfo)
    {
        String server_url = "http://w2academy.com/androidDeleteTeams.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String code = response.toString();
                if(code.equals("1"))
                {
                    values.remove(adapterContextMenuInfo.position);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getContext(),selectedItem + " Team Deleted Successfully",Toast.LENGTH_LONG).show();
                }
                else if(code.equals("0")){
                    Toast.makeText(getContext(),selectedItem + " Try Again Team Can not deleted this time",Toast.LENGTH_LONG).show();
                }
                else {
                    values.remove(adapterContextMenuInfo.position);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getContext(),selectedItem + " Team Deleted Successfully",Toast.LENGTH_LONG).show();
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
                param.put("email", UserEmail);
                param.put("teamName",selectedItem);
                return param;
            }
        };

        Singleton.getmSingleton(getContext()).addRequest(stringRequest);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_team, container, false);

//        RecyclerView recyclerView = (RecyclerView) inflater.inflate(
//                R.layout.recycler_view, container, false);
//        MyAllTeamsAdaptor adapter = new MyAllTeamsAdaptor(recyclerView.getContext(), MyAllTeams.getData());
//        recyclerView.setAdapter(adapter);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        return recyclerView;
    }

    public MyTeams() {
        // Required empty public constructor
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

    public void sendInvitation(final String email){
        String url = "http://w2academy.com/androidInviteMember.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String code = response.toString();
                        if(code.equals("1")){
                            Toast.makeText(getContext(),"Invitation has been sent",Toast.LENGTH_LONG).show();

                        }
                        else if(code.equals("0")){
                            Toast.makeText(getContext(),"Failed to send invitation. Please try later",Toast.LENGTH_LONG).show();
                        }
                        else {

                            Toast.makeText(getContext(),"Invitation has been sent",Toast.LENGTH_LONG).show();
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
                param.put("email", email);
                param.put("teamName", UserDetails.TeamName);
                return param;
            }
        };
        Singleton.getmSingleton(getContext()).addRequest(stringRequest);
    }


    public void addTask(final String title, final String description){

        String url = "http://w2academy.com/androidAddTask.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String code = response.toString();
                        if(code.equals("1")){
                            // sendEmail(email,password);
                            Toast.makeText(getContext(),"A task has been added",Toast.LENGTH_LONG).show();

                        }else {
                            Toast.makeText(getContext(),"Failed to add task",Toast.LENGTH_LONG).show();
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
                param.put("title", title);
                param.put("description", description);
                param.put("teamName", UserDetails.TeamName);

                return param;
            }
        };
        Singleton.getmSingleton(getContext()).addRequest(stringRequest);
    }

    private void addOnClickListener()
    {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                view.showContextMenu();
            }
        });
    }
}

