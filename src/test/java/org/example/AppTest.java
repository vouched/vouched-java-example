package org.example;

import org.apache.commons.io.FileUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

import static org.junit.Assert.*;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    private JSONObject crosscheckRequest;
    private JSONObject inviteRequest;
    private JSONObject jobRequest;
    private JSONObject aamvaRequest;
    private JavaClient client;


    private static JSONObject createCheckRequest() {
      JSONObject address = new JSONObject();
      address.put("streetAddress", "1 Main St");
      address.put("city", "Seattle");
      address.put("postalCode", "98031");
      address.put("state", "WA");
      address.put("country", "US");

      JSONObject obj = new JSONObject();
      obj.put("firstName", "John");
      obj.put("lastName", "Bao");
      obj.put("email","baoman@mail.com");
      obj.put("phone","917-343-3433");
      obj.put("ipAddress","73.19.102.110");
      obj.put("address", address);

      return obj;
    }

    private static JSONObject createInviteRequest() {
      JSONObject inviteRequest = new JSONObject();
      inviteRequest.put("email", "darwin66@lkxloans.com");
      inviteRequest.put("firstName", "John");
      inviteRequest.put("lastName", "Bao");
      inviteRequest.put("phone", "0000000000");
      inviteRequest.put("contact", "phone");
      return inviteRequest;
    }

    private JSONObject createJobRequest() throws IOException {
      JSONObject jobRequest = new JSONObject();
      jobRequest.put("type", "id-verification");

      JSONObject obj = new JSONObject();
      obj.put("dob", "02/11/1995");
      obj.put("firstName", "John");
      obj.put("lastName", "Bao");
      obj.put("idPhoto", client.ImageToBase64("src/main/resources/data/test-id.png"));

      jobRequest.put("params", obj);
      return jobRequest;
    }

    private JSONObject createAamvaRequest() {
      JSONObject aamvaRequest = new JSONObject();
      aamvaRequest.put("licenseNumber", "520AS4197");
      aamvaRequest.put("country", "US");
      aamvaRequest.put("lastName", "Bao");
      aamvaRequest.put("idType", "drivers-license");
      aamvaRequest.put("state", "IA");
      aamvaRequest.put("dob", "05/29/1990");
      aamvaRequest.put("issueDate", "05/29/2020");
      aamvaRequest.put("expirationDate", "05/29/2025");
      return  aamvaRequest;
    }

    @Before
    public void setUp() throws IOException {
      client = new JavaClient();
      crosscheckRequest = AppTest.createCheckRequest();
      inviteRequest = AppTest.createInviteRequest();
      jobRequest = this.createJobRequest();
      aamvaRequest = this.createAamvaRequest();
    }

    @Test
    public void UnauthorizedInvites() throws IOException, InterruptedException {
      CloseableHttpResponse response = client.GetRequest("/invites", "2343253465");
      assertEquals(response.getStatusLine().getStatusCode(), 401);
    }

    @Test
    public void InvitesGet() throws IOException, InterruptedException {
      CloseableHttpResponse response = client.GetRequest("/invites");
      String  responseString = EntityUtils.toString(response.getEntity());
      JSONObject responseJson = new JSONObject(responseString);
      assertTrue(responseJson.getInt("total") > -1);
      assertTrue(responseJson.getInt("pageSize") > -1);
      assertTrue(responseJson.getJSONArray("items").length() > 1);
      assertEquals(response.getStatusLine().getStatusCode(), 200);
    }

    @Test
    public void InviteWithBadPhoneNumber() throws IOException, InterruptedException {
      JSONObject request = new JSONObject(inviteRequest.toString());
      request.remove("email");
      CloseableHttpResponse response = client.PostRequest("/invites", request.toString());
      assertEquals(response.getStatusLine().getStatusCode(), 400);
    }

    @Test
    public void ValidInviteRequest() throws IOException, InterruptedException {
      JSONObject request = new JSONObject(inviteRequest.toString());
      request.remove("email");
      request.put("phone", "+447555555555");
      CloseableHttpResponse response = client.PostRequest("/invites", request.toString());
      String  responseString = EntityUtils.toString(response.getEntity());
      JSONObject responseJson = new JSONObject(responseString);
      assertEquals(responseJson.getBoolean("send"), true);
      assertEquals(response.getStatusLine().getStatusCode(), 201);
    }


    @Test
    public void MakeCrosscheckRequest() throws IOException, InterruptedException {
      CloseableHttpResponse response = client.PostRequest("/identity/crosscheck", crosscheckRequest.toString());
      assertEquals(response.getStatusLine().getStatusCode(), 200);
      String  responseString = EntityUtils.toString(response.getEntity());
      JSONObject responseJson = new JSONObject(responseString);
      assertTrue(!responseJson.getString("id").isEmpty());
    }

    @Test
    public void IdentityDocuments() throws IOException, InterruptedException {
      CloseableHttpResponse response = client.GetRequest("/identity/documents");
      assertEquals(response.getStatusLine().getStatusCode(), 200);
    }


    @Test
    public void JobRequestWithId() throws IOException, InterruptedException {
      CloseableHttpResponse response = client.PostRequest("/jobs", jobRequest.toString());
      assertEquals(response.getStatusLine().getStatusCode(), 201);
      String  responseString = EntityUtils.toString(response.getEntity());
      JSONObject responseJson = new JSONObject(responseString);
      assertTrue(!responseJson.getString("id").isEmpty());
      assertTrue(responseJson.getString("status").equalsIgnoreCase("completed"));
    }

    @Test
    public void JobRequestWithSunglasses() throws IOException, InterruptedException {
      JSONObject request = new JSONObject(jobRequest.toString());
      JSONObject params = request.getJSONObject("params");
      params.put("userPhoto", client.ImageToBase64("src/main/resources/data/test-sunglasses.jpg"));
      request.put("params", params);
      CloseableHttpResponse response = client.PostRequest("/jobs", request.toString());
      assertEquals(response.getStatusLine().getStatusCode(), 201);
      String  responseString = EntityUtils.toString(response.getEntity());
      JSONObject responseJson = new JSONObject(responseString);
      assertTrue(!responseJson.getString("id").isEmpty());
      assertTrue(responseJson.getString("status").equalsIgnoreCase("completed"));
    }

    @Test
    public void JobRequestWithFace() throws IOException, InterruptedException {
      JSONObject request = new JSONObject(jobRequest.toString());
      JSONObject params = request.getJSONObject("params");
      params.put("userPhoto", client.ImageToBase64("src/main/resources/data/test-face.png"));
      params.remove("idPhoto");
      request.put("params", params);
      CloseableHttpResponse response = client.PostRequest("/jobs", request.toString());
      assertEquals(response.getStatusLine().getStatusCode(), 201);
      String  responseString = EntityUtils.toString(response.getEntity());
      JSONObject responseJson = new JSONObject(responseString);
      assertTrue(!responseJson.getString("id").isEmpty());
      assertTrue(responseJson.getString("status").equalsIgnoreCase("completed"));
      JSONObject resultJson = responseJson.getJSONObject("result");
      assertEquals(resultJson.getBoolean("success"), true);
      assertEquals(resultJson.getBoolean("warnings"), false);
      assertEquals(resultJson.getBoolean("successWithSuggestion"), true);
      JSONObject confidences = resultJson.getJSONObject("confidences");
      Float selfieConfidence = confidences.getFloat("selfie");
      assertTrue("Selfie confidence (" + selfieConfidence + ") should be greater than threshold (" + 0.5 + ")",
      selfieConfidence > 0.5);
    }


    @Test
    public void JobRequestWithFaceAndId() throws IOException, InterruptedException {
      JSONObject request = new JSONObject(jobRequest.toString());
      JSONObject params = request.getJSONObject("params");
      params.put("userPhoto", client.ImageToBase64("src/main/resources/data/test-face.png"));
      request.put("params", params);
      CloseableHttpResponse response = client.PostRequest("/jobs", request.toString());
      assertEquals(response.getStatusLine().getStatusCode(), 201);
      String  responseString = EntityUtils.toString(response.getEntity());
      JSONObject responseJson = new JSONObject(responseString);
      assertTrue(!responseJson.getString("id").isEmpty());
      assertTrue(responseJson.getString("status").equalsIgnoreCase("completed"));
    }


    @Test
    public void JobRequestGet() throws IOException, InterruptedException {
      CloseableHttpResponse response = client.GetRequest("/jobs");
      String  responseString = EntityUtils.toString(response.getEntity());
      JSONObject responseJson = new JSONObject(responseString);
      assertTrue(responseJson.getInt("total") > -1);
      assertTrue(responseJson.getInt("pageSize") > -1);
      assertEquals(response.getStatusLine().getStatusCode(), 200);
    }

    @Test
    public void AamvaJobRequest() throws IOException, InterruptedException {
      JSONObject request = new JSONObject(aamvaRequest.toString());
      CloseableHttpResponse response = client.PostRequest("/identity/aamva", request.toString());
      String  responseString = EntityUtils.toString(response.getEntity());
      JSONObject responseJson = new JSONObject(responseString);
      JSONObject resultObject = responseJson.getJSONObject("result");
      assertEquals(response.getStatusLine().getStatusCode(), 200);
      assertTrue(resultObject.getString("aamva_status").equalsIgnoreCase("Pending"));
      assertTrue(resultObject.getString("job_status").equalsIgnoreCase("active"));
    }
}
