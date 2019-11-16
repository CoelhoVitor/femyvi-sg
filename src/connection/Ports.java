package connection;

public enum Ports {
    
    HEALTHCHECK(2001), DOWNLOAD(4000), REMOVE(4001);
    
    private final int value;
    
    Ports(int portValue){
        value = portValue;
    }
    
    public int getValue(){
        return value;
    }
    
}
