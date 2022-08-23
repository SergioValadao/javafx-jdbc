package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.VendedorDao;
import model.entities.Vendedor;

public class VendedorService {
	
	VendedorDao Dao = DaoFactory.createVendedorDao();
	
	public List<Vendedor> findALl(){
		return Dao.findAll();
	}
	
	public void SalvarVendedor(Vendedor obj) {
		if(obj.getId() == null) {
			Dao.insert(obj);	
		}else {
			Dao.update(obj);
		}
	}	
	public void ExcluirVendedor(Vendedor obj) {
		if(obj.getId() != null) {
			Dao.deleteById(obj.getId());
		}
	}
}
