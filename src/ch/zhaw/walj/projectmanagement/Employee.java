package ch.zhaw.walj.projectmanagement;

public class Employee {
	
	private int id;
	private String firstname;
	private String lastname;
	private String kuerzel;
	private String mail;
	private float wage;
	private int supervisor;
	
	public Employee(int id, String firstname, String lastname, String kuerzel, String mail, float wage, int supervisor){
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.kuerzel = kuerzel;
		this.mail = mail;
		this.wage = wage;
		this.supervisor = supervisor;
		
	}
	
	public int getID(){
		return id;
	}
	
	public String getName(){
		String name = firstname + " " + lastname;
		return name;
	}
	
	public String getKuerzel(){
		return kuerzel;
	}
	
	public String getMail(){
		return mail;
	}
	
	public float getWage(){
		return wage;
	}
	
	public int getSupervisor(){
		return supervisor;
	}
}
