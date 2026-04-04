package com.hamitmizrak.business.services.impl;

import com.hamitmizrak.bean.ModelMapperBean;
import com.hamitmizrak.business.dto.BlogCategoryDto;
import com.hamitmizrak.business.services.interfaces.IBlogCategoryServices;
import com.hamitmizrak.data.entity.BlogCategoryEntity;
import com.hamitmizrak.data.mapper.BlogCategoryMapper;
import com.hamitmizrak.data.repository.IBlogCategoryRepository;
import com.hamitmizrak.exception.HamitMizrakException;
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
public class BlogCategoryServicesImpl implements IBlogCategoryServices<BlogCategoryDto, BlogCategoryEntity> {

    // Field

    // 1.YOL (Field Injection CDI)
    /*@Autowired
    private IBlogCategoryRepository iBlogCategoryRepository;*/

    // 2.YOL (Constructor Injection CDI)
    /*
    private final IBlogCategoryRepository iBlogCategoryRepository;
    @Autowired
    public BlogCategoryServicesImpl(IBlogCategoryRepository iBlogCategoryRepository) {
        this.iBlogCategoryRepository = iBlogCategoryRepository;
    }
     */

    // 3.YOL (Field Injection CDI)
    private final IBlogCategoryRepository iBlogCategoryRepository;
    private final ModelMapperBean modelMapperBean;

    // ------------- MODEL MAPPER SERVICE -------------
    @Override
    public BlogCategoryDto entityToDto(BlogCategoryEntity entity) {
        //1.YOL
        //return modelMapperBean.modelMapperMethod().map(entity, BlogCategoryDto.class);

        // 2.YOL
        return BlogCategoryMapper.toDto(entity);
    }

    @Override
    public BlogCategoryEntity dtoToEntity(BlogCategoryDto dto) {
        return BlogCategoryMapper.toEntity(dto);
    }


    // ###############################################################
    // SPEED DATA
    @Override
    public String blogCategorySpeedData(Integer data) {
        if(data!=null){
            for (int i = 1; i <=data ; i++) {
                BlogCategoryEntity blogCategoryEntity= new BlogCategoryEntity();
                blogCategoryEntity.setCategoryName("category"+i);
                iBlogCategoryRepository.save(blogCategoryEntity);
            }
        }else{
            throw new NullPointerException("Integer have not be null");
        }
        return data+" tane veri yüklendi";
    }

    // DELETE ALL
    @Override
    public String blogCategoryDeleteAll() {
        return "";
    }


    // ###############################################################
    // CREATE (BLOG CATEGORY)
    @Override
    public BlogCategoryDto objectServiceCreate(BlogCategoryDto dto) {

        // Hiç bir şey gelmiyorsa
        if(dto==null || dto.getCategoryName()==null || dto.getCategoryName().isBlank() || dto.getCategoryName().isEmpty()) {
            throw  new HamitMizrakException("Kategori adı zorunludur");
        }

        // Varsa
        if(iBlogCategoryRepository.existsByCategoryNameIgnoreCase(dto.getCategoryName())) {
            throw  new HamitMizrakException("Kategori zaten var: "+dto.getCategoryName());
        }

        // Entity
        BlogCategoryEntity blogCategorySaved= iBlogCategoryRepository.save(dtoToEntity(dto));
        return entityToDto(blogCategorySaved) ;
    }

    // LIST (BLOG CATEGORY)
    @Override
    public List<BlogCategoryDto> objectServiceList() {
        return iBlogCategoryRepository.findAll().stream().map(this::entityToDto).toList();
    }

    // FIND BY ID (BLOG CATEGORY)
    @Override
    public BlogCategoryDto objectServiceFindById(Long id) {
        BlogCategoryEntity blogCategoryEntity = iBlogCategoryRepository
                .findById(id)
                .orElseThrow(()-> new _404_NotFoundException(id+" nolu id bulunamadı"));
        return entityToDto(blogCategoryEntity);
    }

    // UPDATE (BLOG CATEGORY)
    @Override
    public BlogCategoryDto objectServiceUpdate(Long id, BlogCategoryDto dto) {
        // Bulamadı
        BlogCategoryEntity blogCategoryEntity = iBlogCategoryRepository
                .findById(id)
                .orElseThrow(()-> new _404_NotFoundException(id+" nolu id bulunamadı"));

        // Kategori zaten varsa
        if(dto.getCategoryName()!=null && !dto.getCategoryName().isBlank()) {
            if(iBlogCategoryRepository.existsByCategoryNameIgnoreCase(dto.getCategoryName())) {
                throw  new HamitMizrakException("Kategori zaten var: "+dto.getCategoryName());
            }

            blogCategoryEntity.setCategoryName(dto.getCategoryName());
        }

        return entityToDto(iBlogCategoryRepository.save(blogCategoryEntity));
    }

    // DELETE (BLOG CATEGORY)
    @Override
    public BlogCategoryDto objectServiceDelete(Long id) {
        BlogCategoryDto found = objectServiceFindById(id);
        iBlogCategoryRepository.deleteById(id);
        return found;
    }

} // end BlogCategoryServicesImpl
