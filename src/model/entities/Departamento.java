package model.entities;

import java.io.Serializable;

public class Departamento implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String name;
	
	public Departamento(){		
	}
	
	public Departamento(Integer id, String name) {	
		this.id = id;
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		//return "Departamento [id=" + id + ", name=" + name + "]";
		return name;
	}	

}
