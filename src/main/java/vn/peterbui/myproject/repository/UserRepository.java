package vn.peterbui.myproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.peterbui.myproject.domain.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail (String email);
    User getUserByEmail(String email);
}