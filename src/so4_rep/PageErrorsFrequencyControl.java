package so4_rep;

import java.util.ArrayList;
import java.util.Random;

public class PageErrorsFrequencyControl
{
	Random r = new Random();
	
	int framesQuantity;
	int processesQuantity;
	int referencesQuantity;
	int max;
	int min;

	ArrayList<Process> processes;
	ArrayList<Frame> frames;
	ArrayList<Integer> assignment = new ArrayList<Integer>();
	
	public PageErrorsFrequencyControl(int framesQuantity, int processesQuantity,
			int referencesQuantity, int max, int min, ArrayList<Process> processes, ArrayList<Frame> frames)
	{
	
		this.framesQuantity = framesQuantity;
		this.processesQuantity = processesQuantity;
		this.referencesQuantity = referencesQuantity;
		this.max = max;
		this.min = min;
	
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
	
	public void LRU()
	{
		int sum = 0;
		for(int i = 0; i < processesQuantity; i++)
		{
			int startingAssignment = 10;
			int bit1 = 0;
			int bit2 = 0;
			int pageErrors = 0;
			boolean isDone = false;
			for(int j =0; j < referencesQuantity; j++)
			{
				if(processes.get(i).bit1 >= max)
				{
					startingAssignment++;
					bit1 = 0;
					Process p = new Process(processes.get(i).pageQuantity, processes.get(i).references, bit1, bit2);
					processes.set(i, p);
					Frame f = new Frame(0, 0);
					frames.set(startingAssignment, f);
				}
				if(processes.get(i).bit2 <= min)
				{
					startingAssignment--;
					bit2 = 0;
					Process p = new Process(processes.get(i).pageQuantity, processes.get(i).references, bit1, bit2);
					processes.set(i, p);
					Frame f = new Frame(0, 0);
					frames.set(startingAssignment+1, f);
				}
				for(int l = 0; l < startingAssignment && !isDone; l++)
				{
					if(getProcess(i).getReference(j) == frames.get(l).value)
					{
						bit2--;
						Process p = new Process(processes.get(i).pageQuantity, processes.get(i).references, bit1, bit2);
						processes.set(i, p);
						Frame f = new Frame(getProcess(i).getReference(j), 0);
						frames.set(l, f);
						for(int k = 0; k < startingAssignment; k++)
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
					if(frames.get(l).value == 0)
					{
						bit1++;
						Process p = new Process(processes.get(i).pageQuantity, processes.get(i).references, bit1, bit2);
						processes.set(i, p);
						Frame f2 = new Frame(getProcess(i).getReference(j), 0);
						frames.set(l, f2);
						pageErrors++;
						isDone = true;
					}
				}
				if(!isDone)
				{
					int zp3 = 0;
					int index = 0;
					for(int m = 0; m < startingAssignment; m++)
					{
						if(frames.get(m).bit > zp3)
						{
							zp3 = frames.get(m).bit;
							index = m;
						}
					}
					bit1++;
					Process p = new Process(processes.get(i).pageQuantity, processes.get(i).references, bit1, bit2);
					processes.set(i, p);
					Frame f3 = new Frame(getProcess(i).getReference(j), 0);
					frames.set(index, f3);
					pageErrors++;
				}
				isDone = false;
			}
			System.out.println("Process number: "+(i+1)+",  \tpages: "+getProcess(i).pageQuantity+",  \tpage errors: "+pageErrors);
			sum += pageErrors;
			clear();
		}
		System.out.println("Total errors: "+sum);
		assignment.clear();
	}
}
