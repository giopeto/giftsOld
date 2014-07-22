package com.gifts.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtil {

	public static JSONObject makeJson(HttpServletRequest request) throws IOException {
		JSONObject jsonObject = new JSONObject();
		StringBuilder sb = new StringBuilder();
	    BufferedReader br = request.getReader();
	    String str;
	    while( (str = br.readLine()) != null ){
	        sb.append(str);
	    }  
	   
	    /*if (sb.length()) {
	    	
	    }*/
	    
	    try {
			jsonObject = new JSONObject(sb.toString());
		} catch (JSONException e) {
			
		}
	    return jsonObject;
	}

}
