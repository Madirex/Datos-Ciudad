package com.angcar.datosciudad.service;

import com.angcar.datosciudad.model.Measurement;
import com.angcar.datosciudad.model.Hora;
import com.angcar.datosciudad.util.Utils;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class MeasurementsService {

    /**
     * Sacar la media de la lista de mediciones sobre el momento
     * @param listaMediciones lista de mediciones
     * @return Optional<Double>
     */
    public static Optional<Double> medicionMedia(List<Measurement> listaMediciones) {
            double media = listaMediciones.stream()
                    .mapToDouble(Utils::obtenerMediaDiaria)
                    .summaryStatistics().getAverage();

            return Optional.of(Math.round(media * 100d) / 100d);
    }

    /**
     * Calcula el dato máximo por la lista de medición sobre el momento
     * @param listaMediciones lista de mediciones
     * @return Map.Entry<Medicion, Optional<Hora>>
     */
    public static Map.Entry<Measurement, Optional<Hora>> medicionMaximaMomento(List<Measurement> listaMediciones) {
        //Mapear la medición con su hora máxima
        Map<Measurement, Optional<Hora>> medicionesMax = listaMediciones.stream()
                .collect(Collectors.toMap(
                        medicion -> medicion, o -> Arrays.stream(o.getHours())
                                .filter(hora -> hora.getValidation().equals("V"))
                                .max(Comparator.comparing(Hora::getValue)),(o, o2) -> o
                ));

        //Conseguir la medición con la hora máxima y meterla en un "'Map.Entry'"
        return Collections.max(medicionesMax.entrySet(),
                Map.Entry.comparingByValue(Comparator.comparingDouble(o -> o.map(hora ->
                        Double.parseDouble(hora.getValue())).orElse(0.0))));
    }

    /**
     * Calcular el dato mínimo de la lista de mediciones sobre el momento
     * @param listaMediciones una lisa de mediciones
     * @return Map.Entry<Medicion, Optional<Hora>>
     */
    public static Map.Entry<Measurement, Optional<Hora>> medicionMinimaMomento(List<Measurement> listaMediciones) {
        //Mapear la medición con su hora máxima
        Map<Measurement, Optional<Hora>> medicionesMin = listaMediciones.stream()
                .collect(Collectors.toMap(
                        medicion -> medicion, o -> Arrays.stream(o.getHours())
                                .filter(hora -> hora.getValidation().equals("V"))
                                .min(Comparator.comparing(Hora::getValue)),(o, o2) -> o
                ));

        //Conseguir la medición con la hora máxima y meterla en un "'Map.Entry'"
        return Collections.min(medicionesMin.entrySet(),
                Map.Entry.comparingByValue(Comparator.comparingDouble(o -> o.map(hora ->
                        Double.parseDouble(hora.getValue())).orElse(0.0))));
    }

    /**
     * Lista de días que ha llovido y precipitación de cada día
     * @param listaMediciones Una lista de mediciones
     * @return Devuelve un map con le fecha de precipitación y su cantidad
     */
    public static Map<LocalDate, Double> listaDiasPrecipitacion(List<Measurement> listaMediciones) {

        return listaMediciones.stream()
                .collect(Collectors.toMap(Utils::obtenerFechaMedicion,
                        Utils::obtenerMediaDiaria, (k1, k2) -> k1));
    }
}