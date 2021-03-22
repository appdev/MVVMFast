package com.apkdv.mvvmfast.deserializer;

import com.apkdv.mvvmfast.network.GsonUtil;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by ZhaoShulin on 2019-06-17 15:19.
 * <br>
 * Desc: json 转换
 * <br>
 */
public class StringToListDeserializer implements JsonDeserializer<ArrayList<String>> {
    @Override
    public ArrayList<String> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json == null || json.toString().isEmpty() || json.toString().equals("null") || json.toString().equals("\"\"")) {
            return new ArrayList<>();
        } else
            return GsonUtil.gson()
                    .fromJson(json, typeOfT);
    }
}
