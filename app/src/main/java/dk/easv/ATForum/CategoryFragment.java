package dk.easv.ATForum;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import com.example.forum.R;

import java.util.List;

import dk.easv.ATForum.Adapters.CategoryAdapter;
import dk.easv.ATForum.Interfaces.IDataAccess;
import dk.easv.ATForum.Models.Category;
import dk.easv.ATForum.Models.Role;
import dk.easv.ATForum.Models.User;


public class CategoryFragment extends ListFragment {

    private static final String TAG = "XYZ";
    private CategoryAdapter categoryAdapter;
    private List<Category> categoryList;
    private ListView categoryListView;
    private Role role;
    private User currentUser;

    private IDataAccess dataAccess;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        role = (Role) getActivity().getIntent().getSerializableExtra("role");
        Log.d(TAG, "onCreateView: " + role);
        currentUser = (User) getActivity().getIntent().getSerializableExtra("currentUser");
        return inflater.inflate(R.layout.fragment_category, container, false);
    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dataAccess = DataAccessFactory.getInstance();
        categoryListView = getListView();
        dataAccess.getAllCategories(new IDataAccess.IONCategoriesResult() {
            @Override
            public void onResult(List<Category> categories) {
                categoryList = categories;
                categoryAdapter = new CategoryAdapter(getActivity(), R.layout.category_cell, categoryList);
                setListAdapter(categoryAdapter);
                Log.d(TAG, "Users: " + categoryList.get(0));
            }
        });
        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                categoryListView.setLongClickable(true);
                Category category = categoryList.get(position);
                String uid = category.getUid();
                if (!role.getRoleName().equals("user")) {
                        dataAccess.deleteCategory(uid);
                        Toast.makeText(getActivity(), "Deleted category with id: " + uid, Toast.LENGTH_SHORT).show();
                    }
                return true;
            }
        });
    }

    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
