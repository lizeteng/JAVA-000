package io.github.kimmking.gateway.outbound;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * @author lizeteng
 * @date 2020/11/04
 */
public abstract class AbstractOutboundHandler {

  protected String proxyServer;

  public AbstractOutboundHandler(String proxyServer) {
    this.proxyServer = proxyServer;
  }

  public abstract void handle(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest);

  protected String buildUrl(FullHttpRequest fullHttpRequest) {
    return proxyServer + fullHttpRequest.uri();
  }
}
