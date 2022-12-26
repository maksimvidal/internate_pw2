package parser;

import generated.ObjectFactory;
import generated.PojoType;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;

public class JaxbParser implements Parser<PojoType> {

    @Override
    public void marshall(String fileName, PojoType toMarshall) throws IOException {
        final String FILE_PATH = String.format(PATH, fileName);
        File file = new File(FILE_PATH);
        ObjectFactory objectFactory = new ObjectFactory();

        file.createNewFile();

        try{
            JAXBContext jContext = JAXBContext.newInstance(PojoType.class);
            Marshaller marshallObj = jContext.createMarshaller();
            marshallObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            marshallObj.marshal(toMarshall, file);

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public PojoType unmarshall(String fileName, Class<PojoType> clazz) {
        try{
            final String FILE_PATH = String.format(PATH, fileName);
            File file = new File(FILE_PATH);

            JAXBContext jContext = JAXBContext.newInstance(clazz);
            Unmarshaller unmarshallerObj = jContext.createUnmarshaller();

            PojoType entity =(PojoType) unmarshallerObj.unmarshal(file);

            return entity;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
