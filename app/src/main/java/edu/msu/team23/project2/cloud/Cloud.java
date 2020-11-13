package edu.msu.team23.project2.cloud;

import edu.msu.team23.project2.CheckersGame;
import edu.msu.team23.project2.cloud.models.CheckersResult;
import edu.msu.team23.project2.cloud.models.ConnectResult;
import edu.msu.team23.project2.cloud.models.LoadResult;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

@SuppressWarnings("deprecation")
public class Cloud {
    private static final String MAGIC = "";
    private static final String BASE_URL = "";
    private static final String SAVE_STATE_PATH = "save-state.php";
    private static final String LOAD_STATE_PATH = "load-state.php";
    private static final String LOGIN_PATH = "login.php";
    private static final String CREATE_USER_PATH = "create-user.php";
    private static final String SEARCH_PATH = "search.php";
    private static final String CONNECT_PATH = "connect.php";
    private static final String DISCONNECT_PATH = "disconnect.php";
    private static final String UTF8 = "UTF-8";

    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .build();

    /**
     * Save the current state of the game to the server's database.
     * @param userName Username of the client
     * @param password Password for the logged in user
     * @return Result of the save state call
     */
    public CheckersResult saveState(String userName, String password, CheckersGame game) {
        return new CheckersResult();
    }

    /**
     * Attempt to load the state of the game the user is in.
     * @param userName Username of the client
     * @param password Password for the logged in user
     * @return Result of the load state call
     */
    public LoadResult loadState(String userName, String password) {
        return new LoadResult();
    }

    /**
     * Login to a pre-existing user in the server database.
     * @param userName Username of the client
     * @param password Password for the logged in user
     * @return Result of the login user call
     */
    public CheckersResult loginUser(String userName, String password) {
        return new CheckersResult();
    }

    /**
     * Create a new user in the server's database.
     * @param userName Username of the client
     * @param password Password for the logged in user
     * @return Result of the create user call
     */
    public CheckersResult createUser(String userName, String password) {
        return new CheckersResult();
    }

    /**
     * Begin the search for a new game.
     * @param userName Username of the client
     * @param password Password for the logged in user
     * @return Result of the search call
     */
    public CheckersResult search(String userName, String password) {
        return new CheckersResult();
    }

    /**
     * Attempt to connect to a new game.
     * @param userName Username of the client
     * @param password Password for the logged in user
     * @return Result of the connect call
     */
    public ConnectResult connect(String userName, String password) {
        return new ConnectResult();
    }

    /**
     * Disconnect from the game the client is in.
     * @param userName Username of the client
     * @param password Password for the logged in user
     * @return Result of the disconnect call
     */
    public CheckersResult disconnect(String userName, String password) {
        return new CheckersResult();
    }
}
