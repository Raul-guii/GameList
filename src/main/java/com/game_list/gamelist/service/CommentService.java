package com.game_list.gamelist.service;

import com.game_list.gamelist.dto.comment.CommentResponse;
import com.game_list.gamelist.entity.Comment;
import com.game_list.gamelist.entity.Game;
import com.game_list.gamelist.entity.User;
import com.game_list.gamelist.exception.AccessDeniedException;
import com.game_list.gamelist.exception.ResourceNotFoundException;
import com.game_list.gamelist.repository.CommentRepository;
import com.game_list.gamelist.repository.GameRepository;
import com.game_list.gamelist.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;

    /**
     * Cria um comentário em um jogo, identificado pelo externalId da IGDB.
     * Assume que o Game já existe localmente — ou seja, alguém já precisa
     * ter favoritado ou de algum modo persistido esse jogo antes. Se o jogo
     * ainda não existe no banco local, lança ResourceNotFoundException.
     */
    @Transactional
    public CommentResponse addComment(String username, Long gameExternalId, String content) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        Game game = gameRepository.findByExternalId(gameExternalId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Jogo ainda não está na base local. Favorite ou pesquise o jogo antes de comentar."));

        Comment comment = new Comment();
        comment.setUser(user);
        comment.setGame(game);
        comment.setContent(content);

        Comment saved = commentRepository.save(comment);
        return CommentResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentsByGame(Long gameExternalId) {
        return commentRepository.findByGame_ExternalIdOrderByCreatedAtDesc(gameExternalId)
                .stream()
                .map(CommentResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentsByUser(String username) {
        return commentRepository.findByUser_Username(username)
                .stream()
                .map(CommentResponse::from)
                .toList();
    }

    @Transactional
    public CommentResponse updateComment(Long commentId, String username, String newContent) {
        Comment comment = commentRepository.findByIdAndUser_Username(commentId, username)
                .orElseThrow(() -> new AccessDeniedException(
                        "Comentário não encontrado ou você não tem permissão para editá-lo"));

        comment.setContent(newContent);
        Comment saved = commentRepository.save(comment);
        return CommentResponse.from(saved);
    }

    /**
     * Deleta um comentário. O autor pode deletar o próprio comentário;
     * um ADMIN pode deletar qualquer comentário (moderação).
     */
    @Transactional
    public void deleteComment(Long commentId, String username, boolean isAdmin) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comentário não encontrado"));

        boolean isAuthor = comment.getUser().getUsername().equals(username);

        if (!isAuthor && !isAdmin) {
            throw new AccessDeniedException("Você não tem permissão para deletar este comentário");
        }

        commentRepository.delete(comment);
    }
}