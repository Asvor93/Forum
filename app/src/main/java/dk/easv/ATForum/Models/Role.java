package dk.easv.ATForum.Models;

import java.io.Serializable;

public class Role implements Serializable {
   private String roleName;

    public Role(String roleName) {
        this.roleName = roleName;
    }

    public Role() {}

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
