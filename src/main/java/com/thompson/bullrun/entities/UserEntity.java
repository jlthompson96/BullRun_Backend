package com.thompson.bullrun.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.lang.reflect.Array;
import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "Users")
public class UserEntity {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private ArrayList<String> stocksOwned;
}