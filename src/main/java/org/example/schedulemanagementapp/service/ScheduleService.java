package org.example.schedulemanagementapp.service;

import lombok.RequiredArgsConstructor;
import org.example.schedulemanagementapp.dto.ScheduleRequestDto;
import org.example.schedulemanagementapp.dto.ScheduleResponseDto;
import org.example.schedulemanagementapp.entity.Schedule;
import org.example.schedulemanagementapp.repository.ScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    @Transactional
    public ScheduleResponseDto save(ScheduleRequestDto scheduleRequestDto) {
        Schedule schedule = new Schedule(
                scheduleRequestDto.getTitle(),
                scheduleRequestDto.getContents(),
                scheduleRequestDto.getName(),
                scheduleRequestDto.getPassword()
        );
        Schedule savedMember = scheduleRepository.save(schedule);

        return new ScheduleResponseDto(savedMember.getId(), savedMember.getTitle(), savedMember.getContents(), savedMember.getName(), savedMember.getCreatedAt(), savedMember.getModifiedAt());
    }

    @Transactional(readOnly = true)
    public List<ScheduleResponseDto> findAll(String name) {
        List<Schedule> scheduleList;

        if (name != null && !name.isEmpty()) {
            scheduleList = scheduleRepository.findByName(name);
        } else {
            scheduleList = scheduleRepository.findAll();
        }

        return scheduleList.stream()
                .map(schedule -> new ScheduleResponseDto(
                        schedule.getId(),
                        schedule.getTitle(),
                        schedule.getContents(),
                        schedule.getName(),
                        schedule.getCreatedAt(),
                        schedule.getModifiedAt()))
                .sorted(Comparator.comparing(ScheduleResponseDto::getModifiedAt).reversed())
                .collect(Collectors.toList());
    }
}
