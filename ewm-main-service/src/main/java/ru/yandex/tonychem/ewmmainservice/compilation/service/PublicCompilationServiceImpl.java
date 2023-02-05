package ru.yandex.tonychem.ewmmainservice.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.yandex.tonychem.ewmmainservice.compilation.model.dto.CompilationDto;
import ru.yandex.tonychem.ewmmainservice.compilation.model.entity.Compilation;
import ru.yandex.tonychem.ewmmainservice.compilation.model.mapper.CompilationMapper;
import ru.yandex.tonychem.ewmmainservice.compilation.repository.CompilationRepository;
import ru.yandex.tonychem.ewmmainservice.exception.exceptions.NoSuchCompilationException;
import ru.yandex.tonychem.ewmmainservice.participation.repository.ParticipationRepository;
import statistics.client.StatisticsClient;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicCompilationServiceImpl implements PublicCompilationService {
    private final CompilationRepository compilationRepository;
    private final ParticipationRepository participationRepository;
    private final StatisticsClient statisticsClient;

    @Override
    public ResponseEntity<Object> compilationsBy(Boolean pinned, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        List<Compilation> compilationList;

        if (pinned) {
            compilationList = compilationRepository.findCompilationsByPinnedTrue(pageable);
        } else {
            compilationList = compilationRepository.findAll(pageable).toList();
        }

        List<CompilationDto> compilationDtoList = compilationList.stream()
                .map(compilation -> CompilationMapper.toCompilationDto(compilation, participationRepository,
                        statisticsClient))
                .collect(Collectors.toList());
        return new ResponseEntity<>(compilationDtoList, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> compilationById(long compId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(
                () -> new NoSuchCompilationException("Compilation with id=" + compId + " was not found")
        );

        return new ResponseEntity<>(CompilationMapper.toCompilationDto(compilation, participationRepository,
                statisticsClient), HttpStatus.OK);
    }
}
