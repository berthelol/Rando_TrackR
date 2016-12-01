package com.example.loic.rando_trackr;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;
import static android.R.attr.password;
import static android.R.id.message;
import static android.R.string.cancel;
import static com.example.loic.rando_trackr.R.id.sms;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {


    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private EditText mfirstname;
    private EditText mlastname;
    private EditText mphonenumber;
    private CheckBox msmscheck;
    private CheckBox mcallcheck;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Shared preference
        sharedPreferences = getApplicationContext().getSharedPreferences("com.example.loic.rando_trackr", Context.MODE_PRIVATE);
        //if already register pass the login page
        if(sharedPreferences.getBoolean("alreadyregister",false))
        {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            return;
        }
        // Set up the login form.
        mfirstname = (EditText) findViewById(R.id.firstname);
        mlastname = (EditText) findViewById(R.id.lastname);
        mphonenumber = (EditText) findViewById(R.id.telephonenumber);
        msmscheck = (CheckBox) findViewById(R.id.sms_check);
        mcallcheck = (CheckBox) findViewById(R.id.call_check);

        Button SignInButton = (Button) findViewById(R.id.sign_in_button);
        SignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
    }
    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mfirstname.setError(null);
        mlastname.setError(null);
        mphonenumber.setError(null);

        // Store values at the time of the login attempt.
        String firstname = mfirstname.getText().toString();
        String lastname = mlastname.getText().toString();
        String phonenumber = mphonenumber.getText().toString();
        Boolean sms = msmscheck.isChecked();
        Boolean call = mcallcheck.isChecked();

        boolean cancel = false;
        View focusView = null;
        //TODO
       /* // Check for a valid fistname.
        if (!firstname.isEmpty()) {
            mlastname.setError(getString(R.string.error_invalid_firstname));
            focusView = mlastname;
            cancel = true;
        }
        // Check for a valid lastname.
        if (!lastname.isEmpty()) {
            mfirstname.setError(getString(R.string.error_invalid_lastname));
            focusView = mfirstname;
            cancel = true;
        }
        // Check for a valid phonenumber.
        if (!phonenumber.isEmpty() && isPhonenumbervalid(phonenumber)) {
            mphonenumber.setError(getString(R.string.error_invalid_phonenumber));
            focusView = mphonenumber;
            cancel = true;
        }*/

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            mAuthTask = new UserLoginTask(firstname,lastname,phonenumber,sms,call);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isPhonenumbervalid(String phonenb) {
        return phonenb.length() >= 9;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private String fistname;
        private String lastname;
        private String phonenumber;
        private Boolean sms;
        private Boolean call;

        UserLoginTask(String firstame, String lastname,String phonenumber,Boolean sms,Boolean call) {
            this.fistname = firstame;
            this.lastname = lastname;
            this.phonenumber = phonenumber;
            this.sms = sms;
            this.call = call;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;


            if (success) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                //intent.putExtra(EXTRA_MESSAGE, message);
                sharedPreferences.edit().putString("firstname",this.fistname).apply();
                sharedPreferences.edit().putString("lastname",this.lastname).apply();
                sharedPreferences.edit().putString("phonenumber",this.phonenumber).apply();
                sharedPreferences.edit().putBoolean("call_checked",this.call).apply();
                sharedPreferences.edit().putBoolean("sms_checked",this.sms).apply();
                sharedPreferences.edit().putBoolean("alreadyregister",true).apply();
                startActivity(intent);
                finish();
            } else {
                mlastname.setError("Error connecting");
                mlastname.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;

        }
    }
}

