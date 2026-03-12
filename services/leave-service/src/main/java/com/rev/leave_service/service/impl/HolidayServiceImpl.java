package com.rev.leave_service.service.impl;

import com.rev.leave_service.entity.Holiday;
import com.rev.leave_service.repository.HolidayRepository;
import com.rev.leave_service.service.HolidayService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class HolidayServiceImpl implements HolidayService {

    private final HolidayRepository holidayRepository;

    @Override
    public Holiday createHoliday(LocalDate date, String name, String description) {
        Holiday holiday = new Holiday();
        holiday.setHolidayDate(date);
        holiday.setName(name);
        holiday.setDescription(description);
        return holidayRepository.save(holiday);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Holiday> getAllHolidays() {
        return holidayRepository.findAll();
    }

    @Override
    public void deleteHoliday(Long id) {
        holidayRepository.deleteById(id);
    }
}
