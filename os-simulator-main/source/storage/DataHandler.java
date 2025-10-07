package storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.*;
import java.lang.reflect.Type;

public class DataHandler {

    public static <T> T loadData(String path, Type type) {
        try (Reader reader = new FileReader(path)) {
            Gson gson = new Gson();
            return gson.fromJson(reader, type);
        } catch (FileNotFoundException e) {
            // FIX: Return null when the file doesn't exist.
            // The calling code will be responsible for creating a default object.
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            // Also return null for any other reading errors.
            return null;
        }
    }

    public static <T> void saveData(String path, T data) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File outputFile = new File(path);
        File parentDir = outputFile.getParentFile();

        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        try (Writer writer = new FileWriter(outputFile)) {
            gson.toJson(data, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}