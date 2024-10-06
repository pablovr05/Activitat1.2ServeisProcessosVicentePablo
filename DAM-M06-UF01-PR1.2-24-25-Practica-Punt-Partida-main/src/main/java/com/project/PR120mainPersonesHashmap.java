package com.project;

import java.io.*;
import java.util.HashMap;
import java.util.Map.Entry;

import com.project.excepcions.IOFitxerExcepcio;

public class PR120mainPersonesHashmap {
    private static String filePath = System.getProperty("user.dir") + "/data/PR120persones.dat";

    public static void main(String[] args) {
        HashMap<String, Integer> persones = new HashMap<>();
        persones.put("Anna", 25);
        persones.put("Bernat", 30);
        persones.put("Carla", 22);
        persones.put("David", 35);
        persones.put("Elena", 28);

        try {
            escriurePersones(persones);
            llegirPersones();
        } catch (IOFitxerExcepcio e) {
            System.err.println("Error en treballar amb el fitxer: " + e.getMessage());
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

    // Mètode per escriure les persones al fitxer
    public static void escriurePersones(HashMap<String, Integer> persones) throws IOFitxerExcepcio {
       // *************** CODI PRÀCTICA **********************/
       try {
        FileOutputStream fos = new FileOutputStream(filePath);

        ObjectOutputStream oss = new ObjectOutputStream(fos);
        oss.writeObject(persones);
        
        oss.close();
        fos.close();

       } catch (FileNotFoundException e) {
        System.out.println("El ficherpo no se encontró: " + e.getMessage());
       } catch (IOException e) {
        System.out.println("Error de Entrada/Salida: " + e.getMessage());
       }
    }

    // Mètode per llegir les persones des del fitxer
    public static void llegirPersones() throws IOFitxerExcepcio {
        // *************** CODI PRÀCTICA **********************/
        try {
            FileInputStream fis = new FileInputStream(filePath);
            ObjectInputStream ois = new ObjectInputStream(fis);

            @SuppressWarnings("unchecked")
            HashMap<String, Integer> persones = (HashMap<String, Integer>) ois.readObject();

            for (Entry<String, Integer> entry : persones.entrySet()) {
                String nombre = entry.getKey();
                int edad = entry.getValue();
                System.out.println(nombre + ": " + edad + " anys");
            }

            ois.close();
            fis.close();
            
        } catch (FileNotFoundException e) {
            System.out.println("El ficherpo no se encontró: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error de Entrada/Salida: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("La clase no se encontró: " + e.getMessage());;
        } 
    }
}
