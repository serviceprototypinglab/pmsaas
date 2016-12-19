package ch.zhaw.init.walj.projectmanagement.util.dbclasses;

/**
 * implementation of the database class Weights
 * @author Janine Walther, ZHAW
 *
 */
public class Weight {

	private int id;
	private int taskIDFS;
	private int month;
	private double weight;
	
	/**
	 * constructor of weight
	 * @param id ID of the weight
	 * @param taskIDFS ID of the task
	 * @param month number of the month
	 * @param weight the weight of the month
	 */
	public Weight (int id, int taskIDFS, int month, double weight){
		this.id = id;
		this.taskIDFS = taskIDFS;
		this.month = month;
		this.weight = weight;
	}

	/**
	 * @return ID of the weight
	 */
	public int getID(){
		return id;
	}

	/**
	 * @return ID of the task
	 */
	public int getTaskIDFS(){
		return taskIDFS;
	}
	
	/**
	 * @return number of the month
	 */
	public int getMonth(){
		return month;
	}

	/**
	 * @return weight of the month
	 */
	public double getWeight(){
		return weight;
	}
}
