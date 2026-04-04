package com.hamitmizrak.controller.api.impl;

import com.hamitmizrak.business.dto.BlogDto;
import com.hamitmizrak.business.services.interfaces.IBlogServices;
import com.hamitmizrak.controller.api.interfaces.IBlogApi;
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
@RequestMapping("/blog/api/v1.0.0")
@CrossOrigin(origins = {FrontEnd.REACT_URL,FrontEnd.ANGULAR_URL})
public class BlogApiImpl implements IBlogApi<BlogDto> {

    // Field
    private final IBlogServices<BlogDto, ?> iBlogServices;



    // #######################################################################
    // SPEED & DELETE_ALL
    // SPEED DATA
    @Override
    public ResponseEntity<String> blogSpeedData(Integer data) {
        return null;
    }

    // DELETE ALL
    @Override
    public ResponseEntity<String> blogDeleteAll() {
        return null;
    }

    // #######################################################################
    // CREATE
    // http://localhost:9999/blog/api/v1.0.0/create
    @Override
    @PostMapping("/create")
    public ResponseEntity<ApiResult<?>> objectApiCreate(@Valid @RequestBody BlogDto dto) {
        try {
            return ResponseEntity.ok(ApiResult.success(iBlogServices.objectServiceCreate(dto)));
         }catch (Exception e){
            return ResponseEntity.ok(ApiResult.error("blogError", e.getMessage(),"/blog/api/v1.0.0/create"));
        }
    }

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
