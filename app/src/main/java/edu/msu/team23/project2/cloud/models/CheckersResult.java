package edu.msu.team23.project2.cloud.models;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * Retrofit class representing the result of a call from the checkers game.
 */
@Root(name = "checkers")
public class CheckersResult {
    /**
     * Status of the result.
     */
    @Attribute
    private String status;

    /**
     * Message of the result.
     */
    @Attribute(name = "msg", required = false)
    private String message;

    /**
     * Get the status of the result.
     * @return The status of the result
     */
    public String getStatus() {
        return status;
    }

    /**
     * Set the status of the result.
     * @param status Status to set the result to
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Get the message of the result.
     * @return The message of the result
     */
    public String getMessage() {
        return message;
    }

    /**
     * Set the message of the result.
     * @param message the message of the result
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Empty Constructor.
     */
    public CheckersResult() {}

    /**
     * Constructor.
     * @param status Status of the result
     * @param msg Message of the result
     */
    public CheckersResult(String status, String msg) {
        this.status = status;
        this.message = msg;
    }
}
