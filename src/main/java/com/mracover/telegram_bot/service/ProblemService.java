package com.mracover.telegram_bot.service;

import com.mracover.telegram_bot.model.Problem;

import java.util.List;

public interface ProblemService {
    Problem addProblem (Problem problem);
    List<Problem> getAllProblems();
    Problem findProblemById(Long id);
    Problem updateProblem(Problem problem);
    void deleteProblemById(Long id);
}
