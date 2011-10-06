package com.qsi.example;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Future;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;


@Entity
public class Task
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(length = 48)
	@NotEmpty
	@Size(max = 48)
	private String description;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Future
	private Date dueDate;

	private static final SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");

	public Date getDueDate()
	{
		return dueDate;
	}

	public Task setDueDate(String dueDate) throws ParseException
	{
		this.dueDate = format.parse(dueDate);
		return this;
	}

	public Long getId()
	{
		return id;
	}

	public Task setId(Long id)
	{
		this.id = id;
		return this;
	}

	public String getDescription()
	{
		return description;
	}

	public Task setDescription(String description)
	{
		this.description = description;
		return this;
	}

}
