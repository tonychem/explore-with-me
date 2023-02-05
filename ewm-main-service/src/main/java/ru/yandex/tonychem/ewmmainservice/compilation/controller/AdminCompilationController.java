package ru.yandex.tonychem.ewmmainservice.compilation.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.tonychem.ewmmainservice.compilation.model.dto.NewCompilationDto;
import ru.yandex.tonychem.ewmmainservice.compilation.model.dto.UpdateCompilationRequest;
import ru.yandex.tonychem.ewmmainservice.compilation.service.AdminCompilationService;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/admin/compilations", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AdminCompilationController {
    private final AdminCompilationService adminCompilationService;

    @PostMapping
    public ResponseEntity<Object> createCompilation(@RequestBody @Valid NewCompilationDto newCompilationDto) {
        return adminCompilationService.createCompilation(newCompilationDto);
    }

    @DeleteMapping("/{compId}")
    public ResponseEntity<Void> deleteCompilation(@PathVariable long compId) {
        return adminCompilationService.deleteCompilation(compId);
    }

    @PatchMapping("/{compId}")
    public ResponseEntity<Object> updateCompilation(@PathVariable long compId,
                                  @RequestBody @Valid UpdateCompilationRequest updateCompilationRequest) {
        return adminCompilationService.updateCompilation(compId, updateCompilationRequest);
    }
}
