package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.util.Alertas;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.services.DepartamentoService;
import model.services.VendedorService;

public class MainViewController implements Initializable {

	@FXML
	private MenuItem menuItemVendedor;
	
	@FXML
	private MenuItem menuItemDepartamento;
	
	@FXML
	private MenuItem menuItemSobre;
	
	@FXML
	public void onMenuItemVendedorAction() {
		loadView("/gui/VendedorLista.fxml", (VendedorListaController controle) -> { 
			controle.setVendedorService(new VendedorService());
			controle.updateTableView();
		});

	}
	
	@FXML
	public void onMenuItemDepartamentoAction() {
		loadView("/gui/DepartamentoLista.fxml", (DepartamentListaController controle) -> { 
			controle.setDepartamentoService(new DepartamentoService());
			controle.updateTableView();
		});
	}
	
	@FXML
	public void onMenuItemSobreAction() {
		loadView("/gui/Sobre.fxml", x -> {});		
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		
	}
	
	private synchronized <T> void loadView(String loadActive, Consumer<T> initialize) {		
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(loadActive));
			VBox newvbox = loader.load();
			
			Scene scene = Main.getMainScene();
			
			VBox mainVbox = (VBox) ((ScrollPane) scene.getRoot()).getContent();
			
			Node mainMenu = mainVbox.getChildren().get(0);
			mainVbox.getChildren().clear();
			mainVbox.getChildren().add(mainMenu);
			
			mainVbox.getChildren().addAll(newvbox.getChildren());
			
			T control = loader.getController();
			initialize.accept(control);			
			
		} catch (IOException e) {
			Alertas.showAlert("Falha grave", "Não foi possivel mostra tela", e.getMessage(),AlertType.ERROR);
		}
	}
}
