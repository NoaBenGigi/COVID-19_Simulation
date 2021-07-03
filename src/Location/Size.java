package Location;

public class Size {
    private int width;
    private int height;

    public Size(int w, int h){
        width=w;
        height=h;
    }

    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
    public String toString() {
    	return "Size- W :"+getWidth()+" H: "+getHeight(); 
    }
}



