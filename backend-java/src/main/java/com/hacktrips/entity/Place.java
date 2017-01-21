package com.hacktrips.entity;

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

}
