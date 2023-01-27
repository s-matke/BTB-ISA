package rs.ftn.uns.btb.core.appointment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.ftn.uns.btb.core.appointment.dtos.AppointmentDTO;
import rs.ftn.uns.btb.core.appointment.interfaces.AppointmentState;
import rs.ftn.uns.btb.core.center.Center;
import rs.ftn.uns.btb.core.report.Report;
import rs.ftn.uns.btb.core.scheduled_appointment.ScheduledAppointment;
import rs.ftn.uns.btb.core.staff.Staff;
import rs.ftn.uns.btb.core.user.User;

import javax.persistence.*;

import org.springframework.scheduling.annotation.Scheduled;

import java.sql.Date; // SQL DATE = YYYY-MM-DD, JAVAUTIL DATE = YYYY-MM-DD HH:MM:SS
import java.sql.Time;

@Entity
@Table(name = "appointment")
@Getter @Setter
public class Appointment {

    @Id
    @Column(nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "DATE", nullable = false)
    private Date date;

    @Column(name = "TIME", nullable = false)
    private Time time;

    @Column(name = "DURATION", nullable = false)
    private Integer duration;

    @Column(name = "state", nullable = false)
    private AppointmentState state;

    @JsonIgnore
    private String activationCode;

    // Centar u kom postoji termin
    @ManyToOne(fetch = FetchType.EAGER)
    //@JoinColumn(name="center_id", nullable = false)
    @JsonIgnore
    private Center center;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private Staff staff;

    // @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    // @JsonIgnore
    // private Set<Report>

    @OneToOne(mappedBy = "appointment", cascade = CascadeType.ALL)
    @JsonIgnore
    private Report report;

    @OneToOne(mappedBy = "appointment", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private ScheduledAppointment scheduledAppointment;
    
    // @ManyToOne(cascade = CascadeType.ALL)
    // @JoinTable(
    //     name = "scheduledApp",
    //     joinColumns = @JoinColumn(
    //         name = "appointment_id",
    //         referencedColumnName = "id"
    //     ),
    //     inverseJoinColumns = @JoinColumn(
    //         name = "user_id",
    //         referencedColumnName = "id"
    //     )
    // )
    // @JsonIgnore
    // private User user;

    public Appointment() {}

    public void copyValuesFromAppointmentDto(AppointmentDTO appointmentDTO){
        this.setDate(appointmentDTO.getDate());
        this.setTime(appointmentDTO.getTime());
        this.setDuration(appointmentDTO.getDuration());
    }

}
