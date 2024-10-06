package com.project;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import com.project.excepcions.IOFitxerExcepcio;
import java.io.*;
import java.util.HashMap;
import static org.junit.jupiter.api.Assertions.*;

class PR120mainPersonesHashmapTest {

    @TempDir
    File directoriTemporal;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void testEscriureILlegirPersones() throws IOException, IOFitxerExcepcio {
        File fitxerTemporal = new File(directoriTemporal, "PR120persones.dat");
        HashMap<String, Integer> persones = new HashMap<>();
        persones.put("Anna", 25);
        persones.put("Bernat", 30);
        String filePathAnterior = PR120mainPersonesHashmap.getFilePath();
        PR120mainPersonesHashmap.setFilePath(fitxerTemporal.getAbsolutePath());
        PR120mainPersonesHashmap.escriurePersones(persones);
        PR120mainPersonesHashmap.llegirPersones();
        String output = outContent.toString();
        assertTrue(output.contains("Anna: 25 anys"));
        assertTrue(output.contains("Bernat: 30 anys"));
        PR120mainPersonesHashmap.setFilePath(filePathAnterior);
    }

    @Test
    void testLlegirFitxerInexistent() throws IOException {
        File fitxerInexistent = new File(directoriTemporal, "PR120persones.dat");
        assertFalse(fitxerInexistent.exists());
        String filePathAnterior = PR120mainPersonesHashmap.getFilePath();
        PR120mainPersonesHashmap.setFilePath(fitxerInexistent.getAbsolutePath());
        IOFitxerExcepcio excepcio = assertThrows(IOFitxerExcepcio.class, () -> {
            PR120mainPersonesHashmap.llegirPersones();
        });
        assertTrue(excepcio.getMessage().contains("Error llegint les dades del fitxer"));
        assertNotNull(excepcio.getCause());
        assertTrue(excepcio.getCause() instanceof FileNotFoundException);
        PR120mainPersonesHashmap.setFilePath(filePathAnterior);
    }

    @Test
    void testEscriureFitxerAmbError() throws IOException {
        File fitxerAmbError = new File(directoriTemporal, "PR120persones.dat");
        assertTrue(fitxerAmbError.createNewFile(), "El fitxer no s'ha pogut crear.");
        fitxerAmbError.setWritable(false);
        HashMap<String, Integer> persones = new HashMap<>();
        persones.put("Test", 20);
        String filePathAnterior = PR120mainPersonesHashmap.getFilePath();
        PR120mainPersonesHashmap.setFilePath(fitxerAmbError.getAbsolutePath());
        IOFitxerExcepcio excepcio = assertThrows(IOFitxerExcepcio.class, () -> {
            PR120mainPersonesHashmap.escriurePersones(persones);
        });
        assertTrue(excepcio.getMessage().contains("Error escrivint les dades al fitxer"));
        assertNotNull(excepcio.getCause());
        assertTrue(excepcio.getCause() instanceof IOException);
        PR120mainPersonesHashmap.setFilePath(filePathAnterior);
        fitxerAmbError.setWritable(true);
    }
}
