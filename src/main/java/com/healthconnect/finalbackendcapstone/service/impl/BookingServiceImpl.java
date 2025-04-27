package com.healthconnect.finalbackendcapstone.service.impl;

import com.healthconnect.finalbackendcapstone.dto.BookingDTO;
import com.healthconnect.finalbackendcapstone.exception.ResourceNotFoundException;
import com.healthconnect.finalbackendcapstone.model.Booking;
import com.healthconnect.finalbackendcapstone.model.Booking.AppointmentType;
import com.healthconnect.finalbackendcapstone.model.Booking.BookingStatus;
import com.healthconnect.finalbackendcapstone.model.Clinic;
import com.healthconnect.finalbackendcapstone.model.DoctorProfile;
import com.healthconnect.finalbackendcapstone.model.Patient;
import com.healthconnect.finalbackendcapstone.model.User;
import com.healthconnect.finalbackendcapstone.repository.BookingRepository;
import com.healthconnect.finalbackendcapstone.repository.ClinicRepository;
import com.healthconnect.finalbackendcapstone.repository.DoctorProfileRepository;
import com.healthconnect.finalbackendcapstone.repository.PatientRepository;
import com.healthconnect.finalbackendcapstone.repository.UserRepository;
import com.healthconnect.finalbackendcapstone.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true) // Default to read-only for better performance
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final DoctorProfileRepository doctorProfileRepository;
    private final ClinicRepository clinicRepository;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository,
                             UserRepository userRepository,
                             PatientRepository patientRepository,
                             DoctorProfileRepository doctorProfileRepository,
                             ClinicRepository clinicRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
        this.doctorProfileRepository = doctorProfileRepository;
        this.clinicRepository = clinicRepository;
    }

    @Override
    @Transactional
    public BookingDTO createBooking(BookingDTO bookingDTO) {
        // Validate clinic exists and check consultation mode
        if (bookingDTO.getClinicId() != null) {
            Clinic clinic = clinicRepository.findById(bookingDTO.getClinicId())
                    .orElseThrow(() -> new ResourceNotFoundException("Clinic not found with id: " + bookingDTO.getClinicId()));
            
            // Validate consultation mode
            if (bookingDTO.getAppointmentType() == AppointmentType.in_clinic && 
                clinic.getConsultationMode() == Clinic.ConsultationMode.ONLINE) {
                throw new IllegalStateException("This clinic only accepts online consultations");
            }
            if (bookingDTO.getAppointmentType() == AppointmentType.online && 
                clinic.getConsultationMode() == Clinic.ConsultationMode.IN_CLINIC) {
                throw new IllegalStateException("This clinic only accepts in-clinic consultations");
            }
        } else if (bookingDTO.getAppointmentType() == AppointmentType.in_clinic) {
            throw new IllegalStateException("Clinic ID is required for in-clinic appointments");
        }

        Booking booking = mapToEntity(bookingDTO);
        Booking savedBooking = bookingRepository.save(booking);
        return mapToDTO(savedBooking);
    }

    @Override
    public BookingDTO getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "id", id));
        return mapToDTO(booking);
    }

    // Paginated methods
    @Override
    public Page<BookingDTO> getBookingsByUserId(Long userId, Pageable pageable) {
        return bookingRepository.findByUserId(userId, pageable)
                .map(this::mapToDTO);
    }

    @Override
    public Page<BookingDTO> getBookingsByPatientId(Long patientId, Pageable pageable) {
        return bookingRepository.findByPatientId(patientId, pageable)
                .map(this::mapToDTO);
    }

    @Override
    public Page<BookingDTO> getBookingsByDoctorId(Long doctorId, Pageable pageable) {
        return bookingRepository.findByDoctorId(doctorId, pageable)
                .map(this::mapToDTO);
    }

    @Override
    public Page<BookingDTO> getBookingsByClinicId(Long clinicId, Pageable pageable) {
        return bookingRepository.findByClinicId(clinicId, pageable)
                .map(this::mapToDTO);
    }

    @Override
    public Page<BookingDTO> getBookingsByAppointmentType(AppointmentType appointmentType, Pageable pageable) {
        return bookingRepository.findByAppointmentType(appointmentType, pageable)
                .map(this::mapToDTO);
    }

    @Override
    public Page<BookingDTO> getBookingsByStatus(BookingStatus status, Pageable pageable) {
        return bookingRepository.findByStatus(status, pageable)
                .map(this::mapToDTO);
    }

    @Override
    public Page<BookingDTO> getBookingsByDateRange(LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return bookingRepository.findByAppointmentDatetimeBetween(start, end, pageable)
                .map(this::mapToDTO);
    }

    @Override
    public Page<BookingDTO> getBookingsByDoctorIdAndStatus(Long doctorId, BookingStatus status, Pageable pageable) {
        return bookingRepository.findByDoctorIdAndStatus(doctorId, status, pageable)
                .map(this::mapToDTO);
    }

    @Override
    public Page<BookingDTO> getBookingsByPatientIdAndStatus(Long patientId, BookingStatus status, Pageable pageable) {
        return bookingRepository.findByPatientIdAndStatus(patientId, status, pageable)
                .map(this::mapToDTO);
    }

    @Override
    public Page<BookingDTO> getBookingsByUserIdAndStatus(Long userId, BookingStatus status, Pageable pageable) {
        return bookingRepository.findByUserIdAndStatus(userId, status, pageable)
                .map(this::mapToDTO);
    }

    @Override
    public Page<BookingDTO> getBookingsByDoctorIdAndDateRange(Long doctorId, LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return bookingRepository.findByDoctorIdAndAppointmentDatetimeBetween(doctorId, start, end, pageable)
                .map(this::mapToDTO);
    }

    @Override
    public Page<BookingDTO> getBookingsByPatientIdAndDateRange(Long patientId, LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return bookingRepository.findByPatientIdAndAppointmentDatetimeBetween(patientId, start, end, pageable)
                .map(this::mapToDTO);
    }

    @Override
    public Page<BookingDTO> getBookingsByDoctorIdAndStatusAndDateRange(Long doctorId, BookingStatus status, LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return bookingRepository.findByDoctorIdAndStatusAndDateRange(doctorId, status, start, end, pageable)
                .map(this::mapToDTO);
    }

    @Override
    public Page<BookingDTO> getBookingsByPatientIdAndStatusAndDateRange(Long patientId, BookingStatus status, LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return bookingRepository.findByPatientIdAndStatusAndDateRange(patientId, status, start, end, pageable)
                .map(this::mapToDTO);
    }

    // Legacy non-paginated methods (for backward compatibility)
    @Override
    public List<BookingDTO> getBookingsByUserId(Long userId) {
        List<Booking> bookings = bookingRepository.findByUserId(userId);
        return bookings.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDTO> getBookingsByPatientId(Long patientId) {
        List<Booking> bookings = bookingRepository.findByPatientId(patientId);
        return bookings.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDTO> getBookingsByDoctorId(Long doctorId) {
        List<Booking> bookings = bookingRepository.findByDoctorId(doctorId);
        return bookings.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDTO> getBookingsByClinicId(Long clinicId) {
        List<Booking> bookings = bookingRepository.findByClinicId(clinicId);
        return bookings.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDTO> getBookingsByAppointmentType(AppointmentType appointmentType) {
        List<Booking> bookings = bookingRepository.findByAppointmentType(appointmentType);
        return bookings.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDTO> getBookingsByStatus(BookingStatus status) {
        List<Booking> bookings = bookingRepository.findByStatus(status);
        return bookings.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDTO> getBookingsByDateRange(LocalDateTime start, LocalDateTime end) {
        List<Booking> bookings = bookingRepository.findByAppointmentDatetimeBetween(start, end);
        return bookings.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDTO> getBookingsByDoctorIdAndStatus(Long doctorId, BookingStatus status) {
        // Using default page size (should be configurable in production)
        Page<Booking> page = bookingRepository.findByDoctorIdAndStatus(doctorId, status, Pageable.unpaged());
        return page.getContent().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDTO> getBookingsByPatientIdAndStatus(Long patientId, BookingStatus status) {
        Page<Booking> page = bookingRepository.findByPatientIdAndStatus(patientId, status, Pageable.unpaged());
        return page.getContent().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDTO> getBookingsByUserIdAndStatus(Long userId, BookingStatus status) {
        Page<Booking> page = bookingRepository.findByUserIdAndStatus(userId, status, Pageable.unpaged());
        return page.getContent().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDTO> getBookingsByDoctorIdAndDateRange(Long doctorId, LocalDateTime start, LocalDateTime end) {
        Page<Booking> page = bookingRepository.findByDoctorIdAndAppointmentDatetimeBetween(doctorId, start, end, Pageable.unpaged());
        return page.getContent().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDTO> getBookingsByPatientIdAndDateRange(Long patientId, LocalDateTime start, LocalDateTime end) {
        Page<Booking> page = bookingRepository.findByPatientIdAndAppointmentDatetimeBetween(patientId, start, end, Pageable.unpaged());
        return page.getContent().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDTO> getBookingsByDoctorIdAndStatusAndDateRange(Long doctorId, BookingStatus status, LocalDateTime start, LocalDateTime end) {
        List<Booking> bookings = bookingRepository.findByDoctorIdAndStatusAndDateRange(doctorId, status, start, end);
        return bookings.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDTO> getBookingsByPatientIdAndStatusAndDateRange(Long patientId, BookingStatus status, LocalDateTime start, LocalDateTime end) {
        List<Booking> bookings = bookingRepository.findByPatientIdAndStatusAndDateRange(patientId, status, start, end);
        return bookings.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BookingDTO updateBooking(Long id, BookingDTO bookingDTO) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));
        
        // Validate clinic and consultation mode if changing
        if (bookingDTO.getClinicId() != null || bookingDTO.getAppointmentType() != null) {
            Clinic clinic = bookingDTO.getClinicId() != null ? 
                    clinicRepository.findById(bookingDTO.getClinicId())
                            .orElseThrow(() -> new ResourceNotFoundException("Clinic not found with id: " + bookingDTO.getClinicId())) :
                    booking.getClinic();
            
            AppointmentType appointmentType = bookingDTO.getAppointmentType() != null ?
                    bookingDTO.getAppointmentType() : booking.getAppointmentType();
            
            if (clinic != null) {
                // Validate consultation mode
                if (appointmentType == AppointmentType.in_clinic && 
                    clinic.getConsultationMode() == Clinic.ConsultationMode.ONLINE) {
                    throw new IllegalStateException("This clinic only accepts online consultations");
                }
                if (appointmentType == AppointmentType.online && 
                    clinic.getConsultationMode() == Clinic.ConsultationMode.IN_CLINIC) {
                    throw new IllegalStateException("This clinic only accepts in-clinic consultations");
                }
            } else if (appointmentType == AppointmentType.in_clinic) {
                throw new IllegalStateException("Clinic ID is required for in-clinic appointments");
            }
        }
        
        // Update fields only if provided (to prevent nullifying existing data)
        if (bookingDTO.getAppointmentType() != null) {
            booking.setAppointmentType(bookingDTO.getAppointmentType());
        }
        
        if (bookingDTO.getAppointmentDatetime() != null) {
            booking.setAppointmentDatetime(bookingDTO.getAppointmentDatetime());
        }
        
        if (bookingDTO.getReason() != null) {
            booking.setReason(bookingDTO.getReason());
        }
        
        if (bookingDTO.getStatus() != null) {
            booking.setStatus(bookingDTO.getStatus());
        }
        
        if (bookingDTO.getVideoCallLink() != null) {
            booking.setVideoCallLink(bookingDTO.getVideoCallLink());
        }
        
        if (bookingDTO.getBookingNotes() != null) {
            booking.setBookingNotes(bookingDTO.getBookingNotes());
        }
        
        // Update relationships if IDs provided
        if (bookingDTO.getUserId() != null) {
            User user = userRepository.findById(bookingDTO.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + bookingDTO.getUserId()));
            booking.setUser(user);
        }
        
        if (bookingDTO.getPatientId() != null) {
            Patient patient = patientRepository.findById(bookingDTO.getPatientId())
                    .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + bookingDTO.getPatientId()));
            booking.setPatient(patient);
        }
        
        if (bookingDTO.getDoctorId() != null) {
            DoctorProfile doctor = doctorProfileRepository.findById(bookingDTO.getDoctorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + bookingDTO.getDoctorId()));
            booking.setDoctor(doctor);
        }
        
        if (bookingDTO.getClinicId() != null) {
            Clinic clinic = clinicRepository.findById(bookingDTO.getClinicId())
                    .orElseThrow(() -> new ResourceNotFoundException("Clinic not found with id: " + bookingDTO.getClinicId()));
            booking.setClinic(clinic);
        } else if (bookingDTO.getClinicId() == null && bookingDTO.getAppointmentType() == AppointmentType.online) {
            // If appointment type is online and clinic ID is null, set clinic to null
            booking.setClinic(null);
        }
        
        Booking updatedBooking = bookingRepository.save(booking);
        return mapToDTO(updatedBooking);
    }

    @Override
    @Transactional
    public BookingDTO updateBookingStatus(Long id, BookingStatus status) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "id", id));
        
        booking.setStatus(status);
        Booking updatedBooking = bookingRepository.save(booking);
        return mapToDTO(updatedBooking);
    }

    @Override
    @Transactional
    public void deleteBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "id", id));
        bookingRepository.delete(booking);
    }
    
    @Override
    public long countBookingsByDoctorIdAndStatus(Long doctorId, BookingStatus status) {
        return bookingRepository.countByDoctorIdAndStatus(doctorId, status);
    }
    
    @Override
    public long countBookingsByDoctorIdAndDateRange(Long doctorId, LocalDateTime start, LocalDateTime end) {
        return bookingRepository.countByDoctorIdAndDateRange(doctorId, start, end);
    }

    // Improved entity-to-DTO mapping with null checks
    private Booking mapToEntity(BookingDTO bookingDTO) {
        Booking booking = new Booking();
        
        if (bookingDTO.getId() != null) {
            booking.setId(bookingDTO.getId());
        }
        
        booking.setAppointmentType(bookingDTO.getAppointmentType() != null ? 
                bookingDTO.getAppointmentType() : AppointmentType.in_clinic);
                
        booking.setAppointmentDatetime(bookingDTO.getAppointmentDatetime());
        booking.setReason(bookingDTO.getReason());
        
        booking.setStatus(bookingDTO.getStatus() != null ? 
                bookingDTO.getStatus() : BookingStatus.pending);
                
        booking.setVideoCallLink(bookingDTO.getVideoCallLink());
        booking.setBookingNotes(bookingDTO.getBookingNotes());
        
        // Set relationships with error handling
        if (bookingDTO.getUserId() != null) {
            User user = userRepository.findById(bookingDTO.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", bookingDTO.getUserId()));
            booking.setUser(user);
        }
        
        if (bookingDTO.getPatientId() != null) {
            Patient patient = patientRepository.findById(bookingDTO.getPatientId())
                    .orElseThrow(() -> new ResourceNotFoundException("Patient", "id", bookingDTO.getPatientId()));
            booking.setPatient(patient);
        }
        
        if (bookingDTO.getDoctorId() != null) {
            DoctorProfile doctor = doctorProfileRepository.findById(bookingDTO.getDoctorId())
                    .orElseThrow(() -> new ResourceNotFoundException("DoctorProfile", "id", bookingDTO.getDoctorId()));
            booking.setDoctor(doctor);
        }
        
        if (bookingDTO.getClinicId() != null) {
            Clinic clinic = clinicRepository.findById(bookingDTO.getClinicId())
                    .orElseThrow(() -> new ResourceNotFoundException("Clinic", "id", bookingDTO.getClinicId()));
            booking.setClinic(clinic);
        }
        
        return booking;
    }

    private BookingDTO mapToDTO(Booking booking) {
        if (booking == null) {
            return null;
        }
        
        BookingDTO bookingDTO = new BookingDTO();
        
        bookingDTO.setId(booking.getId());
        bookingDTO.setAppointmentType(booking.getAppointmentType());
        bookingDTO.setAppointmentDatetime(booking.getAppointmentDatetime());
        bookingDTO.setReason(booking.getReason());
        bookingDTO.setStatus(booking.getStatus());
        bookingDTO.setVideoCallLink(booking.getVideoCallLink());
        bookingDTO.setBookingNotes(booking.getBookingNotes());
        bookingDTO.setCreatedAt(booking.getCreatedAt());
        bookingDTO.setUpdatedAt(booking.getUpdatedAt());
        
        // Set relationship IDs with null checks
        if (booking.getUser() != null) {
            bookingDTO.setUserId(booking.getUser().getId());
        }
        
        if (booking.getPatient() != null) {
            bookingDTO.setPatientId(booking.getPatient().getId());
        }
        
        if (booking.getDoctor() != null) {
            bookingDTO.setDoctorId(booking.getDoctor().getId());
        }
        
        if (booking.getClinic() != null) {
            bookingDTO.setClinicId(booking.getClinic().getId());
        }
        
        return bookingDTO;
    }
} 