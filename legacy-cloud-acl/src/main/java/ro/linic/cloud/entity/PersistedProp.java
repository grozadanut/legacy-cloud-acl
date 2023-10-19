package ro.linic.cloud.entity;

import static ro.linic.cloud.util.PresentationUtils.EMPTY_STRING;
import static ro.linic.cloud.util.PresentationUtils.safeString;
import static ro.linic.cloud.util.StringUtils.isEmpty;

import java.io.Serializable;
import java.util.Optional;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "persisted_prop")
public class PersistedProp implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public static final String FIRMA_NAME_KEY = "firma_name";
	public static final String FIRMA_NAME_DEFAULT_VALUE = "SC LINIC SRL";
	
	public static final String FIRMA_CUI_KEY = "firma_cui";
	public static final String FIRMA_CUI_DEFAULT_VALUE = "RO14998343";
	
	public static final String FIRMA_REG_COM_KEY = "firma_reg_com";
	public static final String FIRMA_REG_COM_DEFAULT_VALUE = "J05/1111/2002";
	
	public static final String FIRMA_CAP_SOCIAL_KEY = "firma_cap_social";
	public static final String FIRMA_CAP_SOCIAL_DEFAULT_VALUE = "100,000.00 RON";
	
	public static final String FIRMA_MAIN_BANK_KEY = "firma_main_bank";
	public static final String FIRMA_MAIN_BANK_DEFAULT_VALUE = "BANCA TRANSILVANIA ORADEA";
	
	public static final String FIRMA_MAIN_BANK_ACC_KEY = "firma_main_bank_acc";
	public static final String FIRMA_MAIN_BANK_ACC_DEFAULT_VALUE = "RO48 BTRL 0050 1202 K652 77XX - RON";
	
	public static final String FIRMA_SECONDARY_BANK_KEY = "firma_secondary_bank";
	public static final String FIRMA_SECONDARY_BANK_DEFAULT_VALUE = "TREZORERIA MARGHITA";
	
	public static final String FIRMA_SECONDARY_BANK_ACC_KEY = "firma_secondary_bank_acc";
	public static final String FIRMA_SECONDARY_BANK_ACC_DEFAULT_VALUE = "RO02 TREZ 0825 069X XX00 0324 - RON";
	
	public static final String FIRMA_ADDRESS_KEY = "firma_address";
	public static final String FIRMA_ADDRESS_DEFAULT_VALUE = "Marghita PL Colibri - Str T. Vladimirescu 59, PL Linic - Str N. Balcescu 51";
	
	public static final String FIRMA_BILLING_ADDRESS_KEY = "firma_billing_address";
	public static final String FIRMA_BILLING_ADDRESS_DEFAULT_VALUE = "MARGINE Str Principala nr 218A, BIHOR";
	
	public static final String FIRMA_BILLING_COD_JUDET_KEY = "firma_billing_cod_judet";
	public static final String FIRMA_BILLING_COD_JUDET_DEFAULT_VALUE = "RO-BH";
	public static final String FIRMA_BILLING_CITY_KEY = "firma_billing_city";
	public static final String FIRMA_BILLING_CITY_DEFAULT_VALUE = "MARGINE";
	public static final String FIRMA_BILLING_PRIMARY_LINE_KEY = "firma_billing_primary_line";
	public static final String FIRMA_BILLING_PRIMARY_LINE_DEFAULT_VALUE = "Str Principala nr 218A";
	
	public static final String FIRMA_PHONE_KEY = "firma_phone";
	public static final String FIRMA_PHONE_DEFAULT_VALUE = "Colibri - 0787577227, Linic - 0259362437";
	
	public static final String FIRMA_EMAIL_KEY = "firma_email";
	public static final String FIRMA_EMAIL_DEFAULT_VALUE = "colibridepot@gmail.com, sclinicsrl@gmail.com";
	
	public static final String FIRMA_WEBSITE_KEY = "firma_website";
	public static final String FIRMA_WEBSITE_DEFAULT_VALUE = "www.linic.ro";
	
	public static final String TVA_PERCENT_KEY = "tva_percent";
	public static final String TVA_PERCENT_DEFAULT = "0.19";
	
	public static final String SERIA_FACTURA_KEY = "seria_factura";
	public static final String SERIA_FACTURA_DEFAULT = "LIND";
	
	public static final String MIGRATION_DATE_KEY = "migration_date";
	public static final String MIGRATION_DATE_DEFAULT = "2020-04-18";
	
	public static final String NR_BON_PROMO_KEY = "nr_bon_promo";
	public static final String NR_BON_PROMO_DEFAULT = "0";
	
	public static final String PREFIX_AROMA_KEY = "nr_bon_promo";
	public static final String PREFIX_AROMA_DEFAULT = EMPTY_STRING;
	
	public static final String HAS_MAIL_SMTP_KEY = "has_mail_smtp";
	public static final String HAS_MAIL_SMTP_DEFAULT = "false";
	
	public static final String SMTP_SESSION_ID_KEY = "smtp_session_id";
	public static final String SMTP_SESSION_ID_DEFAULT = "0";
	
	public static final String CAFEA_BARCODE_KEY = "cafea_barcode";
	public static final String CAFEA_BARCODE_DEFAULT = "213";
	public static final String GRAME_CAFEA_PE_SHOT_KEY = "grame_cafea_pe_shot";
	public static final String GRAME_CAFEA_PE_SHOT_DEFAULT = "7.5";
	
	public static final String ID_FIELD = "id";
	public static final String COMPANY_FIELD = "company";
	public static final String KEY_FIELD = "key";
	public static final String VALUE_FIELD = "value";
	
	public static final String EMAIL_SIGNATURE_KEY = "email_signature";
	public static final String EMAIL_SIGNATURE_DEFAULT = "--<br>"
			+ "Colibri Clean SRL<br>"
			+ "CIF: 42794063<br>"
			+ "Mobil: <a href=\"tel:+40 760 337 394\">+40 760 337 394</a><br>"
			+ "Website: <a href=\"https://colibriclean.ro/\">colibriclean.ro</a><br>"
			+ "Facebook: <a href=\"https://www.facebook.com/colibriclean/\">@colibriclean</a><br>"
			+ "Instagram: <a href=\"https://www.instagram.com/colibriclean_/\">colibriclean_</a><br><br>"
			+ "Groza Danut<br>"
			+ "<a href=\"mailto:danut@colibriclean.ro\">danut@colibriclean.ro</a>";
	
	public static final String CONTABIL_EMAIL_KEY = "contabil_email";
	public static final String CONTABIL_EMAIL_DEFAULT = "competenttaxadvisor@gmail.com";
	public static final String CONTABIL_NAME_KEY = "contabil_name";
	public static final String CONTABIL_NAME_DEFAULT = "COMPETENT TAX ADVISOR SRL";
	
	public static final String PROP_ROW_SEP = ";";
	public static final String PROP_VALUE_SEP = ":";
	public static final String PLANNED_CONTRACT_MONTH_KEY = "planned_contract_month";
	
	/**
	 * Format is: [userId][PROP_VALUE_SEP][companyId][PROP_VALUE_SEP][imageUUID][PROP_ROW_SEP]
	 */
	public static final String BILL_SIGNATURE_KEY = "bill_signature";
	public static final String BILL_SIGNATURE_DEFAULT = EMPTY_STRING;
	public static final String BILL_STAMP_KEY = "bill_stamp";
	public static final String BILL_STAMP_DEFAULT = EMPTY_STRING;
	
	public static final String SMS_APIKEY_KEY = "sms_api_key";
	public static final String SMS_API_SECRET_KEY = "sms_api_secret";
	
	public static final String ANAF_API_KEY = "anaf_api";
	public static final String ANAF_API_DEFAULT = "https://webservicesp.anaf.ro/PlatitorTvaRest/api/v8/ws/tva";
	public static final String E_FACTURA_CUST_ID_KEY = "e_factura_cust_id";
	public static final String E_FACTURA_CUST_ID_DEFAULT = "urn:cen.eu:en16931:2017#compliant#urn:efactura.mfinante.ro:CIUS-RO:1.0.1";
	
	@ManyToOne
	@JoinColumn(name = "company_id")
	private Company company;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(nullable = false)
	private String key;
	@Column(columnDefinition = "text")
	private String value;
	
	public static Optional<String> uuidFromProp(final String propValue, final String userId, final String companyId)
	{
		if (isEmpty(propValue) || isEmpty(userId) || isEmpty(companyId))
			return Optional.empty();
		
		final String[] rows = propValue.split(PersistedProp.PROP_ROW_SEP);
		
		for (final String row : rows)
		{
			final String[] rowToken = row.split(PersistedProp.PROP_VALUE_SEP);
			if (rowToken[0].equals(userId) && //userId
					rowToken[1].equals(companyId)) //companyId
				return Optional.ofNullable(rowToken[2]); //imageUUID
		}
		
		return Optional.empty();
	}
	
	public PersistedProp()
	{
	}
	
	public PersistedProp(final String key, final String value)
	{
		super();
		this.key = key;
		this.value = value;
	}

	public Company getCompany()
	{
		return company;
	}
	
	public void setCompany(final Company company)
	{
		this.company = company;
	}

	public String getKey()
	{
		return key;
	}

	public PersistedProp setKey(final String key)
	{
		this.key = key;
		return this;
	}
	
	public String getValueOr(final String defaultVal)
	{
		return safeString(value, defaultVal);
	}
	
	public PersistedProp setValue(final String value)
	{
		this.value = value;
		return this;
	}
	
	public String getValue()
	{
		return value;
	}

	public int getId()
	{
		return id;
	}

	@Override
	public String toString() {
		return "PersistedProp [key=" + key + ", value=" + value + "]";
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
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
		final PersistedProp other = (PersistedProp) obj;
		if (id != other.id)
			return false;
		if (key == null)
		{
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
	}
}
