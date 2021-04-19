package com.cursor.onlineshop.dtos;

import com.cursor.onlineshop.entities.user.UserPermission;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountDto {
    private String username;
    private String password;
    private String email;
    private Set<UserPermission> permissions;

    public CreateAccountDto(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
}
