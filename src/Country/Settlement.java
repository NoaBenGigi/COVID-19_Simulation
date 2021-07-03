package Country;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.atomic.AtomicInteger;

import Population.*;
import Location.Location;
import Location.Point;
import UI.Caretaker_logFile;
import UI.MainWindow;
import UI.SaveLogFile;
import Virus.*;

public class Settlement implements Runnable {
	private String name;
	private Location location;
	private List<Person> people;
	private RamzorColor ramzorcolor;
	private int max_people_num;
	private int vaccine_doses_num;
	private Settlement[] connected_settlements;
	private List<Settlement> neighbors = new ArrayList<Settlement>();
	private List<Person> sick_people = new ArrayList<Person>();
	private List<Person> healthy_people = new ArrayList<Person>();
	private List<Vaccinated> vaccinated_people = new ArrayList<Vaccinated>();
	private List<Convalescent> convalescent_people = new ArrayList<Convalescent>();
	AtomicInteger percent=new AtomicInteger();
	private int diedNum = 0;
	private Map map;
	private SaveLogFile saveTo_LogFile=null;
	public Settlement(String name, Location location, List<Person> people){
		this.name = name;
		this.location = location;
		this.people= new ArrayList<Person>(people);
		healthy_people= new ArrayList<Person>(people);
		this.ramzorcolor= RamzorColor.Green;
	}
	public void SetMaxPeople(int max_people_num){this.max_people_num= max_people_num;}
	public void setMap(Map map) { this.map = map; }
	public String GetName() {
		return this.name;
	}
	public int GetSicknum(){return this.sick_people.size();}
	public Location GetLocation() {
		return this.location;
	}
	public List<Person> GetPeople() {
		return this.people;
	}
	public int GetDiedNum() {
		return diedNum;
	}
	public RamzorColor GetRamzorColor() {
		return this.ramzorcolor;
	}
	public int GetMaxSickPeople() {
		return this.max_people_num;
	}
	public int GetNumOfVaccineDoses() {
		return this.vaccine_doses_num;
	}
	public void Remove1VaccineDose() { //reduce number of vaccine doses
		this.vaccine_doses_num -= 1;
	}
	public int GetMaxPeople() {
		return this.max_people_num;
	}
	public void SetNeighbors(Settlement settlement) { this.neighbors.add(settlement); }
	public void RemoveNeihbor(Settlement settlement1) {
		this.neighbors.remove(settlement1);
	}
	public List<Settlement> GetNeighbors() {
		return this.neighbors;
	}
	public Settlement[] GetConnectedSettlements() {
		return this.connected_settlements;
	}
	public List<Person> GetHealthyPeopleList() {
		return this.healthy_people;
	}
	public List<Person> GetSickPeopleList() {
		return this.sick_people;
	}
	public void SetVaccineDosesNum(int vaccine_doses_num) { this.vaccine_doses_num = vaccine_doses_num; }
	public void SetRamzorColor(RamzorColor nemRamzorColor) {
		this.ramzorcolor = nemRamzorColor;
	}
	public List<Convalescent> GetConvalescent_people() { return convalescent_people; }

	public String toString() {
		return "Name: " + GetName() + "," + GetLocation() + ",Ramzor Color:" + GetRamzorColor();
	}
	public RamzorColor calculateRamzorGrade() {
		return ramzorcolor;
	}
	public double contagiousPercent() {
		double contagiousPer = sick_people.size() / (double) (people.size() + sick_people.size());
		RamzorColor newColor = RamzorColor.GetColor(contagiousPer);
		this.ramzorcolor = newColor;
		return contagiousPer;
	}
	public boolean addPerson(Person p) {
		//if(people.size() < )
		this.people.add(p);
		return true;
	}

	public boolean transferPerson(Person p, Settlement s) { //s - the new settlement, p - the person who transfer
		double multiplyStatistic;
		Settlement firstSettlement,secSettlement;
		multiplyStatistic = s.GetRamzorColor().GetStatistic() * this.GetRamzorColor().GetStatistic();
		if (s.GetPeople().size() < s.max_people_num || multiplyStatistic > Math.random()) {
			int hash_this=System.identityHashCode(this);
			int hash_neighbor= System.identityHashCode(s);
			if(hash_this<hash_neighbor){
				firstSettlement = s;
				secSettlement = this;
			}
			else{
				firstSettlement = this;
				secSettlement = s;
			}
			synchronized (firstSettlement){
				synchronized (secSettlement){
					if (p instanceof Sick) {
						this.sick_people.remove(p);
						s.sick_people.add(p);
						//p.SetSettlement(s);
					} else if (p instanceof Healthy || p instanceof Vaccinated) {
						this.people.remove(p);
						s.people.add(p);
						//p.SetSettlement(s);
					}
					return true;
				}
			}
		} else {
			return false;
		}
	}

	public Point randomLocation() {
		Random rand = new Random();
		int minX = location.getPosition().getX();
		int maxX = location.getSize().getWidth() + minX;
		int x = rand.nextInt(maxX - minX + 1) + minX;
		Random rand1 = new Random();
		int minY = location.getPosition().getY();
		int maxY = location.getSize().getHeight() + minY;
		int y = rand1.nextInt(maxY - minY + 1) + minY;
		return new Point(x, y);
	}

	public synchronized void killSucceeded() {
		diedNum++;
	}
	public synchronized void MakeSick() {
		Person temp;
		IVirus[] random = {new ChineseVariant(), new BritishVariant(), new SouthAfricanVariant()};
		int randomVariant;
		//int non_sick_people=this.GetPeople().size() - this.GetDiedNum();
		double one_percent_from_settlement =  0.01 * (people.size()) ;
		for (int i = 0; i < (int) one_percent_from_settlement; i++) {
			randomVariant = ((int) (Math.random() * 3));
			temp = people.get(i).contagion(random[randomVariant]);
			sick_people.add(temp);
			people.remove(i);
			//non_sick_people=this.GetPeople().size() - this.GetDiedNum();
		}
	}
	public synchronized void contagionTheSettlement() {
		final int TRY_TO_CONTAGION = 3;
		int randomPerson;
		int random_variant;
		IVirus virus;
		IVirus[] randomViruses = {new ChineseVariant(), new BritishVariant(), new SouthAfricanVariant()};
		for (int i = 0; i < (int) (GetSickPeopleList().size() * 0.2); i++) {
			random_variant = ((int) (Math.random() * 3));
			virus =((Sick) this.GetSickPeopleList().get(i)).GetVirus();
			for (int k = 0; k < TRY_TO_CONTAGION && (this.GetPeople().size() > 3); k++) {
				randomPerson = ((int) ((Math.random()) * this.GetPeople().size()));
				//virus=VirusManagement.randomVariantToContagion(virus);
				if (virus.tryToContagion(((Sick) this.GetSickPeopleList().get(i)), this.GetPeople().get(randomPerson))) {
					Person newSickPeople = this.GetPeople().get(randomPerson).contagion(virus);
					this.GetSickPeopleList().add(newSickPeople);
					this.GetPeople().remove(randomPerson);
					this.contagiousPercent();
				}

			}
		}
	}
	public synchronized void convalescenceThePopulation() {
		//This function change the sick people who pass 25 days from contagious time
		Person convalescentPerson;
		for (int j = 0; j < this.GetSickPeopleList().size(); j++) {
			if (((Sick) this.GetSickPeopleList().get(j)).getFromContagionTime() > 25) {
				convalescentPerson = ((Sick) this.GetSickPeopleList().get(j)).recover();
				this.GetPeople().add(convalescentPerson);
				this.GetSickPeopleList().remove(convalescentPerson);
				this.calculateRamzorGrade();
			}
		}
	}
	public void tryToMovePeople() {
		Settlement settlement2; //reference to the new settlement
		int randomNeighbor;
		int randomPerson;
		if (this.GetNeighbors().size() > 0) {//check if there is any neighbors?
			for (int j = 0; j < (int) (0.03 * this.GetPeople().size()); j++) {
				if (this.GetPeople().size() < this.GetMaxPeople()) {
					randomNeighbor = ((int) ((Math.random()) * this.GetNeighbors().size()));
					randomPerson = ((int) ((Math.random()) * this.GetPeople().size()));
					settlement2 = this.GetNeighbors().get(randomNeighbor);
					//we send to the function the person we want to transfer and the new settlement
					if (this.transferPerson(this.GetPeople().get(randomPerson), settlement2)) {
						this.contagiousPercent();
					}
				}
			}
			for (int j = 0; j < (int) (0.03 * this.GetSickPeopleList().size()); j++) {
				if (this.GetSickPeopleList().size() < this.GetMaxPeople()) {
					randomNeighbor = ((int) ((Math.random()) * this.GetNeighbors().size()));
					randomPerson = ((int) ((Math.random()) * this.GetSickPeopleList().size()));
					settlement2 = this.GetNeighbors().get(randomNeighbor);
					//we send to the function the person we want to transfer and the new settlement
					if (this.transferPerson(this.GetPeople().get(randomPerson), settlement2))
						this.contagiousPercent();
				}
			}
		}
	}
	public synchronized void tryToKillInSettlement(){
		for (int i=0 ; i< sick_people.size(); i++) {
			if(((Sick)sick_people.get(i)).GetVirus().tryToKill((Sick)this.sick_people.get(i))){
				killSucceeded();// died number ++
				sick_people.remove(i);
				//removeFromListPeople((Sick)sick_people.get(i));
			}
		}
		//Caretaker_logFile caretakerLogFile=new Caretaker_logFile();
		//if(caretakerLogFile.getCurr_file() != null){
		MainWindow.getInstance();
		if(MainWindow.getF()!= null) {
			double diedPercent = (double) this.diedNum / people.size() * 100.0;
			long temp = (long) diedPercent;
			if (temp > this.percent.get()) {
				this.percent.getAndIncrement();
				MainWindow.getF().writeToFile(this);
			}
		}
		//}
		/*if(map.GetLogFile() != null){
			double diedPercent = (double)this.diedNum/people.size()*100.0;
			long temp = (long) diedPercent;
			if(temp > this.percent.get()){
				this.percent.getAndIncrement();
				map.GetLogFile().writetoFile(this);
			}
		}*/
	}
	public synchronized void tryToVaccinate() {
		Person p;
		for (int i = 0; i < this.GetPeople().size(); i++) {//all the people that Not sick in the settlement
			if (this.GetNumOfVaccineDoses() > 0) {
				//check that there is people in the list, also check that there is a vaccine doses.
				if (this.GetPeople().get(i) instanceof Healthy) {// we need to check that the person doesn't get vaccinated before
					p = this.people.get(i);
					people.remove(i);
					people.add(((Healthy) p).vaccinate());
					this.Remove1VaccineDose();//update num of vaccine doses
				}
			}
		}
	}
	@Override
	public void run() {
		while (!map.isSTOP_simulation()) {
			synchronized (map) {
				while (map.isPAUSE_simulation()) {
					try {
						map.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			while (map.isPLAY_simulation()) {
				contagionTheSettlement();
				convalescenceThePopulation();
				tryToVaccinate();
				tryToKillInSettlement();
				tryToMovePeople();
				try {
					map.getCyclicBarrier().await();
				} catch (InterruptedException | BrokenBarrierException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
