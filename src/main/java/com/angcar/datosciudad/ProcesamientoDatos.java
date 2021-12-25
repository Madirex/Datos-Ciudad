package com.angcar.datosciudad;

import com.angcar.datosciudad.io.ConsultasXPATH;
import com.angcar.datosciudad.io.JAXBdbMediciones;
import com.angcar.datosciudad.model.resultados.ResultadoMediciones;
import com.angcar.datosciudad.service.MeasurementResultsService;
import com.angcar.datosciudad.util.Utils;
import org.jdom2.JDOMException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ProcesamientoDatos {
    private final String[] ARGS;
    public static String path_destination;
    private static ProcesamientoDatos procesamientoDatos;

    /**
     * ProcesamientoDatos constructor privado
     * @param argumentos Array de String con los argumentos iniciales del programa
     */
    private ProcesamientoDatos(String[] argumentos){
        Utils.init_time = System.currentTimeMillis();
        this.ARGS = argumentos;
        ejecutarPrograma();
    }

    /**
     * Singleton
     * @param argumentos Argumentos iniciales del programa
     * @return Singleton respuesta
     */
    public static ProcesamientoDatos getInstance(String[] argumentos){
        if (procesamientoDatos == null){
            if (argumentos.length >= 2 && (argumentos.length % 2 == 0)) {
                procesamientoDatos = new ProcesamientoDatos(argumentos);
            } else {
                System.err.println(
                        "Error de sintaxis:\nDebes de utilizar el siguiente formato:\njava -jar datosciudad.jar <ciudad>" +
                                " <directorio>");
            }
        }
        return procesamientoDatos;
    }

    /**
     * Ejecución del programa
     */
    public void ejecutarPrograma() {
        List<String[]> pares = IntStream.iterate(0, i -> i += 2).limit(ARGS.length / 2)
                .mapToObj(n -> new String[]{ARGS[n], ARGS[n + 1]}).collect(Collectors.toList());

        pares.forEach(pair -> {
            String ciudad = pair[0]; //Argumento ciudad
            path_destination = pair[1] + File.separator;

            Utils.createEmptyFolders(path_destination);

            if (Utils.inicializarDatosCSV()) {
                ResultadoMediciones datosResultadoMediciones = new ResultadoMediciones();

                //En su lugar, leer los datos desde el XML
                try {
                    Utils.inicializarDatosXML();
                    MeasurementResultsService res = new MeasurementResultsService(ciudad);
                    datosResultadoMediciones = res.cargarResultadosMediciones();

                } catch (IOException | JDOMException e) {
                    System.err.println("No se ha podido inicializar los datos del XML.");
                    System.exit(0);
                }

                //Una vez recibidos los datos de las mediciones, trabajar con ellos
                //CREAR LA BASE DE DATOS DE MEDICIONES
                JAXBdbMediciones bd = JAXBdbMediciones.getInstance();

                //Intentar generar Base de datos
                try {

                String uri = System.getProperty("user.dir") + File.separator + "src" + File.separator
                        + "main"  + File.separator + "resources" +  File.separator +"data" + File.separator+"db"
                        + File.separator + "mediciones.xml";

                    //Crear base de datos de mediciones
                    ConsultasXPATH.operacionesXpath(ciudad,path_destination,
                            bd.crearBDMediciones(datosResultadoMediciones, uri)
                    );

                    System.out.println("Base de datos XML creada.");

                } catch (JAXBException | ParserConfigurationException e) {
                    System.err.println("No se ha podido crear la base de datos de mediciones.");
                    System.exit(0);
                } catch (XPathExpressionException | IOException e) {
                    System.err.println("No se han podido realizar las consultas con XPATH.");
                }

            } else {
                System.err.println("Los archivos CSV no se han podido leer.");
                System.exit(0);
            }
        });
    }

    /**
     * Mide el tiempo de ejecución del programa y devuelve un informe
     * @return Devuelve cuándo se ha creado el informe y cuánto tiempo ha tardado
     */
    public static String tiempoInforme() {
        double tiempo = (double) (System.currentTimeMillis() - Utils.init_time)/1000;
        LocalDate fecha = LocalDate.now();
        String formatearFecha = "dd/MM/yyyy";
        LocalTime hora = LocalTime.now();
        String formatearHora = "HH:mm:ss";

        return "Informe generado en el día " + fecha.format(DateTimeFormatter.ofPattern(formatearFecha))
                + " a las " + hora.format(DateTimeFormatter.ofPattern(formatearHora))+ " en "+ tiempo + " segundos";
    }
}
