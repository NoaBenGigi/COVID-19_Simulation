package Virus;
import Population.Person;
import Population.Sick;

public interface IVirus {
	public double contagionProbability(Person p);
	public boolean tryToContagion(Person p1, Person p2);
	public boolean tryToKill(Sick p);
	public boolean[] getMutations();
	public String getType();
	public IVirus[] getMutations_types();
	public boolean isEqual(IVirus virus);
}
