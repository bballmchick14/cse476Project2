package edu.msu.team23.project1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.text.MessageFormat;

/**
 * Class for handling the state of the checkers game.
 */
public class CheckersGame implements Serializable {
    /**
     * Enumeration for the teams in the game.
     */
    enum Team implements Serializable {
        WHITE,
        GREEN
    }

    /**
     * Number of spaces on a side of the board.
     */
    private static final int SPACES_ON_SIDE = 8;

    /**
     * Relative width of a space.
     */
    private static final float SPACE_WIDTH = 1f / SPACES_ON_SIDE;

    /**
     * View this checkers game is in.
     */
    private transient CheckersView view;

    /**
     * Name of the player whose turn it is.
     */
    private Team teamTurn;

    /**
     * Name of the player on the green team.
     */
    private final String greenPlayer;

    /**
     * Name of the player on the white team.
     */
    private final String whitePlayer;

    /**
     * Representation of the game board.
     * Each position has a CheckersPiece object if there is a piece present
     * and null if the space is empty.
     */
    private CheckersPiece[][] board;

    /**
     * Has the current turn had a move of one space?
     */
    private boolean hasSingleMoved;

    /**
     * Is the game complete?
     */
    private boolean isComplete;

    /**
     * Space of the piece that has moved.
     * If no piece has moved this turn, the variable is null.
     */
    private Space hasMovedSpace;

    /**
     * Piece that has moved this turn.
     * If no piece has moved this turn, the variable is null.
     */
    private transient CheckersPiece hasMovedPiece;

    /**
     * The checkers piece being dragged. If we are not dragging, the variable is null.
     */
    private CheckersPiece draggingPiece = null;

    /**
     * The space the dragging piece came from.
     * If there is no dragging piece, the variable is null.
     */
    private Space draggingSpace = null;

    /**
     * Most recent relative X touch when dragging.
     */
    private transient float lastRelX;

    /**
     * Most recent relative Y touch when dragging.
     */
    private transient float lastRelY;

    /**
     * Image of the checker board.
     */
    private transient Bitmap boardImage;

    /**
     * Side length of the board in pixels.
     */
    private transient int boardSize;

    /**
     * Constructor foe the CheckersGame class.
     * @param context Application context
     * @param view View this game is a part of
     * @param greenPlayer Name of the player on the green team
     * @param whitePlayer Name of the player on the white team
     */
    public CheckersGame(Context context, CheckersView view, String greenPlayer, String whitePlayer) {
        // Instantiate member variables
        boardImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.board);
        this.greenPlayer = greenPlayer;
        this.whitePlayer = whitePlayer;
        this.view = view;

        // Reset the game to the initial state
        reset(context);
    }

    /**
     * Draws the checkers game.
     * @param canvas Canvas to draw on
     */
    public void draw(Canvas canvas) {
        // Determine the size of the board
        boardSize = canvas.getWidth();

        // Determine where to draw the board and how much to scale it
        float boardScaleFactor = (float)boardSize / (float) boardImage.getWidth();

        // Draw the board
        canvas.save();
        canvas.scale(boardScaleFactor, boardScaleFactor);
        canvas.drawBitmap(boardImage, 0, 0, null);
        canvas.restore();

        // Draw the pieces
        for (int col = 0; col < board.length; col++) {
            for (int row = 0; row < board[col].length; row++) {
                if (board[row][col] != null) {
                    board[row][col].draw(canvas, boardSize);
                }
            }
        }

        // Draw the dragging piece again so it will appear on top
        if (draggingPiece != null) {
            draggingPiece.draw(canvas, boardSize);
        }
    }

    /**
     * Handle a touch event from the view.
     * @param view The view that is the source of the touch
     * @param event The motion event describing the touch
     * @return true if the touch is handled
     */
    public boolean onTouchEvent(View view, MotionEvent event) {
        // Convert an x,y location to a relative location on the board
        float relX = event.getX() / boardSize;
        float relY = event.getY() / boardSize;

        // Delegate work to corresponding function
        switch(event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                return onTouched(relX, relY);
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                return onReleased(view, relX, relY);
            case MotionEvent.ACTION_MOVE:
                return onMove(view, relX, relY);
        }
        return false;
    }

    /**
     * Handle a touch message.
     * @param x X location for the touch, relative to the game - 0 to 1 over the game
     * @param y Y location for the touch, relative to the game - 0 to 1 over the game
     * @return true if the touch is handled
     */
    private boolean onTouched(float x, float y) {
        // If the game is still in progress, search for a piece in that was hit by the touch.
        // If a piece got hit, grab it.
        if (!isComplete) {
            for (int row = 0; row < board.length; row++) {
                for (int col = 0; col < board[row].length; col++) {
                    if (board[row][col] != null && board[row][col].getTeam() == teamTurn && board[row][col].hit(x, y, boardSize)) {
                        draggingPiece = board[row][col];
                        draggingSpace = new Space(row, col);
                        lastRelX = x;
                        lastRelY = y;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Handle a release of a touch message.
     * @param view The view that is the source of the touch
     * @param x x location for the touch release, relative to the game - 0 to 1 over the game
     * @param y y location for the touch release, relative to the game - 0 to 1 over the game
     * @return true if the touch is handled
     */
    private boolean onReleased(View view, float x, float y) {
        if (draggingPiece != null) {
            // Find the closest space to the release point to determine where the piece might snap
            // to if the move is valid
            Space closestSpace = closestSpace(x, y);

            // Do the move if it is valid
            if (isValidMove(draggingSpace, closestSpace, draggingPiece)) {
                // Move the piece to the new space
                board[closestSpace.getRow()][closestSpace.getCol()] = draggingPiece;
                board[draggingSpace.getRow()][draggingSpace.getCol()] = null;

                // Move the piece to the correct relative position
                PointF snapPos = spaceToPos(closestSpace.getRow(), closestSpace.getCol());
                draggingPiece.setPosition(snapPos.x, snapPos.y);

                // If move is only one space, save state of hasSingleMoved
                if (Math.abs(closestSpace.getRow() - draggingSpace.getRow()) == 1) {
                    hasSingleMoved = true;
                }

                // If a piece was jumped, remove the jumped piece
                if (Math.abs(closestSpace.getRow() - draggingSpace.getRow()) == 2) {
                    board[(closestSpace.getRow() + draggingSpace.getRow()) / 2][(closestSpace.getCol() + draggingSpace.getCol()) / 2] = null;
                }

                // If the piece got to the end make it a king
                if (draggingPiece.getTeam() == Team.GREEN && closestSpace.getRow() == 0
                    || draggingPiece.getTeam() == Team.WHITE && closestSpace.getRow() == SPACES_ON_SIDE - 1) {
                    draggingPiece.makeKing(view.getContext());
                }

                // Store info about the piece that moved for future move validation
                hasMovedPiece = draggingPiece;
                hasMovedSpace = closestSpace;
            } else {
                // Reset the piece's position to it's original space
                returnPiece();

                // Present a toast for an invalid move
                Toast.makeText(view.getContext(), R.string.invalid_move, Toast.LENGTH_SHORT).show();
            }
            // Let go of the piece
            releasePiece();

            // End the game if a team won
            handleWin(hasWon());

            view.invalidate();
            return true;
        }
        return false;
    }

    /**
     * Handle a move message.
     * @param view The view that is the source of the touch
     * @param relX Most recent relative X touch when dragging
     * @param relY Most recent relative Y touch when dragging
     * @return true if the touch is handled
     */
    private boolean onMove(View view, float relX, float relY) {
        if(draggingPiece != null) {
            //
            // Gather values for below calculations
            //
            // Image of the piece
            Bitmap image = draggingPiece.getImage();
            // Scale factor of the piece
            float scaleFactor = (float)boardSize / (float)image.getWidth() / 8;
            // Size of half of the piece in pixels
            int halfPieceSize = (int)(draggingPiece.getImage().getWidth() * scaleFactor / 2);
            // Relative position of the piece
            PointF pieceRelPos = draggingPiece.getRelPos();
            // Proposed new relative X position from the move
            int pendingNewX = (int)((pieceRelPos.x + (relX - lastRelX)) * boardSize);
            // Proposed new relative Y position from the move
            int pendingNewY = (int)((pieceRelPos.y + (relY - lastRelY)) * boardSize);

            float dx; // Relative distance to move in the X
            float dy; // Relative distance to move in the Y

            //
            // Determine if the proposed move will move the piece off the board in X and Y directions.
            // If it will go off the board, don't let it. Otherwise move as normal.
            //
            if (pendingNewX - halfPieceSize < 0) {
                dx = 0;
                lastRelX = (0f + halfPieceSize) / boardSize;
                draggingPiece.setPosition(lastRelX, pieceRelPos.y);
            } else if (pendingNewX + halfPieceSize > boardSize) {
                dx = 0;
                lastRelX = (float)(boardSize - halfPieceSize) / boardSize;
                draggingPiece.setPosition(lastRelX, pieceRelPos.y);
            } else {
                dx = relX - lastRelX;
            }

            if (pendingNewY - halfPieceSize < 0) {
                dy = 0;
                lastRelY = (0f + halfPieceSize) / boardSize;
                draggingPiece.setPosition(pieceRelPos.x, lastRelY);
            } else if (pendingNewY + halfPieceSize > boardSize) {
                dy = 0;
                lastRelY = (float)(boardSize - halfPieceSize) / boardSize;
                draggingPiece.setPosition(pieceRelPos.x, lastRelY);
            } else {
                dy = relY - lastRelY;
            }

            draggingPiece.move(dx, dy);
            lastRelX = dx == 0 ? lastRelX : relX;
            lastRelY = dy == 0 ? lastRelY : relY;
            view.invalidate();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Return the currently dragging piece to it's space before dragging.
     */
    public void returnPiece() {
        if (draggingPiece != null) {
            PointF originalPos = spaceToPos(draggingSpace.getRow(), draggingSpace.getCol());
            draggingPiece.setPosition(originalPos.x, originalPos.y);
        }
    }

    /**
     * Let go of the piece.
     */
    public void releasePiece() {
        draggingPiece = null;
        draggingSpace = null;
    }

    /**
     * Determines if a player has won the game.
     * @return Team that won, null otherwise
     */
    public Team hasWon() {
        // Determine if each team has pieces still exist
        boolean hasGreenPiece = false;
        boolean hasWhitePiece = false;
        for (CheckersPiece[] row : board) {
            for (CheckersPiece piece : row) {
                if (piece != null) {
                    switch (piece.getTeam()) {
                        case GREEN:
                            hasGreenPiece = true;
                            break;
                        case WHITE:
                            hasWhitePiece = true;
                            break;
                    }
                }
            }
        }

        // Designate the winning team
        Team winningTeam = null;
        if (!hasWhitePiece) {
            winningTeam = Team.GREEN;
        } else if (!hasGreenPiece) {
            winningTeam = Team.WHITE;
        }
        return winningTeam;
    }

    /**
     * Handle a team winning the game.
     * @param winner The team that won the game
     */
    public void handleWin(Team winner) {
        if (winner != null) {
            isComplete = true;
            returnPiece();
            releasePiece();

            Intent intent = new Intent((Activity)view.getContext(), GameOverActivity.class);
            intent.putExtra(CheckersActivity.WINNER, winner == Team.GREEN ? greenPlayer : whitePlayer);
            intent.putExtra(CheckersActivity.LOSER, winner == Team.GREEN ? whitePlayer : greenPlayer);
            ((Activity)view.getContext()).startActivity(intent);
        }
    }

    /**
     * Handle a player resigning the game.
     */
    public void handleResign() {
        if (!isComplete) {
            handleWin(teamTurn == Team.GREEN ? Team.WHITE : Team.GREEN);
        }
    }

    /**
     * Handles advancing to the next player's turn.
     */
    public void nextTurn() {
        if (!isComplete) {
            if (hasMovedPiece != null) {
                hasSingleMoved = false;
                teamTurn = teamTurn == Team.GREEN ? Team.WHITE : Team.GREEN;
                setTurnView(teamTurn);
                hasMovedPiece = null;
                hasMovedSpace = null;
            } else {
                Toast.makeText(view.getContext(), R.string.must_move_message, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Determine which space a piece is closest to.
     * This function does not test valid moves, only which space to attempt to move to based on its
     * position.
     * @param x X position of the piece
     * @param y Y position of the piece
     * @return The space the piece is closest to
     */
    private static Space closestSpace(float x, float y) {
        return new Space(
                Math.round((y - (SPACE_WIDTH / 2)) / SPACE_WIDTH),
                Math.round((x - (SPACE_WIDTH / 2)) / SPACE_WIDTH)
        );
    }

    /**
     * Generate a relative position on the screen based on the space the piece is in
     * @param row Row of the space
     * @param col Column of the space
     * @return relative position of the piece
     */
    private PointF spaceToPos(int row, int col) {
        return new PointF((SPACE_WIDTH / 2f) + (SPACE_WIDTH * col), (SPACE_WIDTH / 2f) + (SPACE_WIDTH * row));
    }

    /**
     * Determine if a move from one space to another is valid.
     * @param beginSpace Space the piece will be moving from
     * @param endSpace Space the piece will be moving to
     * @param piece The piece doing the move
     * @return True if the move is valid
     */
    private boolean isValidMove(Space beginSpace, Space endSpace, CheckersPiece  piece) {
        int rise = endSpace.getRow() - beginSpace.getRow();
        int run = endSpace.getCol() - beginSpace.getCol();
        float slope = Math.abs((float)rise / run);
        Space midSpace = new Space((endSpace.getRow() + beginSpace.getRow()) / 2, (endSpace.getCol() + beginSpace.getCol()) / 2);
        Team enemyTeam =  piece.getTeam() == Team.GREEN ? Team.WHITE : Team.GREEN;

        return (
                // Game must be in progress
                !isComplete
                // End space must be on the board
                && endSpace.getRow() >= 0 && endSpace.getRow() < SPACES_ON_SIDE && endSpace.getCol() >= 0 && endSpace.getCol() < SPACES_ON_SIDE
                // End space must be open
                && board[endSpace.getRow()][endSpace.getCol()] == null
                // Slope must be one
                && Math.abs(slope - 1) < 0.00000001
                // Validate vertical direction
                && (piece.isKing() || (piece.getTeam() == Team.GREEN && rise < 0) || (piece.getTeam() == Team.WHITE && rise > 0))
                // can either move one space or 2 when jumping an apposing piece
                && ((Math.abs(rise) == 1 && hasMovedPiece == null) || (Math.abs(rise) == 2 && board[midSpace.getRow()][midSpace.getCol()] != null && board[midSpace.getRow()][midSpace.getCol()].getTeam() == enemyTeam) && (hasMovedPiece == null || (hasMovedPiece == piece && !hasSingleMoved)))
        );
    }

    /**
     * Reset the game to its initial state.
     * @param context Context of the application
     */
    public void reset(Context context) {
        // Set the turn
        teamTurn = Team.GREEN;
        isComplete = false;

        // Set the spaces of all the pieces
        board = new CheckersPiece[][]{
                {null, new CheckersPiece(context, Team.WHITE), null, new CheckersPiece(context, Team.WHITE), null, new CheckersPiece(context, Team.WHITE), null, new CheckersPiece(context, Team.WHITE)},
                {new CheckersPiece(context, Team.WHITE), null, new CheckersPiece(context, Team.WHITE), null, new CheckersPiece(context, Team.WHITE), null, new CheckersPiece(context, Team.WHITE), null},
                {null, new CheckersPiece(context, Team.WHITE), null, new CheckersPiece(context, Team.WHITE), null, new CheckersPiece(context, Team.WHITE), null, new CheckersPiece(context, Team.WHITE)},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {new CheckersPiece(context, Team.GREEN), null, new CheckersPiece(context, Team.GREEN), null, new CheckersPiece(context, Team.GREEN), null, new CheckersPiece(context, Team.GREEN), null},
                {null, new CheckersPiece(context, Team.GREEN), null, new CheckersPiece(context, Team.GREEN), null, new CheckersPiece(context, Team.GREEN), null, new CheckersPiece(context, Team.GREEN)},
                {new CheckersPiece(context, Team.GREEN), null, new CheckersPiece(context, Team.GREEN), null, new CheckersPiece(context, Team.GREEN), null, new CheckersPiece(context, Team.GREEN), null},
        };

        // Set the actual piece positions on the screen
        setPiecePositions();
    }

    /**
     * Update the relative position of all the pieces based on their space.
     */
    private void setPiecePositions() {
        for (int col = 0; col < board.length; col ++) {
            for (int row = 0; row < board[col].length; row++) {
                if (board[row][col] != null) {
                    PointF pos = spaceToPos(row, col);
                    board[row][col].setPosition(pos.x, pos.y);
                }
            }
        }
    }

    /**
     * Setup for views outside this view.
     */
    public void externalSetup() {
        setTurnView(Team.GREEN);
    }

    /**
     * Change the display to say who's turn it is.
     */
    private void setTurnView(Team team) {
        TextView turnView = (TextView)((Activity) view.getContext()).findViewById(R.id.turnView);
        turnView.setText(MessageFormat.format("{0}{1}", team == Team.GREEN ? greenPlayer : whitePlayer, view.getContext().getResources().getString(R.string.turn_suffix)));
    }

    /**
     * Restore the transient data after serialization.
     */
    public void restoreTransientData(CheckersView view) {
        if (view != null) {
            this.view = view;
            boardImage = BitmapFactory.decodeResource(view.getContext().getResources(), R.drawable.board);

            // Restore all the transient data in each piece
            for (int col = 0; col < board.length; col ++) {
                for (int row = 0; row < board[col].length; row++) {
                    if (board[row][col] != null) {
                        board[row][col].restoreTransientData(view.getContext());
                    }
                }
            }
        }

        if (hasMovedSpace != null) {
            hasMovedPiece = board[hasMovedSpace.getRow()][hasMovedSpace.getCol()];
        }
    }
}
