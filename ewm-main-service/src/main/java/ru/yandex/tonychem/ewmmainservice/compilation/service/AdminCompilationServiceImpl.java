package ru.yandex.tonychem.ewmmainservice.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.yandex.tonychem.ewmmainservice.compilation.model.dto.NewCompilationDto;
import ru.yandex.tonychem.ewmmainservice.compilation.model.dto.UpdateCompilationRequest;


@Service
@RequiredArgsConstructor
public class AdminCompilationServiceImpl implements AdminCompilationService {
    @Override
    public ResponseEntity<Object> createCompilation(NewCompilationDto newCompilationDto) {
        return null;
    }

    @Override
    public ResponseEntity<Void> deleteCompilation(long compId) {
        return null;
    }

    @Override
    public ResponseEntity<Object> updateCompilation(long compId, UpdateCompilationRequest updateCompilationRequest) {
        return null;
    }
}
