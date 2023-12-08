package com.utc.formut.configuration.security;

import com.utc.formut.web.models.User;
import com.utc.formut.web.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class AuthenticationService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public org.springframework.security.core.userdetails.User loadUserByUsername(String email) throws UsernameNotFoundException {
        User u = userRepository.findByMail(email).orElse(null);
        if (u == null) {
            throw new UsernameNotFoundException("Utilisateur non trouvé : " + email);
        }

        return createUser(u);
    }

    private org.springframework.security.core.userdetails.User createUser(User u) {
        return new org.springframework.security.core.userdetails.User(u.getMail(), u.getPassword(), createAuthorities(u));
    }

    private Collection<GrantedAuthority> createAuthorities(User u) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + u.getAuthaurization().toUpperCase()));
        return authorities;
    }
}
