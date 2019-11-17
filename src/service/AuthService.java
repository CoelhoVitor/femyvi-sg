
package service;

import javax.xml.bind.JAXBException;
import model.Database;
import model.User;

public class AuthService {
    
    private final XMLService xmlService = new XMLService();
    
    public boolean authUser(User userToAuth) throws JAXBException {
        Database db = xmlService.readDatabase();
        
        for (User user : db.getUsers()) {
            if (checkUser(user, userToAuth)) {
                return true;
            }
        }
        
        return false;
    }
    
    private boolean checkUser(User userDb, User userToAuth) {
        
        HashService hs = new HashService();
        
        return userDb.getLogin().equals(userToAuth.getLogin()) 
                && hs.compareHash(userDb, userToAuth);
    }
    
}
