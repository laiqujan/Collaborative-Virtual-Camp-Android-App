package com.example.mahnoor.CollaborativeVirtualCamp;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class FileFragment extends Fragment {

    public String fileUrl;
    public FileFragment() {
        // Required empty public constructor
    }

    AlertDialog.Builder builder;
    ListView listView;
    ArrayList<String> values= new ArrayList<String>();
    String length;
    ArrayAdapter<String> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_file_fregment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle("Files");
        listView = (ListView) getActivity().findViewById(R.id.files_list);
        addOnClickListener();
        builder  = new AlertDialog.Builder(getContext());
        String files_url = "http://w2academy.com/androidFiles.php";
        StringRequest jsonArrayRequest = new StringRequest(Request.Method.POST, files_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        int count = 0;
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            while (count<jsonArray.length())
                            {
                                JSONObject jsonObject = jsonArray.getJSONObject(count);
                                values.add(jsonObject.getString("files"));

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
                params.put("repositoryName",UserDetails.Repository);
                return params;
            }
        };

        Singleton.getmSingleton(getContext()).addRequest(jsonArrayRequest);


    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.files_menu_option,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo adapterContextMenuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId())
        {
            case R.id.delete:

                String fileToDelete = (listView.getItemAtPosition(adapterContextMenuInfo.position)).toString();
                DeleteFile(fileToDelete,values,adapter,adapterContextMenuInfo);
                break;
            case R.id.view_versions:
                ViewVersions versions= new ViewVersions();
                String File = (listView.getItemAtPosition(adapterContextMenuInfo.position)).toString();
                UserDetails.File=File;
                FragmentManager versionfragmentManager = getActivity().getSupportFragmentManager();
                versionfragmentManager.beginTransaction().replace(R.id.frag_fram,versions).commit();
                break;

        }

        return super.onContextItemSelected(item);
    }




    private void DeleteFile(final String filetodelete, final ArrayList<String> values,final ArrayAdapter<String> adapter,final AdapterView.AdapterContextMenuInfo adapterContextMenuInfo) {
        String server_url = "http://w2academy.com/androidDeleteFile.php";
        builder  = new AlertDialog.Builder(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                String code = response.toString();
                if(code.equals("1"))
                {
                    values.remove(adapterContextMenuInfo.position);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getContext(),"Deleted",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getContext(),"Not Deleted",Toast.LENGTH_LONG).show();
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
                param.put("file", filetodelete);
                param.put("repository", UserDetails.Repository);
                return param;
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
