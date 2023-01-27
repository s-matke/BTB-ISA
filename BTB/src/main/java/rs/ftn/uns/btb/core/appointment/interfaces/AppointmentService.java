package rs.ftn.uns.btb.core.appointment.interfaces;
import rs.ftn.uns.btb.core.appointment.Appointment;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rs.ftn.uns.btb.core.appointment.dtos.BookAppointmentDTO;
import rs.ftn.uns.btb.core.user.User;

public interface AppointmentService {

    Appointment create(Appointment appointment) throws  Exception;

    List<Appointment> findByCenterId(Long id);

    List<Appointment> findAll();
    List<Appointment> getAllAvailable();
    Appointment getBooked( Long user_id);
    Appointment activate(String activation)throws Exception;
    void deleteSelection(Long[] idsOfAppointmentsToRemove);

    Appointment findOne(Long id);

    Appointment update(Appointment appointment) throws Exception;
    //Iterable<Appointment> GetAll() throws Exception;
    void SendConfirmationCode(Appointment app) throws Exception;
    Appointment CancelAppointment(BookAppointmentDTO dto) throws Exception;
}
