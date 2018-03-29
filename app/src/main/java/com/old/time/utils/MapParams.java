package com.old.time.utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by NING on 2018/3/29.
 */

public class MapParams {

    private JSONObject jsonObject;

    public MapParams() {
        jsonObject = new JSONObject();


    }
    public void putParams(String string, Object object) {
        try {
            jsonObject.put(string, object);

        } catch (JSONException e) {
            e.printStackTrace();

        }
    }

    public String getParamString() {

        return jsonObject.toString();
    }
}
