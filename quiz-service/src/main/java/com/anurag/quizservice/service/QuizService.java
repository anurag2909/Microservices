package com.anurag.quizservice.service;


import com.anurag.quizservice.dao.QuizDao;
import com.anurag.quizservice.feign.QuizInterface;
import com.anurag.quizservice.model.QuestionWrapper;
import com.anurag.quizservice.model.Quiz;
import com.anurag.quizservice.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuizService {

    @Autowired
    QuizDao quizDao;

    @Autowired
    QuizInterface quizInterface;

    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(Integer id) {

        Quiz quiz = quizDao.findById(id).get();
        List<Integer> questionIds = quiz.getQuestionIds();
        ResponseEntity<List<QuestionWrapper>> questions = quizInterface.getQuestionsFromId(questionIds);

        return questions;
    }


    public ResponseEntity<String> createQuiz(String category, int numQ, String title) {

        List<Integer> questions = quizInterface.getQuestionsForQuiz(category,numQ).getBody();

        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setQuestionIds(questions);

        quizDao.save(quiz);

        return new ResponseEntity<>("success", HttpStatus.CREATED);
    }

    public ResponseEntity<Integer> calculateResult(Integer id, List<Response> responses) {

        ResponseEntity<Integer> score =  quizInterface.getScore(responses);
        return score;

    }
}
