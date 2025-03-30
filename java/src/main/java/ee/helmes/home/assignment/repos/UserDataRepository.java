package ee.helmes.home.assignment.repos;

import ee.helmes.home.assignment.models.UserData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDataRepository extends JpaRepository<UserData, Long> {
}
