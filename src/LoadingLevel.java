
public class LoadingLevel implements Runnable {
	  private Data load;
	  
	    // standard constructors
	  LoadingLevel(Data load){
		  this.load = load;
	  }
	    public void run() {
	       load.receive();
	    }
}
