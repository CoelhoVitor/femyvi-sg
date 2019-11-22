package connection;

import com.sun.net.ssl.internal.ssl.Provider;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.security.Security;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.xml.bind.JAXBException;
import model.User;
import model.UserMessage;
import service.AuthService;

public class UserAuth extends Thread {

    private int port;

    private final AuthService authService = new AuthService();

    private final UserMessageSocket userMessageSocket = new UserMessageSocket();

    public UserAuth(Ports port) {
        this.port = port.getValue();
    }

    @Override
    public void run() {

        Security.addProvider(new Provider());
        System.setProperty("javax.net.ssl.keyStore", "sgkeystore.ks");
        System.setProperty("javax.net.ssl.keyStorePassword", "femyvi-sg");
        SSLServerSocketFactory sslServerSocketfactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();

        while (true) {
            try {
                // receive user from client
                SSLServerSocket server = (SSLServerSocket) sslServerSocketfactory.createServerSocket(port);
                System.out.println("User Auth iniciado na porta " + port);
                SSLSocket socket = (SSLSocket) server.accept();
                UserMessage um = userMessageSocket.receiveUserMessage(socket);
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
            } catch (IOException ex) {
                Logger.getLogger(UserAuth.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(UserAuth.class.getName()).log(Level.SEVERE, null, ex);
            } catch (JAXBException ex) {
                Logger.getLogger(UserAuth.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

}
