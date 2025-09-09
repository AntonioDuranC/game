package pe.com.ladc.services;

import pe.com.ladc.entity.Games;
import pe.com.ladc.exceptions.GameDontExistException;
import pe.com.ladc.repository.GamesRepository;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@Dependent
public class GameService {

    @Inject
    private GamesRepository gamesRepository;

    public List<Games> findPaginated(int page, int pageSize,String sortFiled,String name) {
        if (name != null && !name.isEmpty()) {
            return gamesRepository.findPaginatedByName(page, pageSize, sortFiled, name);
        }
        return gamesRepository.findPaginated(page, pageSize,sortFiled);
    }

    public long count(String name) {
        if (name != null && !name.isEmpty()) {
            return gamesRepository.countByName(name);
        }
        return gamesRepository.count();
    }

    public Optional<Games> findById(long id) {
        return gamesRepository.findByIdOptional(id);
    }

    @Transactional
    public void createGame(Games game){
        game.setId(0);
        gamesRepository.persist(game);
    }

    @Transactional
    public void replaceGame(Games game){
        gamesRepository.findByIdOptional(game.getId()).ifPresentOrElse(
                v-> gamesRepository.persist(game),()-> {
                    throw new GameDontExistException("Game with id "+game.getId()+" does not exist");
                }
        );
    }

    @Transactional
    public void updateGame(long id,String name,String category){
        gamesRepository.findByIdOptional(id).ifPresentOrElse(v->{
            if (name != null && !name.isEmpty()) {
                v.setName(name);
            }
            if (category != null && !category.isEmpty()) {
                v.setCategory(category);
            }
            gamesRepository.persist(v);
        },()-> {throw new GameDontExistException("Game with id "+id+" does not exist");});
    }

    @Transactional
    public void deleteGame(long id){
        gamesRepository.findByIdOptional(id).ifPresentOrElse(v->{
            gamesRepository.delete(v);
        },()-> {
            throw new GameDontExistException("Game with id "+id+" does not exist");
        });
    }
}
