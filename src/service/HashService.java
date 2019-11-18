package service;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.User;

public class HashService {

    public boolean compareHash(User userDb, User userToAuth) {
        return generateHash(userToAuth.getPassword()).equals(userDb.getPassword());
    }

    private String generateHash(String password) {
        try {

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte messageDigest[] = md.digest(password.getBytes("UTF-8"));

            StringBuilder hexPassword = new StringBuilder();
            for (byte b : messageDigest) {
                hexPassword.append(String.format("%02X", 0xFF & b));
            }
            return hexPassword.toString().toLowerCase();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            Logger.getLogger(HashService.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

}
