package vn.peterbui.myproject.config;

import org.modelmapper.Condition;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.peterbui.myproject.domain.Company;
import vn.peterbui.myproject.domain.Resume;
import vn.peterbui.myproject.domain.response.ResCreateUserDTO;
import vn.peterbui.myproject.domain.response.ResResumeDTO;
import vn.peterbui.myproject.domain.response.ResUpdateUserDTO;
import vn.peterbui.myproject.domain.response.ResUserDTO;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Tạo TypeMap để ánh xạ từ Company sang UserDTO.CompanyUser
        TypeMap<Company, ResUserDTO.CompanyUser> userDtoTypeMap = modelMapper.createTypeMap(Company.class, ResUserDTO.CompanyUser.class);
        userDtoTypeMap.addMappings(mapper -> {
            mapper.map(Company::getId, ResUserDTO.CompanyUser::setId);
            mapper.map(Company::getName, ResUserDTO.CompanyUser::setName);
        });

        TypeMap<Company, ResUpdateUserDTO.CompanyUser> updateUserDtoTypeMap = modelMapper.createTypeMap(Company.class, ResUpdateUserDTO.CompanyUser.class);

        updateUserDtoTypeMap.addMappings(mapper -> {
            mapper.map(Company::getId, ResUpdateUserDTO.CompanyUser::setId);
            mapper.map(Company::getName, ResUpdateUserDTO.CompanyUser::setName);
        });

        TypeMap<Company, ResCreateUserDTO.CompanyUser> createUserDtoTypeMap = modelMapper.createTypeMap(Company.class, ResCreateUserDTO.CompanyUser.class);

        createUserDtoTypeMap.addMappings(mapper -> {
            mapper.map(Company::getId, ResCreateUserDTO.CompanyUser::setId);
            mapper.map(Company::getName, ResCreateUserDTO.CompanyUser::setName);
        });
        return modelMapper;
    }
}

