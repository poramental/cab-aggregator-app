package com.modsen.driverservice.services;

import com.modsen.driverservice.dto.AutoDto;
import com.modsen.driverservice.entities.Auto;
import com.modsen.driverservice.exceptions.AutoAlreadyExistException;
import com.modsen.driverservice.exceptions.AutoNotFoundException;
import com.modsen.driverservice.exceptions.SortTypeException;
import com.modsen.driverservice.mappers.AutoMapper;
import com.modsen.driverservice.repositories.AutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AutoService {

    private final AutoRepository autoRepository;

    private final AutoMapper autoMapper;


    public ResponseEntity<List<AutoDto>> getAll(){
        return ResponseEntity.ok(autoRepository.findAll().stream()
                .map(autoMapper::entityToDto).collect(Collectors.toList()));
    }

    public HttpStatus add(AutoDto autoDto) throws AutoAlreadyExistException{
        checkAutoExist(autoDto);
        autoRepository.save(autoMapper.dtoToEntity(autoDto));
        return HttpStatus.OK;
    }

    public void checkAutoExist(AutoDto autoDto) throws AutoAlreadyExistException{

        if(autoRepository.findByNumber(autoDto.getNumber()).isPresent()){
            throw new AutoAlreadyExistException(String.format("auto with number: %s is present.",
                    autoDto.getNumber()));
        }
    }

    public HttpStatus deleteByNumber(String number) throws AutoNotFoundException{
        Optional<Auto> auto_opt = autoRepository.findByNumber(number);

        if(auto_opt.isPresent()){
            autoRepository.delete(auto_opt.get());
            return HttpStatus.OK;
        }else
            throw new AutoNotFoundException(String.format("auto with number: %s is not found.", number));
    }

    public HttpStatus update(AutoDto autoDto) throws AutoNotFoundException{
        Optional<Auto> auto_opt = autoRepository.findByNumber(autoDto.getNumber());
        if(auto_opt.isPresent()){
            Auto auto_db = auto_opt.get();

            if(Objects.nonNull(autoDto.getNumber()) && !autoDto.getNumber().isEmpty()){
                auto_db.setNumber(autoDto.getNumber());
            }

            if(Objects.nonNull(autoDto.getColor()) && !autoDto.getColor().isEmpty()){
                auto_db.setColor(autoDto.getColor());
            }

            if(Objects.nonNull(autoDto.getModel()) && !autoDto.getModel().isEmpty()){
                auto_db.setModel(autoDto.getModel());
            }

            return HttpStatus.OK;
        }else
            throw new AutoNotFoundException(String.format("auto with number: %s is not found",
                autoDto.getNumber()));
    }

    public ResponseEntity<AutoDto> getByNumber(String number)throws AutoNotFoundException{
        Optional<Auto> auto_opt = autoRepository.findByNumber(number);
        if(auto_opt.isPresent()) return ResponseEntity.ok(autoMapper.entityToDto(auto_opt.get()));
        throw new AutoNotFoundException(String.format("auto with number :%s is not found.", number));
    }

    public ResponseEntity<AutoDto> getById(Long id) throws AutoNotFoundException{
        Optional<Auto> auto_opt = autoRepository.findById(id);
        if(auto_opt.isPresent()) return ResponseEntity.ok(autoMapper.entityToDto(auto_opt.get()));
        throw new AutoNotFoundException(String.format("auto with id :%s is not found.", id));
    }

   public HttpStatus deleteById(Long id) throws AutoNotFoundException {
        Optional<Auto> auto_opt = autoRepository.findById(id);
        if(auto_opt.isPresent()){
            autoRepository.delete(auto_opt.get());
            return HttpStatus.OK;
        } throw new AutoNotFoundException(String.format("auto with id: %s is not found", id));
   }

   public ResponseEntity<List<AutoDto>> getSortedList(String type)
           throws SortTypeException{
       List<Auto> autos = switch (type.toLowerCase()) {
           case "model" ->
                   autoRepository.findAll(Sort.by(Sort.Order.asc("model")));
           case "color" ->
                   autoRepository.findAll(Sort.by(Sort.Order.asc("color")));
           default ->
                   throw new SortTypeException("Invalid type of sort");
       };
       return new ResponseEntity<>(autos.stream()
               .map(autoMapper::entityToDto)
               .collect(Collectors.toList()),
               HttpStatus.OK);
   }

}

