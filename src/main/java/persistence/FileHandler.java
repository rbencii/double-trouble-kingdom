package persistence;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import model.Game;
import model.Map;
import model.Pair;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Implementation of FileHandler class, contains static methods for saving and loading Map and Game state to and from files.
 */
// Simplify classes with Factory
public class FileHandler {

    static ObjectMapper mapper = new ObjectMapper().registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES));


    /**
     * Saves a Map instance to a specified file.
     *
     * @param file the File we want to save to
     * @param map  the Map instance we want to save
     */
    public static void saveMap(File file, Map map) {
        saveToFile(file, map, ".dtk");
    }

    /**
     * Saves a Game instance to a specified file.
     *
     * @param file the File we want to save to
     * @param game the Game instance we want to save
     */
    public static void saveGame(File file, Game game) {
        saveToFile(file, game, ".dtk_save");
    }

    /**
     * The logic of saving an Object state to a specified file with a specified file type.
     *
     * @param file the File we want to save to
     * @param obj  the Object we want to save
     * @param type the extension of the file we save to
     */
    public static void saveToFile(File file, Object obj, String type) {
        try {
            FileOutputStream fileOut = new FileOutputStream(file + type);
            Serialize(fileOut, obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads a Map instance from a specified file.
     *
     * @param file the file we want to load from
     * @return a Map instance from the specified file
     */
    public static Map loadMap(File file) {
        try {
            return mapper.readValue(file, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Pair<Map, File> loadMapAndFile(File file) {
        try {

            Map map = mapper.readValue(file, Map.class);
            return new Pair<>(map, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Loads a Game instance from a specified file.
     *
     * @param file the file we want to load from
     * @return a Game instance from the specified file
     */
    public static Game loadGame(File file) {
        Game game = null;
        try {
            game = mapper.readValue(file, Game.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return game;
    }

    public static void Serialize(FileOutputStream file, Object obj) throws IOException {
        mapper.enable(com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT);
        mapper.writeValue(file, obj);
    }
}
