package rs.ftn.uns.btb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ftn.uns.btb.model.Person;
import rs.ftn.uns.btb.model.User;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import rs.ftn.uns.btb.model.User;


public interface UserRepository extends JpaRepository<User, Long> {
    public User findOneById(Long id);

    @Query(value = "SELECT u "+
            "FROM users u "+
            "WHERE LOWER(u.email)='string@'",nativeQuery = true)// AND u.password = ?2",nativeQuery = true)
    public User checkLogin(String email, String password);

    public User findOneByEmailAndPassword(String email, String password);


    public List<User> findByFirstNameAndLastNameAllIgnoringCase(String firstName, String lastName);


//    public User findOneById(Long id);

}
