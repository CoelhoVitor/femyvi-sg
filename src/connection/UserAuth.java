
package connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import model.User;
import model.UserMessage;
import service.AuthService;

public class UserAuth extends Thread {

    private int port;
    
    private final AuthService authService = new AuthService();

    public UserAuth(Ports port) {
        this.port = port.getValue();
    }
    
    @Override
    public void run() {
        try {
            while (true) {
                // receive user from client
                ServerSocket server = new ServerSocket(port);
                System.out.println("User Auth iniciado na porta " + port);
                Socket socket = server.accept();
                InputStream inputStream = socket.getInputStream();
                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

                UserMessage um = (UserMessage) objectInputStream.readObject();
                System.out.println(um.toString());
                
                // valid user
                User userToAuth = new User(um);
                Boolean validUser = authService.authUser(userToAuth);
                
                // send to client if user is valid
                OutputStream outputStream = socket.getOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                objectOutputStream.writeObject(validUser);

                socket.close();
                server.close();
            }            
        } catch (IOException ex) {
            Logger.getLogger(UserAuth.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(UserAuth.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JAXBException ex) {
            Logger.getLogger(UserAuth.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
}
