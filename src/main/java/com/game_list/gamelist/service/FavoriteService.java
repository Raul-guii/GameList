package com.game_list.gamelist.service;

import com.game_list.gamelist.dto.FavoriteDTO;
import com.game_list.gamelist.dto.GameDTO;
import com.game_list.gamelist.entity.Favorite;
import com.game_list.gamelist.entity.Game;
import com.game_list.gamelist.entity.User;
import com.game_list.gamelist.exception.ConflictException;
import com.game_list.gamelist.exception.ResourceNotFoundException;
import com.game_list.gamelist.repository.FavoriteRepository;
import com.game_list.gamelist.repository.GameRepository;
import com.game_list.gamelist.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final IgdbClient igdbClient;

    public FavoriteDTO addFavorite(Long userId, Long externalGameId) {

        Game game = gameRepository.findByExternalId(externalGameId)
                .orElseGet(() -> {

                    List<GameDTO> result = igdbClient.sendRequest(
                            "fields id, name, cover.url; where id = " + externalGameId + ";"
                    );

                    if (result.isEmpty()) {
                        throw new ResourceNotFoundException("Jogo não encontrado no IGDB");
                    }

                    GameDTO apiGame = result.get(0);

                    Game newGame = new Game();
                    newGame.setExternalId(apiGame.getId());
                    newGame.setName(apiGame.getName());
                    newGame.setCoverUrl(apiGame.getCoverUrl());

                    return gameRepository.save(newGame);
                });

        boolean alreadyFav = favoriteRepository.existsByUserIdAndGameId(userId, game.getId());

        if (alreadyFav) {
            throw new ConflictException("Jogo já está favoritado");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        Favorite fav = new Favorite();
        fav.setUser(user);
        fav.setGame(game);

        favoriteRepository.save(fav);

        return new FavoriteDTO(
                fav.getId(),
                fav.getUser().getId(),
                fav.getGame().getExternalId()
        );
    }

    public List<FavoriteDTO> getFavoriteDTOByUser(Long userId) {
        return favoriteRepository.findByUserId(userId)
                .stream()
                .map(fav -> new FavoriteDTO(
                        fav.getId(),
                        fav.getUser().getId(),
                        fav.getGame().getExternalId()
                ))
                .toList();
    }

    public List<Favorite> getFavoriteByUser(Long userId) {
        return favoriteRepository.findByUserId(userId);
    }

    public void removeFavorite(Long userId, Long externalGameId) {

        Game game = gameRepository.findByExternalId(externalGameId)
                .orElseThrow(() -> new ResourceNotFoundException("Jogo não encontrado"));

        Favorite fav = favoriteRepository.findByUserIdAndGameId(userId, game.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Favorito não encontrado"));

        favoriteRepository.delete(fav);
    }

    public boolean isFavorite(Long userId, Long externalGameId) {
        return favoriteRepository
                .findByUserIdAndGame_ExternalId(userId, externalGameId)
                .isPresent();
    }
}