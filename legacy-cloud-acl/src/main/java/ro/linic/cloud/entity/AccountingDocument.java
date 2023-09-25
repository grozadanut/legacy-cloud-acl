package ro.linic.cloud.entity;

import static ro.linic.cloud.util.NumberUtils.add;
import static ro.linic.cloud.util.NumberUtils.multiply;
import static ro.linic.cloud.util.PresentationUtils.safeString;
import static ro.linic.cloud.util.StringUtils.globalIsMatch;
import static ro.linic.cloud.util.StringUtils.isEmpty;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import ro.linic.cloud.util.StringUtils.TextFilterMethod;

@Entity
@Table(name = "accountingdocument")
public class AccountingDocument extends Document
{
	private static final long serialVersionUID = 1L;
	
	public static final Integer DEFAULT_TERMEN_PLATA = 0;
	public static final BigDecimal POINTS_PER_HOUR = new BigDecimal("100");
	public static final BigDecimal RON_PER_HOUR = new BigDecimal("20");
	public static final BigDecimal RON_PER_POINT = RON_PER_HOUR.divide(POINTS_PER_HOUR, 12, RoundingMode.HALF_EVEN);
	public static final BigDecimal POINT_PER_RON = POINTS_PER_HOUR.divide(RON_PER_HOUR, 12, RoundingMode.HALF_EVEN);
	
	public static final String NR_DOC_FIELD = "nrDoc";
	public static final String NR_RECEPTIE_FIELD = "nrReceptie";
	public static final String DOC_FIELD = "doc";
	public static final String GESTIUNE_FIELD = "gestiune";
	public static final String OPERATIONS_FIELD = "operatiuni";
	public static final String DATA_RECEPTIE_FIELD = "dataReceptie";
	public static final String RPZ_FIELD = "rpz";
	public static final String REG_CASA_FIELD = "regCasa";
	public static final String IN_CONTABILITATE_FIELD = "inContabilitate";
	public static final String EDITABLE_FIELD = "editable";
	public static final String PAID_BY_FIELD = "paidBy";
	public static final String PAID_DOCS_FIELD = "paidDocs";
	public static final String MASINA_FIELD = "masina";
	public static final String TRANSPORT_DATE_FIELD = "transportDateTime";
	public static final String SHOULD_TRANSPORT_FIELD = "shouldTransport";
	public static final String POINTS_FIELD = "points";
	public static final String CONT_BANCAR_FIELD = "contBancar";
	public static final String PHONE_FIELD = "phone";
	public static final String ADDRESS_FIELD = "address";
	public static final String INDICATII_FIELD = "indicatii";
	public static final String TRANSPORT_TYPE_FIELD = "transportType";
	public static final String PAY_AT_DRIVER_FIELD = "payAtDriver";
	
	public static final String FACTURA_NAME = "FACTURA";
	public static final String PROFORMA_NAME = "PROFORMA";
	public static final String AVIZ_NAME = "AVIZ";
	public static final String BON_CONSUM_NAME = "BON CONSUM";
	public static final String TRANSFER_DOC_NAME = "BON TRANSFER";
	public static final String BON_CASA_NAME = "BON DE CASA";
	public static final String PROCES_VERBAL_NAME = "PROCES VERBAL";
	public static final String RAPORT_PRODUCTIE_NAME = "RAPORT DE PRODUCTIE";
	public static final String LISTA_INVENTAR_NAME = "LISTA INVENTAR";
	public static final String BORDEROU_ACHIZITIE_NAME = "BORDEROU ACHIZITIE";
	public static final String PROCES_SCHIMBARE_PRET_NAME = "PROCES SCHIMBARE PRET";
	public static final String CASARE_NAME = "CASARE";
	public static final String AMORTIZARE_NAME = "AMORTIZARE";
	public static final String NOTIFICARE_NAME = "NOTIFICARE";
	public static final String CONTRACT_NAME = "CONTRACT";
	
	public static final String CHITANTA_NAME = "CHITANTA";
	public static final String MONETAR_NAME = "MONETAR";
	public static final String CARD_NAME = "CARD";
	public static final String ORDIN_PLATA_NAME = "ORDIN PLATA";
	public static final String DISP_INCASARE_NAME = "DISPOZITIE INCASARE";
	public static final String DISP_PLATA_NAME = "DISPOZITIE PLATA";
	public static final String STAT_PLATA_NAME = "STAT DE PLATA";
	public static final String SALAR_NAME = "SALAR";
	
	public static final String BON_CASA_NR_NEINCHIS = "-1";
	public static final String BON_CASA_NR_FAILED_SUFFIX = "_NEFISCALIZAT";
	public static final String AUTOMATIC_TRANSFER_PREFIX = "TR_";
	public static final String AUTOMATIC_NOTIFICATION_PREFIX = "NOT_";
	public static final String AUTOMATIC_PROCES_PRET_PREFIX = "PSP_";
	public static final String AUTOMATIC_BREW_PREFIX = "PR_";
	public static final String AUTOMATIC_SALAR_PREFIX = "SALAR_";
	
	public enum LoadBonuri
	{
		FARA_BONURI, SI_BONURI, DOAR_BONURI;
	}
	
	public enum RPZLoad
	{
		DOAR_RPZ, FARA_RPZ, INDIFERENT;
	}
	
	public enum CasaLoad
	{
		DOAR_CASA, FARA_CASA, INDIFERENT;
	}
	
	public enum BancaLoad
	{
		DOAR_BANCA, FARA_BANCA, INDIFERENT;
	}
	
	public enum DocumentTypesLoad
	{
		DOAR_DISCOUNTURI, FARA_DISCOUNTURI, INDIFERENT;
	}
	
	public enum ContaLoad
	{
		DOAR_CONTA, FARA_CONTA, INDIFERENT;
	}
	
	public enum CoveredDocsLoad
	{
		DOAR_COVERED, FARA_COVERED, INDIFERENT;
	}
	
	@ManyToOne
	@JoinColumn(name = "gestiune_id", nullable = false)
	private Gestiune gestiune;

	/**
	 * Factura, Proces verbal, Bon de casa, Bon consum, Chitanta, OP, Monetar...
	 */
	@Column(nullable = false)
	private String doc;

	// String because it can contain letters(BFZ1)
	@Column(name = "nrdoc")
	private String nrDoc;

	/**
	 * TRUE daca face parte din raportul zilnic de gestiune
	 */
	@Column(columnDefinition="BOOLEAN DEFAULT false")
	private boolean rpz = false;
	
	/**
	 * TRUE daca face parte din registrul de casa
	 */
	@Column(columnDefinition="BOOLEAN DEFAULT false", name = "regcasa")
	private boolean regCasa = false;
	
	@Column(columnDefinition="BOOLEAN DEFAULT false", name = "incontabilitate")
	private boolean inContabilitate = false;
	@Column(columnDefinition="BOOLEAN DEFAULT true")
	private boolean editable = true;
	
	@Column(name = "datareceptie")
	private LocalDateTime dataReceptie = LocalDateTime.now();
	/*
	 * String because it can contain letters.
	 * Used only on some documents(ex: Receptie marfa)
	 */
	@Column(name = "nrreceptie")
	private String nrReceptie;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "accDoc", cascade = CascadeType.ALL)
	private Set<Operatiune> operatiuni = new HashSet<>();
	
	@Column(columnDefinition = "text")
    private String indicatii;
	@Column(columnDefinition = "text")
    private String address;
	private String phone;
	
	public static String automaticTransferNr(final LocalDate date)
	{
		return MessageFormat.format("{0}{1}", AccountingDocument.AUTOMATIC_TRANSFER_PREFIX, date.format(DateTimeFormatter.ISO_DATE));
	}
	
	public static String automaticNotificationNr(final LocalDate date)
	{
		return MessageFormat.format("{0}{1}", AccountingDocument.AUTOMATIC_NOTIFICATION_PREFIX, date.format(DateTimeFormatter.ISO_DATE));
	}
	
	public static String automaticProcesSchimbarePretNr(final LocalDate date)
	{
		return MessageFormat.format("{0}{1}", AccountingDocument.AUTOMATIC_PROCES_PRET_PREFIX, date.format(DateTimeFormatter.ofPattern("uuuu-MM-'W'W")));
	}
	
	public static String automaticBrewNr(final LocalDate date, final Gestiune gestiune)
	{
		return MessageFormat.format("{0}{1}_{2}",
				AccountingDocument.AUTOMATIC_BREW_PREFIX,
				date.format(DateTimeFormatter.ISO_DATE),
				safeString(gestiune, Gestiune::getImportName));
	}
	
	public static String automaticSalarNr(final LocalDate date)
	{
		return MessageFormat.format("{0}{1}",
				AccountingDocument.AUTOMATIC_SALAR_PREFIX,
				date.format(DateTimeFormatter.ofPattern("uuuu-MM")));
	}
	
	public static boolean isUnclosedBonCasa(final AccountingDocument bon)
	{
		return globalIsMatch(bon.getNrDoc(), BON_CASA_NR_NEINCHIS, TextFilterMethod.EQUALS) &&
				globalIsMatch(bon.getDoc(), BON_CASA_NAME, TextFilterMethod.EQUALS);
	}
	
	public static BigDecimal extractTvaAmount(final BigDecimal totalCuTva, final BigDecimal tvaExtractDivisor)
	{
		if (totalCuTva == null || tvaExtractDivisor == null)
			return null;
		
		final BigDecimal totalFaraTva = totalCuTva.divide(tvaExtractDivisor, 2, RoundingMode.HALF_EVEN);
		return totalCuTva.subtract(totalFaraTva);
	}

	public static BigDecimal extractTvaPercentage(final BigDecimal totalFaraTva, final BigDecimal totalTva)
	{
		return extractTvaPercentage(totalFaraTva, totalTva, 4);
	}
	
	public static BigDecimal extractTvaPercentage(final BigDecimal totalFaraTva, final BigDecimal totalTva, final int scale)
	{
		if (totalFaraTva == null || totalTva == null || totalFaraTva.compareTo(BigDecimal.ZERO) == 0)
			return BigDecimal.ZERO;
		
		return totalTva.divide(totalFaraTva, scale, RoundingMode.HALF_EVEN).abs();
	}
	
	public static int calculatePointsFromProfit(final BigDecimal profit)
	{
		return multiply(profit, POINT_PER_RON).setScale(0, RoundingMode.HALF_EVEN).intValue();
	}
	
	public AccountingDocument()
	{
	}

	public Gestiune getGestiune()
	{
		return gestiune;
	}

	public void setGestiune(final Gestiune gestiune)
	{
		this.gestiune = gestiune;
	}

	public String getNrDoc()
	{
		return nrDoc;
	}

	public void setNrDoc(final String nrDoc)
	{
		this.nrDoc = nrDoc;
	}

	public String getDoc()
	{
		return doc;
	}

	public void setDoc(final String doc)
	{
		this.doc = doc;
	}
	
	public boolean isRpz()
	{
		return rpz;
	}

	public void setRpz(final boolean rpz)
	{
		this.rpz = rpz;
	}
	
	public boolean isIntrareInRpz()
	{
		return TipDoc.CUMPARARE.equals(getTipDoc()) || TipDoc.PLATA.equals(getTipDoc());
	}
	
	public boolean isIesireInRpz()
	{
		return TipDoc.VANZARE.equals(getTipDoc()) || TipDoc.INCASARE.equals(getTipDoc());
	}
	
	public boolean isRegCasa()
	{
		return regCasa;
	}
	
	public void setRegCasa(final boolean regCasa)
	{
		this.regCasa = regCasa;
	}
	
	public boolean isInContabilitate()
	{
		return inContabilitate;
	}
	
	public void setInContabilitate(final boolean inContabilitate)
	{
		this.inContabilitate = inContabilitate;
	}
	
	public boolean isEditable()
	{
		return editable;
	}
	
	public boolean isNotEditable()
	{
		return !isEditable();
	}
	
	public void setEditable(final boolean editable)
	{
		this.editable = editable;
	}
	
	public String getIndicatii()
	{
		return indicatii;
	}
	
	public void setIndicatii(final String indicatii)
	{
		this.indicatii = indicatii;
	}
	
	public String getAddress()
	{
		return address;
	}
	
	public void setAddress(final String address)
	{
		this.address = address;
	}
	
	public String getPhone()
	{
		return phone;
	}
	
	public void setPhone(final String phone)
	{
		this.phone = phone;
	}
	
	public Optional<String> phone()
	{
		return isEmpty(phone) ? Optional.empty() : Optional.ofNullable(phone);
	}
	
	public Optional<String> address()
	{
		return isEmpty(address) ? Optional.empty() : Optional.ofNullable(address);
	}
	
	public String addressNullable()
	{
		return isEmpty(address) ? null : address;
	}
	
	public Set<Operatiune> getOperatiuni()
	{
		return operatiuni;
	}
	
	public Stream<Operatiune> getOperatiuni_Stream()
	{
		return operatiuni.stream();
	}
	
	public void setOperatiuni(final Set<Operatiune> operatiuni)
	{
		this.operatiuni = operatiuni;
	}
	
	public LocalDateTime getDataReceptie()
	{
		return dataReceptie;
	}
	
	public void setDataReceptie(final LocalDateTime dataReceptie)
	{
		this.dataReceptie = dataReceptie;
	}
	
	public String getNrReceptie()
	{
		return nrReceptie;
	}
	
	public void setNrReceptie(final String nrReceptie)
	{
		this.nrReceptie = nrReceptie;
	}
	
	/**
	 * In case we have operations, we don't use the total field;
	 * We calculate the total by adding the sum of all the operations
	 */
	private BigDecimal getVanzareTotalFaraTva()
	{
		return getOperatiuni().stream()
				.map(Operatiune::getValoareVanzareFaraTVA)
				.filter(Objects::nonNull)
				.reduce(BigDecimal::add)
				.orElse(BigDecimal.ZERO);
	}
	
	public BigDecimal getVanzareTotalTva()
	{
		final BigDecimal totalTva = super.getTotalTva();
		
		if (totalTva == null)
			return getOperatiuni().stream()
					.map(Operatiune::getValoareVanzareTVA)
					.filter(Objects::nonNull)
					.reduce(BigDecimal::add)
					.orElse(BigDecimal.ZERO);
			
		return totalTva;
	}
	
	public BigDecimal getVanzareTotal()
	{
		final BigDecimal total = super.getTotal();
		
		if (total == null)
			return add(getVanzareTotalFaraTva(), getVanzareTotalTva());
			
		return total;
	}
	
	@Override
	public BigDecimal getTotal()
	{
		final BigDecimal total = super.getTotal();
		
		if (total == null)
			if (TipDoc.VANZARE.equals(getTipDoc()))
				return getVanzareTotal();
			else if (TipDoc.CUMPARARE.equals(getTipDoc()))
				return getAchizitieTotal();
			else
				return BigDecimal.ZERO;
		
		return total;
	}
	
	@Override
	public BigDecimal getTotalTva()
	{
		final BigDecimal totalTva = super.getTotalTva();
		
		if (totalTva == null)
			if (TipDoc.VANZARE.equals(getTipDoc()))
				return getVanzareTotalTva();
			else if (TipDoc.CUMPARARE.equals(getTipDoc()))
				return getAchizitieTotalTva();
			else
				return BigDecimal.ZERO;
		
		return totalTva;
	}
	
	
	@Override
	public BigDecimal getTotalRpz()
	{
		final BigDecimal totalRPZOperatiuni = getTotalRPZOperatiuni();
		
		if (totalRPZOperatiuni != null)
			return totalRPZOperatiuni;
		
		if (isRpz())
			return getVanzareTotal();
		
		return BigDecimal.ZERO;
	}
	
	@Override
	public BigDecimal getTotalRpzTva()
	{
		final BigDecimal totalRPZTvaOperatiuni = getTotalRPZTvaOperatiuni();
		
		if (totalRPZTvaOperatiuni != null)
			return totalRPZTvaOperatiuni;
		
		if (isRpz())
			return getVanzareTotalTva();
		
		return BigDecimal.ZERO;
	}
	
	private BigDecimal getTotalRPZOperatiuni()
	{
		if (getOperatiuni().isEmpty())
			return null;
		
		return getOperatiuni().stream()
				.filter(Operatiune::isRpz)
				.map(op -> add(op.getValoareVanzareFaraTVA(), op.getValoareVanzareTVA()))
				.filter(Objects::nonNull)
				.reduce(BigDecimal::add)
				.orElse(BigDecimal.ZERO);
	}
	
	private BigDecimal getTotalRPZTvaOperatiuni()
	{
		return getOperatiuni().stream()
				.filter(Operatiune::isRpz)
				.map(Operatiune::getValoareVanzareTVA)
				.filter(Objects::nonNull)
				.reduce(BigDecimal::add)
				.orElse(null);
	}
	
	private BigDecimal getAchizitieTotalFaraTva()
	{
		return getOperatiuni().stream()
				.map(Operatiune::getValoareAchizitieFaraTVA)
				.filter(Objects::nonNull)
				.reduce(BigDecimal::add)
				.orElse(BigDecimal.ZERO);
	}
	
	public BigDecimal getAchizitieTotalTva()
	{
		final BigDecimal totalTva = super.getTotalTva();
		
		if (totalTva == null)
			return getOperatiuni().stream()
					.map(Operatiune::getValoareAchizitieTVA)
					.filter(Objects::nonNull)
					.reduce(BigDecimal::add)
					.orElse(BigDecimal.ZERO);
			
		return totalTva;
	}
	
	public BigDecimal getAchizitieTotal()
	{
		final BigDecimal total = super.getTotal();
		
		if (total == null)
			return add(getAchizitieTotalFaraTva(), getAchizitieTotalTva());
			
		return total;
	}

	private boolean isCumparareBonCasa()
	{
		return BON_CASA_NAME.equalsIgnoreCase(safeString(getDoc())) &&
				(TipDoc.CUMPARARE.equals(getTipDoc()) || TipDoc.PLATA.equals(getTipDoc()));
	}

	public boolean isDispPlataIncasare()
	{
		return AccountingDocument.DISP_INCASARE_NAME.equalsIgnoreCase(getDoc()) ||
				AccountingDocument.DISP_PLATA_NAME.equalsIgnoreCase(getDoc());
	}
	
	public BigDecimal tvaDePlata()
	{
		return (TipDoc.CUMPARARE.equals(getTipDoc()) || TipDoc.PLATA.equals(getTipDoc())) ? getTotalTva().negate() :
			getTotalTva();
	}
	
	public Stream<AccountingDocument> separateForRPZ()
	{
		if (getOperatiuni().isEmpty())
			return Stream.of(this);
		
		final Map<Gestiune, List<Operatiune>> opsByGest = getOperatiuni_Stream()
				.collect(Collectors.groupingBy(Operatiune::getGestiune));
		
		return opsByGest.entrySet().stream()
				.map(entry ->
				{
					final AccountingDocument accDoc = new AccountingDocument();
					accDoc.setCompany(getCompany());
					accDoc.setPartner(getPartner());
					accDoc.setTipDoc(getTipDoc());
					accDoc.setName(getName());
					accDoc.setOperator(getOperator());
					accDoc.setDoc(getDoc());
					accDoc.setNrDoc(getNrDoc());
					accDoc.setNrReceptie(getNrReceptie());
					accDoc.setRpz(isRpz());
					accDoc.setRegCasa(isRegCasa());
					accDoc.setDataDoc(getDataDoc());
					accDoc.setDataReceptie(getDataReceptie());
					accDoc.setEditable(isEditable());
					accDoc.setInContabilitate(isInContabilitate());
					accDoc.setScadenta(getScadenta());
					accDoc.setGestiune(entry.getKey());
					accDoc.setAddress(getAddress());
					accDoc.setPhone(getPhone());
					accDoc.getOperatiuni().addAll(entry.getValue());
					return accDoc;
				});
	}
	
	public int calculatedPoints()
	{
		return calculatePointsFromProfit(getTotal().subtract(getAchizitieTotal()));
	}
	
	@Override
	public String toString()
	{
		final StringBuilder builder = new StringBuilder();
		builder.append("AccountingDocument [gestiune=");
		builder.append(safeString(getGestiune(), Gestiune::getImportName));
		builder.append(", doc=");
		builder.append(doc);
		builder.append(", nrDoc=");
		builder.append(nrDoc);
		builder.append(", rpz=");
		builder.append(rpz);
		builder.append(", regCasa=");
		builder.append(regCasa);
		builder.append(", inContabilitate=");
		builder.append(inContabilitate);
		builder.append(", editable=");
		builder.append(editable);
		builder.append(", dataReceptie=");
		builder.append(dataReceptie);
		builder.append(", nrReceptie=");
		builder.append(nrReceptie);
		builder.append(", partner=");
		builder.append(safeString(getPartner(), Partner::getName));
		builder.append(", tipDoc=");
		builder.append(getTipDoc());
		builder.append(", dataDoc=");
		builder.append(getDataDoc());
		builder.append(", id=");
		builder.append(getId());
		builder.append(", name=");
		builder.append(getName());
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((doc == null) ? 0 : doc.hashCode());
		result = prime * result + ((gestiune == null) ? 0 : gestiune.hashCode());
		result = prime * result + ((nrDoc == null) ? 0 : nrDoc.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		final AccountingDocument other = (AccountingDocument) obj;
		if (doc == null)
		{
			if (other.doc != null)
				return false;
		} else if (!doc.equals(other.doc))
			return false;
		if (gestiune == null)
		{
			if (other.gestiune != null)
				return false;
		} else if (!gestiune.equals(other.gestiune))
			return false;
		if (nrDoc == null)
		{
			if (other.nrDoc != null)
				return false;
		} else if (!nrDoc.equals(other.nrDoc))
			return false;
		return true;
	}
}
