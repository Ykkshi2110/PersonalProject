package vn.peterbui.myproject.convert;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import vn.peterbui.myproject.domain.User;
import vn.peterbui.myproject.domain.dto.RoleDTO;
import vn.peterbui.myproject.domain.dto.UserDTO;

@Component
public class ConvertUtils {
    @SuppressWarnings("unused")
    private final ModelMapper modelMapper;

    public ConvertUtils(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public UserDTO convertToDto(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setAddress(user.getAddress());
        userDTO.setAvatar(user.getAvatar());
        userDTO.setEmail(user.getEmail());
        userDTO.setFullName(user.getFullName());
        userDTO.setPhone(user.getPhone());

        // Set roleDTO
        // Register người dùng không nhập Role vào 
        if (user.getRole() != null) {
            RoleDTO roleDTO = new RoleDTO();
            roleDTO.setId(user.getRole().getId());
            roleDTO.setName(user.getRole().getName());
            userDTO.setRole(roleDTO);
        }
        return userDTO;
    }
}
