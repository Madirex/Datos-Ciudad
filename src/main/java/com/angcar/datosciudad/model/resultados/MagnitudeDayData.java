package com.angcar.datosciudad.model.resultados;

import lombok.Data;

import javax.xml.bind.annotation.XmlType;

@Data
@XmlType(propOrder = {"fecha", "valor"})
public class MagnitudeDayData {
    private String fecha;
    private double valor;
}
