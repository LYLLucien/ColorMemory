package com.lucien.colormemory.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.lucien.colormemory.R;
import com.lucien.colormemory.dao.DBDao;
import com.lucien.colormemory.view.adapter.RankCursorAdapter;

public class RankActivity extends BaseActivity implements View.OnClickListener {

    private DBDao dbDao;
    private ImageView btnBack;
    private ListView lvRank;
    private RankCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        initConfig();
        initView();
    }

    private void initConfig() {
        dbDao = DBDao.getDBDaoInstance(this);
    }

    private void initView() {
        btnBack = (ImageView) this.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
        lvRank = (ListView) this.findViewById(R.id.lvRank);
        adapter = new RankCursorAdapter(this, dbDao.getRank(), new String[]{"_id", "name", "score"});
        lvRank.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnBack:
                finish();
                break;
            default:
                break;
        }
    }
}
