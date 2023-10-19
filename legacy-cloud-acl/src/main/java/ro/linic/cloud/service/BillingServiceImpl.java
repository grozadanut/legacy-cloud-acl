package ro.linic.cloud.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.linic.cloud.dto.InvoiceOldDto;
import ro.linic.cloud.entity.AccountingDocument;
import ro.linic.cloud.entity.Document.TipDoc;
import ro.linic.cloud.entity.PersistedProp;
import ro.linic.cloud.mapper.InvoiceMapper;
import ro.linic.cloud.pojo.Invoice;
import ro.linic.cloud.repository.AccountingDocumentRepository;
import ro.linic.cloud.repository.PersistedPropRepository;

@Service
public class BillingServiceImpl implements BillingService {
	
	@Autowired private PersistedPropRepository persistedPropRepo;
	@Autowired private AccountingDocumentRepository accDocRepo;
	@Autowired private InvoiceMapper billingMapper;
	
	@Override
	public Invoice getInvoice(final Long invoiceId) {
		final AccountingDocument accDoc = accDocRepo.findById(invoiceId).get();
		
		if (!TipDoc.VANZARE.equals(accDoc.getTipDoc()))
			throw new RuntimeException("TipDoc trebuie sa fie VANZARE!");
		if (!AccountingDocument.FACTURA_NAME.equalsIgnoreCase(accDoc.getDoc()))
			throw new RuntimeException("Documentul trebuie sa fie FACTURA!");
		
		final List<PersistedProp> props = persistedPropRepo.findByCompanyId(accDoc.getCompany().getId());
		final String seriaFactura = findOrDefault(props, PersistedProp.SERIA_FACTURA_KEY, PersistedProp.SERIA_FACTURA_DEFAULT);
		final String firmaName = findOrDefault(props, PersistedProp.FIRMA_NAME_KEY, PersistedProp.FIRMA_NAME_DEFAULT_VALUE);
		final String firmaCui = findOrDefault(props, PersistedProp.FIRMA_CUI_KEY, PersistedProp.FIRMA_CUI_DEFAULT_VALUE);
		final String firmaCapSocial = findOrDefault(props, PersistedProp.FIRMA_CAP_SOCIAL_KEY, PersistedProp.FIRMA_CAP_SOCIAL_DEFAULT_VALUE);
		final String firmaPhone = findOrDefault(props, PersistedProp.FIRMA_PHONE_KEY, PersistedProp.FIRMA_PHONE_DEFAULT_VALUE);
		final String firmaEmail = findOrDefault(props, PersistedProp.FIRMA_EMAIL_KEY, PersistedProp.FIRMA_EMAIL_DEFAULT_VALUE);
		final String firmaIban = findOrDefault(props, PersistedProp.FIRMA_MAIN_BANK_ACC_KEY, PersistedProp.FIRMA_MAIN_BANK_ACC_DEFAULT_VALUE)
				.replaceAll("\\s+",""); // remove whitespace
		final String firmaBillingAddressStreet = findOrDefault(props, PersistedProp.FIRMA_BILLING_PRIMARY_LINE_KEY, PersistedProp.FIRMA_BILLING_PRIMARY_LINE_DEFAULT_VALUE);
		final String firmaBillingAddressCity = findOrDefault(props, PersistedProp.FIRMA_BILLING_CITY_KEY, PersistedProp.FIRMA_BILLING_CITY_DEFAULT_VALUE);
		final String firmaBillingAddressCodJudet = findOrDefault(props, PersistedProp.FIRMA_BILLING_COD_JUDET_KEY, PersistedProp.FIRMA_BILLING_COD_JUDET_DEFAULT_VALUE);
		
		final InvoiceOldDto invOld = new InvoiceOldDto(accDoc, seriaFactura, firmaName, firmaCui, firmaCapSocial, firmaBillingAddressStreet,
				firmaBillingAddressCity, firmaBillingAddressCodJudet, firmaPhone, firmaEmail, firmaIban);
		
		return billingMapper.toInvoice(invOld);
	}
	
	private String findOrDefault(final List<PersistedProp> props, final String key, final String defaultValue) {
		return props.stream().filter(prop -> key.equalsIgnoreCase(prop.getKey()))
				.findFirst()
				.map(PersistedProp::getValue)
				.orElse(defaultValue);
	}
}
