package tsocket.zby.com.tsocket.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import java.util.ArrayList;
import java.util.List;
import tsocket.zby.com.tsocket.AppConstants;
import tsocket.zby.com.tsocket.AppString;
import tsocket.zby.com.tsocket.R;
import tsocket.zby.com.tsocket.adapter.LanguageAdapter;
import tsocket.zby.com.tsocket.utils.SharedPerfenceUtils;
import tsocket.zby.com.tsocket.utils.Tools;
import tsocket.zby.com.tsocket.view.HeaderLayout;

/**
 * @author Administrator
 */
public class SettingLanguageActivity extends BaseActivity {

  @BindView(R.id.layout_header) HeaderLayout mLayoutHeader;
  @BindView(R.id.listView) ListView mListView;
  private SharedPerfenceUtils mSetupData;

  private List<String> list;
  private LanguageAdapter wAdapter;
  private int language_item;

  @Override protected void onCreate(Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_setting_language);
    ButterKnife.bind(this);
    init();
  }

  private void init() {
    mSetupData = SharedPerfenceUtils.getSetupData(this);
    language_item = mSetupData.readInt(AppString.language, AppConstants.language_default);
    if (list == null) {
      list = new ArrayList<String>();
      list.add(getString(R.string.language_zh));
      list.add(getString(R.string.language_en));
    }
    if (wAdapter == null) {
      wAdapter = new LanguageAdapter(this, list, language_item);
    } else {
      wAdapter.updateIndex(language_item);
    }
    mListView.setAdapter(wAdapter);
    mListView.setOnItemClickListener(new OnItemClickListener() {

      @Override public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // TODO Auto-generated method stub
        language_item = arg2;
        wAdapter.updateIndex(language_item);
      }
    });
  }

  @Override protected void onLanguageChange() {
    // TODO Auto-generated method stub
    mLayoutHeader.setTextTitle(R.string.text_language);
    wAdapter.notifyDataSetChanged();
  }

  @OnClick(R.id.layout_title_right) public void onBack() {
    finish();
  }

  @OnClick(R.id.layout_title_right) public void onSave() {
    mSetupData.saveInt(AppString.language, language_item);
    Tools.switchLanguage(SettingLanguageActivity.this, language_item);
    finish();
  }
}
