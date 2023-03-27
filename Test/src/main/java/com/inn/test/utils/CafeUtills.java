//util clases are used to store all the genrics of class
package com.inn.test.utils;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class CafeUtills {

	private CafeUtills()
	{
		
	}
	
	public static ResponseEntity<String> getResponseEntity(String responseMessage, HttpStatus httpStatus)
	{
	return new ResponseEntity<String>( "{\"message\":\""+ responseMessage + "\"}", httpStatus);
	}
	
	public static String getUUID()
	{
		Date date = new Date();
		return "Bills:-" + date.getTime();
	}
	
	public static JSONArray getJsonArrayFromStrng(String data) throws JSONException {
		JSONArray jsonArray = new JSONArray(data);
		return jsonArray;
	}
	
	public static Map<String,Object> getMapFromJson(String data)
	{
		if(!Strings.isNullOrEmpty(data))
		{
//GSON: to convert Java Objects into their JSON representation or vice versa
			
			return new Gson().fromJson(data, new TypeToken<Map<String,Object>>(){
				
			}.getType());
		}
		return new HashMap<>();
	}
	
	public static Boolean isFileExist(String path)
	{
		System.out.println("Inside isFileExist ");
		
		try {
			File file = new File(path);
			return file!=null && file.exists() ? Boolean.TRUE: Boolean.FALSE;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return false;
	}
}
