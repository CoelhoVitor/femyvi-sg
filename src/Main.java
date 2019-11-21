
import connection.FileFetch;
import connection.FileRemove;
import connection.FileUpload;
import connection.HealthChecker;
import connection.Ports;
import connection.UserAuth;

public class Main {

    public static void main(String[] args) {
        FileUpload fup = new FileUpload(Ports.UPLOAD);
        FileRemove fr = new FileRemove(Ports.REMOVE);
        FileFetch ff = new FileFetch(Ports.FETCH);
        UserAuth ua = new UserAuth(Ports.AUTH);
        HealthChecker hc = new HealthChecker(Ports.HEALTHCHECK);
        
        // Start Health Checker Thread on Port 3000
        hc.start();

        // Start File Upload Thread on Port 3003
        fup.start();

        // Start File Remove Thread on Port 3002
        fr.start();
        
        // Start File Fetch Thread on Port 3001
        ff.start();

        // Start User Auth Thread on Port 3004
        ua.start();
    }

}
