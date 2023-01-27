package rs.ftn.uns.btb.core.user.interfaces;

import rs.ftn.uns.btb.core.role.Role;
import rs.ftn.uns.btb.core.security.dtos.UserRequest;
import rs.ftn.uns.btb.core.user.User;

import java.util.List;

public interface UserService {

    User create(User user) throws Exception;
    User activate(String activation)throws Exception;
    List<User> findAll();
    User findById(Long id);
    User findOne(Long id);
    User checkLogin(String email, String password);
    List<User> findByFirstNameAndLastName(String firstName, String lastName);
    User update(User user) throws Exception;

    User findByEmail(String email);

    User add(UserRequest userRequest);

    List<Role> getRolesByUser(Long id);
}

