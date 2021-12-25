package com.angcar.datosciudad.model;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Weathering extends Measurement {

    public Weathering(String provincial, String municipal, String station, String magnitude, String samplingPoint, int year, int month, int day, Hora[] hours) {
        super(provincial, municipal, station, magnitude, samplingPoint, year, month, day, hours);
    }
}
