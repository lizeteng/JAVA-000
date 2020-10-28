package com.lizeteng.httpclient;

import com.lizeteng.httpclient.util.HttpClientUtils;

import java.io.IOException;

/**
 * @author lizeteng
 * @date 2020/10/28
 */
public class Main {

  private static final String URL = "http://localhost:8088/api/hello";

  public static void main(String[] args) {
    try {
      String response = HttpClientUtils.get(URL);
      System.out.println(response);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
