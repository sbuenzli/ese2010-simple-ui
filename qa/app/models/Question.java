package models;

/**
 * Question a user has asked (see UserComment for all properties).
 * 
 * @author sbuenzli@student.unibe.ch
 * @version 2010-09-24
 */
public class Question extends UserComment {
	public int answerCount; // cached value for KnowledgeBase.getAnswers(this).size()
	
	public Question(User owner, String content)
	{
		super(owner, content);
	}
}
