package models;

import java.util.List;

/**
 * TestKnowledgeBase adds direct access to the knowledge base's data structures
 * (use only for testing!). 
 * 
 * @author sbuenzli@student.unibe.ch
 * @version 2010-09-30
 */
public class TestKnowledgeBase extends KnowledgeBase
{
	public List<User> users()
	{
		return (List<User>)_users.clone();
	}
	
	public List<Question> questions()
	{
		return (List<Question>)_questions.clone();
	}
	
	public List<Answer> answers()
	{
		return (List<Answer>)_answers.clone();
	}
}
