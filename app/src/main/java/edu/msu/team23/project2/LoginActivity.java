package edu.msu.team23.project2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import edu.msu.team23.project2.cloud.Cloud;
import edu.msu.team23.project2.cloud.models.CheckersResult;

public class LoginActivity extends AppCompatActivity {
    public static final String USERNAME = "LoginActivity.username";
    public static final String PASSWORD = "LoginActivity.password";
    private static final int CREATE_USER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    /**
     * Handler for when the login button in pressed.
     * @param view View the event came from
     */
    public void onLogin(View view) {
        final EditText usernameField = findViewById(R.id.loginUsername);
        final EditText passwordField = findViewById(R.id.loginPassword);

        // Only attempt a login if the input is valid
        if (UserCredentialFormatService.areFieldsValid(this, usernameField, passwordField)) {
            // Activity thread is in
            final LoginActivity activity = this;

            new Thread(new Runnable() {
                @Override
                public void run() {
                    // Attempt login call to the server
                    Cloud cloud = new Cloud();
                    final CheckersResult result = cloud.loginUser(usernameField.getText().toString(), passwordField.getText().toString());

                    if (result != null && result.getStatus().equals(Cloud.STATUS_SUCCESS)) {
                        // Login was successful, start the menu activity
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startMenuActivity(activity, usernameField.getText().toString(), passwordField.getText().toString());
                            }
                        });
                    } else {
                        // Login failed, show corresponding error
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (result != null && result.getMessage().equals(Cloud.PASSWORD_ERROR)) {
                                    passwordField.setError(getResources().getString(R.string.password_error));
                                } else {
                                    usernameField.setError(getResources().getString(R.string.nonexistent_user_error));
                                }
                            }
                        });
                    }
                }
            }).start();
        }
    }

    /**
     * Handler for when the create user button is pressed.
     * @param view View the event came from
     */
    public void onCreateUser(View view) {
        Intent intent = new Intent(this, CreateUserActivity.class);
        startActivityForResult(intent, CREATE_USER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if(requestCode == CREATE_USER && resultCode == Activity.RESULT_OK) {
                startMenuActivity(
                        this,
                        data.getStringExtra(CreateUserActivity.USERNAME),
                        data.getStringExtra(CreateUserActivity.PASSWORD)
                );
            }
        }

    }

    /**
     * Start the menu activity
     * @param activity Activity starting from
     * @param username User's username
     * @param password User's password
     */
    public void startMenuActivity(LoginActivity activity, String username, String password) {
        Intent intent = new Intent(activity, MenuActivity.class);
        intent.putExtra(USERNAME, username);
        intent.putExtra(PASSWORD, password);
        startActivity(intent);
    }
}