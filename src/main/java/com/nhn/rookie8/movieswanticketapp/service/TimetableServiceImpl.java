package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.TimetableDTO;
import com.nhn.rookie8.movieswanticketapp.dto.TimetableInputDTO;
import com.nhn.rookie8.movieswanticketapp.entity.QTimetable;
import com.nhn.rookie8.movieswanticketapp.entity.Timetable;
import com.nhn.rookie8.movieswanticketapp.repository.TimetableRepository;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class TimetableServiceImpl implements TimetableService {
    private final TimetableRepository timetableRepository;

    @Override
    public String registerTimetable(TimetableInputDTO timetableInputDTO) {
        try {
            Timetable timetable = dtoToEntity(timetableInputDTO);
            timetableRepository.save(timetable);

            log.info("Registered Entity : {}", timetable);

            return timetable.getTimetableId();
        } catch (Exception e) {
            log.error(e);
            return "";
        }
    }

    @Override
    public String deleteTimetable(String timeTableId) {
        try {
            timetableRepository.deleteById(timeTableId);

            log.info("Deleted TimetableID : {}", timeTableId);

            return timeTableId;
        } catch (Exception e) {
            log.error(e);
            return "";
        }
    }

    @Override
    public TimetableDTO getTimetable(String timeTableId) {
        try {
            Optional<Timetable> result = timetableRepository.findById(timeTableId);

            log.info("Search Result : {}", result.isPresent() ? result.get() : "No Result");

            return result.isPresent() ? entityToDTO(result.get()) : null;
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    @Override
    public List<TimetableDTO> getAllTimetableOfMovie(String movieId) {
        try {
            BooleanBuilder booleanBuilder = getUnScreened(movieId);
            Pageable pageable = PageRequest.of(0, 1000, Sort.by("startTime").ascending());

            List<Timetable> result = timetableRepository.findAll(booleanBuilder, pageable).toList();
            List<TimetableDTO> schedulesList = new ArrayList<>();

            log.info("Search Results : {}", result);

            result.forEach(e -> schedulesList.add(entityToDTO(e)));
            return schedulesList;
        } catch (Exception e) {
            log.error(e);
            return Collections.emptyList();
        }
    }

    private BooleanBuilder getUnScreened(String movieId) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QTimetable qTimetable = QTimetable.timetable;

        booleanBuilder.and(qTimetable.movieId.eq(movieId));
        booleanBuilder.and(qTimetable.startTime.gt(LocalDateTime.now()));

        return booleanBuilder;
    }
}