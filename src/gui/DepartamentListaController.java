package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import gui.listenner.DataChangeListenner;
import gui.util.Alertas;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Departamento;
import model.services.DepartamentoService;

public class DepartamentListaController implements Initializable, DataChangeListenner {
	
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
	private void onbtnewAction(ActionEvent event){
		Stage currentState = Utils.currentStage(event);
		Departamento obj = new Departamento();
		CreateDialogForm(obj, currentState, "/gui/DepartamentoForm.fxml");		
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
	
	public void CreateDialogForm(Departamento obj, Stage oldstate, String formload) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(formload));		
			Pane painel = loader.load();
			
			DepartamentoFormControl control = loader.getController();			
			control.setDepartamento(obj);			
			control.setDepartamentoService(new DepartamentoService());
			control.subdataChangeList(this);
			control.updateDepartamentoformControl();
			Stage dialog = new Stage();			
			dialog.setTitle("Novo Departamento");
			dialog.setScene(new Scene(painel));
			dialog.setResizable(false);
			dialog.initOwner(oldstate);
			dialog.initModality(Modality.WINDOW_MODAL);
			dialog.showAndWait();
			
		} catch (IOException e) {			
			Alertas.showAlert("IO Exception", "Erro lendo formulario",  e.getMessage(), AlertType.ERROR);
		}
		
	}

	@Override
	public void onDataChangeListenner() {		
		updateTableView();
	}
}
