package edu.msu.team23.project1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

/**
 * Activity for displaying the main menu.
 */
public class MainActivity extends AppCompatActivity {
    /**
     * Intent extra key for the green player's name.
     */
    public static final String GREEN_PLAYER = "MainActivity.greenPlayer";

    /**
     * Intent extra key for the white player's name.
     */
    public static final String WHITE_PLAYER = "MainActivity.whitePlayer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Handler for starting the checkers game activity.
     * @param view View the event happened on
     */
    public void onStartGame(View view) {
        EditText greenPlayer = findViewById(R.id.greenPlayerNameField);
        EditText whitePlayer = findViewById(R.id.whitePlayerNameField);

        if (areFieldsValid(greenPlayer, whitePlayer)) {
            Intent intent = new Intent(this, CheckersActivity.class);
            intent.putExtra(GREEN_PLAYER, greenPlayer.getText().toString());
            intent.putExtra(WHITE_PLAYER, whitePlayer.getText().toString());
            startActivity(intent);
        }
    }

    /**
     * Determine if the entries in the fields on the main activity are valid.
     * @param greenPlayer The edit text view where the green player's name is entered
     * @param whitePlayer The edit text view where the white player's name is entered
     * @return True if the entries in the fields on the main activity are valid
     */
    private boolean areFieldsValid(EditText greenPlayer, EditText whitePlayer) {
        boolean validFields = true;

        if (greenPlayer != null && whitePlayer != null) {
            if (greenPlayer.getText().toString().length() == 0) {
                validFields = false;
                greenPlayer.setError(getResources().getString(R.string.missing_name_error));
            }
            if (whitePlayer.getText().toString().length() == 0) {
                validFields = false;
                whitePlayer.setError(getResources().getString(R.string.missing_name_error));
            }
            if (validFields && greenPlayer.getText().toString().equals(whitePlayer.getText().toString())) {
                whitePlayer.setError(getResources().getString(R.string.duplicate_name_error));
                validFields = false;
            }
        } else {
            validFields = false;
        }

        return validFields;
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