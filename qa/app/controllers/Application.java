package controllers;

import play.*;
import play.mvc.*;
import play.data.validation.*;
import models.*;
import java.util.*;

/**
 * Sole controller for the Basic Knowledge Base's web interface.
 * 
 * @author sbuenzli@student.unibe.ch
 * @version 2010-09-30
 */
public class Application extends Controller {
	public static TestKnowledgeBase kb;
	
	/**
	 * Adds information about the logged in user (if any) to all
	 * pages rendered by this controller.
	 **/
	@Before
	public static void setConnectedUser()
	{
		renderArgs.put("user", kb.getUser(Security.connected()));
	}
	
	/* List the questions. The default page lists the questions ordered by votes. */
	public static void index()
	{
		List<Question> questions = kb.questions();
		Collections.sort(questions, new VoteCountComparator());
		for (Question question : questions)
			question.answerCount = kb.getAnswers(question).size();
		render(questions);
	}
	
	/* Add a question. The logged in user can add a question. */
	public static void addQuestion(@Required String newQuestion)
	{
		if (validation.hasErrors())
		{
			flash.error("What was the question?");
			index();
		}
		
		User user = (User)renderArgs.get("user");
		// This throws for non-authenticated users (since this route isn't
		// exposed to them, there's no need to handle the exception, though)
		Question question = new Question(user, newQuestion);
		kb.addQuestion(question);
		question.id = kb.questions().indexOf(question);
		index();
	}
	
	/* View and answer a question. The logged in user can view and answer a question.
	 * He can answer the same question multiple times. */
	public static void showQuestion(int id)
	{
		Question question = kb.getQuestion(id);
		List<Answer> answers = kb.getAnswers(question);
		Collections.sort(answers, new VoteCountComparator());
		render(id, question, answers);
	}
	
	public static void addAnswer(int id, @Required String newAnswer)
	{
		if (validation.hasErrors())
		{
			flash.error("What was the answer?");
			showQuestion(id);
		}
		
		User user = (User)renderArgs.get("user");
		Question question = kb.getQuestion(id);
		Answer answer = new Answer(question, user, newAnswer);
		kb.addAnswer(answer);
		answer.id = kb.answers().indexOf(answer);
		showQuestion(id);
	}
	
	/* Vote up and down. The logged in user can vote up or down questions and answers.
	 * The user has only one vote (up/down) per question or answer. He can not vote
	 * for his own question and answer. */
	public static void voteUpQuestion(int id)
	{
		User user = (User)renderArgs.get("user");
		Question question = kb.getQuestion(id);
		if (question.getOwner() != user)
			question.voteUpFor(user);
		// else - no need to throw, since this route isn't exposed to the user
		showQuestion(id);
	}
	
	public static void voteDownQuestion(int id)
	{
		User user = (User)renderArgs.get("user");
		Question question = kb.getQuestion(id);
		if (question.getOwner() != user)
			question.voteDownFor(user);
		showQuestion(id);
	}
	
	public static void voteUpAnswer(int id, int aid)
	{
		User user = (User)renderArgs.get("user");
		Answer answer = kb.getAnswer(aid);
		if (answer.getOwner() != user)
			answer.voteUpFor(user);
		showQuestion(id);
	}
	
	public static void voteDownAnswer(int id, int aid)
	{
		User user = (User)renderArgs.get("user");
		Answer answer = kb.getAnswer(aid);
		if (answer.getOwner() != user)
			answer.voteDownFor(user);
		showQuestion(id);
	}
}
