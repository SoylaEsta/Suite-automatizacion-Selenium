package com.proyecto.selenium.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CapturaPantallaUtil {

    public static void capturar(WebDriver driver, String nombreArchivo) {
        TakesScreenshot ts = (TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);

        // Crea el nombre del archivo con marca de tiempo
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String nombreFinal = nombreArchivo + "_" + timeStamp + ".png";

        // Carpeta donde se guardarán las capturas
        Path destino = Paths.get("screenshots", nombreFinal);

        try {
            Files.createDirectories(destino.getParent()); // asegura que exista la carpeta
            Files.copy(source.toPath(), destino);
            System.out.println(" Captura guardada en: " + destino.toAbsolutePath());
        } catch (IOException e) {
            System.err.println(" Error al guardar captura: " + e.getMessage());
        }
    }
}
