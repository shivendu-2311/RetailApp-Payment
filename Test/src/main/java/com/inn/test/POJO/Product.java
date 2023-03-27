package com.inn.test.POJO;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@DynamicUpdate
@DynamicInsert
@Table(name ="product")


@NamedQuery(name = "Product.getAllProduct", query = "select new com.inn.test.wrapper.ProductWrapper(p.id, p.name, p.description, p.price, p.status, p.category.id, p.category.name) from Product p")
@NamedQuery(name = "Product.updateProductStatus", query ="update Product p set p.status =: status where p.id =:id")
@NamedQuery(name = "Product.getProductByCategory", query ="select new com.inn.test.wrapper.ProductWrapper(p.id, p.name) from Product p where p.id =:id and p.status ='true'")
@NamedQuery(name = "Product.getProductById", query ="select new com.inn.test.wrapper.ProductWrapper(p.id,p.name,p.description,p.price) from Product p where p.id =: id")

public class Product implements Serializable{
	
	//private static final long serialVersionUID = 1L;

	public static final long serialVersionUID = 123456L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "name")
	private String name;
	
	@ManyToOne(fetch = FetchType.LAZY)//category is annoted with manytoone because it foreign key related Category Pojo
//FetchType.LAZY used when we want fetch data by specifying not when we accesing the pojo memeber by select * from product
	@JoinColumn(name = "category_fk", nullable = false)
	private Category category; 
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "price")
	private int price;
	
	@Column(name = "status")
	private String status;

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

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Product(Integer id, String name, Category category, String description, int price, String status) {
		super();
		this.id = id;
		this.name = name;
		this.category = category;
		this.description = description;
		this.price = price;
		this.status = status;
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + ", category=" + category + ", description=" + description
				+ ", price=" + price + ", status=" + status + ", getId()=" + getId() + ", getName()=" + getName()
				+ ", getCategory()=" + getCategory() + ", getDescription()=" + getDescription() + ", getPrice()="
				+ getPrice() + ", getStatus()=" + getStatus() + "]";
	}
	
	public Product()
	{
		
	}

	
	

}
