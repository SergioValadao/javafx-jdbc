package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import connection.DbException;
import gui.listenner.DataChangeListenner;
import gui.util.Alertas;
import gui.util.TxtRestricao;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Departamento;
import model.exceptions.ValidationException;
import model.services.DepartamentoService;

public class VendedorFormControl implements Initializable {
	
	private Departamento departamento;
	private DepartamentoService service;
	private List<DataChangeListenner> Changelistenner = new ArrayList<>(); 
	
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
	
	public void subdataChangeList(DataChangeListenner listener) {
		Changelistenner.add(listener);
	}
	
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
		try {
		this.departamento = getFormData();		
		this.service.SalvarDepartamento(departamento);
		notifyDataChangeListenner();
		Utils.currentStage(event).close();
		}catch(ValidationException e) {
			setErrorMessages(e.getErrors());		
		}catch(DbException e) {
			Alertas.showAlert("Erro", "Departamento", e.getMessage(), AlertType.ERROR);
		}
	}
	
	private void notifyDataChangeListenner() {
		for(DataChangeListenner listenner: Changelistenner) {
			listenner.onDataChangeListenner();
		}
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
		ValidationException exception = new ValidationException("Nome de departamento invalido!");
		if(txtDepartamento.getText()==null || txtDepartamento.getText().trim() == "") {
			exception.addErrors("Departamento", exception.getMessage());
		}
		dep.setId(Utils.ParseInteger(txtId.getText()));
		dep.setName(txtDepartamento.getText());
		if(exception.getErrors().size()>0) {
			throw exception;
		}		
		return dep;		
	}
	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		if(fields.contains("Departamento")) {
			lblErro.setText(errors.get("Departamento"));
		}
	}

}
