package com.mono.app.service;

import com.mono.app.dto.request.LabelRequest;
import com.mono.app.dto.response.LabelResponse;
import com.mono.app.exceptions.BusinessFault;
import com.mono.app.exceptions.types.ErrorCode;
import com.mono.app.exceptions.types.ErrorType;
import com.mono.app.model.Label;
import com.mono.app.model.User;
import com.mono.app.repository.LabelRepository;
import com.mono.app.utils.mapper.types.Mapper;
import com.mono.app.utils.security.AuthenticatedUserResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Service
public class LabelService {
    private final LabelRepository repository;
    private final Mapper<Label, LabelResponse> mapper;
    private final AuthenticatedUserResolver resolver;

    @Autowired
    public LabelService(
            LabelRepository repository,
            @Qualifier("labelMapper")
            Mapper<Label, LabelResponse> mapper,
            AuthenticatedUserResolver resolver
    ) {
        this.repository = repository;
        this.mapper = mapper;
        this.resolver = resolver;
    }

    public Set<LabelResponse> getAllUserLabels() {
        User user = resolver.getAuthenticatedUser();

        return repository.findAllUserLabels(user.getId()).stream().map(mapper::toDTO).collect(toSet());
    }

    public Optional<Label> getUserLabel(String name, Long userId) {
        return repository.findUserLabel(name, userId);
    }

    public LabelResponse addLabel(LabelRequest request) {
        User user = resolver.getAuthenticatedUser();

        final Optional<Label> existingLabel = repository.findUserLabel(request.name(), user.getId());

        if (existingLabel.isPresent()) {
            throw new BusinessFault(
                    String.format("Label with name %s already exists", request.name()),
                    ErrorCode.E002,
                    ErrorType.ERROR);
        } else {
            Label label = Label.builder()
                    .name(request.name())
                    .isEnabled(true)
                    .user(user)
                    .build();

            label = repository.save(label);

            return mapper.toDTO(label);
        }
    }
}
