package com.goldsign.canal.app.jxky.entity;

import java.util.Date;

import com.alibaba.otter.canal.protocol.CanalEntry.EventType;
import com.goldsign.canal.entity.BaseCanalEntity;

public class Student extends BaseCanalEntity<Student> {
	private static final long serialVersionUID = 1L;
	String id;
	String name;
	String age;
	Integer count;
	Date addtime;
	Date addstamp;
	String tmp1;
	String tmp2;
	
	public Student() {
		super();
	}
	public Student(EventType eventType) {
		super(eventType);
	}
	
	public Date getAddtime() {
		return addtime;
	}
	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}
	public Date getAddstamp() {
		return addstamp;
	}
	public void setAddstamp(Date addstamp) {
		this.addstamp = addstamp;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	
	public String getTmp1() {
		return tmp1;
	}
	public void setTmp1(String tmp1) {
		this.tmp1 = tmp1;
	}
	public String getTmp2() {
		return tmp2;
	}
	public void setTmp2(String tmp2) {
		this.tmp2 = tmp2;
	}
	@Override
	public String toString() {
		return "Student [id=" + id + ", name=" + name + ", age=" + age + ", count=" + count + "]";
	}
	
}
