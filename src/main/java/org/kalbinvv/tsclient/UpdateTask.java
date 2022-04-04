package org.kalbinvv.tsclient;


public class UpdateTask{

	private Updateable updateable;
	
	public UpdateTask(Updateable updateable){
		this.updateable = updateable;
	}
	
	public void run() {
		updateable.update();
	}

	public void setUpdateable(Updateable updateable) {
		this.updateable = updateable;
	}
	
}
