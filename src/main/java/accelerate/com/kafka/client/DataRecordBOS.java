package accelerate.com.kafka.client;

public class DataRecordBOS {

    // Sätt din USER här....
	String user = "micke";
    String taskId;
    String myTopicName;
    String myGuess;
    String var1;
    String var2;
    String var3;
    String[] data;

    // Default 
    public DataRecordBOS() {
    }

    // Tex för att svara på en uppgift
    public DataRecordBOS(String taskId, String myGuess) {
    	this.taskId = taskId;
    	this.myGuess = myGuess;
    }

    // Skicka DATA taggen...
    public DataRecordBOS( 
    		String taskId, 
    		String[] dataArray) {
        
    	this.taskId = taskId;
        this.data = dataArray;
    }

    // Used to only send data
    public DataRecordBOS( 
    		String user,
    		String myTopicName, 
    		String[] dataArray) {
        
    	this.user = user;
    	this.myTopicName = myTopicName;
        this.data = dataArray;
    }
    
    // Used for missions when sending in data to the "game engine"
    public DataRecordBOS( 
    		String taskId, 
    		String var1, 
    		String var2, 
    		String var3, 
    		String[] dataArray) {
        
    	this.taskId = taskId;
    	this.var1 = var1;
        this.var2 = var2;
        this.var3 = var3;
        this.data = dataArray;
    }

    public String getuser() {
        return user;
    }
    public String getmyTopicName() {
        return myTopicName;
    }

    public String gettaskId() {
        return taskId;
    }
    public String getmyGuess() {
        return myGuess;
    }
    public String getvar1() {
        return var1;
    }
    public String getvar2() {
        return var2;
    }
    public String getvar3() {
        return var3;
    }

    public String[] getData() {
        return data;
    }
    
    public String toString() {
    	return new com.google.gson.Gson().toJson(this);
    }
  
    
}
