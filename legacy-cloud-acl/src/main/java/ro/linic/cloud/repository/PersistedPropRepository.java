package ro.linic.cloud.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import ro.linic.cloud.entity.PersistedProp;

public interface PersistedPropRepository extends CrudRepository<PersistedProp, Integer> {
	Optional<PersistedProp> findByCompanyIdAndKeyIgnoreCase(@Param("companyId") int companyId, @Param("key") String key);
	List<PersistedProp> findByCompanyId(@Param("companyId") int companyId);
}