package parser;

import generated.*;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static parser.DeliveryAttributes.*;

public class SaxParser extends DefaultHandler {

    private DeliveryType currentDelivery;
    private List<DeliveryType> deliveries;
    private StringBuilder data = new StringBuilder();

    @Override
    public void startDocument() throws SAXException {
        deliveries = new ArrayList<>();
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        data.append(ch, start, length);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        switch (qName) {
            case DELIVERY -> currentDelivery = new DeliveryType();
            case ITEM -> currentDelivery.setItem(new ItemType());
            case CLIENT -> currentDelivery.setClient(new ClientType());
            case VEHICLE -> currentDelivery.getCourier().setVehicle(new VehicleType());
            case COURIER -> currentDelivery.setCourier(new CourierType());
        }

        data = new StringBuilder();
    }
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        switch (qName) {
            case DELIVERY -> deliveries.add(currentDelivery);
            case DELIVERY_PRICE -> currentDelivery.setDeliveryPrice(Float.valueOf(data.toString()));
            case DELIVERY_START -> currentDelivery.setDeliveryStart(from(data.toString()));
            case DELIVERY_END -> currentDelivery.setDeliveryEnd(from(data.toString()));

            case AGE -> currentDelivery.getCourier().setAge(Byte.parseByte(data.toString()));
            case COURIER_FIRST_NAME -> currentDelivery.getCourier().setFirstName(data.toString());
            case COURIER_LAST_NAME -> currentDelivery.getCourier().setLastName(data.toString());
            case EMPLOYED_AT -> currentDelivery.getCourier().setEmployedAt(from(data.toString()));
            case COURIER_PHONE_NUMBER -> currentDelivery.getCourier().setPhoneNumber(data.toString());

            case VEHICLE_NAME -> currentDelivery.getCourier().getVehicle().setVehicleName(data.toString());

            case CLIENT_FIRST_NAME -> currentDelivery.getClient().setClientFirstName(data.toString());
            case CLIENT_LAST_NAME -> currentDelivery.getClient().setClientLastName(data.toString());
            case CLIENT_AGE -> currentDelivery.getClient().setClientAge(Byte.parseByte(data.toString()));
            case CLIENT_PHONE -> currentDelivery.getClient().setPhone(data.toString());
            case CLIENT_EMAIL -> currentDelivery.getClient().setEmail(data.toString());

            case ITEM_NAME -> currentDelivery.getItem().setItemName(data.toString());
            case ITEM_WEIGHT -> currentDelivery.getItem().setWeight(Float.valueOf(data.toString()));
            case ITEM_PRICE -> currentDelivery.getItem().setPrice(Float.valueOf(data.toString()));

            case ADDRESS -> currentDelivery.setAddress(data.toString());
            case FINISHED -> currentDelivery.setFinished(Boolean.parseBoolean(data.toString()));
        }
    }

    public List<DeliveryType> getDeliveries() {
        return deliveries;
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

class DeliveryAttributes {

    public static final String DELIVERY_PRICE = "deliveryPrice";
    public static final String ADDRESS = "address";
    public static final String COURIER = "courier";
    public static final String CLIENT = "client";
    public static final String ITEM = "item";
    public static final String DELIVERY_START = "deliveryStart";
    public static final String DELIVERY_END = "deliveryEnd";
    public static final String FINISHED = "finished";
    public static final String DELIVERY = "Delivery";
    public static final String AGE = "age";
    public static final String EMPLOYED_AT = "employedAt";
    public static final String COURIER_FIRST_NAME = "firstName";
    public static final String COURIER_LAST_NAME = "lastName";
    public static final String COURIER_PHONE_NUMBER =  "phoneNumber";
    public static final String VEHICLE = "vehicle";
    public static final String VEHICLE_NAME = "vehicleName";

    public static final String CLIENT_FIRST_NAME = "clientFirstName";
    public static final String CLIENT_LAST_NAME = "clientLastName";
    public static final String CLIENT_AGE = "clientAge";
    public static final String CLIENT_PHONE = "phone";
    public static final String CLIENT_EMAIL = "email";

    public static final String ITEM_PRICE = "price";
    public static final String ITEM_WEIGHT = "weight";
    public static final String ITEM_NAME = "itemName";
}
