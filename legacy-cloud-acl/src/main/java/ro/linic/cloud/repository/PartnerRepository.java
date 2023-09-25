package ro.linic.cloud.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import ro.linic.cloud.entity.Partner;

public interface PartnerRepository extends CrudRepository<Partner, Integer> {
	List<Partner> findByCompanyIdAndName(@Param("companyId") int companyId, @Param("name") String name);
}