package com.angcar.datosciudad.model.resultados;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Data
@XmlRootElement(name = "resultados")
public class ResultadosMediciones {

    @Getter(AccessLevel.NONE) private List<ResultadoMediciones> resultados;

    @XmlElement(name = "resultado")
    public List<ResultadoMediciones> getResultados() {
        return resultados;
    }
}
