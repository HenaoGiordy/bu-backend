package com.univalle.bubackend.services;


import com.univalle.bubackend.DTOs.auth.*;
import com.univalle.bubackend.exceptions.change_password.UserNotFound;
import com.univalle.bubackend.exceptions.resetpassword.AlreadyLinkHasBeenCreated;
import com.univalle.bubackend.exceptions.resetpassword.PasswordDoesNotMatch;
import com.univalle.bubackend.exceptions.resetpassword.TokenExpired;
import com.univalle.bubackend.exceptions.resetpassword.TokenNotFound;
import com.univalle.bubackend.models.PasswordResetToken;
import com.univalle.bubackend.models.UserEntity;
import com.univalle.bubackend.repository.PasswordResetTokenRepositoy;
import com.univalle.bubackend.repository.UserEntityRepository;
import com.univalle.bubackend.security.utils.JwtUtils;
import com.univalle.bubackend.services.email.EmailServiceImpl;
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

import java.util.*;


@Service
@AllArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private UserEntityRepository userEntityRepository;

    private JwtUtils jwtUtils;

    private PasswordEncoder passwordEncoder;

    private EmailServiceImpl emailServiceImpl;

    private PasswordResetTokenRepositoy passwordResetTokenRepositoy;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userEntityRepository.findByUsername(username)
                .orElseThrow(() -> new PasswordDoesNotMatch("Usuario o contraseña incorrectas"));

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
            throw new PasswordDoesNotMatch("Usuario o contraseña incorrectas");
        }
        if(!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new PasswordDoesNotMatch("Usuario o contraseña incorrectas");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    public VerifyResponse verifyToken(VerifyRequest verifyRequest){
        String validToken = jwtUtils.validateToken(verifyRequest.token()).getToken();
        return new VerifyResponse("Verified", validToken);
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

    public SendResetPasswordResponse sendResetPassword(SendResetPasswordRequest sendResetPasswordRequest){
        Optional<UserEntity> userOp = userEntityRepository.findByEmail(sendResetPasswordRequest.email());
        UserEntity usuario = userOp.orElseThrow(() -> new UserNotFound("Usuario no encontrado"));

        Optional<PasswordResetToken> passwordResetTokenOp = passwordResetTokenRepositoy.findByUser(usuario);

        if(passwordResetTokenOp.isPresent()){
            throw new AlreadyLinkHasBeenCreated("Ya se ha enviado un link a esta dirección");
        }

        String token = UUID.randomUUID().toString();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 8);
        Date expirationDate = calendar.getTime();

        passwordResetTokenRepositoy.save(PasswordResetToken.builder()
                .user(usuario)
                .token(token)
                .expiryDate(expirationDate)
                .build());

        emailServiceImpl.sendPasswordResetEmail(sendResetPasswordRequest.email(), token);
        return new SendResetPasswordResponse("Se envió un correo con un link para reestabler la contraseña", sendResetPasswordRequest.email());
    }

    public ResetPasswordResponse resetPassword(ResetPasswordRequest resetPasswordRequest, String token) {
        Optional<PasswordResetToken> tokenOpt = passwordResetTokenRepositoy.findByToken(token);

        PasswordResetToken passwordResetToken = tokenOpt.orElseThrow(() -> new TokenNotFound("Token Inválido"));

        if (!passwordResetToken.getUsedToken() && !passwordResetToken.getExpiryDate().before(Calendar.getInstance().getTime())) {

            Optional<UserEntity> userOp = userEntityRepository.findById(passwordResetToken.getUser().getId());
            UserEntity usuario = userOp.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

            if(resetPasswordRequest.password().equals(resetPasswordRequest.passwordConfirmation())){
                usuario.setPassword(passwordEncoder.encode(resetPasswordRequest.password()));
                passwordResetTokenRepositoy.delete(passwordResetToken);
                return new ResetPasswordResponse("Se ha cambiado la contraseña con exito");
            }else {
                throw new PasswordDoesNotMatch("Las contraseñas no coinciden");
            }

        }else {
            throw new TokenExpired("El token para cambiar la contraseña ya expiró");
        }
    }
}
