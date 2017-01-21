package com.hacktrips.model.carto;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.hacktrips.model.minube.POIData;

import lombok.Data;

@Data
public class CartoPostgreSQL {

	public enum TypeSQL {
		CREATE,
		INSERT
	}
	
	private static final String CREATE_TABLE = "CREATE TABLE";
	private static final String INSERT_INTO = "INSERT INTO";
	private static final String VALUES = "VALUES";

	private String tableName;
	private Map<String, Object> columns = new HashMap<>();
	private TypeSQL typeSQL;
	private List<POIData> pois;

	public CartoPostgreSQL(String tableName, Object entity) {
		this.tableName = tableName;
	}

	public String generateDataSet() {
		if (TypeSQL.CREATE.equals(typeSQL)) {
			prepareColumnsForCreate();
			return createCmd();
		} else if (TypeSQL.INSERT.equals(typeSQL)) {
			prepareColumnsForInsert();
			insertCmd();
		}
		return null;
	}
	
	private void prepareColumnsForCreate() {
		Field[] fields = POIData.class.getFields();
		for (Field field : fields) {
			columns.put(field.getName(), field.getDeclaringClass());
		}
	}
	
	private void prepareColumnsForInsert() {
		columns.clear();
		for (POIData poi : pois) {
			columns.put("name", poi.getName());
		}
	}
	
	private String createCmd() {
		StringBuffer str = new StringBuffer();
		str.append(CREATE_TABLE);
		str.append(StringUtils.SPACE);
		str.append(tableName);
		str.append("(");
		int count = 0;
		for (String key : columns.keySet()) {
			str.append(key);
			str.append(StringUtils.SPACE);
			Object type = columns.get(key);
			if (type instanceof Integer) {
				str.append("integer");
			} else if (type instanceof String) {
				str.append("varchar");
			}
			if (++count < columns.size()) {
				str.append(",");
			}
		}
		str.append(");");
		return str.toString();
	}
	
	private String insertCmd() {
		StringBuffer str = new StringBuffer();
		str.append(INSERT_INTO);
		str.append(StringUtils.SPACE);
		str.append(tableName);
		str.append("(");
		int count = 0;
		for (String key : columns.keySet()) {
			str.append(key);
			str.append(StringUtils.SPACE);			
			if (++count < columns.size()) {
				str.append(",");
			}
		}
		str.append(")");
		str.append(StringUtils.SPACE);
		str.append(VALUES);
		str.append(StringUtils.SPACE);
		str.append("(");

		count = 0;
		for (String key : columns.keySet()) {
			String value = (String)columns.get(key);
			str.append(value);
			str.append(StringUtils.SPACE);
			if (++count < columns.size()) {
				str.append(",");
			}
		}
		str.append(");");
		return str.toString();
	}
}
