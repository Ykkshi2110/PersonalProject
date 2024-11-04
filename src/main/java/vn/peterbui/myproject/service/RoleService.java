package vn.peterbui.myproject.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import vn.peterbui.myproject.domain.response.Meta;
import vn.peterbui.myproject.domain.Permission;
import vn.peterbui.myproject.domain.Role;
import vn.peterbui.myproject.domain.response.ResultPaginationDTO;
import vn.peterbui.myproject.exception.IdInvalidException;
import vn.peterbui.myproject.repository.RoleRepository;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final PermissionService permissionService;

    private static final String ERROR_ROLE_EXISTS = "Role does not exists";
    public Role handleCreateRole(@Valid Role role) {
        if (this.roleRepository.existsByName(role.getName()))
            throw new IdInvalidException("Role name already exists");

        if (role.getPermissions() != null) {
            List<Long> reqPermissions = role.getPermissions().stream().map(Permission::getId)
                    .collect(Collectors.toList());
            List<Permission> dbPermissions = this.permissionService.findByIdAndCheckInvalid(reqPermissions);
            role.setPermissions(dbPermissions);
        }

        return this.roleRepository.save(role);
    }

    public ResultPaginationDTO handleFetchAllRole(Specification<Role> spec, Pageable pageable){
        Page<Role> rolePage = this.roleRepository.findAll(spec, pageable);
        Meta meta = new Meta();
        ResultPaginationDTO resultPaginationDTO = new ResultPaginationDTO();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(rolePage.getTotalPages());
        meta.setTotal(rolePage.getTotalElements());
        resultPaginationDTO.setMeta(meta);
        resultPaginationDTO.setResult(rolePage.getContent());
        return resultPaginationDTO;
    }

    public Role handleUpdateRole(Role role){
        Role currentRole = this.roleRepository.findById(role.getId()).orElseThrow(() -> new IdInvalidException(ERROR_ROLE_EXISTS));
        currentRole.setName(role.getName());
        currentRole.setDescription(role.getDescription());
        currentRole.setActive(role.isActive());
        if(role.getPermissions() != null){
            List<Long> reqPermissions = role.getPermissions().stream().map(id -> id.getId()).collect(Collectors.toList());
            List<Permission> dbPermissions = this.permissionService.findByIdAndCheckInvalid(reqPermissions);
            currentRole.setPermissions(dbPermissions);
        }
        return this.roleRepository.save(currentRole);
    }

    // Vì role là ownerside nên chỉ cần xóa là những liên quan Entity liên kết đều đc xóa trong permission_role
    public void handleDeleteRole(long id){
        Role currentRole = this.roleRepository.findById(id).orElseThrow(() -> new IdInvalidException(ERROR_ROLE_EXISTS));
        this.roleRepository.delete(currentRole);
    }

    public Role fetchRoleById(long id){
        return this.roleRepository.findById(id).orElseThrow(() -> new IdInvalidException(ERROR_ROLE_EXISTS));
    }

}
