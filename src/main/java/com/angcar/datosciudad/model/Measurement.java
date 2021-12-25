package com.angcar.datosciudad.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Measurement {
    private String provincial;
    private String municipal;
    private String station;
    private String magnitude;
    private String samplingPoint;
    private int year;
    private int month;
    private int day;
    private Hora[] hours;
}

