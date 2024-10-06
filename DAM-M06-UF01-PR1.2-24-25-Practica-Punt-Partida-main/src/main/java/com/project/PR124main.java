package com.project;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class PR124main {

    private static final int ID_SIZE = 4;
    private static final int NAME_MAX_BYTES = 40; 
    private static final int GRADE_SIZE = 4; 

    // Posicions dels camps dins el registre
    private static final int NAME_POS = ID_SIZE; 
    private static final int GRADE_POS = NAME_POS + NAME_MAX_BYTES;

    private String filePath;

    private Scanner scanner = new Scanner(System.in);

    public PR124main() {
        this.filePath = System.getProperty("user.dir") + "/data/PR124estudiants.dat"; // Valor per defecte
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public static void main(String[] args) {
        PR124main gestor = new PR124main();
        boolean sortir = false;

        while (!sortir) {
            try {
                gestor.mostrarMenu();
                int opcio = gestor.getOpcioMenu();

                switch (opcio) {
                    case 1 -> gestor.llistarEstudiants();
                    case 2 -> gestor.afegirEstudiant();
                    case 3 -> gestor.consultarNota();
                    case 4 -> gestor.actualitzarNota();
                    case 5 -> sortir = true;
                    default -> System.out.println("Opció no vàlida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Si us plau, introdueix un número vàlid.");
            } catch (IOException e) {
                System.out.println("Error en la manipulació del fitxer: " + e.getMessage());
            }
        }
    }

    private void mostrarMenu() {
        System.out.println("\nMenú de Gestió d'Estudiants");
        System.out.println("1. Llistar estudiants");
        System.out.println("2. Afegir nou estudiant");
        System.out.println("3. Consultar nota d'un estudiant");
        System.out.println("4. Actualitzar nota d'un estudiant");
        System.out.println("5. Sortir");
        System.out.print("Selecciona una opció: ");
    }

    private int getOpcioMenu() {
        return Integer.parseInt(scanner.nextLine());
    }

    public void llistarEstudiants() throws IOException {
        llistarEstudiantsFitxer();
    }

    public void afegirEstudiant() throws IOException {
        int registre = demanarRegistre();
        String nom = demanarNom();
        float nota = demanarNota();
        afegirEstudiantFitxer(registre, nom, nota);
    }

    public void consultarNota() throws IOException {
        int registre = demanarRegistre();
        consultarNotaFitxer(registre);
    }

    public void actualitzarNota() throws IOException {
        int registre = demanarRegistre();
        float novaNota = demanarNota();
        actualitzarNotaFitxer(registre, novaNota);
    }

    private int demanarRegistre() {
        System.out.print("Introdueix el número de registre (enter positiu): ");
        int registre = Integer.parseInt(scanner.nextLine());
        if (registre < 0) {
            throw new IllegalArgumentException("El número de registre ha de ser positiu.");
        }
        return registre;
    }

    private String demanarNom() {
        System.out.print("Introdueix el nom (màxim 20 caràcters, depenent dels bytes UTF-8): ");
        return scanner.nextLine();
    }

    private float demanarNota() {
        System.out.print("Introdueix la nota (valor entre 0 i 10): ");
        float nota = Float.parseFloat(scanner.nextLine());
        if (nota < 0 || nota > 10) {
            throw new IllegalArgumentException("La nota ha de ser un valor entre 0 i 10.");
        }
        return nota;
    }

    private long trobarPosicioRegistre(RandomAccessFile raf, int registreBuscat) throws IOException {
        long posicio = -1;
        long numRegistres = raf.length() / (ID_SIZE + NAME_MAX_BYTES + GRADE_SIZE);
        for (long i = 0; i < numRegistres; i++) {
            raf.seek(i * (ID_SIZE + NAME_MAX_BYTES + GRADE_SIZE));
            int registre = raf.readInt();
            if (registre == registreBuscat) {
                posicio = i * (ID_SIZE + NAME_MAX_BYTES + GRADE_SIZE);
                break;
            }
        }
        return posicio;
    }

    public void llistarEstudiantsFitxer() throws IOException {
        File fitxer = new File(filePath);
        if (!fitxer.exists() || fitxer.length() == 0) {
            System.out.println("No hi ha estudiants registrats.");
            return;
        }
        try (RandomAccessFile raf = new RandomAccessFile(fitxer, "r")) {
            long numRegistres = raf.length() / (ID_SIZE + NAME_MAX_BYTES + GRADE_SIZE);
            for (long i = 0; i < numRegistres; i++) {
                raf.seek(i * (ID_SIZE + NAME_MAX_BYTES + GRADE_SIZE));
                int registre = raf.readInt();
                String nom = llegirNom(raf);
                float nota = raf.readFloat();
                System.out.println("Registre: " + registre + ", Nom: " + nom + ", Nota: " + nota);
            }
        }
    }

    public void afegirEstudiantFitxer(int registre, String nom, float nota) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(filePath, "rw")) {
            long numRegistres = raf.length() / (ID_SIZE + NAME_MAX_BYTES + GRADE_SIZE);
            long posicio = trobarPosicioRegistre(raf, registre);
            if (posicio != -1) {
                System.out.println("Ja existeix un estudiant amb registre: " + registre);
                return;
            }
            // Afegir el nou estudiant
            raf.seek(numRegistres * (ID_SIZE + NAME_MAX_BYTES + GRADE_SIZE));
            raf.writeInt(registre);
            escriureNom(raf, nom);
            raf.writeFloat(nota);
            System.out.println("Estudiant afegit correctament.");
        }
    }

    public void consultarNotaFitxer(int registre) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(filePath, "r")) {
            long posicio = trobarPosicioRegistre(raf, registre);
            if (posicio == -1) {
                System.out.println("No s'ha trobat l'estudiant amb registre: " + registre);
                return;
            }
            raf.seek(posicio);
            int reg = raf.readInt();
            String nom = llegirNom(raf);
            float nota = raf.readFloat();
            System.out.println("Registre: " + reg + ", Nom: " + nom + ", Nota: " + nota);
        }
    }

    public void actualitzarNotaFitxer(int registre, float novaNota) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(filePath, "rw")) {
            long posicio = trobarPosicioRegistre(raf, registre);
            if (posicio == -1) {
                System.out.println("No s'ha trobat l'estudiant amb registre: " + registre);
                return;
            }
            raf.seek(posicio + GRADE_POS);
            raf.writeFloat(novaNota);
            System.out.println("Nota actualitzada correctament.");
        }
    }

    private String llegirNom(RandomAccessFile raf) throws IOException {
        byte[] bytesNom = new byte[NAME_MAX_BYTES];
        raf.read(bytesNom);
        return new String(bytesNom, StandardCharsets.UTF_8).trim();
    }

    private void escriureNom(RandomAccessFile raf, String nom) throws IOException {
        byte[] bytesNom = nom.getBytes(StandardCharsets.UTF_8);
        if (bytesNom.length > NAME_MAX_BYTES) {
            throw new IllegalArgumentException("El nom supera el màxim de bytes permitits.");
        }
        raf.write(bytesNom);
        for (int i = bytesNom.length; i < NAME_MAX_BYTES; i++) {
            raf.writeByte(0);
        }
    }
}
