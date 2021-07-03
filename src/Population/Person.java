package Population;
import Country.Settlement;
import Simulation.Clock;
import Location.Point;
import Virus.IVirus;
public abstract class Person {
	
	private int age;
	private Point location;
	private Settlement settlement;

	public Person(int age, Point location, Settlement settlement) {
		this.age = age;
		this.location= location;
		this.settlement= settlement;	
    }
	public int GetAge() {return age;}
	public Point Getlocation() {return location;}
	public Settlement GetSettlement() {return settlement;}
	public void SetSettlement(Settlement settlement){
		this.settlement= settlement;
	}
	public String toString(){
		
		return  "Person: Age:" + age ;
		
	}
	public double contagionProbability(){return 1;}
	public Sick contagion(IVirus virus) {
		if(this instanceof Sick) {
			return (Sick)this;
		}
		return new Sick(this.age, this.location, this.settlement,Clock.now(), virus);
	}
	public double GetDistance(Point p1, Point p2) {
		return Math.sqrt(Math.pow(p1.getX()-p2.getX(), 2)+ Math.pow(p1.getY()-p2.getY(), 2));
	}
	public boolean equals(Object o) {
		if (!(o instanceof Person))
			return false;
		Person p = (Person)o;
		return this.age == p.age && this.location == p.location && this.settlement== p.settlement; //true
	}



}
