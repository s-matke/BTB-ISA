package rs.ftn.uns.btb.service;
import rs.ftn.uns.btb.model.User;
import rs.ftn.uns.btb.model.dto.UserDTO;

import java.util.Collection;
import java.util.List;

import java.util.ArrayList;
import java.util.Collection;

public interface UserService {

    User create(User user) throws Exception;


    List<User> findAll();
    List<User> findByFirstNameAndLastName(String firstName, String lastName);



    User findOne(Long id);
    User checkLogin(String email, String password);

    Collection<User> findAll();
    List<User> findByFirstNameAndLastName(String firstName, String lastName);


    User update(User user) throws Exception;

//    User findOne(Long jmbg);

}

