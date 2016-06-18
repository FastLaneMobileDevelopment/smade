package io.bega.servicebase.model;

import java.io.UnsupportedEncodingException;

import android.util.Base64;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class UserToken {

  @SerializedName("userId")
  public String UserId;

  @SerializedName("id")
  public String Token;

  @SerializedName("ttl")
  public int Ttl;

  @SerializedName("created")
  public String Created;

    @SerializedName("mobile")
    public String MobileCreated;

  public UserToken() {

  }
    public boolean isValid()
    {
        if (MobileCreated == null || MobileCreated.length() == 0)
        {
            return false;
        }

        try {
            DateTimeFormatter formatter =
                    DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            DateTime created = formatter.parseDateTime(MobileCreated);
            DateTime current = created.plusMillis(Ttl);
            return current.isAfterNow();
        }
        catch(Exception ex)
        {
            Log.e("Error","error", ex);
            return false;
        }
    }

  @Override
  public String toString() {
    return String.format("Basic %s", encodeCredentials());
  }

  private String encodeCredentials() {
    byte[] data;
    try {
      String credentials = String.format("%s:%s", UserId, Token);
      data = credentials.getBytes("UTF-8");
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException("Error encoding credentials: " + e.getMessage(), e);
    }
    return Base64.encodeToString(data, Base64.DEFAULT);
  }
}
