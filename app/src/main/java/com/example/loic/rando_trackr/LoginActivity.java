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
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;
import static android.R.attr.password;
import static android.R.id.message;
import static android.R.string.cancel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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

        Log.i("fistname",firstname);
        Log.i("lastname",lastname);
        Log.i("phonenumber",phonenumber);

        boolean cancel = false;
        View focusView = null;

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
            mAuthTask = new UserLoginTask(firstname,lastname,phonenumber);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isPhonenumbervalid(String phonenb) {
        //TODO: Replace this with your own logic
        return phonenb.length() >= 9;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private String fistname;
        private String lastname;
        private String phonenumber;

        UserLoginTask(String firstame, String lastname,String phonenumber) {
            this.fistname = firstame;
            this.lastname = lastname;
            this.phonenumber = phonenumber;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            // TODO: register the new account here.
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
                sharedPreferences.edit().putBoolean("alreadyregister",true).apply();
                startActivity(intent);
                //finish();
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

