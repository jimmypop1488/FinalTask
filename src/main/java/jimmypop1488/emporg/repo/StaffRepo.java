package jimmypop1488.emporg.repo;

import jimmypop1488.emporg.model.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StaffRepo extends JpaRepository<Staff, UUID> {
    Optional<Staff> findById(UUID staffid);
    Optional<Staff> findByName(String name);
}
