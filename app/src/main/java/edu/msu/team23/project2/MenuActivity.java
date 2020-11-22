package edu.msu.team23.project2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;

/**
 * Activity for displaying the main menu.
 */
public class MenuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    /**
     * Handler for starting the checkers game activity.
     * @param view View the event happened on
     */
    public void onStartGame(View view) {
        /*
         * Functionality needs to be changed for multi-player
         */
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
}