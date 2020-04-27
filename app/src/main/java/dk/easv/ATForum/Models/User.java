package dk.easv.ATForum.Models;

import java.io.Serializable;

public class User implements Serializable {
        private String uid;
        private String username;
        private String name;
        private String email;
        private String photoURL;
        private String role;

    public User() {
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public User(String username, String name, String email, String role, String photoURL) {
            this.username = username;
            this.name = name;
            this.email = email;
            this.role = role;
            this.photoURL = photoURL;
        }

        public String getPhotoURL() {
            return photoURL;
        }

    @Override
    public String toString() {
        return "User{" +
                "uid='" + uid + '\'' +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", photoURL='" + photoURL + '\'' +
                ", role='" + role + '\'' +
                '}';
    }

    public void setPhotoURL(String photoURL) {
            this.photoURL = photoURL;
        }
    }
