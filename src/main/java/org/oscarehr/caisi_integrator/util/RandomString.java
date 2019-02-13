package org.oscarehr.caisi_integrator.util;

public class RandomString
{
	/**
	 * Creates a new RandomString object.
	 */
	public RandomString()
	{
		// Empty constructor
	}

	/**
	 * Retrieve an eight (8) alphanumeric character string.
	 * 
	 * @return An eight (8) character string.
	 */
	public static String getString()
	{
		return getString(8);
	}

	/**
	 * Return a random string of varying length. Contains both letters and
	 * numbers.
	 * 
	 * @param length
	 *           The length of the string.
	 * @return The randomly generated text.
	 */
	public static String getString(int length)
	{
		// Generate a random string of length
		String validChars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		StringBuilder randomText = new StringBuilder();
		for (int i = 0; i < length; i++)
		{
			int randSeed = new Double(Math.random() * validChars.length()).intValue();
			randomText.append(validChars.charAt(randSeed));
		}

		return randomText.toString();
	}

	/**
	 * Retrieve a random alphanumeric character.
	 * 
	 * @return The random character.
	 */
	public static char getChar()
	{
		String validChars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		int randSeed = new Double(Math.random() * validChars.length()).intValue();

		return validChars.charAt(randSeed);
	}

	/**
	 * A random string, but only containing integers.
	 * 
	 * @param length
	 *           Length of the random string.
	 * @return The randomly generated string that consists solely of integers.
	 */
	public static String getInt(int length)
	{
		String validChars = "0123456789";
		StringBuilder randomText = new StringBuilder();
		for (int i = 0; i < length; i++)
		{
			int randSeed = new Double(Math.random() * validChars.length()).intValue();
			randomText.append(validChars.charAt(randSeed));
		}

		return randomText.toString();
	}
}