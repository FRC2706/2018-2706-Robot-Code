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

public class RobotConfig {

    private static final File CONFIG_FOLDER = new File("/home/lvuser/config/");
    
    /**
     * Gets a configuration value from the robot filesystem
     * 
     * @param name The name of the property to get
     * @param defaultValue The value to be returned if no other value exists
     * @return The value for of the property
     */
    public static boolean get(String name, boolean defaultValue) {
        Optional<JsonPrimitive> val = get(name, new JsonPrimitive(defaultValue));
        if(val.isPresent()) {
            return val.get().getAsBoolean();
        }
        else {
            return defaultValue;
        }
    }
    
    /**
     * Gets a configuration value from the robot filesystem
     * 
     * @param name The name of the property to get
     * @param defaultValue The value to be returned if no other value exists
     * @return The value for of the property
     */
    public static char get(String name, char defaultValue) {
        Optional<JsonPrimitive> val = get(name, new JsonPrimitive(defaultValue));
        if(val.isPresent()) {
            return val.get().getAsCharacter();
        }
        else {
            return defaultValue;
        }
    }
    
    /**
     * Gets a configuration value from the robot filesystem
     * 
     * @param name The name of the property to get
     * @param defaultValue The value to be returned if no other value exists
     * @return The value for of the property
     */
    public static Number get(String name, Number defaultValue) {
        Optional<JsonPrimitive> val = get(name, new JsonPrimitive(defaultValue));
        if(val.isPresent()) {
            return val.get().getAsNumber();
        }
        else {
            return defaultValue;
        }
    }
    
    /**
     * Gets a configuration value from the robot filesystem
     * 
     * @param name The name of the property to get
     * @param defaultValue The value to be returned if no other value exists
     * @return The value for of the property
     */
    public static String get(String name, String defaultValue) {
        Optional<JsonPrimitive> val = get(name, new JsonPrimitive(defaultValue));
        if(val.isPresent()) {
            return val.get().getAsString();
        }
        else {
            return defaultValue;
        }
    }
    
    private static Optional<JsonPrimitive> get(String name, JsonPrimitive defaultValue) {
        String[] path = name.split(".");
        if(path.length < 2) {
            return Optional.empty();
        }
        
        JsonObject json;
        JsonObject root;        
        Optional<JsonObject> optional = getJSON(path[0]);
        if(optional.isPresent()) {
            json = optional.get();
            root = json;
        }
        else {
            return Optional.empty();
        }

        boolean success = true;
        
        for(int i = 1; i < path.length - 2; i++) {
            if(!json.has(path[i])) {
                json.addProperty(path[i], "");
                success = false;
            }
            json = json.getAsJsonObject(path[i]);
        }
        
        if(!json.has(path[path.length - 1])) {
            json.add(path[path.length - 1], defaultValue);
            
            success = false;
        }
        else {
            return Optional.of(json.getAsJsonPrimitive(path[path.length - 1]));
        }
        
        if(!success) {
            reserialize(path[0], root);
        }
        
        return Optional.empty();
        
    }
    
    private static Optional<JsonObject> getJSON(String name) {
        File configFile = new File(CONFIG_FOLDER, name + ".json");
        
        StringBuilder src = new StringBuilder();
        try {
            if(!configFile.isFile()) {
                configFile.createNewFile();
                return Optional.empty();
            }
            
            BufferedReader configReader = new BufferedReader(new FileReader(configFile));
            
            String line;
            while((line = configReader.readLine()) != null) {
                // JSON doesn't care about newlines
                src.append(line); 
            }
            
            configReader.close();
        }
        catch(IOException e) {
            return Optional.empty();
        }
        
        return Optional.of(
                   new Gson().fromJson(src.toString(), JsonObject.class));
    }
    
    private static void reserialize(String name, JsonObject object) {
        String output = new GsonBuilder().setPrettyPrinting().create().toJson(object);
        
        File configFile = new File(CONFIG_FOLDER, name + ".json");
        
        try {
            FileWriter writer = new FileWriter(configFile);
            writer.write(output);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        
    }
}
