package org.twittig.okhttp.oauth2;

public class OAuthError {
  protected String error;
  protected String errorDescription;
  protected String errorUri;

  protected transient Exception exception;

  public OAuthError(Exception e) {
    exception = e;
  }

  public String getError() {
    return error;
  }

  public String getErrorDescription() {
    return errorDescription;
  }

  public String getErrorUri() {
    return errorUri;
  }

  public Exception getErrorException() {
    return exception;
  }
}
