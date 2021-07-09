package org.twittig.okhttp;

import okhttp3.Interceptor;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class AccessTokenInterceptor implements Interceptor {

  private final AccessTokenProvider accessTokenProvider;

  public AccessTokenInterceptor(AccessTokenProvider accessTokenProvider) {
    this.accessTokenProvider = accessTokenProvider;
  }

  @NotNull
  @Override
  public Response intercept(@NotNull Chain chain) throws IOException {

    String token = accessTokenProvider.getToken();

    if (token == null) {
      return chain.proceed(chain.request());
    }
    return chain.proceed(
        chain
            .request()
            .newBuilder()
            .addHeader("Authorization", String.format("Bearer %s", token))
            .build());
  }
}
