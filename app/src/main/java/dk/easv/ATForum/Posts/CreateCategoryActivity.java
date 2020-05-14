package dk.easv.ATForum.Posts;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.forum.R;

import java.util.HashMap;
import java.util.Map;

import dk.easv.ATForum.DataAccessFactory;
import dk.easv.ATForum.Interfaces.IDataAccess;
import dk.easv.ATForum.MenuActivity;
import dk.easv.ATForum.Models.Category;

public class CreateCategoryActivity extends MenuActivity {
    IDataAccess dataAccess;
    EditText txtCatName, txtCatDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_category);

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

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void createCategory() {
        final String catNameString = txtCatName.getText().toString();
        final String catDescriptionString = txtCatDescription.getText().toString();

        Map<String, Object> category = new HashMap<>();
        category.put("categoryName", catNameString);
        category.put("description", catDescriptionString);

        dataAccess.createCategory(category, new IDataAccess.IONCategoryResult() {
            @Override
            public void onResult(Category category) {
                finish();
            }
        });

    }
}
