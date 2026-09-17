package gr.hua.citizen_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import gr.hua.citizen_service.model.PensionApplication;

public interface PensionApplicationRepository extends JpaRepository<PensionApplication, Long> {

    List<PensionApplication> findByCitizenUsername(String citizenUsername);
}