package com.lizeteng.httpclient.util;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;

/**
 * @author lizeteng
 * @date 2020/10/28
 */
public class HttpClientUtils {

  private static final OkHttpClient OK_HTTP_CLIENT = new OkHttpClient();

  private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

  private HttpClientUtils() {}

  public static String get(String url) throws IOException {
    Request request = new Request.Builder().url(url).build();

    try (Response response = OK_HTTP_CLIENT.newCall(request).execute()) {
      return response.body().string();
    }
  }

  public static String post(String url, String body) throws IOException {
    RequestBody requestBody = RequestBody.create(body, JSON);

    Request request = new Request.Builder().url(url).post(requestBody).build();

    try (Response response = OK_HTTP_CLIENT.newCall(request).execute()) {
      return response.body().string();
    }
  }
}
