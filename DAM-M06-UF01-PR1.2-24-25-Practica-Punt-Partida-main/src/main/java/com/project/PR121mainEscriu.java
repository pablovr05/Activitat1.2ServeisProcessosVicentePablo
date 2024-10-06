package com.project;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import com.project.excepcions.IOFitxerExcepcio;
import com.project.objectes.PR121hashmap;

public class PR121mainEscriu {
    private static String filePath = System.getProperty("user.dir") + "/data/PR121HashMapData.ser";

    public static void main(String[] args) {
        PR121hashmap hashMap = new PR121hashmap();
        hashMap.getPersones().put("Anna", 25);
        hashMap.getPersones().put("Bernat", 30);
        hashMap.getPersones().put("Carla", 22);
        hashMap.getPersones().put("David", 35);
        hashMap.getPersones().put("Elena", 28);

        try {
            serialitzarHashMap(hashMap);
            System.out.println("HashMap serialitzat correctament a " + filePath);
        } catch (IOFitxerExcepcio e) {
            System.err.println("Error al desar l'arxiu: " + e.getMessage());
        }
    }

    public static String getFilePath() {
        return filePath;
    }

    public static void setFilePath(String newFilePath) {
        filePath = newFilePath;
    }

    /**
     *
     * @param hashMap
     * @throws IOFitxerExcepcio 
     */
    public static void serialitzarHashMap(PR121hashmap hashMap) throws IOFitxerExcepcio {
        File directory = new File(System.getProperty("user.dir") + "/data");
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                throw new IOFitxerExcepcio("No s'ha pogut crear el directori: " + directory.getAbsolutePath());
            }
        }

        try (FileOutputStream fos = new FileOutputStream(filePath);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(hashMap);
        } catch (IOException e) { // Inclou FileNotFoundException
            throw new IOFitxerExcepcio("Error escrivint les dades al fitxer: " + filePath, e);
        }
    }
}
