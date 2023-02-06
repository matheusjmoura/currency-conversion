package com.matheusjmoura.currencyconversion.domain;

import com.matheusjmoura.currencyconversion.application.v1.request.CreateUserRequest;
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
@Table(name = "\"user\"")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "User Entity")
public class User {

    @Id
    @Schema(description = "ID of the user", example = "a96c7f4d-760d-416a-9828-5c57ed0fb888")
    private UUID id;
    @Schema(description = "Name of the user", example = "Matheus Moura")
    private String name;
    @Version
    Long version;

    public static User from(@NonNull CreateUserRequest request) {
        return new User(UUID.randomUUID(), request.getName(), null);
    }

}
