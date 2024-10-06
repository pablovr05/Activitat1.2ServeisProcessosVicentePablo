package com.project;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Map;

import com.project.excepcions.IOFitxerExcepcio;
import com.project.objectes.PR121hashmap;

public class PR121mainLlegeix {
    private static String filePath = System.getProperty("user.dir") + "/data/PR121HashMapData.ser";

    public static void main(String[] args) {
        try {
            PR121hashmap hashMap = deserialitzarHashMap();
            hashMap.getPersones().forEach((nom, edat) -> System.out.println(nom + ": " + edat + " anys"));
        } catch (IOFitxerExcepcio e) {
            System.err.println("Error al llegir l'arxiu: " + e.getMessage());
        }
    }

    /**
     * Deserialitza l'objecte PR121hashmap des del fitxer especificat.
     *
     * @return Una instància de PR121hashmap deserialitzada.
     * @throws IOFitxerExcepcio Si es produeix un error durant la deserialització.
     */
    public static PR121hashmap deserialitzarHashMap() throws IOFitxerExcepcio {
        // Utilitzar try-with-resources per gestionar automàticament els fluxos
        try (FileInputStream fis = new FileInputStream(filePath);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            PR121hashmap hashMap = (PR121hashmap) ois.readObject();
            return hashMap;
        } catch (IOException e) { // Inclou FileNotFoundException
            throw new IOFitxerExcepcio("Error en deserialitzar l'objecte HashMap: " + filePath, e);
        } catch (ClassNotFoundException e) {
            throw new IOFitxerExcepcio("La classe PR121hashmap no s'ha trobat durant la deserialització.", e);
        }
    }

    // Getter per a filePath
    public static String getFilePath() {
        return filePath;
    }

    // Setter per a filePath
    public static void setFilePath(String newFilePath) {
        filePath = newFilePath;
    }
}
