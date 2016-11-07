package ch.zhaw.init.walj.projectmanagement.util.dbclasses;

public class Weight {

	private int id;
	private int taskIDFS;
	private int month;
	private double weight;
	
	public Weight (int id, int taskIDFS, int month, double weight){
		this.id = id;
		this.taskIDFS = taskIDFS;
		this.month = month;
		this.weight = weight;
	}

	public int getID(){
		return id;
	}
	
	public int getTaskIDFS(){
		return taskIDFS;
	}
	
	public int getMonth(){
		return month;
	}
	
	public double getWeight(){
		return weight;
	}
}
