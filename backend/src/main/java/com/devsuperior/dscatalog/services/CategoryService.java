package com.devsuperior.dscatalog.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.exceptions.EntityNotFoundException;
import com.devsuperior.dscatalog.repository.CategoryRepository;

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
            ()-> new EntityNotFoundException("Categoria não encontrada")));

        return categoryDto;
    }

    @Transactional
    public CategoryDTO insert(CategoryDTO dto) {
        
        Category enitity = new Category();
        enitity.setName(dto.getName());
        enitity = repository.save(enitity);

        return new CategoryDTO(enitity);
    }

}
