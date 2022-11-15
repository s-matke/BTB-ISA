package rs.ftn.uns.btb.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ftn.uns.btb.model.User;

import rs.ftn.uns.btb.model.dto.UserLoginDTO;
import rs.ftn.uns.btb.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;


import rs.ftn.uns.btb.model.dto.UserDTO;

import rs.ftn.uns.btb.model.dto.UserUpdateDTO;

import rs.ftn.uns.btb.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


import java.util.Collection;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@CrossOrigin("http://localhost:3000")
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
    @Operation(summary = "Get user by id", description = "Get user by id", method="GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "found user by id",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "404", description = "user not found", content = @Content)
    })
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
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
    public ResponseEntity<List<UserDTO>> getUsers() {
       List<User> users = _userService.findAll();
        List<UserDTO> usersDTO = new ArrayList<>();
        for (User u : users) {
            usersDTO.add(new UserDTO(u.getJmbg(),u.getFirstName(),u.getLastName(),u.getEmail()));
        }
        return new ResponseEntity<>(usersDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/findByFullName")
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
    @PutMapping(value="/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody UserUpdateDTO userUpdateDTO){
        User userForUpdate = _userService.findOne(id);

        userForUpdate.copyValuesFromDTO(userUpdateDTO);

        User updatedUser = null;

        try {
            updatedUser = _userService.update(userForUpdate);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }

        if(updatedUser == null){
            return new ResponseEntity<User>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<User>(updatedUser, HttpStatus.OK);

        }



}
