package so4_rep;

import java.util.ArrayList;
import java.util.Random;

public class EqualAssignment
{	
	Random r = new Random();
	
	int framesQuantity;
	int processesQuantity;
	int referencesQuantity;

	ArrayList<Process> processes;
	ArrayList<Frame> frames;
	ArrayList<Integer> assignment = new ArrayList<Integer>();
	
	public EqualAssignment(int framesQuantity, int processesQuantity, int referencesQuantity, ArrayList<Process> processes, 
			ArrayList<Frame> frames)
	{
	
		this.framesQuantity = framesQuantity;
		this.processesQuantity = processesQuantity;
		this.referencesQuantity = referencesQuantity;
	
		this.processes = processes;
		this.frames = frames;
	}
	
	public Process getProcess(int index)
	{
		return processes.get(index);
	}
	
	public void clear()
	{
		Frame f = new Frame(0, 0);
		for(int i = 0; i < framesQuantity; i++)
		{
			frames.set(i,  f);
		}
	}
	
	public void equalAssignment()
	{
		for(int i = 0; i < processesQuantity; i++)
		{
			int a = framesQuantity / processesQuantity;
			assignment.add(a);
		}
	}
	
	public void LRU()
	{
		int assign = assignment.get(0);
		int range1 = 0;
		int range2 = assign;
		int sum2 = 0;
		
		for(int i = 0; i < processesQuantity; i++)
		{
			int pageErrors = 0;
			boolean isDone = false;
			if(i != 0)
			{
				assign = assignment.get(i);
				range1 = range2;
				range2 += assign;
			}
			for(int j = 0; j < referencesQuantity; j++)
			{
				for(int r = range1; r < range2 && !isDone; r++)
				{
					if(getProcess(i).getReference(j) == frames.get(r).value)
					{
						Frame f = new Frame(getProcess(i).getReference(j), 0);
						frames.set(r, f);
						for(int k = 0; k < range2; k++)
						{
							if(getProcess(i).getReference(j) != frames.get(k).value)
							{
								int zp1 = frames.get(k).value;
								int zp2 = frames.get(k).bit;
								Frame fr = new Frame(zp1, (zp2)+1);
								frames.set(k, fr);
							}
						}
						isDone = true;
					}
					if(frames.get(r).value == 0)
					{
						Frame f2 = new Frame(getProcess(i).getReference(j), 0);
						frames.set(r, f2);
						pageErrors++;
						isDone = true;
					}
				}
				if(!isDone)
				{
					int zp3 = 0;
					int index = 0;
					for(int l = 0; l < range2; l++)
					{
						if(frames.get(l).bit > zp3)
						{
							zp3 = frames.get(l).bit;
							index = l;
						}
					}
					Frame f3 = new Frame(getProcess(i).getReference(j), 0);
					frames.set(index, f3);
					pageErrors++;
				}
				isDone = false;
			}
			System.out.println("Process number: "+(i+1)+",  \tpages: "+getProcess(i).pageQuantity+
					",  \tassigned frames: "+assign+",  \tpage errors: "+pageErrors);
			sum2 += pageErrors;
		}
		System.out.println("Total errors: "+sum2);
		assignment.clear();
		clear();
	}
}
