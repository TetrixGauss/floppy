package com.ath.floppy;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ath.floppy.util.PatternMatchUtil;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import javax.xml.validation.Validator;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    private static final int PASSWORD_MIN_LENGTH = 6;
    View registerView;
    private String register_url = "https://parseapi.back4app.com/users";

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        registerView = inflater.inflate(R.layout.fragment_register, container, false);

        buttonListener();
        return registerView;
    }

    private void buttonListener() {
        Button regButton = registerView.findViewById(R.id.btn_register);

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValidData()) {
                    createUser();
                    resetViewText(R.id.et_email);
                    resetViewText(R.id.password);
                    resetViewText(R.id.password2);
                    resetViewText(R.id.username);
                }
            }
        });
    }

    private boolean isValidData() {
        if (!PatternMatchUtil.isValidEmail(getViewText(R.id.et_email))) {
            Toast.makeText(getActivity().getApplicationContext(), "Email is incorrect!", Toast.LENGTH_SHORT).show();
        } else if (getViewText(R.id.password).length() < PASSWORD_MIN_LENGTH) {
            Toast.makeText(getActivity().getApplicationContext(), "Password needs to be minimum " + PASSWORD_MIN_LENGTH + " characters!", Toast.LENGTH_SHORT).show();
        } else if (!getViewText(R.id.password).equals(getViewText(R.id.password2))) {
            Toast.makeText(getActivity().getApplicationContext(), "The passwords do not match!", Toast.LENGTH_SHORT).show();
        } else if (!PatternMatchUtil.isValidName(getViewText(R.id.username))) {
            Toast.makeText(getActivity().getApplicationContext(), "User Name does not match requirements!", Toast.LENGTH_SHORT).show();
        } else {
            return true;
        }
        return false;
    }

    private String getViewText(int viewId) {
        View view = registerView.findViewById(viewId);
        if (view instanceof EditText) {
            return ((EditText) view).getText().toString().trim();
        } else if (view instanceof TextView) {
            return ((TextView) view).getText().toString().trim();
        } else {
            return "";
        }
    }

    private void resetViewText(int viewId) {
        View view = registerView.findViewById(viewId);
        if (view instanceof EditText) {
           ((EditText) view).setText("");
        } else if (view instanceof TextView) {
            ((TextView) view).setText("");
        }
    }

    public void createUser() {
        ParseUser user = new ParseUser();
        user.setUsername(getViewText(R.id.username));
        user.setPassword(getViewText(R.id.password));
        user.setEmail(getViewText(R.id.et_email));

        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    // Hooray! Let them use the app now.
                    Toast.makeText(getActivity().getApplicationContext(), "Please verify your email and Login!", Toast.LENGTH_LONG).show();
                } else {
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                    Toast.makeText(getActivity().getApplicationContext(), "Sign Up was not succeed! Please try again!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
