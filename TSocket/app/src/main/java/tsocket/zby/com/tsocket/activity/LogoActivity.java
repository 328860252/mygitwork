package tsocket.zby.com.tsocket.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import tsocket.zby.com.tsocket.AppString;
import tsocket.zby.com.tsocket.utils.SharedPerfenceUtils;

public class LogoActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Observable.timer(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        //退出全屏
                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
                                WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                        Intent intent;
                        if (SharedPerfenceUtils.getSetupData(LogoActivity.this)
                                .readBoolean(AppString.FIRST, true)) {
                            intent = new Intent(LogoActivity.this, GuideViewActivity.class);
                        } else {
                            intent = new Intent(LogoActivity.this, DeviceListActivity.class);
                        }
                        startActivity(intent);
                        finish();
                    }
                });
    }
}
