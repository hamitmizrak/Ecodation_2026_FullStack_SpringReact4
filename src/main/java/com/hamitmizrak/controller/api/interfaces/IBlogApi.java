package com.hamitmizrak.controller.api.interfaces;

import com.hamitmizrak.controller.api.ICrudApi;
import com.hamitmizrak.controller.api.ICrudImageApi;
import com.hamitmizrak.error.ApiResult;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

// D: Dto
public interface IBlogApi<D> extends ICrudImageApi<D> {

    // SPEED DATA
    public ResponseEntity<String>  blogSpeedData(Integer data);


    // DELETE ALL
    public ResponseEntity<String> blogDeleteAll();


}
