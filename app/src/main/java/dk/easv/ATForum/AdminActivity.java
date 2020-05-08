package dk.easv.ATForum;

import androidx.appcompat.app.AppCompatActivity;

import com.example.forum.R;

import android.os.Bundle;
import android.widget.ListView;

import java.util.List;

import dk.easv.ATForum.Models.User;
import dk.easv.ATForum.Models.UserAdapter;

public class AdminActivity extends AppCompatActivity {

    UserAdapter userAdapter;

    List<User> userList;

    ListView userListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        //get All users

        userListView = findViewById(R.id.listUsers);
        userAdapter = new UserAdapter(this, R.layout.cell, userList);
        userListView.setAdapter(userAdapter);
    }
}
