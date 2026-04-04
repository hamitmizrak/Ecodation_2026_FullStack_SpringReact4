package com.hamitmizrak.business.services.impl;

import com.hamitmizrak.bean.ModelMapperBean;
import com.hamitmizrak.business.dto.BlogDto;
import com.hamitmizrak.business.services.interfaces.IBlogServices;
import com.hamitmizrak.data.entity.BlogCategoryEntity;
import com.hamitmizrak.data.entity.BlogEntity;
import com.hamitmizrak.data.mapper.BlogMapper;
import com.hamitmizrak.data.repository.IBlogCategoryRepository;
import com.hamitmizrak.data.repository.IBlogRepository;
import com.hamitmizrak.exception.HamitMizrakException;
import com.hamitmizrak.exception._404_NotFoundException;
import com.hamitmizrak.file_upload.ImageService;
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
    private final IBlogCategoryRepository  iBlogCategoryRepository;
    private final ModelMapperBean modelMapperBean;

    // Blog Resimleri silmek istersem serverda da silinmesi gerekiyor
    private final ImageService imageService;

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
        BlogCategoryEntity blogCategoryEntity=iBlogCategoryRepository.findByCategoryNameIgnoreCase("category")
                .orElseGet(()->iBlogCategoryRepository.save(BlogCategoryEntity.builder().categoryName("Genel").build()));

        for (int i = 1; i <=data ; i++) {
            BlogEntity blogEntity= BlogEntity.builder()
                    .header("header"+i)
                    .title("title"+i)
                    .content("content"+i)
                    .image("resim.png")
                    .blogCategoryToBlogEntity(blogCategoryEntity)
                    .build();
            iBlogRepository.save(blogEntity);
        }
        System.out.println("Blog speed data  "+data+" tane eklendi");
        log.info("Blog speed data  "+data+" tane eklendi");
        return "Blog speed data  "+data+" tane eklendi";
    }

    // DELETE ALL
    @Override
    public String blogDeleteAll() {
        iBlogRepository.deleteAll();
        return "Tüm bloglar silindi.";
    }

    // ###############################################################
    // VALIDATE
    private void validate(BlogDto dto) {

        // Eğer veri yoksa
        if(dto ==null) {
            throw new HamitMizrakException("Blog verisi boş");
        }else{
            if(dto.getHeader()==null || dto.getHeader().isBlank())  throw new HamitMizrakException("ÜSt başlık zorunlu");
            if(dto.getTitle()==null || dto.getTitle().isBlank())  throw new HamitMizrakException("Alt başlık zorunlu");
            if(dto.getContent()==null || dto.getContent().isBlank())  throw new HamitMizrakException("İçerik zorunlu");
        }
    }


    // ###############################################################
    // CREATE (BLOG )
    @Override
    public BlogDto objectServiceCreate(BlogDto dto) {

        // Validation
        validate(dto);

        // Kategori Seçiniz
        Long catId=dto.getBlogCategoryDto()!=null ? dto.getBlogCategoryDto().getBlogCategoryId():null;
        if(catId==null) throw new HamitMizrakException("Kategori Seçiniz");

        // Kategori Bul
        BlogCategoryEntity blogCategory= iBlogCategoryRepository
                .findById(catId)
                .orElseThrow(()->new _404_NotFoundException(catId+" id'li kategori bulunamadı"));

        // Mapper
        BlogEntity blogEntity= dtoToEntity(dto);
        blogEntity.setBlogCategoryToBlogEntity(blogCategory);

        // Entity
        BlogEntity blogSaved= iBlogRepository.save(blogEntity);
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

        // Alan bazlı güncelleme(Validation)
        validate(dto);

        // Kategori değişimi
        if(dto.getBlogCategoryDto()!=null && dto.getBlogCategoryDto().getBlogCategoryId()!=null) {
            Long catId=dto.getBlogCategoryDto().getBlogCategoryId();
            BlogCategoryEntity blogCategoryEntity = iBlogCategoryRepository.findById(catId)
                    .orElseThrow(()->new _404_NotFoundException(catId+ " id'li kategori bulunamadı"));
            blogEntity.setBlogCategoryToBlogEntity(blogCategoryEntity);
        }

        return entityToDto(iBlogRepository.save(blogEntity));
    }

    // DELETE (BLOG )
    @Override
    public BlogDto objectServiceDelete(Long id) {

        // blogcategory id yoksa blog silinsin ama kayıtlı ve ilişkili veri varsa bunun doğrulanmasını sağlamak
        BlogDto found = objectServiceFindById(id);
        iBlogRepository.deleteById(id);
        return found;
    }

} // end BlogServicesImpl
