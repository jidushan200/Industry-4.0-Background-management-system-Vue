package org.tsinghuatj.framework.utils.excel;

import java.util.Date;

import org.joda.time.DateTime;
import org.tsinghuatj.framework.utils.excel.annotation.ExcelField;

public class Student {

	@ExcelField(title = "学号", order = 1)
	private Long id;

	@ExcelField(title = "姓名", order = 2)
	private String name;

	@ExcelField(title = "入学日期", order = 3)
	private Date date;

	@ExcelField(title = "班级", order = 4)
	private Integer classes;

	@ExcelField(title = "是否开除", order = 5)
	private String expel;

	public Student() {

	}

	public Student(String id, Date date, String name, String expel) {
		this.id = Long.valueOf(id);
		this.date = date;
		this.name = name;
		this.expel = expel;
	}

	@Override
	public String toString() {
		return "Student{" + "id=" + id + ", name='" + name + '\'' + ", date=" + new DateTime(date).toString("yyyy-MM-dd HH:mm:ss") + ", classes=" + classes + ", expel='" + expel + '\'' + '}';
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Integer getClasses() {
		return classes;
	}

	public void setClasses(Integer classes) {
		this.classes = classes;
	}

	public String getExpel() {
		return expel;
	}

	public void setExpel(String expel) {
		this.expel = expel;
	}
}
