package com.dzhenetl.diplom.entity;

import com.dzhenetl.diplom.security.domain.User;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "files")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String path;
    private String filename;
    private Long size;
    @ManyToOne
    private User user;
}
