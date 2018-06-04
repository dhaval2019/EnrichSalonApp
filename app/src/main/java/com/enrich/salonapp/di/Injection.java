package com.enrich.salonapp.di;

import android.content.Context;

import com.enrich.salonapp.data.DataRepository;
import com.enrich.salonapp.data.local.LocalDataSource;
import com.enrich.salonapp.data.remote.ApiService;
import com.enrich.salonapp.data.remote.RemoteDataSource;
import com.enrich.salonapp.util.EnrichUtils;
import com.enrich.salonapp.util.NetworkHelper;
import com.enrich.salonapp.util.threads.MainUiThread;
import com.enrich.salonapp.util.threads.ThreadExecutor;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.raizlabs.android.dbflow.config.DatabaseDefinition;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.enrich.salonapp.data.remote.RemoteDataSource.HOST;


public class Injection {

    public static DataRepository provideDataRepository(final Context context, MainUiThread mainUiThread,
                                                       ThreadExecutor threadExecutor, DatabaseDefinition databaseDefinition) {
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder requestBuilder;

                if (EnrichUtils.getAuthenticationModel(context) != null) {
                    requestBuilder = original.newBuilder().header("Content-Type", "application/json").header("Authorization", EnrichUtils.getAuthenticationModel(context).accessToken);
                } else {
                    requestBuilder = original.newBuilder().header("Content-Type", "application/json");
                }

                Request request = requestBuilder.build();

                return chain.proceed(request);
            }
        };

        OkHttpClient okHttpClient =
                new OkHttpClient.Builder()
                        .readTimeout(60, TimeUnit.SECONDS)
                        .connectTimeout(60, TimeUnit.SECONDS)
                        .addNetworkInterceptor(new StethoInterceptor())
                        .addInterceptor(interceptor)
                        .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        return DataRepository.getInstance(
                RemoteDataSource.getInstance(mainUiThread, threadExecutor, apiService),
                LocalDataSource.getInstance(mainUiThread, threadExecutor, databaseDefinition),
                NetworkHelper.getInstance());
    }
}
