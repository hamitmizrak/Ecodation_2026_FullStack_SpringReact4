package com.hamitmizrak.business.services.impl;

import com.hamitmizrak.bean.ModelMapperBean;
import com.hamitmizrak.business.dto.BlogDto;
import com.hamitmizrak.business.services.interfaces.IBlogServices;
import com.hamitmizrak.data.entity.BlogEntity;
import com.hamitmizrak.data.mapper.BlogMapper;
import com.hamitmizrak.data.repository.IBlogRepository;
import com.hamitmizrak.exception._404_NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

// LOMBOK
@RequiredArgsConstructor //CDI
@Log4j2

// Service: Asıl İş yükünü yapan yer demektir.
@Service
public class BlogServicesImpl implements IBlogServices<BlogDto, BlogEntity> {

    // Field

    // 1.YOL (Field Injection CDI)
    /*@Autowired
    private IBlogRepository iBlogRepository;*/

    // 2.YOL (Constructor Injection CDI)
    /*
    private final IBlogRepository iBlogRepository;
    @Autowired
    public BlogServicesImpl(IBlogRepository iBlogRepository) {
        this.iBlogRepository = iBlogRepository;
    }
     */

    // 3.YOL (Field Injection CDI)
    private final IBlogRepository iBlogRepository;
    private final ModelMapperBean modelMapperBean;

    // ###############################################################
    // MODEL MAPPER SERVICE
    @Override
    public BlogDto entityToDto(BlogEntity entity) {
        //1.YOL
        //return modelMapperBean.modelMapperMethod().map(entity, BlogDto.class);

        // 2.YOL
        return BlogMapper.toDto(entity);
    }

    @Override
    public BlogEntity dtoToEntity(BlogDto dto) {
        return BlogMapper.toEntity(dto);
    }


    // ###############################################################
    // SPEED DATA
    @Override
    public String blogSpeedData(Integer data) {
        return "";
    }

    // DELETE ALL
    @Override
    public String blogDeleteAll() {
        return "";
    }


    // ###############################################################
    // CREATE (BLOG )
    @Override
    public BlogDto objectServiceCreate(BlogDto dto) {

        // Entity
        BlogEntity blogSaved= iBlogRepository.save(dtoToEntity(dto));
        return entityToDto(blogSaved) ;
    }

    // LIST (BLOG )
    @Override
    public List<BlogDto> objectServiceList() {
        return iBlogRepository.findAll().stream().map(this::entityToDto).toList();
    }

    // FIND BY ID (BLOG )
    @Override
    public BlogDto objectServiceFindById(Long id) {
        BlogEntity blogEntity = iBlogRepository
                .findById(id)
                .orElseThrow(()-> new _404_NotFoundException(id+" nolu id bulunamadı"));
        return entityToDto(blogEntity);
    }

    // UPDATE (BLOG )
    @Override
    public BlogDto objectServiceUpdate(Long id, BlogDto dto) {
        // Bulamadı
        BlogEntity blogEntity = iBlogRepository
                .findById(id)
                .orElseThrow(()-> new _404_NotFoundException(id+" nolu id bulunamadı"));

        return entityToDto(iBlogRepository.save(blogEntity));
    }

    // DELETE (BLOG )
    @Override
    public BlogDto objectServiceDelete(Long id) {
        BlogDto found = objectServiceFindById(id);
        iBlogRepository.deleteById(id);
        return found;
    }

} // end BlogServicesImpl
