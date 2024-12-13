package ro.linic.cloud.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import ro.linic.cloud.entity.AccountingDocument;
import ro.linic.cloud.entity.AccountingDocument.BancaLoad;
import ro.linic.cloud.entity.AccountingDocument.CasaLoad;
import ro.linic.cloud.entity.AccountingDocument.ContaLoad;
import ro.linic.cloud.entity.AccountingDocument.DocumentTypesLoad;
import ro.linic.cloud.entity.AccountingDocument.RPZLoad;
import ro.linic.cloud.entity.Document;
import ro.linic.cloud.entity.Document.TipDoc;
import ro.linic.cloud.entity.Operatiune;

@Service
public class LegacyServiceImpl implements LegacyService {
	public static final String NEWLINE = System.lineSeparator();
	public static final LocalTime POSTGRES_MAX_TIME = LocalTime.of(23, 59, 59);
	
	@Autowired private EntityManager em;

	@Override
	public List<Document> filteredDocuments(Integer gestiuneId, Long partnerId, TipDoc tipDoc, LocalDate from,
			LocalDate to, RPZLoad rpzLoad, CasaLoad casaLoad, BancaLoad bancaLoad, Integer contBancarId,
			DocumentTypesLoad documentTypes, Boolean shouldTransport, Integer userId,
			ContaLoad contaLoad, LocalDate transportFrom, LocalDate transportTo) {
		final List<Document> result = new ArrayList<>();

		if (documentTypes.equals(DocumentTypesLoad.FARA_DISCOUNTURI) || documentTypes.equals(DocumentTypesLoad.INDIFERENT)) {
			result.addAll(loadAccDocs(gestiuneId, partnerId, tipDoc, from, to, rpzLoad, casaLoad, bancaLoad,
					contBancarId, contaLoad, shouldTransport, userId, transportFrom, transportTo));
		}

		return result;
	}
	
	protected List<AccountingDocument> loadAccDocs(final Integer gestiuneId, final Long partnerId, final TipDoc tipDoc,
			final LocalDate from, final LocalDate to, final RPZLoad rpzLoad, final CasaLoad casaLoad, final BancaLoad bancaLoad,
			final Integer contBancarId, final ContaLoad contaLoad, final Boolean shouldTransport,
			final Integer userId, final LocalDate transportFrom, final LocalDate transportTo)
	{
		// LOAD ACCOUNTING DOCUMENTS
		final StringBuilder sb = new StringBuilder();
        sb.append("SELECT new AccountingDocument(company, doc.id, partner, doc.name, doc.tipDoc, doc.total,\r\n" + 
        		"doc.totalTva, doc.scadenta, doc.dataDoc, operator, gestiune,\r\n" + 
        		"doc.doc, doc.nrDoc, doc.rpz, doc.regCasa, doc.dataReceptie,\r\n" + 
        		"doc.nrReceptie,\r\n" +
		        "doc.inContabilitate, doc.editable, doc.indicatii, doc.address, doc.phone)"
		        + " FROM AccountingDocument doc").append(NEWLINE);
        sb.append("LEFT JOIN doc."+Document.COMPANY_FIELD+" company").append(NEWLINE);
        sb.append("LEFT JOIN doc.partner partner").append(NEWLINE);
        sb.append("LEFT JOIN doc.operator operator").append(NEWLINE);
        sb.append("LEFT JOIN doc.gestiune gestiune WHERE").append(NEWLINE);
        sb.append("doc.dataDoc >= :qStart").append(NEWLINE);
        sb.append("AND doc.dataDoc <= :qEnd").append(NEWLINE);
        if (tipDoc != null) {
			sb.append("AND doc.tipDoc = :qTipDoc").append(NEWLINE);
		}
        if (partnerId != null) {
			sb.append("AND partner.id = :qPartnerId").append(NEWLINE);
		}
        if (gestiuneId != null) {
			sb.append("AND gestiune.id = :qGestiuneId").append(NEWLINE);
		}
        if (!rpzLoad.equals(RPZLoad.INDIFERENT)) {
			sb.append("AND doc.rpz = :qRpz").append(NEWLINE);
		}
        if (!casaLoad.equals(CasaLoad.INDIFERENT)) {
			sb.append("AND doc.regCasa = :qRegCasa").append(NEWLINE);
		}
        if (userId != null) {
			sb.append("AND operator.id = :qUserId").append(NEWLINE);
		}
        
//        switch (bancaLoad)
//        {
//        case DOAR_BANCA:
//        	if (contBancarId != null) {
//				sb.append("AND doc.contBancar.id = :qContBancarId").append(NEWLINE);
//			} else {
//				sb.append("AND doc.contBancar IS NOT NULL").append(NEWLINE);
//			}
//        	break;
//        case FARA_BANCA:
//        	if (contBancarId != null) {
//				sb.append("AND doc.contBancar.id != :qContBancarId").append(NEWLINE);
//			} else {
//				sb.append("AND doc.contBancar IS NULL").append(NEWLINE);
//			}
//        	break;
//        case INDIFERENT:
//        default:
//        	break;
//        }
        
        if (!contaLoad.equals(ContaLoad.INDIFERENT)) {
			sb.append("AND doc.inContabilitate = :qInContabilitate").append(NEWLINE);
		}
        
        final TypedQuery<AccountingDocument> query = em.createQuery(sb.toString(), AccountingDocument.class);
        query.setParameter("qStart", from.atStartOfDay());
        query.setParameter("qEnd", to.atTime(POSTGRES_MAX_TIME));
        if (tipDoc != null) {
			query.setParameter("qTipDoc", tipDoc);
		}
        if (partnerId != null) {
			query.setParameter("qPartnerId", partnerId);
		}
        if (gestiuneId != null) {
			query.setParameter("qGestiuneId", gestiuneId);
		}
        if (!rpzLoad.equals(RPZLoad.INDIFERENT)) {
			query.setParameter("qRpz", rpzLoad.equals(RPZLoad.DOAR_RPZ));
		}
        if (!casaLoad.equals(CasaLoad.INDIFERENT)) {
			query.setParameter("qRegCasa", casaLoad.equals(CasaLoad.DOAR_CASA));
		}
        if (userId != null) {
			query.setParameter("qUserId", userId);
		}
        
//        switch (bancaLoad)
//        {
//        case DOAR_BANCA:
//        	if (contBancarId != null) {
//				query.setParameter("qContBancarId", contBancarId);
//			}
//        	break;
//        case FARA_BANCA:
//        	if (contBancarId != null) {
//				query.setParameter("qContBancarId", contBancarId);
//			}
//        	break;
//        case INDIFERENT:
//        default:
//        	break;
//        }
        
        if (!contaLoad.equals(ContaLoad.INDIFERENT)) {
			query.setParameter("qInContabilitate", contaLoad.equals(ContaLoad.DOAR_CONTA));
		}
        
		final Map<Long, List<AccountingDocument>> accDocs = query.getResultStream()
				.collect(Collectors.groupingBy(AccountingDocument::getId));
		
		// LOAD AND FILL OPERATIONS INTO THE ACCOUNTING DOCUMENT
		fillAccDocsWithOperations(accDocs);
		
		// filter out covered docs
//		ImmutableMap<Long, BigDecimal> paysIdToConnectedTotal = ImmutableMap.of();
//		ImmutableMap<Long, BigDecimal> paidIdToConnectedTotal = ImmutableMap.of();
//		if (!coveredLoad.equals(CoveredDocsLoad.INDIFERENT) && !accDocs.isEmpty())
//        {
//			final StringBuilder paysCoveredSb = new StringBuilder();
//			paysCoveredSb.append("SELECT adm.id.paysId, SUM(adm.total) FROM AccountingDocumentMapping adm").append(NEWLINE)
//			.append("WHERE adm.id.paysId IN :qAccDocIds").append(NEWLINE)
//			.append("GROUP BY adm.id.paysId").append(NEWLINE);
//			final StringBuilder paidCoveredSb = new StringBuilder();
//			paidCoveredSb.append("SELECT adm.id.paidId, SUM(adm.total) FROM AccountingDocumentMapping adm").append(NEWLINE)
//			.append("WHERE adm.id.paidId IN :qAccDocIds").append(NEWLINE)
//			.append("GROUP BY adm.id.paidId").append(NEWLINE);
//					
//			final Query paysCoveredQuery = em.createQuery(paysCoveredSb.toString());
//			paysCoveredQuery.setParameter("qAccDocIds", accDocs.keySet());
//			final Stream<Object[]> paysResultS = paysCoveredQuery.getResultStream();
//			paysIdToConnectedTotal = paysResultS
//					.collect(toImmutableMap(obj -> (Long) obj[0], obj -> (BigDecimal) obj[1]));
//			
//			final Query paidCoveredQuery = em.createQuery(paidCoveredSb.toString());
//			paidCoveredQuery.setParameter("qAccDocIds", accDocs.keySet());
//			final Stream<Object[]> paidResultS = paidCoveredQuery.getResultStream();
//			paidIdToConnectedTotal = paidResultS
//					.collect(toImmutableMap(obj -> (Long) obj[0], obj -> (BigDecimal) obj[1]));
//			
//        }
//		final ImmutableMap<Long, BigDecimal> paysIdToConnectedTotalf = paysIdToConnectedTotal;
//		final ImmutableMap<Long, BigDecimal> paidIdToConnectedTotalf = paidIdToConnectedTotal;
//		final java.util.function.Predicate<AccountingDocument> coveredFilter = accDoc -> 
//		{
//			if (coveredLoad.equals(CoveredDocsLoad.INDIFERENT)) {
//				return true;
//			}
//			
//			final BigDecimal paysTotal = Optional.ofNullable(paysIdToConnectedTotalf.get(accDoc.getId()))
//					.orElse(BigDecimal.ZERO);
//			final BigDecimal paidTotal = Optional.ofNullable(paidIdToConnectedTotalf.get(accDoc.getId()))
//					.orElse(BigDecimal.ZERO);
//			final BigDecimal docTotal = accDoc.getTotal();
//			return coveredLoad.equals(CoveredDocsLoad.DOAR_COVERED) ?
//					// DOAR_COVERED -> connected total == doc total
//					(equal(docTotal, paysTotal) || equal(docTotal, paidTotal)) :
//						// FARA_COVERED -> connected total != doc total
//						(!equal(docTotal, paysTotal) && !equal(docTotal, paidTotal));
//		};

        return accDocs.values().stream()
        		.flatMap(List::stream)
//        		.filter(coveredFilter)
        		.sorted(Comparator.nullsFirst(Comparator.comparing(AccountingDocument::getDataDoc)))
        		.collect(Collectors.toList());
	}
	
	/**
	 * @param accDocs key - AccountingDocument::getId
	 */
	protected void fillAccDocsWithOperations(final Map<Long, List<AccountingDocument>> accDocs)
	{
		if (accDocs.isEmpty()) {
			return;
		}
		
		// LOAD AND FILL OPERATIONS INTO THE ACCOUNTING DOCUMENT
		final StringBuilder sb = new StringBuilder();
		sb.append("SELECT new Operatiune(company, op.id, gestiune, accDoc, op.tipOp, op.barcode, op.name,\r\n" + 
				"op.uom, op.categorie, op.rpz, op.cantitate, op.pretUnitarAchizitieFaraTVA,\r\n" + 
				"op.valoareAchizitieFaraTVA, op.valoareAchizitieTVA, op.pretVanzareUnitarCuTVA,\r\n" + 
				"op.valoareVanzareFaraTVA, op.valoareVanzareTVA, operator, op.dataOp,\r\n" + 
				"op.idx, op.shouldVerify) FROM Operatiune op").append(NEWLINE);
		sb.append("LEFT JOIN op.company company").append(NEWLINE);
		sb.append("LEFT JOIN op.gestiune gestiune").append(NEWLINE);
		sb.append("LEFT JOIN op.accDoc accDoc").append(NEWLINE);
		sb.append("LEFT JOIN op.operator operator").append(NEWLINE);
		sb.append("LEFT JOIN accDoc.partner partner WHERE").append(NEWLINE);
		sb.append("accDoc.id IN :qAccDocIds").append(NEWLINE);

		final TypedQuery<Operatiune> opQuery = em.createQuery(sb.toString(), Operatiune.class);
		final List<List<Long>> accDocIdsChunks = Lists.partition(new ArrayList<>(accDocs.keySet()), 16384);

		for (final List<Long> currentAccDocIds : accDocIdsChunks)
		{
			opQuery.setParameter("qAccDocIds", currentAccDocIds);
			final Map<Long, List<Operatiune>> currentOps = opQuery.getResultStream()
					.collect(Collectors.groupingBy(op -> op.getAccDoc().getId()));

			currentOps.entrySet().forEach(opEntry -> {
				final AccountingDocument accDoc = accDocs.get(opEntry.getKey()).get(0);
				accDoc.getOperatiuni().addAll(opEntry.getValue());
				accDoc.getOperatiuni().forEach(op -> op.setAccDoc(accDoc));
			});
		}
	}
}
