package connection;

import com.sun.net.ssl.internal.ssl.Provider;
import java.io.IOException;
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
import model.UserMessage;
import service.SplitService;

public class FileFetch extends Thread {

    private int port;

    private final FileMessageSocket fileMessageSocket = new FileMessageSocket();

    private final UserMessageSocket userMessageSocket = new UserMessageSocket();

    private final SplitService splitService = new SplitService();

    public FileFetch(Ports port) {
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
            SSLSocketFactory sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            while (true) {
                // receive file from client

                SSLServerSocket server = (SSLServerSocket) sslServerSocketfactory.createServerSocket(port);
                System.out.println("File Fetch iniciado na porta " + port);
                SSLSocket socket = (SSLSocket) server.accept();
                UserMessage um = userMessageSocket.receiveUserMessage(socket);
                System.out.println(um.toString());
                socket.close();

                // send user to SA and get file parts
                ArrayList<FileMessage> mergedFiles = new ArrayList<>();

                // SA 1
                SSLSocket socketToSA_1 = (SSLSocket) sslSocketFactory.createSocket("localhost", Ports.FETCH_SA_1.getValue());
                socketToSA_1.startHandshake();
                
                userMessageSocket.sendUserMessage(socketToSA_1, um);
                ArrayList<FileMessage> fileMessages_1 = fileMessageSocket.receiveFileMessageList(socketToSA_1);
                socketToSA_1.close();

                if (!fileMessages_1.isEmpty()) {
                    // SA 2
                    SSLSocket socketToSA_2 = (SSLSocket) sslSocketFactory.createSocket("localhost", Ports.FETCH_SA_2.getValue());
                    socketToSA_2.startHandshake();
                    userMessageSocket.sendUserMessage(socketToSA_2, um);
                    ArrayList<FileMessage> fileMessages_2 = fileMessageSocket.receiveFileMessageList(socketToSA_2);
                    socketToSA_2.close();

                    for (int i = 0; i < fileMessages_1.size(); i++) {
                        FileMessage f1 = fileMessages_1.get(i);
                        FileMessage f2 = fileMessages_2.get(i);
                        FileMessage fm = splitService.merge(f1, f2);
                        mergedFiles.add(fm);
                    }
                }

                socket = (SSLSocket) server.accept();
                fileMessageSocket.sendFileMessageList(socket, mergedFiles);

                server.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(FileUpload.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FileUpload.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
