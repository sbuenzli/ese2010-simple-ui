package models;

import java.util.Date;
import java.util.HashMap;

/**
 * UserComment is the base class for both Question and Answer,
 * holding the common attributes owner, content string and timestamp
 * and counting votes for all users (ensuring that no user votes more
 * than once).
 * 
 * @author sbuenzli@student.unibe.ch
 * @version 2010-09-30
 */
public abstract class UserComment {
	private User _owner;
	private String _content;
	private Date _timestamp;
	private HashMap<User, Boolean /* isUpVote */> _votes = new HashMap<User, Boolean>();
	public int id; // cached value for KnowledgeBase._questions.indexOf(this)
	
	public UserComment(User owner, String content)
	{
		if (null == owner || null == content || content.length() == 0)
			throw new IllegalArgumentException("User comments must have an owner and content!");
		
		_owner = owner;
		_content = content;
		_timestamp = new Date();
	}
	
	public User getOwner()
	{
		return _owner;
	}
	
	public String getContent()
	{
		return _content;
	}
	
	public Date getTimestamp()
	{
		return _timestamp;
	}
	
	public Boolean voteUpFor(User user)
	{
		if (_owner == user)
			throw new IllegalArgumentException("User can't vote for himself/herself!");
		
		return _votes.put(user, true);
	}
	
	public Boolean voteDownFor(User user)
	{
		if (_owner == user)
			throw new IllegalArgumentException("User can't vote for himself/herself!");
		
		return _votes.put(user, false);
	}
	
	public Boolean voteCancelFor(User user)
	{
		return _votes.remove(user);
	}
	
	public Boolean getVoteFor(User user)
	{
		return _votes.get(user);
	}
	
	public int getVoteCount()
	{
		int count = 0;
		for (User user : _votes.keySet())
			count += _votes.get(user) ? 1 : -1;
		return count;
	}
}
