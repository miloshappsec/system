package repository;

import model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

//    @Query(value = "SELECT * FROM users WHERE username = '" + username + "'", nativeQuery = true)
//    User findByUsernameUnsafe(String username);
}
