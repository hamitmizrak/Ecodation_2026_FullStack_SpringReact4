package com.hamitmizrak.controller.api.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hamitmizrak.business.dto.BlogDto;
import com.hamitmizrak.business.services.interfaces.IBlogServices;
import com.hamitmizrak.controller.api.interfaces.IBlogApi;
import com.hamitmizrak.error.ApiResult;
import com.hamitmizrak.file_upload.ImageService;
import com.hamitmizrak.utily.FrontEnd;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

// LOMBOK
@RequiredArgsConstructor
@Log4j2

// API
@RestController
@RequestMapping("/blog/api/v1.0.0")
@CrossOrigin(origins = {FrontEnd.REACT_URL,FrontEnd.ANGULAR_URL})
public class BlogApiImpl implements IBlogApi<BlogDto> {

    // Field
    private final IBlogServices<BlogDto, ?> iBlogServices;

    // Resim Ekleme (Api)
    private final ImageService imageService;
    private final ObjectMapper objectMapper;


    // #######################################################################
    // SPEED & DELETE_ALL
    // SPEED DATA
    // http://localhost:9999/blog/category/api/v1.0.0/speed-data/4
    @Override
    @PostMapping("/speed-data/{count}")
    public ResponseEntity<String> blogSpeedData(@PathVariable(name="count")  Integer data) {
        return ResponseEntity.ok(iBlogServices.blogSpeedData(data==null ? 0 : data));
    }

    // DELETE ALL
    // http://localhost:9999/blog/api/v1.0.0/all-delete
    @Override
    @DeleteMapping("/all-delete")
    public ResponseEntity<String> blogDeleteAll() {
        return ResponseEntity.ok(iBlogServices.blogDeleteAll());
    }

    // #######################################################################
    // CREATE (JSON Resimsiz)
    // http://localhost:9999/blog/api/v1.0.0/create
    @Override
    @PostMapping(value = "/create",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResult<?>> objectApiCreate(@Valid @RequestBody BlogDto dto) {
        try {
            return ResponseEntity.ok(ApiResult.success(iBlogServices.objectServiceCreate(dto)));
         }catch (Exception e){
            return ResponseEntity.ok(ApiResult.error("blogError", e.getMessage(),"/blog/api/v1.0.0/create"));
        }
    }

    // CREATE (JSON Resimli: Multipart)
    // http://localhost:9999/blog/api/v1.0.0/create
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Override
    public ResponseEntity<ApiResult<?>> objectApiCreate(
            @RequestPart("blog") String json,
            @RequestPart(value = "file", required = false) MultipartFile multipartFile) {
        try {
            BlogDto blogDto = objectMapper.readValue(json,BlogDto.class);
            String relative = imageService.saveBlogImage(multipartFile); //upload/blog/...
            blogDto.setImage(relative);
            return ResponseEntity.ok(ApiResult.success(iBlogServices.objectServiceCreate(blogDto)));
        }catch (Exception e){
            return ResponseEntity.ok(ApiResult.error("blogError", e.getMessage(),"/blog/api/v1.0.0/create[multipart/form-data]"));
        }
    }

    // #######################################################################
    // LIST (BLOG )
    // http://localhost:9999/blog/api/v1.0.0/list
    @Override
    @GetMapping("/list")
    public ResponseEntity<ApiResult<List<BlogDto>>> objectApiList() {
        try {
            return ResponseEntity.ok(ApiResult.success(iBlogServices.objectServiceList()));
        }catch (Exception e){
            return ResponseEntity.ok(ApiResult.error("blogError", e.getMessage(),"/blog/api/v1.0.0/list"));
        }
    }


    // #######################################################################
    // FIND (BLOG )
    // http://localhost:9999/blog/api/v1.0.0/find/1
    @Override
    @GetMapping("/find/{id}")
    public ResponseEntity<ApiResult<?>> objectApiFindById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(ApiResult.success(iBlogServices.objectServiceFindById(id)));
        }catch (Exception e){
            return ResponseEntity.ok(ApiResult.error("blogError", e.getMessage(),"/blog/api/v1.0.0/find/"+id));
        }
    }


    // #######################################################################
    // UPDATE (BLOG )
    // http://localhost:9999/blog/api/v1.0.0/update/1
    @Override
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResult<?>> objectApiUpdate(@PathVariable(name = "id") Long id, @Valid @RequestBody BlogDto dto) {
        try {
            return ResponseEntity.ok(ApiResult.success(iBlogServices.objectServiceUpdate(id,dto)));
        }catch (Exception e){
            return ResponseEntity.ok(ApiResult.error("blogError", e.getMessage(),"/blog/api/v1.0.0/update/"+id));
        }
    }

    @Override
    public ResponseEntity<ApiResult<?>> objectApiUpdate(String json, MultipartFile multipartFile) {
        return null;
    }


    // #######################################################################
    // DELETE (BLOG )
    // http://localhost:9999/blog/api/v1.0.0/delete/1
    @Override
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResult<?>> objectApiDelete(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(ApiResult.success(iBlogServices.objectServiceDelete(id)));
        }catch (Exception e){
            return ResponseEntity.ok(ApiResult.error("blogError", e.getMessage(),"/blog/api/v1.0.0/delete/"+id));
        }
    }
} //end BlogApiImpl
