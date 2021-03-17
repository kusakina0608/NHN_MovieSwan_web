package com.nhn.rookie8.movieswanticketapp.service;

import com.nhn.rookie8.movieswanticketapp.dto.DiscountDTO;
import com.nhn.rookie8.movieswanticketapp.dto.ScheduleDTO;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    public DiscountDTO getDiscount(int hour){
        if(hour < 9){
            return DiscountDTO.builder()
                    .discountType("조조 할인(오전 09:00 이전)")
                    .discountRatio(0.25)
                    .build();
        }
        else{
            return DiscountDTO.builder()
                    .discountType("없음")
                    .discountRatio(0.0)
                    .build();
        }
    }

    @Override
    public TimetableDTO getTimetable(String timeTableId) {
        try {
            Optional<Timetable> result = timetableRepository.findById(timeTableId);

            log.info("Search Result : {}", result.isPresent() ? result.get() : "No Result");

            return result.map(this::entityToDTO).orElse(null);

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

    public List<ScheduleDTO> getMovieScheduleDetail(List<TimetableDTO> timetableDTOList){

        //List<TimetableDTO> -> Map<String, List<ScheduleDTO.Detail>> -> List<ScheduleDTO>
        return timetableDTOList.stream().collect(
                Collectors.groupingBy(
                        item->item.getStartTime().toLocalDate(),
                        Collectors.mapping(item->
                                        ScheduleDTO.Detail.builder()
                                                .time(item.getStartTime().toLocalTime())
                                                .timeTableId(item.getTimetableId())
                                                .build()
                                ,Collectors.toList()
                        )
                )
        ).entrySet().stream().map(item->
                ScheduleDTO.builder()
                        .date(item.getKey())
                        .detail(item.getValue())
                        .build()
        ).collect(Collectors.toList());

    }
}
