package com.hckj.yddxst.fragment;

import android.view.View;
import android.widget.ImageView;

import com.hckj.yddxst.R;
import com.hckj.yddxst.activity.OnlineCheckActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 描述: 测评模式子菜单 <br>
 * 日期: 2019-11-27 23:26 <br>
 * 作者: 林明健 <br>
 */
public class CheckModelSubMenuFragment extends BaseFragment {
    @BindView(R.id.menu_back)
    ImageView menuBack;
    @BindView(R.id.menu_djzshd)
    ImageView menuDjzshd;
    @BindView(R.id.menu_ghtj)
    ImageView menuGhtj;

    static CheckModelSubMenuFragment newInstance() {
        return new CheckModelSubMenuFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_check_model_sub;
    }

    @Override
    protected void lazyLoad() {

    }


    @OnClick({R.id.menu_back, R.id.menu_djzshd, R.id.menu_ghtj})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.menu_back: // 回到首页
                onBackPressed();
                break;
            case R.id.menu_djzshd: // 党建知识互动
                startActivity(OnlineCheckActivity.class);
                break;
            case R.id.menu_ghtj:
                replaceFragment(PlanFragment.newInstance());
                break;
        }
    }
}
