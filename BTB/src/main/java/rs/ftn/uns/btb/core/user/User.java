package rs.ftn.uns.btb.core.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import rs.ftn.uns.btb.core.report.Report;
import rs.ftn.uns.btb.core.role.Role;
import rs.ftn.uns.btb.core.appointment.Appointment;
import rs.ftn.uns.btb.core.scheduled_appointment.ScheduledAppointment;
import rs.ftn.uns.btb.core.security.dtos.UserRequest;
import rs.ftn.uns.btb.core.survey.answer.SurveyAnswers;
import rs.ftn.uns.btb.core.user.dtos.UserUpdateDTO;
import rs.ftn.uns.btb.core.user.interfaces.Person;

import javax.persistence.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter @Setter
public class User extends Person {

    @Column(name = "profession", nullable = true)
    private String profession;

    @Column(name = "job", nullable = true)
    private String job;

    @Column(name = "penalty", nullable = true)
    private Integer penalty;

    // @Column(name = "username", nullable = false)
    // private String username;

    @OneToMany(mappedBy = "users", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Getter @Setter
    @JsonIgnore
    private Set<SurveyAnswers> surveyAnswers = new HashSet<>();
    
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Report> historyAppointments = new HashSet<>();
    @JsonIgnore
    private String activationCode;
    @JsonIgnore
    private boolean activated;
    
    public User() {}

    @OneToMany(mappedBy = "users", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
    @JsonIgnore
    private Set<ScheduledAppointment> scheduledAppointments = new HashSet<>();    

    // @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
    // @JoinTable(
    //     name = "scheduledApp",
    //     joinColumns = @JoinColumn(
    //         name = "user_id",
    //         referencedColumnName = "id"
    //     ),
    //     inverseJoinColumns = @JoinColumn(
    //         name = "appointment_id",
    //         referencedColumnName = "id"
    //     )
    // )
    // @JsonIgnore
    // private Set<Appointment> allAppointments = new HashSet<>();

    public User(UserUpdateDTO userUpdateDTO) {
        this.setJmbg((userUpdateDTO.getJmbg()));
        this.setFirstName(userUpdateDTO.getFirstName());
        this.setLastName(userUpdateDTO.getLastName());
        this.setEmail(userUpdateDTO.getEmail());
        this.setGender(userUpdateDTO.getGender());
        this.setPhone(userUpdateDTO.getPhone());
        this.setAddress(userUpdateDTO.getAddress());
        this.setCity(userUpdateDTO.getCity());
        this.setCountry(userUpdateDTO.getCountry());
        this.setProfession(userUpdateDTO.getProfession());
        this.setJob(userUpdateDTO.getJob());
    }

    public void copyValuesFromUserRequest(UserRequest userRequest) {
        this.setEmail(userRequest.getEmail());
        this.setPassword(userRequest.getPassword());
        this.setFirstName(userRequest.getFirstName());
        this.setLastName(userRequest.getLastName());
        this.setJmbg(userRequest.getJmbg());
    }

    // @ManyToMany(fetch = FetchType.EAGER)
    // @JoinTable(name = "users_roles",
    //         joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
    //         inverseJoinColumns = @JoinColumn(name = "roles_id", referencedColumnName = "id"))
    // private List<Role> roles;

    // @JsonIgnore
    // @Override
    // @Transactional
    // public Collection<? extends GrantedAuthority> getAuthorities() {
    //     return this.getRoles();
    // }

}
