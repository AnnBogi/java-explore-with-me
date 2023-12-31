package ru.practicum.ewm.mainservice.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.UserDto;
import ru.practicum.ewm.mainservice.category.dto.CategoryDto;
import ru.practicum.ewm.mainservice.category.dto.NewCategoryDto;
import ru.practicum.ewm.mainservice.category.entity.Category;
import ru.practicum.ewm.mainservice.category.mapper.CategoryMapper;
import ru.practicum.ewm.mainservice.category.service.CategoryService;
import ru.practicum.ewm.mainservice.compilation.dto.CompilationDto;
import ru.practicum.ewm.mainservice.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.mainservice.compilation.dto.UpdateCompilationDTO;
import ru.practicum.ewm.mainservice.compilation.entity.Compilation;
import ru.practicum.ewm.mainservice.compilation.mapper.CompilationMapper;
import ru.practicum.ewm.mainservice.compilation.service.CompilationService;
import ru.practicum.ewm.mainservice.event.dto.EventFullDto;
import ru.practicum.ewm.mainservice.event.dto.ResponseEventDto;
import ru.practicum.ewm.mainservice.event.dto.UpdateEventAdminRequest;
import ru.practicum.ewm.mainservice.event.entity.Event;
import ru.practicum.ewm.mainservice.event.mapper.EventMapper;
import ru.practicum.ewm.mainservice.event.service.EventService;
import ru.practicum.ewm.mainservice.repository.AdminEventRequestParameters;
import ru.practicum.ewm.mainservice.user.entity.User;
import ru.practicum.ewm.mainservice.user.mapper.UserMapper;
import ru.practicum.ewm.mainservice.user.service.UserService;
import ru.practicum.ewm.mainservice.util.Marker;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Validated
public class AdminController {

    private final CategoryService categoryService;
    private final CompilationService compilationService;
    private final EventService eventService;
    private final UserService userService;
    private final CategoryMapper categoryMapper;
    private final CompilationMapper compilationMapper;
    private final EventMapper eventMapper;
    private final UserMapper userMapper;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("admin/users")
    public UserDto createUser(@Validated @RequestBody UserDto userDto) {
        final User user = userMapper.fromRequest(userDto);
        User createdUser = userService.addUser(user);
        return userMapper.toDto(createdUser);
    }

    @GetMapping("admin/users")
    public List<UserDto> getUsers(@RequestParam(required = false) List<Long> ids,
                                  @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                  @Positive @RequestParam(defaultValue = "10") int size) {
        final List<User> users;
        if (ids == null) {
            users = userService.findAll(from, size);
        } else {
            users = userService.findByIds(ids);
        }

        return users.stream()
                    .map(userMapper::toDto)
                    .collect(Collectors.toList());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("admin/users/{userId}")
    public void deleteUser(@PathVariable @Positive long userId) {
        userService.deleteById(userId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("admin/categories")
    public CategoryDto createUser(@Validated @RequestBody NewCategoryDto categoryDto) {
        Category createdCategory = categoryService.addCategory(categoryMapper.fromRequest(categoryDto));
        return categoryMapper.toDto(createdCategory);
    }

    @PatchMapping("admin/categories/{categoryId}")
    public CategoryDto updateUser(@Positive @PathVariable long categoryId,
                                  @Validated @RequestBody CategoryDto categoryDto) {
        Category updatedCategory = categoryMapper.fromRequest(categoryDto);
        updatedCategory.setId(categoryId);
        updatedCategory = categoryService.updateCategory(updatedCategory);
        return categoryMapper.toDto(updatedCategory);
    }

    @GetMapping("admin/events")
    public List<EventFullDto> getEvents(@Validated AdminEventRequestParameters adminEventRequestParameters) {
        List<Event> events = eventService.findEventsForAdmin(adminEventRequestParameters);
        return events.stream()
                .map(eventMapper::toFullDto)
                .collect(Collectors.toList());
    }

    @PatchMapping("admin/events/{eventId}")
    public ResponseEventDto editEvent(@PathVariable @Positive long eventId,
                                      @Validated(Marker.OnUpdate.class) @RequestBody UpdateEventAdminRequest updateEventDto) {
        Event event = eventMapper.fromUpdateAdminRequest(updateEventDto, eventId);
        event = eventService.updateEventByAdmin(event);
        return eventMapper.toFullDto(event);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("admin/categories/{categoryId}")
    public void deleteCategoryById(@Positive @PathVariable long categoryId) {
        categoryService.deleteById(categoryId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("admin/compilations")
    public CompilationDto postCompilation(@RequestBody @Validated(Marker.OnCreation.class) NewCompilationDto compilationDto) {
        if (compilationDto.getPinned() == null) {
            compilationDto.setPinned(false);
        }
        Compilation compilation = compilationMapper.fromDto(compilationDto);
        compilation = compilationService.postCompilation(compilation);
        return compilationMapper.toDto(compilation);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("admin/compilations/{compilationId}")
    public void deleteCompilation(@PathVariable @Positive long compilationId) {
        compilationService.deleteCompilation(compilationId);
    }

    @PatchMapping("admin/compilations/{compilationId}")
    public CompilationDto patchCompilation(@PathVariable @Positive long compilationId,
                                           @RequestBody @Validated(Marker.OnUpdate.class) UpdateCompilationDTO compilationDTO) {
        Compilation compilation = compilationMapper.fromDto(compilationDTO);
        compilation.setId(compilationId);
        compilation = compilationService.patchCompilation(compilation);
        return compilationMapper.toDto(compilation);
    }
}