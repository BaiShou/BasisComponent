package com.arnold.common.architecture.http.converter;

import com.arnold.common.architecture.http.GlobalHttpHandler;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import okhttp3.ResponseBody;
import retrofit2.Converter;
/**
 * @author：baisoo 创建时间：2018/11/9 11:17
 * 类描述：自定义响应类  修改做拦截抛出操作 应用到Gson反序列和序列化部分的知识
 * <p>
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public class CustomGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;
    private GlobalHttpHandler httpHandler;

    CustomGsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter, GlobalHttpHandler httpHandler) {
        this.gson = gson;
        this.adapter = adapter;
        this.httpHandler = httpHandler;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {

        if (httpHandler!=null){
            return httpHandler.onHttpInterceptResponse();
        }else {
            JsonReader jsonReader = gson.newJsonReader(value.charStream());
            try {
                return adapter.read(jsonReader);
            } finally {
                value.close();
            }
        }

    }

}
