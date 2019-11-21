package connection;

import com.sun.net.ssl.internal.ssl.Provider;
import java.io.IOException;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.Security;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import model.FileMessage;
import service.SplitService;

public class FileUpload extends Thread {

    private int port;

    private final SplitService splitService = new SplitService();

    private final FileMessageSocket fileMessageSocket = new FileMessageSocket();

    public FileUpload(Ports port) {
        this.port = port.getValue();
    }

    @Override
    public void run() {
        try {
            Security.addProvider(new Provider());
            System.setProperty("javax.net.ssl.keyStore", "sgkeystore.ks");
            System.setProperty("javax.net.ssl.keyStorePassword", "femyvi-sg");
            System.setProperty("javax.net.ssl.trustStore", "sgkeystore.ks");
            
            SSLServerSocketFactory sslServerSocketfactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();

            while (true) {
                // receive file from client
                SSLServerSocket server = (SSLServerSocket) sslServerSocketfactory.createServerSocket(port);
                System.out.println("File Upload iniciado na porta " + port);
                SSLSocket socket = (SSLSocket) server.accept();
                FileMessage fm = fileMessageSocket.receiveFileMessage(socket);
                System.out.println(fm.toString());

                socket.close();
                server.close();

                // send file to SA
                ArrayList<FileMessage> splittedFm = splitService.split(fm);

                // SA 1
                SSLSocketFactory sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
                SSLSocket socketToSA_1 = (SSLSocket) sslSocketFactory.createSocket("localhost", Ports.UPLOAD_SA_1.getValue());

                socketToSA_1.startHandshake();
                fileMessageSocket.sendFileMessage(socketToSA_1, splittedFm.get(0));
                socketToSA_1.close();

                // SA 2
                SSLSocket socketToSA_2 = (SSLSocket) sslSocketFactory.createSocket("localhost", Ports.UPLOAD_SA_2.getValue());

                socketToSA_2.startHandshake();
                fileMessageSocket.sendFileMessage(socketToSA_2, splittedFm.get(1));
                socketToSA_2.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(FileUpload.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FileUpload.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
