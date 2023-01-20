package com.mracover.telegram_bot.controller;

import com.mracover.telegram_bot.exception.userException.NoSuchUserException;
import com.mracover.telegram_bot.model.User;
import com.mracover.telegram_bot.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Контроллер пользователей", description = "Методы для работы с пользователем (отсутсвует добавление и обновление проблем)")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Возращает всех пользователей")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Пользователи найдены",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = User.class))
                            )
                    }

            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Нет ни одного пользователя в базе данных",
                    content =
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = NoSuchUserException.class)
                    )
            )
    })
    @GetMapping("/")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @Operation(summary = "Возращает пользователей по id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Пользователи найдена",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = User.class)
                            )
                    }

            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователи не найдена",
                    content =
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = NoSuchUserException.class)
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") long id) {
        User user = userService.findUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Operation(summary = "Возращает пользователей по TelegramUserID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Пользователи найдена",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = User.class)
                            )
                    }

            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователи не найдена",
                    content =
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = NoSuchUserException.class)
                    )
            )
    })
    @GetMapping("/telegramId/{id}")
    public ResponseEntity<User> getUserByTelegramId(@PathVariable("id") long id) {
        User user = userService.findUserByTelegramId(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Operation(summary = "Удаляет пользователя по id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Пользователь удален"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователя с таким id не сущетсвует",
                    content =
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = NoSuchUserException.class)
                    )
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable long id) {
        userService.deleteUserById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Удаляет пользователя по TelegramUserID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Пользователь удален"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователя с таким TelegramUserID не сущетсвует",
                    content =
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = NoSuchUserException.class)
                    )
            )
    })
    @DeleteMapping("/telegramId/{id}")
    public ResponseEntity<Void> deleteUserByTelegramId(@PathVariable long id) {
        userService.deleteUserByTelegramId(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
