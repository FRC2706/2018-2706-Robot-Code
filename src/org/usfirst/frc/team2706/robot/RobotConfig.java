package org.usfirst.frc.team2706.robot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

/**
 * Handles loading of data required for autonomous modes and {@link RobotMap}
 * 
 * @author eandr127
 */
public class RobotConfig {

    /**
     * Gets a configuration value from the robot filesystem
     * 
     * @param name The name of the property to get
     * @param defaultValue The value to be returned if no other value exists
     * @return The value for of the property
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(String name, T defaultValue) {
        /*// Create JSON primitives depending on the type of T
        if (defaultValue instanceof Boolean) {
            // Try to get the value of the config location or return Optional.empty() for default
            Optional<JsonPrimitive> val =
                            get(name, new JsonPrimitive(Boolean.class.cast(defaultValue)));
            if (val.isPresent()) {
                // Cast the returned value back to to T to be returned
                return (T) defaultValue.getClass().cast(val.get().getAsBoolean());
            }
        } else if (defaultValue instanceof String) {
            // Try to get the value of the config location or return Optional.empty() for default
            Optional<JsonPrimitive> val =
                            get(name, new JsonPrimitive(String.class.cast(defaultValue)));
            if (val.isPresent()) {
                // Cast the returned value back to to T to be returned
                return (T) defaultValue.getClass().cast(val.get().getAsString());
            }
        } else if (defaultValue instanceof Character) {
            // Try to get the value of the config location or return Optional.empty() for default
            Optional<JsonPrimitive> val =
                            get(name, new JsonPrimitive(Character.class.cast(defaultValue)));
            if (val.isPresent()) {
                // Cast the returned value back to to T to be returned
                return (T) defaultValue.getClass().cast(val.get().getAsCharacter());
            }
        } else if (defaultValue instanceof Number) {
            // Try to get the value of the config location or return Optional.empty() for default
            Optional<JsonPrimitive> val =
                            get(name, new JsonPrimitive(Number.class.cast(defaultValue)));
            if (val.isPresent()) {
                // Cast the number back to the correct type it should be
                Number num = val.get().getAsNumber();
                if (defaultValue instanceof Byte) {
                    return (T) defaultValue.getClass().cast(num.byteValue());
                } else if (defaultValue instanceof Double) {
                    return (T) defaultValue.getClass().cast(num.doubleValue());
                } else if (defaultValue instanceof Float) {
                    return (T) defaultValue.getClass().cast(num.floatValue());
                } else if (defaultValue instanceof Integer) {
                    return (T) defaultValue.getClass().cast(num.intValue());
                } else if (defaultValue instanceof Long) {
                    return (T) defaultValue.getClass().cast(num.longValue());
                } else if (defaultValue instanceof Short) {
                    return (T) defaultValue.getClass().cast(num.shortValue());
                }
            }
        }
*/
        // Either config didn't have correct information, T wasn't a valid type, or value wasn't
        // overridden
        return defaultValue;
    }

    /**
     * Location of the folder that config files are stored
     */
    private static final File CONFIG_FOLDER = new File("/home/lvuser/config/");

    /**
     * The name of the primitives for override and value
     */
    private static final String OVERRIDE = "override", VALUE = "value";

    /**
     * Different JSON types
     */
    private static final int TYPE_BOOLEAN = 0, TYPE_NUMBER = 1, TYPE_STRING = 2;

    /**
     * Get's a JSON primitive from a location or return default value
     * 
     * @param name The name of the property to find
     * @param defaultValue The default value in case the name can't be found
     * @return The JSON primitive value of the property if it can be found
     */
    private static Optional<JsonPrimitive> get(String name, JsonPrimitive defaultValue) {
        // Get each name in the chain of objects
        String[] path = name.split("\\.");

        // Ensure path has a file name, and a name within the file
        if (path.length < 2) {
            return Optional.empty();
        }

        JsonObject json;
        JsonObject root;

        // Try to load the file from the filesystem, otherwise make empty object t
        Optional<JsonObject> optional = getJSON(path[0]);
        if (optional.isPresent()) {
            json = optional.get();
        } else {
            json = new JsonObject();
        }

        // Save root for reserialization
        root = json;

        // Assume every value required can be loaded successfully until they're not
        boolean reserialize = true;

        // Go through each sub object starting after the file name
        for (int i = 1; i < path.length; i++) {
            // Ensure that the name exists and is an object that can have children
            if (!json.has(path[i]) || !json.get(path[i]).isJsonObject()) {
                // Remake the missing object and indicate that reserialization is necessary
                json.remove(path[i]);
                json.add(path[i], new JsonObject());
                reserialize = false;
            }

            // Move to child so that loop will get closer to the final name in the path
            json = json.getAsJsonObject(path[i]);
        }

        // Ensure that value and override are valid, otherwise they must both be made before
        // reserialization
        if (!json.has(VALUE) || !json.get(VALUE).isJsonPrimitive() || getPrimitiveType(
                        json.get(VALUE).getAsJsonPrimitive()) != getPrimitiveType(defaultValue)
                        || !json.has(OVERRIDE) || !json.get(OVERRIDE).isJsonPrimitive()) {
            reserialize = false;
        }
        // Value is not overriden
        else if (!json.get(OVERRIDE).getAsBoolean()) {

            // Indicate that the override and value must be remade
            if (!json.get(VALUE).getAsJsonPrimitive().equals(defaultValue)) {
                reserialize = false;
            } else {
                // No changes are required, and cofnig indicated default value
                return Optional.empty();
            }
        }

        // JSON must be rewritten
        if (!reserialize) {
            // Remake override and value tags as default
            json.remove(OVERRIDE);
            json.addProperty(OVERRIDE, false);

            json.remove(VALUE);
            json.add(VALUE, defaultValue);

            // Rewrite file
            reserialize(path[0], root);

            // Use default value
            return Optional.empty();
        } else {
            // Config indicates that its value should be used, so use it
            return Optional.of(json.getAsJsonPrimitive("value"));
        }
    }

    /**
     * Gets the type of a {@code JsonPrimitive}
     * 
     * @param primitive The primitive to check
     * @return The type
     */
    private static int getPrimitiveType(JsonPrimitive primitive) {
        if (primitive.isBoolean()) {
            return TYPE_BOOLEAN;
        } else if (primitive.isNumber()) {
            return TYPE_NUMBER;
        } else {
            return TYPE_STRING;
        }
    }

    /**
     * Loads JSON from a file name (without file extension)
     * 
     * @param name The name of the file
     * @return The Root tag of the JSON file if it can be loaded
     */
    private static Optional<JsonObject> getJSON(String name) {
        // Get the file location
        File configFile = new File(CONFIG_FOLDER, name + ".json");

        StringBuilder src = new StringBuilder();
        try {

            // Create file as it doesn't exist
            if (!configFile.isFile()) {
                configFile.getParentFile().mkdirs();
                configFile.createNewFile();

                // Continue to create JSON for file and write it back to the file
                return Optional.empty();
            }

            BufferedReader configReader = new BufferedReader(new FileReader(configFile));

            // Read each line into a String
            String line;
            while ((line = configReader.readLine()) != null) {
                // JSON doesn't care about newlines
                src.append(line);
            }

            configReader.close();
        } catch (IOException e) {
            // Something went wrong, just use default value
            return Optional.empty();
        }

        // Create the root JSON object from the JSON text
        return Optional.of(new Gson().fromJson(src.toString(), JsonObject.class));
    }

    /**
     * Rewrite the JSON file
     * 
     * @param name The name of the file name without its file extension
     * @param object The root JSON to reserialize
     */
    private static void reserialize(String name, JsonObject object) {
        // Create JSON String with nice formatting
        String output = new GsonBuilder().setPrettyPrinting().create().toJson(object);

        // Find file to write to
        File configFile = new File(CONFIG_FOLDER, name + ".json");

        // Write the JSON data to the file
        try {
            FileWriter writer = new FileWriter(configFile);
            writer.write(output);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
