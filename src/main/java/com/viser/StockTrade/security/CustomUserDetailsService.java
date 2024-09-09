package com.viser.StockTrade.security;

import com.viser.StockTrade.entity.Role;
import com.viser.StockTrade.entity.User;
import com.viser.StockTrade.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserService userService;

    /**
     * Loads user-specific data given the username.
     *
     * This method is used by Spring Security to retrieve user details from a data source.
     * It fetches the {@link User} entity by its username, and if found, returns a {@link UserDetails} object
     * that includes the username, password, and authorities (roles). If the user is not found, it throws
     * a {@link UsernameNotFoundException}.
     *
     * @param username the username of the user to be retrieved
     * @return a {@link UserDetails} object containing the user's username, password, and roles
     * @throws UsernameNotFoundException if the user with the given username is not found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Username not found");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
    }

    /**
     * Maps a list of roles to a collection of {@link GrantedAuthority} objects.
     *
     * This method converts a list of {@link Role} entities into a collection of {@link GrantedAuthority}
     * objects, where each role is mapped to a {@link SimpleGrantedAuthority}. This is used by Spring Security
     * to handle role-based authorization by converting role names into authorities that are understood by the
     * security framework.
     *
     * @param roles a list of {@link Role} objects to be converted
     * @return a collection of {@link GrantedAuthority} objects corresponding to the provided roles
     */
    private Collection<GrantedAuthority> mapRolesToAuthorities(List<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }
}
