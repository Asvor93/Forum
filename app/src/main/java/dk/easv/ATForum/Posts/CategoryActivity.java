package dk.easv.ATForum.Posts;

import com.example.forum.R;
import com.r0adkll.slidr.Slidr;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import dk.easv.ATForum.Adapters.CategoryAdapter;
import dk.easv.ATForum.DataAccessFactory;
import dk.easv.ATForum.Interfaces.IDataAccess;
import dk.easv.ATForum.MenuActivity;
import dk.easv.ATForum.Models.Category;

public class CategoryActivity extends MenuActivity {
    // Tag for logging
    private static final String TAG = "XYZ";
    // Request code for creating a category
    private static final int CATEGORY_CREATE_REQUEST = 9;

    // The adapter for the categories
    private CategoryAdapter categoryAdapter;

    // The list of categories
    private List<Category> categoryList;

    // The listView that shows all the categories
    private ListView categoryListView;

    // The interface that handles data access
    private IDataAccess dataAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        Slidr.attach(this);

        categoryListView = findViewById(R.id.catList);

        dataAccess = DataAccessFactory.getInstance();

        // Fetches all categories from the database
        dataAccess.getAllCategories(new IDataAccess.IOnResult<List<Category>>() {
            @Override
            public void onResult(List<Category> categories) {
                categoryList = categories;
                categoryAdapter = new CategoryAdapter(CategoryActivity.this, R.layout.category_cell, categoryList, currentUser, role);
                categoryListView.setAdapter(categoryAdapter);
            }
        });

        // Opens up the activity for showing all topics within the chosen category
        categoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String catUid = categoryList.get(position).getUid();
                Intent topicIntent = new Intent(CategoryActivity.this, TopicActivity.class);
                topicIntent.putExtra("catId", catUid);
                startActivity(topicIntent);
            }
        });

        // Adds a long click listener to the items in the list view that deletes the element if the user has sufficient permission
        categoryListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                categoryListView.setLongClickable(true);
                Category category = categoryList.get(position);
                String uid = category.getUid();
                if (!role.getRoleName().equals("user")) {
                    dataAccess.deleteCategory(uid);
                    Toast.makeText(CategoryActivity.this, "Deleted category with id: " + uid, Toast.LENGTH_SHORT).show();
                }
                categoryList.remove(position);
                categoryAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        categoryMenuItem.setVisible(false);
        categoryMenuItem.setEnabled(false);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && requestCode == CATEGORY_CREATE_REQUEST) {
            Category cat = (Category) data.getExtras().getSerializable("newCategory");
            if (cat != null) {
                categoryList.add(cat);
                categoryAdapter.notifyDataSetChanged();
            }
        }
    }
}
