package com.hy0417sage.notes;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hy0417sage.notes.NotesAdapter.ImageAdapter;
import com.hy0417sage.notes.NotesAdapter.NotesData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 기능2. 메모 상세보기
 * **/

public class Details extends Fragment {

    Interface activity;

    TextView text_title;
    TextView text_content;
    TextView img_count;

    List<NotesData> memo_list = new ArrayList<>();
    LinearLayoutManager horizonalLayoutManager;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout fragment_Details = (LinearLayout) inflater.inflate(R.layout.details, container, false);
        setHasOptionsMenu(true);
        Toolbar toolbar = fragment_Details.findViewById(R.id.toolbar);
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);

        activity = (Interface) getActivity();

        text_title = (TextView) fragment_Details.findViewById(R.id.text_title);
        text_content = (TextView) fragment_Details.findViewById(R.id.text_content);
        img_count = (TextView) fragment_Details.findViewById(R.id.img_count);

        horizonalLayoutManager
                = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        recyclerView = (RecyclerView) fragment_Details.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setLayoutManager(horizonalLayoutManager);

        //1. 작성된 메모의 제목과 본문을 볼수 있습니다.
        text_title.setText(activity.title);
        text_content.setText(activity.content);
        text_content.setMovementMethod(new ScrollingMovementMethod());

        /* 2. 메모에 첨부되어있는 이미지를 볼 수 있습니다.
         * CreateAndEdit을 통해 메모에 첨부되어있는 이미지를 ImageAdapter로 볼 수 있습니다. */
        memo_list.clear();
        for (int i = 0; i < activity.url.size(); i++) {
            NotesData memo_data = new NotesData(activity.url.get(i), "Details");
            memo_list.add(memo_data);
        }

        adapter = new ImageAdapter(getActivity().getApplicationContext(), memo_list);
        recyclerView.setAdapter(adapter);

        return fragment_Details;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(getActivity()).invalidateOptionsMenu();
        img_count.setText("사진 개수 : " + activity.url.size() + "개");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.details_menu, menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            /* 메모를 저장하자마자 메모 리스트를 통하지 않고 메모를 수정, 삭제하기 위해
             * 로컬영역의 마지막에 저장되어있는 데이터를 불러옵니다. */

            //3. 상단 메뉴를 통해 메모 내용을 편집할 수 있습니다.
            case R.id.edit_button:
                if (activity.nowIndex == 0) {
                    activity.first_data_edit_delete();
                }
                activity.onFragmentChange("CreateAndEdit");
                break;

            //3. 상단 메뉴를 통해 메모 내용을 삭제할 수 있습니다.
            case R.id.delete_button:
                if (activity.nowIndex == 0) {
                    activity.first_data_edit_delete();
                    activity.mDBHelper.deleteColumn(activity.nowIndex);
                } else {
                    activity.data_delete();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
