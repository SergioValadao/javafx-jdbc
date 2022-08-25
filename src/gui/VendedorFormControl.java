package gui;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import connection.DbException;
import gui.listenner.DataChangeListenner;
import gui.util.Alertas;
import gui.util.TxtRestricao;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.Departamento;
import model.entities.Vendedor;
import model.exceptions.ValidationException;
import model.services.DepartamentoService;
import model.services.VendedorService;

public class VendedorFormControl implements Initializable {

	private Vendedor vendedor;
	private VendedorService service;
	private DepartamentoService departamentoService;
	private List<DataChangeListenner> Changelistenner = new ArrayList<>();

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtVendedor;

	@FXML
	private TextField txtEmail;

	@FXML
	private DatePicker dpkAniversario;

	@FXML
	private TextField txtSalario;

	@FXML
	private ComboBox<Departamento> cbxDepartamento;

	@FXML
	private Button btCancelar;

	@FXML
	private Button btSalvar;

	@FXML
	private Label lblErro;

	private ObservableList<Departamento> obsList;

	public void subdataChangeList(DataChangeListenner listener) {
		Changelistenner.add(listener);
	}

	public void setVendedor(Vendedor vendedor) {
		this.vendedor = vendedor;
	}

	public void setVendedorService(VendedorService service, DepartamentoService depService) {
		this.service = service;
		this.departamentoService = depService;
	}

	@FXML
	private void onbtCancelarAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	@FXML
	private void onbtSalvarAction(ActionEvent event) {
		try {
			this.vendedor = getFormData();
			this.service.SalvarVendedor(vendedor);
			notifyDataChangeListenner();
			Utils.currentStage(event).close();
		} catch (ValidationException e) {
			setErrorMessages(e.getErrors());
		} catch (DbException e) {
			Alertas.showAlert("Erro", "Vendedor", e.getMessage(), AlertType.ERROR);
		}
	}

	private void notifyDataChangeListenner() {
		for (DataChangeListenner listenner : Changelistenner) {
			listenner.onDataChangeListenner();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ontxtControleAction();
	}

	@FXML
	private void ontxtControleAction() {
		TxtRestricao.setTextMaxLength(txtVendedor, 70);
		TxtRestricao.setTextMaxLength(txtEmail, 80);
		TxtRestricao.setTextString(txtVendedor);
		TxtRestricao.setTextDouble(txtSalario);
		Utils.formatDatePicker(dpkAniversario, "dd/MM/YYYY");
		initializeComboBoxDepartment();
	}

	public void updateVendedorformControl() {
		if (this.vendedor == null) {
			throw new DbException("Nenhum registro para editar");
		}
		String id = vendedor.getId() == null ? "" : String.valueOf(vendedor.getId());
		txtId.setText(id);
		txtVendedor.setText(vendedor.getName());
		txtEmail.setText(vendedor.getEmail());
		Locale.setDefault(Locale.US);
		txtSalario.setText(String.format("%.2f", vendedor.getBaseSalary()));
		if (vendedor.getBirthDate() != null) {
			dpkAniversario.setValue(LocalDate.ofInstant(vendedor.getBirthDate().toInstant(), ZoneId.systemDefault()));
		}
		if(vendedor.getDepartment() != null) {
			cbxDepartamento.setValue(vendedor.getDepartment());;
		}else {
			cbxDepartamento.getSelectionModel().selectFirst();
		}		
	}

	public Vendedor getFormData() {
		Vendedor dep = new Vendedor();
		ValidationException exception = new ValidationException("Nome de vendedor invalido!");
		if (txtVendedor.getText() == null || txtVendedor.getText().trim() == "") {
			exception.addErrors("Vendedor", exception.getMessage());
		}
		dep.setId(Utils.ParseInteger(txtId.getText()));
		dep.setName(txtVendedor.getText());
		if (exception.getErrors().size() > 0) {
			throw exception;
		}
		return dep;
	}

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		if (fields.contains("Vendedor")) {
			lblErro.setText(errors.get("Vendedor"));
		}
	}

	public void loadAssociatObjetcts() {
		List<Departamento> list = departamentoService.findALl();
		obsList = FXCollections.observableArrayList(list);
		cbxDepartamento.setItems(obsList);
	}

	private void initializeComboBoxDepartment() {
		Callback<ListView<Departamento>, ListCell<Departamento>> factory = lv -> new ListCell<Departamento>() {
			@Override
			protected void updateItem(Departamento item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getName());
			}
		};
		cbxDepartamento.setCellFactory(factory);
		cbxDepartamento.setButtonCell(factory.call(null));
	}
}
