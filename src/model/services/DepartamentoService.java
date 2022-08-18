package model.services;

import java.util.ArrayList;
import java.util.List;

import model.entities.Departamento;

public class DepartamentoService {
	
	public List<Departamento> findALl(){
		List<Departamento> lista = new ArrayList<>();	
		lista.add(new Departamento(1, "Moveis"));
		lista.add(new Departamento(2, "Eletrodomesticos"));
		lista.add(new Departamento(3, "Eletronicos"));
		lista.add(new Departamento(4, "Smartphones"));
		return lista;
	}

}
