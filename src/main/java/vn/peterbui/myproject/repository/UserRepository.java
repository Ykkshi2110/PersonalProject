package vn.peterbui.myproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import vn.peterbui.myproject.domain.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User>{
    boolean existsByEmail (String email);
    User getUserByEmail(String email);
    User findByRefreshTokenAndEmail(String token, String email);
}