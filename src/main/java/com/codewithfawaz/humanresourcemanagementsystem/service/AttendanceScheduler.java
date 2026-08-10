package com.codewithfawaz.humanresourcemanagementsystem.service;

import com.codewithfawaz.humanresourcemanagementsystem.model.AttendanceRecord;
import com.codewithfawaz.humanresourcemanagementsystem.model.AttendanceStatus;
import com.codewithfawaz.humanresourcemanagementsystem.repository.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceScheduler {
    private final AttendanceRepository attendanceRepository;

    @Scheduled(cron = "0 0 19 * * *")
    void autoCloseTimesheets(){
        List<AttendanceRecord> attendanceRecords = attendanceRepository.findByDateAndClockOutTime(LocalDate.now(), null);
        LocalTime closingTime = LocalTime.of(17,0);
        attendanceRecords = attendanceRecords.stream().map(record -> {
            record.setClockOutTime(closingTime);
            record.setStatus(AttendanceStatus.SYSTEM_CLOSED);
            return record;
        }).collect(Collectors.toList());
        attendanceRepository.saveAll(attendanceRecords);
    }
}