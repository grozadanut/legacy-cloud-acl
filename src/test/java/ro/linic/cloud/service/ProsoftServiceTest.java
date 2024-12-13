package ro.linic.cloud.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ro.linic.cloud.entity.AccountingDocument;
import ro.linic.cloud.entity.AccountingDocument.BancaLoad;
import ro.linic.cloud.entity.AccountingDocument.CasaLoad;
import ro.linic.cloud.entity.AccountingDocument.ContaLoad;
import ro.linic.cloud.entity.AccountingDocument.DocumentTypesLoad;
import ro.linic.cloud.entity.AccountingDocument.RPZLoad;
import ro.linic.cloud.entity.Company;
import ro.linic.cloud.entity.Document.TipDoc;
import ro.linic.cloud.entity.Gestiune;
import ro.linic.cloud.entity.Operatiune;
import ro.linic.cloud.entity.Partner;

@ExtendWith(MockitoExtension.class)
public class ProsoftServiceTest {
	@Mock private LegacyService legacyService;
	@InjectMocks private ProsoftServiceImpl prosoftService;
	
	@Test
	public void shouldReturnEmptyXml_whenNoOutgInvoicesPresent() {
		// given
		when(legacyService.filteredDocuments(null, null, TipDoc.VANZARE, LocalDate.now(), LocalDate.now(), RPZLoad.DOAR_RPZ, CasaLoad.INDIFERENT,
				BancaLoad.INDIFERENT, null, DocumentTypesLoad.FARA_DISCOUNTURI, null, null, ContaLoad.INDIFERENT, null, null))
		.thenReturn(List.of());
		
		// when
		final String outgXml = prosoftService.outgInvoices_Xml(LocalDate.now(), LocalDate.now());
		
		// then
		assertThat(outgXml).isEqualToIgnoringWhitespace("""
				<?xml version="1.0" standalone="yes"?>
				<SetDate>
				</SetDate>""");
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void shouldReturnXml_whenOutgInvoicesPresent() {
		// given
		final AccountingDocument invoice1 = new AccountingDocument();
		final Company company = new Company();
		company.setId(1);
		company.setName("LINIC SRL");
		invoice1.setCompany(company);
		invoice1.setId(1L);
		invoice1.setDataDoc(LocalDateTime.of(2023, 10, 17, 13, 13));
		invoice1.setDoc(AccountingDocument.FACTURA_NAME);
		invoice1.setNrDoc("101");
		invoice1.setScadenta(LocalDate.of(2023, 11, 16));
		invoice1.setTipDoc(TipDoc.VANZARE);
		
		final Gestiune linicGest = new Gestiune();
		linicGest.setImportName("L1");
		linicGest.setName("Linic");
		invoice1.setGestiune(linicGest);
		
		final Partner customer = new Partner();
		customer.setName("CLIENT INC RO");
		customer.setCodFiscal("12345678");
		customer.setRegCom("J40/1251/2023");;
		invoice1.setPartner(customer);
		
		final Gestiune colibriGest = new Gestiune();
		colibriGest.setImportName("L2");
		colibriGest.setName("Colibri");
		
		final Operatiune op = new Operatiune();
		op.setId(1L);
		op.setCategorie("MARFA");
		op.setBarcode("59");
		op.setCantitate(new BigDecimal("100"));
		op.setName("MATERIALE 40KG");
		op.setPretVanzareUnitarCuTVA(new BigDecimal("45.22"));
		op.setValoareVanzareFaraTVA(new BigDecimal("3800"));
		op.setValoareVanzareTVA(new BigDecimal("722"));
		op.setUom("BUC");
		op.setGestiune(colibriGest);
		op.setAccDoc(invoice1);
		op.setCompany(company);
		invoice1.getOperatiuni().add(op);
		
		when(legacyService.filteredDocuments(null, null, TipDoc.VANZARE, LocalDate.now(), LocalDate.now(), RPZLoad.DOAR_RPZ, CasaLoad.INDIFERENT,
				BancaLoad.INDIFERENT, null, DocumentTypesLoad.FARA_DISCOUNTURI, null, null, ContaLoad.INDIFERENT, null, null))
		.thenReturn(List.of(invoice1));
		
		// when
		final String outgXml = prosoftService.outgInvoices_Xml(LocalDate.now(), LocalDate.now());

		// then
		assertThat(outgXml).isXmlEqualTo("""
				<?xml version="1.0" standalone="yes"?>
				<SetDate>
				  <Livrari>
				    <MODUL>GESTIUNE-OUT</MODUL>
				    <Unitate>LINIC SRL</Unitate>
				    <gestiune>L2</gestiune>
				    <tert>CLIENT INC RO</tert>
				    <cif>12345678</cif>
				    <Nr>101</Nr>
				    <Data_>2023-10-17T13:13:00+03:00</Data_>
				    <moneda>LEI</moneda>
				    <Curs>1.0000</Curs>
				    <Stvai>0</Stvai>
				    <Cod>MARFA</Cod>
				    <cota_tva>19</cota_tva>
				    <Cod_mat>59</Cod_mat>
				    <Den_mat>MATERIALE 40KG</Den_mat>
				    <UM>BUC</UM>
				    <Cantitate>100</Cantitate>
				    <PretLiv>45.22</PretLiv>
				    <ValLivFTVA>3800</ValLivFTVA>
				    <ValLivTVA>722</ValLivTVA>
				  </Livrari>
				</SetDate>
				""");
	}
	
	@Test
	public void shouldReturnEmptyXml_whenNoIncInvoicesPresent() {
		// given
		when(legacyService.filteredDocuments(null, null, TipDoc.CUMPARARE, LocalDate.now(), LocalDate.now(), RPZLoad.DOAR_RPZ, CasaLoad.INDIFERENT,
				BancaLoad.INDIFERENT, null, DocumentTypesLoad.FARA_DISCOUNTURI, null, null, ContaLoad.INDIFERENT, null, null))
		.thenReturn(List.of());
		
		// when
		final String incXml = prosoftService.incInvoices_Xml(LocalDate.now(), LocalDate.now());
		
		// then
		assertThat(incXml).isEqualToIgnoringWhitespace("""
				<?xml version="1.0" standalone="yes"?>
				<SetDate>
				</SetDate>""");
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void shouldReturnXml_whenIncInvoicesPresent() {
		// given
		final AccountingDocument invoice1 = new AccountingDocument();
		final Company company = new Company();
		company.setId(1);
		company.setName("LINIC SRL");
		invoice1.setCompany(company);
		invoice1.setId(1L);
		invoice1.setDataDoc(LocalDateTime.of(2023, 10, 17, 13, 13));
		invoice1.setDoc(AccountingDocument.FACTURA_NAME);
		invoice1.setNrDoc("101");
		invoice1.setScadenta(LocalDate.of(2023, 11, 16));
		invoice1.setTipDoc(TipDoc.CUMPARARE);
		
		final Gestiune linicGest = new Gestiune();
		linicGest.setImportName("L1");
		linicGest.setName("Linic");
		invoice1.setGestiune(linicGest);
		
		final Partner supplier = new Partner();
		supplier.setName("SUPPLIER INC RO");
		supplier.setCodFiscal("12345678");
		supplier.setRegCom("J40/1251/2023");;
		invoice1.setPartner(supplier);
		
		final Gestiune colibriGest = new Gestiune();
		colibriGest.setImportName("L2");
		colibriGest.setName("Colibri");
		
		final Operatiune op = new Operatiune();
		op.setId(1L);
		op.setCategorie("MARFA");
		op.setBarcode("59");
		op.setCantitate(new BigDecimal("100"));
		op.setName("MATERIALE 40KG");
		op.setPretUnitarAchizitieFaraTVA(new BigDecimal("20"));
		op.setValoareAchizitieFaraTVA(new BigDecimal("2000"));
		op.setValoareAchizitieTVA(new BigDecimal("380"));
		op.setPretVanzareUnitarCuTVA(new BigDecimal("45.22"));
		op.setValoareVanzareFaraTVA(new BigDecimal("3800"));
		op.setValoareVanzareTVA(new BigDecimal("722"));
		op.setUom("BUC");
		op.setGestiune(colibriGest);
		op.setAccDoc(invoice1);
		op.setCompany(company);
		invoice1.getOperatiuni().add(op);
		
		when(legacyService.filteredDocuments(null, null, TipDoc.CUMPARARE, LocalDate.now(), LocalDate.now(), RPZLoad.DOAR_RPZ, CasaLoad.INDIFERENT,
				BancaLoad.INDIFERENT, null, DocumentTypesLoad.FARA_DISCOUNTURI, null, null, ContaLoad.INDIFERENT, null, null))
		.thenReturn(List.of(invoice1));
		
		// when
		final String incXml = prosoftService.incInvoices_Xml(LocalDate.now(), LocalDate.now());

		// then
		assertThat(incXml).isXmlEqualTo("""
				<?xml version="1.0" standalone="yes"?>
				<SetDate>
				  <Achizitii>
				    <MODUL>GESTIUNE-IN</MODUL>
				    <Unitate>LINIC SRL</Unitate>
				    <gestiune>L2</gestiune>
				    <tert>SUPPLIER INC RO</tert>
				    <cif>12345678</cif>
				    <Nr>101</Nr>
				    <Data_>2023-10-17T13:13:00+03:00</Data_>
				    <moneda>LEI</moneda>
				    <Curs>1.0000</Curs>
				    <Stvai>0</Stvai>
				    <Cod>MARFA</Cod>
				    <cota_tva>19</cota_tva>
				    <Cod_mat>59</Cod_mat>
				    <Den_mat>MATERIALE 40KG</Den_mat>
				    <UM>BUC</UM>
				    <Cantitate>100</Cantitate>
				    <Pret>20</Pret>
				    <ValFTVA>2000</ValFTVA>
				    <ValTVA>380</ValTVA>
				    <PretLiv>45.22</PretLiv>
				    <ValLivFTVA>3800</ValLivFTVA>
				    <ValLivTVA>722</ValLivTVA>
				  </Achizitii>
				</SetDate>
				""");
	}
}
