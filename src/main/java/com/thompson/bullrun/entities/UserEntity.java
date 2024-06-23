package com.thompson.bullrun.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

/**
 * This class represents a User entity in the MongoDB database.
 * It is annotated with @Document to indicate that it is a MongoDB document stored in the "Users" collection.
 * Lombok annotations are used to automatically generate getters, setters, and other utility methods.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "Users")
public class UserEntity {
    /**
     * The unique identifier for the user. It is annotated with @Id to indicate that it is the primary key.
     */
    @Id
    private String id;

    /**
     * The first name of the user.
     */
    private String firstName;

    /**
     * The last name of the user.
     */
    private String lastName;

    /**
     * A list of stocks owned by the user. Each stock is represented by a string.
     */
    private ArrayList<String> stocksOwned;
}