package com.hamitmizrak.controller.api.interfaces;

import com.hamitmizrak.controller.api.ICrudApi;
import org.springframework.http.ResponseEntity;

// D: Dto
public interface IBlogCategoryApi <D> extends ICrudApi<D> {

    // SPEED DATA Category
    public ResponseEntity<String>  blogCategorySpeedData(Integer data);

    // DELETE ALL
    public ResponseEntity<String> blogCategoryDeleteAll();
}
