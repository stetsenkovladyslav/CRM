package com.cursor.onlineshop.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.core.io.Resource;

@AllArgsConstructor
@Getter
public class GetFileDto {
    private Resource file;
    private String fileName;
}
