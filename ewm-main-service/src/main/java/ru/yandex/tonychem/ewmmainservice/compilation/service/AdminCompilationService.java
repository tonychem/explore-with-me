package ru.yandex.tonychem.ewmmainservice.compilation.service;

import org.springframework.http.ResponseEntity;
import ru.yandex.tonychem.ewmmainservice.compilation.model.dto.NewCompilationDto;
import ru.yandex.tonychem.ewmmainservice.compilation.model.dto.UpdateCompilationRequest;

public interface AdminCompilationService {

    ResponseEntity<Object> createCompilation(NewCompilationDto newCompilationDto);

    ResponseEntity<Void> deleteCompilation(long compId);

    ResponseEntity<Object> updateCompilation(long compId, UpdateCompilationRequest updateCompilationRequest);
}
