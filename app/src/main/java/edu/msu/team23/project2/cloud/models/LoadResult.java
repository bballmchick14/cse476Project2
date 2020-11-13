package edu.msu.team23.project2.cloud.models;

import org.simpleframework.xml.Attribute;

/**
 * Retrofit class representing the result of a load state call.
 */
public class LoadResult extends CheckersResult {
    /**
     * Serialized CheckersGame object to update the game state.
     * Only present if the game has not ended and it is the user's turn.
     */
    @Attribute(required = false)
    private String state;

    /**
     * The team that has won the game
     * Only present of the game has ended.
     */
    @Attribute(required = false)
    private String winningTeam;

    /**
     * Get the serialized state of the game.
     * @return The serialized state of the game
     */
    public String getState() {
        return state;
    }

    /**
     * Set the serialized state of the game.
     * @param state The serialized state of the game
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * Get the team that has won the game.
     * @return The team that has won the game
     */
    public String getWinningTeam() {
        return winningTeam;
    }

    /**
     * Set the team that has won the game.
     * @param winningTeam The team that has won the game
     */
    public void setWon(String winningTeam) {
        this.winningTeam = winningTeam;
    }

    /**
     * Empty constructor.
     */
    public LoadResult() {}

    /**
     * Constructor.
     * @param status Status of the result
     * @param msg Message of the result
     * @param state Serialized game state
     * @param winningTeam The win status of the user
     */
    public LoadResult(String status, String msg, String state, String winningTeam) {
        super(status, msg);
        this.state = state;
        this.winningTeam = winningTeam;
    }
}
