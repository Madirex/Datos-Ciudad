package com.angcar.datosciudad.util;

import com.angcar.datosciudad.io.ReaderFiles;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

public class XMLConvertUtil {

    /**
     * Recoge la cabecera del CSV y se guarda cada nombre de la columna en una lista de String
     * @param path {@link Path}
     * @return ArrayList<String>
     */
    public static ArrayList<String> getTitlesCSV(Path path) {
        ArrayList<String> elementosString = new ArrayList<>();
        try (Stream<String> stream = Files.lines(path, Charset.forName("windows-1252"))) {
            Optional<String[]> elementosArray = stream.map(s -> s.split(";", -1)).findFirst();
            elementosString.addAll(Arrays.asList(elementosArray.get()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return elementosString;
    }

    /**
     * Genera el XML
     * @param nombreArchivo {@link String}
     * @param doc {@link Document}
     * @throws IOException
     */
    public static void generarXML(String nombreArchivo, Document doc) throws IOException {
        XMLOutputter xml = new XMLOutputter();
        xml.setFormat(Format.getPrettyFormat());
        xml.output(doc, new FileWriter(ReaderFiles.PATH_FILES + File.separator + nombreArchivo + ".xml"));
    }

    /**
     * Se crea un elemento item donde se le asignan todos los elementos, devolviendo el elemento item
     * @param elementos ArrayList<String>
     * @param splitted String[]
     * @return Element
     */
    public static Element createXMLItem(ArrayList<String> elementos, String[] splitted) {
        Element contenido = new Element("item");
        for(int n = 0; n < elementos.size(); n++) {
            Element c = new Element(elementos.get(n));
            c.setText(splitted[n]);
            contenido.addContent(c);
        }

        return contenido;
    }

    /**
     * Recibe un nombre de archivo y se le quita la extensión .csv si la encuentra
     * @param nombreArchivo {@link String}
     * @return String
     */
    public static String quitarExtensionCSV(String nombreArchivo) {
        StringBuilder b = new StringBuilder(nombreArchivo);
        b.replace(nombreArchivo.lastIndexOf(".csv"), nombreArchivo.length(), "" );
        return b.toString();
    }
}
