package ro.linic.cloud.service;

import java.time.LocalDate;
import java.util.List;

import ro.linic.cloud.entity.AccountingDocument.BancaLoad;
import ro.linic.cloud.entity.AccountingDocument.CasaLoad;
import ro.linic.cloud.entity.AccountingDocument.ContaLoad;
import ro.linic.cloud.entity.AccountingDocument.DocumentTypesLoad;
import ro.linic.cloud.entity.AccountingDocument.RPZLoad;
import ro.linic.cloud.entity.Document;
import ro.linic.cloud.entity.Document.TipDoc;

public interface LegacyService {
	public List<Document> filteredDocuments(final Integer gestiuneId, final Long partnerId, final TipDoc tipDoc, final LocalDate from,
			final LocalDate to, final RPZLoad rpzLoad, final CasaLoad casaLoad, final BancaLoad bancaLoad, final Integer contBancarId,
			final DocumentTypesLoad documentTypes,/* final CoveredDocsLoad coveredLoad, */ final Boolean shouldTransport,
			final Integer userId, final ContaLoad contaLoad, final LocalDate transportFrom, final LocalDate transportTo);
}
