package rs.ftn.uns.btb.core.appointment;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ftn.uns.btb.core.appointment.Appointment;
import rs.ftn.uns.btb.core.user.User;

import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findAllByCenterId(Long id);
    List<Appointment> findAll();

    Appointment findOneById(Long id);
    Optional<Appointment> findByActivationCode(String activationCode);
}
