package com.mracover.telegram_bot.controller;

import com.mracover.telegram_bot.exception.problemException.NoSuchProblemException;
import com.mracover.telegram_bot.model.Problem;
import com.mracover.telegram_bot.service.ProblemService;
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
@RequestMapping("/api/problems")
@Tag(name = "Контроллер проблем", description = "Методы для работы с проблемами (отсутсвует добавление и обновление проблем)")
public class ProblemController {

    private final ProblemService problemService;

    public ProblemController(ProblemService problemService) {
        this.problemService = problemService;
    }

    @Operation(summary = "Возращает все проблема от пользователей")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Проблемы найдены",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Problem.class))
                            )
                    }

            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Нет ни одной проблемы в базе данных",
                    content =
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = NoSuchProblemException.class)
                    )
            )
    })
    @GetMapping("/")
    public ResponseEntity<List<Problem>> getAllProblems() {
        List<Problem> problems = problemService.getAllProblems();
        return new ResponseEntity<>(problems, HttpStatus.OK);
    }

    @Operation(summary = "Возращает проблемы по id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "проблема найдена",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Problem.class)
                            )
                    }

            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Проблема не найдена",
                    content =
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = NoSuchProblemException.class)
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<Problem> getProblemById(@PathVariable("id") long id) {
        Problem problem = problemService.findProblemById(id);
        return new ResponseEntity<>(problem, HttpStatus.OK);
    }

    @Operation(summary = "Удаляет проблему по id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Проблема удалена"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Проблема с таким id не сущетсвует",
                    content =
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = NoSuchProblemException.class)
                    )
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProblem(@PathVariable long id) {
        problemService.deleteProblemById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
