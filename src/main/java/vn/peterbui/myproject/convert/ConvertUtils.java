package vn.peterbui.myproject.convert;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import vn.peterbui.myproject.domain.User;
import vn.peterbui.myproject.domain.response.ResRoleDTO;
import vn.peterbui.myproject.domain.response.ResUserDTO;

@Component
public class ConvertUtils {
    @SuppressWarnings("unused")
    private final ModelMapper modelMapper;

    public ConvertUtils(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ResUserDTO convertToDto(User user) {
        ResUserDTO resUserDTO = new ResUserDTO();
        resUserDTO.setId(user.getId());
        resUserDTO.setAddress(user.getAddress());
        resUserDTO.setEmail(user.getEmail());
        resUserDTO.setName(user.getName());
        resUserDTO.setAge(user.getAge());
        resUserDTO.setGender(user.getGender());
        // Set roleDTO
        // Register người dùng không nhập Role vào 
        if (user.getRole() != null) {
            ResRoleDTO resRoleDTO = new ResRoleDTO();
            resRoleDTO.setId(user.getRole().getId());
            resRoleDTO.setName(user.getRole().getName());
            resUserDTO.setRole(resRoleDTO);
        }
        return resUserDTO;
    }
}
