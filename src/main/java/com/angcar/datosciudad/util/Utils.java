package com.angcar.datosciudad.util;

import com.angcar.datosciudad.io.JDOMReader;
import com.angcar.datosciudad.io.ReaderFiles;
import com.angcar.datosciudad.model.*;
import org.jdom2.JDOMException;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class Utils {
    private static List<UbicacionEstaciones> estacionesUbi;
    private static List<Contamina> contamina;
    private static List<Weathering> meteo;
    private static List<ZonasMunicipio> municipio;
    private static List<Magnitud> magnContamina;
    private static List<Magnitud> magnMeteo;
    static public long init_time;

    public static List<Magnitud> getMagnContamina() {
        return magnContamina;
    }
    public static List<Magnitud> getMagnMeteo() {
        return magnMeteo;
    }


    /**
     * Carga e inicializa los CSV's
     */
    public static boolean inicializarDatosCSV(){
        //Leer .csv's
        AtomicBoolean realizado = new AtomicBoolean(true);

        ReaderFiles.readDataOfPathUbicacionEstaciones().ifPresentOrElse(
                ubicacionEstaciones -> estacionesUbi = ubicacionEstaciones,
                () -> {System.err.println("No se ha localizado el csv de Estaciones");
            realizado.set(false);
        });

        ReaderFiles.readDataOfPathContaminacion().ifPresentOrElse(
                contaminacion -> contamina = contaminacion,
                () -> {System.err.println("No se ha localizado el csv de contaminación");
            realizado.set(false);
        });

        ReaderFiles.readDataOfPathMeteorologia().ifPresentOrElse(
                meteorologia -> meteo = meteorologia,
                () -> {System.err.println("No se ha localizado el csv de meteorología");
            realizado.set(false);
        });

        ReaderFiles.readDataOfPathMagnitudContaminacion().ifPresentOrElse(
                magnitudContamina -> magnContamina = magnitudContamina,
                () -> {System.err.println("No se ha localizado el csv de magnitudes de contaminación");
            realizado.set(false);
        });

        ReaderFiles.readDataOfPathMagnitudMeteorizacion().ifPresentOrElse(
                magnitudMeteo -> magnMeteo = magnitudMeteo,
                () -> {System.err.println("No se ha localizado el csv de magnitudes de meteorización");
            realizado.set(false);
        });

        ReaderFiles.readDataOfPathZonasMunicipio().ifPresentOrElse(
                munic -> municipio = munic,
                () -> {System.err.println("No se ha localizado el csv de magnitudes de meteorización");
                    realizado.set(false);
                });

        return realizado.get();
    }

    /**
     * Inicializar los datos de XML
     * @throws IOException excepción IO
     * @throws JDOMException excepción JDOM
     */
    public static void inicializarDatosXML() throws IOException, JDOMException {
        //Leer .xml's
        JDOMReader controller = JDOMReader.getInstance();
        AtomicBoolean realizado = new AtomicBoolean(true);
        controller.loadData();

        controller.getZonas().ifPresentOrElse(d -> d = municipio, () -> {
            System.out.println("No se ha localizado el xml de municipio.");
            realizado.set(false);
        });;

        controller.getUbicaciones().ifPresentOrElse(d -> d = estacionesUbi, () -> {
            System.out.println("No se ha localizado el xml de estaciones.");
            realizado.set(false);
        });;

        controller.getMeteorizacion().ifPresentOrElse(d -> d = meteo, () -> {
            System.out.println("No se ha localizado el xml de meteorización.");
            realizado.set(false);
        });;

        controller.getContaminacion().ifPresentOrElse(d -> d = contamina, () -> {
            System.out.println("No se ha localizado el xml de contaminación.");
            realizado.set(false);
        });;

        controller.getMagnitudContaminacion().ifPresentOrElse(d -> d = magnContamina, () -> {
            System.out.println("No se ha localizado el xml de magnitud de contaminación.");
            realizado.set(false);
        });;

        controller.getMagnitudMeteorizacion().ifPresentOrElse(d -> d = magnMeteo, () -> {
            System.out.println("No se ha localizado el xml de magnitud de meteorización.");
            realizado.set(false);
        });;

    }

    /**
     * Obtiene una lista de las magnitudes según el nombre de la magnitud dada una lista
     * @param idMagnitud La ID de la magnitud
     * @param lista Lista de mediciones
     * @return Optional con una lista de mediciones
     */
    public static Optional<List<Measurement>> obtenerMagnitudLista(int idMagnitud, List<Measurement> lista){
        return Optional.of(lista.stream().filter(nombreMagn ->
                        nombreMagn.getMagnitude().equalsIgnoreCase(String.valueOf(idMagnitud)))
                .collect(Collectors.toList()));

    }

    /**
     * Filtra por ciudad
     * @param nombreCiudad El nombre de la ciudad
     * @return Optional con una lista de mediciones
     */
    public static Optional<List<UbicacionEstaciones>> filtrarPorCiudad(String nombreCiudad) {
        return Optional.of(estacionesUbi.stream().filter(ubicacionEstaciones ->
                ubicacionEstaciones.getStationMunicipal().equalsIgnoreCase(nombreCiudad)).collect(Collectors.toList()));
    }

    /**
     * Filtrar por meteorización
     * @param codigoCiudad {@link String}
     * @return Lista de mediciones
     */
    public static List<Measurement> filtrarMeteorizacion(String codigoCiudad) {
        return meteo.stream().filter(punto_muestreo -> punto_muestreo.getSamplingPoint()
                .contains(codigoCiudad)).collect(Collectors.toList());
    }

    /**
     * Filtrar por contaminación
     * @param codigoCiudad {@link String}
     * @return Lista de contaminaciones
     */
    public static List<Measurement> filtrarContaminacion(String codigoCiudad) {
        return contamina.stream().filter(punto_muestreo -> punto_muestreo.getSamplingPoint()
                .contains(codigoCiudad)).collect(Collectors.toList());
    }

    /**
     * Obtiene la fecha de inicio
     * @return LocalDate
     */
    public static String obtenerFechaInicioMedicion(){
        String formatearFecha = "dd/MM/yyyy - 00:00:00";
        Optional<LocalDate> fecha = meteo.stream().min(Comparator.comparingInt(Measurement::getDay))
                .map(s -> LocalDate.of(s.getYear(), s.getMonth(), s.getDay()));

        Optional<LocalDate> fecha2 = contamina.stream().min(Comparator.comparingInt(Measurement::getDay))
                .map(s -> LocalDate.of(s.getYear(), s.getMonth(), s.getDay()));

        if (fecha.isPresent() && fecha2.isPresent()){
            if (fecha.get().equals(fecha2.get())) {
                return fecha.get().format(DateTimeFormatter.ofPattern(formatearFecha));
            }else if(fecha.get().getDayOfMonth() < fecha2.get().getDayOfMonth())
                return fecha.get().format(DateTimeFormatter.ofPattern(formatearFecha));
            else {
                return fecha2.get().format(DateTimeFormatter.ofPattern(formatearFecha));
            }
        }
        else{
            return "";
        }


    }

    /**
     * Obtiene la fecha final
     * @return LocalDate
     */
    public static String obtenerFechaFinalMedicion(){
        String formatearFecha = "dd/MM/yyyy - 00:00:00";

        Optional<LocalDate> fecha = meteo.stream().max(Comparator.comparingInt(Measurement::getDay))
                .map(s -> LocalDate.of(s.getYear(), s.getMonth(), s.getDay()));

        Optional<LocalDate> fecha2 = contamina.stream().max(Comparator.comparingInt(Measurement::getDay))
                .map(s -> LocalDate.of(s.getYear(), s.getMonth(), s.getDay()));

        if (fecha.isPresent() && fecha2.isPresent()){
            if (fecha.get().equals(fecha2.get())) {
                return fecha.get().format(DateTimeFormatter.ofPattern(formatearFecha));
            }else if(fecha.get().getDayOfMonth() > fecha2.get().getDayOfMonth())
                return fecha.get().format(DateTimeFormatter.ofPattern(formatearFecha));
            else {
                return fecha2.get().format(DateTimeFormatter.ofPattern(formatearFecha));
            }
        }
        else{
            return "";
        }
    }


    /**
     * Obtiene la media diaria de una medición
     * @param measurement {@link Measurement}
     * @return double con la media diaria
     */
    public static double obtenerMediaDiaria(Measurement measurement){
            double media = Utils.obtenerHorasValidadas(measurement.getHours())
                    .stream()
                    .mapToDouble(value -> Double.parseDouble(value.getValue()))
                    .summaryStatistics().getAverage();

            return Math.round(media * 100d) / 100d;
    }

    /**
     * Obtener código de la estación principal dada una ciudad
     * @param nombreCiudad {@link String}
     * @return String
     */
    public static String obtenerCodigo(String nombreCiudad) {
        Optional<UbicacionEstaciones> estacion = estacionesUbi.stream().filter(ubicacionEstaciones ->
                ubicacionEstaciones.getStationMunicipal().equalsIgnoreCase(nombreCiudad)).findFirst();

        String name = "";
        if (estacion.isPresent()) name = estacion.get().getStationCode();
        return name;
    }

    /**
     * Obtener estaciones
     * @param ciudad {@link String}
     * @return Optional con una lista de las estaciones en String
     */
    public static Optional<List<String>> obtenerEstaciones(String ciudad) {

        String codigo = obtenerCodigo(ciudad);

        List<UbicacionEstaciones> estacion = estacionesUbi.stream().filter(ubicacionEstaciones ->
                        codigo.substring(6).equals(ubicacionEstaciones.getStationCode().substring(6)))
                .collect(Collectors.toList());

       return Optional.of(estacion.stream().map(UbicacionEstaciones::getStationMunicipal).collect(Collectors.toList()));

    }

    /**
     * Obtiene las horas validadas
     * @param horas Array de horas
     * @return Lista de horas
     */
    public static List<Hora> obtenerHorasValidadas(Hora[] horas){
        return Arrays.stream(horas).filter(hora -> hora.getValidation().equals("V"))
                .collect(Collectors.toList());
    }

    public static Optional<String> obtenerCodeEstacion(String nombreCiudad){
        Optional<List<UbicacionEstaciones>> listaEstaciones = Utils.filtrarPorCiudad(nombreCiudad);
        Optional <String> codigoCiudad = Optional.empty();

        if (Utils.filtrarPorCiudad(nombreCiudad).isPresent()){
            codigoCiudad = Optional.of(Utils.filtrarPorCiudad(nombreCiudad).get().get(0).getStationCode());
        }

        return codigoCiudad;
    }

    /**
     * Obtiene la fecha de una medición
     * @param measurement {@link Measurement}
     * @return fecha de medición
     */
    public static LocalDate obtenerFechaMedicion(Measurement measurement){
        return LocalDate.of(measurement.getYear(), measurement.getMonth(), measurement.getDay());
    }

    /**
     * Dada una hora, obtiene un LocalTime
     * @param value
     * @return
     */
    public static Optional<LocalTime> obtenerHoraMedicion(Optional<Hora> value) {
        return value.map(hora -> LocalTime.of(hora.getNumHour(), 0));
    }

    /**
     * Obtener la fecha y la hora dada una medición
     * @param mapEntry
     * @return
     */
    public static Optional<LocalDateTime> obtenerFechaAndHoraMedicion(Map.Entry<Measurement, Optional<Hora>> mapEntry) {
        LocalDate date;
        LocalTime time;

        if (Utils.obtenerHoraMedicion(mapEntry.getValue()).isPresent()){
            return Optional.of(LocalDateTime.of(Utils.obtenerFechaMedicion(mapEntry.getKey()),
                    Utils.obtenerHoraMedicion(mapEntry.getValue()).get()));
        }

        return Optional.empty();
    }

    /**
     * Crea las carpetas necesarias si no existen
     */
    public static void createEmptyFolders(String path_destination) {

        //Crear directorio inicial si no existe + directorio image
        File directory = new File(path_destination + File.separator + "image");
        while (!directory.exists()){
            if (directory.mkdirs()){
                System.out.println("Directorio " + directory.getPath() + " creado");
            };
        }

        //Crear directorio db si no existe
        directory = new File(System.getProperty("user.dir") + File.separator + "src" + File.separator
                + "main"  + File.separator + "resources" +  File.separator +"data" + File.separator+"db");

        while (!directory.exists()){
            if (directory.mkdirs()){
                System.out.println("Carpeta 'db' creada");
            };
        }

        //Crear directorio style si no existe
        directory = new File(path_destination + File.separator + "style");

        while (!directory.exists()){
            if (directory.mkdirs()){
                System.out.println("Carpeta 'style' creada");
            };
        }
    }
}
