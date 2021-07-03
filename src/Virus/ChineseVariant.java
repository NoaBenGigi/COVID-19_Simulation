package Virus;

import Population.Person;
import Population.Sick;
import Simulation.Clock;

import java.util.ArrayList;

public class ChineseVariant implements IVirus{
	private final int minContigous=5;
	private ArrayList<IVirus> arrayvarius= new ArrayList<IVirus>();
	private static boolean[] mutations = new boolean[] {false,true,false};//for Mutation boolean window
	private static IVirus[] mutations_types = new IVirus[] {new SouthAfricanVariant(),new ChineseVariant(),new BritishVariant()};//to contagion another virus

	public ChineseVariant() {}
	public double contagionProbability(Person p){
		if(p.GetAge()>0 && p.GetAge()<=18)
			return 0.2*p.contagionProbability();
		else{
			if(p.GetAge()>18 && p.GetAge()<=55)
				return 0.5*p.contagionProbability();
			else{ //if(p.GetAge()>55)
				return 0.7*p.contagionProbability();
			}
		}
	}
	public ArrayList<IVirus> GetArrayVirus(){return arrayvarius;}
	public void removeVaruent(IVirus virus) {
		arrayvarius.remove(virus);
	}
	public void addVarient(IVirus virus,boolean val){
		if(val)
			arrayvarius.add(virus);
	}
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
	@Override
	public boolean tryToKill(Sick p) {
		double p_to_die;
		long t = Clock.now()-p.GetContagiousTime();
		if(p.GetAge()<0 && p.GetAge()<=18 ){
			p_to_die=0.001;
		}else{
			if(p.GetAge()<18 && p.GetAge()<=55){
				p_to_die=0.05;
			}else{
				p_to_die=0.1;
			}
		}
		double max = Math.max(0, p_to_die-0.01*p_to_die*Math.pow((t-15),2));
		double p_rand= Math.random();
		return max >= p_rand;
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
	public static void setMutation(int val,boolean value){
		mutations[val]=value;

	}
	public String getType() {
		return "Chinese Variant";
	}
	public boolean isEqual(IVirus virus){
		return getType()==virus.getType();
	}
}
