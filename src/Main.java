
import connection.FileRemove;
import connection.FileUpload;
import connection.HealthChecker;
import connection.Ports;

public class Main {
    
    public static void main(String[] args) {
        HealthChecker hc = new HealthChecker(Ports.HEALTHCHECK);
        FileUpload fup = new FileUpload(Ports.UPLOAD);
        FileRemove fr = new FileRemove(Ports.REMOVE);
        
        // Starting HealthCheck Thread on Port 3000
        hc.start();
        
        // Start File Upload Thread on Port 3003
        fup.start();
        
        // Start File Remove Thread on Port 3002
        fr.start();
    }
    
}
