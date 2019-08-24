package io.skymind.pathmind.data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class User
{
	@Id
	private long id;

	private String name;

	@NotNull
	private String email;

	@NotNull
	private String password;

	public User()
	{
	}

	public User(long id, String name, @NotNull String email, @NotNull String password)
	{
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
	}

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}
}
