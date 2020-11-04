package io.github.kimmking.gateway.outbound.factory;

import io.github.kimmking.gateway.outbound.AbstractOutboundHandler;
import io.github.kimmking.gateway.outbound.httpclient4.HttpOutboundHandler;
import io.github.kimmking.gateway.outbound.okhttp.OkhttpOutboundHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author lizeteng
 * @date 2020/11/04
 */
public class OutboundHandlerFactory {

  private static final Map<String, Function<String, AbstractOutboundHandler>>
      OUTBOUND_HANDLER_BY_TYPE;

  static {
    OUTBOUND_HANDLER_BY_TYPE = new HashMap<>(4);
    OUTBOUND_HANDLER_BY_TYPE.put(Type.HTTP_CLIENT, HttpOutboundHandler::new);
    OUTBOUND_HANDLER_BY_TYPE.put(Type.OK_HTTP, OkhttpOutboundHandler::new);
  }

  public static AbstractOutboundHandler create(String type, String proxyServer) {
    return OUTBOUND_HANDLER_BY_TYPE.get(type).apply(proxyServer);
  }

  public interface Type {

    String HTTP_CLIENT = "HttpClient";

    String OK_HTTP = "OkHttp";
  }
}
