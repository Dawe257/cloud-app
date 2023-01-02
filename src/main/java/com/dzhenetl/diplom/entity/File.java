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
    private String name;
    private byte[] data;
    @ManyToOne
    private User user;
}