package pe.com.ladc.util;

public class GameMessages {

    private GameMessages() {
        throw new IllegalStateException("Utility class");
    }

    public static final String GAME_ALREADY_EXISTS =
            "A game with title '%s' and category '%s' already exists.";
    public static final String GAME_NOT_FOUND =
            "Game not found with id %d";
    public static final String GAME_DOES_NOT_EXIST =
            "Game does not exist with id %d";
    public static final String PAGE_SIZE_INVALID =
            "Page and size must be greater than 0";
    public static final String INVALID_CATEGORY =
            "Invalid category: %s";
}
