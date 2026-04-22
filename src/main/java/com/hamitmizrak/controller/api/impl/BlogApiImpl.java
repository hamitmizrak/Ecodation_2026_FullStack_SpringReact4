package com.hamitmizrak.controller.api.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hamitmizrak.business.dto.BlogDto;
import com.hamitmizrak.business.services.impl.BlogServicesImpl;
import com.hamitmizrak.controller.api.interfaces.IBlogApi;
import com.hamitmizrak.error.ApiResult;
import com.hamitmizrak.utily.FrontEnd;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Log4j2
@RestController
@RequestMapping("/blog/api/v1.0.0")
@CrossOrigin(origins = FrontEnd.REACT_URL)
public class BlogApiImpl implements IBlogApi<BlogDto> {

    /**
     * İstek alma katmanı controller'dır.
     * Business logic, dosya kaydetme/silme ve update akışı service katmanındadır.
     */
    private final BlogServicesImpl blogService;
    private final ObjectMapper objectMapper;

    // -------------------- CREATE --------------------

    /** JSON (resimsiz) */
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<ApiResult<?>> objectApiCreate(@Valid @RequestBody BlogDto dto) {
        try {
            return ResponseEntity.ok(ApiResult.success(blogService.objectServiceCreate(dto)));
        } catch (Exception ex) {
            return ResponseEntity.ok(ApiResult.error("serverError", ex.getMessage(), "/blog/api/v1.0.0/create"));
        }
    }

    /**
     * Multipart (resimli)
     * Controller sadece veriyi alır/parçalar, iş mantığını service'e devreder.
     */
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResult<?>> objectApiCreateMultipart(
            @RequestPart("blog") String json,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) throws JsonProcessingException {
        try {
            BlogDto dto = objectMapper.readValue(json, BlogDto.class);
            return ResponseEntity.ok(ApiResult.success(blogService.objectServiceCreateWithFile(dto, file)));
        } catch (Exception ex) {
            return ResponseEntity.ok(ApiResult.error("serverError", ex.getMessage(), "/blog/api/v1.0.0/create[multipart]"));
        }
    }

    // -------------------- LIST / FIND --------------------

    @GetMapping("/list")
    @Override
    public ResponseEntity<ApiResult<List<BlogDto>>> objectApiList() {
        try {
            List<BlogDto> data = blogService.objectServiceList();
            return ResponseEntity.ok(ApiResult.success(data));
        } catch (Exception ex) {
            return ResponseEntity.ok(ApiResult.error("serverError", ex.getMessage(), "/blog/api/v1.0.0/list"));
        }
    }

    @GetMapping("/find/{id}")
    @Override
    public ResponseEntity<ApiResult<?>> objectApiFindById(@PathVariable Long id) {
        try {
            BlogDto data = blogService.objectServiceFindById(id);
            return ResponseEntity.ok(ApiResult.success(data));
        } catch (Exception ex) {
            return ResponseEntity.ok(ApiResult.error("serverError", ex.getMessage(), "/blog/api/v1.0.0/find/" + id));
        }
    }

    // -------------------- UPDATE --------------------

    /** JSON (resimsiz) */
    @PutMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<ApiResult<?>> objectApiUpdate(@PathVariable Long id,
                                                        @Valid @RequestBody BlogDto dto) {
        try {
            return ResponseEntity.ok(ApiResult.success(blogService.objectServiceUpdate(id, dto)));
        } catch (Exception ex) {
            return ResponseEntity.ok(ApiResult.error("serverError", ex.getMessage(), "/blog/api/v1.0.0/update/" + id));
        }
    }

    /**
     * Multipart (resimli)
     * Controller dosyayı ve json'u alır, update business akışı service katmanında yürür.
     */
    @PutMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResult<?>> objectApiUpdateMultipart(
            @PathVariable Long id,
            @RequestPart("blog") String json,
            @RequestPart(name = "file", required = false) MultipartFile file
    ) throws JsonProcessingException {
        try {
            BlogDto dto = objectMapper.readValue(json, BlogDto.class);
            BlogDto updated = blogService.objectServiceUpdateWithFile(id, dto, file);
            return ResponseEntity.ok(ApiResult.success(updated));
        } catch (Exception ex) {
            return ResponseEntity.ok(ApiResult.error("serverError", ex.getMessage(), "/blog/api/v1.0.0/update[multipart]/" + id));
        }
    }

    // -------------------- DELETE --------------------

    @DeleteMapping("/delete/{id}")
    @Override
    public ResponseEntity<ApiResult<?>> objectApiDelete(@PathVariable Long id) {
        try {
            BlogDto deleted = blogService.objectServiceDelete(id);
            return ResponseEntity.ok(ApiResult.success(deleted));
        } catch (Exception ex) {
            return ResponseEntity.ok(ApiResult.error("serverError", ex.getMessage(), "/blog/api/v1.0.0/delete/" + id));
        }
    }

    // -------------------- EXTRAS --------------------

    @PostMapping("/speed-data/{count}")
    @Override
    public ResponseEntity<String> blogApiSpeedData(@PathVariable("count") Long count) {
        return ResponseEntity.ok(blogService.blogSpeedData(count == null ? 0 : count));
    }

    @DeleteMapping("/all-delete")
    @Override
    public ResponseEntity<String> blogApiAllDelete() {
        return ResponseEntity.ok(blogService.blogAllDelete());
    }
}
