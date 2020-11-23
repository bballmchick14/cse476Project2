package edu.msu.team23.project2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import edu.msu.team23.project2.cloud.Cloud;
import edu.msu.team23.project2.cloud.models.CheckersResult;

public class LoginActivity extends AppCompatActivity {
    private static final int MAX_USERNAME_LENGTH = 32;
    private static final int MAX_PASSWORD_LENGTH = 16;
    public static final String USERNAME = "LoginActivity.username";
    public static final String PASSWORD = "LoginActivity.password";

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
        if (areFieldsValid(usernameField, passwordField)) {
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
                                Intent intent = new Intent(activity, MenuActivity.class);
                                intent.putExtra(USERNAME, usernameField.getText().toString());
                                intent.putExtra(PASSWORD, passwordField.getText().toString());
                                startActivity(intent);
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
                                    usernameField.setError(getResources().getString(R.string.user_error));
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
        // TODO
    }

    /**
     * Are the username and password fields in the correct format?
     * @return Are the username and password fields in the correct format?
     */
    private boolean areFieldsValid(EditText usernameField, EditText passwordField) {
        boolean areFieldsValid = true;

        if (usernameField != null && passwordField != null) {
            String username = usernameField.getText().toString();
            String password = passwordField.getText().toString();

            if (password.length() < 1 || password.length() > MAX_PASSWORD_LENGTH) {
                areFieldsValid = false;
                passwordField.setError(getResources().getString(R.string.invalid_password_length_error));
            }
            if (username.length() < 1 || username.length() > MAX_USERNAME_LENGTH) {
                areFieldsValid = false;
                usernameField.setError(getResources().getString(R.string.invalid_username_length_error));
            }
        } else {
            areFieldsValid = false;
        }

        return areFieldsValid;
    }
}