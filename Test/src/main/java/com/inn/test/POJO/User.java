package com.inn.test.POJO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

//@NameQuery annotation is used to define the single named query.
//implementing userdao interface
@NamedQuery(name = "User.FindByEmail", query= "select u from User u where u.email =:email")
//selecting all the user from db whose role is not admin;
//com.inn.test.wrapper.UserWrapper() to acces UserWrapper constructor
@NamedQuery(name ="User.getAllUser", query = "select new com.inn.test.wrapper.UserWrapper(u.id, u.name,u.email,u.contactNumber,u.status) from User u where u.role ='user'")
@NamedQuery(name ="User.updateStatus", query = "update User u set u.status =:status  where u.id =:id")
@NamedQuery(name ="User.getAllAdmin", query = "select u.email from User u where u.role ='admin'")
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name="user")
public class User {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "id")
	private Integer id;
	@Column(name = "name")
	private String name;
	@Column(name = "contactNumber")
	private String contactNumber;
	@Column(name = "email")
	private String email;
	@Column(name = "password")
	private String password;
	@Column(name = "status")
	private String status;
	@Column(name = "role")
	private String role;
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
	public String getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public User(Integer id, String name, String contactNumber, String email, String password, String status,
			String role) {
		super();
		this.id = id;
		this.name = name;
		this.contactNumber = contactNumber;
		this.email = email;
		this.password = password;
		this.status = status;
		this.role = role;
	}
	public User() {
		super();
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", contactNumber=" + contactNumber + ", email=" + email
				+ ", password=" + password + ", status=" + status + ", role=" + role + "]";
	}
}
