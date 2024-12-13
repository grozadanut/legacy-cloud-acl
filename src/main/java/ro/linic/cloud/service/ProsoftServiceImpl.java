package ro.linic.cloud.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import lombok.extern.java.Log;
import ro.linic.cloud.entity.AccountingDocument;
import ro.linic.cloud.entity.AccountingDocument.BancaLoad;
import ro.linic.cloud.entity.AccountingDocument.CasaLoad;
import ro.linic.cloud.entity.AccountingDocument.ContaLoad;
import ro.linic.cloud.entity.AccountingDocument.DocumentTypesLoad;
import ro.linic.cloud.entity.AccountingDocument.RPZLoad;
import ro.linic.cloud.entity.Document.TipDoc;
import ro.linic.cloud.mapper.ProsoftMapper;

@Service
@Log
public class ProsoftServiceImpl implements ProsoftService {
	public static byte[] zipXmls(Map<String, String> filenameToXmls) {
		try {
			final ByteArrayOutputStream baos = new ByteArrayOutputStream();
			final ZipOutputStream zipOut = new ZipOutputStream(baos);
			
			for (final Entry<String, String> filenameToXml : filenameToXmls.entrySet()) {
				final ByteArrayInputStream bais = new ByteArrayInputStream(filenameToXml.getValue().getBytes());
				final ZipEntry zipEntry = new ZipEntry(filenameToXml.getKey()+".xml");
				zipOut.putNextEntry(zipEntry);
				
				final byte[] bytes = new byte[1024];
				int length;
				while((length = bais.read(bytes)) >= 0 ) {
					zipOut.write(bytes, 0, length);
				}
				bais.close();
			}
			
			zipOut.close();
			baos.close();
			return baos.toByteArray();
		} catch(final IOException e) {
			log.log(Level.SEVERE, "Zipping xmls failed", e);
			return null;
		}
	}
	
	@Autowired private LegacyService legacyService;
	
	@Override
	public byte[] invoices(LocalDate from, LocalDate to) {
		final String outgXml = outgInvoices_Xml(from, to);
		final String incXml = incInvoices_Xml(from, to);
		final String period = from.toString() + "_" + to.toString();
		return zipXmls(Map.of("iesiri_"+period, outgXml,
				"intrari_"+period, incXml));
	}

	@Override
	public String outgInvoices_Xml(LocalDate from, LocalDate to) {
		return toXml(legacyService.filteredDocuments(null, null, TipDoc.VANZARE, from, to, RPZLoad.DOAR_RPZ, 
				CasaLoad.INDIFERENT, BancaLoad.INDIFERENT, null, DocumentTypesLoad.FARA_DISCOUNTURI, null, null, ContaLoad.INDIFERENT, null, null)
				.stream()
				.filter(AccountingDocument.class::isInstance)
				.map(AccountingDocument.class::cast)
				.filter(accDoc -> accDoc.getDoc().equalsIgnoreCase(AccountingDocument.FACTURA_NAME))
				.flatMap(AccountingDocument::getOperatiuni_Stream)
				.map(ProsoftMapper.INSTANCE::toOutgLine)
				.collect(Collectors.toList()));
	}

	@Override
	public String incInvoices_Xml(LocalDate from, LocalDate to) {
		return toXml(legacyService.filteredDocuments(null, null, TipDoc.CUMPARARE, from, to, RPZLoad.DOAR_RPZ, 
				CasaLoad.INDIFERENT, BancaLoad.INDIFERENT, null, DocumentTypesLoad.FARA_DISCOUNTURI, null, null, ContaLoad.INDIFERENT, null, null)
				.stream()
				.filter(AccountingDocument.class::isInstance)
				.map(AccountingDocument.class::cast)
				.filter(accDoc -> accDoc.getDoc().equalsIgnoreCase(AccountingDocument.FACTURA_NAME))
				.flatMap(AccountingDocument::getOperatiuni_Stream)
				.map(ProsoftMapper.INSTANCE::toIncLine)
				.collect(Collectors.toList()));
	}
	
	private String toXml(List lines) {
		final StringBuilder resultXml = new StringBuilder();
		resultXml.append("<?xml version=\"1.0\" standalone=\"yes\"?>\r\n")
		.append("<SetDate>");
		
		try {
			final XmlMapper xmlMapper = new XmlMapper();
			for (final Object line : lines)
				resultXml.append(xmlMapper.writeValueAsString(line));
		} catch (final JsonProcessingException e) {
			log.log(Level.SEVERE, "marshal failed", e);
		}
		resultXml.append("</SetDate>");
		
		return resultXml.toString();
	}
}
