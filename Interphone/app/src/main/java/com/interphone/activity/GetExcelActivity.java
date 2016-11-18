package com.interphone.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.administrator.interphone.R;
import com.interphone.adapter.FileNameAdapter;
import com.interphone.adapter.StringAdapter;
import com.interphone.utils.ExcelUtil;
import java.util.List;

/**
 * 获取sd卡里的 excel文件列表
 */
public class GetExcelActivity extends BaseActivity {

  @Bind(R.id.tv_title) TextView mTvTitle;
  @Bind(R.id.listView) ListView mListView;

  private FileNameAdapter mStringAdapter;
  private List<String> mFileList;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_get_excel);
    ButterKnife.bind(this);
    initBaseViews(this);
    initViews();
  }

  private void initViews() {
    mFileList = ExcelUtil.getInstance().readExcelList();
    mStringAdapter = new FileNameAdapter(mFileList, this);
    mListView.setAdapter(mStringAdapter);
    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.putExtra("filePath", mFileList.get(position));
        setResult(RESULT_OK, intent);
        finish();
      }
    });
  }

  @OnClick(R.id.layout_title_left) public void onClick() {
    finish();
  }
}
