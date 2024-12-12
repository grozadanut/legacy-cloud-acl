package ro.linic.cloud.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.ToString;

@Entity
@Table(name = "users", schema = "user_schema")
@ToString
public class User implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public static final String ID_FIELD = "id";
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(nullable = false)
	private String password;
	private String name;
	@Column(unique = true, nullable = false)
	private String email;
	@Column(unique = true, nullable = true)
	private String cnp;
	private String phone;
	
	public User()
	{
	}
	
	public String getName()
	{
		return name;
	}

	public User setName(final String name)
	{
		this.name = name;
		return this;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(final String email)
	{
		this.email = email;
	}
	
	public void setId(final int id)
	{
		this.id = id;
	}

	public int getId()
	{
		return id;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(final String password)
	{
		this.password = password;
	}
	
	public String getCnp()
	{
		return cnp;
	}
	
	public void setCnp(final String cnp)
	{
		this.cnp = cnp;
	}
	
	public String getPhone()
	{
		return phone;
	}
	
	public void setPhone(final String phone)
	{
		this.phone = phone;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cnp == null) ? 0 : cnp.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final User other = (User) obj;
		if (cnp == null)
		{
			if (other.cnp != null)
				return false;
		} else if (!cnp.equals(other.cnp))
			return false;
		if (email == null)
		{
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (id != other.id)
			return false;
		return true;
	}
}
