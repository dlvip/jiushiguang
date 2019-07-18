package com.old.time.beans;

import java.io.Serializable;

/**
 * Created by diliang on 2017/2/7.
 */
public class PerfecTypeBean implements Serializable {

    public String id;
    public String distitle;
    public String title;

    public static PerfecTypeBean getInstance(String id, String distitle) {

        return new PerfecTypeBean(id, distitle);
    }

    public PerfecTypeBean(String id, String distitle) {
        this.id = id;
        this.distitle = distitle;
    }
}
