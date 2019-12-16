package com.arnold.common.network.di.module

import android.app.Application
import android.content.Context
import com.arnold.common.network.http.log.HttpLogger
import com.arnold.common.network.http.GlobalHttpHandler
import com.arnold.common.network.http.converter.CustomGsonConverterFactory
import com.arnold.common.network.utils.ZipHelper
import com.arnold.common.network.utils.convertCharset
import com.arnold.common.network.utils.isParseable
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.util.concurrent.ExecutorService
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import okhttp3.ResponseBody
import okio.Buffer
import kotlin.text.Charsets.UTF_8


@Module
class ClientModule {

    companion object {
        private const val TIME_OUT = 60
    }

    /**
     * 提供 [Retrofit]
     *
     * @param application   [Application]
     * @param configuration [RetrofitConfiguration]
     * @param builder       [Retrofit.Builder]
     * @param client        [OkHttpClient]
     * @param httpUrl       [HttpUrl]
     * @param gson          [Gson]
     * @return [Retrofit]
     */
    @Singleton
    @Provides
    internal fun provideRetrofit(
        application: Application,
        configuration: RetrofitConfiguration?,
        handler: GlobalHttpHandler?,
        builder: Retrofit.Builder,
        client: OkHttpClient,
        httpUrl: HttpUrl?,
        gson: Gson
    ): Retrofit {
        builder
            .baseUrl(httpUrl)//域名
            .client(client)//设置 OkHttp

        builder
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//使用 RxJava
            .addConverterFactory(CustomGsonConverterFactory.create(gson, handler))//使用 Gson

        configuration?.let {
            it.configRetrofit(application, builder)
        }
        return builder.build()
    }

    /**
     * 提供 [OkHttpClient]
     *
     * @param application     [Application]
     * @param configuration   [OkhttpConfiguration]
     * @param builder         [OkHttpClient.Builder]
     * @param interceptors    [<]
     * @param handler         [GlobalHttpHandler]
     * @param executorService [ExecutorService]
     * @return [OkHttpClient]
     */
    @Singleton
    @Provides
    internal fun provideClient(
        application: Application,
        configuration: OkhttpConfiguration?,
        builder: OkHttpClient.Builder,
        handler: GlobalHttpHandler?,
        executorService: ExecutorService,
        interceptors: MutableList<Interceptor>
    ): OkHttpClient {


        val interceptor = HttpLoggingInterceptor(HttpLogger())
        // BASIC 请求/响应行
        // HEADER 请求/响应行 + 头
        // BODY 请求/响应行 + 头 + 体
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        builder.connectTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)
            .readTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)
            .writeTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)
            .addNetworkInterceptor(interceptor)


        handler?.let {
            builder.addInterceptor(object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    return chain.proceed(it.onHttpRequestBefore(chain, chain.request()))
                }
            })
            //这里还有一个TOKEN处理
            builder.addNetworkInterceptor(object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {

                    val request = chain.request()
                    val originalResponse = chain.proceed(request)
                    val responseBody = originalResponse.body

                    var bodyString: String? = null
                    val contentType = responseBody?.contentType()
                    if (contentType != null && contentType.isParseable()) {
                        bodyString = getBodyString(originalResponse)
                    }

                    return it.onHttpResultResponse(bodyString, chain, originalResponse)
                }

            })
        }


        //如果外部提供了 Interceptor 的集合则遍历添加
        for (interceptor in interceptors) {
            builder.addInterceptor(interceptor)
        }

        //为 OkHttp 设置默认的线程池
        builder.dispatcher(Dispatcher(executorService))

        configuration?.configOkhttp(application, builder)
        return builder.build()
    }

    private fun getBodyString(response: Response): String? {
        val responseBody = response.newBuilder().build().body
        val source = responseBody?.source()
        source?.request(Long.MAX_VALUE) // Buffer the entire body.
        val buffer = source?.buffer()

        //获取content的压缩类型
        val encoding = response
            .headers["Content-Encoding"]

        val clone = buffer?.clone()

        return parseContent(responseBody, encoding, clone)
    }

    /**
     * 解析服务器响应的内容
     *
     * @param responseBody [ResponseBody]
     * @param encoding     编码类型
     * @param clone        克隆后的服务器响应内容
     * @return 解析后的响应结果
     */
    private fun parseContent(
        responseBody: ResponseBody?,
        encoding: String?,
        clone: Buffer?
    ): String? {

        val contentType = responseBody?.contentType()

        var charset = contentType?.charset(UTF_8) ?: UTF_8
        return if (encoding != null && encoding.equals(
                "gzip", ignoreCase = true
            )
        ) {//content 使用 gzip 压缩
            ZipHelper.decompressForGzip(clone?.readByteArray(), charset.convertCharset())//解压
        } else if (encoding != null && encoding.equals(
                "zlib", ignoreCase = true
            )
        ) {//content 使用 zlib 压缩
            ZipHelper.decompressToStringForZlib(
                clone?.readByteArray(),
                charset.convertCharset()
            )//解压
        } else {//content 没有被压缩, 或者使用其他未知压缩方式
            clone?.readString(charset)
        }
    }

    @Singleton
    @Provides
    internal fun provideRetrofitBuilder(): Retrofit.Builder {
        return Retrofit.Builder()
    }

    @Singleton
    @Provides
    internal fun provideClientBuilder(): OkHttpClient.Builder {
        return OkHttpClient.Builder()
    }


    /**
     * [Retrofit] 自定义配置接口
     */
    public interface RetrofitConfiguration {
        fun configRetrofit(context: Context, builder: Retrofit.Builder)
    }

    /**
     * [OkHttpClient] 自定义配置接口
     */
    public interface OkhttpConfiguration {
        fun configOkhttp(context: Context, builder: OkHttpClient.Builder)
    }

}