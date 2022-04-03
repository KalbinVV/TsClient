package org.kalbinvv.tsclient;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javafx.application.Platform;

public class UpdateThread extends Thread{
	
	private Updateable updateable;
	
	public UpdateThread() {
		setUpdateable(new EmptyUpdateable());
	}
	
	@Override
    public void run() {
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
	    Runnable loop = new Runnable() {
	        public void run() {
	             Platform.runLater(new Runnable() {
	                 @Override 
	                 public void run() {
	                	 updateable.update();
	                 }
	             });
	        }
	    };
	    executor.scheduleAtFixedRate(loop, 0, 5, TimeUnit.SECONDS); //Every 5 seconds
    }

	public void setUpdateable(Updateable updateable) {
		this.updateable = updateable;
	}
	
}
