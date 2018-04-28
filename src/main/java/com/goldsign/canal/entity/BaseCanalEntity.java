package com.goldsign.canal.entity;

import java.io.Serializable;

import com.alibaba.otter.canal.protocol.CanalEntry.EventType;

public class BaseCanalEntity<T>  implements Serializable{
	private static final long serialVersionUID = 1L;
	protected EventType eventType;
	
	public BaseCanalEntity() {
		super();
	}
	public BaseCanalEntity(EventType eventType) {
		super();
		this.eventType = eventType;
	}
	
	public EventType getEventType() {
		return eventType;
	}
	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}

}
