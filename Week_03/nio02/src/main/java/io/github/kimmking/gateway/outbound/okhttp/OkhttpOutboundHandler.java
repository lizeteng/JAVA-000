package io.github.kimmking.gateway.outbound.okhttp;

import io.github.kimmking.gateway.outbound.AbstractOutboundHandler;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * @author lizeteng
 * @date 2020/11/04
 */
public class OkhttpOutboundHandler extends AbstractOutboundHandler {

  private static final OkHttpClient OK_HTTP_CLIENT = new OkHttpClient();

  public OkhttpOutboundHandler(String proxyServer) {
    super(proxyServer);
  }

  @Override
  public void handle(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) {
    String url = buildUrl(fullHttpRequest);

    try (Response response = get(url)) {
      FullHttpResponse fullHttpResponse =
          new DefaultFullHttpResponse(
              HTTP_1_1, OK, Unpooled.wrappedBuffer(response.body().bytes()));
      fullHttpRequest.headers().set("Content-Type", "application/json");
      fullHttpRequest
          .headers()
          .setInt("Content-Length", Integer.parseInt(response.header("Content-Length")));
      channelHandlerContext.write(fullHttpResponse);
    } catch (Exception e) {
      e.printStackTrace();
      channelHandlerContext.write(new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT));
    } finally {
      channelHandlerContext.flush();
      channelHandlerContext.close();
    }
  }

  private Response get(String url) throws IOException {
    Request request = new Request.Builder().url(url).build();

    try (Response response = OK_HTTP_CLIENT.newCall(request).execute()) {
      return response;
    }
  }
}
