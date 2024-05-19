package hu.unideb.inf.yourcakeactualdraft.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import hu.unideb.inf.yourcakeactualdraft.Adapter.CakeListAdapter;
import hu.unideb.inf.yourcakeactualdraft.Domain.Cakes;
import hu.unideb.inf.yourcakeactualdraft.R;
import hu.unideb.inf.yourcakeactualdraft.databinding.ActivityListCakesBinding;

public class ListCakesActivity extends BaseActivity {
    ActivityListCakesBinding binding;
    private RecyclerView.Adapter adapterListCake;
    private int categoryId;
    private String categoryName;
    private String searchText;
    private boolean isSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListCakesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        getIntentExtra();
        initList();

    }




    private void initList() {
        DatabaseReference myRef = database.getReference("Cakes");
        binding.progressBar.setVisibility(View.VISIBLE);
        ArrayList<Cakes> list = new ArrayList<>();

        Query query;
        if (isSearch) {
            query = myRef.orderByChild("Title").startAt(searchText).endAt(searchText + '\uf8ff');

        } else {
            query = myRef.orderByChild("CategoryId").equalTo(categoryId);

        }
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot issue: snapshot.getChildren()){
                        list.add(issue.getValue(Cakes.class));
                    }
                    if (list.size()>0){
                        binding.cakeListView.setLayoutManager(new GridLayoutManager(ListCakesActivity.this,2));
                        adapterListCake=new CakeListAdapter(list);
                        binding.cakeListView.setAdapter(adapterListCake);
                    }
                    binding.progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getIntentExtra() {
        categoryId = getIntent().getIntExtra("CategoryId", 0);
        categoryName = getIntent().getStringExtra("CategoryName");
        searchText = getIntent().getStringExtra("text");
        isSearch = getIntent().getBooleanExtra("isSearch", false);

        binding.titleTxt.setText(categoryName);
        binding.backBtn.setOnClickListener(v -> finish());
    }
}