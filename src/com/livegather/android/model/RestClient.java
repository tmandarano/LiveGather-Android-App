package com.livegather.android.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.livegather.android.exceptions.InvalidRequestException;


public class RestClient 
{
	public static final String POST_REQUEST_METHOD = "POST_REQUEST_METHOD";
	public static final String GET_REQUEST_METHOD = "GET_REQUEST_METHOD";
	public static final String PUT_REQUEST_METHOD = "PUT_REQUEST_METHOD";
	public static final String DELETE_REQUEST_METHOD = "DELETE_REQUEST_METHOD";

	private HttpResponse response;
	private JSONObject json;
	
	
	
	
	private String convertStreamToString(InputStream is) {
        /*
         * To convert the InputStream to String we use the BufferedReader.readLine()
         * method. We iterate until the BufferedReader return null which means
         * there's no more data to read. Each line will appended to a StringBuilder
         * and returned as String.
         */
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
    
    public void connect(String url, List<NameValuePair> params, String requestMethodType) throws InvalidRequestException
	{
    	this.connect( url,  params, requestMethodType, null, null);
	}
    
    public void connect(String url, List<NameValuePair> params, String requestMethodType, String username, String password) throws InvalidRequestException
	{
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpUriRequest httprequest = new HttpGet();

		if(username == null) {
			username = "projc";
		}
		if(password == null) {
			password = "pr0j(";
		}
		
		Credentials creds = new UsernamePasswordCredentials(username, password);
		AuthScope authscope = new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT);
		httpclient.getCredentialsProvider().setCredentials(authscope, creds);
				
		if(requestMethodType.equals(GET_REQUEST_METHOD))
		{
			httprequest.set
			httprequest = new HttpGet(url); 
		}
		else if(requestMethodType.equals(POST_REQUEST_METHOD))
		{
			httprequest = new HttpPost(url);
			HttpEntity entity = null;
			try {
				entity = new UrlEncodedFormEntity(params);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			((HttpPost) httprequest).setEntity(entity);
		}
		else if(requestMethodType.equals(PUT_REQUEST_METHOD))
		{
			httprequest = new HttpPut(url); 
			HttpEntity entity = null;
			try {
				entity = new UrlEncodedFormEntity(params);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			((HttpPut) httprequest).setEntity(entity);
		}
		else if(requestMethodType.equals(DELETE_REQUEST_METHOD))
		{
			httprequest = new HttpDelete(url); 
		}
		else
		{
			throw new InvalidRequestException("Request method doesn't matchd delete, put, post or get");
		}

		HttpResponse response = null;

		try {
			response = httpclient.execute(httprequest);
			
			this.response = response;
			
            // Examine the response status
            Log.i("Praeda",response.getStatusLine().toString());
 
            // Get hold of the response entity
            HttpEntity entity = response.getEntity();
            // If the response does not enclose an entity, there is no need
            // to worry about connection release
 
            if (entity != null) {
            	// A Simple JSON Response Read
                InputStream instream = entity.getContent();
                String result= convertStreamToString(instream);
                Log.i("Praeda",result);
 
                // A Simple JSONObject Creation
                JSONObject json=new JSONObject(result);
                this.json = json;
                
                // Closing the input stream will trigger connection release
                instream.close();
            }
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public HttpResponse getResponse() {
		return response;
	}
	public JSONObject getJson() {
		return json;
	}
}
