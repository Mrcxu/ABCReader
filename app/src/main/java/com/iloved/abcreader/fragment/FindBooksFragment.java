package com.iloved.abcreader.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.iloved.abcreader.R;
import com.iloved.abcreader.activity.About;
import com.iloved.abcreader.activity.ClassifyActivity;
import com.iloved.abcreader.adapter.AdapterBookFind;
import com.iloved.abcreader.interfaces.OnItemClickListener;

import java.util.ArrayList;

/**
 * Created by WZBC on 2018/5/16.
 */

public class FindBooksFragment extends Fragment {
    public FindBooksFragment(){}

    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private AdapterBookFind bookAdapterBookFind;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final ArrayList<String> bookClass = new ArrayList<>();
                bookClass.add("玄 幻");
                bookClass.add("武 侠");
                bookClass.add("都 市");
                bookClass.add("历 史");
                bookClass.add("游 戏");
                bookClass.add("科 幻");
                bookClass.add("女 生");
                bookClass.add("灵 异");

        View rootview = inflater.inflate(R.layout.pager_book_find,container,false);
        recyclerView = rootview.findViewById(R.id.recyler_view_find_book);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                     recyclerView.setLayoutManager(staggeredGridLayoutManager);
                     bookAdapterBookFind = new AdapterBookFind(bookClass);
                     recyclerView.setAdapter(bookAdapterBookFind);



        bookAdapterBookFind.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int id) {
                Log.d("123456789", "onItemClick: "+ bookClass.get(id));
                Intent intent = new Intent(getActivity(),ClassifyActivity.class);
                intent.putExtra("type",bookClass.get(id));
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(int id) {

            }
        });

        return rootview;
    }
}
