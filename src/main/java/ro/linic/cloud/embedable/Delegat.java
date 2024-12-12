package ro.linic.cloud.embedable;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Delegat implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Column(name = "delegatname")
	private String delegatName;
	private String cnp;
	@Column(name = "serieci")
	private String serieCI;
	private String elib;
	private String auto;
	
	public Delegat(final String name, final String cnp, final String serieCI, final String elib, final String auto)
	{
		super();
		this.delegatName = name;
		this.cnp = cnp;
		this.serieCI = serieCI;
		this.elib = elib;
		this.auto = auto;
	}
	
	public Delegat()
	{
	}

	public String getName()
	{
		return delegatName;
	}

	public void setName(final String name)
	{
		this.delegatName = name;
	}

	public String getCnp()
	{
		return cnp;
	}

	public void setCnp(final String cnp)
	{
		this.cnp = cnp;
	}

	public String getSerieCI()
	{
		return serieCI;
	}

	public void setSerieCI(final String serieCI)
	{
		this.serieCI = serieCI;
	}

	public String getElib()
	{
		return elib;
	}

	public void setElib(final String elib)
	{
		this.elib = elib;
	}

	public String getAuto()
	{
		return auto;
	}

	public void setAuto(final String auto)
	{
		this.auto = auto;
	}
	
	@Override
	public String toString()
	{
		return "Delegat [name=" + delegatName + ", cnp=" + cnp + ", serieCI=" + serieCI + ", elib=" + elib + ", auto=" + auto
				+ "]";
	}
}
