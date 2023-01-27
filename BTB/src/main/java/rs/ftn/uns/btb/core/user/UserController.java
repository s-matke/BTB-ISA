package rs.ftn.uns.btb.core.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import rs.ftn.uns.btb.core.user.dtos.UserLoginDTO;
import io.swagger.v3.oas.annotations.Parameter;


import rs.ftn.uns.btb.core.user.dtos.UserDTO;
import rs.ftn.uns.btb.core.user.interfaces.UserService;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/api/user")
public class UserController {

    public final UserService _userService;

    @Autowired
    public UserController(UserService _userService) { this._userService = _userService; }


    @Operation(summary = "Create new user summary", description = "Create new user description", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)) }),
            @ApiResponse(responseCode = "409", description = "Not possible to create new staff when given ID is not null",
                    content = @Content )
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User savedUser = null;
        try {
            savedUser = _userService.create(user);
            return new ResponseEntity<User>(savedUser, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<User>(savedUser, HttpStatus.CONFLICT);
        }
    }
    //Spasko
    @GetMapping("/activate-user/{activation}")
    public ResponseEntity<User> activateUser(@PathVariable("activation") String activation)throws Exception {
        User retVal;
        try {
            retVal = _userService.activate(activation);
            return new ResponseEntity<>(retVal, HttpStatus.OK);
        } catch (Exception e) {
            throw e;
        }
    }
    @Operation(summary = "Get user by id", description = "Get user by id", method="GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "found user by id",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "404", description = "user not found", content = @Content)
    })
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<User> getUser(@Parameter(name="id", description = "ID of a user to return", required = true) @PathVariable("id") Long id) {
        User user = _userService.findOne(id);

        if (user == null) {
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @Operation(summary = "Login", description = "Login", method="GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully logged in!",
                    content =
                            { @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)) }
            ),
            @ApiResponse(responseCode = "404", description = "Invalid username or password", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content) })
    @PostMapping(value="/login",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> checkLogin(@RequestBody UserLoginDTO userLoginDTO) {

        User user_info = _userService.checkLogin(userLoginDTO.getEmail(), userLoginDTO.getPassword());
        System.out.println("User check Login returned: " + user_info );
        if (user_info == null) {
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<User>(user_info, HttpStatus.OK);
    }

    @Operation(summary = "Get all users", description = "Get all users", method="GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = User.class))))
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('STAFF') or hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> getUsers() {
       List<User> users = _userService.findAll();
        List<UserDTO> usersDTO = new ArrayList<>();
        for (User u : users) {
            usersDTO.add(new UserDTO(u.getId(), u.getJmbg(),u.getFirstName(),u.getLastName(),u.getEmail()));
        }
        return new ResponseEntity<>(usersDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/findByFullName")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<List<UserDTO>> getUserByFullName(@RequestParam String firstName,
                                                           @RequestParam String lastName) {

        List<User> users = _userService.findByFirstNameAndLastName(firstName, lastName);


        List<UserDTO> usersDTO = new ArrayList<>();
        for (User u : users) {
            usersDTO.add(new UserDTO(u.getJmbg(),u.getFirstName(),u.getLastName(),u.getEmail()));
        }
        return new ResponseEntity<>(usersDTO, HttpStatus.OK);
    }


        @Operation(summary = "Update an existing user", description = "Update an existing user", method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfuly edited",
            content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = User.class) )
            }),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    }

    )
    @PutMapping(value="/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<User> updateUser(@RequestBody User user){

        System.out.println(user.getId());
        User updatedUser = null;

        try {
            updatedUser = _userService.update(user);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }

        if(updatedUser == null){
            return new ResponseEntity<User>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<User>(updatedUser, HttpStatus.OK);

        }

    @GetMapping("/users")
    @PreAuthorize("hasRole('STAFF')")
    List<User> getAllUsers() {
        return _userService.findAll();
    }

    @GetMapping("/findBy/{id}")
    @PreAuthorize("hasRole('STAFF')")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
        System.out.println("e");
        return ResponseEntity.ok(_userService.findById(id));
    }


}
