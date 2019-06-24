package com.old.time.constants;

import com.old.time.BuildConfig;
import com.old.time.MyApplication;

/**
 * Created by NING on 2018/3/5.
 */

public class Constant {

    //蒲公英下载地址
    public static final String PU_GONG_YING_URL = "https://www.pgyer.com/UWDC";

    /*****友盟分享*****/
    public static final String WX_APP_ID = "wxf4a007831b155c2e";
    public static final String WX_APP_SECRET = "2109bae597bf752e77af3191b3fe42ed";

    public static final String WB_APP_KEY = "136328941";
    public static final String WB_APP_SECRET = "248f2423c5d091b9d4b755595e25e4ce";

    public static final String QQ_APP_ID = "1109165436";
    public static final String QQ_APP_KEY = "NkzRMjyOPEfRejyO";

    public static final String SPLASH_FILE_NAME = "splash.srr";

    //动态闪屏序列化地址
    public static final String SPLASH_PATH = MyApplication.getInstance().getFilesDir().getAbsolutePath() + "/alpha/splash";

    public static final int PageSize = 15;

    public static final int PAGE_ALL = 100;

    //热更新App ID
    public static final String TINK_APP_ID = "726aeba59a";

    //友盟分享appKey
    public static final String YOU_MENG_APP_KEY = "5cdd0dbb3fc195fdaf000a39";

    //阿里云环境配置
    public static final String accessKeyId = "LTAIZI9pk9HdFbtB";
    public static final String accessKeySecret = "rGH6Ug2QEJOt3T7Ple9qTgn7XRLqP8";
    public static final String endpoint = "oss-cn-beijing.aliyuncs.com";
    public static final String bucketNameOut = "jsguang";

    public static final int ALIYPHOTO_CALLBACK_SUCCESS = 0x000009; //阿里照片上传返回码：成功
    public static final int ALIYPHOTO_CALLBACK_FILED = 0x000010; //阿里照片上传返回码:失败

    public static final String USER_INFO_KEY = "UserInfoKey";

    //线程名称
    public static final String IMAGEDOWNLOAD_THREAD_NAME = "ImageDownLoad";

    /**
     * 聚合手机归属地key
     */
    public static final String PHONE_KEY = "cf5d54b234390d5248a4ccd892a91a1e";

    /**
     * 聚合图书信息key
     */
    public static final String BOOK_INFO_KEY = "2442dfe07567002631b03cb8d89b633f";

    /**
     * 聚合API手机归属地查询
     */
    public static final String PHONE_DRESS = "http://apis.juhe.cn/mobile/get";

    /**
     * 通过查询ISBN获取相关信息和推荐指数
     */
    public static final String GET_JIHE_BOOK_INFO = "http://feedback.api.juhe.cn/ISBN";

    /**
     * 阿里云图片访问路径
     */
    public static final String OSSURL = "http://jsguang.oss-cn-beijing.aliyuncs.com/";

    /**
     * 服务器baseUrl
     */
    public static String BASE_TEST_URL = BuildConfig.BASE_TEST_URL;

    //检查更新
    public static final String CHECK_UPDATE = BASE_TEST_URL + "checkUpdate";

    //保存快递信息
    public static final String GET_FAST_MAIL_LIST = BASE_TEST_URL + "fastMail/getFastMailList";

    //保存通讯录
    public static final String SAVE_PHONE_BEAN_LIST = BASE_TEST_URL + "phone/savePhoneBeanList";

    //单个用户下的联系人信息
    public static final String GET_USER_SINGLE_PHONE_BEAN = BASE_TEST_URL + "phone/getUserSinglePhoneBean";

    //保存手机号归属地
    public static final String SAVE_PHONE_INFO = BASE_TEST_URL + "phone/savePhoneInfo";

    //获取手机号归属地
    public static final String GET_PHONE_DRESS = BASE_TEST_URL + "phone/getPhoneInfo";

    //用户登陆
    public static final String USER_LOGIN = BASE_TEST_URL + "loginUser";

    //获取验证码
    public static final String GET_PHONE_CODE = BASE_TEST_URL + "getMobileCode";

    //系统用户注册
    public static final String GET_SYSTEM_REGISTER = BASE_TEST_URL + "registerSystemUser";

    //用户注册
    public static final String USER_REGISTER = BASE_TEST_URL + "registerUser";

    //获取轮播图列表
    public static final String GET_HOME_BANNERS = BASE_TEST_URL + "banner/getBanners";

    //添加轮播图
    public static final String INSERT_HOME_BANNERS = BASE_TEST_URL + "banner/insertBanner";

    //获取icon列表
    public static final String GET_HOME_ICONS = BASE_TEST_URL + "icon/getIconList";

    //获取精品课堂列表
    public static final String GET_HOME_COURSES = BASE_TEST_URL + "course/getCourseList";

    //添加章节
    public static final String MUSIC_ADD_MUSIC = BASE_TEST_URL + "music/saveChapter";

    //获取章节列表
    public static final String GET_MUSIC_LIST = BASE_TEST_URL + "music/getChapters";

    //获取名师优讲
    public static final String GET_HONE_TEACHERS = BASE_TEST_URL + "user/getUserList";

    //获取文章列表
    public static final String GET_ARTICLE_LIST = BASE_TEST_URL + "article/getArticleList";

    //修改文章阅读量
    public static final String UPDATE_ARTICLE_READ_COUNT = "article/updateArticleReadCount";

    /**
     * 用户
     */
    private static final String BASE_USER = BASE_TEST_URL + "user/";

    //获取用户信息
    public static final String GET_USER_INFO = BASE_USER + "getUserByUserId";

    //获取用户融云token
    public static final String GET_USER_RONG_TOKEN = BASE_USER + "getUserRongToken";

    //修改用户信息
    public static final String UPDATE_USER_MSG = BASE_USER + "updateUserMsg";

    /**
     * 图书
     */
    private static final String BASE_BOOK = BASE_TEST_URL + "book/";

    //精选图书
    public static final String GET_RECOMMENT_R_BOOK = BASE_BOOK + "getRecommendRBook";

    //获取图书分类
    public static final String GET_BOOK_MALL_LIST = BASE_BOOK + "getPeopleTypeList";

    //保存图书信息
    public static final String CREATE_BOOK_INFO = BASE_BOOK + "createBookInfo";

    //获取图书信息
    public static final String GET_BOOK_INFO = BASE_BOOK + "getBookInfo";

    //图书列表
    public static final String GET_BOOK_LIST = BASE_BOOK + "getBookEntities";

    //用户图书列表
    public static final String GET_USER_BOOK_LIST = BASE_BOOK + "getUserBookList";

    /**
     * 书评
     */
    public static final String BASE_BOOK_COMMENT = BASE_TEST_URL + "bookComment/";

    //创建书评
    public static final String CREATE_BOOK_COMMENT = BASE_BOOK_COMMENT + "createBookComment";

    //获取书评列表
    public static final String GET_BOOK_COMMENTS = BASE_BOOK_COMMENT + "getBookComments";

    /**
     * 书签
     */
    private static final String BASE_SIGN = BASE_TEST_URL + "signname/";

    //创建书签
    public static final String CREATE_SIGN_NAME = BASE_SIGN + "createSignName";

    //书签列表
    public static final String GET_SIGN_NAME_LIST = BASE_SIGN + "getSignNameList";

    //图书书签
    public static final String GET_BOOK_SIGN_NAME_LIST = BASE_SIGN + "getBookSignNameList";


    /**
     * 话题
     */
    private static final String BASE_TOPIC = BASE_TEST_URL + "topic/";

    //创建话题
    public static final String INSERT_TOPIC = BASE_TOPIC + "createTopicEntity";

    //话题详情
    public static final String GET_TOPIC_DETAIL = BASE_TOPIC + "getTopicDetail";

    //话题列表
    public static final String GET_TOPIC_LIST = BASE_TOPIC + "getTopicList";

    /**
     * 动态
     */
    private static final String BASE_DYNAMIC = BASE_TEST_URL + "dynamic/";

    //创建动态
    public static final String CREATE_DYNAMIC = BASE_DYNAMIC + "createDynamic";

    //动态详情
    public static final String GET_DYNAMIC_DETAIL = BASE_DYNAMIC + "getDynamicDetail";

    //用户动态列表
    public static final String GET_USER_DYNAMIC_LIST = BASE_DYNAMIC + "getUserDynamicEntities";

    //话题动态列表
    public static final String GET_TOPIC_DYNAMIC_LIST = BASE_DYNAMIC + "getTopicDynamicEntities";

    //所有动态列表
    public static final String GET_DYNAMIC_LIST = BASE_DYNAMIC + "getDynamicEntities";

    //删除动态
    public static final String DELETE_DYNAMIC = BASE_DYNAMIC + "deleteDynamicEntity";

    /**
     * 添加评论
     */
    public static final String GET_INSERT_COMMENT = BASE_TEST_URL + "comment/insertComment";

    /**
     * 获取评论列表
     */
    public static final String GET_COMMENT_LIST = BASE_TEST_URL + "comment/getCommentList";

    /**
     * 获取活动列表
     */
    public static final String GET_ACTION_LIST = BASE_TEST_URL + "action/getActionList";

    /**
     * 添加活动
     */
    public static final String INSTER_ACTION_BEAN = BASE_TEST_URL + "action/insertAction";

    /**
     * 获取视频信息
     */
    private static final String BASE_VIDEO = BASE_TEST_URL + "video/";

    //上传视频
    public static final String CREATE_VIDEO_INFO = BASE_VIDEO + "createVideoEntity";

    /**
     * 关联id
     */
    private static final String BASE_TVB = BASE_TEST_URL + "topicVideoBook/";

    //获取视频信息
    public static final String GET_VIDEO_DETAIL = BASE_TVB + "getVideoDetail";

    public static String MP4_PATH_URL = "http://iqiyi.qq-zuidazy.com//20181115//768_ea6fbf2b//index.m3u8";

    public static String mHomeUrl = "https://www.baidu.com";

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
    public static final int MSG_PROGRESS = 001;
    public static final int MSG_PREPARED = 002;
    public static final int MSG_PLAY_STATE = 003;
    // 取消
    public static final int MSG_CANCEL = 004;

    public static final int NOTIFICATION_CEDE = 100;

    //本地歌曲listview点击
    public static final String ACTION_LIST_ITEM = "com.old.time.listitem";
    //开始
    public static final String ACTION_START = "com.old.time.start";
    //暂停
    public static final String ACTION_PAUSE = "com.old.time.pause";
    //播放
    public static final String ACTION_PLAY = "com.old.time.play";
    //下一曲
    public static final String ACTION_NEXT = "com.old.time.next";
    //上一曲
    public static final String ACTION_PRV = "com.old.time.prv";
    //播放进度
    public static final String ACTION_PROGRESS = "com.old.time.progress";
    //播放速率
    public static final String ACTION_SPEED = "com.old.time.speed";
    //关闭
    public static final String ACTION_CLOSE = "com.old.time.close";

    /**
     * 音乐配置结束
     **/
}
