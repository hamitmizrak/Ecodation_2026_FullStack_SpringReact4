package com.hamitmizrak.business.services.impl;

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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Log4j2
@Service
public class BlogServicesImpl implements IBlogServices<BlogDto, BlogEntity> {

    private final IBlogRepository blogRepo;
    private final IBlogCategoryRepository categoryRepo;

    // Dosya işlemi business akış içinde service katmanından yönetilir
    private final ImageService imageService;

    // ---------- IModelMapperService ----------
    @Override
    public BlogDto entityToDto(BlogEntity e) {
        return BlogMapper.toDto(e);
    }

    @Override
    public BlogEntity dtoToEntity(BlogDto d) {
        return BlogMapper.toEntity(d);
    }

    // ---------- ICrudService ----------

    @Override
    @Transactional
    public BlogDto objectServiceCreate(BlogDto blogDto) {
        validate(blogDto, true);

        Long catId = blogDto.getBlogCategoryDto() != null ? blogDto.getBlogCategoryDto().getCategoryId() : null;
        if (catId == null) {
            throw new HamitMizrakException("Kategori seçiniz");
        }

        BlogCategoryEntity cat = categoryRepo.findById(catId)
                .orElseThrow(() -> new _404_NotFoundException(catId + " id'li kategori bulunamadı"));

        BlogEntity e = dtoToEntity(blogDto);
        e.setBlogCategoryBlogEntity(cat);

        BlogEntity saved = blogRepo.save(e);
        return entityToDto(saved);
    }

    /**
     * Resimli create business akışı service katmanında.
     */
    @Transactional
    public BlogDto objectServiceCreateWithFile(BlogDto blogDto, MultipartFile file) {
        if (file != null && !file.isEmpty()) {
            String relative = imageService.saveBlogImage(file);
            blogDto.setImage(relative);
        }
        return objectServiceCreate(blogDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BlogDto> objectServiceList() {
        return blogRepo.findAll().stream().map(this::entityToDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public BlogDto objectServiceFindById(Long id) {
        BlogEntity e = blogRepo.findById(id)
                .orElseThrow(() -> new _404_NotFoundException(id + " id'li blog bulunamadı"));
        return entityToDto(e);
    }

    @Override
    @Transactional
    public BlogDto objectServiceUpdate(Long id, BlogDto blogDto) {
        BlogEntity e = blogRepo.findById(id)
                .orElseThrow(() -> new _404_NotFoundException(id + " id'li blog bulunamadı"));

        if (blogDto.getHeader() != null && !blogDto.getHeader().isBlank()) {
            e.setHeader(blogDto.getHeader());
        }
        if (blogDto.getTitle() != null && !blogDto.getTitle().isBlank()) {
            e.setTitle(blogDto.getTitle());
        }
        if (blogDto.getContent() != null && !blogDto.getContent().isBlank()) {
            e.setContent(blogDto.getContent());
        }
        if (blogDto.getImage() != null && !blogDto.getImage().isBlank()) {
            e.setImage(blogDto.getImage());
        }

        if (blogDto.getBlogCategoryDto() != null && blogDto.getBlogCategoryDto().getCategoryId() != null) {
            Long catId = blogDto.getBlogCategoryDto().getCategoryId();
            BlogCategoryEntity cat = categoryRepo.findById(catId)
                    .orElseThrow(() -> new _404_NotFoundException(catId + " id'li kategori bulunamadı"));
            e.setBlogCategoryBlogEntity(cat);
        }

        return entityToDto(blogRepo.save(e));
    }

    /**
     * Resimli update business akışı service katmanında.
     * Yeni resim gelirse kaydet, update et, sonra eski resmi güvenle sil.
     */
    @Transactional
    public BlogDto objectServiceUpdateWithFile(Long id, BlogDto d, MultipartFile file) {
        BlogEntity current = blogRepo.findById(id)
                .orElseThrow(() -> new _404_NotFoundException(id + " id'li blog bulunamadı"));

        String oldUrl = current.getImage();

        if (file != null && !file.isEmpty()) {
            String relative = imageService.saveBlogImage(file);
            d.setImage(relative);
        }

        BlogDto updated = objectServiceUpdate(id, d);

        if (file != null && !file.isEmpty()
                && oldUrl != null
                && oldUrl.startsWith("/upload/")
                && !oldUrl.equals(updated.getImage())) {
            try {
                imageService.deleteByUrl(oldUrl);
            } catch (Exception ignored) {
                // loglanabilir
            }
        }

        return updated;
    }

    @Override
    @Transactional
    public BlogDto objectServiceDelete(Long id) {
        BlogEntity e = blogRepo.findById(id)
                .orElseThrow(() -> new _404_NotFoundException(id + " id'li blog bulunamadı"));

        String img = e.getImage();
        if (img != null && img.startsWith("/upload/")) {
            try {
                imageService.deleteByUrl(img);
            } catch (Exception ignored) {
                // loglanabilir
            }
        }

        BlogDto toReturn = entityToDto(e);
        blogRepo.deleteById(id);
        return toReturn;
    }

    // ---------- Extras ----------
    @Transactional
    @Override
    public String blogSpeedData(Long count) {
        BlogCategoryEntity cat = categoryRepo.findByCategoryNameIgnoreCase("Genel")
                .orElseGet(() -> categoryRepo.save(BlogCategoryEntity.builder().categoryName("Genel").build()));

        for (int i = 1; i <= count; i++) {
            BlogEntity e = BlogEntity.builder()
                    .header("Header " + i)
                    .title("Title " + i)
                    .content("Content " + i)
                    .image("resim.png")
                    .blogCategoryBlogEntity(cat)
                    .build();
            blogRepo.save(e);
        }
        return "Blog speed data eklendi: " + count;
    }

    @Override
    @Transactional
    public String blogAllDelete() {
        blogRepo.deleteAll();
        return "Tüm bloglar silindi";
    }

    // ---------- validate ----------
    private void validate(BlogDto d, boolean creating) {
        if (d == null) {
            throw new HamitMizrakException("Blog verisi boş");
        }
        if (creating) {
            if (d.getHeader() == null || d.getHeader().isBlank()) {
                throw new HamitMizrakException("Header zorunlu");
            }
            if (d.getTitle() == null || d.getTitle().isBlank()) {
                throw new HamitMizrakException("Başlık zorunlu");
            }
            if (d.getContent() == null || d.getContent().isBlank()) {
                throw new HamitMizrakException("İçerik zorunlu");
            }
        }
    }
}
