package vn.peterbui.myproject.convert;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import vn.peterbui.myproject.domain.User;
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
        return userDTO;
    }
}
