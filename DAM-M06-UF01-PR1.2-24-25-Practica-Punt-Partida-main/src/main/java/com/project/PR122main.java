package com.project;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import com.project.excepcions.IOFitxerExcepcio;
import com.project.objectes.PR122persona;

public class PR122main {
    private static String filePath = System.getProperty("user.dir") + "/data/PR122persones.dat";

    public static void main(String[] args) {
        List<PR122persona> persones = new ArrayList<>();
        persones.add(new PR122persona("Maria", "López", 36));
        persones.add(new PR122persona("Gustavo", "Ponts", 63));
        persones.add(new PR122persona("Irene", "Sales", 54));

        try {
            serialitzarPersones(persones);
            List<PR122persona> deserialitzades = deserialitzarPersones();
            deserialitzades.forEach(System.out::println); 
        } catch (IOFitxerExcepcio e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    public static void serialitzarPersones(List<PR122persona> persones) throws IOFitxerExcepcio {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(persones);
        } catch (IOException e) {
            throw new IOFitxerExcepcio("Error en serialitzar la llista de persones", e);
        }
    }

    public static List<PR122persona> deserialitzarPersones() throws IOFitxerExcepcio {
        File fitxer = new File(filePath);
        if (!fitxer.exists()) {
            throw new IOFitxerExcepcio("Fitxer no trobat: " + filePath);
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fitxer))) {
            return (List<PR122persona>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new IOFitxerExcepcio("Error en deserialitzar la llista de persones", e);
        }
    }

    public static String getFilePath() {
        return filePath;
    }

    public static void setFilePath(String newFilePath) {
        filePath = newFilePath;
    }
}
