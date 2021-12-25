package com.angcar.datosciudad.io;

import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.FileWriter;
import java.io.IOException;

public class ConsultasXPATH {
    /**
     * Realizar operaciones XPATH, generar archivo '.md' y mostrar por consola
     * @param ciudad {@link String}
     * @param path {@link String}
     * @throws JAXBException Excepción
     * @throws ParserConfigurationException Excepción
     * @throws XPathExpressionException Excepción
     * @throws IOException Excepción
     */
    public static void operacionesXpath(String ciudad, String path, Document doc)
            throws JAXBException, ParserConfigurationException, XPathExpressionException, IOException {

        XPathFactory factory = XPathFactory.newInstance();
        XPath xpath = factory.newXPath();

        NodeList list2 = (NodeList) xpath.evaluate(
                "//resultado/datos-metereologicos/dato/media",
                doc, XPathConstants.NODESET);

        NodeList list1 = (NodeList) xpath.evaluate(
                "//resultado/datos-metereologicos/dato/@tipo",
                doc, XPathConstants.NODESET);

        NodeList list5 = (NodeList) xpath.evaluate(
                "//resultado/datos-contaminacion/dato/media",
                doc, XPathConstants.NODESET);

        NodeList list6 = (NodeList) xpath.evaluate(
                "//resultado/datos-contaminacion/dato/@tipo",
                doc, XPathConstants.NODESET);

        NodeList list3 = (NodeList) xpath.evaluate(
                "//resultado/@id",
                doc, XPathConstants.NODESET);

        NodeList list4 = (NodeList) xpath.evaluate(
                "//resultado/ciudad",
                doc, XPathConstants.NODESET);

        int numMeteo = list1.getLength()/list3.getLength();
        int numConta = list6.getLength()/list3.getLength();

        StringBuilder md = new StringBuilder();

        for(int i = 0; i < list4.getLength(); i++) {
            if (ciudad.equals(list4.item(i).getTextContent())) {
                System.out.println(list4.item(i).getTextContent() + " id: " + list3.item(i).getTextContent());
                md.append("#").
                        append(list4.item(i).getTextContent())
                        .append(" id: ")
                        .append(list3.item(i).getTextContent())
                        .append("\n");
                System.out.println("Meteorización");
                md.append("##")
                        .append("Meteorización")
                        .append("\n");

                for (int j = i * numMeteo; j < (i * numMeteo) + numMeteo; j++) {
                    System.out.println(list1.item(j).getTextContent() + ": " + list2.item(j).getTextContent());
                    md.append("###")
                            .append(list1.item(j).getTextContent())
                            .append(": ")
                            .append(list2.item(j).getTextContent())
                            .append("\n");
                }

                System.out.println("Contaminación");
                for (int x = i * numConta; x < (i * numConta) + numConta; x++) {
                    System.out.println(list6.item(x).getTextContent() + ": " + list5.item(x).getTextContent());
                    md.append("###")
                            .append(list6.item(x).getTextContent())
                            .append(": ")
                            .append(list5.item(x).getTextContent())
                            .append("\n");
                }
            }
        }

        FileWriter markdown = new FileWriter(path+"informe-ciudad-"+ciudad+".md");
        markdown.write(md.toString());
        markdown.close();
    }
}