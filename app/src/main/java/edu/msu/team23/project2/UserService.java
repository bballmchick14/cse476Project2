package edu.msu.team23.project2;

import android.app.Activity;
import android.content.SharedPreferences;
import android.widget.EditText;

public class UserService {
    private static final String PREFERENCES_FILE_NAME = "preferences";
    private static final String REMEMBER_KEY = "remember";
    private static final String REMEMBER_SET = "true";
    private static final String REMEMBER_CLEAR = "false";
    private static final String REMEMBERED_USERNAME_KEY = "rememberedUsername";
    private static final String REMEMBERED_PASSWORD_KEY = "rememberedPassword";
    private static final String REMEMBERED_EMPTY = "";

    private final SharedPreferences preferences;
    private int minUsernameLength;
    private int maxUsernameLength;
    private int minPasswordLength;
    private int maxPasswordLength;
    private final String invalidUsernameLengthError;
    private final String invalidPasswordLengthError;


    /**
     * Constructor.
     * @param activity Activity the user service is being used in
     */
    public UserService(Activity activity, int minUsernameLength, int maxUsernameLength, int minPasswordLength, int maxPasswordLength) {
        preferences = activity.getSharedPreferences(PREFERENCES_FILE_NAME, Activity.MODE_PRIVATE);
        this.minUsernameLength = minUsernameLength;
        this.maxUsernameLength = maxUsernameLength;
        this.minPasswordLength = minPasswordLength;
        this.maxPasswordLength = maxPasswordLength;
        invalidUsernameLengthError = activity.getResources().getString(R.string.invalid_username_length_error);
        invalidPasswordLengthError = activity.getResources().getString(R.string.invalid_password_length_error);
    }

    /**
     * Are the username and password fields in the correct format?
     * @return Are the username and password fields in the correct format?
     */
    public boolean areFieldsValid(EditText usernameField, EditText passwordField) {
        boolean areFieldsValid = true;

        if (usernameField != null && passwordField != null) {
            String username = usernameField.getText().toString();
            String password = passwordField.getText().toString();

            if (password.length() < minPasswordLength || password.length() > maxPasswordLength) {
                areFieldsValid = false;
                passwordField.setError(String.format(invalidPasswordLengthError, minPasswordLength, maxPasswordLength));
            }
            if (username.length() < minUsernameLength || username.length() > maxUsernameLength) {
                areFieldsValid = false;
                usernameField.setError(String.format(invalidUsernameLengthError, minUsernameLength, maxUsernameLength));
            }
        } else {
            areFieldsValid = false;
        }

        return areFieldsValid;
    }

    /**
     * Store the values for remembering the user.
     * @param username The user's name
     * @param password The user's password
     */
    public void setRemember(String username, String password) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(REMEMBER_KEY, REMEMBER_SET);
        editor.putString(REMEMBERED_USERNAME_KEY, username);
        editor.putString(REMEMBERED_PASSWORD_KEY, password);
        editor.apply();
    }

    /**
     * Get of there is a remembered user.
     * @return True if there is a remembered user
     */
    public boolean isRemembered() {
        String isRememberedValue = preferences.getString(REMEMBER_KEY, "");
        return isRememberedValue != null && isRememberedValue.equals(REMEMBER_SET);
    }

    /**
     * Get the remembered username in preferences.
     * @return The remembered username in preferences
     */
    public String getRememberedUsername() {
        return preferences.getString(REMEMBERED_USERNAME_KEY, "");
    }

    /**
     * Get the remembered password in preferences.
     * @return The remembered password
     */
    public String getRememberedPassword() {
        return preferences.getString(REMEMBERED_PASSWORD_KEY, "");
    }

    /**
     * Clear the remembering of a user.
     */
    public void clearRemember() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(REMEMBER_KEY, REMEMBER_CLEAR);
        editor.putString(REMEMBERED_USERNAME_KEY, REMEMBERED_EMPTY);
        editor.putString(REMEMBERED_PASSWORD_KEY, REMEMBERED_EMPTY);
        editor.apply();
    }
}
