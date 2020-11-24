package edu.msu.team23.project2.cloud;

import java.io.IOException;

import edu.msu.team23.project2.CheckersGame;
import edu.msu.team23.project2.cloud.models.CheckersResult;
import edu.msu.team23.project2.cloud.models.ConnectResult;
import edu.msu.team23.project2.cloud.models.LoadResult;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * Class for making API calls.
 */
@SuppressWarnings("deprecation")
public class Cloud {
    /**
     * Magic string sequence for this application.
     */
    private static final String MAGIC = "7VpFwBLcKne5p0Uu";

    /**
     * Base url for this application's server-side API.
     */
    private static final String BASE_URL = "https://webdev.cse.msu.edu/~szczerb7/cse476/project2/";

    /**
     * Path to the page for saving the state of the checkers game to the database.
     */
    public static final String SAVE_STATE_PATH = "save-state.php";

    /**
     * Path to the page for loading the state of the checkers game to the database.
     */
    public static final String LOAD_STATE_PATH = "load-state.php";

    /**
     * Path to the page for logging into the application.
     */
    public static final String LOGIN_USER_PATH = "login-user.php";

    /**
     * Path to the page for creating a user in the database.
     */
    public static final String CREATE_USER_PATH = "create-user.php";

    /**
     * Path to the page for connecting to a game.
     */
    public static final String CONNECT_PATH = "connect.php";

    /**
     * Path to the page for disconnecting from a game.
     */
    public static final String DISCONNECT_PATH = "disconnect.php";

    /**
     * Result status value returned by the database on a successful call.
     */
    public static final String STATUS_SUCCESS = "success";

    /**
     * Result message value returned by the database when there was an error with a password.
     */
    public static final String PASSWORD_ERROR = "password error";

    /**
     * Result message value returned by the database when a username has already been taken.
     */
    public static final String NAME_TAKEN_ERROR = "name taken";

    /**
     * Retrofit object to create services for API calls.
     */
    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .build();

    /**
     * Save the current state of the game to the server's database.
     * @param username Username of the client
     * @param password Password for the logged in user
     * @return Result of the save state call
     */
    public CheckersResult saveState(String username, String password, CheckersGame game) {
        return new CheckersResult();
    }

    /**
     * Attempt to load the state of the game the user is in.
     * @param username Username of the client
     * @param password Password for the logged in user
     * @return Result of the load state call
     */
    public LoadResult loadState(String username, String password) {
        return new LoadResult();
    }

    /**
     * Login to a pre-existing user in the server database.
     * @param username Username of the client
     * @param password Password for the logged in user
     * @return Result of the login user call
     */
    public CheckersResult loginUser(String username, String password) {
        // Create a service for the API call
        CheckersService service = retrofit.create(CheckersService.class);
        try {
            // Attempt to log in on the server
            Response<CheckersResult> response = service.loginUser(username, MAGIC, password).execute();

            // If the server could not be reached, return null
            if (!response.isSuccessful()) {
                return null;
            }

            // Return the result
            return response.body();
        } catch(IOException | RuntimeException e) {
            // There was another problem with processing the data from the database, return null
            return null;
        }
    }

    /**
     * Create a new user in the server's database.
     * @param username Username of the client
     * @param password Password for the logged in user
     * @return Result of the create user call
     */
    public CheckersResult createUser(String username, String password) {
        // Create a service for the API call
        CheckersService service = retrofit.create(CheckersService.class);
        try {
            // Attempt to create a user in the database
            Response<CheckersResult> response = service.createUser(username, MAGIC, password).execute();

            // If the server could not be reached, return null
            if (!response.isSuccessful()) {
                return null;
            }

            // Return the result
            return response.body();
        } catch(IOException | RuntimeException e) {
            return null;
        }
    }

    /**
     * Attempt to connect to a new game.
     * @param username Username of the client
     * @param password Password for the logged in user
     * @return Result of the connect call
     */
    public ConnectResult connect(String username, String password) {
        return new ConnectResult();
    }

    /**
     * Disconnect from the game the client is in.
     * @param username Username of the client
     * @param password Password for the logged in user
     * @return Result of the disconnect call
     */
    public CheckersResult disconnect(String username, String password) {
        return new CheckersResult();
    }
}
