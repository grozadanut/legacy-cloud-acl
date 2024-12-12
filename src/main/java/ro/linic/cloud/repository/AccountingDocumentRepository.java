package ro.linic.cloud.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import ro.linic.cloud.entity.AccountingDocument;

public interface AccountingDocumentRepository extends CrudRepository<AccountingDocument, Long> {
	List<AccountingDocument> findByCompanyIdAndNrDoc(@Param("companyId") int companyId, @Param("nrDoc") String nrDoc);
}