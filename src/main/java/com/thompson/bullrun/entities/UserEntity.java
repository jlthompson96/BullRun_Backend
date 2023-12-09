package com.thompson.bullrun.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "user")
public class UserEntity {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private int age;
}