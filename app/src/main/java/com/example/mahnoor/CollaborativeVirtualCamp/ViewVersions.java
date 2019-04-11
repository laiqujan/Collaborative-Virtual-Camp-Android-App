package com.example.mahnoor.CollaborativeVirtualCamp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
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
 * Activities that contain this fragment must implement the
 * {@link ViewVersions.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ViewVersions#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewVersions extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ViewVersions() {
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
        return inflater.inflate(R.layout.fragment_view_versions, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().setTitle("Versions");
        listView = (ListView) getActivity().findViewById(R.id.view_versions);
        addOnClickListener();
        builder  = new AlertDialog.Builder(getContext());
        String files_url = "http://w2academy.com/androidViewVersions.php";
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
                                values.add(jsonObject.getString("Versions"));

                                count++;
                            }
                      //      Log.d("count",""+count);

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
                params.put("file",UserDetails.File);
                params.put("repository",UserDetails.Repository);
                params.put("teamName",UserDetails.TeamName);
                return params;
            }
        };

        Singleton.getmSingleton(getContext()).addRequest(jsonArrayRequest);


    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.version_menu_options,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo adapterContextMenuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId())
        {
            case R.id.delete:

                String versionToDelete = (listView.getItemAtPosition(adapterContextMenuInfo.position)).toString();
                DeleteVersion(versionToDelete,values,adapter,adapterContextMenuInfo);
                break;


        }

        return super.onContextItemSelected(item);
    }

    private void DeleteVersion(final String versiontodelete, final ArrayList<String> values,final ArrayAdapter<String> adapter,final AdapterView.AdapterContextMenuInfo adapterContextMenuInfo) {
        String server_url = "http://w2academy.com/androidDeleteVersion.php";
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
                Toast.makeText(getContext(),"An error occured while running the application",Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("version", versiontodelete);
                param.put("file", UserDetails.File);
                param.put("repository",UserDetails.Repository);
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




    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewVersions.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewVersions newInstance(String param1, String param2) {
        ViewVersions fragment = new ViewVersions();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
