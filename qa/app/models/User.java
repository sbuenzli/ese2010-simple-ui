package models;

/**
 * User implements a user having a name.
 * 
 * @author sbuenzli@student.unibe.ch
 * @version 2010-09-24
 */
public class User {
	private String _name;
	
	public User(String name)
	{
		if (null == name || name.length() == 0)
			throw new IllegalArgumentException("Users must have a name!");
		_name = name;
	}
	
	public String getName()
	{
		return _name; 
	}
}
