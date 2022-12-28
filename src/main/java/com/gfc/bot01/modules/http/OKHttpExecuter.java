package com.gfc.bot01.modules.http;

import java.io.IOException;
import java.util.Map;
import com.google.gson.Gson;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OKHttpExecuter {
  private OkHttpClient client = new OkHttpClient();
  private final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
  private final Gson gson = new Gson();

  public void sendPostByJson(String url, String replyToken, String json, Callback callback) {
    Request request =
        new Request.Builder().url(url).header("Authorization", "Bearer {" + replyToken + "}")
            .post(RequestBody.create(json, JSON)).build();
    client.newCall(request).enqueue(callback);
  }

  public void sendPostByJson(String url, Map<String, String> header, String json,
      Callback callback) {
    Request request = new Request.Builder().url(url).headers(Headers.of(header))
        .post(RequestBody.create(json, JSON)).build();
    client.newCall(request).enqueue(callback);
  }

  public String sendGetIntranet(String url, Map<String, String> header,
      Map<String, Object> params) {
    Request request =
        new Request.Builder().url(makeUrl(url, params)).headers(Headers.of(header)).get().build();
    try {
      Response response = client.newCall(request).execute();

      // Model model = gson.fromJson(response.body().string(), Model.class);
      String result = response.body().string();
      // System.out.println(String.format("response: %s", result));
      return result;
    } catch (IOException e) {
      e.printStackTrace();
      return String.format("fail: %s, error: %s", url, e.getMessage());
    }
  }

  private String makeUrl(String url, Map<String, Object> params) {
    if (params == null || params.isEmpty()) {
      return url;
    }
    StringBuilder result = new StringBuilder();
    for (Map.Entry<String, Object> entry : params.entrySet()) {
      result.append(String.format("%s=%s", entry.getKey(), entry.getValue().toString()));
    }
    return url + "?";
  }
}
