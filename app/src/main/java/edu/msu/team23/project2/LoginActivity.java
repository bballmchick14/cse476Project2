package edu.msu.team23.project2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import edu.msu.team23.project2.cloud.Cloud;
import edu.msu.team23.project2.cloud.DatabaseConstants;
import edu.msu.team23.project2.cloud.models.CheckersResult;

/**
 * Activity for handling logging into the application.
 */
public class LoginActivity extends AppCompatActivity {
    /**
     * Key for the user's name.
     */
    public static final String USERNAME = "LoginActivity.username";

    /**
     * Key for the user's password.
     */
    public static final String PASSWORD = "LoginActivity.password";

    /**
     * Response code for creating a user.
     */
    private static final int CREATE_USER = 1;

    /**
     * Service for handling and saving the user's credentials.
     */
    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Create the user service
        userService = new UserService(this,
                1, DatabaseConstants.MAX_USERNAME_LENGTH,
                1, DatabaseConstants.MAX_PASSWORD_LENGTH);

        fillRemembered();
    }

    /**
     * Handler for when the login button in pressed.
     * @param view View the event came from
     */
    public void onLogin(View view) {
        // Get the fields for the username and password
        final EditText usernameField = getUsernameField();
        final EditText passwordField = getPasswordField();

        // Set the state of the remembered user information
        if(getRememberCheckBox().isChecked()) {
            userService.setRemember(usernameField.getText().toString(), passwordField.getText().toString());
        } else {
            userService.clearRemember();
        }

        // Only attempt a login if the input is valid
        if (userService.areFieldsValid(usernameField, passwordField, null)) {
            // Activity thread is in
            final LoginActivity activity = this;

            // Start a thread to log in out of the UI thread
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
                                if (result == null) {
                                    Toast.makeText(activity, R.string.server_connection_error, Toast.LENGTH_SHORT).show();
                                }
                                else if (result.getMessage().equals(Cloud.PASSWORD_ERROR)) {
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
        // Start the create user activity
        Intent intent = new Intent(this, CreateUserActivity.class);
        startActivityForResult(intent, CREATE_USER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            // The response is from creating a user and it succeeded.
            // Continue to the menu activity with the newly created username and password.
            if(requestCode == CREATE_USER && resultCode == Activity.RESULT_OK) {
                // Notify the user that the new user has been created
                Toast.makeText(this, String.format(getString(R.string.user_created), data.getStringExtra(CreateUserActivity.USERNAME)), Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Start the menu activity
     * @param activity Activity starting from
     * @param username User's username
     * @param password User's password
     */
    private void startMenuActivity(LoginActivity activity, String username, String password) {
        // Remove errors from the errors from the EditTexts on the login activity
        getUsernameField().setError(null);
        getPasswordField().setError(null);

        // Set the fields to their remembered state
        fillRemembered();

        // Start the menu activity passing through the user's name and password
        Intent intent = new Intent(activity, MenuActivity.class);
        intent.putExtra(USERNAME, username);
        intent.putExtra(PASSWORD, password);
        startActivity(intent);
    }

    /**
     * Fill the username, password, and remembered fields with remembered data.
     */
    private void fillRemembered() {
        if (userService.isRemembered()) {
            getRememberCheckBox().setChecked(true);
            getUsernameField().setText(userService.getRememberedUsername());
            getPasswordField().setText(userService.getRememberedPassword());
        } else {
            getRememberCheckBox().setChecked(false);
            getUsernameField().setText("");
            getPasswordField().setText("");
        }
    }

    /**
     * Get the remember check box.
     * @return The remember check box
     */
    private CheckBox getRememberCheckBox() {
        return findViewById(R.id.rememberCheckBox);
    }

    /**
     * Get the username field.
     * @return The username field
     */
    private EditText getUsernameField() {
        return findViewById(R.id.loginUsername);
    }

    /**
     * Get the password field.
     * @return The password field
     */
    private EditText getPasswordField() {
        return findViewById(R.id.loginPassword);
    }
}