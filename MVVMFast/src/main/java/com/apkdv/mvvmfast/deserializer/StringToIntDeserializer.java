package com.apkdv.mvvmfast.deserializer;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by ZhaoShulin on 2019-06-17 15:19.
 * <br>
 * Desc: json 转换
 * <br>
 */
public class StringToIntDeserializer implements JsonDeserializer<Integer> {
    @Override
    public Integer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (TextUtils.isEmpty(json.toString()) || json.toString().equals("\"\"")) {
            return 0;
        }
        return new Gson().fromJson(json, typeOfT);
    }
}
