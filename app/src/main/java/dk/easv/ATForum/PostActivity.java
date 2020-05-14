package dk.easv.ATForum;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.example.forum.R;

public class PostActivity extends MenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Bundle bundle = new Bundle();
        bundle.putSerializable("currentUser", currentUser);
        bundle.putSerializable("role", role);
        CategoryFragment catFrag = new CategoryFragment();
        catFrag.setArguments(bundle);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        categoryMenuItem.setVisible(false);
        categoryMenuItem.setEnabled(false);
        return true;
    }

    @Override
    public void invalidateOptionsMenu() {
        super.invalidateOptionsMenu();
    }
}
