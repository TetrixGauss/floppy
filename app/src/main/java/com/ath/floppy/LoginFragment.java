package com.ath.floppy;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ath.floppy.util.PatternMatchUtil;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    View loginView;
    private static final int PASSWORD_MIN_LENGTH = 6;
    private LoginActivity activity;
    private  String user_url = "";
    private String login_url = "https://parseapi.back4app.com/login";
    ProgressBar progressBar;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (LoginActivity) getActivity();
    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        loginView = inflater.inflate(R.layout.fragment_login, container, false);
        progressBar = loginView.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.INVISIBLE);

        buttonListener();
        return loginView;
    }

    private void buttonListener(){
//        final Intent intent = new Intent(getActivity(), MainActivity.class);
        Button loginButton = loginView.findViewById(R.id.btn_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValidData()) {
//                    if (login()) {
//                        startActivity(intent);
//                    }
                    progressBar.setVisibility(View.VISIBLE);
                    loginUser();
                    resetViewText(R.id.user_name);
                    resetViewText(R.id.user_password);
                }
            }
        });
    }

    private boolean isValidData() {
        if (!PatternMatchUtil.isValidName(getViewText(R.id.user_name))) {
            Toast.makeText(getActivity().getApplicationContext(), "User Name is incorrect!", Toast.LENGTH_SHORT).show();
        } else if (getViewText(R.id.user_password).length() < PASSWORD_MIN_LENGTH) {
            Toast.makeText(getActivity().getApplicationContext(), "Password needs to be minimum " + PASSWORD_MIN_LENGTH + " characters!", Toast.LENGTH_SHORT).show();
        } else {
            return true;
        }
        return false;
    }

    private String getViewText(int viewId) {
        View view = loginView.findViewById(viewId);
        if (view instanceof EditText) {
            return ((EditText) view).getText().toString().trim();
        } else if (view instanceof TextView) {
            return ((TextView) view).getText().toString().trim();
        } else {
            return "";
        }
    }

    private void resetViewText(int viewId) {
        View view = loginView.findViewById(viewId);
        if (view instanceof EditText) {
            ((EditText) view).setText("");
        } else if (view instanceof TextView) {
            ((TextView) view).setText("");
        }
    }

    public void loginUser() {
        final Intent intent = new Intent(getActivity(), MainActivity.class);
        ParseUser.logInInBackground(getViewText(R.id.user_name), getViewText(R.id.user_password), new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    // Hooray! The user is logged in.
                    startActivity(intent);
                } else {
                    // Signup failed. Look at the ParseException to see what happened.
                    Toast.makeText(getActivity().getApplicationContext(), "Login was not succeed. Please try again!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


//    private boolean login() {
////        activity.showProgress(true);
//        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
////        String login_url = "https://floppy-xs.herokuapp.com/";
////        final String emailFinal = getViewText(R.id.user_email);
////        final String passwordFinal = getViewText(R.id.user_password);
//
//        // Request a string response from the provided URL.
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, login_url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        // Display the first 500 characters of the response string.
//                        Log.d("COMMUNICATION", response);
//
////                        server = new Gson().fromJson(response, ServerResponse.class);
////
////                        next_url = server.getNext();
////                        previous_url = server.getPrevious();
////                        data = (ArrayList<Result>) server.getResults();
////                        if (previous_url == null){
////                            dataDB = data;
////                        }else {
////                            dataDB.addAll(data);
////                        }
////
////                        for (Result res : dataDB
////                        ) {
////                            Log.d("COMMUNICATION", res.toString());
////
////                            resultViewModel.insert(res);
////                            platformData = res.getPlatforms();
////
//////                            for (Platform pr : platformData ){ //platformsData.getPlatforms()
//////                                platformViewModel.insert(pr);
//////                                pr.setGame_id(res.getId());
//////                                Log.d("PLATFORMS", pr.toString());
//////
//////                                pr.setId(platformData.indexOf(pr));
//////                            }
////                        }
////
////                        Gson gson = new GsonBuilder().create();
////
////                        json = gson.toJson(server.getResults());
////                        for (Result res: server.getResults()){
//////                            res.getPlatforms();
////                            String jsonPlat = gson.toJson(res.getPlatforms());
////                            Log.d("plats", jsonPlat);
////                        }
//////                        String jsonPlat = gson.toJson(server.getResults())
////                        jsonServer = gson.toJson(server);
////
////                        Log.d("RESULTS", json);
////                        if (previous_url == null) {
////                            adapter = new Adapter(data);
////                        }else {
////                            for (Result r: data){
////                                adapter.getResults().add(r);
////                            }
////                        }
////
////                        recyclerView.setAdapter(adapter);
////                        recyclerView.scrollToPosition(adapter.getItemCount() - 40);
//
//                    }
//                }, new Response.ErrorListener() {
//            //            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.d("COMMUNICATION", error.getMessage());
//
//                if( error instanceof NetworkError) {
//                    //handle your network error here.
//                    Toast.makeText(getActivity().getApplicationContext(), "Please check your internet connection and try again!", Toast.LENGTH_LONG).show();
//                } else if( error instanceof ServerError) {
//                    //handle if server error occurs with 5** status code
//                    Log.d("COMMUNICATION", error.getMessage());
//                    Toast.makeText(getActivity().getApplicationContext(), "There was an error with the server! Please try again!", Toast.LENGTH_SHORT).show();
//                } else if( error instanceof AuthFailureError) {
//                    //handle if authFailure occurs.This is generally because of invalid credentials
//                    Log.d("COMMUNICATION", error.getMessage());
//                    Toast.makeText(getActivity().getApplicationContext(), "Please try again!", Toast.LENGTH_SHORT).show();
//                } else if( error instanceof ParseError) {
//                    //handle if the volley is unable to parse the response data.
//                    Log.d("COMMUNICATION", error.getMessage());
//                    Toast.makeText(getActivity().getApplicationContext(), "Please try again!", Toast.LENGTH_SHORT).show();
//                } else if( error instanceof TimeoutError) {
//                    //handle if socket time out is occurred.
//                    Log.d("COMMUNICATION", error.getMessage());
//                    Toast.makeText(getActivity().getApplicationContext(), "Please wait...", Toast.LENGTH_LONG).show();
////                    login(source);
//                }
//            }
//        });
//
//        queue.add(stringRequest);
//
//        return false;
//    }

}
