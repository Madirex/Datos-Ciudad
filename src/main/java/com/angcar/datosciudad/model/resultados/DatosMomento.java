package com.angcar.datosciudad.model.resultados;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DatosMomento {
    private String fecha;
    private String valor;

    public void setFechaConvert(LocalDateTime localDateTime) {
        this.fecha = localDateTime.toString();
    }
}
