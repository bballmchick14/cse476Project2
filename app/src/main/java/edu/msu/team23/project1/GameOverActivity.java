package edu.msu.team23.project1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.text.MessageFormat;

/**
 * Activity for displaying the game over screen.
 */
public class GameOverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        Intent intent = getIntent();
        ((TextView)findViewById(R.id.winnerText)).setText(MessageFormat.format("{0} {1}", intent.getStringExtra(CheckersActivity.WINNER), getResources().getString(R.string.game_over_win_suffix)));
        ((TextView)findViewById(R.id.loserText)).setText(MessageFormat.format("{0} {1}", intent.getStringExtra(CheckersActivity.LOSER), getResources().getString(R.string.game_over_lose_suffix)));
    }

    /**
     * Send the application back to the main menu.
     */
    private void backToMenu() {
        Intent intent= new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    /**
     * Handler for the menu button
     * @param view View the event came from
     */
    public void onMenu(View view) {
        backToMenu();
    }

    @Override
    public void onBackPressed() {
        backToMenu();
    }
}