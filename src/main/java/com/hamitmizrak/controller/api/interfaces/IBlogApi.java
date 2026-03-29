package com.hamitmizrak.controller.api.interfaces;

import com.hamitmizrak.controller.api.ICrudApi;
import com.hamitmizrak.controller.api.ICrudImageApi;
import org.springframework.http.ResponseEntity;

// D: Dto
public interface IBlogApi<D> extends ICrudImageApi<D> {

    // SPEED DATA Category
    public ResponseEntity<String>  blogSpeedData(Integer data);
}
