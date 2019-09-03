package com.arnold.common.network.http.converter;

import com.arnold.common.network.http.GlobalHttpHandler;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * @author：baisoo 创建时间：2018/11/9 11:18
 * 类描述：修改Gson序列化工具，拦截错误
 * <p>
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public class CustomGsonConverterFactory extends Converter.Factory {
    private final Gson gson;
    private final GlobalHttpHandler httpHandler;

    private CustomGsonConverterFactory(Gson gson, GlobalHttpHandler httpHandler) {
        if (gson == null) {
            throw new NullPointerException("gson == null");
        }
        this.httpHandler = httpHandler;
        this.gson = gson;
    }

    public static CustomGsonConverterFactory create() {
        return create(new Gson());
    }

    public static CustomGsonConverterFactory create(Gson gson) {
        return new CustomGsonConverterFactory(gson, null);
    }

    public static CustomGsonConverterFactory create(Gson gson, GlobalHttpHandler httpHandler) {
        return new CustomGsonConverterFactory(gson, httpHandler);
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new CustomGsonResponseBodyConverter(gson, adapter, httpHandler);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new CustomGsonRequestBodyConverter<>(gson, adapter);
    }

}
