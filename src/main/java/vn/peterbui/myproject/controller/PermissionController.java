package vn.peterbui.myproject.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import vn.peterbui.myproject.convert.annotation.ApiMessage;
import vn.peterbui.myproject.domain.Permission;
import vn.peterbui.myproject.domain.dto.ResultPaginationDTO;
import vn.peterbui.myproject.service.PermissionService;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PermissionController {
    public final PermissionService permissionService;

    @PostMapping("/permissions/create")
    @ApiMessage("Create a permission")
    public ResponseEntity<Permission> handleCreatePermission(@RequestBody @Valid Permission permission){
        return ResponseEntity.status(HttpStatus.CREATED).body(this.permissionService.handleCreatPermission(permission));
    }

    @PutMapping("/permissions/update")
    @ApiMessage("Update a permission")
    public ResponseEntity<Permission> handleUpdatePermission(@RequestBody Permission permission){
        return ResponseEntity.status(HttpStatus.CREATED).body(this.permissionService.handleUpdatePermission(permission));
    }

    @GetMapping("/permissions")
    @ApiMessage("Fetch all permission")
    public ResponseEntity<ResultPaginationDTO> fetchAllPermission(@Filter Specification<Permission> spec, Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(this.permissionService.fetchAllPermision(spec, pageable));
    }

    @DeleteMapping("/permissions/delete/{id}")
    @ApiMessage("Delete a permission")
    public ResponseEntity<Void> handleDeletePermission(@PathVariable long id){
        this.permissionService.handleDeletePermission(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
