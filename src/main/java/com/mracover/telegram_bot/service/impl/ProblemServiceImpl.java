package com.mracover.telegram_bot.service.impl;

import com.mracover.telegram_bot.exception.DatabaseException;
import com.mracover.telegram_bot.exception.problemException.NoSuchProblemException;
import com.mracover.telegram_bot.exception.userException.NoSuchUserException;
import com.mracover.telegram_bot.model.Problem;
import com.mracover.telegram_bot.repository.ProblemRepository;
import com.mracover.telegram_bot.service.ProblemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProblemServiceImpl implements ProblemService {

    private ProblemRepository problemRepository;

    public ProblemServiceImpl(ProblemRepository problemRepository) {
        this.problemRepository = problemRepository;
    }

    @Override
    @Transactional
    public Problem addProblem(Problem problem) throws DatabaseException {
        try {
            return problemRepository.save(problem);
        } catch (RuntimeException ex) {
            throw new DatabaseException();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Problem> getAllProblems() throws DatabaseException, NoSuchProblemException{
        List<Problem> problems;
        try {
            problems = problemRepository.findAll();
        } catch (RuntimeException ex) {
            throw new DatabaseException();
        }
        if (problems.isEmpty()) {
            throw new NoSuchProblemException("Сообщение о проблемах не найдены");
        }
        return problems;
    }

    @Override
    @Transactional(readOnly = true)
    public Problem findProblemById(Long id) throws DatabaseException, NoSuchProblemException{
        try {
            return problemRepository.findById(id).orElseThrow(() ->
                    new NoSuchProblemException("Сообщение о проблеме не найдено"));
        } catch (RuntimeException ex) {
            throw new DatabaseException();
        }
    }

    @Override
    @Transactional
    public Problem updateProblem(Problem problem) throws DatabaseException, NoSuchProblemException {
        try {
            return Optional.of(problemRepository.save(problem)).orElseThrow(() ->
                    new NoSuchUserException("Обновляемое сообщение о проблемы не найдено"));
        } catch (RuntimeException ex) {
            throw new DatabaseException();
        }
    }

    @Override
    @Transactional
    public void deleteProblemById(Long id) throws DatabaseException, NoSuchProblemException {
        try {
            findProblemById(id);
            problemRepository.deleteById(id);
        } catch (RuntimeException ex) {
            throw new DatabaseException();
        }
    }
}
