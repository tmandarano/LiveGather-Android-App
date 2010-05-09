package com.livegather.android.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class HttpRequest {
    private DefaultHttpClient httpClient;
    private HttpContext localContext;

    HttpResponse response = null;
    HttpPost httpPost = null;
    HttpGet httpGet = null;
    
    private JSONObject json;
    
    public JSONObject getJson() {
    	return this.json;
    }

    public HttpRequest() {
        HttpParams myParams = new BasicHttpParams();

        HttpConnectionParams.setConnectionTimeout(myParams, 10000);
        HttpConnectionParams.setSoTimeout(myParams, 10000);
        httpClient = new DefaultHttpClient(myParams);       
        localContext = new BasicHttpContext();    
    }

    public void clearCookies() {
        httpClient.getCookieStore().clear();
    }

    public void abort() {
        try {
            if (httpClient != null) {
                System.out.println("Abort.");
                httpPost.abort();
            }
        } catch (Exception e) {
            System.out.println("Your App Name Here" + e);
        }
    }

    public String sendJSONPost(URI uri, String path, Map<String, String> params) {
    	return sendPost(uri, path, params, "application/json");
    }
    
    public String sendPost(URI uri, String path, Map<String, String> params, String contentType) {
		Credentials creds = new UsernamePasswordCredentials(params.get("username"), params.get("password"));
		AuthScope authscope = new AuthScope(uri.getHost(), uri.getPort());
		httpClient.getCredentialsProvider().setCredentials(authscope, creds);
		JSONObject data = new JSONObject(params);
    	
        String ret = null;

        httpClient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.RFC_2109);

        httpPost = new HttpPost(uri);
        response = null;

        StringEntity tmp = null;        

        Log.d("Your App Name Here", "Setting httpPost headers");

        httpPost.setHeader("User-Agent", "SET YOUR USER AGENT STRING HERE");
        httpPost.setHeader("Accept", "text/html,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");

        if (contentType != null) {
            httpPost.setHeader("Content-Type", contentType);
        } else {
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
        }

        try {
            tmp = new StringEntity(data.toString(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.e("Your App Name Here", "HttpUtils : UnsupportedEncodingException : "+e);
        }

        httpPost.setEntity(tmp);

        Log.d("Your App Name Here", uri + "?" + data);

        try {
            response = httpClient.execute(httpPost,localContext);

            if (response != null) {
                this.json = readJSONObject(response);
                ret = EntityUtils.toString(response.getEntity());
            }
        } catch (Exception e) {
            Log.e("Your App Name Here", "HttpUtils: " + e);
        }

        Log.d("Your App Name Here", "Returning value:" + ret);

        return ret;
    }

    public String sendGet(URI uri, String username, String password) {
        httpGet = new HttpGet(uri);  
		Credentials creds = new UsernamePasswordCredentials(username, password);
		AuthScope authscope = new AuthScope(uri.getHost(), uri.getPort());
		httpClient.getCredentialsProvider().setCredentials(authscope, creds);

        try {
            response = httpClient.execute(httpGet);  
        } catch (Exception e) {
            Log.e("Your App Name Here", e.getMessage());
        }

        //int status = response.getStatusLine().getStatusCode();  

        String ret = null;
        // we assume that the response body contains the error message  
        try {
            ret = EntityUtils.toString(response.getEntity());  
        } catch (IOException e) {
            Log.e("Your App Name Here", e.getMessage());
        }

        return ret;
    }

    /**
     * Convert an InputStream to a String by using BufferedReader.readLine(). 
     * Iterate until the BufferedReader return null which means
     * there's no more data to read. Each line will appended to a StringBuilder
     * and returned as String.
     * 
     * @param is An InpuStream to convert
     */
	private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
 
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

	private JSONObject readJSONObject(HttpResponse response) throws IOException, JSONException {
		// Examine the response status
		Log.i("Praeda",response.getStatusLine().toString());

		// Get hold of the response entity
		HttpEntity entity = response.getEntity();

		JSONObject json = new JSONObject();
		if (entity != null) {
			// A Simple JSON Response Read
		    InputStream instream = entity.getContent();
		    String result = convertStreamToString(instream);
		    Log.i("Praeda",result);
		
		    // A Simple JSONObject Creation
		    json = new JSONObject(result);
		    this.json = json;
		    
		    // Closing the input stream will trigger connection release
		    instream.close();
		}
		
		return json;
	}
}