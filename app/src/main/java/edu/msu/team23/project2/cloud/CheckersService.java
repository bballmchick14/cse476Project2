package edu.msu.team23.project2.cloud;

import edu.msu.team23.project2.cloud.models.CheckersResult;
import edu.msu.team23.project2.cloud.models.ConnectResult;
import edu.msu.team23.project2.cloud.models.LoadResult;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

import static edu.msu.team23.project2.cloud.Cloud.SAVE_STATE_PATH;
import static edu.msu.team23.project2.cloud.Cloud.LOAD_STATE_PATH;
import static edu.msu.team23.project2.cloud.Cloud.LOGIN_USER_PATH;
import static edu.msu.team23.project2.cloud.Cloud.CREATE_USER_PATH;
import static edu.msu.team23.project2.cloud.Cloud.CONNECT_PATH;
import static edu.msu.team23.project2.cloud.Cloud.DISCONNECT_PATH;

/**
 * Retrofit service class for talking to the checkers server.
 */
public interface CheckersService {
    /**
     * Save the client's game state to the server.
     * @param xmlData Data from the client's game state
     * @return Result from the server
     */
    @FormUrlEncoded
    @POST(SAVE_STATE_PATH)
    Call<CheckersResult> saveState(@Field("xml") String xmlData);

    /**
     * Load the game state from the server.
     * @param username User's name
     * @param magic Server's magic string
     * @param password User's password
     * @return Result from the server
     */
    @GET(LOAD_STATE_PATH)
    Call<LoadResult> loadState(
            @Query("user") String username,
            @Query("magic") String magic,
            @Query("pw") String password
    );

    /**
     * Login a user.
     * @param username User's name
     * @param magic Server's magic string
     * @param password User's password
     * @return Result from the server
     */
    @GET(LOGIN_USER_PATH)
    Call<CheckersResult> loginUser(
            @Query("user") String username,
            @Query("magic") String magic,
            @Query("pw") String password
    );

    /**
     * Create a new user.
     * @param username User's name
     * @param magic Server's magic string
     * @param password User's password
     * @return Result from the server
     */
    @GET(CREATE_USER_PATH)
    Call<CheckersResult> createUser(
            @Query("user") String username,
            @Query("magic") String magic,
            @Query("pw") String password
    );

    /**
     * Attempt to connect the user to a game.
     * @param username User's name
     * @param magic Server's magic string
     * @param password User's password
     * @return Result from the server
     */
    @GET(CONNECT_PATH)
    Call<ConnectResult> connect(
            @Query("user") String username,
            @Query("magic") String magic,
            @Query("pw") String password
    );

    /**
     * Disconnect the user from their game.
     * @param username User's name
     * @param magic Server's magic string
     * @param password User's password
     * @return Result from the server
     */
    @GET(DISCONNECT_PATH)
    Call<CheckersResult> disconnect(
            @Query("user") String username,
            @Query("magic") String magic,
            @Query("pw") String password
    );
}
