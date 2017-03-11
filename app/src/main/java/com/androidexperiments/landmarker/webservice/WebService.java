package com.androidexperiments.landmarker.webservice;

import android.content.Context;
import android.util.Log;

import com.androidexperiments.landmarker.webservice.cookie.PersistentCookieStore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.concurrent.TimeUnit;

import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class WebService {
    private Context context;
    protected String message = "";
    protected int success = -1;

    private OkHttpClient client;
    private OkHttpClient client_longtimeout;
    private final int TIMEOUT_SECONDS = 60*5;
    private final int TIMEOUT_SECONDS_LONG = 30 * 2 * 2;

    public WebService(Context context) {
        this.context = context;
        CookieHandler cookieHandler = null;
        try {
            // init cookie manager
            cookieHandler = new CookieManager(new PersistentCookieStore(context), CookiePolicy.ACCEPT_ALL);
        } catch (Exception ignored) {
        }

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        client = new OkHttpClient.Builder()
                .cookieJar(new JavaNetCookieJar(cookieHandler))
                .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build();

        client_longtimeout = new OkHttpClient.Builder()
                .cookieJar(new JavaNetCookieJar(cookieHandler))
                .connectTimeout(TIMEOUT_SECONDS_LONG, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_SECONDS_LONG, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build();
    }

    /**
     * GET network request
     *
     * @param url url
     * @return
     */
    protected String GET(String url) {
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response response = client.newCall(request).execute();
            String res = response.body().string();
            Log.e("URL", url);
            return res;
        } catch (Exception e) {
            return "";
        }
    }






    public boolean isSuccess() {
        return success != -1;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public Gson getGsonInstance() {
        return new GsonBuilder().registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory()).create();
    }

    private class NullStringToEmptyAdapterFactory<T> implements TypeAdapterFactory {
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {

            Class<T> rawType = (Class<T>) type.getRawType();
            if (rawType != String.class) {
                return null;
            }
            return (TypeAdapter<T>) new StringAdapter();
        }
    }

    private class StringAdapter extends TypeAdapter<String> {
        public String read(JsonReader reader) throws IOException {
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull();
                return "";
            }
            return reader.nextString();
        }

        public void write(JsonWriter writer, String value) throws IOException {
            if (value == null) {
                writer.nullValue();
                return;
            }
            writer.value(value);
        }
    }
}
