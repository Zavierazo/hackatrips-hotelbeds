package com.hacktrips.model.carto;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;

import com.hacktrips.config.contamination.ContaminationData;
import com.hacktrips.model.minube.POIData;

import lombok.Data;

@Data
public class CartoPostgreSQL {

	public enum TypeSQL {
		CREATE, INSERT
	}

	private static final String CREATE_TABLE = "CREATE TABLE";
	private static final String INSERT_INTO = "INSERT INTO";
	private static final String VALUES = "VALUES";

	private String tableName;
	private Map<String, Class<?>> columns = new HashMap<>();
	private List<Map<String, Object>> valuesOfColumns = new ArrayList<>();
	private List<String> queueToTables = new ArrayList<>();

	private TypeSQL typeSQL;
	private List<POIData> pois;
	private Boolean subset = false;

	public CartoPostgreSQL(String tableName, List<POIData> pois) {
		this.tableName = tableName;
		this.pois = pois;
	}

	public String generateDataSet() {
		if (TypeSQL.CREATE.equals(typeSQL)) {
			prepareColumnsForCreate();
			return createCmd();
		} else if (TypeSQL.INSERT.equals(typeSQL)) {
			prepareColumnsForInsert();
			return insertCmd();
		}
		return null;
	}

	private void prepareColumnsForCreate() {
		columns.clear();
		Field[] fields = POIData.class.getDeclaredFields();
		boolean found = false;
		if (!queueToTables.isEmpty()) {
			for (String entity : queueToTables) {
				for (Field field : fields) {
					if (entity.equalsIgnoreCase(field.getName()) && !found) {
						columns.put(field.getName(), field.getType());
						found = true;
						// skip all queues
					}
				}
			}
			// the queue is only for this iteration in the createCmd maybe we
			// can add more TODO add java queue....
			queueToTables.clear();
		} else {
			if (subset) {
				for (Field field : fields) {
					if (field.getType().isAssignableFrom(ContaminationData.class)) {
						columns.put(field.getName(), field.getType());
						break;
					}
				}
			} else {
				for (Field field : fields) {
					columns.put(field.getName(), field.getType());
				}
			}

		}
	}

	private void prepareColumnsForInsert() {
		// private Integer id;
		// private String name;
		// private Double latitude;
		// private Double longitude;
		// private String picture_url;
		// private Double distance;
		// private Double prob;
		// private Map<Integer, Double> contaminationByHour = new HashMap<>();
		for (POIData poi : pois) {
			Map<String, Object> values = new HashMap<>();
			values.put("id", poi.getId());
			values.put("name", poi.getName());
			values.put("latitude", poi.getLatitude());
			values.put("longitude", poi.getLongitude());
			values.put("picture_url", poi.getPicture_url());
			values.put("distance", poi.getDistance());
			values.put("prob", poi.getProb());
			values.put("contaminationByHour", poi.getContaminationByHour());
			valuesOfColumns.add(values);
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
			boolean skipColumn = false;
			Class<?> type = columns.get(key);
			if (type.isAssignableFrom(Map.class)) {
				queueToTables.add(key);
				skipColumn = true;
			} else {
				str.append(key);
				str.append(StringUtils.SPACE);
				if (type.isAssignableFrom(Integer.class)) {
					str.append("integer");
				} else if (type.isAssignableFrom(String.class)) {
					str.append("varchar");
				} else if (type.isAssignableFrom(Double.class)) {
					str.append("decimal");
				}
			}
			if (++count < columns.size() && !skipColumn) {
				str.append(",");
			}
		}
		str.append(");");
		return str.toString();
	}

	private String insertCmd() {
		StringBuffer str = new StringBuffer();

		for (Map<String, Object> mapValue : valuesOfColumns) {

			SortedSet<String> keys = new TreeSet<String>(mapValue.keySet());

			str.append(StringUtils.SPACE);
			str.append(INSERT_INTO);
			str.append(StringUtils.SPACE);
			str.append(tableName);
			str.append("(");
			int count = 0;
			for (String keyValues : keys) {
				// for (String key : columns.keySet()) {
				// if (key.equalsIgnoreCase(keyValues)) {
				str.append(keyValues);
				str.append(StringUtils.SPACE);
				if (++count < mapValue.size()) {
					str.append(",");
				}
				// }
				// }
			}
			str.append(")");
			str.append(StringUtils.SPACE);
			str.append(VALUES);
			str.append(StringUtils.SPACE);
			str.append("(");
			int count2 = 0;
			for (String keyValues : keys) {
				// for (String key : columns.keySet()) {
				// if (key.equalsIgnoreCase(keyValues)) {
				Object value = mapValue.get(keyValues);
				if (value != null && value instanceof String) {
					str.append("'");
					str.append(value);
					str.append("'");
				} else {
					if (value != null)
						str.append(value);
					else
						str.append(0);
				}

				if (++count2 < mapValue.size()) {
					str.append(",");
				}
				// }
				// }
			}
			str.append(StringUtils.SPACE);
			str.append(");");
		}
		return str.toString();
	}
}
