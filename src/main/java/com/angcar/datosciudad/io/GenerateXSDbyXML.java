package com.angcar.datosciudad.io;

import lombok.SneakyThrows;

import javax.xml.bind.SchemaOutputResolver;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class GenerateXSDbyXML extends SchemaOutputResolver {

    /**
     * Crea el xsd en base a un contexto
     * @param namespaceURI {@link String}
     * @param suggestedFileName {@link String}
     * @return Result
     */
    @SneakyThrows
    public Result createOutput(String namespaceURI, String suggestedFileName) {
        String path = "src" + File.separator + "main" + File.separator + "resources" + File.separator;
        File file = new File(path+suggestedFileName);
        StreamResult result = new StreamResult(file);
        result.setSystemId(file.getAbsolutePath());
        return result;
    }
}
