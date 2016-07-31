package com.pinframework.demo;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserService {
	public static final UserService INSTANCE = new UserService();

	public final List<UserDTO> users = Arrays.asList(new UserDTO(1L, "firstName1", "lastName1"),
			new UserDTO(2L, "firstName2", "lastName2"));
	
	public Optional<UserDTO> find(Long id){
		return users.stream().filter(u -> Objects.equals(u.getId(), id)).findFirst();
	}
	public List<UserDTO> search(String firstName, String lastName){
		return users.stream().filter(u -> matches(u, firstName, lastName)).collect(Collectors.toList());
	}
	
	public boolean matches(UserDTO userDTO, String firstName, String lastName){
		if(firstName != null && firstName.equalsIgnoreCase(userDTO.getFirstName())){
			return true;
		}
		if(lastName != null && lastName.equalsIgnoreCase(userDTO.getLastName())){
			return true;
		}
		return false;
	}
}
