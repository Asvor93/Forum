package dk.easv.ATForum.Models;

import java.io.Serializable;

public class Role implements Serializable {
   private String roleName;
   private String uid;

    public Role(String roleName, String uid) {
        this.roleName = roleName;
        this.uid = uid;
    }

    public Role() {}

    public String getUid() {
        return uid;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
