package com.old.time.utils;

import android.content.Context;

import com.lzy.okgo.model.HttpParams;
import com.old.time.aidl.ChapterBean;
import com.old.time.beans.FastMailBean;
import com.old.time.beans.JHBaseBean;
import com.old.time.beans.PhoneInfo;
import com.old.time.beans.ResultBean;
import com.old.time.beans.UserInfoBean;
import com.old.time.beans.VideosBean;
import com.old.time.constants.Constant;
import com.old.time.okhttps.JsonCallBack;
import com.old.time.okhttps.OkGoUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NING on 2018/9/20.
 */

public class DataUtils {

    /**
     * 获取数据
     *
     * @param fileName
     * @param mContext
     * @return
     */
    public static List<ChapterBean> getModelBeans(String fileName, Context mContext) {
        String string = StringUtils.getJson(fileName + ".json", mContext);
        List<ChapterBean> chapterBeans = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(string);
            JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("list");
            chapterBeans.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject musicObj = jsonArray.getJSONObject(i);
                ChapterBean chapterBean = new ChapterBean();
                chapterBean.setAlbum(musicObj.getString("coverLarge"));
                chapterBean.setAlbumId(Long.parseLong(musicObj.getString("albumId")));
                chapterBean.setAudio(musicObj.getString("playUrl64"));
                chapterBean.setDuration(Long.parseLong(musicObj.getString("duration")));
                chapterBean.setPicUrl(musicObj.getString("coverLarge"));
                chapterBean.setTitle(musicObj.getString("title"));
                chapterBean.setUrl(musicObj.getString("playUrl64"));
                chapterBeans.add(chapterBean);

            }
        } catch (JSONException e) {
            e.printStackTrace();

        }
        return chapterBeans;
    }

    /**
     * 获取数据
     *
     * @param fileName
     * @param mContext
     * @return
     */
    public static List<VideosBean> getVideosBeans(String fileName, Context mContext) {
        String string = StringUtils.getJson(fileName + ".json", mContext);
        List<VideosBean> chapterBeans = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(string);
            JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("list");
            chapterBeans.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject musicObj = jsonArray.getJSONObject(i);
                VideosBean chapterBean = new VideosBean();
                chapterBean.setD_pic(musicObj.getString("d_pic"));
                chapterBean.setD_id(musicObj.getString("d_id"));
                chapterBean.setD_name(musicObj.getString("d_name"));
                chapterBeans.add(chapterBean);

            }
        } catch (JSONException e) {
            e.printStackTrace();

        }
        return chapterBeans;
    }

    public static void getPhoneMsg(final String numStr) {
        HttpParams params = new HttpParams();
        params.put("phone", numStr);
        params.put("key", Constant.PHONE_KEY);
        params.put("dtype", "json");
        OkGoUtils.getInstance().getNetForData(params, Constant.PHONE_DRESS, new JsonCallBack<JHBaseBean<PhoneInfo>>() {

            @Override
            public void onSuccess(JHBaseBean<PhoneInfo> mResultBean) {
                if (mResultBean == null || mResultBean.result == null) {

                    return;
                }
                PhoneInfo phoneInfo = mResultBean.result;
                phoneInfo.setPhone(numStr);
                savePhoneInfo(phoneInfo);
            }

            @Override
            public void onError(JHBaseBean<PhoneInfo> mResultBean) {

            }
        });
    }

    /**
     * 保存手机归属地
     *
     * @param phoneInfo
     */
    private static void savePhoneInfo(PhoneInfo phoneInfo) {
        HttpParams params = new HttpParams();
        params.put("phone", phoneInfo.getPhone());
        params.put("province", phoneInfo.getProvince());
        params.put("city", phoneInfo.getCity());
        params.put("areacode", phoneInfo.getAreacode());
        params.put("zip", phoneInfo.getZip());
        params.put("company", phoneInfo.getCompany());
        params.put("card", phoneInfo.getCard());
        OkGoUtils.getInstance().postNetForData(params, Constant.SAVE_PHONE_INFO, new JsonCallBack<ResultBean>() {
            @Override
            public void onSuccess(ResultBean mResultBean) {

            }

            @Override
            public void onError(ResultBean mResultBean) {

            }
        });
    }

    /**
     * 获取快递信息
     *
     * @param mContext
     * @return
     */
    public static List<FastMailBean> getFastMailBeans(Context mContext) {
        String string = StringUtils.getJson("fast_mail.json", mContext);
        List<FastMailBean> mFastMailBeans = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(string);
            mFastMailBeans.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject fastMailObj = jsonArray.getJSONObject(i);
                FastMailBean mFastMailBean = new FastMailBean();
                mFastMailBean.setName(fastMailObj.getString("name"));
                mFastMailBean.setIcon(fastMailObj.getString("icon"));
                mFastMailBean.setUrl(fastMailObj.getString("url"));
                mFastMailBean.setId(fastMailObj.getString("id"));
                mFastMailBeans.add(mFastMailBean);

            }
        } catch (JSONException e) {
            e.printStackTrace();

        }
        return mFastMailBeans;
    }

    public static List<String> getDateStrings(int size) {
        List<String> strings = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            strings.add("");

        }
        return strings;
    }

    public static void registerUserInfo() {
        String[] userNames = getUserNameS();
        String[] userAvatars = getUserAravers();
        int j = 0;
        for (int i = 0; i < userNames.length; i++) {
            if (i >= userAvatars.length) {
                j = userAvatars.length - 1;

            }
            registerUser(userAvatars[j], userNames[i], String.valueOf(i), String.valueOf(i));

        }
    }

    private static void registerUser(String avatar, String userName, String userId, String mobile) {
        HttpParams params = new HttpParams();
        params.put("avatar", avatar);
        params.put("userName", userName);
        params.put("userId", userId);
        params.put("mobile", mobile);
        OkGoUtils.getInstance().postNetForData(params, Constant.GET_SYSTEM_REGISTER, new JsonCallBack<ResultBean<UserInfoBean>>() {
            @Override
            public void onSuccess(ResultBean<UserInfoBean> mResultBean) {
                DebugLog.d("registerUser==>", mResultBean.data.toString());

            }

            @Override
            public void onError(ResultBean<UserInfoBean> mResultBean) {

            }
        });
    }

    private static String[] getUserNameS() {


        return new String[]{"黄昏半晓、拾忆", "梦落轻寻", "清茶也醉人", "阳光①夏", "喝醉了梦", "铁树开花@", "风居住的味道", //
                "夏花冬雪", "簡單ǒ愛√", "ミ灬纯真小女孩", "云之铃。", "那场櫻花雨", "掌中花", "聆听、这幸福", "等一朵彼岸花开",//
                "情花。，初放", "花雨淋湿了梦境", "羙仯丶亾泩", "青瓷花羽", "暮浴ゞ晨曦", "許願樹丅啲祈禱", "玻璃里的彩虹", //
                "满眼、的温柔", "深海里的星星", "爱是蓝色", "清水漪澜", "夏天的巴比伦狠美", "安。忆雅", "风の痕迹", "孤海未蓝",//
                "玫瑰花的代替", "时光凉透初时模样", "灰色世界里的红玫瑰", "夏日落◇◆", "流星、划过sky", "画朵纸鸢花", "春风十里·",//
                "细致如美瓷╯", "红莺绿柳", "雨嫣", "若雪樱花草", "就像蓝鲸忘了海", "Rain Sounds", "惆怅暮烟垂", "淡藍色瞳孔。",//
                "︶彩虹餹dē夢", "〤twinkle繁夏", "夏初的伤", "秋末的美。", "像儚辷様媄", "薰衣草的夏天", "四号花店", "枕花眠", //
                "捕星光", "舀一瓢月色", "邀我花前醉", "摇铃唤白鹿", "偷捧时间煮酒喝", "扑流萤", "萤火眠眠", "月下无痕", "风居住的街道",//
                "睡于麋鹿林"};
    }

    private static String[] getUserAravers() {

        return new String[]{"http://www.tbw-xie.com/tuxieJDEwLmFsaWNkbi5jb20vYmFvL3VwbG9hZGVkL2kxL1RCMUlPYWlRViQ2WHFhcCQ1JDM.jpg"//
                , "http://img10.360buyimg.com/n0/jfs/t2587/344/774058668/175754/c0b47695/5725aab4Ne8e023fd.jpg"//
                , "http://img1.imgtn.bdimg.com/it/u=3797202546,2028562705&fm=214&gp=0.jpg"//
                , "http://img.mp.itc.cn/upload/20170323/bd834e03006a4ceb9c3c3ff995f772bd_th.jpeg"//
                , "http://img5.imgtn.bdimg.com/it/u=3769986249,4040529360&fm=15&gp=0.jpg"//
                , "http://img5.imgtn.bdimg.com/it/u=4278407699,649956952&fm=15&gp=0.jpg"//
                , "http://img2.imgtn.bdimg.com/it/u=529706351,2873558001&fm=15&gp=0.jpg"//
                , "http://img5.imgtn.bdimg.com/it/u=1185058603,1112505556&fm=26&gp=0.jpg"//
                , "http://img5.imgtn.bdimg.com/it/u=3425809397,1315010404&fm=15&gp=0.jpg"//
                , "http://image4.suning.cn/uimg/b2c/newcatentries/0070181417-000000010115726974_1_800x800.jpg"//
                , "http://img2.imgtn.bdimg.com/it/u=79255844,2460060828&fm=15&gp=0.jpg"//
                , "http://img4.imgtn.bdimg.com/it/u=4291212875,3026070603&fm=15&gp=0.jpg"//
                , "http://img3.imgtn.bdimg.com/it/u=1404253326,3567317521&fm=15&gp=0.jpg"//
                , "http://img1.imgtn.bdimg.com/it/u=1457574772,4208571193&fm=26&gp=0.jpg"//
                , "http://img3.imgtn.bdimg.com/it/u=2036960203,446986364&fm=26&gp=0.jpg"//
                , "http://img4.imgtn.bdimg.com/it/u=977089818,2972028142&fm=26&gp=0.jpg"//
                , "http://img5.imgtn.bdimg.com/it/u=1974953716,3054353363&fm=26&gp=0.jpg"//
                , "http://img1.imgtn.bdimg.com/it/u=2935349475,2125694918&fm=26&gp=0.jpg"//
                , "http://img3.imgtn.bdimg.com/it/u=1709642607,2725224868&fm=26&gp=0.jpg"//
                , "http://img5.imgtn.bdimg.com/it/u=884440541,2903227536&fm=26&gp=0.jpg"//
                , "http://img2.imgtn.bdimg.com/it/u=2471126106,2382116736&fm=26&gp=0.jpg"//
                , "http://img1.imgtn.bdimg.com/it/u=2053400127,1461463396&fm=26&gp=0.jpg"//
                , "http://img5.imgtn.bdimg.com/it/u=1002266063,3139943970&fm=15&gp=0.jpg"//
                , "http://img0.imgtn.bdimg.com/it/u=2385213701,2719092767&fm=15&gp=0.jpg"//
                , "http://img4.imgtn.bdimg.com/it/u=4042731134,1652766081&fm=214&gp=0.jpg"//
                , "http://img0.imgtn.bdimg.com/it/u=325099414,703644908&fm=26&gp=0.jpg"//
                , "http://img0.imgtn.bdimg.com/it/u=3369162403,1185901905&fm=26&gp=0.jpg"//
                , "http://img3.imgtn.bdimg.com/it/u=1444029598,1453968551&fm=26&gp=0.jpg"//
                , "http://img4.imgtn.bdimg.com/it/u=505544546,292271648&fm=26&gp=0.jpg"//
                , "http://5b0988e595225.cdn.sohucs.com/images/20171201/1ab51a8403c04683b405f6d5a1182d18.jpeg"//
                , "http://img1.imgtn.bdimg.com/it/u=1764954471,971112304&fm=26&gp=0.jpg"//
                , "http://img4.imgtn.bdimg.com/it/u=3804696990,2201605820&fm=26&gp=0.jpg"//
                , "http://img2.imgtn.bdimg.com/it/u=790647051,1365287513&fm=26&gp=0.jpg"//
                , "http://inews.gtimg.com/newsapp_bt/0/7894738697/1000"//
                , "http://img0.imgtn.bdimg.com/it/u=3319122996,4241936236&fm=15&gp=0.jpg"//
                , "http://img0.imgtn.bdimg.com/it/u=221098100,4228486634&fm=15&gp=0.jpg"//
                , "http://res.cngoldres.com/upload/2014/1125/5761e583fc17dca0d90704db5cb84383.jpg"//
                , "http://inews.gtimg.com/newsapp_bt/0/8037040335/1000"//
                , "http://image4.suning.cn/uimg/b2c/newcatentries/0070175918-000000000818291409_3_800x800.jpg"//
                , "http://img5.imgtn.bdimg.com/it/u=384565084,3624724591&fm=15&gp=0.jpg"//
                , "http://inews.gtimg.com/newsapp_bt/0/7776100820/1000"//
                , "http://image5.suning.cn/uimg/b2c/newcatentries/0070174141-000000010099611492_2_800x800.jpg"//
                , "http://img0.imgtn.bdimg.com/it/u=1706673149,3281931845&fm=15&gp=0.jpg"//
                , "http://image2.suning.cn/uimg/b2c/newcatentries/0070187915-000000010474108823_1_800x800.jpg"//
                , "http://image3.suning.cn/uimg/b2c/newcatentries/0070181476-000000010070445834_2_800x800.jpg"//
                , "http://image2.suning.cn/uimg/b2c/newcatentries/0070139594-000000010491224730_1_200x200.jpg"//
                , "http://img0.imgtn.bdimg.com/it/u=2597319635,3568582421&fm=15&gp=0.jpg"//
                , "http://img5.imgtn.bdimg.com/it/u=1931723891,3490150378&fm=15&gp=0.jpg"//
                , "http://img1.imgtn.bdimg.com/it/u=3138066373,1321306114&fm=15&gp=0.jpg"//
                , "http://img4.imgtn.bdimg.com/it/u=3113074184,3942921252&fm=15&gp=0.jpg"//
                , "http://img1.imgtn.bdimg.com/it/u=2180904612,2091192637&fm=15&gp=0.jpg"//
                , "http://img5.imgtn.bdimg.com/it/u=3245806547,1873515280&fm=15&gp=0.jpg"//
                , "http://img1.imgtn.bdimg.com/it/u=2173969411,4239827968&fm=15&gp=0.jpg"//
                , "http://img5.imgtn.bdimg.com/it/u=3934308742,3201105497&fm=15&gp=0.jpg"//
                , "http://img5.imgtn.bdimg.com/it/u=3555678337,2289521240&fm=15&gp=0.jpg"//
                , "http://image2.suning.cn/uimg/b2c/newcatentries/0070168967-000000000651473205_4_800x800.jpg"//
                , "http://img5.imgtn.bdimg.com/it/u=351775468,3824694901&fm=15&gp=0.jpg"//
                , "http://img1.imgtn.bdimg.com/it/u=1390895217,923441152&fm=15&gp=0.jpg"//
                , "http://img2.imgtn.bdimg.com/it/u=3678997334,4235644582&fm=15&gp=0.jpg"//
                , "http://img5.imgtn.bdimg.com/it/u=1589904128,2751643939&fm=15&gp=0.jpg"//
                , "http://img2.imgtn.bdimg.com/it/u=2448847534,2087942582&fm=15&gp=0.jpg"//
                , "http://img1.imgtn.bdimg.com/it/u=407390361,1468817992&fm=15&gp=0.jpg"//
                , "http://img2.imgtn.bdimg.com/it/u=4101042559,4201958447&fm=26&gp=0.jpg"//
                , "http://img2.imgtn.bdimg.com/it/u=35546168,3156494880&fm=15&gp=0.jpg"//
                , "http://img3.imgtn.bdimg.com/it/u=404627947,3635187596&fm=15&gp=0.jpg"//
                , "http://img0.imgtn.bdimg.com/it/u=1591911801,2515622973&fm=15&gp=0.jpg"//
                , "http://img5.imgtn.bdimg.com/it/u=1176017423,3925985284&fm=15&gp=0.jpg"//
                , "http://img1.imgtn.bdimg.com/it/u=1374398101,3801518356&fm=15&gp=0.jpg"//
                , "http://m.360buyimg.com/n12/jfs/t2221/243/764855307/468465/9f9334f0/5628aec4Ne7bba205.jpg%21q70.jpg"//
                , "http://img5.imgtn.bdimg.com/it/u=3418510389,719081792&fm=15&gp=0.jpg",};
    }

    public static String getSystemBookId(int indext) {


        return new String[]{"9787515909950"//人的失格
                , "9787533936020"//月亮与六便士
                , "9787201077642"//小王子
                , "9787201142821"//时间的礼物
                , "9787540488475"//显微镜下的大明
                , "9787544294881"//四个春天
                , "9787541135729"//都挺好
                , "9787020139927"//失踪的孩子
                , "9787559614636"//房思琪的初恋乐园
                , "9787559413727"//我们一无所有
                , "9787540485528"//莫斯科绅士
                , "9787540484651"//如父如子
                , "9787544384636"//漫长的告别
                , "9787540485696"//观山海
                , "9787532777914"//鱼翅与花椒
                , "9787533954116"//回答不了
                , "9787559418371"//遮蔽的天空
                , "9787532777532"//长日将尽
                , "9787220105135"//雨
                , "9787532776337"//使女的故事
                , "9787541151200"//马尔多罗之歌
                , "9787220105142"//往事与随想
                , "9787542664051"//冬泳
                , "9787542663559"//无中生有
                , "9787508680798"//肉食者不鄙
                , "9787807133681"//人间草木
                ,}[indext];
    }
}
