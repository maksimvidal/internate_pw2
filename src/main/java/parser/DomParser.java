package parser;

import generated.*;
import org.apache.xerces.dom.DeferredElementImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static parser.DeliveryAttributes.*;
import static parser.DeliveryAttributes.FINISHED;
import static parser.Parser.PATH;

public class DomParser {

    public void marshall(String fileName, DeliveryType toMarshall) throws IOException, ParserConfigurationException {
        final String FILE_PATH = String.format(PATH, fileName);
        File file = new File(FILE_PATH);

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.newDocument();

        try{
            JAXBContext jContext = JAXBContext.newInstance(PojoType.class);
            Marshaller marshallObj = jContext.createMarshaller();
            marshallObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            marshallObj.marshal(toMarshall, document);

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public List<DeliveryType> unmarshall(String fileName) {
        try {
            final String FILE_PATH = String.format(PATH, fileName);
            File file = new File(FILE_PATH);

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder;
            try {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(file);
                doc.getDocumentElement().normalize();

                List<DeliveryType> deliveries = new ArrayList<>();
                NodeList nodeList = doc.getElementsByTagName("Delivery");

                for (int i = 0; i < nodeList.getLength(); i++) {
                    deliveries.add(parseDelivery(nodeList.item(i)));
                }

                return deliveries;
            } catch (Exception exc) {
                exc.printStackTrace();
            }
        } catch (Exception e) {
            throw e;
        }

        return null;
    }

    private DeliveryType parseDelivery(Node deliveryNode) {
        NodeList nodes = deliveryNode.getChildNodes();
        DeliveryType delivery = new DeliveryType();

        for(int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            switch (node.getNodeName()) {
                case DELIVERY_PRICE -> delivery.setDeliveryPrice(Float.valueOf(node.getTextContent()));
                case DELIVERY_START -> delivery.setDeliveryStart(from(node.getTextContent()));
                case DELIVERY_END -> delivery.setDeliveryEnd(from(node.getTextContent()));
                case ADDRESS -> delivery.setAddress(node.getTextContent());
                case FINISHED -> delivery.setFinished(Boolean.parseBoolean(node.getTextContent()));
                case COURIER -> delivery.setCourier(parseCourier(node));
                case CLIENT -> delivery.setClient(parseClient(node));
                case ITEM -> delivery.setItem(parseItem(node));
            }
        }

        return delivery;
    }

    private CourierType parseCourier(Node courierNode) {
        NodeList nodes = courierNode.getChildNodes();
        CourierType courier = new CourierType();

        for(int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            switch (node.getNodeName()) {
                case AGE -> courier.setAge(Byte.parseByte(node.getTextContent()));
                case COURIER_FIRST_NAME -> courier.setFirstName(node.getTextContent());
                case COURIER_LAST_NAME -> courier.setLastName(node.getTextContent());
                case EMPLOYED_AT -> courier.setEmployedAt(from(node.getTextContent()));
                case COURIER_PHONE_NUMBER -> courier.setPhoneNumber(node.getTextContent());
                case VEHICLE -> courier.setVehicle(new VehicleType(node.getChildNodes().item(0).getTextContent()));
            }
        }

        return courier;
    }

    private ClientType parseClient(Node clientNode) {
        NodeList nodes = clientNode.getChildNodes();
        ClientType client = new ClientType();

        for(int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            switch (node.getNodeName()) {
                case CLIENT_FIRST_NAME -> client.setClientFirstName(node.getTextContent());
                case CLIENT_LAST_NAME -> client.setClientLastName(node.getTextContent());
                case CLIENT_AGE -> client.setClientAge(Byte.parseByte(node.getTextContent()));
                case CLIENT_PHONE -> client.setPhone(node.getTextContent());
                case CLIENT_EMAIL -> client.setEmail(node.getTextContent());
            }
        }

        return client;
    }

    private ItemType parseItem(Node itemNode) {
        NodeList nodes = itemNode.getChildNodes();
        ItemType item = new ItemType();

        for(int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            switch (node.getNodeName()) {
                case ITEM_NAME ->item.setItemName(node.getTextContent());
                case ITEM_WEIGHT -> item.setWeight(Float.valueOf(node.getTextContent()));
                case ITEM_PRICE -> item.setPrice(Float.valueOf(node.getTextContent()));
            }
        }

        return item;
    }

    private XMLGregorianCalendar from(String date) {
        try {
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(date);
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
