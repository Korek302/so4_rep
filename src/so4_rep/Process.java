package so4_rep;

import java.util.*;
public class Process
{
	int pageQuantity;
	ArrayList<Integer> references;
	int bit1;
	int bit2;
	public Process(int pageQuantity, ArrayList<Integer> references, int bit1, int bit2)
	{
		this.pageQuantity = pageQuantity;
		this.references = references;
		this.bit1 = bit1;
		this.bit2 = bit2;
	}
	
	public int getReference(int i)
	{
		return references.get(i);
	}
}
