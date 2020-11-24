package edu.msu.team23.project2.cloud;

import java.io.IOException;

import edu.msu.team23.project2.CheckersGame;
import edu.msu.team23.project2.cloud.models.CheckersResult;
import edu.msu.team23.project2.cloud.models.ConnectResult;
import edu.msu.team23.project2.cloud.models.LoadResult;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

@SuppressWarnings("deprecation")
public class Cloud {
    private static final String MAGIC = "7VpFwBLcKne5p0Uu";
    private static final String BASE_URL = "https://webdev.cse.msu.edu/~szczerb7/cse476/project2/";
    public static final String SAVE_STATE_PATH = "save-state.php";
    public static final String LOAD_STATE_PATH = "load-state.php";
    public static final String LOGIN_USER_PATH = "login-user.php";
    public static final String CREATE_USER_PATH = "create-user.php";
    public static final String CONNECT_PATH = "connect.php";
    public static final String DISCONNECT_PATH = "disconnect.php";
    public static final String STATUS_SUCCESS = "success";
    public static final String PASSWORD_ERROR = "password error";
    public static final String NAME_TAKEN_ERROR = "name taken";

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
        CheckersService service = retrofit.create(CheckersService.class);
        try {
            Response<CheckersResult> response = service.loginUser(username, MAGIC, password).execute();
            if (!response.isSuccessful()) {
                return null;
            }
            return response.body();
        } catch(IOException | RuntimeException e) {
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
        CheckersService service = retrofit.create(CheckersService.class);
        try {
            Response<CheckersResult> response = service.createUser(username, MAGIC, password).execute();
            if (!response.isSuccessful()) {
                return null;
            }
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
