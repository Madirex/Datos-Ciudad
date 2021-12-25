package com.angcar.datosciudad.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UbicacionEstaciones {
    private String stationCode;
    private String airQualityZoneDescription;
    private String stationMunicipal;
    private String highDateStation;
    private String areaTypeStation;
    private String stationTypeStation;
    private String ruralSubareaStation;
    private String postalAddressStation;
    private String stationCoordUTMETRS89x;
    private String stationCoordUTMETRS89y;
    private String stationCoordLength;
    private String latitudeCoordStation;
    private String stationAltitude;
    private String noAnalyzerStation;
    private String nO2AnalyzerStation;
    private String analyzerStationPM10;
    private String analyzerStationPM25;
    private String o3AnalyzerStation;
    private String tolAnalyzerStation;
    private String benAnalyzerStation;
    private String xilAnalyzerStation;
    private String coAnalyzerStation;
    private String so2AnalyzerStation;
    private String hctAnalyzerStation;
    private String hnmAnalyzerStation;
}
