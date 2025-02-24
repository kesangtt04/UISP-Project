package UISP.service;

import UISP.domain.Role;
import UISP.domain.User;
import UISP.domain.response.UserDTO;
import UISP.repository.RoleRepository;
import UISP.repository.UserRepositoy;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {
    private final UserRepositoy userRepositoy;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserService(UserRepositoy userRepositoy, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepositoy = userRepositoy;
        this.passwordEncoder=passwordEncoder;
        this.roleRepository=roleRepository;
    }
    public User findByEmail(String email) {
        return this.userRepositoy.findByEmail(email);
    }
    public User Save(User user) {
        return this.userRepositoy.save(user);
    }
    @Transactional
    public UserDTO CreateUser(User user) {
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setAddress(user.getAddress());
        userDTO.setFullname(user.getFullname());

        this.userRepositoy.save(user);
        if(user.getRole()!=null){
            Optional<Role> role=this.roleRepository.findById(user.getRole().getId());
            if(role.isPresent())
            {
                Role role1=role.get();
                userDTO.setRoleName(role1.getRoleName());
            }
        }else{
            userDTO.setRoleName("USER");
        }

        return userDTO;
    }
    public UserDTO UserToDTO(User user) {
        UserDTO userDTO=new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setFullname(user.getFullname());
        userDTO.setAddress(user.getAddress());
        if(user.getRole()!=null){
            userDTO.setRoleName(user.getRole().getRoleName());
        }

        return userDTO;
    }
    public void updateUserToken(String refresh_token, String email){
        User user=this.findByEmail(email);
        user.setRefreshToken(refresh_token);
        this.userRepositoy.save(user);
    }
    public User getUserByRefreshTokenAndEmail(String email,String refreshToken)
    {
        return this.userRepositoy.findByEmailAndRefreshToken(email,refreshToken);
    }
}
