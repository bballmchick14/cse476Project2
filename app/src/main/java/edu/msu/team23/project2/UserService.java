package edu.msu.team23.project2;

import android.app.Activity;
import android.content.SharedPreferences;
import android.widget.EditText;

/**
 * Service class for handling and saving user credentials.
 */
public class UserService {
    /**
     * Name of the preferences file.
     */
    private static final String PREFERENCES_FILE_NAME = "preferences";

    /**
     * Preferences key for if there is a remembered user.
     */
    private static final String REMEMBER_KEY = "remember";

    /**
     * Preferences value for when the remember key is set.
     */
    private static final String REMEMBER_SET = "true";

    /**
     * Preferences value for when the remember key is not set.
     */
    private static final String REMEMBER_CLEAR = "false";

    /**
     * Preferences key for the remembered username.
     */
    private static final String REMEMBERED_USERNAME_KEY = "rememberedUsername";

    /**
     * Preferences key for the remembered password.
     */
    private static final String REMEMBERED_PASSWORD_KEY = "rememberedPassword";

    /**
     * Preferences value for when a username or password are empty.
     */
    private static final String REMEMBERED_EMPTY = "";

    /**
     * Object holding the applications preferences
     */
    private final SharedPreferences preferences;

    /**
     * Minimum length a username can be.
     */
    private final int  minUsernameLength;

    /**
     * Maximum length a username can be.
     */
    private final int maxUsernameLength;

    /**
     * Minimum length a password can be.
     */
    private final int minPasswordLength;

    /**
     * Maximum length a password can be.
     */
    private final int maxPasswordLength;

    /**
     * Formatted string for when the username is invalid.
     * 2 places for numbers:
     * 1 -> minimum username length
     * 2 -> maximum username length
     */
    private final String invalidUsernameLengthError;

    /**
     * Formatted string for when the password is invalid.
     * 2 places for numbers:
     * 1 -> minimum password length
     * 2 -> maximum password length
     */
    private final String invalidPasswordLengthError;

    /**
     * String for when the confirm password is not the same as the password.
     */
    private final String invalidConfirmPasswordError;


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
        invalidConfirmPasswordError = activity.getResources().getString(R.string.invalid_conform_password_error);
    }

    /**
     * Are the username and password fields in the correct format?
     * @return Are the username and password fields in the correct format?
     */
    public boolean areFieldsValid(EditText usernameField, EditText passwordField, EditText confirmPasswordField) {
        boolean areFieldsValid = true;

        if (usernameField != null && passwordField != null) {
            // Clear previous errors
            usernameField.setError(null);
            passwordField.setError(null);
            passwordField.setError(null);

            // Get the strings in the username and password fields
            String username = usernameField.getText().toString();
            String password = passwordField.getText().toString();
            String confirmPassword = confirmPasswordField != null ? confirmPasswordField.getText().toString() : null;

            // If the password is not in the valid range, the fields are not valid and an error is presented
            if (password.length() < minPasswordLength || password.length() > maxPasswordLength) {
                areFieldsValid = false;
                passwordField.setError(String.format(invalidPasswordLengthError, minPasswordLength, maxPasswordLength));
            }

            // If the username is not in the valid range, the fields are not valid and an error is presented
            if (username.length() < minUsernameLength || username.length() > maxUsernameLength) {
                areFieldsValid = false;
                usernameField.setError(String.format(invalidUsernameLengthError, minUsernameLength, maxUsernameLength));
            }

            // If the password confirmation is not the same as the password, the fields are not valid and an error is presented
            if (confirmPasswordField != null && !confirmPassword.equals(password)) {
                areFieldsValid = false;
                confirmPasswordField.setError(invalidConfirmPasswordError);
            }
        } else {
            // One of the fields cannot be found, the fields are not valid
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
