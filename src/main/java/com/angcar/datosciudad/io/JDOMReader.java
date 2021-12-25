package com.angcar.datosciudad.io;

import com.angcar.datosciudad.model.*;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.Document;
import org.jdom2.Element;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JDOMReader {
    private static JDOMReader controller;
    public static final String PATH_FILES = System.getProperty("user.dir") + File.separator + "src" + File.separator
            + "main"  + File.separator + "resources" +  File.separator +"data";
    private static final String PATH_ZONAS = PATH_FILES + File.separator + "calidad_aire_zonas.xml";
    private static final String PATH_UBICA_ESTACIONES = PATH_FILES + File.separator + "calidad_aire_estaciones.xml";
    private static final String PATH_METEO = PATH_FILES + File.separator + "calidad_aire_datos_meteo_mes.xml";
    private static final String PATH_CONTAMINA = PATH_FILES + File.separator + "calidad_aire_datos_mes.xml";
    private static final String PATH_MAGNITUDES_CONTAMINA = PATH_FILES + File.separator + "magnitudes_contaminacion.xml";
    private static final String PATH_MAGNITUDES_METEO = PATH_FILES + File.separator + "magnitudes_meteorizacion.xml";
    private Document dataZonas;
    private Document dataUbicaciones;
    private Document dataMeteo;
    private Document dataContamina;
    private Document dataMagContamina;
    private Document dataMagMeteo;

    private JDOMReader() {
    }

    /**
     * JDOMReader
     * @return JDOMReader
     */
    public static JDOMReader getInstance() {
        if (controller == null) {
            controller = new JDOMReader();
        }
        return controller;
    }

    /**
     * Carga los datos
     * @throws IOException Excepción
     * @throws JDOMException Excepción
     */
    public void loadData() throws IOException, JDOMException {
        SAXBuilder builder = new SAXBuilder();
        File zonasXML = new File(PATH_ZONAS);
        File ubicacionesXML = new File(PATH_UBICA_ESTACIONES);
        File meteoXML = new File(PATH_METEO);
        File contaminaXML = new File(PATH_CONTAMINA);
        File magContaminaXML = new File(PATH_MAGNITUDES_CONTAMINA);
        File magMeteoXML = new File(PATH_MAGNITUDES_METEO);
        this.dataZonas = builder.build(zonasXML);
        this.dataUbicaciones = builder.build(ubicacionesXML);
        this.dataMeteo = builder.build(meteoXML);
        this.dataContamina = builder.build(contaminaXML);
        this.dataMagContamina = builder.build(magContaminaXML);
        this.dataMagMeteo = builder.build(magMeteoXML);
    }

    /**
     * Devuelve una lista Optional con las zonas del municipio
     * @return Optional<List<ZonasMunicipio>>
     */
    public Optional<List<ZonasMunicipio>> getZonas() {
        Element root = this.dataZonas.getRootElement();
        List<Element> listOfZonas = root.getChildren("item");

        List<ZonasMunicipio> zonasList = new ArrayList<>();

        listOfZonas.forEach(zona -> {
            ZonasMunicipio zonas_m = new ZonasMunicipio();
            zonas_m.setAirCodeQualityZone(zona.getChildText("zona_calidad_aire_codigo"));
            zonas_m.setMunicipalAirQualityZone(zona.getChildText("zona_calidad_aire_descripcion"));
            zonas_m.setMunicipalAirQualityZone(zona.getChildText("zona_calidad_aire_municipio"));
            zonasList.add(zonas_m);
        });
        return Optional.of(zonasList);
    }

    /**
     * Devuelve una lista Optional de Ubicación estaciones
     * @return Optional<List<UbicacionEstaciones>>
     */
    public Optional<List<UbicacionEstaciones>> getUbicaciones() {
        Element root = this.dataUbicaciones.getRootElement();
        List<Element> listOfUbic = root.getChildren("item");
        List<UbicacionEstaciones> listUbica = new ArrayList<>();

        listOfUbic.forEach(ubica ->  {
            UbicacionEstaciones ubicacionEstaciones = new UbicacionEstaciones();
            ubicacionEstaciones.setStationCode(ubica.getChildText("estacion_codigo"));
            ubicacionEstaciones.setAirQualityZoneDescription(ubica.getChildText("zona_calidad_aire_descripcion"));
            ubicacionEstaciones.setStationMunicipal(ubica.getChildText("estacion_municipio"));
            ubicacionEstaciones.setHighDateStation(ubica.getChildText("estacion_fecha_alta"));
            ubicacionEstaciones.setAreaTypeStation(ubica.getChildText("estacion_tipo_area"));
            ubicacionEstaciones.setStationTypeStation(ubica.getChildText("estacion_tipo_estacion"));
            ubicacionEstaciones.setRuralSubareaStation(ubica.getChildText("estacion_subarea_rural"));
            ubicacionEstaciones.setPostalAddressStation(ubica.getChildText("estacion_direccion_postal"));
            ubicacionEstaciones.setStationCoordUTMETRS89x(ubica.getChildText("estacion_coord_UTM_ETRS89_x"));
            ubicacionEstaciones.setStationCoordUTMETRS89y(ubica.getChildText("estacion_coord_UTM_ETRS89_y"));
            ubicacionEstaciones.setStationCoordLength(ubica.getChildText("estacion_coord_longitud"));
            ubicacionEstaciones.setLatitudeCoordStation(ubica.getChildText("estacion_coord_latitud"));
            ubicacionEstaciones.setStationAltitude(ubica.getChildText("estacion_altitud"));
            ubicacionEstaciones.setNoAnalyzerStation(ubica.getChildText("estacion_analizador_NO"));
            ubicacionEstaciones.setNO2AnalyzerStation(ubica.getChildText("estacion_analizador_NO2"));
            ubicacionEstaciones.setAnalyzerStationPM10(ubica.getChildText("estacion_analizador_PM10"));
            ubicacionEstaciones.setAnalyzerStationPM25(ubica.getChildText("estacion_analizador_PM2_5"));
            ubicacionEstaciones.setO3AnalyzerStation(ubica.getChildText("estacion_analizador_O3"));
            ubicacionEstaciones.setTolAnalyzerStation(ubica.getChildText("estacion_analizador_TOL"));
            ubicacionEstaciones.setBenAnalyzerStation(ubica.getChildText("estacion_analizador_BEN"));
            ubicacionEstaciones.setXilAnalyzerStation(ubica.getChildText("estacion_analizador_XIL"));
            ubicacionEstaciones.setCoAnalyzerStation(ubica.getChildText("estacion_analizador_CO"));
            ubicacionEstaciones.setSo2AnalyzerStation(ubica.getChildText("estacion_analizador_SO2"));
            ubicacionEstaciones.setHctAnalyzerStation(ubica.getChildText("estacion_analizador_HCT"));
            ubicacionEstaciones.setHnmAnalyzerStation(ubica.getChildText("estacion_analizador_HNM"));
            listUbica.add(ubicacionEstaciones);
        });
        return Optional.of(listUbica);
    }

    /**
     * Devuelve una lista Optional de Meteorización
     * @return Optional<List<Meteorizacion>>
     */
    public Optional<List<Weathering>> getMeteorizacion() {
        Element root = this.dataMeteo.getRootElement();
        List<Element> listOfMeteo = root.getChildren("item");
        List<Weathering> listMeteo = new ArrayList<>();

        listOfMeteo.forEach(meteo -> {
            Weathering weathering = new Weathering();
            weathering.setProvincial(meteo.getChildText("provincia"));
            weathering.setMunicipal(meteo.getChildText("municipio"));
            weathering.setStation(meteo.getChildText("estacion"));
            weathering.setMagnitude(meteo.getChildText("magnitud"));
            weathering.setSamplingPoint(meteo.getChildText("punto_muestreo"));
            weathering.setYear(Integer.parseInt(meteo.getChildText("ano")));
            weathering.setMonth(Integer.parseInt(meteo.getChildText("mes")));
            weathering.setDay(Integer.parseInt(meteo.getChildText("dia")));
            Hora[] horas = new Hora[24];
            for(int n = 0; n < 24; n++){
                horas[n] = new Hora(meteo.getChildText("h" + String.format("%02d", n + 1)),
                        meteo.getChildText("v" + String.format("%02d", n + 1)),n + 1);
            }
            weathering.setHours(horas);
            listMeteo.add(weathering);
        });
        return Optional.of(listMeteo);
    }

    /**
     * Devuelve una lista Optional de Contaminación
     * @return Optional<List<Contaminacion>>
     */
    public Optional<List<Contamina>> getContaminacion() {
        Element root = this.dataContamina.getRootElement();
        List<Element> listOfContamina = root.getChildren("item");
        List<Contamina> listContamina = new ArrayList<>();

        listOfContamina.forEach(cont -> {
            Contamina contamina = new Contamina();
            contamina.setProvincial(cont.getChildText("provincia"));
            contamina.setMunicipal(cont.getChildText("municipio"));
            contamina.setStation(cont.getChildText("estacion"));
            contamina.setMagnitude(cont.getChildText("magnitud"));
            contamina.setSamplingPoint(cont.getChildText("punto_muestreo"));
            contamina.setYear(Integer.parseInt(cont.getChildText("ano")));
            contamina.setMonth(Integer.parseInt(cont.getChildText("mes")));
            contamina.setDay(Integer.parseInt(cont.getChildText("dia")));
            Hora[] horas = new Hora[24];
            for(int n = 0; n < 24; n++){
                horas[n] = new Hora(cont.getChildText("h" + String.format("%02d", n + 1)),
                        cont.getChildText("v" + String.format("%02d", n + 1)),n + 1);
            }
            contamina.setHours(horas);
            listContamina.add(contamina);
        });
        return Optional.of(listContamina);
    }

    /**
     * Devuelve una lista Optional de Meteorización
     * @return Optional<List<Magnitud>>
     */
    public Optional<List<Magnitud>> getMagnitudMeteorizacion() {
        Element root = this.dataMagMeteo.getRootElement();
        List<Element> listOfMagMeteo = root.getChildren("item");
        List<Magnitud> listMagMeteo = new ArrayList<>();

        listOfMagMeteo.forEach(mag -> {
            MagnitudMeteo magnitudMeteo = new MagnitudMeteo();
            magnitudMeteo.setCodeMagnitud(Integer.parseInt(mag.getChildText("cod_magnitud")));
            magnitudMeteo.setDescriptionMagnitude(mag.getChildText("desc_magnitud"));
            magnitudMeteo.setCodeTechnicalMeasure(Integer.parseInt(mag.getChildText("cod_tec_medida")));
            magnitudMeteo.setUnidad(mag.getChildText("unidad"));
            magnitudMeteo.setDescriptionsUnidad(mag.getChildText("desc_unidad"));
            listMagMeteo.add(magnitudMeteo);
        });
        return Optional.of(listMagMeteo);
    }

    /**
     * Devuelve una lista Optional de Contaminación
     * @return Optional<List<Magnitud>>
     */
    public Optional<List<Magnitud>> getMagnitudContaminacion() {
        Element root = this.dataMagContamina.getRootElement();
        List<Element> listOfMagMeteo = root.getChildren("item");
        List<Magnitud> listMagMeteo = new ArrayList<>();

        listOfMagMeteo.forEach(mag -> {
            MagnitudContamina magnitudContamina = new MagnitudContamina();
            magnitudContamina.setCodeMagnitud(Integer.parseInt(mag.getChildText("cod_magnitud")));
            magnitudContamina.setDescriptionMagnitude(mag.getChildText("desc_magnitud"));
            magnitudContamina.setCodeTechnicalMeasure(Integer.parseInt(mag.getChildText("cod_tec_medida")));
            magnitudContamina.setUnidad(mag.getChildText("unidad"));
            magnitudContamina.setDescriptionsUnidad(mag.getChildText("desc_unidad"));
            listMagMeteo.add(magnitudContamina);
        });
        return Optional.of(listMagMeteo);
    }
}