package main;

import generated.DeliveryType;
import generated.PojoType;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import parser.DomParser;
import parser.JaxbParser;
import parser.Parser;
import parser.SaxParser;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.*;
import java.io.File;
import java.io.IOException;

public class Driver {
    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, JAXBException {
        sax();
        System.out.println(" ");
        dom();
    }

    static void sax() throws ParserConfigurationException, SAXException, IOException, JAXBException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        SaxParser handler = new SaxParser();
        parser.parse(new File("src\\main\\resources\\generatedDelivery.xml"), handler);

        DeliveryType delivery = handler.getDeliveries().get(0);

        JAXBContext context = JAXBContext.newInstance(DeliveryType.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(delivery, System.out);
    }

    static void dom() throws JAXBException, ParserConfigurationException {
        JAXBContext jaxbContext   = JAXBContext.newInstance( DeliveryType.class );
        Marshaller jaxbMarshaller   = jaxbContext.createMarshaller();
        DomParser parser = new DomParser();

        var result = parser.unmarshall("generatedDelivery");
        DeliveryType parsed = result.get(0);

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.newDocument();

        jaxbMarshaller.marshal(parsed, System.out);
    }
}
