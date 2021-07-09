package org.twittig.okhttp;

import okhttp3.*;
import org.hamcrest.core.Is;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;

public class OkHttpClientWithOAuth2Test {

  public static final String CLIENT_ID = "clientId";
  public static final String CLIENT_SECRET = "clientSecret";
  public static final String TOKEN_URI = "tokenUri";
  public static final String CLIENT_CREDENTIALS = "client_credentials";
  public static final String OAUTH2_SECURED_API_ENDPOINT = "apiEndpoint";

  @Test
  public void test() throws IOException {

    AccessTokenProvider accessTokenProvider =
        new AccessTokenProvider(
            new OkHttpClient(), CLIENT_ID, CLIENT_SECRET, TOKEN_URI, CLIENT_CREDENTIALS);

    OkHttpClient okHttpClient =
        new OkHttpClient.Builder()
            .addInterceptor(new AccessTokenInterceptor(accessTokenProvider))
            .authenticator(new AccessTokenAuthenticator(accessTokenProvider))
            .build();

    Response response =
        okHttpClient
            .newCall(
                new Request.Builder()
                    .url(OAUTH2_SECURED_API_ENDPOINT)
                    .post(RequestBody.create("{}", MediaType.parse("application/json")))
                    .build())
            .execute();

    assertThat(response.code(), Is.is(200));
  }
}
