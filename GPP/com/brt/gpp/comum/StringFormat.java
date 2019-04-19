package com.brt.gpp.comum;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;

/**
 * <code>StringFormart</code> formats Strings acordding to given pattern.
 * It's possible to align Strings to the LEFT (Appending characters at the end
 * of the String) ou to the RIGHT (Inserting characters at the begining of the String).
 * <p>
 * The pattern is a String that must follow this format <code>(d)(l|r)(.)</code> where:
 * <ul>
 * <li> <code>(d)</code>		represents the new Length
 * <li> <code>(l|r)</code>		represents the Alignment (l - Left, r - Right)
 * <li> <code>(.)</code>		represents ANY character that will be used to resize the String
 * </ul>
 * <hr>
 * Usage:
 * <code>
 *   <pre>
 *     StringFormat sf = new StringFormat("20l.");
 *     String s = "Hello World!";
 *     System.out.println(sf.format(s));
 *  </pre>
 * </code>
 * Output: <code>Hello World!........</code>
 * <hr>
 * <code>
 *   <pre>
 *     StringFormat sf = new StringFormat("20r_");
 *     String s = "Hello World!";
 *     System.out.println(sf.format(s));
 *  </pre>
 * </code>
 * Output: <code>________Hello World!</code>
 *
 * @author Leone Parise Vieira da Silva
 * @since  1.0, 08/10/2007
 */
public class StringFormat extends Format
{
	private static final long serialVersionUID = 8922661761922162629L;
	
	public static char LEFT_ALIGN		= 'L';
	public static char RIGHT_ALIGN		= 'R';

	private String	pattern;
	private int		length;
	private char	character;
	private char	align;

	/**
	 * Creates a Stringformat using the given pattern.
	 * The pattern is a String that must follow this format <code>(d)(l|r)(.)</code> where:
	 * <ul>
	 * <li> <code>(d)</code>		represents the new Length
	 * <li> <code>(l|r)</code>		represents the Alignment (l - Left, r - Right)
	 * <li> <code>(.)</code>		represents ANY character that will be used to resize the String
	 * </ul>
	 *
	 * @param pattern
	 * @see com.brt.gpp.comum.StringFormat;
	 */
	public StringFormat(String pattern)
	{
		compile(pattern);
	}

	private void compile(String pattern)
	{
		if (pattern != null)
		{
			if(!pattern.matches("^\\d+[lrLR].?$"))
				throw new IllegalArgumentException("Invalid pattern: "+pattern);
	
			this.length = Integer.parseInt(pattern.substring(0, pattern.length()-2));
			this.align = Character.toUpperCase(pattern.charAt(pattern.length()-2));
			this.character = pattern.charAt(pattern.length()-1);
		}
		this.pattern = pattern;
	}
		
	/**
	 * Returns a String formatted according to the given pattern.<br>
	 * NOTE: If <code>length</code> given by the pattern is smaller than
	 * <code>str.length()</code> returns a cropped String.
	 *
	 * @param str			String
	 *
	 * @return				Formatted String.
	 */
	public String format(String str)
	{
		if (this.pattern == null)
			return str;
		
		if(str == null) str = "";
		StringBuffer sb = new StringBuffer(str);

		if(this.length <= 0) return sb.toString();

		int delta = this.length - sb.length();

		if(delta < 0)
		{
			delta = Math.abs(delta);

			sb.delete(sb.length()-delta, sb.length());
		}
		else
		{
			for(int i = 0; i < delta; i++)
			{
				if(this.align == LEFT_ALIGN)
					sb.append(this.character);
				else if(this.align == RIGHT_ALIGN)
					sb.insert(0, this.character);
				else
					break;
			}
		}

		return sb.toString();
	}

	public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos)
	{
		String str = obj.toString();
		pos.setEndIndex(pos.getBeginIndex()+length);
		return toAppendTo.insert(pos.getBeginIndex(), format(str));
	}

	public Object parseObject(String source, ParsePosition pos)
	{
		source = source.substring(pos.getIndex());
		pos.setIndex(pos.getIndex()+source.length());
		return source;
	}

	public String getPattern()
	{
		return pattern;
	}

	public void setPattern(String pattern)
	{
		compile(pattern);
	}
}
