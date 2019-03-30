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
        int j;
        for (int i = 0; i < userNames.length; i++) {
            j = i;
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
        params.put("userId", "01" + userId);
        params.put("mobile", "01" + mobile);
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


        return new String[]{"嘴角的樱桃汁", "Cookie", "性子野、", "喝了酱油耍酒疯", "喝可乐的猫", "甚是乖巧", "娇软甜", "日光倾城未必温暖", "凉柚", "官方小可爱√", "你列表最软的妹.", "睡美人的小仙女", "今天小雨转甜", "章鱼小肉丸", "果味小可爱", "萌主系我", "我宣你你造吗", "习惯性、依赖", "我恋小黄人", "攒一口袋星星", "吐个泡泡", "蘸点软妹酱", "试卷是一张微凉的遗书", "鱼巷猫归", "脸红", "不解风情的老妖怪", "颈上鲜草莓", "爱咬吸管的少女", "素小花", "全球少女萌主", "雨含思念", "小可爱在此", "甜蜜的人儿@", "乖乖=猪", "我有小乖乖 ！", "卷猫", "好女人就是我@", "你若不离我便不弃", "天真点", "软萝", "偸吻月亮", "可爱害羞", "偷糖的奶妹", "小梨窝很甜", "奶音能量", "荫酱小可爱", "迴吥箌過祛", "红颜一笑丶尽是殇°", "素小言″", "琴声如瑟月微凉i", "芭比的双重性格", "总有逗比挑衅本宫", "人潮拥挤我尿急", "殺无赦", "风软一江水", "青春喂了作业", "稚屿"};
    }

    private static String[] getUserAravers() {

        return new String[]{"http://img5.duitang.com/uploads/item/201412/26/20141226121110_Y3TEe.jpeg"//
                , "http://g.hiphotos.baidu.com/zhidao/pic/item/8cb1cb1349540923d33ac7c29058d109b3de493e.jpg"//
                , "http://g.hiphotos.baidu.com/zhidao/pic/item/8cb1cb1349540923d33ac7c29058d109b3de493e.jpg"//
                , "http://img4.duitang.com/uploads/item/201412/07/20141207150727_RUAna.jpeg"//
                , "http://cdn.duitang.com/uploads/item/201406/07/20140607104949_Ra52Y.thumb.600_0.jpeg"//
                , "http://image.biaobaiju.com/uploads/20180802/03/1533150534-NhWDyQfjIb.png"//
                , "http://b-ssl.duitang.com/uploads/item/201804/05/20180405125220_YMwnN.thumb.700_0.jpeg"//
                , "http://b-ssl.duitang.com/uploads/item/201804/22/20180422110049_naixP.thumb.700_0.jpeg"//
                , "http://b-ssl.duitang.com/uploads/item/201812/31/20181231164957_xnhhr.thumb.700_0.jpg"//
                , "http://b-ssl.duitang.com/uploads/item/201812/03/20181203132619_lutlx.thumb.700_0.jpeg"//
                , "http://b-ssl.duitang.com/uploads/item/201812/06/20181206125057_xytrw.thumb.700_0.jpg"//
                , "http://b-ssl.duitang.com/uploads/item/201804/11/20180411215320_EFYMH.thumb.700_0.jpeg"//
                , "http://gss0.baidu.com/7Po3dSag_xI4khGko9WTAnF6hhy/zhidao/pic/item/77c6a7efce1b9d16ca37e3caf8deb48f8d5464ac.jpg"//
                , "http://b-ssl.duitang.com/uploads/item/201812/31/20181231204925_dxqmi.thumb.700_0.jpg"//
                , "http://b-ssl.duitang.com/uploads/item/201812/06/20181206125033_hbake.thumb.700_0.jpg"//
                , "http://b-ssl.duitang.com/uploads/item/201804/22/20180422111043_uEKVd.thumb.700_0.jpeg"//
                , "http://b-ssl.duitang.com/uploads/item/201802/11/20180211200037_Xntsk.thumb.700_0.png"//
                , "http://b-ssl.duitang.com/uploads/item/201804/11/20180411215433_iPzN8.thumb.700_0.jpeg"//
                , "http://image.biaobaiju.com/uploads/20181018/15/1539849070-IzuqivyZKO.jpg"//
                , "http://b-ssl.duitang.com/uploads/item/201808/11/20180811210204_kAvwV.jpeg"//
                , "http://b-ssl.duitang.com/uploads/item/201804/05/20180405125220_YMwnN.thumb.700_0.jpeg"//
                , "http://b-ssl.duitang.com/uploads/item/201812/31/20181231204929_hmukd.thumb.700_0.jpg"//
                , "http://b-ssl.duitang.com/uploads/item/201812/06/20181206125812_xqenj.thumb.700_0.jpg"//
                , "http://b-ssl.duitang.com/uploads/item/201812/06/20181206130500_nludt.thumb.700_0.jpg"//
                , "http://pic.qqtn.com/up/2018-6/15285121866017973.jpg"//
                , "http://upload.mnw.cn/2018/0115/1516008979127.jpg"//
                , "http://img.52z.com/upload/news/image/20180628/20180628121141_53328.jpg"//
                , "http://img.52z.com/upload/news/image/20180620/20180620113459_67561.jpg"//
                , "http://img.52z.com/upload/news/image/20180823/20180823052048_61686.jpg"//
                , "http://img.52z.com/upload/news/image/20180828/20180828030424_34000.jpg"//
                , "http://www.caisheng.net/UploadFiles/img_1_3526257594_2702226647_27.jpg"//
                , "http://img.52z.com/upload/news/image/20180823/20180823052049_34069.jpg"//
                , "http://cdn.duitang.com/uploads/item/201210/22/20121022235213_WmVvK.thumb.700_0.jpeg"//
                , "http://img4.duitang.com/uploads/blog/201406/20/20140620125558_ZYrYN.thumb.700_0.jpeg"//
                , "http://img4.duitang.com/uploads/item/201404/19/20140419213843_CYkKk.thumb.700_0.jpeg"//
                , "http://img4.duitang.com/uploads/item/201408/26/20140826012944_sGSPM.jpeg"//
                , "http://tupian.qqjay.com/tou2/2018/0426/fe7db87899f5bf3058c67911da2dccb1.jpg"//
                , "http://cdn.duitang.com/uploads/item/201411/21/20141121120839_a8NUk.thumb.700_0.jpeg"//
                , "http://img4.duitang.com/uploads/item/201512/13/20151213102616_rCiEx.thumb.700_0.jpeg"//
                , "http://img5.duitang.com/uploads/item/201412/05/20141205223608_NYckj.thumb.700_0.jpeg"//
                , "http://b-ssl.duitang.com/uploads/item/201812/07/20181207173003_zdddw.jpg"//
                , "http://b-ssl.duitang.com/uploads/item/201812/25/20181225233301_ekjwg.jpg"//
                , "http://img4.duitang.com/uploads/item/201404/19/20140419213843_CYkKk.thumb.700_0.jpeg"//
                , "http://img4.duitang.com/uploads/item/201410/16/20141016134922_ncEv4.thumb.700_0.jpeg"//
                , "http://img4.duitang.com/uploads/item/201408/26/20140826012944_sGSPM.jpeg"//
                , "http://img4.duitang.com/uploads/item/201405/25/20140525105001_8RBxj.thumb.700_0.jpeg"//
                , "http://img4.duitang.com/uploads/item/201411/10/20141110193251_mzTvi.thumb.700_0.jpeg"//
                , "http://img3.imgtn.bdimg.com/it/u=4230562066,3381494163&fm=26&gp=0.jpg"//
                , "http://img4.duitang.com/uploads/item/201406/07/20140607131350_mUGGB.thumb.600_0.jpeg"//
                , "http://cdn.duitang.com/uploads/item/201602/23/20160223124339_d2NkX.jpeg"//
                , "http://cdn.duitang.com/uploads/item/201308/13/20130813115619_EJCWm.thumb.700_0.jpeg"//
                , "http://img5.duitang.com/uploads/item/201410/22/20141022214913_sJUkx.thumb.700_0.jpeg"//
                , "http://img4.duitang.com/uploads/blog/201309/21/20130921084017_JXvSy.thumb.700_0.png"//
                , "http://img5q.duitang.com/uploads/item/201507/03/20150703224804_vHGrm.thumb.700_0.jpeg"//
                , "http://img4.duitang.com/uploads/item/201306/26/20130626090600_AE4Pn.thumb.700_0.jpeg"//
        };
    }

    public static String getSystemBookId(int indext) {


        return new String[]{"9787506365437"//活着
                , "9787506365604"//在细雨中呼喊
                , "9787208061644"//追风筝的人
                , "9787550013247"//摆渡人
                , "9787544258609"//白夜行
                , "9787544267618"//嫌疑人X的献身
                , "9787506346207"//海子诗全集
                , "9787544270878"//解忧杂货店
                , "9787530218198"//悲剧人偶
                , "9787544285148"//恶意
                , "9787544277617"//霍乱时期的爱情
                , "9787506394314"//白鹿原
                , "9787533665203"//一个陌生女人的来信
                , "9787544242042"//情书
                , "9787548425625"//醒来觉得甚是爱你
                , "9787515909950"//人的失格
                , "9787533936020"//月亮与六便士
                , "9787201077642"//小王子
                , "9787201142821"//时间的礼物
                , "9787540488475"//显微镜下的大明
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
                , "9787508680798"//肉食者不鄙
                , "9787807133681"//人间草木
                , "9787537840330"//丘陵之雕
                , "9787501453580"//古拉格群岛
                , "9787537840330"//丘陵之雕：野夫诗集
                , "9787530212004"//平凡的世界
                , "9787540488789"//一个人生活
                , "9787542664051"//冬泳
                , "9787544294881"//四个春天
                , "9787542663559"//无中生有
                , "9787307204539"//漫画之王
                , "9787567586673"//人行道王国
                , "9787213090769"//我的帝王生涯
                , "9787533954901"//剧演的终章
                , "9787208155398"//守夜
                , "9787544291170"//百年孤独
                , "9787544292764"//海边理发店
                , "9787520140607"//午夜北平
                , "9787521700077"//债务危机
                , "9787541153259"//怪物少女妮莫娜
                ,}[indext];
    }
}
