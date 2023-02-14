package com.matheusjmoura.currencyconversion.domain;

import com.matheusjmoura.currencyconversion.application.v1.request.UserRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Getter
@AllArgsConstructor
@Table(name = "\"user\"")
@Schema(description = "User Entity")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class User {

    @Id
    private UUID id;
    private String name;
    @Version
    Long version;

    public static User from(@NonNull UserRequest request) {
        return new User(
            UUID.randomUUID(),
            request.getName(),
            null);
    }

}
