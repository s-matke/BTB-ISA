package rs.ftn.uns.btb.core.security;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import rs.ftn.uns.btb.core.admin.Admin;
import rs.ftn.uns.btb.core.admin.interfaces.AdminService;
import rs.ftn.uns.btb.core.exception.ResourceConflictException;
import rs.ftn.uns.btb.core.login.dtos.LoginDTO;
import rs.ftn.uns.btb.core.security.dtos.JwtAuthenticationRequest;
import rs.ftn.uns.btb.core.security.dtos.UserRequest;
import rs.ftn.uns.btb.core.security.dtos.UserTokenState;
import rs.ftn.uns.btb.core.staff.Staff;
import rs.ftn.uns.btb.core.staff.interfaces.StaffService;
import rs.ftn.uns.btb.core.user.User;
import rs.ftn.uns.btb.core.user.interfaces.UserService;
import rs.ftn.uns.btb.core.util.TokenUtils;

import java.util.UUID;

@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService _userService;

    @Autowired
    private StaffService _staffService;

    @Autowired
    private AdminService _adminService;

    @Operation(summary = "Login", description = "Login", method="POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully logged in!",
                    content =
                            { @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)) }
            ),
            @ApiResponse(responseCode = "404", description = "Invalid username or password", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content) })
    @PostMapping("/login")
    public ResponseEntity<UserTokenState> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest, HttpServletResponse response) {
    
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword()));
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = null;

        CustomUserDetails customUser = (CustomUserDetails) authentication.getPrincipal();
        jwt = tokenUtils.generateTokeN(customUser.getEmail());

        int expiresIn = tokenUtils.getExpiredIn();

        return ResponseEntity.ok(new UserTokenState(jwt, expiresIn));
    }

    
    @Operation(summary = "Create new user summary", description = "Create new user description", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)) }),
            @ApiResponse(responseCode = "409", description = "Not possible to create new staff when given ID is not null",
                    content = @Content )
    })
    @PostMapping(value = "/signup")
    public ResponseEntity<User> addUser(@RequestBody UserRequest userRequest, UriComponentsBuilder ucBuilder) {
        User existUser = this._userService.findByEmail(userRequest.getEmail());

        if (existUser != null) {
            throw new ResourceConflictException(userRequest.getId(), "Email already exists");
        }

        // User user = this._userService.create(userRequest);

        User createdUser = this._userService.add(userRequest);

        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    // TODO: 
    // temp solution atm
    @GetMapping(value = "/me")
    public ResponseEntity<LoginDTO> me(@RequestHeader("Authorization") String token) {
        String email = tokenUtils.getEmailFromToken(token.split(" ")[1].trim());

        LoginDTO tempMe = new LoginDTO();

        User user = this._userService.findByEmail(email);
        if (user != null) {
            tempMe.setId(user.getId());
            tempMe.setEmail(user.getEmail());
            tempMe.setRole(user.getRole());
            tempMe.setCenter_id(null);
            return new ResponseEntity<LoginDTO>(tempMe, HttpStatus.OK);
        }
        Admin admin = this._adminService.findByEmail(email);
        if (admin != null) {
            tempMe.setId(admin.getId());
            tempMe.setEmail(admin.getEmail());
            tempMe.setRole(admin.getRole());
            tempMe.setCenter_id(null);
            return new ResponseEntity<LoginDTO>(tempMe, HttpStatus.OK);
        }
        Staff staff = this._staffService.findByEmail(email);
        if (staff != null) {
            tempMe.setId(staff.getId());
            tempMe.setEmail(staff.getEmail());
            tempMe.setRole(staff.getRole());
            tempMe.setCenter_id(staff.getCenter().getId());
            return new ResponseEntity<LoginDTO>(tempMe, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
}
