import java.lang.Math;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;


class players
{	
	/********** Instance Variables ************/
	int level, score;
	char operator;


	/********** Constants ***********/
	final int T_QUESTIONS = 10;		/* Ensure only 10 questions are asked for any given level */
	final byte EOF = -1;			/* Return value to indicate <Ctrl + D> */


	Scanner input = new Scanner(System.in);
	Random r_generator = new Random();


	// Initialize a player with level 0 and score 0.
	players()
	{
		level = 0;
		score = 0;
		// Set addition as the default operation.
		operator = '+';
	}

	
	/********** Methods ***********/

	// A function that implements taking input from the user.
	void get_operator()
	{
		char valid_operators[] = {'+', '-', '*', '%'};
		String buffer = "";

		System.out.println();

		while (true)
		{
			System.out.print("Operator [ +, - , *, % ]: ");
			
			// Read the input string.
			try
			{
				buffer = input.nextLine().strip();
			}
			catch (NoSuchElementException e)
			{
				System.out.println("\nEOF Read. Exiting...");
				System.exit(EOF);
			}
			
			// Ensure handling inputs like *input.
			if (buffer.length() != 1)
				continue;

			try
			{
				operator = buffer.charAt(0);
			}
			// Handle empty strings.
			catch (StringIndexOutOfBoundsException e)
			{
				continue;
			}


			// Ensure that the inputs exists in the array of valid operators.
			for(char op: valid_operators)
			{
				if (operator == op)
					return;
			}
		}
	}

	// A function that implements taking input from the user.
	void get_level()
	{	
		while (true)
		{
			System.out.print("Level: ");
			try
			{
				level = input.nextInt();
			}
			catch (InputMismatchException e)
			{
				// Read the next line buffer to avoid infinite loop.
				input.nextLine();
				continue;
			}
			catch (NoSuchElementException e)
			{
				System.out.println("\nExiting....");
				System.exit(EOF);
			}
			
			// break the loop on reading a valid input.
			if (level <= 0 || level/4 != 0) 
				continue;

			break;
		}
	}
	
	void play()
	{
		int q_index = 0, r_answer = 0;
	
		int answer = -1;	// User's answer.

		// Generate 10 random questions.
		while (q_index < T_QUESTIONS)
		{
			boolean right = false;

			int x = random_number_generator(level), y;

			// Handle division by zero in case of level 1.
			if (level == 1)
				y = r_generator.nextInt(1,9);
			else
				y = random_number_generator(level);

			
			// Initializer r_answer according to the operator selected.
			switch (operator)
			{
				case '+':
					r_answer = x+y;
					break;

				case '-':
					r_answer = x-y;
					break;

				case '*':
					r_answer = x*y;
					break;

				case '%':
					r_answer = x%y;
					break;
				// No other case can occur. Already Error Handled.
			}


			System.out.println();

			// Provide 3 attempts to get the right answer. 
			for (int attempt = 0; attempt < 3; attempt++)
			{
				while (true)
				{
					// Print the question according to the selected operator.
					System.out.printf("%d %c %d = ", x, operator, y);

					try
					{
						answer = input.nextInt();
					}
					catch (InputMismatchException e)
					{
						// Read the \n buffer to avoid infinite loop.
						input.nextLine();
						continue;
					}
					// Handle Ctrl + D.
					catch (NoSuchElementException e)
					{
						System.out.println("\nEOF Read. Exiting....");
						System.exit(EOF);
					}
					break;
				}

				// Evaluate the answer.
				if (r_answer == answer)
				{
					// Increase the score on answering right.
					right = true;
					score++;
					break;
				}
				else
					System.out.println("EEE");
			}

			// If the user is unable to answer within three attempts. Print the right answer.
			if (!right)
				System.out.printf("%d %c %d = %d\n", x, operator, y, r_answer);

			q_index++;
		}
	}
	
	// A function that generates a random number with respect according to the level.
	int random_number_generator(int level)
	{
		if (level == 1)
			return r_generator.nextInt(0, 9);
		else
		{
			int lower_bound= (int) Math.pow(10, level-1);
			int upper_bound = (int) (Math.pow(10, level) - 1);

			return r_generator.nextInt(lower_bound, upper_bound);
		}
	}

	void print_scores()
	{
		System.out.println("\n\n==========================================");
		System.out.println("		Results			  ");
		System.out.println("Score : " + score + "\n");
	}
}

class professor
{
	public static void main(String args[])
	{
		players player = new players();

		// Get the level from the user.
		player.get_operator();

		// Get the level from the user.
		player.get_level();

		// Let the user play at the entered level.
		player.play();

		// Print the results for the game play.
		player.print_scores();
	}
}
