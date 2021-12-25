package com.angcar.datosciudad.io;

import com.angcar.datosciudad.model.*;
import com.angcar.datosciudad.model.Hora;
import com.angcar.datosciudad.util.XMLConvertUtil;
import org.jdom2.Document;
import org.jdom2.Element;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * READERS
 */
public class ReaderFiles {
    // Localización csv´s
    public static final String PATH_FILES = System.getProperty("user.dir") + File.separator + "src" + File.separator +
            "main" + File.separator + "resources" + File.separator + "data";
    private static final String PATH_ZONAS = PATH_FILES + File.separator + "calidad_aire_zonas.csv";
    private static final String PATH_UBICA_ESTACIONES = PATH_FILES + File.separator + "calidad_aire_estaciones.csv";
    private static final String PATH_METEO = PATH_FILES + File.separator + "calidad_aire_datos_meteo_mes.csv";
    private static final String PATH_CONTAMINACION = PATH_FILES + File.separator + "calidad_aire_datos_mes.csv";
    private static final String PATH_MAGNITUDES_CONTAMINACION = PATH_FILES + File.separator + "magnitudes_contaminacion.csv";
    private static final String PATH_MAGNITUDES_METEO = PATH_FILES + File.separator + "magnitudes_meteorizacion.csv";

    /**
     * Leer datos de meteorología
     * @return Opcional que de vuelve una lista de {@link Weathering}
     */
    public static Optional<List<Weathering>> readDataOfPathMeteorologia() {
        Path path = Paths.get(PATH_METEO);
        String nombreArchivo = XMLConvertUtil.quitarExtensionCSV(path.getFileName().toString());

        if (Files.exists(path)) {

            try (Stream<String> stream = Files.lines(path, Charset.forName("windows-1252"))) {

                Element contenidos = new Element(nombreArchivo);
                Document doc = new Document(contenidos);
                ArrayList<String> elementos = XMLConvertUtil.getTitlesCSV(path);


                Optional<List<Weathering>> listaFinal = Optional.of(stream
                        .map(s -> s.split(";", -1)).skip(1)
                        .map(splitted -> {

                            contenidos.addContent(XMLConvertUtil.createXMLItem(elementos, splitted));

                            String provincia = splitted[0];
                            String municipio = splitted[1];
                            String estacion = splitted[2];
                            String magnitud = splitted[3];
                            String punto_muestreo = splitted[4];
                            int ano = Integer.parseInt(splitted[5]);
                            int mes = Integer.parseInt(splitted[6]);
                            int dia = Integer.parseInt(splitted[7]);

                            Hora[] horas = new Hora[24];

                            int actualSplitted = 8;

                            for (int i = 0; i < 24; i++){
                                horas[i] = new Hora(splitted[actualSplitted].replace(',','.')
                                        , splitted[actualSplitted + 1], i + 1);
                                actualSplitted +=2;
                            }

                            return new Weathering(provincia, municipio, estacion, magnitud, punto_muestreo, ano, mes, dia, horas);
                        })
                        .collect(Collectors.toList()));

                XMLConvertUtil.generarXML(nombreArchivo,doc); //Generar el XML
                return listaFinal;

            } catch (IOException ex) {
                System.err.println(ex.getMessage());
                return Optional.empty();
            }

        } else {
            return Optional.empty();
        }
    }

    /**
     * Leer datos de contaminación
     * @return Opcional que de vuelve una lista de {@link Contamina}
     */
    public static Optional<List<Contamina>> readDataOfPathContaminacion() {
        Path path = Paths.get(PATH_CONTAMINACION);
        String nombreArchivo = XMLConvertUtil.quitarExtensionCSV(path.getFileName().toString());

        if (Files.exists(path)) {
            try (Stream<String> stream = Files.lines(path, Charset.forName("windows-1252"))) {


                Element contenidos = new Element(nombreArchivo);
                Document doc = new Document(contenidos);
                ArrayList<String> elementos = XMLConvertUtil.getTitlesCSV(path);


                Optional<List<Contamina>> listaFinal =  Optional.of(
                        stream
                        .map(s -> s.split(";", -1)).skip(1)
                        .map(splitted -> {

                            contenidos.addContent(XMLConvertUtil.createXMLItem(elementos, splitted));

                            String provincia = splitted[0];
                            String municipio = splitted[1];
                            String estacion = splitted[2];
                            String magnitud = splitted[3];
                            String punto_muestreo = splitted[4];
                            int ano = Integer.parseInt(splitted[5]);
                            int mes = Integer.parseInt(splitted[6]);
                            int dia = Integer.parseInt(splitted[7]);

                            Hora[] horas = new Hora[24];

                            int actualSplitted = 8;
                            for (int i = 0; i < 24; i++){
                                horas[i] = new Hora(splitted[actualSplitted].replace(',','.')
                                        , splitted[actualSplitted + 1], i + 1);
                                actualSplitted +=2;
                            }

                            return new Contamina(provincia, municipio, estacion, magnitud, punto_muestreo, ano, mes, dia, horas);

                        })
                        .collect(Collectors.toList()));
                XMLConvertUtil.generarXML(nombreArchivo,doc); //Generar el XML
                return listaFinal;
            } catch (IOException ex) {
                System.err.println(ex.getMessage());
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }

    /**
     * Leer datos de ubicación de estaciones
     * @return Opcional que de vuelve una lista de {@link UbicacionEstaciones}
     */
    public static Optional<List<UbicacionEstaciones>> readDataOfPathUbicacionEstaciones() {
        Path path = Paths.get(PATH_UBICA_ESTACIONES);
        String nombreArchivo = XMLConvertUtil.quitarExtensionCSV(path.getFileName().toString());

        if (Files.exists(path)) {
            try (Stream<String> stream = Files.lines(path, Charset.forName("windows-1252"))) {


                Element contenidos = new Element(nombreArchivo);
                Document doc = new Document(contenidos);
                ArrayList<String> elementos = XMLConvertUtil.getTitlesCSV(path);


                Optional<List<UbicacionEstaciones>> listaFinal =  Optional.of(stream
                        .map(s -> s.split(";", -1)).skip(1)
                        .map(splitted -> {

                            contenidos.addContent(XMLConvertUtil.createXMLItem(elementos, splitted));

                            String estacion_codigo = splitted[0];
                            String zona_calidad_aire_descripcion = splitted[1];
                            String estacion_municipio = splitted[2];
                            String estacion_fecha_alta = splitted[3];
                            String estacion_tipo_area = splitted[4];
                            String estacion_tipo_estacion = splitted[5];
                            String estacion_subarea_rural = splitted[6];
                            String estacion_direccion_postal = splitted[7];
                            String estacion_coord_UTM_ETRS89_x = splitted[8];
                            String estacion_coord_UTM_ETRS89_y = splitted[9];
                            String estacion_coord_longitud = splitted[10];
                            String estacion_coord_latitud = splitted[11];
                            String estacion_altitud = splitted[12];
                            String estacion_analizador_NO = splitted[13];
                            String estacion_analizador_NO2 = splitted[14];
                            String estacion_analizador_PM10 = splitted[15];
                            String estacion_analizador_PM2_5 = splitted[16];
                            String estacion_analizador_O3 = splitted[17];
                            String estacion_analizador_TOL = splitted[18];
                            String estacion_analizador_BEN = splitted[19];
                            String estacion_analizador_XIL = splitted[20];
                            String estacion_analizador_CO = splitted[21];
                            String estacion_analizador_SO2 = splitted[22];
                            String estacion_analizador_HCT = splitted[23];
                            String estacion_analizador_HNM = splitted[24];


                            return new UbicacionEstaciones(
                                    estacion_codigo
                                    , zona_calidad_aire_descripcion
                                    , estacion_municipio
                                    , estacion_fecha_alta
                                    , estacion_tipo_area
                                    , estacion_tipo_estacion
                                    , estacion_subarea_rural
                                    , estacion_direccion_postal
                                    , estacion_coord_UTM_ETRS89_x
                                    , estacion_coord_UTM_ETRS89_y
                                    , estacion_coord_longitud
                                    , estacion_coord_latitud
                                    , estacion_altitud
                                    , estacion_analizador_NO
                                    , estacion_analizador_NO2
                                    , estacion_analizador_PM10
                                    , estacion_analizador_PM2_5
                                    , estacion_analizador_O3
                                    , estacion_analizador_TOL
                                    , estacion_analizador_BEN
                                    , estacion_analizador_XIL
                                    , estacion_analizador_CO
                                    , estacion_analizador_SO2
                                    , estacion_analizador_HCT
                                    , estacion_analizador_HNM
                            );
                        })
                        .collect(Collectors.toList()));
                XMLConvertUtil.generarXML(nombreArchivo,doc); //Generar el XML
                return listaFinal;
            } catch (IOException ex) {
                System.err.println(ex.getMessage());
                return Optional.empty();
            }
        }
        else{
            return Optional.empty();
        }

    }

    /**
     * Leer datos de zonas de municipio
     * @return Opcional que de vuelve una lista de {@link ZonasMunicipio}
     */
    public static Optional<List<ZonasMunicipio>> readDataOfPathZonasMunicipio() {

        Path path = Paths.get(PATH_ZONAS);
        String nombreArchivo = XMLConvertUtil.quitarExtensionCSV(path.getFileName().toString());

        if (Files.exists(path)) {
            try (Stream<String> stream = Files.lines(path, Charset.forName("windows-1252"))) {


                Element contenidos = new Element(nombreArchivo);
                Document doc = new Document(contenidos);
                ArrayList<String> elementos = XMLConvertUtil.getTitlesCSV(path);

                Optional<List<ZonasMunicipio>> listaFinal =  Optional.of(stream
                        .map(s -> s.split(";", -1)).skip(1)
                        .map(splitted -> {

                            contenidos.addContent(XMLConvertUtil.createXMLItem(elementos, splitted));

                            String zonaCalidadAireCodigo = splitted[0];
                            String zonaCalidadAireDesc = splitted[1];
                            String zonaCalidadAireMunicipio = splitted[2];

                            return new ZonasMunicipio(zonaCalidadAireCodigo,
                                    zonaCalidadAireDesc, zonaCalidadAireMunicipio);

                        })
                        .collect(Collectors.toList()));
                XMLConvertUtil.generarXML(nombreArchivo,doc); //Generar el XML
                return listaFinal;
            } catch (IOException ex) {
                System.err.println(ex.getMessage());
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }

    /**
     * Leer datos de contaminación
     * @return Opcional que de vuelve una lista de {@link Magnitud}
     */
    public static Optional<List<Magnitud>> readDataOfPathMagnitudContaminacion() {

        Path path = Paths.get(PATH_MAGNITUDES_CONTAMINACION);
        String nombreArchivo = XMLConvertUtil.quitarExtensionCSV(path.getFileName().toString());

        if (Files.exists(path)) {
            try (Stream<String> stream = Files.lines(path, Charset.forName("windows-1252"))) {


                Element contenidos = new Element(nombreArchivo);
                Document doc = new Document(contenidos);
                ArrayList<String> elementos = XMLConvertUtil.getTitlesCSV(path);


                Optional<List<Magnitud>> listaFinal =  Optional.of(stream
                        .map(s -> s.split(";", -1)).skip(1)
                        .map(splitted -> {

                            contenidos.addContent(XMLConvertUtil.createXMLItem(elementos, splitted));

                            int codigo_magnitud = Integer.parseInt(splitted[0]);
                            String descripcion_magnitud = splitted[1];
                            int codigo_tecnica_medida = Integer.parseInt(splitted[2]);
                            String unidad = splitted[3];
                            String descripcion_unidad = splitted[4];
                            String descripcion_tecnica_medida = splitted[5];

                            return new Magnitud(codigo_magnitud,
                                    descripcion_magnitud, codigo_tecnica_medida,
                                    descripcion_tecnica_medida, unidad, descripcion_unidad);

                        })
                        .collect(Collectors.toList()));
                XMLConvertUtil.generarXML(nombreArchivo,doc); //Generar el XML
                return listaFinal;
            } catch (IOException ex) {
                System.err.println(ex.getMessage());
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }

    /**
     * Leer datos magnitud meteorización
     * @return Opcional que de vuelve una lista de {@link Magnitud}
     */
    public static Optional<List<Magnitud>> readDataOfPathMagnitudMeteorizacion() {

        Path path = Paths.get(PATH_MAGNITUDES_METEO);
        String nombreArchivo = XMLConvertUtil.quitarExtensionCSV(path.getFileName().toString());

        if (Files.exists(path)) {
            try (Stream<String> stream = Files.lines(path, Charset.forName("windows-1252"))) {


                Element contenidos = new Element(nombreArchivo);
                Document doc = new Document(contenidos);
                ArrayList<String> elementos = XMLConvertUtil.getTitlesCSV(path);


                Optional<List<Magnitud>> listaFinal =  Optional.of(stream
                        .map(s -> s.split(";", -1)).skip(1)
                        .map(splitted -> {

                            contenidos.addContent(XMLConvertUtil.createXMLItem(elementos, splitted));

                            int codigo_magnitud = Integer.parseInt(splitted[0]);
                            String descripcion_magnitud = splitted[1];
                            int codigo_tecnica_medida = Integer.parseInt(splitted[2]);
                            String unidad = splitted[3];
                            String descripcion_unidad = splitted[4];

                            return new Magnitud(codigo_magnitud,
                                    descripcion_magnitud, codigo_tecnica_medida, unidad,
                                    descripcion_unidad, null);
                        })
                        .collect(Collectors.toList()));
                XMLConvertUtil.generarXML(nombreArchivo,doc); //Generar el XML
                return listaFinal;
            } catch (IOException ex) {
                System.err.println(ex.getMessage());
                return Optional.empty();
            }
        }
        else{
            return Optional.empty();
        }
    }
}