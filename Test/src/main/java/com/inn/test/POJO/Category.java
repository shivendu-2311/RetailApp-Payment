package com.inn.test.POJO;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;




@NamedQuery(name = "Category.getAllCategory", query = "select c from Category c where c.id in(select p.category from Product p where p.status = 'true')")

@Entity
@DynamicUpdate
@DynamicInsert
@Table(name="category")
//Serialization makes any POJO persistable by converting it into a byte stream
//Byte streams are used to perform input and output of 8-bit bytes. 
//They are used to read bytes from the input stream and write bytes to the output stream
public class Category implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name= "id")
	private Integer id;
	@Column(name= "name")
	private String name;
	
	public Category()
{
	
}

public Integer getId() {
	return id;
}

public void setId(Integer id) {
	this.id = id;
}

public String getName() {
	return name;
}

public void setName(String name) {
	this.name = name;
}

public Category(Integer id, String name) {
	super();
	this.id = id;
	this.name = name;
}

@Override
public String toString() {
	return "Category [id=" + id + ", name=" + name + ", getId()=" + getId() + ", getName()=" + getName() + "]";
}




}
