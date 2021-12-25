package com.angcar.datosciudad.model;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Contamina extends Measurement {

    public Contamina(String provincial, String municipal, String station, String magnitude, String pointMaestro, int year, int month, int day, Hora[] hours) {
        super(provincial, municipal, station, magnitude, pointMaestro, year, month, day, hours);
    }
}