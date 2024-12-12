package ro.linic.cloud.embedable;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Address implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Column(columnDefinition = "text")
	private String oras;
	private String judet;
	private String strada; 
    private String nr;
    private String bl;
    private String sc;
    private String ap;
    private String country;
    
    public Address(final String oras, final String judet, final String strada, final String nr, final String bl, final String sc,
    		final String ap, final String country)
	{
		this.oras = oras;
		this.judet = judet;
		this.strada = strada;
		this.nr = nr;
		this.bl = bl;
		this.sc = sc;
		this.ap = ap;
		this.country = country;
	}

	public Address()
	{
	}
    
	public String getOras()
	{
		return oras;
	}

	public Address setOras(final String oras)
	{
		this.oras = oras;
		return this;
	}

	public String getJudet()
	{
		return judet;
	}

	public void setJudet(final String judet)
	{
		this.judet = judet;
	}

	public String getStrada()
	{
		return strada;
	}

	public void setStrada(final String strada)
	{
		this.strada = strada;
	}

	public String getNr()
	{
		return nr;
	}

	public void setNr(final String nr)
	{
		this.nr = nr;
	}

	public String getBl()
	{
		return bl;
	}

	public void setBl(final String bl)
	{
		this.bl = bl;
	}

	public String getSc()
	{
		return sc;
	}

	public void setSc(final String sc)
	{
		this.sc = sc;
	}

	public String getAp()
	{
		return ap;
	}

	public void setAp(final String ap)
	{
		this.ap = ap;
	}
	
	public String getCountry()
	{
		return country;
	}
	
	public void setCountry(final String country)
	{
		this.country = country;
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ap == null) ? 0 : ap.hashCode());
		result = prime * result + ((bl == null) ? 0 : bl.hashCode());
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + ((judet == null) ? 0 : judet.hashCode());
		result = prime * result + ((nr == null) ? 0 : nr.hashCode());
		result = prime * result + ((oras == null) ? 0 : oras.hashCode());
		result = prime * result + ((sc == null) ? 0 : sc.hashCode());
		result = prime * result + ((strada == null) ? 0 : strada.hashCode());
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
		final Address other = (Address) obj;
		if (ap == null)
		{
			if (other.ap != null)
				return false;
		} else if (!ap.equals(other.ap))
			return false;
		if (bl == null)
		{
			if (other.bl != null)
				return false;
		} else if (!bl.equals(other.bl))
			return false;
		if (country == null)
		{
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (judet == null)
		{
			if (other.judet != null)
				return false;
		} else if (!judet.equals(other.judet))
			return false;
		if (nr == null)
		{
			if (other.nr != null)
				return false;
		} else if (!nr.equals(other.nr))
			return false;
		if (oras == null)
		{
			if (other.oras != null)
				return false;
		} else if (!oras.equals(other.oras))
			return false;
		if (sc == null)
		{
			if (other.sc != null)
				return false;
		} else if (!sc.equals(other.sc))
			return false;
		if (strada == null)
		{
			if (other.strada != null)
				return false;
		} else if (!strada.equals(other.strada))
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "Address [oras=" + oras + ", judet=" + judet + ", strada=" + strada + ", nr=" + nr + ", bl=" + bl
				+ ", sc=" + sc + ", ap=" + ap + ", country=" + country + "]";
	}
}
