package vn.peterbui.myproject.service;

import org.springframework.stereotype.Service;
import vn.peterbui.myproject.domain.Role;
import vn.peterbui.myproject.repository.RoleRepository;
import vn.peterbui.myproject.type.RoleType;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService (RoleRepository roleRepository){
        this.roleRepository = roleRepository;
    }

    public Role findRoleByName(RoleType name){
        return this.roleRepository.findByName(name).orElse(null);
    }
}
