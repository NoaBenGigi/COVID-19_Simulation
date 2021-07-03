package Location;


public class Location {
	private Point position;
	private Size size;
    public Location(Point pos, Size s){
        position = pos;
        size = s;
    }
    public Location(Location loc) {
    	this.position = loc.position;
    	this.size = loc.size;
    }
    public Point getPosition(){
        return position;
    }
    public Size getSize(){
        return size;
    }
    public String toString() {
    	return "Location ("+getPosition().toString() + "," + getSize().toString()+")";
    }
}
