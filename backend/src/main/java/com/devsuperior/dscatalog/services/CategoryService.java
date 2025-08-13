package com.devsuperior.dscatalog.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.exceptions.DatabaseException;
import com.devsuperior.dscatalog.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.repository.CategoryRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;

    @Transactional(readOnly = true)
    public Page<CategoryDTO> findAllPaged(Pageable pageable) {
        //Usando funcao lambda para converter category para categoryDto
        return repository.findAll(pageable).map(CategoryDTO::new);
    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
        
        CategoryDTO categoryDto = new CategoryDTO(repository.findById(id).orElseThrow(
            ()-> new ResourceNotFoundException("Categoria não encontrada")));

        return categoryDto;
    }

    @Transactional
    public CategoryDTO insert(CategoryDTO dto) {
        
        Category enitity = new Category();
        enitity.setName(dto.getName());
        enitity = repository.save(enitity);

        return new CategoryDTO(enitity);
    }


    @Transactional
    public CategoryDTO update(Long id, CategoryDTO dto) {
        
        try {
            Category entity = repository.getReferenceById(id);
            entity.setName(dto.getName());
            entity = repository.save(entity);
            return new CategoryDTO(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Categoria não encontrada, id: " + id);
        }

    }

   @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {

        if (!repository.existsById(id)){
            throw new ResourceNotFoundException("Categoria não encontrada, id: " + id);
        }

        try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Falha de integridade referencial");
        }
    }

}
