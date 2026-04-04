package com.hamitmizrak.controller.api;

import com.hamitmizrak.error.ApiResult;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

// D: Dto
public interface ICrudImageApi<D> {

    // CREATE
    public ResponseEntity<ApiResult<?>> objectApiCreate(D d);

    // CREATE (JSON Resimli: Multipart)
    // http://localhost:9999/blog/api/v1.0.0/create
    public ResponseEntity<ApiResult<?>> objectApiCreate( String json,MultipartFile multipartFile);

    // LIST
    public ResponseEntity<ApiResult<List<D>>> objectApiList();

    // FIND BY ID
    public ResponseEntity<ApiResult<?>> objectApiFindById(Long id);

    // UPDATE
    public ResponseEntity<ApiResult<?>> objectApiUpdate(Long id, D d);

    // UPDATE (JSON Resimli: Multipart)
    // http://localhost:9999/blog/api/v1.0.0/create
    public ResponseEntity<ApiResult<?>> objectApiUpdate( String json,MultipartFile multipartFile);

    // DELETE
    public ResponseEntity<ApiResult<?>> objectApiDelete(Long id);

}
