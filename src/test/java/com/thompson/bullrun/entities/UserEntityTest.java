package com.thompson.bullrun.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserEntityTest {

    private UserEntity userEntity;

    @BeforeEach
    public void setUp() {
        userEntity = new UserEntity();
    }

    @Test
    public void shouldSetAndGetId() {
        String id = "123";
        userEntity.setId(id);
        assertEquals(id, userEntity.getId());
    }

    @Test
    public void shouldSetAndGetFirstName() {
        String firstName = "John";
        userEntity.setFirstName(firstName);
        assertEquals(firstName, userEntity.getFirstName());
    }

    @Test
    public void shouldSetAndGetLastName() {
        String lastName = "Doe";
        userEntity.setLastName(lastName);
        assertEquals(lastName, userEntity.getLastName());
    }

    @Test
    public void shouldSetAndGetEmailAddress() {
        String emailAddress = "john.doe@example.com";
        userEntity.setEmailAddress(emailAddress);
        assertEquals(emailAddress, userEntity.getEmailAddress());
    }

    @Test
    public void shouldSetAndGetAge() {
        int age = 30;
        userEntity.setAge(age);
        assertEquals(age, userEntity.getAge());
    }

    @Test
    public void shouldCreateUserEntityWithNoArgsConstructor() {
        UserEntity userEntity = new UserEntity();
        assertNotNull(userEntity);
    }

    @Test
    public void shouldCreateUserEntityWithAllArgsConstructor() {
        UserEntity userEntity = new UserEntity("123", "John", "Doe", "john.doe@example.com", 30);
        assertNotNull(userEntity);
        assertEquals("123", userEntity.getId());
        assertEquals("John", userEntity.getFirstName());
        assertEquals("Doe", userEntity.getLastName());
        assertEquals("john.doe@example.com", userEntity.getEmailAddress());
        assertEquals(30, userEntity.getAge());
    }

    @Test
    public void shouldHaveSameHashCodeForSameData() {
        UserEntity userEntity1 = new UserEntity("123", "John", "Doe", "john.doe@example.com", 30);
        UserEntity userEntity2 = new UserEntity("123", "John", "Doe", "john.doe@example.com", 30);
        assertEquals(userEntity1.hashCode(), userEntity2.hashCode());
    }

    @Test
    public void shouldEqualForSameData() {
        UserEntity userEntity1 = new UserEntity("123", "John", "Doe", "john.doe@example.com", 30);
        UserEntity userEntity2 = new UserEntity("123", "John", "Doe", "john.doe@example.com", 30);
        assertEquals(userEntity1, userEntity2);
    }

    @Test
    public void shouldNotEqualForDifferentData() {
        UserEntity userEntity1 = new UserEntity("123", "John", "Doe", "john.doe@example.com", 30);
        UserEntity userEntity2 = new UserEntity("456", "Jane", "Doe", "jane.doe@example.com", 25);
        assertNotEquals(userEntity1, userEntity2);
    }

    @Test
    public void shouldReturnCorrectToString() {
        UserEntity userEntity = new UserEntity("123", "John", "Doe", "john.doe@example.com", 30);
        String expectedString = "UserEntity(id=123, firstName=John, lastName=Doe, emailAddress=john.doe@example.com, age=30)";
        assertEquals(expectedString, userEntity.toString());
    }
}
