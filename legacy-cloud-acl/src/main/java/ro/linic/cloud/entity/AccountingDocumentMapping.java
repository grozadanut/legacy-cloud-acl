package ro.linic.cloud.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import ro.linic.cloud.embedable.AccountingDocumentMappingId;

@Entity
@Table
public class AccountingDocumentMapping implements Serializable
{
	private static final long serialVersionUID = 1L;

	@EmbeddedId
    private AccountingDocumentMappingId id;
 
    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("paysId")
    @JoinColumn(name = "PAYS_ID")
    private AccountingDocument pays;
 
    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("paidId")
    @JoinColumn(name = "PAID_ID")
    private AccountingDocument paid;
 
    @Column(precision=16, scale=2, nullable = false)
	private BigDecimal total;
 
    public AccountingDocumentMapping() 
    {
    }
    
	public AccountingDocumentMapping(final AccountingDocumentMappingId id, final AccountingDocument pays, final AccountingDocument paid,
			final BigDecimal total)
	{
		super();
		this.id = id;
		this.pays = pays;
		this.paid = paid;
		this.total = total;
	}

	public AccountingDocumentMappingId getId()
	{
		return id;
	}

	public void setId(final AccountingDocumentMappingId id)
	{
		this.id = id;
	}

	public AccountingDocument getPays()
	{
		return pays;
	}

	public AccountingDocumentMapping setPays(final AccountingDocument pays)
	{
		this.pays = pays;
		return this;
	}

	public AccountingDocument getPaid()
	{
		return paid;
	}

	public void setPaid(final AccountingDocument paid)
	{
		this.paid = paid;
	}

	public BigDecimal getTotal()
	{
		return total;
	}

	public AccountingDocumentMapping setTotal(final BigDecimal total)
	{
		this.total = total;
		return this;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((total == null) ? 0 : total.hashCode());
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
		final AccountingDocumentMapping other = (AccountingDocumentMapping) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (total == null) {
			if (other.total != null)
				return false;
		} else if (!total.equals(other.total))
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "AccountingDocumentMapping [id=" + id + ", total=" + total + "]";
	}
}
