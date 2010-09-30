package jobs;

import controllers.Application;
import play.jobs.*;
import models.*;

/**
 * Bootstrap job to set up a few questions and answers by the three users
 * userone, usertwo and userthree.
 * 
 * @author sbuenzli@student.unibe.ch
 * @version 2010-09-30
 */
@OnApplicationStart
public class Bootstrap extends Job {
	public void doJob()
	{
		Application.kb = new TestKnowledgeBase();
		
		TestKnowledgeBase _kb = Application.kb;
		User user1 = new User("userone");
		_kb.addUser(user1);
		User user2 = new User("usertwo");
		_kb.addUser(user2);
		User user3 = new User("userthree");
		_kb.addUser(user3);
		
		Question question1 = new Question(user1, "A question?");
		_kb.addQuestion(question1);
		Question question2 = new Question(user1, "Another question?");
		_kb.addQuestion(question2);
		Question question3 = new Question(user2, "Yet another question?");
		_kb.addQuestion(question3);
		
		for (Question question : _kb.questions())
			question.id = _kb.questions().indexOf(question);
		
		_kb.addAnswer(new Answer(question2, user2, "One answer!"));
		_kb.addAnswer(new Answer(question2, user2, "Another answer!"));
		_kb.addAnswer(new Answer(question3, user1, "Two answers!"));
		_kb.addAnswer(new Answer(question3, user2, "Two more answers!"));
		
		for (Answer answer : _kb.answers())
			answer.id = _kb.answers().indexOf(answer);
	}
}
