package so4_rep;

import java.util.ArrayList;
import java.util.Random;

public class AreaModel
{
	Random r = new Random();
	
	int framesQuantity;
	int processesQuantity;
	int referencesQuantity;
	int workingSetSize;

	ArrayList<Process> processes;
	ArrayList<Frame> frames;
	ArrayList<Integer> assignment = new ArrayList<Integer>();
	
	public AreaModel(int framesQuantity, int processesQuantity,
			int referencesQuantity, int workingSetSize, ArrayList<Process> processes, ArrayList<Frame> frames)
	{
	
		this.framesQuantity = framesQuantity;
		this.processesQuantity = processesQuantity;
		this.referencesQuantity = referencesQuantity;
		this.workingSetSize = workingSetSize;
	
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
			int pageErrors = 0;
			boolean isDone = false;
			ArrayList<Integer> ws = new ArrayList<Integer>();
			ArrayList<Integer> wsQuantity = new ArrayList<Integer>();
			int assignmentQuantity = referencesQuantity/workingSetSize;
			int assign1 = 0;
			int assign2 = workingSetSize;
			for(int howManyAssignments = 0; howManyAssignments < assignmentQuantity; howManyAssignments++)
			{
				for(int j = assign1; j < assign2; j++)
				{
					if(ws.size() == 0)
					{
						ws.add(getProcess(i).getReference(j));
					}
					else
					{
						boolean isKnown = false;
						for(int k = 0; k < ws.size(); k++)
						{
							if(ws.get(k) == getProcess(i).getReference(j))
							{
								isKnown = true;
								break;
							}
						}
						if(isKnown == false)
						{
							ws.add(getProcess(i).getReference(j));
						}
					}
				}
				wsQuantity.add(ws.size());
				ws.clear();
				assign1 += workingSetSize;
				assign2 += workingSetSize;
			}
			int hp1 = 0;
			int hp2 = 0;
			assign1 = 0;
			assign2 = wsQuantity.get(hp2);
			for(int j = 0; j < referencesQuantity; j++)
			{
				if(hp1 > workingSetSize)
				{
					hp1 = 0;
					hp2++;
					assign1 = wsQuantity.get(hp2);
				}
				for(int p = 0; p < assign1 && !isDone; p++)
				{
					if(getProcess(i).getReference(j) == frames.get(p).value)
					{
						Frame f = new Frame(getProcess(i).getReference(j), 0);
						frames.set(p, f);
						for(int k = 0; k < assign2; k++)
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
					if(frames.get(p).value == 0)
					{
						Frame f2 = new Frame(getProcess(i).getReference(j), 0);
						frames.set(p, f2);
						pageErrors++;
						isDone = true;
					}
				}
				if(!isDone)
				{
					int zp3 = 0;
					int index = 0;
					for(int l = 0; l < assign2; l++)
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
				hp1++;
			}
			System.out.println("Process number: "+(i+1)+",  \tpages: "+getProcess(i).pageQuantity+",  \tpage errors: "+pageErrors);
			sum += pageErrors;
			clear();
		}
		System.out.println("Total errors: "+sum);
		assignment.clear();
	}
}
