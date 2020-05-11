package dk.easv.ATForum;

import androidx.appcompat.app.AppCompatActivity;

import com.example.forum.R;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import dk.easv.ATForum.Interfaces.IDataAccess;
import dk.easv.ATForum.Models.Role;
import dk.easv.ATForum.Models.User;
import dk.easv.ATForum.Models.UserAdapter;

public class AdminActivity extends AppCompatActivity {

    private static final String TAG = "XYZ";
    UserAdapter userAdapter;
    List<Role> roleList;
    Role role;
    User user;
    List<User> userList;

    ListView userListView;

    private IDataAccess dataAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        getExtras();
        userListView = findViewById(R.id.listUsers);

        //get All users
        dataAccess = DataAccessFactory.getInstance();
        dataAccess.getAllUsers(user.getUid(), new IDataAccess.IONUsersResult() {
            @Override
            public void onResult(List<User> users) {
                userList = users;
                userAdapter = new UserAdapter(AdminActivity.this, R.layout.cell, userList);
                userListView.setAdapter(userAdapter);
                Log.d(TAG, "Users: " + userList.get(0));
            }
        });

        //get All roles
        dataAccess.getAllRoles(user.getUid(), new IDataAccess.IONRolesResult() {
            @Override
            public void onResult(List<Role> roles) {
                roleList = roles;
                Log.d(TAG, "Roles: " + roleList.get(0));
            }
        });


        userListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                userListView.setLongClickable(true);
                User user = userList.get(position);
                String uid = user.getUid();
                if (!role.getRoleName().equals("user")) {
                    if (roleList.get(position).getRoleName().equals("user") ||
                            role.getRoleName().equals("superAdmin")) {
                        dataAccess.deleteUser(uid);
                        Toast.makeText(AdminActivity.this, "Deleted user with id: " + uid, Toast.LENGTH_SHORT).show();
                    }
                }

                return true;
            }
        });

        Button btnBack = findViewById(R.id.btnBackAdmin);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getExtras() {
        user = (User) getIntent().getSerializableExtra("user");
        role = (Role) getIntent().getSerializableExtra("role");
    }
}
