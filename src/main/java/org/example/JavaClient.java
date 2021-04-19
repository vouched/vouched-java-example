package org.example;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;


import java.util.Base64;
import java.util.Properties;

public class JavaClient {
  private CloseableHttpClient client = null;
  public JavaClient() {
    if(this.client == null) {
      this.client = HttpClients.createDefault();
    }
  }
  public static Properties ConfigProperties() throws IOException {
    Properties prop = new Properties();
    InputStream input = JavaClient.class.getClassLoader().getResourceAsStream("config.properties");
    prop.load(input);
    return prop;
  }

  public CloseableHttpResponse GetRequest(String path) throws IOException, InterruptedException {
    String apiKey = JavaClient.ConfigProperties().getProperty("API_KEY");
    return GetRequest(path, apiKey);
  }

  public CloseableHttpResponse GetRequest(String path, String apikey) throws IOException, InterruptedException {
    HttpGet request = new HttpGet(URI.create(JavaClient.ConfigProperties().getProperty("API_URL")+ path));
    request.addHeader("accept", "application/json");
    request.addHeader("x-api-key", apikey);
    CloseableHttpResponse response = this.client.execute(request);
    return response;
  }


  public CloseableHttpResponse PostRequest(String path, String body) throws IOException, InterruptedException {
    String apiKey = JavaClient.ConfigProperties().getProperty("API_KEY");
    return PostRequest(path, body, apiKey);
  }


  public CloseableHttpResponse PostRequest(String path, String body, String apikey) throws IOException, InterruptedException {
    HttpPost request = new HttpPost(URI.create(JavaClient.ConfigProperties().getProperty("API_URL")+ path));
    StringEntity params = new StringEntity(body, ContentType.APPLICATION_JSON);
    request.addHeader("x-api-key", apikey);
    request.setEntity(params);
    CloseableHttpResponse send = this.client.execute(request);
    return send;
  }

  public String ImageToBase64(String path) throws IOException {
    byte[] fileContent = FileUtils.readFileToByteArray(new File(path));
    String encodedString = Base64.getEncoder().encodeToString(fileContent);
    return encodedString;
  }


}
