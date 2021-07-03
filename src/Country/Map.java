package Country;
import java.util.*;
import java.util.concurrent.CyclicBarrier;

import Population.Person;
import Population.Sick;
import Simulation.Clock;
import Simulation.Main;
import UI.MainWindow;
import UI.SaveLogFile;
import Virus.*;

public class Map implements Iterable<Settlement>{//This class described the map
	private boolean STOP_simulation = false;
	private boolean PLAY_simulation = false;
	private boolean PAUSE_simulation = true;
	private CyclicBarrier cyclicBarrier;
	private List<Settlement> settlements = new ArrayList<Settlement>();
	//private SaveLogFile logFile1=null;
	public Map(List<Settlement> settlements) {//constructor
		this.settlements=settlements;
		for (int i=0;i<settlements.size();i++)
			this.settlements.get(i).setMap(this);
	}
	public CyclicBarrier getCyclicBarrier() {
		return cyclicBarrier;
	}
	public void setCyclicBarrier(CyclicBarrier cyclicBarrier) {
		this.cyclicBarrier = cyclicBarrier;
	}

	public boolean isPLAY_simulation() {
		return PLAY_simulation;
	}
	public boolean isSTOP_simulation() {
		return STOP_simulation;
	}
	public boolean isPAUSE_simulation() { return PAUSE_simulation; }

	/*public void setLogFile(SaveLogFile s){
		this.logFile1 =s ;
	}
	//public SaveLogFile GetLogFile(){
		return this.logFile1;
	}*/

	public void setPLAY_simulation(boolean PLAY_simulation) {
		this.PLAY_simulation = PLAY_simulation;
	}
	public void setSTOP_simulation(boolean STOP_simulation) {
		this.STOP_simulation = STOP_simulation;
	}
	public void setPAUSE_simulation(boolean PAUSE_simulation) { this.PAUSE_simulation = PAUSE_simulation; }
	/*public Map(Settlement[] settlements) {//constructor
		this.settlements=new Settlement[settlements.length];
		for(int i=0; i < this.settlements.length; i++){
			this.settlements[i]=settlements[i];
		}
	}*/
	public void spawn_all(){
		Thread[] pool = new Thread[settlements.size()];
		for (int i=0; i < this.settlements.size(); i++){
			pool[i] = new Thread(settlements.get(i));
			pool[i].start();
		}
	}
	public List<Settlement> GetSettlement() {return this.settlements;}
	public void printMap() {
		for(int i=0;i< this.settlements.size();i++)
			System.out.println(this.settlements.get(i).toString());
	}
	public void ResetMap() {//level 2 in main
		int size_of_new_settlement; //size of the new settlement(about to change in each iteration)
		Person person = null;
		// virus0= ChineseVariant
		// virus1= BritishVariant
		// virus2= SouthAfricanVariant
		Random rand1 = new Random();
		int randomVirus;
		for (Settlement settlement : this) {
			synchronized (settlement) {
				settlement.SetMaxPeople((int) (1.3 * settlement.GetPeople().size()));
				size_of_new_settlement = settlement.GetPeople().size();
				for (int j = 0; j < (int) (0.01 * size_of_new_settlement); j++) {
					randomVirus = rand1.nextInt(3);
					if (!(settlement.GetPeople().get(j) instanceof Sick)) {
						person = settlement.GetPeople().get(j);
						settlement.GetPeople().remove(person);//switch between the old person to the new sick person.
						if (randomVirus == 0)
							person = settlement.GetPeople().get(j).contagion(new ChineseVariant());
						if (randomVirus == 1)
							person = settlement.GetPeople().get(j).contagion(new BritishVariant());
						if (randomVirus == 2)
							person = settlement.GetPeople().get(j).contagion(new SouthAfricanVariant());

						settlement.GetSickPeopleList().add(person);
					}
				}
				settlement.calculateRamzorGrade();//update ramzor color for each settlement
				//the value we sent to the set returns from method calaculateRamzorGrade.
			}
		}
	}
	public boolean SettlementByname(String settlementname){
		for (Settlement settlement : this) {
			if (settlement.GetName().equals(settlementname))
				return true;
		}
		return false;
	}
	public int SettlementByIndex(String settlementname){
		for (int i=0; i<settlements.size();i++)
			if(settlements.get(i).GetName().equals(settlementname))
				return i;
		return -1;
	}
	public Settlement at(int index){
		if(index< settlements.size())
			return settlements.get(index);
		return null;
	}
	public void simulation() {//Third level in the simulation
		Person p;//reference 
		Person new_sick;
		boolean check=false;
		Random rand1= new Random();
		int index;
		final int max_contagions=6;
		List<Person> people=new ArrayList<Person>();
		for(int i =0 ; i<5 ; i++) {//5 Simulations
			for (Settlement settlement : this) {
				for (int k = 0; k < settlement.GetPeople().size(); k++) {
					people.addAll(settlement.GetPeople()); // create a copy of the people list
					p = people.get(k);
					if (p instanceof Sick) {
						for (int c = 0; c < max_contagions; c++) {
							index = rand1.nextInt(settlement.GetPeople().size());
							if (!(settlement.GetPeople().get(index) instanceof Sick)) {
								check = ((Sick) p).GetVirus().tryToContagion(p, settlement.GetPeople().get(index));
								if (check) {
									new_sick = settlement.GetPeople().get(index).contagion(((Sick) p).GetVirus());
									settlement.GetPeople().remove(index);
									settlement.GetPeople().add(new_sick);
								}
							}
						}
					}
					Clock.nextTick();
					settlement.SetRamzorColor(settlement.calculateRamzorGrade());
				}
			}
		}
	}

	@Override
	public Iterator<Settlement> iterator() {
		return settlements.iterator();
	}
}