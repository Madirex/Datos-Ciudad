package com.angcar.datosciudad.model.resultados;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import javax.xml.bind.annotation.*;
import java.util.List;

@Data
@XmlType(name = "resultado", propOrder = {"ciudad", "estacionesAsociadas", "fechaInicio",
        "fechaFin", "datosMeteo", "datosContamina"})
public class ResultadoMediciones {
    @Getter(AccessLevel.NONE) private String id;
    private String ciudad;
    @Getter(AccessLevel.NONE) private List<String> estacionesAsociadas;
    private String fechaInicio;
    private String fechaFin;
    @Getter(AccessLevel.NONE) private List<DatosMagnitud> datosMeteo;
    @Getter(AccessLevel.NONE) private List<DatosMagnitud> datosContamina;

    @XmlAttribute(name = "id")
    public String getId() {
        return id;
    }

    @XmlElementWrapper(name = "estaciones_asociadas")
    @XmlElement(name = "estacion")
    public List<String> getEstacionesAsociadas() {
        return estacionesAsociadas;
    }

    @XmlElementWrapper(name = "datos-metereologicos")
    @XmlElement(name = "dato")
    public List<DatosMagnitud> getDatosMeteo() {
        return datosMeteo;
    }

    @XmlElementWrapper(name = "datos-contaminacion")
    @XmlElement(name = "dato")
    public List<DatosMagnitud> getDatosContamina() {
        return datosContamina;
    }

}
