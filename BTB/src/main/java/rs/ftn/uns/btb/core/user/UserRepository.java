package rs.ftn.uns.btb.core.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import rs.ftn.uns.btb.core.role.Role;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    public User findOneById(Long id);
    Optional<User> findByActivationCode(String activationCode);
    public List<User> findAll();
    @Query(value = "SELECT u "+
            "FROM users u "+
            "WHERE LOWER(u.email)='string@'",nativeQuery = true)// AND u.password = ?2",nativeQuery = true)
    public User checkLogin(String email, String password);
    public User findOneByEmailAndPassword(String email, String password);
    public List<User> findByFirstNameAndLastNameAllIgnoringCase(String firstName, String lastName);

    // @Transactional
    @Query(value = "SELECT r.name FROM users_roles ur, role r WHERE ur.roles_id = r.id AND ur.user_id = ?1", nativeQuery = true)
    public List<Role> findAllRolesByUserId(Long id);

    public User findOneByEmail(String email);

}
