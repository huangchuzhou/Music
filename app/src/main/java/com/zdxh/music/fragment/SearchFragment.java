package com.zdxh.music.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.zdxh.music.R;
import com.zdxh.music.activity.SearchListAty;
import com.zdxh.music.flowlayout.FlowLayout;
import com.zdxh.music.util.DataCallBackListener;

/**
 * Created by huangchuzhou on 2016/4/8.
 * 搜索页面
 */
public class SearchFragment extends Fragment implements View.OnClickListener {

    private FlowLayout mFlowLayout;
    private EditText etSearch;
    private ImageButton btnSearch;
    private String[] hotLabels = {"洋葱","那些年","See you Again","Every Time","小幸运","always"};
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment_layout,container,false);
        mFlowLayout = (FlowLayout) view.findViewById(R.id.flowLayout);
        etSearch = (EditText) view.findViewById(R.id.etSearch);
        btnSearch = (ImageButton) view.findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(this);

        init();
        return view;
    }

    private void init() {
        for (String hotLabel:hotLabels){
            addHotLable(hotLabel, new DataCallBackListener() {
                @Override
                public void onFinish(String hotLabelText) {
                    etSearch.setText(hotLabelText);
                    //把光标移到最后
                    etSearch.setSelection(etSearch.getText().length());
                }
            });
        }
    }

    private void addHotLable(String hotLabel,final DataCallBackListener listener) {
        final TextView tvHotLabel = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.tv_hot_label, mFlowLayout, false);
        tvHotLabel.setText(hotLabel);
        tvHotLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onFinish(tvHotLabel.getText().toString());
            }
        });

        mFlowLayout.addView(tvHotLabel);
    }

    @Override
    public void onClick(View v) {
        //此处的fragment与另一个activity进行通信
        Intent intent = new Intent(getActivity(), SearchListAty.class);
        intent.putExtra("searchName", etSearch.getText().toString());
        if (TextUtils.isEmpty(etSearch.getText().toString())){
            Toast.makeText(getActivity(),"输入不能为空",Toast.LENGTH_SHORT).show();
        }else{

            getActivity().startActivity(intent);
        }

    }
}
