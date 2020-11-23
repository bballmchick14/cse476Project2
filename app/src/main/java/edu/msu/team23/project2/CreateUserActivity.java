package edu.msu.team23.project2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import edu.msu.team23.project2.cloud.Cloud;
import edu.msu.team23.project2.cloud.DatabaseConstants;
import edu.msu.team23.project2.cloud.models.CheckersResult;

public class CreateUserActivity extends AppCompatActivity {
    public static final String USERNAME = "CreateUserActivity.username";
    public static final String PASSWORD = "CreateUserActivity.password";
    public static final String REMEMBER = "CreateUserActivity.remember";

    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        userService = new UserService(this,
                0, DatabaseConstants.MAX_USERNAME_LENGTH,
                0, DatabaseConstants.MAX_PASSWORD_LENGTH);
    }

    /**
     * Handles when the create user button is pressed
     * @param view The view the event came from
     */
    public void onCreateUser(View view) {
        final EditText usernameField = findViewById(R.id.loginUsername);
        final EditText passwordField = findViewById(R.id.loginPassword);

        // Only attempt a login if the input is valid
        if (userService.areFieldsValid(usernameField, passwordField)) {
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
                                result.putExtra(PASSWORD, passwordField.getText().toString());
                                result.putExtra(REMEMBER, getRememberCheckBox().isChecked());
                                setResult(Activity.RESULT_OK, result);
                                finish();
                            }
                        });
                    } else {
                        // Login failed, show corresponding error
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (result != null && result.getMessage().equals(Cloud.NAME_TAKEN_ERROR)) {
                                    usernameField.setError(getResources().getString(R.string.taken_username_error));
                                }
                            }
                        });
                    }
                }
            }).start();
        }
    }

    /**
     * Get the remember check box.
     * @return The remember check box
     */
    private CheckBox getRememberCheckBox() {
        return findViewById(R.id.createRemeberCheckbox);
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