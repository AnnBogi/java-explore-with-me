package ru.practicum.ewm.mainservice.compilation.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.mainservice.compilation.entity.Compilation;
import ru.practicum.ewm.mainservice.compilation.repository.CompilationRepository;
import ru.practicum.ewm.mainservice.compilation.repository.CompilationRepositoryCustom;
import ru.practicum.ewm.mainservice.event.entity.Event;
import ru.practicum.ewm.mainservice.event.repository.EventRepository;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CompilationService {
    private final CompilationRepository compilationRepository;
    private final CompilationRepositoryCustom compilationRepositoryCustom;
    private final EventRepository eventRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    @Transactional
    public Compilation postCompilation(Compilation compilation) {
        compilation = compilationRepository.save(compilation);
        List<Long> eventIds = compilation.getEvents()
                                         .stream()
                                         .map(Event::getId)
                                         .collect(Collectors.toList());
        List<Event> events = eventRepository.findAllById(eventIds);
        if (!events.isEmpty()) {
            compilation.setEvents(events);
        }
        return compilation;
    }

    public List<Compilation> getCompilations(Boolean pinned, int from, int size) {
        return compilationRepositoryCustom.findCompilations(pinned, from, size);
    }

    public Compilation getCompilation(long compilationId) {
        return compilationRepository.findByIdOrThrowNotFoundException(compilationId, "Compilation");
    }

    @Transactional
    public boolean deleteCompilation(long compilationId) {
        compilationRepository.deleteById(compilationId);
        return compilationRepository.findById(compilationId).isEmpty();
    }

    @Transactional
    public Compilation patchCompilation(Compilation updateCompilation) {
        Compilation compilation = compilationRepository.findByIdOrThrowNotFoundException(updateCompilation.getId(),
                "Compilation");
        modelMapper.getConfiguration()
                .setPropertyCondition(Conditions.isNotNull());
        modelMapper.map(updateCompilation, compilation);
        return compilationRepository.save(compilation);
    }
}