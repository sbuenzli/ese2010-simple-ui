package models;

import java.util.Comparator;

public class VoteCountComparator implements Comparator
{
	public int compare(Object o1, Object o2)
	{
		return ((UserComment)o2).getVoteCount() - ((UserComment)o1).getVoteCount();  
	}
}
