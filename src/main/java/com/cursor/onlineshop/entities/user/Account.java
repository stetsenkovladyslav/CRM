package com.cursor.onlineshop.entities.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "accounts")
public class Account implements UserDetails {
    @Id
    @Column(name = "account_id")
    private String accountId;
    private String username;
    private String password;
    private String email;
    @ElementCollection(targetClass = UserPermission.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<UserPermission> permissions;

    public Account(String username, String password, String email) {
        this.accountId = UUID.randomUUID().toString();
        this.username = username;
        this.password = password;
        this.email = email;
        this.permissions = Set.of(UserPermission.ROLE_USER);
    }

    public Account(String username, String password, String email, Set<UserPermission> permissions) {
        this.accountId = UUID.randomUUID().toString();
        this.username = username;
        this.password = password;
        this.email = email;
        this.permissions = permissions;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return permissions.stream().map(r -> new SimpleGrantedAuthority(r.name())).collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
