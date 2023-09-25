package ro.linic.cloud.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Partner implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public static final String ID_FIELD = "id";
	public static final String NAME_FIELD = "name";
	public static final String PHONE_FIELD = "phone";
	public static final String EMAIL_FIELD = "email";
	public static final String COD_FISCAL_FIELD = "codFiscal";
	public static final String DOCUMENTS_FIELD = "documents";
	public static final String ACTIV_FIELD = "activ";
	public static final String FIDELITY_CARD_FIELD = "fidelityCard";
	public static final String INACTIV_FIELD = "inactiv";
	public static final String TVA_INCASARE_FIELD = "tvaLaIncasare";
	public static final String TVA_INCASARE_FROM_FIELD = "dataInceputTvaInc";
	public static final String TVA_INCASARE_TO_FIELD = "dataSfarsitTvaInc";
	public static final String SPLIT_TVA_FIELD = "splitTva";
	public static final String DATA_INCEPUT_SPLIT_TVA_FIELD = "dataInceputSplitTVA";
	public static final String DATA_ANULARE_SPLIT_TVA_FIELD = "dataAnulareSplitTVA";
	public static final String COMPANY_FIELD = "company";
	public static final String NOTIFY_APPOINTMENT_FIELD = "notifyAppointment";
	
	/**
	 *  Usually when the partner is unknown, this partner is used instead
	 */
	public static final String OP_INTERNA = "OP INTERNA";
	public static final String INTERNE = "INTERNE";
	
	/**
	 *  Used mostly for transfers between gestiuni.
	 */
	public static final String L1_L2 = "L1<->L2";
	
	/**
	 * Used when closing Bonuri de casa
	 */
	public static final String STANDARD_PARTNER_NAME = "STANDARD";
	public static final String CARD_NAME = "CARD INCASARE";
	public static final String CLIENT_NAME = "CLIENT";
	public static final String EMPLOYEE_PREFIX = "A. ";
	
	public static final String STAT_PLATA = "STAT DE PLATA";
	// not sure why we have this as a partner
	public static final String MARFA = "MARFA";
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@ManyToOne
	@JoinColumn(name = "company_id")
	private Company company;
	
	@Column(nullable = false)
	private String name;
	private String phone;
	private String email;
	
	@Column(columnDefinition = "text")
    private String indicatii;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "partner")
	private List<Document> documents = new ArrayList<>();
	
	private Boolean inactiv; // inactiv la ANAF
	private String banca;
	private String iban;
	
	@Column(columnDefinition="BOOLEAN DEFAULT true")
	private boolean activ = true; // daca este vizibil pe UI sau nu
	
	public Partner()
	{
	}
	
	public Company getCompany()
	{
		return company;
	}
	
	public void setCompany(final Company company)
	{
		this.company = company;
	}

	public LocalDate firstScadenta()
	{
		return getDocuments().stream()
				.map(Document::getScadenta)
				.filter(Objects::nonNull)
				.sorted()
				.findFirst()
				.orElse(null);
	}
	
	
	public String getName()
	{
		return name;
	}

	public Partner setName(final String name)
	{
		this.name = name;
		return this;
	}

	public String getPhone()
	{
		return phone;
	}
	
	public void setPhone(final String phone)
	{
		this.phone = phone;
	}
	
	public String getEmail()
	{
		return email;
	}
	
	public void setEmail(final String email)
	{
		this.email = email;
	}

	public String getIndicatii()
	{
		return indicatii;
	}
	
	public void setIndicatii(final String indicatii)
	{
		this.indicatii = indicatii;
	}
	
	public Long getId()
	{
		return id;
	}
	
	public boolean isActiv()
	{
		return activ;
	}
	
	public void setActiv(final boolean activ)
	{
		this.activ = activ;
	}

	public List<Document> getDocuments()
	{
		return documents;
	}
	
	public Stream<Document> getDocumentsStream()
	{
		return documents.stream();
	}

	public void setDocuments(final List<Document> documents)
	{
		this.documents = documents;
	}
	
	public Boolean getInactiv()
	{
		return inactiv;
	}
	
	public boolean isInactivNullCheck()
	{
		return inactiv != null && inactiv;
	}

	public void setInactiv(final Boolean inactiv)
	{
		this.inactiv = inactiv;
	}
	
	public String getBanca()
	{
		return banca;
	}
	
	public void setBanca(final String banca)
	{
		this.banca = banca;
	}

	public String getIban()
	{
		return iban;
	}

	public void setIban(final String iban)
	{
		this.iban = iban;
	}
	
	@Override
	public String toString()
	{
		final StringBuilder builder = new StringBuilder();
		builder.append("Partner [id=");
		builder.append(id);
		builder.append(", name=");
		builder.append(name);
		builder.append(", phone=");
		builder.append(phone);
		builder.append(", email=");
		builder.append(email);
		builder.append(", banca=");
		builder.append(banca);
		builder.append(", iban=");
		builder.append(iban);
		builder.append(", activ=");
		builder.append(activ);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((phone == null) ? 0 : phone.hashCode());
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
		final Partner other = (Partner) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (phone == null) {
			if (other.phone != null)
				return false;
		} else if (!phone.equals(other.phone))
			return false;
		return true;
	}
}
