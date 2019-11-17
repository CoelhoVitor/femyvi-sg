
package model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="user")
@XmlAccessorType(XmlAccessType.FIELD)
public class User {

    public User(UserMessage u) {
        this.login = u.getLogin();
        this.password = u.getPassword();
    }

    public User() {
    }
    
    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }
    
    private String login;
    
    private String password;

    public String getLogin() {
        return login;
    }
    
    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" + "login=" + login + ", password=" + password + '}';
    }
        
}
