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
    private static final String OVERRIDE = "override", VALUE = "value";
    private static final int TYPE_BOOLEAN = 0, TYPE_NUMBER = 1, TYPE_STRING = 2;
    
    /**
     * Gets a configuration value from the robot filesystem
     * 
     * @param name The name of the property to get
     * @param defaultValue The value to be returned if no other value exists
     * @return The value for of the property
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(String name, T defaultValue) {
        if(defaultValue instanceof Boolean) {
            Optional<JsonPrimitive> val = get(name, new JsonPrimitive(Boolean.class.cast(defaultValue)));
            if(val.isPresent()) {
                return (T) defaultValue.getClass().cast(val.get().getAsBoolean());
            }
        }
        else if(defaultValue instanceof String) {
            Optional<JsonPrimitive> val = get(name, new JsonPrimitive(String.class.cast(defaultValue)));
            if(val.isPresent()) {
                return (T) defaultValue.getClass().cast(val.get().getAsString());
            }
        }
        else if(defaultValue instanceof Character) {
            Optional<JsonPrimitive> val = get(name, new JsonPrimitive(Character.class.cast(defaultValue)));
            if(val.isPresent()) {
                return (T) defaultValue.getClass().cast(val.get().getAsCharacter());
            }
        }
        else if(defaultValue instanceof Number) {
            Optional<JsonPrimitive> val = get(name, new JsonPrimitive(Number.class.cast(defaultValue)));
            if(val.isPresent()) {
                Number num = val.get().getAsNumber();
                if(defaultValue instanceof Byte) {
                    return (T) defaultValue.getClass().cast(num.byteValue());
                }
                else if(defaultValue instanceof Double) {
                    return (T) defaultValue.getClass().cast(num.doubleValue());
                }
                else if(defaultValue instanceof Float) {
                    return (T) defaultValue.getClass().cast(num.floatValue());
                }
                else if(defaultValue instanceof Integer) {
                    return (T) defaultValue.getClass().cast(num.intValue());
                }
                else if(defaultValue instanceof Long) {
                    return (T) defaultValue.getClass().cast(num.longValue());
                }
                else if(defaultValue instanceof Short) {
                    return (T) defaultValue.getClass().cast(num.shortValue());
                }
            }
        }
        
        return defaultValue;
    } 
    
    private static Optional<JsonPrimitive> get(String name, JsonPrimitive defaultValue) {
        String[] path = name.split("\\.");
        if(path.length < 2) {
            return Optional.empty();
        }
        
        JsonObject json;
        JsonObject root;        
        Optional<JsonObject> optional = getJSON(path[0]);
        if(optional.isPresent()) {
            json = optional.get();
        }
        else {
            json = new JsonObject();
        }
        
        root = json;

        boolean success = true;
        
        for(int i = 1; i < path.length; i++) {
            if(!json.has(path[i]) || !json.get(path[i]).isJsonObject()) {
                json.remove(path[i]);
                json.add(path[i], new JsonObject());
                success = false;
            }
            json = json.getAsJsonObject(path[i]);
        }
        
        if(!json.has(VALUE) || !json.get(VALUE).isJsonPrimitive()
                        || getPrimitiveType(json.get(VALUE).getAsJsonPrimitive())
                        != getPrimitiveType(defaultValue)) {
            success = false;
        }
        
        if(!json.has(OVERRIDE) || !json.get(OVERRIDE).isJsonPrimitive()) {
            success = false;
        }
        else if(!json.get(OVERRIDE).getAsBoolean()) {
            if(!json.get(VALUE).getAsJsonPrimitive().equals(defaultValue)) {
                success = false;
            }
            else {
                return Optional.empty();
            }
        }
        
        if(!success) {
            json.remove(OVERRIDE);
            json.addProperty(OVERRIDE, false);
            
            json.remove(VALUE);
            json.add(VALUE, defaultValue);
            
            reserialize(path[0], root);
            return Optional.empty();
        }
        else {
            return Optional.of(json.getAsJsonPrimitive("value"));
        }
    }
    
    private static int getPrimitiveType(JsonPrimitive primitive) {
        if(primitive.isBoolean()) {
            return TYPE_BOOLEAN;
        }
        else if(primitive.isNumber()) {
            return TYPE_NUMBER;
        }
        else {
            return TYPE_STRING;
        }
    }
    
    private static Optional<JsonObject> getJSON(String name) {
        File configFile = new File(CONFIG_FOLDER, name + ".json");
        
        StringBuilder src = new StringBuilder();
        try {
            if(!configFile.isFile()) {
                configFile.getParentFile().mkdirs();
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
