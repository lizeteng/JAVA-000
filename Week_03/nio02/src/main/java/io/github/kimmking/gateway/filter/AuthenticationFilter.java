package io.github.kimmking.gateway.filter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.util.Objects;

import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * @author lizeteng
 * @date 2020/11/04
 */
public class AuthenticationFilter implements HttpRequestFilter {

  @Override
  public void filter(FullHttpRequest fullRequest, ChannelHandlerContext ctx) {
    if (authorizationIsBlank(fullRequest)) {
      ctx.write(new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.UNAUTHORIZED));
      ctx.flush();
      ctx.close();
    }
  }

  private boolean authorizationIsBlank(FullHttpRequest fullRequest) {
    String authorization = fullRequest.headers().get("Authorization");
    return Objects.isNull(authorization) || "".equals(authorization);
  }
}
