package com.hckj.yddxst.net;

import com.hckj.yddxst.bean.AnswerInfo;
import com.hckj.yddxst.bean.BaikeClassifyInfo;
import com.hckj.yddxst.bean.BaikeInfo;
import com.hckj.yddxst.bean.BuildingDetailInfo;
import com.hckj.yddxst.bean.BuildingInfo;
import com.hckj.yddxst.bean.ClassifyInfo;
import com.hckj.yddxst.bean.CourseInfo;
import com.hckj.yddxst.bean.DocClassifyInfo;
import com.hckj.yddxst.bean.DocInfo;
import com.hckj.yddxst.bean.ImgUploadInfo;
import com.hckj.yddxst.bean.LoginInfo;
import com.hckj.yddxst.bean.MeetingClassifyInfo;
import com.hckj.yddxst.bean.MeetingContentInfo;
import com.hckj.yddxst.bean.MeetingInfo;
import com.hckj.yddxst.bean.MeetingInfo2;
import com.hckj.yddxst.bean.MeetingStartInfo;
import com.hckj.yddxst.bean.MenuAuthInfo;
import com.hckj.yddxst.bean.NewClassifyInfo;
import com.hckj.yddxst.bean.NewsInfo;
import com.hckj.yddxst.bean.PlanQrcodeInfo;
import com.hckj.yddxst.bean.QaInfo;
import com.hckj.yddxst.bean.RedTourInfo;
import com.hckj.yddxst.bean.SearchInfo;
import com.hckj.yddxst.bean.SpeakInfo;
import com.hckj.yddxst.bean.StudyBtnInfo;
import com.hckj.yddxst.bean.TipInfo;
import com.hckj.yddxst.bean.TokenInfo;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * 描述：Retrofit接口请求集合
 * 作者：林明健
 * 日期：2019-08-23 8:46
 */
public interface BaseApi {
    // 大数据报告地址
    String STATISTICS_URL = "http://zddx.rzkeji.com/#/statistics";
    // 会议WebSocket地址
    String WEBSOCKET_URL = "wss://api.rzkeji.com";

    @Multipart
    @POST("user/searchFace")
    Observable<BaseResponse<SearchInfo>> searchFace(@Part MultipartBody.Part file);

    @GET("user/getLoginResult")
    Observable<LoginInfo> faceLogin(@Query("login_key") String key);

    @GET("video/getHotVideoList")
    Observable<BaseResponse<List<CourseInfo>>> getHotVideoList(@Query("limit") int limit, @Query("page") int page);

    @GET("video/getVideoClassifyList")
    Observable<BaseResponse<List<String>>> getCourseTypeList();

    @GET("video/getVideoInfoByClassify")
    Observable<BaseResponse<List<CourseInfo>>> getCourseListByType(@Query("classify") String classify);

    @GET("video/getTeachClassifyOrVideo")
    Observable<BaseResponse<ClassifyInfo>> getTeachClassifyOrVideo(@Query("classify_id") int id, @Query("classify_type") String type);

    @GET("video/getVideoByWord")
    Observable<BaseResponse<AnswerInfo>> getKeywordAnswer(@Query("keyword") String keyword);

    @POST("auth/deviceNumAuth")
    Observable<BaseResponse<String>> getDeviceAuth(@Query("device_num") String device_num);

    @GET("video/getNews")
    Observable<BaseResponse<List<NewsInfo>>> getNews(@Query("type") String type, @Query("page") int page);

    @GET("building/getBuildingInit")
    Observable<BaseResponse<List<BuildingInfo>>> getBuildingInit(@Query("device_num") String deviceNum);

    @GET("building/getInfo")
    Observable<BaseResponse<List<BuildingDetailInfo>>> getBuildingInfo(@Query("button_id") int id, @Query("page") int page);

    @GET("meeting/getMeetings")
    Observable<BaseResponse2<MeetingInfo2>> getMeetings(@Query("device_num") String deviceNum, @Query("classify") String classify);

    @GET("meeting/getMeetings")
    Observable<BaseResponse<List<MeetingInfo>>> getInnerMeetings(@Query("device_num") String deviceNum, @Query("classify") String classify);

    @GET("meeting/getClassify")
    Observable<BaseResponse<List<MeetingClassifyInfo>>> getMeetingClassify();

    @GET("meeting/getMeetingContent")
    Observable<BaseResponse<List<MeetingContentInfo>>> getMeetingContent(@Query("id") int id);

    @POST("meeting/startMeeting")
    Observable<BaseResponse<MeetingStartInfo>> startMeeting(@Query("id") int id, @Query("device_num") String deviceNum, @Query("client_id") String clientId);

    @GET("auth/redInfoList")
    Observable<BaseResponse<List<RedTourInfo>>> getRedTourList();

    @GET("video/getStudyButtonList")
    Observable<BaseResponse<List<StudyBtnInfo>>> getStudyBtnList();

    // 公文箱
    @GET("document/getClassify")
    Observable<BaseResponse<List<DocClassifyInfo>>> getDocumentClassify();

    @GET("document/getDocumentList")
    Observable<BaseResponse<List<DocInfo>>> getDocumentList(@Query("classify_id") String id);

    @POST("meeting/sendMeetingContentToApp")
    Observable<BaseResponse<String>> sendContentToApp(@Query("key") String key, @Query("link_id") String linkId, @Query("content_id") String contentId);

    @GET("meeting/getMeetingDocumentQrcode")
    Observable<BaseResponse<String>> getMeetingDocQrcode(@Query("key") String key);

    @POST("meeting/setMeetingFinish")
    Observable<BaseResponse<String>> setMeetingFinish(@Query("key") String key);

    @Multipart
    @POST("meeting/uploadImg")
    Observable<BaseResponse<ImgUploadInfo>> uploadMeetingImg(@Part MultipartBody.Part file, @Part("key") RequestBody key);

    @Multipart
    @POST("http://zddxapi.rzkeji.com/api/dangv2/data/uploadBirthdayImg")
    Observable<BaseResponse<ImgUploadInfo>> uploadBirthdayImg(@Part MultipartBody.Part file);

    @GET("other/getQrcode")
    Observable<BaseResponse<PlanQrcodeInfo>> getQrcode();

    @GET("https://openapi.data-baker.com/oauth/2.0/token?grant_type=client_credentials&client_secret=ZTNmMmZjZTItYzQ0Ny00MTQ3LWFlNDgtMzA0NWJjYTQ0ODY0&client_id=e710b1ce-e9c8-4860-9737-cc255a91deba")
    Call<TokenInfo> getToken();

    @FormUrlEncoded
    @POST("https://openapi.data-baker.com/tts")
    @Streaming
    Observable<ResponseBody> getVoice(@Field("access_token") String token, @Field("domain") String domain, @Field("audiotype") String audiotype,
                                      @Field("spectrum") String spectrum, @Field("volume") String volume, @Field("speed") String speed,
                                      @Field("language") String lg, @Field("voice_name") String voiceName, @Field("text") String text);


    @GET("other/getCertUrlByDeviceKey")
    Observable<BaseResponse<String>> getCertUrl(@Query("device_key") String deviceKey);

    @GET("other/getCertUrlByDeviceKeyV2")
    Observable<BaseResponse<String>> getCertUrl(@Query("device_key") String deviceKey, @Query("device_num") String deviceNum);

    @GET
    @Streaming
    Observable<ResponseBody> getCertFile(@Url String url);

    @GET("other/getInfoByKeyword")
    Observable<BaseResponse<QaInfo>> getQaByWord(@Query("keyword") String keyword);

    // 获取设备开放功能列表
    @GET("other/getDeviceFunction")
    Observable<BaseResponse<List<MenuAuthInfo>>> getMenuAuth(@Query("device_num") String deviceNum);

    @GET("meeting/getMeetingsV2")
    Observable<BaseResponse<List<MeetingClassifyInfo>>> getMeetingClassifyList(@Query("device_num") String deviceNum, @Query("classify") String classify);

    @GET("video/getNewsClassifyList")
    Observable<BaseResponse<List<NewClassifyInfo>>> getNewsClassify();

    @GET("video/getNews")
    Observable<BaseResponse<List<NewsInfo>>> getNews(@Query("type") String type, @Query("classify_id")String classifyId, @Query("page") int page);

    @GET("robot/getRobotHandleList")
    Observable<BaseResponse<List<SpeakInfo>>> getSpeakList();

    // 获取人物提示问答内容
    @GET("robot/getRobotTips")
    Observable<BaseResponse<List<TipInfo>>> getTips();

    // 获取百科分类
    @GET("baike/getClassify")
    Observable<BaseResponse<List<BaikeClassifyInfo>>> getBaikeClassify();

    @GET("baike/getBaikeList")
    Observable<BaseResponse<List<BaikeInfo>>> getBaikeInfo(@Query("classify_id") int cid, @Query("page") int page, @Query("limit") int limit);
}
