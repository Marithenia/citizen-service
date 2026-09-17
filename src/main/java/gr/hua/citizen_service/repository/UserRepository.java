package gr.hua.citizen_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import gr.hua.citizen_service.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}