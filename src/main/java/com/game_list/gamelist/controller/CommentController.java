package com.game_list.gamelist.controller;

import com.game_list.gamelist.dto.comment.CommentResponse;
import com.game_list.gamelist.dto.comment.CreateCommentRequest;
import com.game_list.gamelist.dto.comment.UpdateCommentRequest;
import com.game_list.gamelist.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/games/{gameExternalId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponse addComment(@PathVariable Long gameExternalId,
                                      @Valid @RequestBody CreateCommentRequest request,
                                      Authentication auth) {
        return commentService.addComment(auth.getName(), gameExternalId, request.content());
    }

    @GetMapping("/games/{gameExternalId}")
    public List<CommentResponse> getCommentsByGame(@PathVariable Long gameExternalId) {
        return commentService.getCommentsByGame(gameExternalId);
    }

    @GetMapping("/me")
    public List<CommentResponse> getMyComments(Authentication auth) {
        return commentService.getCommentsByUser(auth.getName());
    }

    @PatchMapping("/{commentId}")
    public CommentResponse updateComment(@PathVariable Long commentId,
                                         @Valid @RequestBody UpdateCommentRequest request,
                                         Authentication auth) {
        return commentService.updateComment(commentId, auth.getName(), request.content());
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long commentId, Authentication auth) {
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        commentService.deleteComment(commentId, auth.getName(), isAdmin);
    }
}