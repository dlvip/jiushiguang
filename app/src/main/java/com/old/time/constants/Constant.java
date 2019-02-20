package com.old.time.constants;

import com.old.time.MyApplication;

/**
 * Created by NING on 2018/3/5.
 */

public class Constant {

    public static final String SPLASH_FILE_NAME = "splash.srr";

    //动态闪屏序列化地址
    public static final String SPLASH_PATH = MyApplication.getInstance().getFilesDir().getAbsolutePath() + "/alpha/splash";

    public static final int PageSize = 15;

    public static final int PAGE_ALL = 100;

    //热更新App ID
    public static final String TINK_APP_ID = "726aeba59a";

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
     * 聚合API手机归属地查询
     */
    public static final String PHONE_DRESS = "http://apis.juhe.cn/mobile/get";

    public static final String PHONE_KEY = "cf5d54b234390d5248a4ccd892a91a1e";

    /**
     * 融云key
     */
    public static final String RONG_YUN_KEY = "x18ywvqfxcbjc";

    /**
     * 融云
     */
    public static final String RONG_TOKEN = "sign=a0c1ebbfd3f060420c72311e709c83d14242b4b1x18ywvqfxcbjc1550657140000e0071&uid=17600075773";

    /**
     * 阿里云图片访问路径
     */
    public static final String OSSURL = "http://jsguang.oss-cn-beijing.aliyuncs.com/";

    /**
     * 服务器baseUrl
     */
//    public static String BASE_URL = "http://120.76.72.60/webconsole/wap/";

    /**
     * 测试baseUrl
     */
    public static String BASE_TEST_URL = "http://localhost:9090/jiushiguang/";

    //用户登陆
    public static final String USER_LOGIN = BASE_TEST_URL + "loginUser";

    //获取验证码
    public static final String GET_PHONE_CODE = BASE_TEST_URL + "getMobileCode";

    //用户注册
    public static final String USER_REGISTER = BASE_TEST_URL + "registerUser";

    //获取用户信息
    public static final String GET_USER_INFO = BASE_TEST_URL + "user/getUserByUserId";

    //修改用户信息
    public static final String UPDATE_USER_MSG = BASE_TEST_URL + "user/updateUserMsg";

    // 发布动态
    public static final String SEND_CONTENT = BASE_TEST_URL + "saveContent";

    //获取圈子列表
    public static final String GET_LIST_CONTENT = BASE_TEST_URL + "listContent";

    //获取相册列表
    public static final String GET_ALUMLIST = BASE_TEST_URL + "getAlbumList";

    //获取轮播图列表
    public static final String GET_HOME_BANNERS = BASE_TEST_URL + "banner/getBanners";

    //添加轮播图
    public static final String INSERT_HOME_BANNERS = BASE_TEST_URL + "banner/insertBanner";

    //获取icon列表
    public static final String GET_HOME_ICONS = BASE_TEST_URL + "icon/getIconList";

    //添加专辑
    public static final String COURSE_ADD_COURSE = BASE_TEST_URL + "course/saveCourse";

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

    //保存话题
    public static final String INSERT_TOPIC = BASE_TEST_URL + "topic/insertTopic";

    /**
     * 获取话题列表
     */
    public static final String GET_TOPIC_LIST = BASE_TEST_URL + "topic/getTopicList";

    /**
     * 获取话题详情
     */
    public static final String GET_TOPIC_DETAIL = BASE_TEST_URL + "topic/getTopicDetail";

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
     * 保存宝贝信息
     */
    public static final String INSERT_GOODS_INFO = BASE_TEST_URL + "goods/insertGoods";

    /**
     * 获取宝贝用户
     */
    public static final String INSERT_GOODS_USER = BASE_TEST_URL + "goods/getGoodsEntityUser";

    /**
     * 设置宝贝详情
     */
    public static final String INSERT_GOODS_DETAIL_ID = BASE_TEST_URL + "goods/updateGoodsEntityDetailId";

    /**
     * 获取宝贝列表
     */
    public static final String GET_GOODS_LIST = BASE_TEST_URL + "goods/getGoodsList";

    /**
     * 管理员获取宝贝
     */
    public static final String GET_ADMIN_GOODS_LIST = BASE_TEST_URL + "goods/getGoodsListByAdmin";

    /**
     * 添加活动
     */
    public static final String INSTER_ACTION_BEAN = BASE_TEST_URL + "action/insertAction";

    /**
     * 添加视频
     */
    public static final String ADD_VIDEO = BASE_TEST_URL + "video/addVideo";

    /**
     * 获取视频列表
     */
    public static final String GET_VIDEO_LIST = BASE_TEST_URL + "video/getVideos";

    public static String PHOTO_PIC_URL = "http://up.enterdesk.com/edpic_source/8f/e5/f7/8fe5f7a16412b3d234847311bacafa7c.jpg";

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

    /**音乐配置结束**/
}
