package so4_rep;

import java.util.ArrayList;
import java.util.Random;

public class Test
{
	Random r = new Random();
	int framesQuantity = 100;
	int processesQuantity = 10;
	int referencesQuantity = 1000;
	int max = 1;
	int min = -16;
	int workingSetSize = 50;
	
	ArrayList<Process> processes = new ArrayList<Process>();
	ArrayList<Frame> frames = new ArrayList<Frame>();
	
	public void addProcesses()
	{
		for(int i = 0; i < processesQuantity; i++)
		{
			int pagesQuantity = r.nextInt(10) + 5;
			ArrayList<Integer> references = new ArrayList<Integer>();
			for(int j = 0; j < referencesQuantity; j++)
			{
				references.add(r.nextInt(pagesQuantity) + 1);
			}
			processes.add(new Process(pagesQuantity, references, 0, 0));
		}
	}
	
	public void initializeMemory()
	{
		Frame f = new Frame(0, 0);
		for(int i = 0; i < framesQuantity; i++)
		{
			frames.add(f);
		}
	}
	
	public static void main(String[] args)
	{
		Test test = new Test();
		test.initializeMemory();
		test.addProcesses();
		
		
		EqualAssignment ea = new EqualAssignment(test.framesQuantity, test.processesQuantity,
				test.referencesQuantity, test.processes, test.frames);
		System.out.println("Equal assignment: ");
		ea.equalAssignment();
		ea.LRU();
		
		ProportionalAssignment pa = new ProportionalAssignment(test.framesQuantity, test.processesQuantity,
				test.referencesQuantity, test.processes, test.frames);
		System.out.println("Proportional assignment: ");
		pa.proportionalAssignment();
		pa.LRU();
		
		PageErrorsFrequencyControl pefc = new PageErrorsFrequencyControl(test.framesQuantity, test.processesQuantity,
				test.referencesQuantity, test.max, test.min, test.processes, test.frames);
		System.out.println("Page errors frequency control: ");
		pefc.LRU();
		
		AreaModel am = new AreaModel(test.framesQuantity, test.processesQuantity,
				test.referencesQuantity, test.workingSetSize, test.processes, test.frames);
		System.out.println("Area model: ");
		am.LRU();
	}
}
