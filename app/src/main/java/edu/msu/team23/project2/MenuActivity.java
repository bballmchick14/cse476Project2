package edu.msu.team23.project2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity for displaying the main menu.
 */
public class MenuActivity extends AppCompatActivity {
    /**
     * Logged in user's username.
     */
    private String username;

    /**
     * Logged in user's password.
     */
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Intent intent = getIntent();
        username = intent.getStringExtra(LoginActivity.USERNAME);
        password = intent.getStringExtra(LoginActivity.PASSWORD);
        getLoggedInUser().setText(String.format("  %s", username));
    }

    /**
     * Handler for starting the checkers game activity.
     * @param view View the event happened on
     */
    public void onStartGame(View view) {
        //Functionality needs to be changed for multi-player
        Toast.makeText(view.getContext(), R.string.feature_not_implemented, Toast.LENGTH_SHORT).show();
    }

    /**
     * Handler for displaying rules.
     * @param view View the event happened on
     */
    public void onRules(View view) {
        // Rules was selected
        // Instantiate a dialog box builder
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

        // Parameterize the builder
        builder.setTitle(R.string.rules_title);
        builder.setMessage(R.string.rules);
        builder.setPositiveButton(android.R.string.ok, null);

        // Create the dialog box and show it
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Get the field for the logged in user.
     * @return The field for the logged in user
     */
    public TextView getLoggedInUser() {
        return findViewById(R.id.loggedInUser);
    }
}