package ro.linic.cloud.mapper;

import static ro.linic.cloud.util.PresentationUtils.safeString;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import ro.linic.cloud.dto.InvoiceOldDto;
import ro.linic.cloud.embedable.Address;
import ro.linic.cloud.entity.AccountingDocument;
import ro.linic.cloud.entity.Gestiune;
import ro.linic.cloud.entity.Operatiune;
import ro.linic.cloud.pojo.Invoice;
import ro.linic.cloud.pojo.InvoiceLine;
import ro.linic.cloud.pojo.TaxCategory;
import ro.linic.cloud.pojo.TaxSubtotal;
import ro.linic.cloud.util.LocalDateUtils;
import ro.linic.cloud.util.PresentationUtils;

@Mapper
public interface InvoiceMapper {
	InvoiceMapper INSTANCE = Mappers.getMapper(InvoiceMapper.class);
	
	@Mapping(target = "id", source = "invOld.accDoc.id")
	@Mapping(target = "invoiceNumber", source = ".", qualifiedByName = "getInvoiceNumber")
	@Mapping(target = "issueDate", source = "invOld.accDoc.dataDoc")
	@Mapping(target = "dueDate", source = "invOld.accDoc.scadenta")
	@Mapping(target = "accountingSupplier.taxId", source = "invOld.firmaCui")
	@Mapping(target = "accountingSupplier.registrationName", source = "invOld.firmaName")
	@Mapping(target = "accountingSupplier.registrationId", source = "invOld.firmaRegCom")
	@Mapping(target = "accountingSupplier.companyLegalForm", source = "invOld.firmaCapSocial", qualifiedByName = "displayCapSocial")
	@Mapping(target = "accountingSupplier.postalAddress.country", constant = "RO")
	@Mapping(target = "accountingSupplier.postalAddress.countrySubentity", source = "invOld.firmaBillingAddressCodJudet")
	@Mapping(target = "accountingSupplier.postalAddress.city", source = "invOld.firmaBillingAddressCity")
	@Mapping(target = "accountingSupplier.postalAddress.primaryLine", source = "invOld.firmaBillingAddressStreet")
	@Mapping(target = "accountingSupplier.contactName", source = "invOld.accDoc.operator.name")
	@Mapping(target = "accountingSupplier.telephone", source = "invOld.firmaPhone")
	@Mapping(target = "accountingSupplier.electronicMail", source = "invOld.firmaEmail")
	@Mapping(target = "accountingCustomer.taxId", source = "invOld.accDoc.partner.codFiscal")
	@Mapping(target = "accountingCustomer.registrationName", source = "invOld.accDoc.partner.name")
	@Mapping(target = "accountingCustomer.registrationId", source = "invOld.accDoc.partner.regCom")
	@Mapping(target = "accountingCustomer.postalAddress", source = "invOld.accDoc.partner.address", qualifiedByName = "extractAddress")
	@Mapping(target = "accountingCustomer.contactName", source = "invOld.accDoc.partner.delegat.name")
	@Mapping(target = "accountingCustomer.telephone", source = "invOld.accDoc.partner.phone")
	@Mapping(target = "accountingCustomer.electronicMail", source = "invOld.accDoc.partner.email")
	@Mapping(target = "documentCurrencyCode", constant = "RON")
	@Mapping(target = "paymentMeansCode", constant = "30")
	@Mapping(target = "paymentId", source = "invOld.accDoc", qualifiedByName = "mapPaymentId")
	@Mapping(target = "payeeFinancialAccount.id", source = "invOld.firmaIban")
	@Mapping(target = "payeeFinancialAccount.name", source = "invOld.firmaName")
	@Mapping(target = "taxCurrencyCode", constant = "RON")
	@Mapping(target = "taxAmount", source = "invOld.accDoc.totalTva")
	@Mapping(target = "taxSubtotals", source = "invOld.accDoc", qualifiedByName = "getTaxSubtotals")
	@Mapping(target = "lineExtensionAmount", source = "invOld.accDoc.vanzareTotalFaraTva")
	@Mapping(target = "taxExclusiveAmount", source = "invOld.accDoc.vanzareTotalFaraTva")
	@Mapping(target = "taxInclusiveAmount", source = "invOld.accDoc.total")
	@Mapping(target = "prepaidAmount", source = "invOld.accDoc.totalLinked")
	@Mapping(target = "payableAmount", source = "invOld.accDoc.totalUnlinked")
	@Mapping(target = "lines", source = "invOld.accDoc.operatiuni")
	@Mapping(target = "allowanceCharges", ignore = true)
	@Mapping(target = "allowanceTotalAmount", ignore = true)
	@Mapping(target = "chargeTotalAmount", ignore = true)
	@Mapping(target = "note", ignore = true)
	@Mapping(target = "payableRoundingAmount", ignore = true)
	@Mapping(target = "payeeParty", ignore = true)
	@Mapping(target = "payeeFinancialAccount.financialInstitutionBranch", ignore = true)
	@Mapping(target = "payeeFinancialAccount.currency", ignore = true)
	@Mapping(target = "accountingSupplier.businessName", ignore = true)
	@Mapping(target = "accountingSupplier.postalAddress.postalZone", ignore = true)
	@Mapping(target = "accountingSupplier.postalAddress.secondaryLine", ignore = true)
	@Mapping(target = "accountingCustomer.companyLegalForm", ignore = true)
	@Mapping(target = "accountingCustomer.businessName", ignore = true)
	Invoice toInvoice(InvoiceOldDto invOld);
	
	@Mapping(target = "sellersItemIdentification", source = "op.barcode")
	@Mapping(target = "quantity", source = "op.cantitate")
	@Mapping(target = "uom", source = "op.uomInternational")
	@Mapping(target = "classifiedTaxCategory", source = "op")
	@Mapping(target = "price", source = "op.pretVanzareUnitarFaraTVA")
	@Mapping(target = "baseQuantity", constant = "1")
	@Mapping(target = "lineExtensionAmount", source = "op.valoareVanzareFaraTVA")
	@Mapping(target = "taxAmount", source = "op.valoareVanzareTVA")
	@Mapping(target = "allowanceCharges", ignore = true)
	@Mapping(target = "buyersItemIdentification", ignore = true)
	@Mapping(target = "description", ignore = true)
	@Mapping(target = "note", ignore = true)
	InvoiceLine toInvoiceLine(Operatiune op);
	
	@Mapping(target = "code", constant = "S")
	@Mapping(target = "percent", source = "op.tvaPercentCalculated")
	@Mapping(target = "taxScheme", constant = "VAT")
	@Mapping(target = "taxExemptionReason", ignore = true)
	TaxCategory operatiuneToTaxCategory(Operatiune op);
	
	@Named("extractAddress")
	default ro.linic.cloud.pojo.Address extractAddress(final Address address) {
		if (address == null)
            return null;
		
		final ro.linic.cloud.pojo.Address target = new ro.linic.cloud.pojo.Address();
		target.setCountry(PresentationUtils.safeString(address.getCountry(), "RO"));
		target.setCountrySubentity(address.getJudet());
		target.setCity(address.getOras());
		target.setPrimaryLine(address.getStrada());
		target.setPostalZone(address.getNr());
        return target;
    }
	
	@Named("getInvoiceNumber")
	default String getInvoiceNumber(final InvoiceOldDto invOld) {
        return safeString(invOld.getSeriaFactura()) + safeString(invOld.getAccDoc().getGestiune(), Gestiune::getImportName) + "-" +
	    		safeString(invOld.getAccDoc().getNrDoc());
    }
	
	@Named("getTaxSubtotals")
	default List<TaxSubtotal> getTaxSubtotals(final AccountingDocument accDoc) {
		if (accDoc == null)
			return List.of();
		
        return accDoc.getOperatiuni_Stream()
        		.collect(Collectors.groupingBy(Operatiune::getTvaPercentCalculated))
        		.entrySet().stream()
        		.map(vatToOps -> 
        		{
        			final TaxSubtotal taxSubtotal = new TaxSubtotal();
        			taxSubtotal.setTaxableAmount(vatToOps.getValue().stream()
        					.map(Operatiune::getValoareVanzareFaraTVA)
        					.reduce(BigDecimal::add)
        					.orElse(BigDecimal.ZERO));
        			taxSubtotal.setTaxAmount(vatToOps.getValue().stream()
        					.map(Operatiune::getValoareVanzareTVA)
        					.reduce(BigDecimal::add)
        					.orElse(BigDecimal.ZERO));
        			final TaxCategory taxCategory = new TaxCategory();
        			taxCategory.setCode("S");
        			taxCategory.setPercent(vatToOps.getKey());
        			taxCategory.setTaxScheme("VAT");
					taxSubtotal.setTaxCategory(taxCategory);
					return taxSubtotal;
        		})
        		.collect(Collectors.toList());
    }
	
	@Named("displayCapSocial")
	default String displayCapSocial(final String firmaCapSocial) {
        return "Capital social "+firmaCapSocial;
    }
	
	@Named("mapPaymentId")
	default String mapPaymentId(final AccountingDocument accDoc) {
        return MessageFormat.format("FF_{0}/{1}",
        		safeString(accDoc, AccountingDocument::getNrDoc),
        		safeString(accDoc, AccountingDocument::getDataDoc,
        				dataDoc -> LocalDateUtils.displayLocalDateTime(dataDoc, LocalDateUtils.DATE_FORMATTER)));
    }
	
	default Instant map(final LocalDateTime value) {
        return value == null ? null : value.atZone(ZoneId.of("Europe/Bucharest")).toInstant();
    }
	default Instant map(final LocalDate value) {
        return value == null ? null : value.atStartOfDay().atZone(ZoneId.of("Europe/Bucharest")).toInstant();
    }
}
