package edu.msu.team23.project1;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Custom view class for the checkers game.
 */
public class CheckersView extends View {
    /**
     * Bundle key for storing the game.
     */
    private static final String CHECKERS_GAME = "CheckersView.checkersGame";

    /**
     * Checkers game for this view.
     */
    private CheckersGame checkersGame;

    /**
     * Constructor.
     * @param context Context of this application
     */
    public CheckersView(Context context) {
        super(context);
        init(context);
    }

    /**
     * Constructor.
     * @param context Context of this application
     * @param attrs Attributes
     */
    public CheckersView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * Constructor.
     * @param context Context of this application
     * @param attrs Attributes
     * @param defStyle Style
     */
    public CheckersView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    /**
     * Initialize the view.
     * @param context Context of this application
     */
    private void init(Context context) {
        Intent intent = ((Activity)context).getIntent();
        checkersGame = new CheckersGame(getContext(), this, intent.getStringExtra(MainActivity.GREEN_PLAYER), intent.getStringExtra(MainActivity.WHITE_PLAYER));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        checkersGame.draw(canvas);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return checkersGame.onTouchEvent(this, event);
    }

    /**
     * Save the game to a bundle.
     * @param bundle The bundle we save to
     */
    public void saveInstanceState(Bundle bundle) {
        // Release and return the piece in case it was being dragged
        checkersGame.returnPiece();
        checkersGame.releasePiece();

        // Save the game to the bundle
        bundle.putSerializable(CHECKERS_GAME, checkersGame);
    }

    /**
     * Load the game from a bundle.
     * @param bundle The bundle we save to
     */
    public void loadInstanceState(Bundle bundle) {
        checkersGame = (CheckersGame)bundle.getSerializable(CHECKERS_GAME);
        if (checkersGame != null) {
            checkersGame.restoreTransientData(this);
        }
    }

    /**
     * Perform setup for views that need to communicate with each other.
     */
    public void externalSetup() {
        checkersGame.externalSetup();
    }

    /**
     * Handler for the done button.
     */
    public void onDone() {
        checkersGame.nextTurn();
    }

    /**
     * Handler for the resign button.
     */
    public void onResign() {
        checkersGame.handleResign();
    }

    /**
     * Get the checkers game associated with this view.
     * @return The checkers game associated with this view
     */
    public CheckersGame getCheckersGame() {
        return checkersGame;
    }
}
