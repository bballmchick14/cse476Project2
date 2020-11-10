package edu.msu.team23.project1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

/**
 * Activity for displaying the play area and controls of a checkers game.
 */
public class CheckersActivity extends AppCompatActivity {
    /**
     * Bundle key for the winner's name.
     */
    public static final String WINNER = "CheckersActivity.winner";

    /**
     * Bundle key for the loser's name.
     */
    public static final String LOSER = "CheckersActivity.loser";

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_checkers);
        getCheckersView().externalSetup();

        if (bundle != null) {
            getCheckersView().loadInstanceState(bundle);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        getCheckersView().saveInstanceState(bundle);
    }

    /**
     * Get the checkers view.
     * @return CheckersView reference
     */
    private CheckersView getCheckersView() {
        return (CheckersView)findViewById(R.id.checkersView);
    }

    /**
     * Button handler for the done button.
     * @param view View the button was in
     */
    public void onDone(View view) {
        getCheckersView().onDone();
    }

    /**
     * Button handler for the resign button.
     * @param view View the button was in
     */
    public void onResign(View view) {
        getCheckersView().onResign();
    }
}