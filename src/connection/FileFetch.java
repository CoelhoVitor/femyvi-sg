package connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
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
            while (true) {
                // receive file from client
                ServerSocket server = new ServerSocket(port);
                System.out.println("File Fetch iniciado na porta " + port);
                Socket socket = server.accept();
                UserMessage um = userMessageSocket.receiveUserMessage(socket);
                System.out.println(um.toString());
                socket.close();

                // send user to SA and get file parts
                ArrayList<FileMessage> mergedFiles = new ArrayList<>();

                // SA 1
                Socket socketToSA_1 = new Socket("localhost", Ports.FETCH_SA_1.getValue());
                userMessageSocket.sendUserMessage(socketToSA_1, um);
                ArrayList<FileMessage> fileMessages_1 = fileMessageSocket.receiveFileMessageList(socketToSA_1);
                socketToSA_1.close();

                if (!fileMessages_1.isEmpty()) {
                    // SA 2
                    Socket socketToSA_2 = new Socket("localhost", Ports.FETCH_SA_2.getValue());
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

                socket = server.accept();
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
