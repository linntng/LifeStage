
package com.loop.lifestage.mapper;
import com.loop.lifestage.model.User;
import com.loop.lifestage.dto.UserDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toUserDTO(User user);
    User toUser(UserDTO userDTO);
}