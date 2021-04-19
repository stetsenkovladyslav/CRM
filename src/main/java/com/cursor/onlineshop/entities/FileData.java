package com.cursor.onlineshop.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "files")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileData {

    @Id
    @Column(name = "file_id")
    private String fileId;
    @Column(name = "file_name")
    private String fileName;
    @Column(name = "author_id")
    private String authorId;
}
