package com.livegather.android.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.livegather.android.exceptions.InvalidRequestException;


public class RestClient2 
{
	public static final String POST_REQUEST_METHOD = "POST_REQUEST_METHOD";
	public static final String GET_REQUEST_METHOD = "GET_REQUEST_METHOD";
	public static final String PUT_REQUEST_METHOD = "PUT_REQUEST_METHOD";
	public static final String DELETE_REQUEST_METHOD = "DELETE_REQUEST_METHOD";

	private HttpResponse response;
	private JSONObject json;
	

	/**
	 * Calls the main connect method without a username or password. Used mainly 
	 * when an unauthenticated user is doing very basic things that don't require 
	 * auth.
	 * 
	 * @param uri 					The uri to connect to
	 * @param path					The path at the end of the host uri to call
	 * @param params				Parameters to pass to the REST service
	 * @param requestMethodType		The type of request being made
	 * @throws InvalidRequestException
	 */
	public void connect(URI uri, String path, HttpParams params, String requestMethodType) throws InvalidRequestException
	{
    	this.connect(uri, path, params, requestMethodType, "", "");
	}
    
	/**
	 * Makes an HttpClient call in order to execute a REST call using passed in 
	 * parameters, credentials and using the specified method type. This is 
	 * meant to generalize the act of making REST calls.
	 * 
	 * @param uri 					The uri to connect to
	 * @param path					The path at the end of the host uri to call
	 * @param params				Parameters to pass to the REST service
	 * @param requestMethodType		The type of request being made
	 * @param username				Username for the user
	 * @param password				Password for the user
	 * @throws InvalidRequestException
	 */
    public void connect(URI uri, String path, HttpParams params, String requestMethodType, String username, String password) throws InvalidRequestException
	{
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpUriRequest httprequest;

		Credentials creds = new UsernamePasswordCredentials(username, password);
		AuthScope authscope = new AuthScope(uri.getHost(), uri.getPort());
		httpclient.getCredentialsProvider().setCredentials(authscope, creds);

		URI fullUri = null;
		try {
			// In addition to the main URI we also need the URI to the actual 
			// call being made
			fullUri = new URI(uri.toString() + path);
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		if(requestMethodType.equals(GET_REQUEST_METHOD))
		{
			httprequest = new HttpGet(fullUri);
		}
		else if(requestMethodType.equals(POST_REQUEST_METHOD))
		{
			httprequest = new HttpPost(fullUri);
			httprequest.setParams(params);
		}
		else if(requestMethodType.equals(PUT_REQUEST_METHOD))
		{
			httprequest = new HttpPut(fullUri); 
			httprequest.setParams(params);
		}
		else if(requestMethodType.equals(DELETE_REQUEST_METHOD))
		{
			httprequest = new HttpDelete(fullUri); 
			httprequest.setParams(params);
		}
		else
		{
			throw new InvalidRequestException("Request method doesn't matchd delete, put, post or get");
		}

		HttpResponse response;

		try {
			response = httpclient.execute(httprequest);
			
			this.response = response;
			
            // Examine the response status
            Log.i("Praeda",response.getStatusLine().toString());
 
            // Get hold of the response entity
            HttpEntity entity = response.getEntity();
 
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
		} finally {
			httpclient.getConnectionManager().shutdown(); 
		}
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

	public HttpResponse getResponse() {
		return response;
	}
	public JSONObject getJson() {
		return json;
	}
}
