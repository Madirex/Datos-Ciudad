package com.angcar.datosciudad.service;

import com.angcar.datosciudad.ProcesamientoDatos;
import com.angcar.datosciudad.io.WriterFiles;
import com.angcar.datosciudad.model.*;
import com.angcar.datosciudad.util.Utils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Datos de HTML
 */
public class DatosHTML {
    private StringBuilder stringHTMLData;
    private static final String PROGRAM_NAME = "Servicio meteorológico y contaminación";

    public DatosHTML(){
        stringHTMLData = new StringBuilder();
    }

    public StringBuilder getStringHTMLData(){
        return this.stringHTMLData;
    }

    /**
     * Reset HTML Data
     */
    public void resetHTMLData(){

        if (stringHTMLData !=null) {
            stringHTMLData.setLength(0);
        }
    }

    /**
     * Genera el HTML
     * @param nombreCiudad {@link String}
     * @throws IOException Excepción IO
     */
    public void generarHtml(String nombreCiudad, StringBuilder datosMediciones) throws IOException {

        //HEAD
        StringBuilder htmlString = new StringBuilder();
        htmlString.append(String.format("<!DOCTYPE html>\n" +
                "<html lang=\"es\">\n" +
                "    <head>\n" +
                "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
                "    <title>%s</title>\n" +
                "    <link rel=\"stylesheet\" type=\"text/css\" href=\"style/style.css\" media=\"screen\" />" +
                "    </head>\n" +
                "    <body>\n" +
                "<header>\n" +
                "\t<img src=\"image/informe.png\">\n" +
                "\t</header>\n" +
                "<section>\n",PROGRAM_NAME));

        //Obtener estaciones
        StringBuilder estacionesAsociadas = new StringBuilder();
        Utils.obtenerEstaciones(nombreCiudad).ifPresent(strings -> strings
                .forEach(s -> estacionesAsociadas.append("<p>").append(s).append("</p>")));

        String fechaIni = Utils.obtenerFechaInicioMedicion();
        String fechaFin = Utils.obtenerFechaFinalMedicion();

        htmlString.append(String.format("<h1>%s</h1>\n" +
                        "<h2>%s</h2>\n" +
                        "        <h3>Estaciones asociadas:</h3>\n" +
                        "        %s\n" +
                        "<h3>Fecha de inicio de la medición:</h3>\n" +
                        "<p>%s</p>\n" +
                        "<h3>Fecha de fin de la medición:</h3>\n" +
                        "<p>%s</p>\n"
                ,PROGRAM_NAME, nombreCiudad, estacionesAsociadas, fechaIni, fechaFin));


        ///CARGAR DATOS MEDICIONES
        htmlString.append(datosMediciones); //Agregar Datos de mediciones
        resetHTMLData(); //Resetear StringHTMLData


        htmlString.append("<p>" + ProcesamientoDatos.tiempoInforme() + "</p>");

        //END
        htmlString .append("</section>\n" +
                "</body>\n" +
                "</html>");

        WriterFiles.writeFile(htmlString.toString(), nombreCiudad);
    }

    /**
     * Agregar Datos de magnitud al HTML
     * @param isPrecipitation boolean
     * @param media {@link Double}
     * @param mapEntryMax Map.Entry<Medicion, Optional<Hora>>
     * @param mapEntryMin Map.Entry<Medicion, Optional<Hora>>
     * @param magnitud {@link Magnitud}
     * @param listaMediciones List<Medicion> listaMediciones
     * @return StringBuilder
     * @throws IOException excepción
     */
    public void agregarDatosMagnitudHtml(boolean isPrecipitation, Double media, Map.Entry<Measurement, Optional<Hora>> mapEntryMax,
                                         Map.Entry<Measurement, Optional<Hora>> mapEntryMin,
                                         Magnitud magnitud, List<Measurement> listaMediciones, String nombreCiudad) throws IOException {

        //MEDICIÓN MÁXIMA
        String medicionMaxMomento = "No se ha encontrado valor máximo."; //Por defecto

        if (mapEntryMax.getValue().isPresent()){
            medicionMaxMomento = "Valor máximo <b>" + mapEntryMax.getValue().get().getValue()
                    + " " + magnitud.getUnidad()
                    + "</b> el día <b>" + Utils.obtenerFechaMedicion(mapEntryMax.getKey())
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    + "</b> a las <b>" + String.format("%02d:00:00"
                    , mapEntryMax.getValue().get().getNumHour()) + "</b>";
        }

        //MEDICIÓN MÍNIMA
        String medicionMinMomento = "No se ha encontrado valor máximo."; //Por defecto

        if (mapEntryMin.getValue().isPresent()){
            medicionMinMomento = "Valor mínimo <b>" + mapEntryMin.getValue().get().getValue()
                    + " " + magnitud.getUnidad()
                    + "</b> el día <b>" + Utils.obtenerFechaMedicion(mapEntryMin.getKey())
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    + "</b> a las <b>" + String.format("%02d:00:00"
                    , mapEntryMin.getValue().get().getNumHour()) + "</b>";
        }

        //Agregar gráfica
        String actualPath = "image" + File.separator + magnitud.getCodeMagnitud() + ".png";
        ChartUtilities.saveChartAsPNG(
                new File(ProcesamientoDatos.path_destination + actualPath),
                datosMedicion(listaMediciones, magnitud.getDescriptionMagnitude(),
                        nombreCiudad)
                , 800, 800);

        //Agregar descripción de magnitud y media mensual
        stringHTMLData.append(String.format("\n<h2>%s</h2>\n" +
                        "<p>Valor medio mensual: %s</p>\n",
                magnitud.getDescriptionMagnitude(), media));

        //Agregar días si es precipitación
        if (isPrecipitation) {
            //Crear la tabla
            //PRECIPITACIÓN DE CADA DÍA QUE HA LLOVIDO
            StringBuilder tabla = new StringBuilder();
            tabla.append("\n<table>\n" +
                    "<tr>\n" +
                    "<th>Día</th>\n" +
                    "<th>Precipitación</th>\n" +
                    "</tr>\n");
            MeasurementsService.listaDiasPrecipitacion(listaMediciones)
                    .forEach((key, value) ->
                            tabla.append("<tr>\n" + "<td>").append(key).append("</td>\n")
                                    .append("<td>").append(value).append("</td>\n").append("</tr>\n")
                    );
            tabla.append("</table>\n");

            stringHTMLData.append(String.format("<p>Lista de los días que ha llovido y" +
                    "precipitación de cada día:</p>\n" + "%s", tabla));

        }
        //Agregar momento y valor del máximo y mínimo impacto SOLO si no es de tipo precipitación
        else{
            stringHTMLData.append(String.format(
                    "<p>Momento y valor máximo impacto: %s</p>\n" +
                            "<p>Momento y valor mínimo impacto: %s</p>\n",
                    medicionMaxMomento, medicionMinMomento));
        }

        //Generar gráfico
        stringHTMLData.append(String.format("<p>Gráfica de la evolución del impacto:</p>\n" +
                "<img src=\"%s\" />",actualPath));

    }

    /**
     * Crea la gráfica con los datos de medición
     * @param listaMediciones Lista de mediciones {@link List}<{@link Measurement}>
     * @param descripcion_magnitud Descripción de magnitud {@link String}
     * @return List<Medicion>
     */
        private JFreeChart datosMedicion(List<Measurement> listaMediciones, String descripcion_magnitud, String nombreCiudad) {

            DefaultCategoryDataset dataset = new DefaultCategoryDataset();

            listaMediciones.forEach(medicion ->
                    dataset.setValue(Utils.obtenerMediaDiaria(medicion),
                            "Día " + medicion.getDay(), "Día " + medicion.getDay()));

            return ChartFactory.createBarChart3D(nombreCiudad, descripcion_magnitud,
                    descripcion_magnitud, dataset, PlotOrientation.VERTICAL, true,
                    true, false);
        }
}