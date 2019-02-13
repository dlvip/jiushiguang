package com.old.time.beans;

import java.util.ArrayList;
import java.util.List;

public class FastMailBean {

    public static FastMailBean instance(String id, String url, String icon, String name) {
        FastMailBean fastMailBean = new FastMailBean();
        fastMailBean.setId(id);
        fastMailBean.setUrl(url);
        fastMailBean.setIcon(icon);
        fastMailBean.setName(name);

        return fastMailBean;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String id;

    /**
     * 官网
     */
    private String url;

    /**
     * 图标
     */
    private String icon;

    /**
     * 名称
     */
    private String name;


    public static List<FastMailBean> getFastMailBeans() {
        List<FastMailBean> fastMailBeans = new ArrayList<>();
        fastMailBeans.add(FastMailBean.instance("0", "http://www.sf-express.com/cn/sc/index.html"//
                , "https://cdn.kuaidi100.com/images/frame/baidu/all/sf.gif?version=201707191039", "顺丰速运"));
        fastMailBeans.add(FastMailBean.instance("1", "http://www.sto.cn"//
                , "https://cdn.kuaidi100.com/images/frame/baidu/all/st.gif?version=201707191039", "申通快递"));
        fastMailBeans.add(FastMailBean.instance("2", "http://www.yundaex.com"//
                , "https://cdn.kuaidi100.com/images/frame/baidu/all/yd.gif?version=201707191039", "韵达快递"));
        fastMailBeans.add(FastMailBean.instance("3", "http://www.yto.net.cn/"//
                , "https://cdn.kuaidi100.com/images/frame/baidu/all/yt.gif?version=201707191039", "圆通速递"));
        fastMailBeans.add(FastMailBean.instance("4", "http://www.zto.cn"//
                , "https://cdn.kuaidi100.com/images/frame/baidu/all/zt.gif?version=201707191039", "中通快递"));
        fastMailBeans.add(FastMailBean.instance("5", "http://www.ems.com.cn/"//
                , "https://cdn.kuaidi100.com/images/frame/baidu/all/ems.gif?version=201707191039", "EMS快递"));
        fastMailBeans.add(FastMailBean.instance("6", "http://www.ttkdex.com"//
                , "https://cdn.kuaidi100.com/images/frame/baidu/all/tt.gif?version=201707191039", "天天快递"));
        fastMailBeans.add(FastMailBean.instance("7", "http://www.zjs.com.cn"//
                , "https://cdn.kuaidi100.com/images/frame/baidu/all/zjs.gif?version=201707191039", "宅急送"));
        fastMailBeans.add(FastMailBean.instance("8", "http://www.deppon.com"//
                , "https://cdn.kuaidi100.com/images/frame/baidu/all/dbwl.gif?version=201707191039", "德邦快递"));
        fastMailBeans.add(FastMailBean.instance("9", "http://www.800bestex.com/"//
                , "https://cdn.kuaidi100.com/images/frame/baidu/all/baishikuaidi.jpg?version=201707191039", "百世快递"));
        fastMailBeans.add(FastMailBean.instance("10", "http://yjcx.chinapost.com.cn"//
                , "https://cdn.kuaidi100.com/images/frame/baidu/all/yzgn.gif?version=201707191039", "邮政快递"));
        fastMailBeans.add(FastMailBean.instance("10", "http://www.qfkd.com.cn"//
                , "https://cdn.kuaidi100.com/images/frame/baidu/all/qfkd.gif?version=201707191039", "全峰全球专递"));
        return fastMailBeans;
    }

}
