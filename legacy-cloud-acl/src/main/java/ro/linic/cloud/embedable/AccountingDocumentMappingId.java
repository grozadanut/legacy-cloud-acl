package ro.linic.cloud.embedable;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class AccountingDocumentMappingId implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Column(name = "PAYS_ID")
    private Long paysId;
 
    @Column(name = "PAID_ID")
    private Long paidId;
    
    public AccountingDocumentMappingId() {
	}

	public AccountingDocumentMappingId(final Long paysId, final Long paidId) {
		this.paysId = paysId;
		this.paidId = paidId;
	}

	public Long getPaysId() {
		return paysId;
	}

	public Long getPaidId() {
		return paidId;
	}

	public void setPaysId(final Long paysId) {
		this.paysId = paysId;
	}

	public void setPaidId(final Long paidId) {
		this.paidId = paidId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((paidId == null) ? 0 : paidId.hashCode());
		result = prime * result + ((paysId == null) ? 0 : paysId.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final AccountingDocumentMappingId other = (AccountingDocumentMappingId) obj;
		if (paidId == null) {
			if (other.paidId != null)
				return false;
		} else if (!paidId.equals(other.paidId))
			return false;
		if (paysId == null) {
			if (other.paysId != null)
				return false;
		} else if (!paysId.equals(other.paysId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "AccountingDocumentMappingId [paysId=" + paysId + ", paidId=" + paidId + "]";
	}
}
