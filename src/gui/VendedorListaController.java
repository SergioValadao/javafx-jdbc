package gui;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import gui.listenner.DataChangeListenner;
import gui.util.Alertas;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Vendedor;
import model.services.DepartamentoService;
import model.services.VendedorService;

public class VendedorListaController implements Initializable, DataChangeListenner {

	private VendedorService service;

	@FXML
	private TableView<Vendedor> tbvVendedor;
	@FXML
	private TableColumn<Vendedor, Integer> tbvId;
	@FXML
	private TableColumn<Vendedor, String> tbvNome;
	@FXML
	private TableColumn<Vendedor, String> tbvEmail;
	@FXML
	private TableColumn<Vendedor, Date > tbvAniversario;
	@FXML
	private TableColumn<Vendedor, Double> tbvSalario;
	
	@FXML
	private TableColumn<Vendedor, Vendedor> tbvEdit;	
	@FXML
	private TableColumn<Vendedor, Vendedor> tbvExcluir;
	
	@FXML
	private Button btNew;

	private ObservableList<Vendedor> obsLista;

	@FXML
	private void onbtnewAction(ActionEvent event) {
		Stage currentState = Utils.currentStage(event);
		Vendedor obj = new Vendedor();
		CreateDialogForm(obj, currentState, "/gui/VendedorForm.fxml");
	}

	public void setVendedorService(VendedorService service) {
		this.service = service;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initilizeNodes();
	}

	private void initilizeNodes() {
		
		tbvId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tbvNome.setCellValueFactory(new PropertyValueFactory<>("name"));
		tbvEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		tbvAniversario.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
		Utils.formatTableColumnDate(tbvAniversario, "dd/MM/yyyy" );
		tbvSalario.setCellValueFactory(new PropertyValueFactory<>("baseSalary"));
		Utils.formatTableColumnDouble(tbvSalario, 2);
		
		// define o tamanho da tableview para o tamanho da janela????
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tbvVendedor.prefHeightProperty().bind(stage.heightProperty());
	}

	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Serviço deve ser criado antes.");
		}
		obsLista = FXCollections.observableArrayList(service.findALl());
		tbvVendedor.setItems(obsLista);
		initEditButtons();
		initExcluirButtons();
	}

	public void CreateDialogForm(Vendedor obj, Stage oldstate, String formload) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(formload));
			Pane painel = loader.load();

			VendedorFormControl control = loader.getController();
     		control.setVendedor(obj);
			control.setVendedorService(new VendedorService(), new DepartamentoService());
			control.loadAssociatObjetcts();
			control.subdataChangeList(this);
			control.updateVendedorformControl();
			Stage dialog = new Stage();
			dialog.setTitle("Novo Vendedor");
			dialog.setScene(new Scene(painel));
			dialog.setResizable(false);
			dialog.initOwner(oldstate);
			dialog.initModality(Modality.WINDOW_MODAL);
			dialog.showAndWait();

		} catch (IOException e) {
			Alertas.showAlert("IO Exception", "Erro lendo formulario", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDataChangeListenner() {
		updateTableView();
	}

	private void initEditButtons() {
		tbvEdit.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tbvEdit.setCellFactory(param -> new TableCell<Vendedor, Vendedor>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Vendedor obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> CreateDialogForm(obj, Utils.currentStage(event), "/gui/VendedorForm.fxml"));
			}
		});
	}
	private void initExcluirButtons() {
		tbvExcluir.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tbvExcluir.setCellFactory(param -> new TableCell<Vendedor, Vendedor>() {
			private final Button button = new Button("Excluir");

			@Override
			protected void updateItem(Vendedor obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> ExcluirVendedor(obj, Utils.currentStage(event), "/gui/VendedorForm.fxml"));
			}
		});
	}

	private void ExcluirVendedor(Vendedor obj, Stage currentStage, String string) {
		Optional<ButtonType> result = Alertas.showConfirmation("Confirma", "Deseja excluir o Vendedor '" + obj.getName() +"'?");
		if(result.get().equals(ButtonType.OK)) {
			service.ExcluirVendedor(obj);
			updateTableView();
		}
		
	}
}
