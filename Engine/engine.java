public class engine {

public boolean LoadXMLFile(string filePath)
{
    File file = new File(filePath);
    if(!(filePath.toLowerCase().endsWith(".xml") || file.exist()))
        return false;
    try {
        InputStream inputStream = new FileInputStream(new File("src/resources/world.xml"));

    } catch (JAXBException | FileNotFoundException e) {
        e.printStackTrace();
    }

    return true;
}

}
