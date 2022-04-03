package org.kalbinvv.tsclient;


public class UpdateTask{

	private Updateable updateable;
	
	public UpdateTask(Updateable updateable){
		this.updateable = updateable;
	}
	
	public void run() {
		getUpdateable().update();
	}

	public Updateable getUpdateable() {
		return updateable;
	}

	public void setUpdateable(Updateable updateable) {
		this.updateable = updateable;
	}
	
}
