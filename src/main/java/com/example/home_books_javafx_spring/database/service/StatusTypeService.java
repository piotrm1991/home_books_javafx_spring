package com.example.home_books_javafx_spring.database.service;

import com.example.home_books_javafx_spring.config.FieldsConfig;
import com.example.home_books_javafx_spring.database.entities.Publisher;
import com.example.home_books_javafx_spring.database.entities.StatusType;
import com.example.home_books_javafx_spring.database.repository.StatusTypeRepository;
import com.example.home_books_javafx_spring.dto.DtoMapper;
import com.example.home_books_javafx_spring.dto.models.StatusTypeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.home_books_javafx_spring.config.FieldsConfig.DEFAULT_PUBLISHER_NAME;

@Service
public class StatusTypeService {

    @Autowired
    StatusTypeRepository statusTypeRepository;

    @Autowired
    DtoMapper dtoMapper;

    @Transactional
    public void addStatusType(StatusType statusType) {
        this.statusTypeRepository.save(statusType);
    }

    @Transactional
    public void addStatusType(StatusTypeDto statusTypeDto) {
        StatusType statusType = this.dtoMapper.fromStatusTypeDto(statusTypeDto);
        this.statusTypeRepository.save(statusType);
    }

    public List<StatusType> getAllStatusTypes() {
        return this.statusTypeRepository.findAll();
    }

    public StatusType getById(Integer id) {
        return this.statusTypeRepository.findById(id).get();
    }

    public List<StatusTypeDto> getAllStatusTypesDto() {
        List<StatusType> statusTypes = this.statusTypeRepository.findAll();
        List<StatusTypeDto> statusTypesDto = new ArrayList<>();
        statusTypes.stream().forEach(statusType -> statusTypesDto.add(this.dtoMapper.fromStatusType(statusType)));
        return statusTypesDto;
    }

    @Transactional
    public void deleteStatusType(Integer id) {
        this.statusTypeRepository.deleteById(id);
    }

    private StatusType getDefaultStatusType() {
        return this.statusTypeRepository.findStatusTypeByName(FieldsConfig.DEFAULT_STATUS_TYPE).stream().findAny().get();
    }

    @Transactional
    @PostConstruct
    public void prepareDefaultStatusTypes() {
        List<String> defaultStatusTypes = new ArrayList<>();
        defaultStatusTypes.add(FieldsConfig.DEFAULT_STATUS_TYPE);
        defaultStatusTypes.add(FieldsConfig.DEFAULT_STATUS_TYPE_BORROWED);
        defaultStatusTypes.add(FieldsConfig.DEFAULT_STATUS_TYPE_HOME);
        defaultStatusTypes.stream().forEach(s -> {
            Optional<StatusType> first = this.statusTypeRepository.findStatusTypeByName(s).stream().findFirst();
            if (first.isEmpty()) {
                this.statusTypeRepository.save(StatusType.builder().name(s).build());
            }
        });
    }
}
