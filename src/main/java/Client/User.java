package Client;

import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDate;
import java.util.UUID;

public class User {
    private UUID account_id;
    private String username;
    private String password;
    private LocalDate date_of_birth;

    public User() {}

    public User(UUID account_id, String username, String password, LocalDate date_of_birth) {
        this.account_id = account_id;
        this.username = username;
        this.password = password;
        this.date_of_birth = date_of_birth;
    }

    public UUID getAccount_id() {
        return account_id;
    }

    public void setAccount_id(UUID account_id) {
        this.account_id = account_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(LocalDate date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String hashPassword(String password){
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static Boolean checkPassword(String password, String hashedPassword){
        return BCrypt.checkpw(password, hashedPassword);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + account_id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", date=" + date_of_birth +
                '}';
    }
}