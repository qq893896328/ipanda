package cn.cntv.app.ipanda.utils;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/***
 * json工具解析类
 */
public class JsonUtils {

   private static Gson gson = new Gson();

    public static <T> T toBean(Class<T> type, String s) {
        if (s == null) {
            return null;
        }
       return gson.fromJson(s, type);
    }

    public static <T> T toBean(Type type, String s) {
        if (s == null) {
            return null;
        }
        return gson.fromJson(s, type);
    }


    public static <T> List<T> getList(Class<T[]> type, String s) {
        if (s == null) return null;
        List<T> results = new ArrayList<T>();
        try {
            T[] _next = toBean(type, s);
            if (_next != null)
                Collections.addAll(results, _next);
        } catch (Exception e) {
            return null;
        }
        return results;
    }


    public static <T> T toBean(Class<T> type, Reader reader) throws IOException {

        TypeAdapter<T> adapter = gson.getAdapter(type);
        JsonReader jsonReader = gson.newJsonReader(reader);
        try {
           return adapter.read(jsonReader);
        } finally {
            jsonReader.close();
        }

    }
}
