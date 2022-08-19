package gui;

import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Departamento;
import model.services.DepartamentoService;

public class DepartamentListaController implements Initializable {
	
	private DepartamentoService service;

	@FXML
	private TableView<Departamento> tbvdep;

	@FXML
	private TableColumn<Departamento, Integer> tbvdepId;
	
	@FXML
	private TableColumn<Departamento, String> tbvdepName;
	
	@FXML
	private Button btNew;
	
	private ObservableList<Departamento> obsLista;

	@FXML
	private void onbtnewAction(){
		System.out.println("Apertou novo registro");
	}
	
	public void setDepartamentoService(DepartamentoService service ) {
		this.service = service;
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {		
		initilizeNodes();
		//setDepartamentoService(new DepartamentoService());
		//updateTableView();

	}

	private void initilizeNodes() {
		tbvdepId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tbvdepName.setCellValueFactory(new PropertyValueFactory<>("Name"));
		
		//define o tamanho da tableview para o tamanho da janela????
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tbvdep.prefHeightProperty().bind(stage.heightProperty());		
	}
	
	public void updateTableView() {
		if(service == null) {
			throw new IllegalStateException("Serviço deve ser criado antes.");
		}
		obsLista  = FXCollections.observableArrayList(service.findALl());
		tbvdep.setItems(obsLista);
	}
}
