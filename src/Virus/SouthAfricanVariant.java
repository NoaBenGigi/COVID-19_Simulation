package Virus;

import Population.Person;
import Population.Sick;
import Simulation.Clock;

public class SouthAfricanVariant implements IVirus{
	private static boolean[] mutations = new boolean[] {true,false,false};//for Mutation boolean window
	private static IVirus[] mutations_types = new IVirus[] {new SouthAfricanVariant(),new ChineseVariant(),new BritishVariant()};//to contagion another virus
	private final int minContigous=5;
	//private ArrayList<IVirus> arrayvarius= new ArrayList<IVirus>();
	public SouthAfricanVariant() {}
	public double contagionProbability(Person p){
		if(p.GetAge()>0 && p.GetAge()<=18)
			return 0.6*p.contagionProbability();
		else{//if p.GetAge()> 18
			return 0.5*p.contagionProbability();
		}
	}
	//public static ArrayList<IVirus> GetArrayVirus(){return arrayvarius;}
	//public void removeVaruent(IVirus virus) {
		//arrayvarius.remove(virus);
	//}
	//public void addVarient(IVirus virus){
	//	arrayvarius.add(virus);
	//}
	public boolean tryToContagion(Person p1, Person p2){
		if(p2 instanceof Sick)
			return false; // can't get sick because he is already sick.
		else
		{
			Sick s1= (Sick)p1;
			if(Clock.TicksPerDay((int)s1.GetContagiousTime())< minContigous){
				return false;
			}
			double distance= p1.GetDistance(p1.Getlocation(), p2.Getlocation());
			double cp_new=contagionProbability(p2)* Math.min(1, 0.14*Math.pow(Math.E, 2-(0.25*distance)));
			return cp_new >= Math.random();
		}
	}
	public boolean tryToKill(Sick p) {
		double x;
		long t= Clock.now()- p.GetContagiousTime();
		if(p.GetAge()>0 && p.GetAge()<=18) {
			x= 0.05;
		}
		else {
			x= 0.08;
		}
		double result= x- (0.01*x)*(Math.pow((t-15),2));
		double max = Math.max(0, result);
	    double rand= Math.random();
		return max >= rand;//die.
	
	}
	public static boolean[] getMutations1() {
		return mutations;
	}
	public  boolean[] getMutations() {
		return mutations;
	}

	public IVirus[] getMutations_types() {
		return mutations_types;
	}
	public static void setMutation(int val,boolean value) //to set mutation
	{
		mutations[val]=value;

	}
	public String getType() {
		return "SouthAfrican Variant";
	}
	public boolean isEqual(IVirus virus){
		return getType()==virus.getType();
	}
		
}

