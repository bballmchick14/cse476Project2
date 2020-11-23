package edu.msu.team23.project2;

import android.content.Context;
import android.widget.EditText;

public class UserCredentialFormatService {
    private static final int MAX_USERNAME_LENGTH = 32;
    private static final int MAX_PASSWORD_LENGTH = 16;

    /**
     * Are the username and password fields in the correct format?
     * @return Are the username and password fields in the correct format?
     */
    public static boolean areFieldsValid(Context context, EditText usernameField, EditText passwordField) {
        boolean areFieldsValid = true;

        if (usernameField != null && passwordField != null) {
            String username = usernameField.getText().toString();
            String password = passwordField.getText().toString();

            if (password.length() < 1 || password.length() > MAX_PASSWORD_LENGTH) {
                areFieldsValid = false;
                passwordField.setError(context.getResources().getString(R.string.invalid_password_length_error));
            }
            if (username.length() < 1 || username.length() > MAX_USERNAME_LENGTH) {
                areFieldsValid = false;
                usernameField.setError(context.getResources().getString(R.string.invalid_username_length_error));
            }
        } else {
            areFieldsValid = false;
        }

        return areFieldsValid;
    }
}
