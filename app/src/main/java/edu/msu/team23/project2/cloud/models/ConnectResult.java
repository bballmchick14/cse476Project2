package edu.msu.team23.project2.cloud.models;

import org.simpleframework.xml.Attribute;

/**
 * Retrofit class representing the result of a connect call.
 */
public class ConnectResult extends CheckersResult {
    /**
     * Team the user is on when a game is successfully connected to.
     */
    @Attribute(required = false)
    private String team;

    /**
     * Name of the opponent in a game that was successfully connected to.
     */
    @Attribute(required = false)
    private String opponentName;

    /**
     * Get the team the user is on when a game is successfully connected to.
     * @return The team the user is on when a game is successfully connected to
     */
    public String getTeam() {
        return team;
    }

    /**
     * Set the team the user is on when a game is successfully connected to.
     * @param team The team the user is on when a game is successfully connected to
     */
    public void setTeam(String team) {
        this.team = team;
    }

    /**
     * Get the name of the opponent in a game that was successfully connected to.
     * @return The name of the opponent in a game that was successfully connected to
     */
    public String getOpponentName() {
        return opponentName;
    }

    /**
     * Set the name of the opponent in a game that was successfully connected to.
     * @param opponentName The name of the opponent in a game that was successfully connected to
     */
    public void setOpponentName(String opponentName) {
        this.opponentName = opponentName;
    }

    /**
     * Empty constructor.
     */
    public ConnectResult() {}

    /**
     * Constructor.
     * @param status Status of the result
     * @param msg Message of the result
     * @param team Team the user is on when a game is successfully connected to
     * @param opponentName Name of the opponent in a game that was successfully connected to
     */
    public ConnectResult(String status, String msg, String team, String opponentName) {
        super(status, msg);
        this.team = team;
        this.opponentName = opponentName;
    }
}
