package com.game_list.gamelist.controller;

import com.game_list.gamelist.dto.ProfileResponse;
import com.game_list.gamelist.dto.UpdateProfileNameDTO;
import com.game_list.gamelist.entity.Profile;
import com.game_list.gamelist.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/me")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // busca perfil do usuário logado
    @GetMapping("/profile")
    public ProfileResponse getProfile(Authentication auth) {
        Profile profile = userService.getUserProfile(auth.getName());
        return ProfileResponse.from(profile);
    }

    // edita nome de perfil
    @PatchMapping("/profile/name")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateProfileName(@RequestBody UpdateProfileNameDTO request, Authentication auth) {
        userService.editProfileName(auth.getName(), request.name());
    }

    // desativar a própria conta
    @PatchMapping("/deactivate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivateAccount(Authentication auth) {
        userService.deactivateAccount(auth.getName());
    }

    // deletar a própria conta
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccount(Authentication auth) {
        userService.deleteAccount(auth.getName());
    }
}