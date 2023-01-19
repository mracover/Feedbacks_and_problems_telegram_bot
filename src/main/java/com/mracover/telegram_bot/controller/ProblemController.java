package com.mracover.telegram_bot.controller;

import com.mracover.telegram_bot.model.Problem;
import com.mracover.telegram_bot.service.ProblemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/problems")
public class ProblemController {

    private final ProblemService problemService;

    public ProblemController(ProblemService problemService) {
        this.problemService = problemService;
    }

    @GetMapping("/")
    public ResponseEntity<List<Problem>> getAllProblems() {
        List<Problem> problems = problemService.getAllProblems();
        return new ResponseEntity<>(problems, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Problem> getProblemById(@PathVariable("id") long id) {
        Problem problem = problemService.findProblemById(id);
        return new ResponseEntity<>(problem, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProblem(@PathVariable long id) {
        problemService.deleteProblemById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
