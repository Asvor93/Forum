package dk.easv.ATForum;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import com.example.forum.R;

import java.util.List;

import dk.easv.ATForum.Adapters.CategoryAdapter;
import dk.easv.ATForum.Interfaces.IDataAccess;
import dk.easv.ATForum.Models.Category;
import dk.easv.ATForum.Models.User;
import dk.easv.ATForum.Models.UserAdapter;


public class CategoryFragment extends ListFragment {

    private static final String TAG = "XYZ";
    CategoryAdapter categoryAdapter;
    List<Category> categoryList;
    ListView categoryListView;

    Category currentCat;

    private IDataAccess dataAccess;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
        dataAccess.getAllCategories(currentCat.getUid(), new IDataAccess.IONCategoriesResult() {
            @Override
            public void onResult(List<Category> categories) {
                categoryList = categories;
                categoryAdapter = new CategoryAdapter(getActivity(), R.layout.category_Cell, categoryList);
                categoryListView.setAdapter(categoryAdapter);
                setListAdapter(categoryAdapter);
                Log.d(TAG, "Users: " + categoryList.get(0));
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
