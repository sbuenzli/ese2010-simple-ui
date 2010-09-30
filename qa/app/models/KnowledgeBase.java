package models;

import java.util.*;

/**
 * KnowlegeBase is a simple in-memory database for users and their questions and
 * answers (which they can also vote up or down). You have to implement access to
 * users, questions and answers, though, if you actually want to work with it.
 * 
 * @author sbuenzli@student.unibe.ch
 * @version 2010-09-30
 */
public abstract class KnowledgeBase {
	protected ArrayList<User> _users = new ArrayList<User>();
	protected ArrayList<Question> _questions = new ArrayList<Question>();
	protected ArrayList<Answer> _answers = new ArrayList<Answer>();
	
	public void addUser(User user)
	{
		_users.add(user);
	}
	
	public User getUser(String name)
	{
		for (User user : _users)
			if (user.getName().equals(name))
				return user;
		return null;
	}
	
	public void removeUser(User user)
	{
		if (_users.indexOf(user) == -1)
			throw new IllegalArgumentException("User was never added!");
		
		ArrayList<UserComment> userComments = new ArrayList<UserComment>();
		
		for (Question question : _questions)
		{
			question.voteCancelFor(user);
			if (question.getOwner() == user)
				userComments.add(question);
		}
		
		for (Answer answer : _answers)
		{
			answer.voteCancelFor(user);
			if (answer.getOwner() == user)
				userComments.add(answer);
		}
		
		for (UserComment comment : userComments)
		{
			if (comment instanceof Question)
				removeQuestion((Question)comment);
			else
				removeAnswer((Answer)comment);
		}
		
		_users.remove(user);
	}
	
	public void addQuestion(Question question)
	{
		if (_users.indexOf(question.getOwner()) == -1)
			throw new IllegalArgumentException("Question from unknown user!");
		_questions.add(question);
	}
	
	public Question getQuestion(int id)
	{
		return _questions.get(id);
	}
	
	public void removeQuestion(Question question)
	{
		if (_questions.indexOf(question) == -1)
			throw new IllegalArgumentException("Question was never added!");
		_questions.remove(question);
		
		for (Answer answer : getAnswers(question))
			removeAnswer(answer);
	}
	
	public void addAnswer(Answer answer)
	{
		if (_users.indexOf(answer.getOwner()) == -1)
			throw new IllegalArgumentException("Answer from unknown user!");
		_answers.add(answer);
	}
	
	public Answer getAnswer(int aid)
	{
		return _answers.get(aid);
	}
	
	public List<Answer> getAnswers(Question question)
	{
		List<Answer> answers = new ArrayList<Answer>();
		
		for (Answer answer : _answers)
			if (answer.getQuestion() == question)
				answers.add(answer);
		
		return answers;
	}
	
	public void removeAnswer(Answer answer)
	{
		_answers.remove(answer);
	}
}
