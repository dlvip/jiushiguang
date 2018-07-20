package com.old.time.utils;

import com.google.gson.Gson;
import com.old.time.beans.ResultBean;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by NING on 2018/7/20.
 */

public class GsonUtils {

    public static <T> T jsonToBean(String json, Class<T> clazz) {
        Gson gson = new Gson();
        return gson.fromJson(json, clazz);
    }

    public static <T> T jsonToBean(String json, Object obj) {
        Gson gson = new Gson();
        return (T) gson.fromJson(json, obj.getClass());
    }

    /**
     * 把json转为ResultBean，
     *
     * @param json
     * @param clazz 指名ResultBean中的范型
     * @return
     */
    public static ResultBean toResultBean(String json, Class clazz) {
        Gson gson = new Gson();
        Type objectType = type(ResultBean.class, clazz);
        ResultBean bean = gson.fromJson(json, objectType);

        return bean;
    }

    static ParameterizedType type(final Class raw, final Type... args) {
        return new ParameterizedType() {
            public Type getRawType() {

                return raw;
            }

            public Type[] getActualTypeArguments() {

                return args;
            }


            public Type getOwnerType() {

                return null;
            }
        };
    }

}
