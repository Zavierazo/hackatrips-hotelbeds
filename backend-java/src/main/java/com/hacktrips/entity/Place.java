package com.hacktrips.entity;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import lombok.Data;

@Data
public class Place {

	public String icon;
	public String name;
	public String formatted_address;
	public String formatted_phone_number;
	public String reference;
	public JSONObject reviews;
	public String placeId;
	public String opening_hours;
	public HashMap<String,String> hours;
	public HashMap<String,Object> dayHours; 
	public ArrayList<String> weekday_text;

}
