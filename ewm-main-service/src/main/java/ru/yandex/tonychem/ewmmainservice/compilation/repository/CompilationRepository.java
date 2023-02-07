package ru.yandex.tonychem.ewmmainservice.compilation.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.tonychem.ewmmainservice.compilation.model.entity.Compilation;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    List<Compilation> findCompilationsByPinnedTrue(Pageable pageable);

    List<Compilation> findCompilationsByPinnedFalse(Pageable pageable);
}
