package ro.linic.cloud.entity;

import static ro.linic.cloud.util.NumberUtils.add;
import static ro.linic.cloud.util.NumberUtils.divide;
import static ro.linic.cloud.util.NumberUtils.equal;
import static ro.linic.cloud.util.NumberUtils.findClosest;
import static ro.linic.cloud.util.NumberUtils.subtract;
import static ro.linic.cloud.util.PresentationUtils.safeString;
import static ro.linic.cloud.util.StringUtils.isEmpty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Operatiune implements Serializable
{
	private static final long serialVersionUID = 1L;

	public static final String ID_FIELD = "id";
	public static final String GESTIUNE_FIELD = "gestiune";
	public static final String TIP_OP_FIELD = "tipOp";
	public static final String BARCODE_FIELD = "barcode";
	public static final String NAME_FIELD = "name";
	public static final String DATA_OP_FIELD = "dataOp";
	public static final String RPZ_FIELD = "rpz";
	public static final String CATEGORIE_FIELD = "categorie";
	public static final String UM_FIELD = "uom";
	public static final String CANTITATE_FIELD = "cantitate";
	public static final String PUA_fTVA_FIELD = "pretUnitarAchizitieFaraTVA";
	public static final String VA_fTVA_FIELD = "valoareAchizitieFaraTVA";
	public static final String VA_TVA_FIELD = "valoareAchizitieTVA";
	public static final String PRET_UNITAR_FIELD = "pretVanzareUnitarCuTVA";
	public static final String VV_fTVA_FIELD = "valoareVanzareFaraTVA";
	public static final String VV_TVA_FIELD = "valoareVanzareTVA";
	public static final String OPERATOR_FIELD = "operator";
	public static final String ACC_DOC_FIELD = "accDoc";
	public static final String VERIFICAT_FIELD = "verificat";
	public static final String SHOULD_VERIFY_FIELD = "shouldVerify";
	public static final String COMPANY_FIELD = "company";
	public static final String COTA_TVA_FIELD = "cotaTva";
	
	public static List<BigDecimal> VAT_RATES_RO = List.of(new BigDecimal("0.19"), new BigDecimal("0.09"), new BigDecimal("0.05"), new BigDecimal("0"));

	public enum TipOp
	{
		INTRARE, IESIRE;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "gestiune_id", nullable = false)
	private Gestiune gestiune;
	
	@ManyToOne
	@JoinColumn(name = "company_id")
	private Company company;
	
	@ManyToOne
	@JoinColumn(name = "accounting_doc_id", nullable = true)
	private AccountingDocument accDoc;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, name = "tipop")
	private TipOp tipOp;

	@Column(nullable = false)
	private String barcode; // taken from product
	private String name; // taken from product
	private String uom; // taken from product
	/**
	 * MARFA, AMBALAJE, PRESTARI SERVICII...(taken from product)
	 */
	private String categorie;
	private String department; // taken from product

	/**
	 * TRUE daca face parte din raportul zilnic de gestiune
	 */
	@Column(columnDefinition="BOOLEAN DEFAULT false")
	private boolean rpz = false;

	@Column(precision = 12, scale = 4, nullable = false)
	private BigDecimal cantitate;

	@Column(precision = 16, scale = 8, nullable = true, name = "pretunitarachizitiefaratva")
	private BigDecimal pretUnitarAchizitieFaraTVA;

	@Column(precision = 12, scale = 2, nullable = true, name = "valoareachizitiefaratva")
	private BigDecimal valoareAchizitieFaraTVA;

	@Column(precision = 10, scale = 2, nullable = true, name = "valoareachizitietva")
	private BigDecimal valoareAchizitieTVA;

	@Column(precision = 10, scale = 2, nullable = false, name = "pretvanzareunitarcutva")
	private BigDecimal pretVanzareUnitarCuTVA;

	@Column(precision = 12, scale = 2, nullable = false, name = "valoarevanzarefaratva")
	private BigDecimal valoareVanzareFaraTVA;

	@Column(precision = 10, scale = 2, nullable = false, name = "valoarevanzaretva")
	private BigDecimal valoareVanzareTVA;

	@ManyToOne
	@JoinColumn(name = "operator_id", nullable = true)
	private User operator;
	@Column(name = "dataop")
	private LocalDateTime dataOp = LocalDateTime.now();
	private String idx; // NOT used after migration is complete
	
	@Column(columnDefinition="BOOLEAN DEFAULT false", name = "shouldverify")
	private boolean shouldVerify = false;
	
	/**
	 * Updateaza campurile care se calculeaza pe baza cantitatii si a pretului unitar:<br>
	 * setValoareAchizitieFaraTVA<br>
	 * setValoareAchizitieTVA<br>
	 * setValoareVanzareFaraTVA<br>
	 * setValoareVanzareTVA<br>
	 */
	public static void updateAmounts(final Operatiune op, final BigDecimal tvaPercent, final BigDecimal tvaExtractDivisor)
	{
		if (op.getPretUnitarAchizitieFaraTVA() != null)
		{
			op.setValoareAchizitieFaraTVA(op.getPretUnitarAchizitieFaraTVA().multiply(op.getCantitate()).setScale(2, RoundingMode.HALF_EVEN));
			op.setValoareAchizitieTVA(op.getValoareAchizitieFaraTVA().multiply(tvaPercent).setScale(2, RoundingMode.HALF_EVEN));
		}
		if (op.getPretVanzareUnitarCuTVA() != null)
		{
			op.setValoareVanzareFaraTVA(op.getPretVanzareUnitarCuTVA().multiply(op.getCantitate()).divide(tvaExtractDivisor, 2, RoundingMode.HALF_EVEN));
			op.setValoareVanzareTVA(op.getPretVanzareUnitarCuTVA().multiply(op.getCantitate()).setScale(2, RoundingMode.HALF_EVEN).subtract(op.getValoareVanzareFaraTVA()));
		}
	}
	
	public static BigDecimal tvaExtractDivisor(final BigDecimal tvaPercent)
	{
		return tvaPercent.add(BigDecimal.ONE);
	}
	
	public Operatiune()
	{
	}
	
	public Operatiune(final Company company, final Long id, final Gestiune gestiune, final AccountingDocument accDoc, final TipOp tipOp, final String barcode, final String name,
			final String uom, final String categorie, final boolean rpz, final BigDecimal cantitate, final BigDecimal pretUnitarAchizitieFaraTVA,
			final BigDecimal valoareAchizitieFaraTVA, final BigDecimal valoareAchizitieTVA, final BigDecimal pretVanzareUnitarCuTVA,
			final BigDecimal valoareVanzareFaraTVA, final BigDecimal valoareVanzareTVA, final User operator, final LocalDateTime dataOp,
			final String idx, final boolean shouldVerify)
	{
		super();
		this.company = company;
		this.id = id;
		this.gestiune = gestiune;
		this.accDoc = accDoc;
		this.tipOp = tipOp;
		this.barcode = barcode;
		this.name = name;
		this.uom = uom;
		this.categorie = categorie;
		this.rpz = rpz;
		this.cantitate = cantitate;
		this.pretUnitarAchizitieFaraTVA = pretUnitarAchizitieFaraTVA;
		this.valoareAchizitieFaraTVA = valoareAchizitieFaraTVA;
		this.valoareAchizitieTVA = valoareAchizitieTVA;
		this.pretVanzareUnitarCuTVA = pretVanzareUnitarCuTVA;
		this.valoareVanzareFaraTVA = valoareVanzareFaraTVA;
		this.valoareVanzareTVA = valoareVanzareTVA;
		this.operator = operator;
		this.dataOp = dataOp;
		this.idx = idx;
		this.shouldVerify = shouldVerify;
	}

	public Company getCompany()
	{
		return company;
	}
	
	public void setCompany(final Company company)
	{
		this.company = company;
	}

	public Gestiune getGestiune()
	{
		return gestiune;
	}

	public void setGestiune(final Gestiune gestiune)
	{
		this.gestiune = gestiune;
	}
	
	public AccountingDocument getAccDoc()
	{
		return accDoc;
	}
	
	public Operatiune setAccDoc(final AccountingDocument accDoc)
	{
		this.accDoc = accDoc;
		return this;
	}

	public Long getId()
	{
		return id;
	}

	public void setId(final Long id)
	{
		this.id = id;
	}

	public TipOp getTipOp()
	{
		return tipOp;
	}

	public void setTipOp(final TipOp tipOp)
	{
		this.tipOp = tipOp;
	}

	public String getBarcode()
	{
		return barcode;
	}

	public void setBarcode(final String barcode)
	{
		this.barcode = barcode;
	}

	public String getName()
	{
		return name;
	}

	public void setName(final String name)
	{
		this.name = name;
	}

	public String getUom()
	{
		return uom;
	}
	
	public void setUom(final String uom)
	{
		this.uom = uom;
	}
	
	public String getUomInternational()
	{
		if (isEmpty(uom))
			return "C62"; //C62=unitate
		
		if (uom.trim().equalsIgnoreCase("BUC"))
			return "C62"; //C62=unitate
		if (uom.trim().equalsIgnoreCase("KG"))
			return "KGM"; //KGM=kilogram
		if (uom.trim().equalsIgnoreCase("ML") || uom.trim().equalsIgnoreCase("M"))
			return "LM"; //LM=metru liniar
		if (uom.trim().equalsIgnoreCase("MP") || uom.trim().equalsIgnoreCase("M2"))
			return "MTK"; //MTK=metru patrat
		if (uom.trim().equalsIgnoreCase("KM"))
			return "KMT"; //KMT=kilometru
		if (uom.trim().equalsIgnoreCase("PER") || uom.trim().equalsIgnoreCase("SET"))
			return "SET"; //SET=set
		if (uom.trim().equalsIgnoreCase("PLACA"))
			return "XPG"; //XPG=placa
		
		return "C62"; //C62=unitate
	}
	
	public boolean isShouldVerify()
	{
		return shouldVerify;
	}
	
	public void setShouldVerify(final boolean shouldVerify)
	{
		this.shouldVerify = shouldVerify;
	}
	
	public BigDecimal getCantitate()
	{
		return cantitate;
	}

	public void setCantitate(final BigDecimal cantitate)
	{
		this.cantitate = cantitate;
	}

	public BigDecimal getPretUnitarAchizitieFaraTVA()
	{
		return pretUnitarAchizitieFaraTVA;
	}

	public void setPretUnitarAchizitieFaraTVA(final BigDecimal pretUnitarAchizitieFaraTVA)
	{
		this.pretUnitarAchizitieFaraTVA = pretUnitarAchizitieFaraTVA;
	}

	public BigDecimal getValoareAchizitieFaraTVA()
	{
		return valoareAchizitieFaraTVA;
	}

	public void setValoareAchizitieFaraTVA(final BigDecimal valoareAchizitieFaraTVA)
	{
		this.valoareAchizitieFaraTVA = valoareAchizitieFaraTVA;
	}

	public BigDecimal getValoareAchizitieTVA()
	{
		return valoareAchizitieTVA;
	}

	public void setValoareAchizitieTVA(final BigDecimal valoareAchizitieTVA)
	{
		this.valoareAchizitieTVA = valoareAchizitieTVA;
	}
	
	public BigDecimal getPretVanzareUnitarFaraTVA()
	{
		final BigDecimal tvaPercent = getVanzareTvaPercentCalculated();
    	final BigDecimal tvaExtractDivisor = add(tvaPercent, BigDecimal.ONE);
    	final BigDecimal tvaUnitar = AccountingDocument.extractTvaAmount(getPretVanzareUnitarCuTVA(), tvaExtractDivisor);
    	return subtract(getPretVanzareUnitarCuTVA(), tvaUnitar);
	}

	public BigDecimal getPretVanzareUnitarCuTVA()
	{
		return pretVanzareUnitarCuTVA;
	}

	public void setPretVanzareUnitarCuTVA(final BigDecimal pretVanzareUnitarCuTVA)
	{
		this.pretVanzareUnitarCuTVA = pretVanzareUnitarCuTVA;
	}

	public BigDecimal getValoareVanzareFaraTVA()
	{
		return valoareVanzareFaraTVA;
	}

	public void setValoareVanzareFaraTVA(final BigDecimal valoareVanzareFaraTVA)
	{
		this.valoareVanzareFaraTVA = valoareVanzareFaraTVA;
	}

	public BigDecimal getValoareVanzareTVA()
	{
		return valoareVanzareTVA;
	}

	public void setValoareVanzareTVA(final BigDecimal valoareVanzareTVA)
	{
		this.valoareVanzareTVA = valoareVanzareTVA;
	}

	public String getCategorie()
	{
		return categorie;
	}

	public void setCategorie(final String categorie)
	{
		this.categorie = categorie;
	}
	
	public String getDepartment()
	{
		return department;
	}
	
	public Optional<String> dept()
	{
		return Optional.ofNullable(department);
	}
	
	public void setDepartment(final String department)
	{
		this.department = department;
	}
	
	public User getOperator()
	{
		return operator;
	}

	public void setOperator(final User operator)
	{
		this.operator = operator;
	}

	public LocalDateTime getDataOp()
	{
		return dataOp;
	}

	public void setDataOp(final LocalDateTime dataOp)
	{
		this.dataOp = dataOp;
	}

	public boolean isRpz()
	{
		return rpz;
	}

	public void setRpz(final boolean rpz)
	{
		this.rpz = rpz;
	}

	public String getIdx()
	{
		return idx;
	}

	public void setIdx(final String idx)
	{
		this.idx = idx;
	}
	
	public BigDecimal getTotal()
	{
		switch (getTipOp())
		{
		case INTRARE:
			return add(valoareAchizitieFaraTVA, valoareAchizitieTVA);

		case IESIRE:
			return add(valoareVanzareFaraTVA, valoareVanzareTVA);
			
		default:
			throw new UnsupportedOperationException("TipOp not supported "+tipOp);
		}
	}
	
	/**
	 * If the fields are already completed, calculates the VAT amount based on sale value.
	 * IMPORTANT: as the VAT is calculated based on sale value, this will NOT work for ops
	 * that only have acquisition value(eg.: materie prima)
	 */
	public BigDecimal getVanzareTvaPercentCalculated()
	{
		if (equal(getValoareVanzareFaraTVA(), BigDecimal.ZERO))
			return BigDecimal.ZERO;
		return findClosest(VAT_RATES_RO, divide(getValoareVanzareTVA(), getValoareVanzareFaraTVA(), 2, RoundingMode.HALF_EVEN).abs());
	}
	
	public BigDecimal getAchizitieTvaPercentCalculated()
	{
		if (equal(getValoareAchizitieFaraTVA(), BigDecimal.ZERO))
			return BigDecimal.ZERO;
		return findClosest(VAT_RATES_RO, divide(getValoareAchizitieTVA(), getValoareAchizitieFaraTVA(), 2, RoundingMode.HALF_EVEN).abs());
	}
	
	@Override
	public String toString()
	{
		final StringBuilder builder = new StringBuilder();
		builder.append("Operatiune [id=");
		builder.append(id);
		builder.append(", accDoc=");
		builder.append(safeString(accDoc, AccountingDocument::getId, String::valueOf));
		builder.append(", gestiune=");
		builder.append(safeString(gestiune, Gestiune::getImportName));
		builder.append(", tipOp=");
		builder.append(tipOp);
		builder.append(", barcode=");
		builder.append(barcode);
		builder.append(", name=");
		builder.append(name);
		builder.append(", uom=");
		builder.append(uom);
		builder.append(", categorie=");
		builder.append(categorie);
		builder.append(", rpz=");
		builder.append(rpz);
		builder.append(", cantitate=");
		builder.append(cantitate);
		builder.append(", pretUnitarAchizitieFaraTVA=");
		builder.append(pretUnitarAchizitieFaraTVA);
		builder.append(", valoareAchizitieFaraTVA=");
		builder.append(valoareAchizitieFaraTVA);
		builder.append(", valoareAchizitieTVA=");
		builder.append(valoareAchizitieTVA);
		builder.append(", pretVanzareUnitarCuTVA=");
		builder.append(pretVanzareUnitarCuTVA);
		builder.append(", valoareVanzareFaraTVA=");
		builder.append(valoareVanzareFaraTVA);
		builder.append(", valoareVanzareTVA=");
		builder.append(valoareVanzareTVA);
		builder.append(", shouldVerify=");
		builder.append(shouldVerify);
		builder.append(", dataOp=");
		builder.append(dataOp);
		builder.append("]");
		return builder.toString();
	}
	
	public Operatiune duplicate()
	{
		final Operatiune duplicate = new Operatiune();
		duplicate.setCompany(getCompany());
		duplicate.setBarcode(getBarcode());
		duplicate.setCantitate(getCantitate());
		duplicate.setCategorie(getCategorie());
		duplicate.setDepartment(getDepartment());
		duplicate.setGestiune(getGestiune());
		duplicate.setName(getName());
		duplicate.setPretUnitarAchizitieFaraTVA(getPretUnitarAchizitieFaraTVA());
		duplicate.setPretVanzareUnitarCuTVA(getPretVanzareUnitarCuTVA());
		duplicate.setRpz(isRpz());
		duplicate.setTipOp(getTipOp());
		duplicate.setUom(getUom());
		duplicate.setValoareAchizitieFaraTVA(getValoareAchizitieFaraTVA());
		duplicate.setValoareAchizitieTVA(getValoareAchizitieTVA());
		duplicate.setValoareVanzareFaraTVA(getValoareVanzareFaraTVA());
		duplicate.setValoareVanzareTVA(getValoareVanzareTVA());
		return duplicate;
	}
}
