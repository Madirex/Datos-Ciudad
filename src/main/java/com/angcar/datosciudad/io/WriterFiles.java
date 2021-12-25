package com.angcar.datosciudad.io;

import com.angcar.datosciudad.ProcesamientoDatos;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class WriterFiles {

    /**
     * Escritor de archivo HTML
     * @param value Valor a escribir
     */
    public static void writeFile(String value, String nombreCiudad){
        //Fecha
        LocalDate fecha = LocalDate.now();
        String formatearFecha = "dd-MM-yyyy";

        String path = ProcesamientoDatos.path_destination;
        String fileName = nombreCiudad + "-" + fecha.format(DateTimeFormatter.ofPattern(formatearFecha)) + ".html";

        File file = new File(path + File.separator + fileName);

        try{
            FileUtils.writeStringToFile(file, value);
        }
        catch (IOException e){
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
