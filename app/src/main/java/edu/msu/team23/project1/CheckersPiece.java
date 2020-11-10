package edu.msu.team23.project1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PointF;

import java.io.Serializable;

/**
 * Class for representing a checkers piece in a checkers game.
 */
public class CheckersPiece implements Serializable {
    /**
     * Enumeration for all the different images that can be used to display a piece.
     */
    public enum PieceImage implements Serializable {
        WHITE_MAN(R.drawable.white_man),
        WHITE_KING(R.drawable.white_king),
        GREEN_MAN(R.drawable.green_man),
        GREEN_KING(R.drawable.green_king);

        /**
         * ID of the image associated with the type of piece.
         */
        public final int IMAGE_ID;

        /**
         * Constructor
         * @param imageId ID of the image associated with the type of piece.
         */
        PieceImage(int imageId) {
            IMAGE_ID = imageId;
        }
    }

    /**
     * Team the piece is on.
     */
    private final CheckersGame.Team team;

    /**
     * Is this piece a king?
     */
    private boolean isKing = false;

    /**
     * Key for the image this piece should have.
     */
    private PieceImage imageKey;

    /**
     * Image for displaying this piece.
     */
    private transient Bitmap image;

    /**
     * Relative x location of the piece.
     * Value from 0-1 representing percentage distance the center of the piece is horizontally on the board.
     */
    private float x;

    /**
     * Relative y location of the piece.
     * Value from 0-1 representing percentage distance the center of the piece is vertically on the board.
     */
    private float y;

    /**
     * Constructor for a checkers piece.
     * @param context Context of the application
     * @param team Team the piece is on
     */
    public CheckersPiece(Context context, CheckersGame.Team team) {
        this.team = team;
        updateImage(context);
    }

    /**
     * Draw the checkers piece.
     * @param canvas Canvas to draw on
     * @param boardSize size of the board
     */
    public void draw(Canvas canvas, int boardSize) {
        if (image != null) {
            // Determine scale factor
            float scaleFactor = (float)boardSize / (float)image.getWidth() / 8;

            // Draw image in the correct space
            canvas.save();
            canvas.translate(x * boardSize, y * boardSize);
            canvas.scale(scaleFactor, scaleFactor);
            canvas.translate(-image.getWidth() / 2f, -image.getHeight() / 2f);
            canvas.drawBitmap(image, 0, 0, null);
            canvas.restore();
        }
    }

    /**
     * Test to see if a relative location is on the piece.
     * @param testX X location as a normalized coordinate (0 to 1)
     * @param testY Y location as a normalized coordinate (0 to 1)
     * @param boardSize the size of the puzzle in pixels
     * @return true if we hit the piece
     */
    public boolean hit(float testX, float testY, int boardSize) {
        // Make relative to the location and size to the piece size
        float scaleFactor = (float)boardSize / (float)image.getWidth() / 8;
        int pX = (int)((testX - x) * boardSize / scaleFactor) + image.getWidth() / 2;
        int pY = (int)((testY - y) * boardSize / scaleFactor) + image.getHeight() / 2;

        if(pX < 0 || pX >= image.getWidth() || pY < 0 || pY >= image.getHeight()) {
            return false;
        }

        // We are within the rectangle of the piece.
        // Are we touching actual picture?
        return (image.getPixel(pX, pY) & 0xff000000) != 0;
    }

    /**
     * Move the checkers piece by dx, dy.
     * @param dx x amount to move
     * @param dy y amount to move
     */
    public void move(float dx, float dy) {
        x += dx;
        y += dy;
    }

    /**
     * Set the position of the piece.
     * @param x New x position of the piece
     * @param y New y position of the piece
     */
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Make this piece a king.
     * @param context Context of the application
     */
    public void makeKing(Context context) {
        isKing = true;
        updateImage(context);
    }

    /**
     * Update this pieces image based on its team and king status.
     * @param context context of this application
     */
    private void updateImage(Context context) {
        if (team == CheckersGame.Team.GREEN) {
            if (isKing) {
                imageKey = PieceImage.GREEN_KING;
            } else {
                imageKey = PieceImage.GREEN_MAN;
            }
        } else {
            if (isKing) {
                imageKey = PieceImage.WHITE_KING;
            } else {
                imageKey = PieceImage.WHITE_MAN;
            }
        }
        image = BitmapFactory.decodeResource(context.getResources(), imageKey.IMAGE_ID);
    }

    /**
     * Restore the transient data.
     * @param context Context of the application
     */
    public void restoreTransientData(Context context) {
        // Load the piece bitmap from the image key
        image = BitmapFactory.decodeResource(context.getResources(), imageKey.IMAGE_ID);
    }

    /**
     * Getter for the image of this piece.
     * @return The image of this piece
     */
    public Bitmap getImage() {
        return image;
    }

    /**
     * Getter for this piece's position.
     * @return This piece's position
     */
    public PointF getRelPos() {
        return new PointF(x, y);
    }

    /**
     * Getter for this piece's team.
     * @return This pieces team
     */
    public CheckersGame.Team getTeam() {
        return team;
    }

    /**
     * Getter for this piece's king status.
     * @return This piece's king status
     */
    public boolean isKing() {
        return isKing;
    }
}
