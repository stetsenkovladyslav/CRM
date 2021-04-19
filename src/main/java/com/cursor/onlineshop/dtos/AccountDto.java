package com.cursor.onlineshop.dtos;

import com.cursor.onlineshop.entities.user.UserPermission;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {
    private String accountId;
    private String username;
    private String password;
    private String email;
    private Set<UserPermission> permissions;
}