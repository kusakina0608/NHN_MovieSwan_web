package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.TimetableDTO;
import com.nhn.rookie8.movieswanticketapp.dto.TimetableInputDTO;
import com.nhn.rookie8.movieswanticketapp.entity.Timetable;
import com.nhn.rookie8.movieswanticketapp.repository.TimetableRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class TimetableServiceImpl implements TimetableService {
    private final TimetableRepository repository;

    @Override
    public String registerTimetable(TimetableInputDTO timetableInputDTO) {
        try {
            Timetable entity = dtoToEntity(timetableInputDTO);
            log.info("Entity : {}", entity);
            repository.save(entity);
            return entity.getTimetableId();
        } catch (Exception e) {
            log.error(e);
            return "";
        }
    }

    @Override
    public String deleteTimetable(String timeTableId) {
        try {
            log.info("Deleted tid : {}", timeTableId);
            repository.deleteById(timeTableId);
            return timeTableId;
        } catch (Exception e) {
            log.error(e);
            return "";
        }
    }

    @Override
    public TimetableDTO getTimetable(String timeTableId) {
        try {
            Optional<Timetable> result = repository.findById(timeTableId);
            return result.isPresent() ? entityToDTO(result.get()) : null;
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    @Override
    public List<TimetableDTO> getAllTimetable(String mid) {
        try {
            List<Timetable> result = repository.findByMovieIdOrderByStartTimeAsc(mid);
            List<TimetableDTO> schedulesList = new ArrayList<>();

            result.forEach(e -> schedulesList.add(entityToDTO(e)));
            return schedulesList;
        } catch (Exception e) {
            log.error(e);
            return Collections.emptyList();
        }
    }
}