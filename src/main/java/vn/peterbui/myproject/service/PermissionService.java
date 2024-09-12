package vn.peterbui.myproject.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import vn.peterbui.myproject.domain.Meta;
import vn.peterbui.myproject.domain.Permission;
import vn.peterbui.myproject.domain.dto.ResultPaginationDTO;
import vn.peterbui.myproject.exception.PermissionAttributeExists;
import vn.peterbui.myproject.exception.PermissionDoesNotEx;
import vn.peterbui.myproject.repository.PermissionRepository;

@Service
@RequiredArgsConstructor
public class PermissionService {
    private final PermissionRepository permissionRepository;

    public boolean checkAttributeExist(String apiPath, String method, String module) {
        return this.permissionRepository.existsByApiPath(apiPath) && this.permissionRepository.existsByMethod(method)
                && this.permissionRepository.existsByModule(module);
    }

    public Permission handleCreatPermission(@Valid Permission permission) {
        if (this.checkAttributeExist(permission.getApiPath(), permission.getMethod(), permission.getModule()))
            throw new PermissionAttributeExists("Exception occurs...");
        return this.permissionRepository.save(permission);
    }

    public Permission handleUpdatePermission(Permission permission) {
        Permission currentPermission = this.permissionRepository.findById(permission.getId());
        if (currentPermission == null) {
            throw new PermissionDoesNotEx("Permission does not exists");
        }
        if (this.checkAttributeExist(permission.getApiPath(), permission.getMethod(), permission.getModule()))
            throw new PermissionAttributeExists("Exception occurs...");

        currentPermission.setName(permission.getName());
        currentPermission.setApiPath(permission.getApiPath());
        currentPermission.setMethod(permission.getMethod());
        currentPermission.setModule(permission.getModule());
        return this.permissionRepository.save(currentPermission);
    }

   public ResultPaginationDTO fetchAllPermision(Specification<Permission> spec, Pageable pageable){
        Page<Permission> permissionPage = this.permissionRepository.findAll(spec, pageable);
        Meta meta = new Meta();
        ResultPaginationDTO resultPaginationDTO = new ResultPaginationDTO();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(permissionPage.getTotalPages());
        meta.setTotal(permissionPage.getTotalElements());
        resultPaginationDTO.setMeta(meta);
        resultPaginationDTO.setResult(permissionPage.getContent());
        return resultPaginationDTO;
   }

    public List<Permission> findByIdAndCheckInvalid(List<Long> reqPermissions) {
        return reqPermissions.stream()
                .map(permissionId -> this.permissionRepository.findById(permissionId).orElse(null))
                .collect(Collectors.toList());
    }

    
}
