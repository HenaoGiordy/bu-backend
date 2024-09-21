package com.univalle.bubackend.services;


import com.univalle.bubackend.DTOs.auth.AuthRequest;
import com.univalle.bubackend.DTOs.auth.AuthResponse;
import com.univalle.bubackend.DTOs.auth.RegisterRequest;
import com.univalle.bubackend.DTOs.auth.RegisterResponse;
import com.univalle.bubackend.models.UserEntity;
import com.univalle.bubackend.repository.UserEntityRepository;
import com.univalle.bubackend.security.utils.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;


@Service
@AllArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private UserEntityRepository userEntityRepository;

    private JwtUtils jwtUtils;

    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userEntityRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("No se encontró el usuario"));

        List<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<>();

        userEntity.getRoles().forEach(role -> grantedAuthorities
                .add(new SimpleGrantedAuthority("ROLE_".concat(role.getName().name()))));


        return new User(userEntity.getUsername(), userEntity.getPassword(), grantedAuthorities);
    }

    public AuthResponse login(AuthRequest request) {
        String username = request.username();
        String password = request.password();

        Authentication authentication = this.authenticate(username, password);

        String token = jwtUtils.createToken(authentication);

        return new AuthResponse(username,"Successful",token);
    }

    private Authentication authenticate(String username, String password) {
        UserDetails userDetails = loadUserByUsername(username);
        if(userDetails == null) {
            throw new UsernameNotFoundException("User or password is incorrect");
        }
        if(!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Wrong password");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    public RegisterResponse register(RegisterRequest register){
        String username = register.username();
        String password = register.password();
        userEntityRepository.save(UserEntity.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .build());
        return new RegisterResponse(username, "Usuario creado satisfactoriamente");
    }
}
