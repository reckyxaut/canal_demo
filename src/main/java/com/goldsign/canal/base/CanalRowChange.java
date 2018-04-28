package com.goldsign.canal.base;

import java.io.Serializable;
import java.util.List;

import com.alibaba.otter.canal.protocol.CanalEntry.EventType;
import com.alibaba.otter.canal.protocol.CanalEntry.RowData;

public class CanalRowChange implements Serializable{
	
	private static final long serialVersionUID = 3649317582631619814L;

	private String schemaName;

	private String tableName;
	
	private EventType eventType;

	private List<RowData> rowData;

	private String sql;

	
	public CanalRowChange() {
		super();
	}

	public CanalRowChange(String schemaName, String tableName, EventType eventType, List<RowData> rowData, String sql) {
		super();
		this.schemaName = schemaName;
		this.tableName = tableName;
		this.eventType = eventType;
		this.rowData = rowData;
		this.sql = sql;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public String getSchemaName() {
		return schemaName;
	}

	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public List<RowData> getRowData() {
		return rowData;
	}

	public void setRowData(List<RowData> rowData) {
		this.rowData = rowData;
	}

	public EventType getEventType() {
		return eventType;
	}

	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}
}
