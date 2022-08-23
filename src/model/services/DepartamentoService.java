package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartamentoDao;
import model.entities.Departamento;

public class DepartamentoService {
	
	DepartamentoDao Dao = DaoFactory.createDepartmentDao();
	
	public List<Departamento> findALl(){
		return Dao.findAll();
	}
	
	public void SalvarDepartamento(Departamento obj) {
		if(obj.getId() == null) {
			Dao.insert(obj);	
		}else {
			Dao.update(obj);
		}
	}	
	public void ExcluirDepartamento(Departamento obj) {
		if(obj.getId() != null) {
			Dao.deleteById(obj.getId());
		}
	}
}
