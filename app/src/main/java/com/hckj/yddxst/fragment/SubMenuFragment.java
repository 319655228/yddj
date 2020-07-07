package com.hckj.yddxst.fragment;

import android.view.View;

import com.hckj.yddxst.R;
import com.hckj.yddxst.activity.LoginActivity;
import com.hckj.yddxst.utils.SpUtil;

import butterknife.OnClick;

/*
 * 描述: 三会一课主持人子菜单 <br>
 * 日期: 2019-09-22 13:15 <br>
 * 作者: 林明健 <br>
 */
public class SubMenuFragment extends BaseFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_sub_menu;
    }

    static SubMenuFragment newInstance() {
        return new SubMenuFragment();
    }

    @Override
    protected void lazyLoad() {

    }

    @OnClick({R.id.menu_back, R.id.menu_zbywdh, R.id.menu_zwh, R.id.menu_dxzh, R.id.menu_dk, R.id.menu_rlsb, R.id.menu_rdlc})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.menu_back:  // 回到首页
                onBackPressed();
                break;
            case R.id.menu_zbywdh: // 支部委员大会
                replaceFragment(MeetingFragment.newInstance("党员大会模块"));
                break;
            case R.id.menu_zwh: // 支委会
                replaceFragment(MeetingFragment.newInstance("支委会模块"));
                break;
            case R.id.menu_dxzh: // 党小组会
                replaceFragment(MeetingFragment.newInstance("党小组会模块"));
                break;
            case R.id.menu_dk: // 党课讲师
                replaceFragment(PartyClassFragment.newInstance());
                break;
            case R.id.menu_rdlc:  // 入党流程
                replaceFragment(MeetingFragment.newInstance("入党流程"));
                break;
            case R.id.menu_rlsb:  // 人脸识别
                if (checkAuth("face_check")) {
                    startActivity(LoginActivity.class);
                }
                break;
        }
    }

    /**
     * @param tag 检测是否具有权限
     */
    private boolean checkAuth(String tag) {
        String status = SpUtil.get(getAppActivity(), tag, "");
        if ("1".equals(status)) {
            return true;
        } else {
            showAlertDialog("您的套餐暂时不支持该功能。如有疑问，请联系供应商。");
            return false;
        }
    }
}
