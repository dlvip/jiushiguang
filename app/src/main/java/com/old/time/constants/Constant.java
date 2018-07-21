package com.old.time.constants;

/**
 * Created by NING on 2018/3/5.
 */

public class Constant {

    //阿里云环境配置
    public static final String accessKeyId = "LTAIZI9pk9HdFbtB";
    public static final String accessKeySecret = "rGH6Ug2QEJOt3T7Ple9qTgn7XRLqP8";
    public static final String endpoint = "oss-cn-beijing.aliyuncs.com";
    public static final String bucketNameOut = "jsguang";
    public static final int ALIYPHOTO_CALLBACK_SUCCESS = 0x000009; //阿里照片上传返回码：成功
    public static final int ALIYPHOTO_CALLBACK_FILED = 0x000010; //阿里照片上传返回码:失败

    //线程名称
    public static final String IMAGEDOWNLOAD_THREAD_NAME = "ImageDownLoad";

    /**
     * 阿里云图片访问路径
     */
    public static final String OSSURL = "http://jsguang.oss-cn-beijing.aliyuncs.com/";

    /**
     * 服务器baseUrl
     */
    public static String BASE_URL = "http://120.76.72.60/webconsole/wap/";

    /**
     * 测试baseUrl
     */
    public static String BASE_TEST_URL = "http://192.168.0.4:8080/jiushiguang/";

    //用户登陆
    public static final String USER_LOGIN = BASE_TEST_URL + "loginByPhone";

    //获取验证码
    public static final String GET_PHONE_CODE = BASE_TEST_URL + "getVerification";

    //用户注册
    public static final String USER_REGISTER = BASE_TEST_URL + "regedit";

    //获取用户信息
    public static final String GET_USER_INFO = BASE_TEST_URL + "getUserInfo";

    // 发布动态
    public static final String SEND_CONTENT = BASE_TEST_URL + "saveContent";

    //获取圈子列表
    public static final String GET_LIST_CONTENT = BASE_TEST_URL + "listContent";

    //获取视频列表
    public static final String GET_VIDEO_LIST = BASE_TEST_URL + "video/getVideos";

    //获取相册列表
    public static final String GET_ALUMLIST = BASE_TEST_URL + "getAlbumList";

    //获取文章列表
    public static final String GET_ARTICLE_LIST = BASE_TEST_URL + "article/getArticleList";

    public static String PHOTO_PIC_URL = "http://up.enterdesk.com/edpic_source/8f/e5/f7/8fe5f7a16412b3d234847311bacafa7c.jpg";

    public static String MP4_PATH_URL = "http://o1.longbeidata.com//filekey/b245e7761b2e4600884b1cf828bd5a7f.mp4";

    public static String mHomeUrl = "http://yst.longbei.ren/html/yst/index.html";

    /**
     * 请求code值
     */
    public static final int STATUS_FRIEND_00 = 0;
    public static final int STATUS_FRIEND_08 = -8;//退出登录
    public static final int STATUS_FRIEND_17 = -17;//敏感词
    public static final int STATUS_FRIEND_90 = -90;
    public static final int STATUS_FRIEND_910 = -910;
    public static final int STATUS_SERVER_ERROR = 404;
    public static final int STATUS_FRIEND_10 = -10;
    public static final int STATUS_FRIEND_1003 = -1003;//需要强制更新
    public static final int STATUS_FRIEND_2000 = -2000;//没有网络
    public static final int STATUS_FRIEND_2001 = -2001;//网络超时
    public static final int STATUS_FRIEND_1403 = -1403;//本部分考试结束
    public static final int STATUS_FRIEND_1406 = -1406;//本题已经做过了

    /**
     * 音乐配置开始
     **/
    //本地歌曲listview点击
    public static final String ACTION_LIST_ITEM = "com.old.time.listitem";
    public static final int MSG_PROGRESS = 001;
    public static final int MSG_PREPARED = 002;
    public static final int MSG_PLAY_STATE = 003;
    // 取消
    public static final int MSG_CANCEL = 004;

    public static final int NOTIFICATION_CEDE = 100;

    //暂停音乐
    public static final String ACTION_PAUSE = "com.old.time.pause";
    //播放音乐
    public static final String ACTION_PLAY = "com.old.time.play";
    //下一曲
    public static final String ACTION_NEXT = "com.old.time.next";
    //上一曲
    public static final String ACTION_PRV = "com.old.time.prv";

    public static final String ACTION_CLOSE = "com.old.time.close";

    /**音乐配置结束**/
}
