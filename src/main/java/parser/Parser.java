package parser;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public interface Parser <T> {

    String PATH = "src/main/resources/%s.xml";

    void marshall(String fileName, T t) throws IOException, ParserConfigurationException;

    T unmarshall(String fileName, Class<T> clazz);
}
