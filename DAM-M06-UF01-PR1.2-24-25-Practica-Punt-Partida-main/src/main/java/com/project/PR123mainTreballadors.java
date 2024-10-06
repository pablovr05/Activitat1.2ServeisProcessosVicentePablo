package com.project;

import com.project.excepcions.IOFitxerExcepcio;
import com.project.utilitats.UtilsCSV;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PR123mainTreballadors {
    private String filePath = System.getProperty("user.dir") + "/data/PR123treballadors.csv";
    private Scanner scanner = new Scanner(System.in);

    // Getters i setters per a filePath
    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void iniciar() {
        boolean sortir = false;

        while (!sortir) {
            try {
                // Mostrar menú
                mostrarMenu();

                // Llegir opció de l'usuari
                int opcio = Integer.parseInt(scanner.nextLine());

                switch (opcio) {
                    case 1 -> mostrarTreballadors();
                    case 2 -> modificarTreballadorInteractiu();
                    case 3 -> {
                        System.out.println("Sortint...");
                        sortir = true;
                    }
                    default -> System.out.println("Opció no vàlida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Si us plau, introdueix un número vàlid.");
            } catch (IOFitxerExcepcio e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }

    // Mètode que mostra el menú
    private void mostrarMenu() {
        System.out.println("\nMenú de Gestió de Treballadors");
        System.out.println("1. Mostra tots els treballadors");
        System.out.println("2. Modificar dades d'un treballador");
        System.out.println("3. Sortir");
        System.out.print("Selecciona una opció: ");
    }

    // Mètode per mostrar els treballadors llegint el fitxer CSV
    public void mostrarTreballadors() throws IOFitxerExcepcio {
        List<String> treballadorsCSV = llegirFitxerCSV();

        if (treballadorsCSV.isEmpty()) {
            System.out.println("No hi ha treballadors per mostrar.");
            return;
        }

        // Mostrar capçalera
        String capcalera = treballadorsCSV.get(0);
        String[] columnes = capcalera.split(",");
        System.out.println(String.join(" | ", columnes));

        // Mostrar separador
        StringBuilder separador = new StringBuilder();
        for (String col : columnes) {
            separador.append("-".repeat(col.length())).append(" | ");
        }
        System.out.println(separador.toString());

        // Mostrar treballadors
        for (int i = 1; i < treballadorsCSV.size(); i++) {
            System.out.println(treballadorsCSV.get(i).replace(",", " | "));
        }
    }

    // Mètode per modificar un treballador (interactiu)
    public void modificarTreballadorInteractiu() throws IOFitxerExcepcio {
        // Demanar l'ID del treballador
        System.out.print("\nIntrodueix l'ID del treballador que vols modificar: ");
        String id = scanner.nextLine();

        // Demanar quina dada vols modificar
        System.out.print("Quina dada vols modificar (Nom, Cognom, Departament, Salari)? ");
        String columna = scanner.nextLine();

        // Validar columna
        if (!columna.equalsIgnoreCase("Nom") &&
            !columna.equalsIgnoreCase("Cognom") &&
            !columna.equalsIgnoreCase("Departament") &&
            !columna.equalsIgnoreCase("Salari")) {
            System.out.println("Dada no vàlida. Les opcions són: Nom, Cognom, Departament, Salari.");
            return;
        }

        // Demanar el nou valor
        System.out.print("Introdueix el nou valor per a " + columna + ": ");
        String nouValor = scanner.nextLine();

        // Modificar treballador
        boolean modificat = modificarTreballador(id, columna, nouValor);

        if (modificat) {
            System.out.println("Treballador modificat correctament.");
            mostrarTreballadors();
            System.out.print("Vols guardar els canvis? (s/n): ");
            String resposta = scanner.nextLine();
            if (resposta.equalsIgnoreCase("s")) {
                System.out.println("Canvis guardats.");
            } else {
                System.out.println("Canvis no guardats.");
                // Recarregar el fitxer per descartar canvis no guardats
                mostrarTreballadors();
            }
        } else {
            System.out.println("No s'ha trobat cap treballador amb l'ID proporcionat.");
        }
    }

    // Mètode que modifica treballador (per a tests i usuaris) llegint i escrivint sobre disc
    public boolean modificarTreballador(String id, String columna, String nouValor) throws IOFitxerExcepcio {
        List<String> treballadorsCSV = llegirFitxerCSV();
        boolean trobat = false;

        if (treballadorsCSV.isEmpty()) {
            throw new IOFitxerExcepcio("El fitxer està buit.");
        }

        // Trobar l'índex de la columna
        String capcalera = treballadorsCSV.get(0);
        String[] columnes = capcalera.split(",");
        int indexColumna = -1;
        for (int i = 0; i < columnes.length; i++) {
            if (columnes[i].equalsIgnoreCase(columna)) {
                indexColumna = i;
                break;
            }
        }

        if (indexColumna == -1) {
            throw new IOFitxerExcepcio("Columna " + columna + " no trobada.");
        }

        // Iterar sobre les línies per trobar l'ID
        for (int i = 1; i < treballadorsCSV.size(); i++) {
            String[] dades = treballadorsCSV.get(i).split(",");
            if (dades[0].equals(id)) {
                dades[indexColumna] = nouValor;
                treballadorsCSV.set(i, String.join(",", dades));
                trobat = true;
                break;
            }
        }

        if (trobat) {
            escriureFitxerCSV(treballadorsCSV);
        }

        return trobat;
    }

    // Encapsulació de llegir el fitxer CSV
    private List<String> llegirFitxerCSV() throws IOFitxerExcepcio {
        List<String> treballadorsCSV = UtilsCSV.llegir(filePath);
        if (treballadorsCSV == null) {
            throw new IOFitxerExcepcio("Error en llegir el fitxer: " + filePath);
        }
        return treballadorsCSV;
    }

    // Encapsulació d'escriure el fitxer CSV
    private void escriureFitxerCSV(List<String> treballadorsCSV) throws IOFitxerExcepcio {
        try {
            UtilsCSV.escriure(filePath, treballadorsCSV);
        } catch (Exception e) {
            throw new IOFitxerExcepcio("Error en escriure el fitxer: " + filePath, e);
        }
    }

    // Mètode main
    public static void main(String[] args) {
        PR123mainTreballadors programa = new PR123mainTreballadors();
        programa.iniciar();
    }
}
