package vn.peterbui.myproject.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.peterbui.myproject.domain.Role;
import vn.peterbui.myproject.type.RoleType;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleType name); 
    
}
