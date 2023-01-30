package com.mracover.telegram_bot.service;

import com.mracover.telegram_bot.exception.problemException.NoSuchProblemException;
import com.mracover.telegram_bot.model.Problem;
import com.mracover.telegram_bot.repository.ProblemRepository;
import com.mracover.telegram_bot.service.impl.ProblemServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProblemServiceTest {

    private final static Problem problem = new Problem();
    private ProblemRepository problemRepository;
    private ProblemService problemService;

    @BeforeEach
    void prepare() {
        System.out.println("Before each: " + this);
        this.problemRepository = Mockito.mock(ProblemRepository.class);
        this.problemService = new ProblemServiceImpl(problemRepository);
    }

    @Test
    void shouldAddProblem() {
        Mockito.doReturn(problem).when(problemRepository).save(problem);
        var answer = problemService.addProblem(problem);
        assertNotNull(answer);
    }

    @Test
    void shouldGetAllProblems() {
        List<Problem> problemList = new ArrayList<>();
        problemList.add(problem);
        Mockito.doReturn(problemList).when(problemRepository).findAll();
        var answer = problemService.getAllProblems();
        assertFalse(answer.isEmpty());
    }

    @Test
    void shouldThrowNoSuchProblemExceptionWhenGetAllProblems() {
        List<Problem> problemList = new ArrayList<>();
        Mockito.doReturn(problemList).when(problemRepository).findAll();
        assertThrows(NoSuchProblemException.class, () ->
                problemService.getAllProblems());
    }

    @Test
    void shouldFindProblemById() {
        Mockito.doReturn(Optional.of(new Problem())).when(problemRepository).findById(Mockito.anyLong());
        var answer = problemService.findProblemById(Mockito.anyLong());
        assertNotNull(answer);
    }

    @Test
    void shouldThrowNoSuchProblemExceptionWhenFindProblemById() {
        Mockito.doReturn(Optional.empty()).when(problemRepository).findById(Mockito.anyLong());
        assertThrows(NoSuchProblemException.class, () ->
                problemService.findProblemById(Mockito.anyLong()));
    }

    @Test
    void shouldUpdateProblem() {
        Mockito.doReturn(new Problem()).when(problemRepository).save(problem);
        var answer = problemService.updateProblem(problem);
        assertNotNull(answer);
    }

    @Test
    void shouldThrowNoSuchProblemExceptionWhenUpdateProblem() {
        Mockito.doReturn(null).when(problemRepository).save(problem);
        assertThrows(NoSuchProblemException.class, () ->
                problemService.updateProblem(problem));
    }

    @Test
    void shouldDeleteProblemById() {
        Mockito.doReturn(Optional.of(new Problem())).when(problemRepository).findById(Mockito.anyLong());
        Mockito.doNothing().when(problemRepository).deleteById(Mockito.anyLong());
        assertDoesNotThrow(() -> problemService.deleteProblemById(Mockito.anyLong()));
    }
}