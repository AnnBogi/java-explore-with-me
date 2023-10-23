package ru.practicum.ewm.mainservice.repository.helper;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import ru.practicum.mainservice.exception.NotFoundException;

import java.util.Optional;

@NoRepositoryBean
public interface RepositoryHelper<T, ID> extends JpaRepository<T, ID> {
    default T findByIdOrThrowNotFoundException(ID id, String type) {
        Optional<T> optional = findById(id);
        return optional.orElseThrow(
                () -> new NotFoundException("{0} with ID: {1} was not found", type, id)
        );
    }
}