package rs.ftn.uns.btb.core.scheduled_appointment;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.Setter;
import rs.ftn.uns.btb.core.appointment.Appointment;
import rs.ftn.uns.btb.core.user.User;

@Entity
@Table(name = "scheduled_appointment", uniqueConstraints = @UniqueConstraint(columnNames = { "appointment_id" }))
@Getter @Setter
public class ScheduledAppointment implements Serializable {
    

    @Id
    @Column(nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "appointment_id", referencedColumnName = "id")
    private Appointment appointment;

    @ManyToOne(fetch = FetchType.EAGER)
    private User users;

    public ScheduledAppointment() {}  

}
