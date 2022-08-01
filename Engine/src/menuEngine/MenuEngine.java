package menuEngine;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class MenuEngine {

    public boolean LoadXMLFile(String filePath)
    {
        File file = new File(filePath);
        if(!(filePath.toLowerCase().endsWith(".xml") || file.exists()))
            return false;
        try {
            InputStream inputStream = new FileInputStream(new File("src/resources/world.xml"));

        } catch (JAXBException | FileNotFoundException e) {
            e.printStackTrace();
        }

        return true;
    }
}
