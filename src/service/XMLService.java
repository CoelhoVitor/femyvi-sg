
package service;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import model.Database;
import model.User;

public class XMLService {
    
    private final String USER_FILENAME = "user.xml";
    
    public Database readDatabase() throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Database.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        Database db = (Database) unmarshaller.unmarshal(new File(USER_FILENAME));
        return db;
    }
    
    public void writeDatabase(Database db) throws JAXBException {
        File file = new File(USER_FILENAME);
        JAXBContext jaxbContext = JAXBContext.newInstance(Database.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        jaxbMarshaller.marshal(db, file);
    }
    
}
