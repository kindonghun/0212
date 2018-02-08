package application;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class PaymentController {
	private ObservableList<Order> data;
	private static Stage stage;
	private Main mainApp;
	@FXML
	private TableView<Order> table = new TableView<Order>();
	@FXML
	private TableColumn<Order, String> fc;
	@FXML
	private TableColumn<Order, String> sc;

	@FXML
	private TextField SumPrice;

	public void setData(ObservableList<Order> data) {
		this.data = data;
		table.setItems(data);
		SumPrice.setText(String.format("%d", CalcurateSumPrice()));
		// SumPrice.setText(String.format("%d",1000));
	}

	public void setMainApp(Main mainApp) {
		this.mainApp = mainApp;
	}

	@FXML
	public void back() { //�ڷΰ���
		mainApp.ShowOrderView();
	}

	@FXML
	private void initialize() {
		table.setItems(data);
		fc.setCellValueFactory(cellData -> cellData.getValue().MenuName1Property());
		sc.setCellValueFactory(cellData -> cellData.getValue().PriceProperty());

	}

	@FXML
	public void PaymentPostulat() throws IOException { //�ֹ��Ϸ� dialog
		Stage dialog = new Stage(StageStyle.UTILITY);
		dialog.initModality(Modality.WINDOW_MODAL);
		dialog.initOwner(stage);
		dialog.setTitle("�ֹ��Ϸ�");
		Parent parent = FXMLLoader.load(getClass().getResource("paymentPostulat.fxml"));
		Scene scene = new Scene(parent);
		dialog.setScene(scene);
		// dialog.setResizable(false);
		
		dialog.show();
		
	}

	@FXML
	public void receipt() { //���������
		String menuInvoice = new String();
		
		for(Order order : data) {
			menuInvoice +=String.format("%s, %s��\r\n",order.getMenuName1(),order.getPrice() ) ;
		}
		
		String message = "--------������--------\r\n"
						+"�����̸�: Java		\r\n"
						+"������ġ: ITBank 11��\r\n"
						+"������ȭ: 112		\r\n"
						+"==================\r\n"
						+"���ĸ�	  �ݾ�	\r\n"
						+"==================\r\n"
						+menuInvoice+
						"==================\r\n"
						+String.format("�ΰ���:%7.0f��\r\n",+Integer.parseInt(SumPrice.getText())*0.1)
						+String.format("��   ��:%s\r\n",SumPrice.getText()+"��")
						+String.format("��   ��:%s",SumPrice.getText()+"��");
	
		File file = new File("������.txt");
		FileWriter writer = null;

		try {
			// ���� ������ ���뿡 �̾ ������ true��, ���� ������ ���ְ� ���� ������ false�� �����Ѵ�.
			writer = new FileWriter(file, true);
			writer.write(message);
			writer.flush();
			
			table.setItems(data);
			fc.setCellValueFactory(cellData -> cellData.getValue().MenuName1Property());
			sc.setCellValueFactory(cellData -> cellData.getValue().PriceProperty());
			System.out.println("����������µǾ����ϴ�");
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (writer != null)
					writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private int CalcurateSumPrice() {
		int sum = 0;
		for (Order order : data) {
			if (order != null) {
				sum += Integer.parseInt(order.getPrice());
			}
		}
		return sum;
	}
}
