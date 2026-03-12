package com.rev.leave_service.service;

import com.rev.leave_service.entity.Holiday;
import java.time.LocalDate;
import java.util.List;

public interface HolidayService {
    Holiday createHoliday(LocalDate date, String name, String description);
    List<Holiday> getAllHolidays();
    void deleteHoliday(Long id);
}
