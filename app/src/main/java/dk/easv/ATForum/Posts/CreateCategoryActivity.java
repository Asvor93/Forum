package dk.easv.ATForum.Posts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.forum.R;
import com.r0adkll.slidr.Slidr;

import java.util.HashMap;
import java.util.Map;

import dk.easv.ATForum.DataAccessFactory;
import dk.easv.ATForum.Interfaces.IDataAccess;
import dk.easv.ATForum.MenuActivity;
import dk.easv.ATForum.Models.Category;

public class CreateCategoryActivity extends MenuActivity {
    // The interface to access the database
    IDataAccess dataAccess;

    // The views that contains the text to edit
    EditText txtCatName, txtCatDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_category);

        Slidr.attach(this);

        dataAccess = DataAccessFactory.getInstance();

        txtCatName = findViewById(R.id.etNewCatName);
        txtCatDescription = findViewById(R.id.etNewCatDescription);

        Button btnSubmit = findViewById(R.id.submitNewCat);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createCategory();
            }
        });
    }

    /**
     * Calls the createCategory method on the dataAccess interface and then closes the CreateCategory intent when it is finished
     */
    private void createCategory() {
        final String catNameString = txtCatName.getText().toString();
        final String catDescriptionString = txtCatDescription.getText().toString();

        Map<String, Object> category = new HashMap<>();
        category.put("categoryName", catNameString);
        category.put("description", catDescriptionString);

        dataAccess.createCategory(category, new IDataAccess.IOnResult<Category>() {
            @Override
            public void onResult(Category category) {
                Intent result = new Intent();
                result.putExtra("newCategory", category);
                setResult(RESULT_OK, result);
                finish();
            }
        });

    }
}
