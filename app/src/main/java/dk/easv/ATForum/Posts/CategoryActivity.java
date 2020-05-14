package dk.easv.ATForum.Posts;

import androidx.appcompat.app.AppCompatActivity;

import com.example.forum.R;
import android.os.Bundle;
import android.widget.ListView;

import java.util.List;

import dk.easv.ATForum.Adapters.CategoryAdapter;
import dk.easv.ATForum.Interfaces.IDataAccess;
import dk.easv.ATForum.Models.Category;
import dk.easv.ATForum.Models.Role;
import dk.easv.ATForum.Models.User;

public class CategoryActivity extends AppCompatActivity {
    private static final String TAG = "XYZ";
    private CategoryAdapter categoryAdapter;
    private List<Category> categoryList;
    private ListView categoryListView;
    private Role role;
    private User currentUser;

    private IDataAccess dataAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);


    }
}
