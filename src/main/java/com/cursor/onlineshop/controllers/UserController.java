package com.cursor.onlineshop.controllers;

import com.cursor.onlineshop.dtos.UserDto;
import com.cursor.onlineshop.entities.user.Account;
import com.cursor.onlineshop.entities.user.UserPermission;
import com.cursor.onlineshop.security.JwtUtils;
import com.cursor.onlineshop.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/accounts")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtTokenUtil;

    // add limit, offset and sort params
    @GetMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<List<UserDto>> getAllUsersInfo() {
        List<UserDto> usersDtos = userService.getAll();

        // usersDtos could not be null, check userService.getAll(), it could be empty
        // don't return NOT_FOUND here, return OK even if the list is empty
        return usersDtos != null
                ? new ResponseEntity<>(usersDtos, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/{accountId}")
    public ResponseEntity<UserDto> getUserInfo(@PathVariable(value = "accountId") String accountId) {
        UserDto requestedUserDto = userService.getUserInfoById(accountId);
        String requestedUserName = requestedUserDto.getUsername();

        // this check should be moved to separate methods
        Account requester = (Account) userService
                .loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if (requester.getUsername().equals(requestedUserName)
                || requester.getPermissions().contains(UserPermission.ROLE_ADMIN)) {
            return new ResponseEntity<>(requestedUserDto, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PutMapping(
            value = "/{accountId}",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> editUser(
            @PathVariable(value = "accountId") String accountId,
            @RequestBody UserDto editedUserDto
    ) {
        UserDto requestedUserDto = userService.getUserInfoById(accountId);


        // all security checks should be in another method
        Account requester = (Account) userService
                .loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if (requester.getUsername().equals(requestedUserDto.getUsername())) {
            modifyUserDto(accountId, editedUserDto, requestedUserDto);
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            editedUserDto.getUsername(), editedUserDto.getPassword()));
            String jwt = jwtTokenUtil.generateToken(
                    new Account(editedUserDto.getUsername(), editedUserDto.getPassword(),
                            editedUserDto.getEmail(), editedUserDto.getPermissions()));
            return ResponseEntity.ok(jwt);
        }
        if (requester.getPermissions().contains(UserPermission.ROLE_ADMIN)) {
            modifyUserDto(accountId, editedUserDto, requestedUserDto);
            // Don't add it "User is updated". If status 200, it means everything is fine
            return ResponseEntity.ok("User is updated");
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }


    // I would move this method to service class
    private void modifyUserDto(String accountId, UserDto editedUserDto, UserDto requestedUserDto) {
        editedUserDto.setAccountId(accountId);
        if (editedUserDto.getUsername() == null) {
            editedUserDto.setUsername(requestedUserDto.getUsername());
        }
        if (editedUserDto.getEmail() == null) {
            editedUserDto.setEmail(requestedUserDto.getEmail());
        }
        if (editedUserDto.getPermissions() == null) {
            editedUserDto.setPermissions(requestedUserDto.getPermissions());
        }
        if (editedUserDto.getFirstName() == null) {
            editedUserDto.setFirstName(requestedUserDto.getFirstName());
        }
        if (editedUserDto.getLastName() == null) {
            editedUserDto.setLastName(requestedUserDto.getLastName());
        }
        if (editedUserDto.getAge() == null) {
            editedUserDto.setAge(requestedUserDto.getAge());
        }
        if (editedUserDto.getPhoneNumber() == null) {
            editedUserDto.setPhoneNumber(requestedUserDto.getPhoneNumber());
        }
        userService.update(editedUserDto);
    }

    @DeleteMapping(value = "/{accountId}")
    public ResponseEntity<String> delete(@PathVariable(value = "accountId") String accountId) {
        String accountToDeleteUsername = userService.getAccountById(accountId).getUsername();

        // extract to separate method
        Account requester = (Account) userService
                .loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if (requester.getUsername().equals(accountToDeleteUsername)
                || requester.getPermissions().contains(UserPermission.ROLE_ADMIN)) {
            // use return ResponseEntity.ok(userService.delete(accountId));
            return new ResponseEntity<>(userService.delete(accountId), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}
