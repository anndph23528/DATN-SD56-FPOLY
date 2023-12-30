package com.example.datnsd56.controller;

import com.example.datnsd56.entity.Image;
import com.example.datnsd56.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/image")
public class ImageController {
    @Autowired
    private ImageService imageService;

    @GetMapping("getAll")
    @PreAuthorize("hasAuthority('admin')")

    public List<Image> getAll(@RequestParam(value = "page", defaultValue = "0") Integer page) {
        List<Image> list = imageService.getAll(page).getContent();
        return list;
    }

    @PostMapping("add")
    @PreAuthorize("hasAuthority('admin')")

    public void add(@RequestBody Image image) {
        imageService.add(image);
    }

    @GetMapping("detail/{id}")
    @PreAuthorize("hasAuthority('admin')")

    public Image detail(@PathVariable("id") Integer id) {
        Image image = imageService.getById(id);
        return image;

    }
//    @GetMapping("/{id}")
//    public ResponseEntity<byte[]> getImage(@PathVariable Integer id) {
//        Image image = (Image) imageService.getImagesForProducts(id);
//        if (image != null && image.getImageData() != null) {
//            return ResponseEntity.ok()
//                .contentType(MediaType.IMAGE_JPEG)
//                .body(image.getImageData());
//        } else {
//            return ResponseEntity.notFound().build();
//        }


    @PutMapping("update/{id}")
    @PreAuthorize("hasAuthority('admin')")

    public void update(@RequestBody Image image) {
        imageService.update(image);
    }

    @DeleteMapping("delete/{id}")
    @PreAuthorize("hasAuthority('admin')")

    public void delete(@PathVariable("id") Integer id) {
        imageService.delete(id);
    }

}
