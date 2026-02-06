
package com.game_list.gamelist.controller;

import com.game_list.gamelist.dto.ProfileResponse;
import com.game_list.gamelist.dto.UpdateProfileNameDTO;
import com.game_list.gamelist.entity.Profile;
import com.game_list.gamelist.entity.User;
import com.game_list.gamelist.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/me")
@CrossOrigin(origins = {"*"})
public class UserController {
    
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    // busca perfil do usuario logado
    @GetMapping("/profile")
    public ProfileResponse getProfile(Authentication auth){
        Profile profile = userService.getUserProfile(auth.getName());
        return ProfileResponse.from(profile);
    }
    
    // edita nome de perfil
    @PatchMapping("/profile/name")    
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateProfileName(@RequestBody UpdateProfileNameDTO request, Authentication auth){
        userService.editProfileName(auth.getName(), request.name());
    }
    
    //desativar conta
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/deactive")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactiveAccount(Authentication auth){
        userService.deactivateAccount(auth.getName());
    }
    
    //deletar conta
    @DeleteMapping("/users/{username}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccount(Authentication auth){
        userService.deleteAccount(auth.getName());
    }
}