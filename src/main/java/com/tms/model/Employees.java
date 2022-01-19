package com.tms.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "employees" , schema = "public")
public class Employees  implements Serializable {

    private  static final long serialVersionUID = 1L;

	@Id
	@Column(name = "employeesId")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employees_seq_gen")
	@SequenceGenerator(name = "employees_seq_gen", sequenceName = "employees_seq", allocationSize = 1)
	private Long id;

	private String firstName;

	@Column(name = "middle_name")
	private String middleName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "email")
	private String email;

	@Column(name = "address")
	private String address;

	@Column(name = "mobile_number")
	private Integer mobileNumber;

	@Column(name = "phone_number")
	private Integer phoneNumber;
	
	@Column(name = "username")
	private String username;
	
	@Column(name = "password", nullable = false)
	private String password;

	private String status;
    
}
