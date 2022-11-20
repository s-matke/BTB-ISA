package rs.ftn.uns.btb.core.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ftn.uns.btb.core.user.interfaces.UserService;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    public final UserRepository _userRepo;

    @Autowired
    public UserServiceImpl(UserRepository _userRepo) { this._userRepo = _userRepo; }

    @Override
    public User create(User user) throws Exception {
        user.setRole(Roles.USER);
        User newUser = this._userRepo.save(user);
        return newUser;
    }

    public User findOne(Long id) {
        User user = this._userRepo.findOneById(id);
        return  user;
    }
    @Override
    public User checkLogin(String email,String password){
        //User user = this._userRepo.checkLogin(email,password);
        User user = this._userRepo.findOneByEmailAndPassword(email,password);
        return user;
    }

    @Override
    public List<User> findAll() {
        List<User> users = _userRepo.findAll();
        return users;
    }

    @Override
    public List<User> findByFirstNameAndLastName(String firstName, String lastName) {
        return _userRepo.findByFirstNameAndLastNameAllIgnoringCase(firstName, lastName);
    }


    @Override
    public User update(User user) throws Exception {
        User userToUpdate = this._userRepo.findOneById(user.getId());

        if(userToUpdate == null){
            throw new Exception("User does not exist");
        }

        userToUpdate.setJmbg(user.getJmbg());
        userToUpdate.setFirstName(user.getFirstName());
        userToUpdate.setLastName(user.getLastName());
        //userToUpdate.setEmail(user.getEmail());
        userToUpdate.setGender(user.getGender());
        userToUpdate.setPhone(user.getPhone());
        userToUpdate.setAddress(user.getAddress());
        userToUpdate.setCity(user.getCity());
        userToUpdate.setCountry(user.getCountry());
        userToUpdate.setJob(user.getJob());
        userToUpdate.setProfession(user.getProfession());

        User updatedUser = _userRepo.save(userToUpdate);

        return updatedUser;
    }

    @Override
    public User findById(Long id) {
        return this._userRepo.findOneById(id);
    }

}