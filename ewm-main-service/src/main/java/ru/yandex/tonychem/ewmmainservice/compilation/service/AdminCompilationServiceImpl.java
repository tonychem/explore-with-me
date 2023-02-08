package ru.yandex.tonychem.ewmmainservice.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.tonychem.ewmmainservice.compilation.model.dto.NewCompilationDto;
import ru.yandex.tonychem.ewmmainservice.compilation.model.dto.UpdateCompilationRequest;
import ru.yandex.tonychem.ewmmainservice.compilation.model.entity.Compilation;
import ru.yandex.tonychem.ewmmainservice.compilation.model.mapper.CompilationMapper;
import ru.yandex.tonychem.ewmmainservice.compilation.repository.CompilationRepository;
import ru.yandex.tonychem.ewmmainservice.event.model.entity.Event;
import ru.yandex.tonychem.ewmmainservice.event.repository.EventRepository;
import ru.yandex.tonychem.ewmmainservice.exception.exceptions.NoSuchCompilationException;
import ru.yandex.tonychem.ewmmainservice.participation.repository.ParticipationRepository;
import statistics.client.StatisticsClient;

import java.util.Collections;
import java.util.List;


@Service
@RequiredArgsConstructor
public class AdminCompilationServiceImpl implements AdminCompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final StatisticsClient statisticsClient;
    private final ParticipationRepository participationRepository;

    @Override
    public ResponseEntity<Object> createCompilation(NewCompilationDto newCompilationDto) {
        Compilation compilation = new Compilation();

        compilation.setTitle(newCompilationDto.getTitle());
        compilation.setPinned(newCompilationDto.getPinned());

        List<Event> eventList;

        if (newCompilationDto.getEvents() != null) {
            eventList = eventRepository.findAllById(newCompilationDto.getEvents());
        } else {
            eventList = Collections.emptyList();
        }

        compilation.setEvents(eventList);

        Compilation savedCompilation = compilationRepository.save(compilation);

        return new ResponseEntity<>(CompilationMapper.toCompilationDto(savedCompilation, participationRepository,
                statisticsClient), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> deleteCompilation(long compId) {
        compilationRepository.deleteById(compId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    @Transactional
    public ResponseEntity<Object> updateCompilation(long compId, UpdateCompilationRequest updateCompilationRequest) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(
                () -> new NoSuchCompilationException("Compilation with id=" + compId + " was not found")
        );

        compilation = CompilationMapper.updateCompilationFields(updateCompilationRequest, compilation, eventRepository);

        Compilation savedCompilation = compilationRepository.save(compilation);
        return new ResponseEntity<>(CompilationMapper.toCompilationDto(savedCompilation, participationRepository,
                statisticsClient), HttpStatus.OK);
    }
}
