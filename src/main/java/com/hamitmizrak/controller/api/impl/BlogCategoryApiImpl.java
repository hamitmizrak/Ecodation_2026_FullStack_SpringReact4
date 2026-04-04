package com.hamitmizrak.controller.api.impl;

import com.hamitmizrak.business.dto.BlogCategoryDto;
import com.hamitmizrak.business.services.interfaces.IBlogCategoryServices;
import com.hamitmizrak.controller.api.interfaces.IBlogCategoryApi;
import com.hamitmizrak.error.ApiResult;
import com.hamitmizrak.utily.FrontEnd;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// LOMBOK
@RequiredArgsConstructor
@Log4j2

// API
@RestController
@RequestMapping("/blog/category/api/v1.0.0")
@CrossOrigin(origins = {FrontEnd.REACT_URL,FrontEnd.ANGULAR_URL})
public class BlogCategoryApiImpl implements IBlogCategoryApi<BlogCategoryDto> {

    // Field
    private final IBlogCategoryServices<BlogCategoryDto, ?> iBlogCategoryServices;


    // #######################################################################
    // SPEED & DELETE_ALL
    // SPEED DATA
    // http://localhost:9999/blog/category/api/v1.0.0/speed-data/4
    @Override
    @PostMapping("/speed-data/{count}")
    public ResponseEntity<String> blogCategorySpeedData(@PathVariable(name="count")  Integer data) {
        return ResponseEntity.ok(iBlogCategoryServices.blogCategorySpeedData(data==null ? 0 : data));
    }

    // DELETE ALL
    // http://localhost:9999/blog/category/api/v1.0.0/all-delete
    @Override
    @DeleteMapping("/all-delete")
    public ResponseEntity<String> blogCategoryDeleteAll() {
        return ResponseEntity.ok(iBlogCategoryServices.blogCategoryDeleteAll());
    }

    // #######################################################################
    // CREATE
    // http://localhost:9999/blog/category/api/v1.0.0/create
    @Override
    @PostMapping("/create")
    public ResponseEntity<ApiResult<?>> objectApiCreate(@Valid @RequestBody BlogCategoryDto dto) {
        try {
            return ResponseEntity.ok(ApiResult.success(iBlogCategoryServices.objectServiceCreate(dto)));
         }catch (Exception e){
            return ResponseEntity.ok(ApiResult.error("blogCategoryError", e.getMessage(),"/blog/category/api/v1.0.0/create"));
        }
    }

    // LIST (BLOG CATEGORY)
    // http://localhost:9999/blog/category/api/v1.0.0/list
    @Override
    @GetMapping("/list")
    public ResponseEntity<ApiResult<List<BlogCategoryDto>>> objectApiList() {
        try {
            return ResponseEntity.ok(ApiResult.success(iBlogCategoryServices.objectServiceList()));
        }catch (Exception e){
            return ResponseEntity.ok(ApiResult.error("blogCategoryError", e.getMessage(),"/blog/category/api/v1.0.0/list"));
        }
    }

    // FIND (BLOG CATEGORY)
    // http://localhost:9999/blog/category/api/v1.0.0/find/1
    @Override
    @GetMapping("/find/{id}")
    public ResponseEntity<ApiResult<?>> objectApiFindById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(ApiResult.success(iBlogCategoryServices.objectServiceFindById(id)));
        }catch (Exception e){
            return ResponseEntity.ok(ApiResult.error("blogCategoryError", e.getMessage(),"/blog/category/api/v1.0.0/find/"+id));
        }
    }

    // UPDATE (BLOG CATEGORY)
    // http://localhost:9999/blog/category/api/v1.0.0/update/1
    @Override
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResult<?>> objectApiUpdate(@PathVariable(name = "id") Long id, @Valid @RequestBody BlogCategoryDto dto) {
        try {
            return ResponseEntity.ok(ApiResult.success(iBlogCategoryServices.objectServiceUpdate(id,dto)));
        }catch (Exception e){
            return ResponseEntity.ok(ApiResult.error("blogCategoryError", e.getMessage(),"/blog/category/api/v1.0.0/update/"+id));
        }
    }

    // DELETE (BLOG CATEGORY)
    // http://localhost:9999/blog/category/api/v1.0.0/delete/1
    @Override
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResult<?>> objectApiDelete(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(ApiResult.success(iBlogCategoryServices.objectServiceDelete(id)));
        }catch (Exception e){
            return ResponseEntity.ok(ApiResult.error("blogCategoryError", e.getMessage(),"/blog/category/api/v1.0.0/delete/"+id));
        }
    }
} //end BlogCategoryApiImpl
