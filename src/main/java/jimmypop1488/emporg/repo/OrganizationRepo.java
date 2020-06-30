package jimmypop1488.emporg.repo;

import jimmypop1488.emporg.model.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OrganizationRepo extends JpaRepository<Organization, UUID> {
    Optional<Organization> findById(UUID orgid);
    Optional<Organization> findByName(String name);
}