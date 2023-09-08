package lab.space.my_house_24_rest.repository;

import lab.space.my_house_24_rest.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepo extends JpaRepository<Token,Integer> {
    @Query("select t from Token t inner join User a on  t.user.id = a.id where a.id = :id and (t.expired = false or t.revoked = false)")
    List<Token> findAllValidTokensByAdmin(Long id);

    Optional<Token> findByToken(String token);
}
