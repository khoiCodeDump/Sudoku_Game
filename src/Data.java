import javax.swing.JLabel;

public class Data {
	 private boolean transfer;
	 JLabel status;
	 Data(JLabel status){
		 this.status = status;
	 }
	 public synchronized void receive() {
	        while (transfer) {
	            status.setText("Loading...");
	        }
	        transfer = true;
	    }
	 
	public synchronized void send() {
	        
	    transfer = false;
	        
	}
}
