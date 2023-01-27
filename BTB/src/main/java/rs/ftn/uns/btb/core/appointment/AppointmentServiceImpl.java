package rs.ftn.uns.btb.core.appointment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.ftn.uns.btb.core.appointment.dtos.BookAppointmentDTO;
import rs.ftn.uns.btb.core.appointment.interfaces.AppointmentService;
import rs.ftn.uns.btb.core.appointment.interfaces.AppointmentState;
import rs.ftn.uns.btb.core.scheduled_appointment.ScheduledAppointment;
import rs.ftn.uns.btb.core.scheduled_appointment.ScheduledAppointmentRepository;
import rs.ftn.uns.btb.core.user.User;
import rs.ftn.uns.btb.core.user.UserRepository;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    public final AppointmentRepository _appointmentRepo;
    public final ScheduledAppointmentRepository scheduledAppointmentRepository;
    public final UserRepository userRepository;

    @Autowired
    public AppointmentServiceImpl(AppointmentRepository _appointmentRepo, ScheduledAppointmentRepository scheduledAppointmentRepository,UserRepository userRepository) {
        this._appointmentRepo = _appointmentRepo;
        this.scheduledAppointmentRepository = scheduledAppointmentRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Appointment create(Appointment appointment) throws  Exception{
        appointment.setState(AppointmentState.AVAILABLE);
        Appointment newAppointment = this._appointmentRepo.save(appointment);
        return newAppointment;
    }

    @Override
    public List<Appointment> findByCenterId(Long id) {
        List<Appointment> appointmentsForCenter = _appointmentRepo.findAllByCenterId(id);
        return appointmentsForCenter;
    }

    @Override
    public void deleteSelection(Long[] idsOfAppointmentsToRemove) {
        _appointmentRepo.deleteAllById(Arrays.asList(idsOfAppointmentsToRemove));
    }

    @Override
    public Appointment findOne(Long id) {
        return this._appointmentRepo.findById(id).orElseGet(null);
    }
    
    public List<Appointment> findAll() {
        List<Appointment> allAppointments = _appointmentRepo.findAll();
        return allAppointments;
    }
    public List<Appointment> getAllAvailable() {


        List<Appointment> allAppointments = _appointmentRepo.findAll();
        Iterator<Appointment> iterator = allAppointments.iterator();
        while (iterator.hasNext()) {
            Appointment appointment = iterator.next();
            if (appointment.getState() != AppointmentState.AVAILABLE) {
                iterator.remove();
            }
        }
        return allAppointments;
    }

    @Override
    public Appointment update(Appointment appointment) throws Exception {
        Appointment appointmentToUpdate = this._appointmentRepo.findOneById(appointment.getId());

        if (appointmentToUpdate == null) {
            throw new Exception("Appointment does not exist");
        }

        appointmentToUpdate.setDate(appointment.getDate());
        appointmentToUpdate.setTime(appointment.getTime());
        appointmentToUpdate.setDuration(appointment.getDuration());
        appointmentToUpdate.setState(appointment.getState());

        Appointment updatedAppointment = _appointmentRepo.save(appointmentToUpdate);

        return updatedAppointment;
    }
    /*
    @Override
    public Iterable<Appointment> GetAll() throws Exception {
        return _appointmentRepo.findAll();
    }*/
    @Override
    public void SendConfirmationCode(Appointment app) throws Exception {
        ScheduledAppointment sa = scheduledAppointmentRepository.findByAppointmentId(app.getId());
        User customer = userRepository.findById(sa.getId()).get();
/*
        String activationLink = "http://localhost:8086/api/appointment/confirm/"+ app.getConfirmationCode();
        Map<String, DataSource> files = new HashMap<>();
        PrepareQrCode(activationLink, files);
        emailSenderService.sendMailWithInlineResources(customer.getEmail(), "Confirm booked appointment", "Appointment activation link is:" + activationLink, files);
*/
    }
    @Override
    public Appointment getBooked(Long user_id) {
        ScheduledAppointment sa = scheduledAppointmentRepository.findByUsersId(user_id);
        return sa.getAppointment();
    }
    public Appointment CancelAppointment(BookAppointmentDTO dto) throws Exception {
        Appointment appointment = _appointmentRepo.findById(dto.appointmentId).get();
        User customer = userRepository.findById(dto.customerId).get();

        //provera da li je za manje od 24h

        ScheduledAppointment sa =  scheduledAppointmentRepository.findByAppointmentId(dto.appointmentId);
        //provera da li je za appointment trenutno dodeljen ovaj korisnik
        if(sa.getUsers().getId() != customer.getId()){
            return appointment;
        }
        //provera da li je booked ili pending
        if(appointment.getState() != AppointmentState.SCHEDULED && appointment.getState() != AppointmentState.FINISHED){
            return appointment;
        }
        //namesti polja
        appointment.setState(AppointmentState.AVAILABLE);
        //_appointmentRepo.save(appointment);
        update(appointment);
        scheduledAppointmentRepository.delete(sa);
        return appointment;

    }

}
