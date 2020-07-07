package com.hckj.yddxst.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.blankj.rxbus.RxBus;
import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.SPUtils;
import com.hckj.yddxst.Constant;
import com.hckj.yddxst.R;
import com.hckj.yddxst.activity.CalendarActivity;
import com.hckj.yddxst.activity.DJZXActivity;
import com.hckj.yddxst.activity.DocClassifyActvity;
import com.hckj.yddxst.activity.FollowHeartActivity;
import com.hckj.yddxst.activity.PartyOathActivity;
import com.hckj.yddxst.activity.PoliticalPhotoActivity;
import com.hckj.yddxst.activity.RLActivity;
import com.hckj.yddxst.activity.RedTourActivity;
import com.hckj.yddxst.activity.SXKActivity;
import com.hckj.yddxst.bean.MenuAuthInfo;
import com.hckj.yddxst.bean.SpeakInfo;
import com.hckj.yddxst.net.BaseResponse;
import com.hckj.yddxst.net.RxException;
import com.hckj.yddxst.net.RxHelper;
import com.hckj.yddxst.net.YddxObserver;
import com.hckj.yddxst.task.CalendarListTask;
import com.hckj.yddxst.tts.BakerTtsHandler;
import com.hckj.yddxst.utils.CommonUtils;
import com.hckj.yddxst.utils.FileIOUtils;
import com.hckj.yddxst.utils.SpUtil;
import com.hckj.yddxst.widget.BubbleLayout;
import com.hckj.yddxst.widget.YddxSettingDialog;
import com.rxjava.rxlife.RxLife;
import com.tencent.bugly.beta.Beta;

import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * 描述: 首页菜单Fragment <br>
 * 日期: 2019-09-22 13:03 <br>
 * 作者: 林明健 <br>
 */
public class MenuFragment extends BaseFragment {
    @BindView(R.id.et_device_num)
    EditText etDeviceNum;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @BindView(R.id.layout_device_num)
    LinearLayout layoutDeviceNum;
    @BindView(R.id.menu_znjjy)
    ImageView menuZzwy;
    @BindView(R.id.menu_xsms)
    ImageView menuXsms;
    @BindView(R.id.menu_khms)
    ImageView menuKhms;
    //    @BindView(R.id.menu_hszl)
//    ImageView menuHszl;
    //    @BindView(R.id.menu_sphy)
//    ImageView menuSphy;
//    @BindView(R.id.menu_sjbg)
//    ImageView menuSjbg;
    @BindView(R.id.menu_setting)
    ImageView menuSetting;
    @BindView(R.id.layout_sub_menu)
    RelativeLayout layoutSubMenu;
    //    @BindView(R.id.menu_shyk)
//    ImageView menuShyk;
//    @BindView(R.id.menu_djzs)
//    ImageView menuDjzs;
    @BindView(R.id.menu_xxrd)
    ImageView menuXxrd;
    //    @BindView(R.id.menu_gwx)
//    ImageView menuGwx;
    @BindView(R.id.layout_bubble)
    BubbleLayout layoutBubble;
    @BindView(R.id.menu_zzsrly)
    ImageView menuZzsrly;
    @BindView(R.id.menu_dwgwx)
    ImageView menuDwgwx;
    @BindView(R.id.menu_djbk)
    ImageView menuDjbk;
    @BindView(R.id.menu_ghghszl)
    ImageView menuGhghszl;
    @BindView(R.id.menu_zsjzj)
    ImageView menuZsjzj;
    @BindView(R.id.menu_djzhzx)
    ImageView menuDjzhzx;
    @BindView(R.id.menu_jrsxk)
    ImageView menuJrsxk;
    private List<SpeakInfo> mSpeakList;
    private long lastTimeMills;

    public static MenuFragment newInstance() {
        return new MenuFragment();
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    public void onResume() {
        super.onResume();
        lastTimeMills = System.currentTimeMillis();
        stopSpeech();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_menu;
    }

    @Override
    public void onDestroy() {
        RxBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        request().getSpeakList()
                .compose(RxHelper.io2m())
                .as(RxLife.as(this))
                .subscribe(new YddxObserver<List<SpeakInfo>>(getAppActivity(), false) {
                    @Override
                    public void onSuccess(List<SpeakInfo> res) {
                        mSpeakList = res;
                    }

                    @Override
                    public void onFailure(Throwable e, String err) {

                    }
                });
        // 订阅首页返回事件
        RxBus.getDefault().subscribe(this, RxHelper.TAG_INDEX_BACK, new RxBus.Callback<String>() {
            @Override
            public void onEvent(String str) {
                hideSubMenu();
            }
        });

        // 订阅音频开始播放事件， 如果当前Fragment处于可见状态，并且存在动作，执行动作 显示气泡框
        RxBus.getDefault().subscribe(this, RxHelper.TAG_SPEECH_BEGIN, new RxBus.Callback<String>() {
            @Override
            public void onEvent(String actPath) {
                if (isVisible() && !TextUtils.isEmpty(actPath)) {
                    switchAnim(actPath);
                    showBubble();
                }
            }
        });

        // 订阅音频播放结束事件， 记录最后播放时间，隐藏气泡框
        RxBus.getDefault().subscribe(this, RxHelper.TAG_SPEECH_COMPLETE, new RxBus.Callback<String>() {
            @Override
            public void onEvent(String s) {
                lastTimeMills = System.currentTimeMillis();
                hideBubble();
            }
        });

        // 订阅主页点击事件, 切换气泡
        RxBus.getDefault().subscribe(this, RxHelper.TAG_INDEX_TOUCH, new RxBus.Callback<String>() {
            @Override
            public void onEvent(String str) {
                if (isVisible()) {
                    switchBubble();
                }
            }
        });

        Observable.interval(30, 10, TimeUnit.SECONDS)
                .filter(aLong -> {
                    boolean canAct = isVisible() && mSpeakList != null && !mSpeakList.isEmpty();
                    if (canAct && !BakerTtsHandler.isPlaying() && System.currentTimeMillis() - lastTimeMills < 10000) {
                        FileIOUtils.deleteDir(Constant.FILE_DIR);
                    }
                    return canAct;
                })
                .filter(aLong -> isVisible() && mSpeakList != null && !mSpeakList.isEmpty() && getAppActivity() != null && getAppActivity().isIndexForeground())
                .compose(RxHelper.io2m())
                .as(RxLife.as(this))
                .subscribe(aLong -> {
                    boolean menuSilent = SPUtils.getInstance().getBoolean("MENU_SILENT", false);
                    String randomAct = CommonUtils.getRandomAction(menuSilent);
                    if (menuSilent) {
                        switchAnim(randomAct);
                    } else {
                        if (BakerTtsHandler.isPlaying()) {
                            switchAnim(randomAct);
                        } else if ((lastTimeMills == 0 || System.currentTimeMillis() - lastTimeMills > 10000)) {
                            switchBubble();
                        }
                    }
                }, throwable -> {

                });

    }

    @Override
    protected void initView() {
        super.initView();
        layoutBubble.setOnClickListener(v -> switchBubble());
    }

    /**
     * 描述: 切换气泡框 <br>
     * 日期: 2020-03-02 21:56 <br>
     * 作者: 林明健 <br>
     */
    private void switchBubble() {
        if (mSpeakList == null || mSpeakList.isEmpty())
            return;
        boolean menuSilent = SPUtils.getInstance().getBoolean("MENU_SILENT", false);
        String randomAct = CommonUtils.getRandomAction(menuSilent);
        int size = mSpeakList.size();
        String speak = mSpeakList.get((int) (Math.random() * size)).getSpeak();
        stopSpeech();
//        startSpeech(speak, randomAct);
        layoutBubble.setText(speak);
    }


    @OnLongClick({R.id.menu_setting})
    public void onViewLongClicked(View view) {
        switch (view.getId()) {
            case R.id.menu_setting:
                showAlertDialog(DeviceUtils.getUniqueDeviceId());
                break;
            default:
                break;
        }
    }

    @OnClick({R.id.menu_djbk, R.id.menu_jrsxk,R.id.btn_confirm, R.id.btn_cancel, R.id.menu_zsjzj, R.id.menu_dwgwx, R.id.menu_znjjy, R.id.menu_xsms, R.id.menu_khms, R.id.menu_ghghszl, R.id.menu_setting, R.id.menu_xxrd, R.id.menu_zzsrly, R.id.menu_djzhzx})
    public void onViewClicked(View view) {
        int viewId = view.getId();

//        if (viewId != R.id.menu_party_center) {
//            stopSpeech();
//            hideSubMenu();
//        }

        switch (viewId) {
//            case R.id.menu_party_center:
//                toggleSubMenu();
//                break;
            case R.id.menu_jrsxk://总书记思想库
                SXKActivity.startActivity(getAppActivity());
                break;
            case R.id.menu_djzhzx: // 智慧党建中心
                DJZXActivity.startActivity(getAppActivity());
                break;
//            case R.id.menu_zsjzj: // 总书记足迹
//                if (checkAuth("news")) {
//                    replaceFragment(new NewsFragment());
//                }
//                break;
            case R.id.menu_zsjzj: // 总书记足迹
                CalendarActivity.startActivity(getAppActivity());
                break;
            case R.id.menu_zzsrly: // 政治生日留影
                PoliticalPhotoActivity.startActivity(getAppActivity());
                break;
            case R.id.menu_znjjy: //  智能讲解员
                if (checkAuth("invented_servant")) {
                    replaceFragment(new BuildingFragment());
                }
                break;
            case R.id.menu_xsms: // 宣誓模式
                if (checkAuth("oath_mode")) {
                    PartyOathActivity.startActivity(getAppActivity());
                }
                break;
            case R.id.menu_khms: // 考核模式
                if (checkAuth("examine_mode")) {
                    replaceFragment(CheckModelSubMenuFragment.newInstance());
                }
                break;
            case R.id.menu_ghghszl: // 共和国之旅
//                if (checkAuth("red_trip")) {
                RedTourActivity.startActivity(getAppActivity());
//                }
                break;
//            case R.id.menu_sphy:  // 设置可连接设备
//                if (checkAuth("remote_meeting")) {
//                    showToast("暂无可连接设备");
//                    startSpeech("暂无可连接设备");
//                }
//                break;
//            case R.id.menu_sjbg:  // 大数据报告
//                if (checkAuth("data_summary")) {
//                    OnlineCheckActivity.startActivity(getAppActivity(), BaseApi.STATISTICS_URL);
//                }
//                break;
            case R.id.menu_setting: // 显示设置对话框
                showSettingDialog();
                break;
//            case R.id.menu_shyk:// 三会一课
//                if (checkAuth("meeting")) {
//                    replaceFragment(SubMenuFragment.newInstance());
//                }
//                break;
            case R.id.menu_dwgwx://公文箱
//                if (checkAuth("document")) {
                DocClassifyActvity.startActivity(getAppActivity());
//                }
                break;
//            case R.id.menu_djzs: // 智能讲解员
//                if (checkAuth("ai_helper")) {
//                    replaceFragment(QuestionFragment.newInstance());
//                }
//                break;
            case R.id.menu_xxrd: // 学习热点
                if (checkAuth("study_hot")) {
                    startActivity(FollowHeartActivity.class);
                }

                break;
            case R.id.btn_confirm: // 请求设备认证
                requestAuth();
                break;
            case R.id.btn_cancel:  // 取消设备认证
                layoutDeviceNum.setVisibility(View.GONE);
                break;
//            case R.id.menu_gwx:
//                if (checkAuth("document")) {
//                    DocClassifyActvity.startActivity(getAppActivity());
//                }
//                break;
            case R.id.menu_djbk://党建百科
                replaceFragment(BaikeClassifyFragment.newInstance(null));
                break;
            default:
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

    /*
     * 描述: 设备号认证 <br>
     * 日期: 2019-09-22 13:05 <br>
     * 作者: 林明健 <br>
     * @return void
     */
    private void requestAuth() {
        String deviceNum = etDeviceNum.getText().toString().trim();
        if (TextUtils.isEmpty(deviceNum)) {
            showToast("请输入正确的设备编号");
            return;
        }
        request().getDeviceAuth(deviceNum)
                .flatMap((Function<BaseResponse<String>, ObservableSource<BaseResponse<List<MenuAuthInfo>>>>) baseResp -> {
                    //接口不规范......
                    if ("true".equals(baseResp.getData())) {
                        return request().getMenuAuth(deviceNum);
                    }
                    return Observable.error(new RxException(baseResp.getMessage()));
                })
                .compose(RxHelper.io2m())
                .as(RxLife.as(this))
                .subscribe(new YddxObserver<List<MenuAuthInfo>>(getAppActivity(), false) {
                    @Override
                    public void onSuccess(List<MenuAuthInfo> res) {
                        SpUtil.save(getAppActivity(), SpUtil.DEVICE_NUM, deviceNum);
                        layoutDeviceNum.setVisibility(View.GONE);
                        showToast("设置成功");
                        if (res != null && !res.isEmpty()) {
                            for (MenuAuthInfo info : res) {
                                SpUtil.save(getAppActivity(), info.getTag(), info.getStatus());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String err) {
                        showAlertDialog(err);
                    }
                });
    }

    /*
     * 描述: 切换子菜单显示状态 <br>
     * 日期: 2019-09-22 13:06 <br>
     * 作者: 林明健 <br>
     */
//    private void toggleSubMenu() {
//        if (layoutSubMenu.getVisibility() == View.VISIBLE) {
//            layoutSubMenu.setVisibility(View.GONE);
//            menuPartyCenter.setImageResource(R.drawable.bg_bot_zhdjzx);
//        } else {
//            layoutSubMenu.setVisibility(View.VISIBLE);
//            menuPartyCenter.setImageResource(R.drawable.bg_bot_zhdjzx_press);
//        }
//    }

    /**
     * 描述: 显示设置对话框、 <br>
     * 日期: 2019-09-22 13:06 <br>
     * 作者: 林明健 <br>
     */
    private void showSettingDialog() {
        YddxSettingDialog dialog = new YddxSettingDialog(getAppActivity());
        dialog.setOnItemClickLisenter(new YddxSettingDialog.onItemClickLisenter() {
            @Override
            public void onAuthClick(Dialog dialog) {
                etDeviceNum.setText("");
                layoutDeviceNum.setVisibility(View.VISIBLE);
                dialog.dismiss();
            }

            @Override
            public void onUpdateClick(Dialog dialog) {
                Beta.checkUpgrade();
                dialog.dismiss();
            }

            @Override
            public void onQuitClick(Dialog dialog) {
                dialog.dismiss();
            }

            @Override
            public void onSilenceClick(Dialog dialog, boolean isSilent) {
                SPUtils.getInstance().put("MENU_SILENT", isSilent);
                dialog.dismiss();
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    /*
     * 描述: 隐藏党建中心子菜单 <br>
     * 日期: 2019-09-22 13:04 <br>
     * 作者: 林明健 <br>
     */
    private void hideSubMenu() {
        if (layoutSubMenu.getVisibility() == View.VISIBLE) {
            layoutSubMenu.setVisibility(View.GONE);
//            menuPartyCenter.setImageResource(R.drawable.bg_bot_zhdjzx);
        }
    }

    private void showBubble() {
        if (layoutBubble.getVisibility() != View.VISIBLE) {
            layoutBubble.setVisibility(View.VISIBLE);
        }
    }

    private void hideBubble() {
        if (layoutBubble.getVisibility() == View.VISIBLE) {
            layoutBubble.setVisibility(View.GONE);
        }
    }

    @Override
    protected void stopSpeech() {
        super.stopSpeech();
        hideBubble();
    }
}
