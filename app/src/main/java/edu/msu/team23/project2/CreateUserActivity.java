package edu.msu.team23.project2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import edu.msu.team23.project2.cloud.Cloud;
import edu.msu.team23.project2.cloud.DatabaseConstants;
import edu.msu.team23.project2.cloud.models.CheckersResult;

/**
 * Class for handling creating a new user.
 */
public class CreateUserActivity extends AppCompatActivity {
    /**
     * Key for the new user's name.
     */
    public static final String USERNAME = "CreateUserActivity.username";

    /**
     * Service for handling and saving the user's credentials.
     */
    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        userService = new UserService(this,
                1, DatabaseConstants.MAX_USERNAME_LENGTH,
                1, DatabaseConstants.MAX_PASSWORD_LENGTH);
    }

    /**
     * Handles when the create user button is pressed
     * @param view The view the event came from
     */
    public void onCreateUser(View view) {
        final EditText usernameField = findViewById(R.id.loginUsername);
        final EditText passwordField = findViewById(R.id.loginPassword);
        final EditText passwordConfirmField = findViewById(R.id.loginPasswordConfirm);

        // Only attempt a login if the input is valid
        if (userService.areFieldsValid(usernameField, passwordField, passwordConfirmField)) {
            // Activity thread is in
            final CreateUserActivity activity = this;

            new Thread(new Runnable() {
                @Override
                public void run() {
                    // Attempt create user call to the server
                    Cloud cloud = new Cloud();
                    final CheckersResult result = cloud.createUser(usernameField.getText().toString(), passwordField.getText().toString());

                    if (result != null && result.getStatus().equals(Cloud.STATUS_SUCCESS)) {
                        // Login was successful, start the menu activity
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent result = new Intent();
                                result.putExtra(USERNAME, usernameField.getText().toString());
                                setResult(Activity.RESULT_OK, result);
                                finish();
                            }
                        });
                    } else {
                        // Login failed, show corresponding error
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (result == null) {
                                    Toast.makeText(activity, R.string.server_connection_error, Toast.LENGTH_SHORT).show();
                                } else if (result.getMessage().equals(Cloud.NAME_TAKEN_ERROR)) {
                                    usernameField.setError(getResources().getString(R.string.taken_username_error));
                                }
                            }
                        });
                    }
                }
            }).start();
        }
    }
}