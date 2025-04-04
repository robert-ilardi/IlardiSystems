/**
 * Created Jun 24, 2024
 */
package com.ilardi.systems.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @Author rober
 */

public class SimpleJsonRestClient<Tin, Tout> {

  private static final Logger logger = LogManager.getLogger(SimpleJsonRestClient.class);

  protected ObjectMapper jsonMapper;

  public SimpleJsonRestClient() {
    jsonMapper = getObjectMapper();
  }

  protected ObjectMapper getObjectMapper() {
    return new ObjectMapper();
  }

  protected CloseableHttpClient createHttpClient() {
    CloseableHttpClient httpClient;

    // httpClient=new DefaultHttpClient();
    httpClient = HttpClientBuilder.create().build();

    return httpClient;
  }

  protected HttpPost createHttpPost(String apiUrl, String jsonStr, Properties customHeaders) throws UnsupportedEncodingException {
    HttpPost httpPost;
    Set<Object> kSet;
    Iterator<Object> kIter;
    String key, value;

    httpPost = new HttpPost(apiUrl);

    httpPost.setHeader("Content-Type", "application/json");
    httpPost.setHeader("Accept", "application/json");

    if (customHeaders != null) {
      kSet = customHeaders.keySet();
      kIter = kSet.iterator();

      while (kIter.hasNext()) {
        key = (String) kIter.next();

        value = customHeaders.getProperty(key);

        httpPost.setHeader(key, value);
      }
    }

    httpPost.setEntity(new StringEntity(jsonStr));

    return httpPost;
  }

  public Tout send(String apiUrl, Tin inputObj, Class<Tout> outputClass, Properties customHeaders) throws IOException {
    Tout outputObj;
    String jsonStr, outStr;

    jsonStr = toJsonString(inputObj);

    outStr = send(apiUrl, jsonStr, customHeaders);

    outputObj = fromJsonString(outStr, outputClass);

    return outputObj;
  }

  protected String send(String apiUrl, String jsonStr, Properties customHeaders) throws IOException {
    String outStr;
    CloseableHttpClient httpClient = null;
    HttpPost httpPost = null;
    HttpResponse httpRes;

    try {
      httpClient = createHttpClient();

      httpPost = createHttpPost(apiUrl, jsonStr, customHeaders);

      httpRes = post(httpClient, httpPost);

      outStr = handle(httpRes);

      return outStr;
    }
    finally {
      if (httpPost != null) {
        httpPost.releaseConnection();
      }

      if (httpClient != null) {
        httpClient.close();
      }
    }
  }

  protected String handle(HttpResponse httpRes) throws ParseException, IOException {
    String resStr;
    int httpStatusCd;
    HttpEntity httpEntity;

    httpStatusCd = httpRes.getStatusLine().getStatusCode();

    httpEntity = httpRes.getEntity();

    if (httpEntity != null) {
      resStr = EntityUtils.toString(httpEntity);

      switch (httpStatusCd) {
        case HttpStatus.SC_OK:
          return resStr;
        default:
          throw new IOException("HTTP Error Code Received. HTTP Status Code: " + httpStatusCd + " ; returned text: " + resStr);
      }
    }
    else {
      throw new IOException("NULL HTTP Entity Received. HTTP Status Code: " + httpStatusCd);
    }
  }

  protected HttpResponse post(HttpClient httpClient, HttpPost httpPost) throws ClientProtocolException, IOException {
    HttpResponse httpRes;

    httpRes = httpClient.execute(httpPost);

    return httpRes;
  }

  protected String toJsonString(Tin inputObj) throws JsonProcessingException {
    String jsonStr;

    jsonStr = jsonMapper.writeValueAsString(inputObj);

    return jsonStr;
  }

  protected Tout fromJsonString(String jsonStr, Class<Tout> outputClass) throws JsonMappingException, JsonProcessingException {
    Tout outputObj;

    outputObj = jsonMapper.readValue(jsonStr, outputClass);

    return outputObj;
  }

}
