package model.dao;

import connection.ConnectDB;
import model.dao.impl.DepartamentoDaoJDBC;
import model.dao.impl.VendedoDaoJDBC;

public class DaoFactory {

	public static VendedorDao createVendedorDao() {
		return new VendedoDaoJDBC(ConnectDB.DbConnection());
	}
	
	public static DepartamentoDao createDepartmentDao() {
		return new DepartamentoDaoJDBC(ConnectDB.DbConnection());
	}
}
