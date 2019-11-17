
package connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.FileMessage;
import service.SplitService;

public class FileRemove extends Thread {
    
    private int port;
    
    private final SplitService splitService = new SplitService();
    
    private final FileMessageSocket fileMessageSocket = new FileMessageSocket();

    public FileRemove(Ports port) {
        this.port = port.getValue();
    }

    @Override
    public void run() {
        try {
            while (true) {
                // receive file from client
                ServerSocket server = new ServerSocket(port);
                System.out.println("File Remove iniciado na porta " + port);
                Socket socket = server.accept();            
                FileMessage fm = fileMessageSocket.receiveFileMessage(socket);
                System.out.println(fm.toString());

                socket.close();
                server.close();

                // send file to SA

                ArrayList<FileMessage> splittedFm = splitService.run(fm);

                // SA 1
                Socket socketToSA_1 = new Socket("localhost", Ports.REMOVE_SA_1.getValue());
                fileMessageSocket.sendFileMessage(socketToSA_1, splittedFm.get(0));
                socketToSA_1.close();

                // SA 2
                Socket socketToSA_2 = new Socket("localhost", Ports.REMOVE_SA_2.getValue());
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
