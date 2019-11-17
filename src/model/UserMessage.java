
package model;

import java.io.Serializable;

public class UserMessage implements Serializable {
    
    private String login;
    
    private String password;

    public UserMessage(User u) {
        this.login = u.getLogin();
        this.password = u.getPassword();
    }
    
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
        return "UserMessage{" + "login=" + login + ", password=" + password + '}';
    }
    
}
