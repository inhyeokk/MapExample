package com.example.map.data.remote.api;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class KakaoLocalServiceFactory {
    private static final String BASE_URL = "https://dapi.kakao.com";
    private static final String REST_API_KEY = "b83c1687f3a7d168dbd29d37e543d9c1";

    public static <T> T create(Class<T> apiClass) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(new HeaderInterceptor())
            .addInterceptor(loggingInterceptor)
            .build();
        return new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(apiClass);
    }

    private static class HeaderInterceptor implements Interceptor {

        @NonNull
        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
            Request request = chain.request().newBuilder()
                .addHeader("Authorization", "KakaoAK " + REST_API_KEY)
                .build();
            return chain.proceed(request);
        }
    }
}
