package com.matheusjmoura.currencyconversion.application.v1;

import com.matheusjmoura.currencyconversion.application.v1.request.CreateUserRequest;
import com.matheusjmoura.currencyconversion.application.v1.response.UserResponse;
import com.matheusjmoura.currencyconversion.exception.common.ErrorAttributes;
import com.matheusjmoura.currencyconversion.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@Slf4j
@RestController
@AllArgsConstructor
@Tag(name = "User Operations")
@RequestMapping(path = "/api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    @Operation(summary = "Create new user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User created"),
        @ApiResponse(responseCode = "400", description = "Bad Request", content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = ErrorAttributes.class))}),
        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = ErrorAttributes.class))})
    })
    public Mono<ResponseEntity<UserResponse>> create(@RequestBody @Valid CreateUserRequest request) {
        log.info("Receiving request to create new user. Request body: {}", request);

        return userService.create(request)
            .map(userResponse -> new ResponseEntity<>(userResponse, HttpStatus.CREATED));
    }

}
