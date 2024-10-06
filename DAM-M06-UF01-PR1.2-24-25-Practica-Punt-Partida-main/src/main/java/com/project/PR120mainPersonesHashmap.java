package com.project;

import java.io.*;
import java.util.HashMap;
import java.util.Map.Entry;

import com.project.excepcions.IOFitxerExcepcio;

public class PR120mainPersonesHashmap {
    private static String filePath = System.getProperty("user.dir") + "/data/PR120persones.dat";

    public static void main(String[] args) {
        HashMap<String, Integer> persones = new HashMap<>();
        persones.put("Carla", 22);
        persones.put("Bernat", 30);
        persones.put("David", 35);
        persones.put("Anna", 25);
        persones.put("Elena", 28);

        try {
            escriurePersones(persones);
            llegirPersones();
        } catch (IOFitxerExcepcio e) {
            System.err.println("Error en treballar amb el fitxer: " + e.getMessage());
        }
    }

    public static String getFilePath() {
        return filePath;
    }

    public static void setFilePath(String newFilePath) {
        filePath = newFilePath;
    }

    public static void escriurePersones(HashMap<String, Integer> persones) throws IOFitxerExcepcio {
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(filePath))) {
            for (Entry<String, Integer> entry : persones.entrySet()) {
                dos.writeUTF(entry.getKey());
                dos.writeInt(entry.getValue()); 
            }
        } catch (FileNotFoundException e) {
            throw new IOFitxerExcepcio("El fitxer no s'ha trobat: " + filePath, e);
        } catch (IOException e) {
            throw new IOFitxerExcepcio("Error escrivint les dades al fitxer: " + filePath, e);
        }
    }

    public static void llegirPersones() throws IOFitxerExcepcio {
        try (DataInputStream dis = new DataInputStream(new FileInputStream(filePath))) {
            System.out.println("Contingut del fitxer:");
            while (true) {
                String nom = dis.readUTF(); 
                int edad = dis.readInt(); 
                System.out.println(nom + ": " + edad + " anys");
            }
        } catch (EOFException e) {
        } catch (FileNotFoundException e) {
            throw new IOFitxerExcepcio("El fitxer no s'ha trobat: " + filePath, e);
        } catch (IOException e) {
            throw new IOFitxerExcepcio("Error llegint les dades del fitxer: " + filePath, e);
        }
    }
}
