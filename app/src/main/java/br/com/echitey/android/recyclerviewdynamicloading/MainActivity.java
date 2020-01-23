package br.com.echitey.android.recyclerviewdynamicloading;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.echitey.android.recyclerviewdynamicloading.adapter.MyAdapter;
import br.com.echitey.android.recyclerviewdynamicloading.interfaces.ILoadMore;
import br.com.echitey.android.recyclerviewdynamicloading.model.Item;

public class MainActivity extends AppCompatActivity {

    List<Item> items = new ArrayList<>();
    MyAdapter adapter;
    private int INIT = 0, STEP = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //RANDOM DATA
        getData(INIT, STEP);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyAdapter(recyclerView, this, items);
        recyclerView.setAdapter(adapter);

        // Set Load more Event
        adapter.setiLoadMore(new ILoadMore() {
            @Override
            public void onLoadMore() {
                if(items.size() < 60){ //  MAX ITENS
                    items.add(null);
                    adapter.notifyItemInserted(items.size()-1);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            items.remove(items.size()-1);
                            adapter.notifyItemRemoved(items.size());

                            //More Random Data
                            int index = items.size();
                            int end = index + STEP;
                            getData(index, end);

                            adapter.notifyDataSetChanged();
                            adapter.setLoaded();
                        }
                    }, 5000);
                } else {
                    Toast.makeText(MainActivity.this, "Loading Complete!", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void getData(int index, int end) {
        for(int i=index; i<end; i++){
            String name = UUID.randomUUID().toString();
            Item item = new Item(name, i+1);
            items.add(item);
        }
    }
}
