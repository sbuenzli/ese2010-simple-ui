package models;

/**
 * Answer a user has given to a question (see UserComment for all properties).
 * 
 * @author sbuenzli@student.unibe.ch
 * @version 2010-09-24
 */
public class Answer extends UserComment {
	private Question _question;
	
	public Answer(Question question, User owner, String content)
	{
		super(owner, content);
		
		if (null == question || getTimestamp().before(question.getTimestamp()))
			throw new IllegalArgumentException("Answer was not to a (older) question");
		
		_question = question;
	}
	
	public Question getQuestion()
	{
		return _question;
	}
}
