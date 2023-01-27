package rs.ftn.uns.btb.core.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import rs.ftn.uns.btb.core.role.Role;
import rs.ftn.uns.btb.core.role.interfaces.IRole;
import rs.ftn.uns.btb.core.security.dtos.UserRequest;
import rs.ftn.uns.btb.core.user.interfaces.UserService;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    public final UserRepository _userRepo;

    @Autowired
    private IRole _roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository _userRepo) { this._userRepo = _userRepo; }

    @Override
    public User create(User user) throws Exception {
        user.setRole(Roles.USER);
        user.setActivated(false);
        String actCode = UUID.randomUUID().toString();
        System.out.println("actCode");
        System.out.println(actCode);
        user.setActivationCode(actCode);
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

    @Override
    public User findByEmail(String email) {
        return this._userRepo.findOneByEmail(email);
    }

    @Override
    public User add(UserRequest userRequest) {
        User u = new User();
        u.setActivated(false);
        String actCode = UUID.randomUUID().toString();
        System.out.println("actCode");
        System.out.println(actCode);
        u.setActivationCode(actCode);

        userRequest.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        u.copyValuesFromUserRequest(userRequest);

        u.setStatus(true);
        u.setRole(Roles.USER);

        List<Role> roles = _roleService.findByName("ROLE_USER");
        u.setRoles(roles);

        return this._userRepo.save(u);
    } 


    @Override
    public List<Role> getRolesByUser(Long id) {
        User u = this.findById(id);
        
        if (u == null) {
            return null;
        }

        List<Role> allRoles = this._userRepo.findAllRolesByUserId(u.getId());

        return allRoles;
    }

}
