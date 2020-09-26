package az.maqa.microservices.service.impl;

import az.maqa.microservices.dto.UserDTO;
import az.maqa.microservices.entity.User;
import az.maqa.microservices.repository.UserRepository;
import az.maqa.microservices.request.RequestUser;
import az.maqa.microservices.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder passwordEncoder;


    @Override
    public UserDTO createUser(RequestUser requestUser) {
        UserDTO userDetails = modelMapper.map(requestUser, UserDTO.class);

        userDetails.setUserId(UUID.randomUUID().toString());
        userDetails.setEncryptedPassword(passwordEncoder.encode(userDetails.getPassword()));

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        User userEntity = modelMapper.map(userDetails, User.class);

        userRepository.save(userEntity);

        UserDTO returnValue = modelMapper.map(userEntity, UserDTO.class);

        return returnValue;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(username);

        if (user == null) throw new UsernameNotFoundException(username);

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getEncryptedPassword(),
                true, true, true, true, new ArrayList<>());
    }


    @Override
    public UserDTO getUserDetailsByEmail(String email) {
        User user = userRepository.findUserByEmail(email);

        if (user == null) throw new UsernameNotFoundException(email);

        return modelMapper.map(user, UserDTO.class);
    }


}
