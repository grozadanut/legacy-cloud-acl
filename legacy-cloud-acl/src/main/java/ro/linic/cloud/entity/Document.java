package ro.linic.cloud.entity;

import static ro.linic.cloud.util.StringUtils.globalIsMatch;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import ro.linic.cloud.util.StringUtils.TextFilterMethod;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Document implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public static final String OPERATOR_FIELD = "operator";
	public static final String PARTNER_FIELD = "partner";
	public static final String DATA_DOC_FIELD = "dataDoc";
	public static final String TIP_DOC_FIELD = "tipDoc";
	public static final String TOTAL_FIELD = "total";
	public static final String TVA_FIELD = "totalTva";
	public static final String ID_FIELD = "id";
	public static final String NAME_FIELD = "name";
	public static final String COMPANY_FIELD = "company";
	public static final String SCADENTA_FIELD = "scadenta";

	public enum TipDoc
	{
		CUMPARARE("C"), VANZARE("V"), PLATA("P"), INCASARE("I"), VOUCHER("Vo");

		private String shortName;
		
		private TipDoc(final String shortName)
		{
			this.shortName = shortName;
		}
		
		public String shortName()
		{
			return shortName;
		}

		public static TipDoc parse(final String value)
		{
			if (globalIsMatch(value, TipDoc.CUMPARARE.name(), TextFilterMethod.EQUALS)
					|| globalIsMatch(value, "C", TextFilterMethod.EQUALS))
				return TipDoc.CUMPARARE;
			else if (globalIsMatch(value, TipDoc.VANZARE.name(), TextFilterMethod.EQUALS)
					|| globalIsMatch(value, "V", TextFilterMethod.EQUALS))
				return TipDoc.VANZARE;
			else if (globalIsMatch(value, TipDoc.PLATA.name(), TextFilterMethod.EQUALS)
					|| globalIsMatch(value, "P", TextFilterMethod.EQUALS))
				return TipDoc.PLATA;
			else if (globalIsMatch(value, TipDoc.INCASARE.name(), TextFilterMethod.EQUALS)
					|| globalIsMatch(value, "I", TextFilterMethod.EQUALS))
				return TipDoc.INCASARE;
			else if (globalIsMatch(value, TipDoc.VOUCHER.name(), TextFilterMethod.EQUALS)
					|| globalIsMatch(value, "Vo", TextFilterMethod.EQUALS))
				return TipDoc.VOUCHER;

			throw new IllegalArgumentException("Doc type not permitted: " + value);
		}
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "company_id")
	private Company company;

	@ManyToOne
	@JoinColumn(name = "partner_id", nullable = true)
	private Partner partner;

	/**
	 * Document description; mostly used in the Urmarire Parteneri region(CVF 14, VANZARI...)
	 */
	@Column(columnDefinition = "text")
	private String name;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, name = "tipdoc")
	private TipDoc tipDoc;

	@Column(precision = 16, scale = 2, nullable = true)
	private BigDecimal total;
	
	@Column(precision = 14, scale = 2, nullable = true, name ="totaltva")
	private BigDecimal totalTva;

	private LocalDate scadenta;
	@Column(name ="datadoc")
	private LocalDateTime dataDoc = LocalDateTime.now();
	@ManyToOne
	@JoinColumn(name = "operator_id", nullable = true)
	private User operator;

	public Document()
	{
	}

	public Document(final Company company, final Long id, final Partner partner, final String name, final TipDoc tipDoc,
			final BigDecimal total, final BigDecimal totalTva, final LocalDate scadenta, final LocalDateTime dataDoc,
			final User operator)
	{
		super();
		this.company = company;
		this.id = id;
		this.partner = partner;
		this.name = name;
		this.tipDoc = tipDoc;
		this.total = total;
		this.totalTva = totalTva;
		this.scadenta = scadenta;
		this.dataDoc = dataDoc;
		this.operator = operator;
	}
	
	public Company getCompany()
	{
		return company;
	}
	
	public void setCompany(final Company company)
	{
		this.company = company;
	}

	public Partner getPartner()
	{
		return partner;
	}

	public Document setPartner(final Partner partner)
	{
		this.partner = partner;
		return this;
	}

	public TipDoc getTipDoc()
	{
		return tipDoc;
	}

	public void setTipDoc(final TipDoc tipDoc)
	{
		this.tipDoc = tipDoc;
	}

	public BigDecimal getTotal()
	{
		return total;
	}
	
	public BigDecimal getTotalRpz()
	{
		return BigDecimal.ZERO;
	}
	
	public BigDecimal getTotalRpzTva()
	{
		return BigDecimal.ZERO;
	}
	
	public BigDecimal getTotalTva()
	{
		return totalTva;
	}
	
	public void setTotalTva(final BigDecimal totalTva)
	{
		this.totalTva = totalTva;
	}

	public void setTotal(final BigDecimal total)
	{
		this.total = total;
	}

	public LocalDate getScadenta()
	{
		return scadenta;
	}

	public void setScadenta(final LocalDate scadenta)
	{
		this.scadenta = scadenta;
	}

	public LocalDateTime getDataDoc()
	{
		return dataDoc;
	}
	
	public LocalDate getDataDoc_toLocalDate()
	{
		return dataDoc.toLocalDate();
	}

	public void setDataDoc(final LocalDateTime dataDoc)
	{
		this.dataDoc = dataDoc;
	}

	public Long getId()
	{
		return id;
	}

	public User getOperator()
	{
		return operator;
	}

	public void setOperator(final User operator)
	{
		this.operator = operator;
	}

	public String getName()
	{
		return name;
	}

	public void setName(final String name)
	{
		this.name = name;
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dataDoc == null) ? 0 : dataDoc.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((partner == null) ? 0 : partner.hashCode());
		result = prime * result + ((tipDoc == null) ? 0 : tipDoc.hashCode());
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
		final Document other = (Document) obj;
		if (dataDoc == null)
		{
			if (other.dataDoc != null)
				return false;
		} else if (!dataDoc.equals(other.dataDoc))
			return false;
		if (id == null)
		{
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null)
		{
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (partner == null)
		{
			if (other.partner != null)
				return false;
		} else if (!partner.equals(other.partner))
			return false;
		if (tipDoc != other.tipDoc)
			return false;
		if (total == null)
		{
			if (other.total != null)
				return false;
		} else if (!total.equals(other.total))
			return false;
		return true;
	}
}
