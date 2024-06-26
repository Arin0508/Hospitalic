package com.had.hospital_management.service;

import com.had.hospital_management.model.Appointment;
import com.had.hospital_management.repository.AppointmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentService {
    @Autowired
    private AppointmentRepository appointmentRepository;
    public Appointment save(Appointment appointment){
        return appointmentRepository.save(appointment);
    }

    // NONE
    public List<Appointment>findAll(){
        return appointmentRepository.findAll();
    }

    // NONE
    public Appointment getById(Long id){
        return appointmentRepository.findById(id).orElse(null);
    }

    // Doctor
    public List<Appointment>getAppointmentByDoctorId(Long id){
        return appointmentRepository.findAppointmentByDoctorId(id);
    }

    // Pat
    public List<Appointment>getAppointmentByPatientId(Long id){
        return appointmentRepository.findAppointmentByPatientId(id);
    }

    // Lab
    public List<Appointment>getAppointmentByLabId(Long id){
        return appointmentRepository.findAppointmentByLabId(id);
    }

    // Doc
    @Transactional
    public void assignLab(Long lab_id,Long id){
        appointmentRepository.assignLab(lab_id,id);
    }
    
    // Lab
    @Transactional
    public void updateDoctorStatus(Long id){
        appointmentRepository.updateDoctorStatus(id);
    }
    @Transactional
    public void updateLabStatus(Long id){
        appointmentRepository.updateLabStatus(id);
    }
    @Transactional
    public void addPrescription(Long id,String pres){
        appointmentRepository.addPrescription(id,pres);
    }
    @Transactional
    public void addLabPrescription(Long id,String pres){
        appointmentRepository.addLabPrescription(id,pres);
    }
    // None
    @Transactional
    public void deleteById(Long id) {
        Appointment appointment = appointmentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("appointment with id " + id + " not found"));
        appointmentRepository.delete(appointment);
    }


}