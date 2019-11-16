
import connection.HealthChecker;

public class Main {
    
    public static void main(String[] args) {
        HealthChecker hc = new HealthChecker();
        
        // Starting HealthCheck Thread
        hc.start();
    }
    
}
