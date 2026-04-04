package com.hamitmizrak.controller.api.interfaces;

import com.hamitmizrak.controller.api.ICrudApi;
import com.hamitmizrak.controller.api.ICrudImageApi;
import org.springframework.http.ResponseEntity;

// D: Dto
public interface IBlogApi<D> extends ICrudImageApi<D> {

    // SPEED DATA
    public ResponseEntity<String>  blogSpeedData(Integer data);


    // DELETE ALL
    public ResponseEntity<String> blogDeleteAll();
}
