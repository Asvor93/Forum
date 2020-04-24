package dk.easv.ATForum.Models;

public class User {
        private String uid;
        private String username;
        private String name;
        private String email;
        private String photoURL;
        private String role;

        public User(String username, String name, String email, String role) {
            this.username = username;
            this.name = name;
            this.email = email;
            this.role = role;
        }

        public String getPhotoURL() {
            return photoURL;
        }

        public void setPhotoURL(String photoURL) {
            this.photoURL = photoURL;
        }
    }
