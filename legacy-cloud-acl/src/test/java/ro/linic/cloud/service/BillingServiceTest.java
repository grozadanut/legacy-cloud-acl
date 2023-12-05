package ro.linic.cloud.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ro.linic.cloud.embedable.Address;
import ro.linic.cloud.embedable.Delegat;
import ro.linic.cloud.entity.AccountingDocument;
import ro.linic.cloud.entity.AccountingDocumentMapping;
import ro.linic.cloud.entity.Company;
import ro.linic.cloud.entity.Document.TipDoc;
import ro.linic.cloud.entity.Gestiune;
import ro.linic.cloud.entity.Operatiune;
import ro.linic.cloud.entity.Partner;
import ro.linic.cloud.entity.User;
import ro.linic.cloud.pojo.Invoice;
import ro.linic.cloud.repository.AccountingDocumentRepository;
import ro.linic.cloud.repository.PersistedPropRepository;

@ExtendWith(MockitoExtension.class)
public class BillingServiceTest {

	@Mock private PersistedPropRepository persistedPropRepo;
	@Mock private AccountingDocumentRepository accDocRepo;
	@InjectMocks private BillingServiceImpl billingService;
	
	@Test
	public void whenGetInvoice_thenTransformAccDocToInvoice() {
		// given
		final AccountingDocument accDoc = new AccountingDocument();
		final Company company = new Company();
		company.setId(1);
		accDoc.setCompany(company);
		accDoc.setId(1L);
		accDoc.setDataDoc(LocalDateTime.of(2023, 10, 17, 13, 13));
		accDoc.setDoc(AccountingDocument.FACTURA_NAME);
		accDoc.setNrDoc("101");
		accDoc.setScadenta(LocalDate.of(2023, 11, 16));
		accDoc.setTipDoc(TipDoc.VANZARE);
		
		final Gestiune linicGest = new Gestiune();
		linicGest.setImportName("L1");
		linicGest.setName("Linic");
		accDoc.setGestiune(linicGest);
		
		final User danut = new User();
		danut.setEmail("danut@yahoo.com");
		danut.setCnp("1xxx12254");
		danut.setName("Groza Danut");
		danut.setPhone("0712354789");
		accDoc.setOperator(danut);
		
		final Partner customer = new Partner();
		customer.setEmail("client@yahoo.com");
		customer.setName("CLIENT INC RO");
		customer.setPhone("0745613215");
		final Address customerAddress = new Address();
		customerAddress.setOras("Bihor Chet Principala");
		customer.setAddress(customerAddress);
		customer.setCodFiscal("12345678");
		customer.setRegCom("J40/1251/2023");;
		final Delegat delegat = new Delegat();
		delegat.setName("Client");
		customer.setDelegat(delegat);
		accDoc.setPartner(customer);
		
		final Operatiune op = new Operatiune();
		op.setId(1L);
		op.setBarcode("59");
		op.setCantitate(new BigDecimal("100"));
		op.setName("MATERIALE 40KG");
		op.setPretVanzareUnitarCuTVA(new BigDecimal("45.22"));
		op.setValoareVanzareFaraTVA(new BigDecimal("3800"));
		op.setValoareVanzareTVA(new BigDecimal("722"));
		op.setUom("BUC");
		accDoc.getOperatiuni().add(op);

		accDoc.getPaidBy().add(new AccountingDocumentMapping(null, null, null, new BigDecimal("1000")));
		
		when(accDocRepo.findById(1L)).thenReturn(Optional.of(accDoc));
		
		// when
		final Invoice invoice = billingService.getInvoice(1L);
		
		// then
		assertThat(invoice.getId()).isEqualTo(1);
		assertThat(invoice.getAllowanceCharges()).isNullOrEmpty();
		assertThat(invoice.getAllowanceTotalAmount()).isNull();
		assertThat(invoice.getChargeTotalAmount()).isNull();
		assertThat(invoice.getDocumentCurrencyCode()).isEqualTo("RON");
		assertThat(invoice.getDueDate()).isEqualTo(LocalDateTime.of(2023, 11, 16, 0, 0).atZone(ZoneId.of("Europe/Bucharest")).toInstant());
		assertThat(invoice.getInvoiceNumber()).isEqualTo("LINDL1-101");
		assertThat(invoice.getIssueDate()).isEqualTo(LocalDateTime.of(2023, 10, 17, 13, 13).atZone(ZoneId.of("Europe/Bucharest")).toInstant());
		assertThat(invoice.getLineExtensionAmount()).isEqualByComparingTo(new BigDecimal("3800"));
		assertThat(invoice.getNote()).isNull();
		assertThat(invoice.getPayableAmount()).isEqualByComparingTo(new BigDecimal("3522"));
		assertThat(invoice.getPayableRoundingAmount()).isNull();
		assertThat(invoice.getPaymentMeansCode()).isEqualTo("30");
		assertThat(invoice.getPaymentId()).isEqualTo("FF_101/2023-10-17");
		assertThat(invoice.getPayeeFinancialAccount().getFinancialInstitutionBranch()).isNull();
		assertThat(invoice.getPayeeFinancialAccount().getCurrency()).isNull();
		assertThat(invoice.getPayeeFinancialAccount().getId()).isEqualTo("RO48BTRL00501202K65277XX-RON");
		assertThat(invoice.getPayeeFinancialAccount().getName()).isEqualTo("SC LINIC SRL");
		assertThat(invoice.getPrepaidAmount()).isEqualByComparingTo(new BigDecimal("1000"));
		assertThat(invoice.getTaxAmount()).isEqualByComparingTo(new BigDecimal("722"));
		assertThat(invoice.getTaxCurrencyCode()).isEqualTo("RON");
		assertThat(invoice.getTaxExclusiveAmount()).isEqualByComparingTo(new BigDecimal("3800"));
		assertThat(invoice.getTaxInclusiveAmount()).isEqualByComparingTo(new BigDecimal("4522"));
		assertThat(invoice.getTaxSubtotals()).hasSize(1);
		assertThat(invoice.getTaxSubtotals().get(0).getTaxableAmount()).isEqualByComparingTo(new BigDecimal("3800"));
		assertThat(invoice.getTaxSubtotals().get(0).getTaxAmount()).isEqualByComparingTo(new BigDecimal("722"));
		assertThat(invoice.getTaxSubtotals().get(0).getTaxCategory().getCode()).isEqualTo("S");
		assertThat(invoice.getTaxSubtotals().get(0).getTaxCategory().getPercent()).isEqualByComparingTo(new BigDecimal("0.19"));
		assertThat(invoice.getTaxSubtotals().get(0).getTaxCategory().getTaxExemptionReason()).isNull();
		assertThat(invoice.getTaxSubtotals().get(0).getTaxCategory().getTaxScheme()).isEqualTo("VAT");
		assertThat(invoice.getPayeeParty()).isNull();
		
		assertThat(invoice.getAccountingSupplier().getBusinessName()).isNull();
		assertThat(invoice.getAccountingSupplier().getContactName()).isEqualTo("Groza Danut");
		assertThat(invoice.getAccountingSupplier().getElectronicMail()).isEqualTo("colibridepot@gmail.com, sclinicsrl@gmail.com");
		assertThat(invoice.getAccountingSupplier().getPostalAddress().getCity()).isEqualTo("MARGINE");
		assertThat(invoice.getAccountingSupplier().getPostalAddress().getCountry()).isEqualTo("RO");
		assertThat(invoice.getAccountingSupplier().getPostalAddress().getCountrySubentity()).isEqualTo("RO-BH");
		assertThat(invoice.getAccountingSupplier().getPostalAddress().getPostalZone()).isNullOrEmpty();
		assertThat(invoice.getAccountingSupplier().getPostalAddress().getPrimaryLine()).isEqualTo("Str Principala nr 218A");
		assertThat(invoice.getAccountingSupplier().getRegistrationName()).isEqualTo("SC LINIC SRL");
		assertThat(invoice.getAccountingSupplier().getRegistrationId()).isEqualTo("J05/1111/2002");
		assertThat(invoice.getAccountingSupplier().getCompanyLegalForm()).isEqualTo("Capital social 100,000.00 RON");
		assertThat(invoice.getAccountingSupplier().getTaxId()).isEqualTo("RO14998343");
		assertThat(invoice.getAccountingSupplier().getTelephone()).isEqualTo("Colibri - 0787577227, Linic - 0259362437");
		
		assertThat(invoice.getAccountingCustomer().getBusinessName()).isNull();
		assertThat(invoice.getAccountingCustomer().getContactName()).isEqualTo("Client");
		assertThat(invoice.getAccountingCustomer().getElectronicMail()).isEqualTo("client@yahoo.com");
		assertThat(invoice.getAccountingCustomer().getPostalAddress().getCity()).isEqualTo("Chet");
		assertThat(invoice.getAccountingCustomer().getPostalAddress().getCountry()).isEqualTo("RO");
		assertThat(invoice.getAccountingCustomer().getPostalAddress().getCountrySubentity()).isEqualTo("RO-BH");
		assertThat(invoice.getAccountingCustomer().getPostalAddress().getPostalZone()).isNullOrEmpty();
		assertThat(invoice.getAccountingCustomer().getPostalAddress().getPrimaryLine()).isEqualTo("Principala");
		assertThat(invoice.getAccountingCustomer().getRegistrationName()).isEqualTo("CLIENT INC RO");
		assertThat(invoice.getAccountingCustomer().getRegistrationId()).isEqualTo("J40/1251/2023");
		assertThat(invoice.getAccountingCustomer().getCompanyLegalForm()).isNull();
		assertThat(invoice.getAccountingCustomer().getTelephone()).isEqualTo("0745613215");
		assertThat(invoice.getAccountingCustomer().getTaxId()).isEqualTo("12345678");
		
		assertThat(invoice.getLines()).hasSize(1);
		assertThat(invoice.getLines().get(0).getAllowanceCharges()).isNullOrEmpty();
		assertThat(invoice.getLines().get(0).getBuyersItemIdentification()).isNull();
		assertThat(invoice.getLines().get(0).getClassifiedTaxCategory().getCode()).isEqualTo("S");
		assertThat(invoice.getLines().get(0).getClassifiedTaxCategory().getPercent()).isEqualTo(new BigDecimal("0.19"));
		assertThat(invoice.getLines().get(0).getClassifiedTaxCategory().getTaxExemptionReason()).isNull();
		assertThat(invoice.getLines().get(0).getClassifiedTaxCategory().getTaxScheme()).isEqualTo("VAT");
		assertThat(invoice.getLines().get(0).getId()).isEqualTo(1L);
		assertThat(invoice.getLines().get(0).getLineExtensionAmount()).isEqualByComparingTo(new BigDecimal("3800"));
		assertThat(invoice.getLines().get(0).getName()).isEqualTo("MATERIALE 40KG");
		assertThat(invoice.getLines().get(0).getNote()).isNull();
		assertThat(invoice.getLines().get(0).getPrice()).isEqualByComparingTo(new BigDecimal("38"));
		assertThat(invoice.getLines().get(0).getBaseQuantity()).isEqualByComparingTo(new BigDecimal("1"));
		assertThat(invoice.getLines().get(0).getQuantity()).isEqualByComparingTo(new BigDecimal("100"));
		assertThat(invoice.getLines().get(0).getSellersItemIdentification()).isEqualTo("59");
		assertThat(invoice.getLines().get(0).getTaxAmount()).isEqualByComparingTo(new BigDecimal("722"));
		assertThat(invoice.getLines().get(0).getUom()).isEqualTo("C62");
	}
	
	@Test
	public void whenAmountIs05AndTva01_thenTvaPercentShouldBe19() {
		// given
		final AccountingDocument accDoc = new AccountingDocument();
		final Company company = new Company();
		company.setId(1);
		accDoc.setCompany(company);
		accDoc.setId(1L);
		accDoc.setTipDoc(TipDoc.VANZARE);
		accDoc.setDoc(AccountingDocument.FACTURA_NAME);
		final Operatiune op = new Operatiune();
		op.setPretVanzareUnitarCuTVA(new BigDecimal("0.6"));
		op.setValoareVanzareFaraTVA(new BigDecimal("0.5"));
		op.setValoareVanzareTVA(new BigDecimal("0.1"));
		op.setCantitate(BigDecimal.ONE);
		accDoc.getOperatiuni().add(op);
		when(accDocRepo.findById(1L)).thenReturn(Optional.of(accDoc));
		
		// when
		final Invoice invoice = billingService.getInvoice(1L);
		
		// then
		assertThat(invoice.getId()).isEqualTo(1);
		assertThat(invoice.getLineExtensionAmount()).isEqualByComparingTo(new BigDecimal("0.5"));
		assertThat(invoice.getPayableAmount()).isEqualByComparingTo(new BigDecimal("0.6"));
		assertThat(invoice.getTaxAmount()).isEqualByComparingTo(new BigDecimal("0.1"));
		assertThat(invoice.getTaxExclusiveAmount()).isEqualByComparingTo(new BigDecimal("0.5"));
		assertThat(invoice.getTaxInclusiveAmount()).isEqualByComparingTo(new BigDecimal("0.6"));
		assertThat(invoice.getTaxSubtotals()).hasSize(1);
		assertThat(invoice.getTaxSubtotals().get(0).getTaxableAmount()).isEqualByComparingTo(new BigDecimal("0.5"));
		assertThat(invoice.getTaxSubtotals().get(0).getTaxAmount()).isEqualByComparingTo(new BigDecimal("0.1"));
		assertThat(invoice.getTaxSubtotals().get(0).getTaxCategory().getCode()).isEqualTo("S");
		assertThat(invoice.getTaxSubtotals().get(0).getTaxCategory().getPercent()).isEqualByComparingTo(new BigDecimal("0.19"));
		assertThat(invoice.getTaxSubtotals().get(0).getTaxCategory().getTaxExemptionReason()).isNull();
		assertThat(invoice.getTaxSubtotals().get(0).getTaxCategory().getTaxScheme()).isEqualTo("VAT");
		
		assertThat(invoice.getLines()).hasSize(1);
		assertThat(invoice.getLines().get(0).getClassifiedTaxCategory().getCode()).isEqualTo("S");
		assertThat(invoice.getLines().get(0).getClassifiedTaxCategory().getPercent()).isEqualTo(new BigDecimal("0.19"));
		assertThat(invoice.getLines().get(0).getClassifiedTaxCategory().getTaxExemptionReason()).isNull();
		assertThat(invoice.getLines().get(0).getClassifiedTaxCategory().getTaxScheme()).isEqualTo("VAT");
		assertThat(invoice.getLines().get(0).getLineExtensionAmount()).isEqualByComparingTo(new BigDecimal("0.5"));
		assertThat(invoice.getLines().get(0).getPrice()).isEqualByComparingTo(new BigDecimal("0.5"));
		assertThat(invoice.getLines().get(0).getQuantity()).isEqualByComparingTo(new BigDecimal("1"));
		assertThat(invoice.getLines().get(0).getTaxAmount()).isEqualByComparingTo(new BigDecimal("0.1"));
	}
	
	@Test
	public void whenAccDocNotFound_thenThrowException() {
		// given
		when(accDocRepo.findById(1L)).thenReturn(Optional.empty());

		// when then
		assertThatThrownBy(() -> billingService.getInvoice(1L))
		.isInstanceOf(RuntimeException.class);
	}
	
	@Test
	public void whenAccDocIsNotFactura_thenThrowException() {
		// given
		final AccountingDocument accDoc = new AccountingDocument();
		final Company company = new Company();
		company.setId(1);
		accDoc.setCompany(company);
		accDoc.setId(1L);
		accDoc.setTipDoc(TipDoc.VANZARE);
		accDoc.setDoc(AccountingDocument.AVIZ_NAME);
		when(accDocRepo.findById(1L)).thenReturn(Optional.of(accDoc));

		// when then
		assertThatThrownBy(() -> billingService.getInvoice(1L))
		.isInstanceOf(RuntimeException.class)
		.hasMessage("Documentul trebuie sa fie FACTURA!");
	}
	
	@Test
	public void whenTipDocIsNotVanzare_thenThrowException() {
		// given
		final AccountingDocument accDoc = new AccountingDocument();
		final Company company = new Company();
		company.setId(1);
		accDoc.setCompany(company);
		accDoc.setId(1L);
		accDoc.setDoc(AccountingDocument.FACTURA_NAME);
		accDoc.setTipDoc(TipDoc.CUMPARARE);
		when(accDocRepo.findById(1L)).thenReturn(Optional.of(accDoc));

		// when then
		assertThatThrownBy(() -> billingService.getInvoice(1L))
		.isInstanceOf(RuntimeException.class)
		.hasMessage("TipDoc trebuie sa fie VANZARE!");
	}
}
