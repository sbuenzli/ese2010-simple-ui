/**
 * The goal of this task is to implement a simple "knowledge base" with users,
 * questions and answers.
 * 
 * The requirements you are asked to implement are:
 * 	Users have a name.
 * 	Questions are owned by a user.
 * 	Answers are owned by a user and belong to a question.
 * 	Questions and answers have a timestamp and a contents.
 * 	Questions and answers can be voted up and down by a user.
 * 	A user can be deleted, in which case its votes, answers and questions are deleted.
 * 
 * Note that this task only involves to model the requested features of the entities
 * User, Question, Answer, ( Vote ?). We do not ask you to provide a user-interface.
 * The code has to be accompanied with unit-tests though. For each of the above
 * requirements, it is required to have at least one test method containing at least
 * one assertion.
 */

package models;

import java.util.*;
import play.test.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnitTests extends UnitTest {
	private TestKnowledgeBase _kb;
	
	@org.junit.Before
	public void setupKnowledgeBase()
	{
		_kb = new TestKnowledgeBase();
		
		User user1 = new User("User One");
		_kb.addUser(user1);
		User user2 = new User("User Two");
		_kb.addUser(user2);
		User user3 = new User("User Three");
		_kb.addUser(user3);
		
		Question question1 = new Question(user1, "A question?");
		_kb.addQuestion(question1);
		Question question2 = new Question(user1, "Another question?");
		_kb.addQuestion(question2);
		Question question3 = new Question(user2, "Yet another question?");
		_kb.addQuestion(question3);
		
		_kb.addAnswer(new Answer(question2, user2, "One answer!"));
		_kb.addAnswer(new Answer(question2, user2, "Another answer!"));
		_kb.addAnswer(new Answer(question3, user1, "Two answers!"));
		_kb.addAnswer(new Answer(question3, user2, "Two more answers!"));
	}
	
	@Test /* Users have a name. */
	public void testUserHasName()
	{
		assertEquals(_kb.users().size(), 3);
		for (User user : _kb.users())
			assertNotNull(user.getName());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testUserMustHaveNonNullName()
	{
		new User(null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testUserMustHaveName()
	{
		new User("");
	}
	
	@Test /* Questions are owned by a user. */
	public void testOwnedQuestions()
	{
		assertEquals(_kb.questions().size(), 3);
		for (Question question : _kb.questions())
		{
			assertNotNull(question.getOwner());
			assertTrue(_kb.users().contains(question.getOwner()));
		}
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testQuestionMustBeOwned()
	{
		new Question(null, "Question?");
	}
	
	@Test /* Answers are owned by a user and belong to a question. */
	public void testOwnedAnswers()
	{
		assertEquals(_kb.answers().size(), 4);
		for (Answer answer : _kb.answers())
		{
			assertNotNull(answer.getOwner());
			assertTrue(_kb.users().contains(answer.getOwner()));
			assertTrue(_kb.questions().contains(answer.getQuestion()));
		}
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testAnswersMayNotBeEmpty()
	{
		new Answer(null, null, null);
	}
	
	@Test /* Questions and answers have a timestamp and a contents. */
	public void testTimestampsAndContents()
	{
		for (Question question : _kb.questions())
		{
			assertNotNull(question.getTimestamp());
			assertTrue(!question.getTimestamp().after(new Date()));
			assertNotNull(question.getContent());
			assertTrue(question.getContent().length() > 0);
		}
		for (Answer answer : _kb.answers())
		{
			assertNotNull(answer.getTimestamp());
			assertTrue(!answer.getTimestamp().after(new Date()));
			assertTrue(!answer.getTimestamp().after(answer.getQuestion().getTimestamp()));
			assertNotNull(answer.getContent());
			assertTrue(answer.getContent().length() > 0);
		}
	}
	
	@Test /* Questions and answers can be voted up and down by a user. */
	public void testVoting()
	{
		User user1 = _kb.getUser("User One");
		Question question3 = _kb.getQuestion(2);
		Answer answer1 = _kb.getAnswer(0);
		
		assertEquals(question3.getVoteCount(), 0);
		assertNull(question3.voteUpFor(user1));
		assertEquals(question3.getVoteCount(), 1);
		assertEquals(question3.voteUpFor(user1), true);
		assertEquals(question3.getVoteCount(), 1);
		assertEquals(question3.voteDownFor(user1), true);
		assertEquals(question3.getVoteCount(), -1);
		assertEquals(question3.voteCancelFor(user1), false);
		assertEquals(question3.getVoteCount(), 0);
		
		assertEquals(answer1.getVoteCount(), 0);
		assertNull(answer1.voteDownFor(user1));
		assertEquals(answer1.getVoteCount(), -1);
		assertEquals(answer1.voteDownFor(user1), false);
		assertEquals(answer1.getVoteCount(), -1);
		assertEquals(answer1.voteUpFor(user1), false);
		assertEquals(answer1.getVoteCount(), 1);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testUserCantVoteForHerself()
	{
		User user1 = _kb.getUser("User One");
		Question question1 = _kb.getQuestion(0);
		question1.voteUpFor(user1);
	}
	
	@Test /* A user can be deleted, in which case its votes, answers and questions are deleted. */
	public void testRemoveUser1()
	{
		User user1 = _kb.getUser("User One");
		User user3 = _kb.getUser("User Three");
		Question question3 = _kb.getQuestion(2);
		Answer answer4 = _kb.getAnswer(3);
		
		question3.voteUpFor(user1);
		assertEquals(question3.getVoteCount(), 1);
		question3.voteDownFor(user3);
		assertEquals(question3.getVoteCount(), 0);
		
		answer4.voteDownFor(user1);
		assertEquals(answer4.getVoteCount(), -1);
		answer4.voteUpFor(user3);
		assertEquals(question3.getVoteCount(), 0);
		
		_kb.removeUser(user1);
		assertEquals(_kb.users().size(), 2);
		assertEquals(_kb.questions().size(), 1);
		assertTrue(_kb.questions().contains(question3));
		assertEquals(question3.getVoteCount(), -1);
		assertEquals(_kb.answers().size(), 1);
	}
	
	@Test
	public void testRemoveUser2()
	{
		User user2 = _kb.getUser("User Two");
		Question question2 = _kb.getQuestion(1);
		
		question2.voteUpFor(user2);
		assertEquals(question2.getVoteCount(), 1);
		
		_kb.removeUser(user2);
		assertEquals(_kb.users().size(), 2);
		assertEquals(_kb.questions().size(), 2);
		assertTrue(_kb.questions().contains(question2));
		assertEquals(question2.getVoteCount(), 0);
		assertEquals(_kb.answers().size(), 0);
	}
	
	public static void main(String[] args)
	{
		org.junit.runner.JUnitCore.runClasses(UnitTests.class);
	}
}
