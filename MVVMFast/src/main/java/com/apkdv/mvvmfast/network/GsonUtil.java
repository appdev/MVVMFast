package com.apkdv.mvvmfast.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.pandaq.rxpanda.gsonadapter.DefaultTypeAdapters;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;


public class GsonUtil {

    private static Gson gson;

    public static synchronized Gson gson() {
        if (gson == null) {
            gson = create();
        }
        return gson;
    }


    private static Gson create() {
        return new GsonBuilder()
                .registerTypeAdapter(Boolean.class, DefaultTypeAdapters.BOOLEAN)
                .registerTypeAdapter(Boolean.class, DefaultTypeAdapters.BOOLEAN_AS_STRING)
                .registerTypeAdapter(boolean.class, DefaultTypeAdapters.BOOLEAN)
                .registerTypeAdapter(Integer.class, DefaultTypeAdapters.INTEGER)
                .registerTypeAdapter(int.class, DefaultTypeAdapters.INTEGER)
                .registerTypeAdapter(Long.class, DefaultTypeAdapters.LONG)
                .registerTypeAdapter(long.class, DefaultTypeAdapters.LONG)
                .registerTypeAdapter(Float.class, DefaultTypeAdapters.FLOAT)
                .registerTypeAdapter(float.class, DefaultTypeAdapters.FLOAT)
                .registerTypeAdapter(Double.class, DefaultTypeAdapters.DOUBLE)
                .registerTypeAdapter(double.class, DefaultTypeAdapters.DOUBLE)
                .registerTypeAdapter(Number.class, DefaultTypeAdapters.NUMBER)
                .registerTypeAdapter(String.class, DefaultTypeAdapters.STRING)
                .registerTypeAdapter(BigDecimal.class, DefaultTypeAdapters.BIG_DECIMAL)
                .serializeNulls() //不忽略为 null 的参数
                .create();
    }

    /**
     * Return the type of {@link List} with the {@code type}.
     *
     * @param type The type.
     * @return the type of {@link List} with the {@code type}
     */
    public static Type getListType(final Type type) {
        return TypeToken.getParameterized(List.class, type).getType();
    }

    /**
     * @param keyCls key type
     * @param valCls value type
     * @return the type of {@link HashMap} with the {@code type}
     */
    public static Type getHashMapType(Type keyCls, Type valCls) {
        return TypeToken.getParameterized(HashMap.class, keyCls, valCls).getType();
    }
}