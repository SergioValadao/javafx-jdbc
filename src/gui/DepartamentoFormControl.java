package gui;

import java.net.URL;
import java.util.ResourceBundle;

import connection.DbException;
import gui.util.TxtRestricao;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Departamento;
import model.services.DepartamentoService;

public class DepartamentoFormControl implements Initializable {
	
	private Departamento departamento;
	private DepartamentoService service;
	
	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtDepartamento;
	
	@FXML
	private Button btCancelar;
	
	@FXML
	private Button btSalvar;
	
	@FXML
	private Label lblErro;
	
	public void setDepartamento(Departamento departamento ) {
		this.departamento = departamento;
	}
	
	public void setDepartamentoService(DepartamentoService service ) {		 
		this.service = service;
	}
	
	@FXML
	private void onbtCancelarAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}
	
	@FXML
	private void onbtSalvarAction(ActionEvent event) {
		this.departamento = getFormData();		
		this.service.SalvarDepartamento(departamento);
		Utils.currentStage(event).close();
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {		
		// TODO Auto-generated method stub		
	}
	
	@FXML
	private void ontxtControleAction() {
		TxtRestricao.setTextMaxLength(txtDepartamento, 30);
		TxtRestricao.setTextString(txtDepartamento);
	}
	
	public void updateDepartamentoformControl() {
		if(this.departamento == null) {
			throw new DbException("Nenhum registro para editar");
		}
		String id = departamento.getId()==null?"":String.valueOf(departamento.getId());
		txtId.setText(id);
		txtDepartamento.setText(departamento.getName());
	} 
	
	public Departamento getFormData() {
		Departamento dep = new Departamento();
		dep.setId(Utils.ParseInteger(txtId.getText()));
		dep.setName(txtDepartamento.getText());
		return dep;		
	}

}
