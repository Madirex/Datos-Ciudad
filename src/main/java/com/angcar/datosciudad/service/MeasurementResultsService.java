package com.angcar.datosciudad.service;

import com.angcar.datosciudad.ProcesamientoDatos;
import com.angcar.datosciudad.model.Hora;
import com.angcar.datosciudad.model.Magnitud;
import com.angcar.datosciudad.model.Measurement;
import com.angcar.datosciudad.model.resultados.MagnitudeDayData;
import com.angcar.datosciudad.model.resultados.DatosMagnitud;
import com.angcar.datosciudad.model.resultados.DatosMomento;
import com.angcar.datosciudad.model.resultados.ResultadoMediciones;
import com.angcar.datosciudad.util.Utils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;


public class MeasurementResultsService {

    private boolean isPrecipitation;
    private DatosHTML datosHTML;

    private final String CITY_NAME;

    /**
     * COnstructor
     * @param nombreCiudad
     */
    public MeasurementResultsService(String nombreCiudad){
        this.CITY_NAME = nombreCiudad;
        datosHTML = new DatosHTML();
    }

    /**
     * Carga y devuelve el resultado de las mediciones
     * @return
     */
    public ResultadoMediciones cargarResultadosMediciones() {

        //Crear Html StringBuilder
        StringBuilder stringMedicionesData = new StringBuilder();

        ResultadoMediciones resultadoMediciones = new ResultadoMediciones();

        //Agregar valores
        resultadoMediciones.setId(java.util.UUID.randomUUID().toString());
        resultadoMediciones.setCiudad(CITY_NAME);
        Utils.obtenerEstaciones(CITY_NAME).ifPresent(resultadoMediciones::setEstacionesAsociadas);
        resultadoMediciones.setFechaInicio(Utils.obtenerFechaInicioMedicion());
        resultadoMediciones.setFechaFin(Utils.obtenerFechaFinalMedicion());

        //AGREGAR LISTAS DE DATOS DE MEDICIONES (METEO Y CONTAMINA)
        String codigoCiudad;
        if (Utils.obtenerCodeEstacion(CITY_NAME).isPresent()){
            codigoCiudad = Utils.obtenerCodeEstacion(CITY_NAME).get();

            //Filtrar por ciudad pasada por parámetro
            List<Measurement> listaMeteorizacion = Utils.filtrarMeteorizacion(codigoCiudad);
            List<Measurement> listaContaminacion = Utils.filtrarContaminacion(codigoCiudad);

            //Agregar los respectivos filtros
            Predicate<Magnitud> filtroMedicion = (Magnitud m) -> m.getCodeMagnitud() == 83 || m.getCodeMagnitud() == 88
                    || m.getCodeMagnitud() == 89 || m.getCodeMagnitud() == 86 || m.getCodeMagnitud() == 81;

            Predicate<Magnitud> filtroContamina = (Magnitud m) -> m.getCodeMagnitud() != -1;

            stringMedicionesData.append("<h1>Meteorología</h1>\n");
            Optional<List<DatosMagnitud>> listaMeteo = procesarDatosMagnitud(Utils.getMagnMeteo(),
                    listaMeteorizacion,filtroMedicion);
            stringMedicionesData.append(datosHTML.getStringHTMLData());
            datosHTML.resetHTMLData();

            stringMedicionesData.append("<h1>Contaminación</h1>\n");
            Optional<List<DatosMagnitud>> listaContamina = procesarDatosMagnitud(Utils.getMagnContamina(),
                    listaContaminacion, filtroContamina);
            stringMedicionesData.append(datosHTML.getStringHTMLData());
            datosHTML.resetHTMLData();

            //Agregar
            listaMeteo.ifPresent(resultadoMediciones::setDatosMeteo);
            listaContamina.ifPresent(resultadoMediciones::setDatosContamina);

            //Intentar generar HTML
            try {
                datosHTML.generarHtml(this.CITY_NAME,stringMedicionesData);
            } catch (IOException e) {
                System.err.println("No se ha podido generar el HTML.");
            }


            File fileCSS = new File(System.getProperty("user.dir") + File.separator + "src" + File.separator
                    + "main" + File.separator + "resources" + File.separator + "style" + File.separator + "style.css");
            File fileCSSDest = new File(ProcesamientoDatos.path_destination + File.separator + "style" +
                    File.separator + "style.css");

            File fileImg = new File(System.getProperty("user.dir") + File.separator + "src" + File.separator
                    + "main" + File.separator + "resources" + File.separator + "image" + File.separator + "informe.png");
            File fileImgDest = new File(ProcesamientoDatos.path_destination + File.separator + "image" +
                    File.separator + "informe.png");
            try {
                FileUtils.copyFile(fileCSS,fileCSSDest);
                FileUtils.copyFile(fileImg,fileImgDest);
            } catch (IOException e) {
                System.err.println("No se ha podido copiar el CSS y su imagen");
                System.out.println(e);
            }





            //Asignar los respectivos valores
        }else{
            System.err.println("No se ha podido localizar el código de la ciudad");
            System.exit(0);
        }

        return resultadoMediciones;
    }


    /**
     * Procesa los datos de la magnitud
     * @param magnitudLista List<Magnitud>
     * @param listaMeasurement List<Medicion> listaMedicion
     * @param filtro Predicate<Magnitud> filtro
     * @return Optional<List<DatosMagnitud>>
     */
    private Optional<List<DatosMagnitud>> procesarDatosMagnitud(List<Magnitud> magnitudLista, List<Measurement> listaMeasurement, Predicate<Magnitud> filtro) {
        List<DatosMagnitud> listaDatos = new ArrayList<>();

        magnitudLista.stream().filter(filtro).forEach((magnitud) -> {
            int idMagnitud = magnitud.getCodeMagnitud();

            //Filtrar por magnitud
            List<Measurement> listaMediciones;

            if (Utils.obtenerMagnitudLista(idMagnitud, listaMeasurement).isPresent()){
                listaMediciones = Utils.obtenerMagnitudLista(idMagnitud, listaMeasurement).get();

                if (!listaMediciones.isEmpty()){
                    //AGREGAR MAGNITUD
                        DatosMagnitud datosMagnitud = new DatosMagnitud();
                        datosMagnitud.setTipo(magnitud.getDescriptionMagnitude());

                    //Detectar si es precipitación
                    if (datosMagnitud.getTipo().equalsIgnoreCase("Precipitación")){
                        isPrecipitation = true;
                    }else{
                        isPrecipitation = false;
                    }

                    //Agregar media mensual
                        Optional<Double> medicionOpt = MeasurementsService.medicionMedia(listaMediciones);
                        medicionOpt.ifPresent(datosMagnitud::setMedia);

                    //Agregar momentos;
                        Map.Entry<Measurement, Optional<Hora>> mapEntryMax;
                        Map.Entry<Measurement, Optional<Hora>> mapEntryMin;

                    //Agregar momento máximo
                    mapEntryMax =  MeasurementsService.medicionMaximaMomento(listaMediciones);
                        DatosMomento max = new DatosMomento();
                            Utils.obtenerFechaAndHoraMedicion(mapEntryMax).ifPresent(max::setFechaConvert);
                    mapEntryMax.getValue().ifPresent(hora -> max.setValor(hora.getValue()));
                        datosMagnitud.setMaxima(max);

                    //Agregar momento mínimo
                    mapEntryMin =  MeasurementsService.medicionMinimaMomento(listaMediciones);
                        DatosMomento min = new DatosMomento();
                            Utils.obtenerFechaAndHoraMedicion(mapEntryMin).ifPresent(min::setFechaConvert);
                    mapEntryMin.getValue().ifPresent(hora -> min.setValor(hora.getValue()));
                        datosMagnitud.setMinima(min);

                    //Agregar días
                        List<MagnitudeDayData> listaDeDias = new ArrayList<>();


                        if (isPrecipitation) {
                            MeasurementsService.listaDiasPrecipitacion(listaMediciones).forEach((key, value) -> {
                                MagnitudeDayData magnitudeDayData = new MagnitudeDayData();
                                magnitudeDayData.setFecha(key.toString());
                                magnitudeDayData.setValor(value);
                                listaDeDias.add(magnitudeDayData);
                            });
                        }

                    datosMagnitud.setDias(listaDeDias);

                    //Agregar a la lista de datos
                    listaDatos.add(datosMagnitud);

                    //Agregar también al HTML
                    try {
                        datosHTML.agregarDatosMagnitudHtml(isPrecipitation,datosMagnitud.getMedia(),
                                mapEntryMax,mapEntryMin,magnitud, listaMediciones, CITY_NAME);
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.err.println("No se ha podido generar el HTML");
                    }
                }
            }
        }
        );

        if (listaDatos.isEmpty()){
            return Optional.empty();
        }else{
            return Optional.of(listaDatos);
        }
    }


}