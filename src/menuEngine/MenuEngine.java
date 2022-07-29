package menuEngine;

import jaxb.schema.generated.CTEEnigma;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class MenuEngine {

    private final static String JAXB_XML_PACKAGE_NAME = "jaxb.schema.generated";
    public boolean LoadXMLFile(String filePath) // TODO: copy all classes
    {
        File file = new File(filePath);
        if(!(filePath.toLowerCase().endsWith(".xml")))
        {
            System.out.println("The file you entered isn't XML file.");
            return false;
        }
        else if(!file.exists())
        {
            System.out.println("The file you entered isn't exists.");
            return false;
        }

        try {
            InputStream inputStream = new FileInputStream(new File(filePath));
            JAXBContext jc = JAXBContext.newInstance(JAXB_XML_PACKAGE_NAME);
            Unmarshaller u = jc.createUnmarshaller();
            CTEEnigma eng = (CTEEnigma) u.unmarshal(inputStream);
            System.out.println("id of rotor 1 is: " + eng.getCTEMachine().getCTERotors().getCTERotor().get(1).getId());

        }  catch (JAXBException | FileNotFoundException e) {
            e.printStackTrace();
        }

        return true;
    }
}
