package com.devsuperior.dscatalog.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.repository.CategoryRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;

    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll() {
        
        //Usando funcao lambda para converter category para categoryDto
        return repository.findAll().stream().map(x -> new CategoryDTO(x)).toList();
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

}
