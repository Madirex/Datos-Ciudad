package com.angcar.datosciudad.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Hora {
    private String value;
    private String validation;
    private int numHour;
}