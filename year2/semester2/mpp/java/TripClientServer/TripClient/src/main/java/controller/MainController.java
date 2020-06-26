package controller;

import domain.Account;
import domain.Booking;
import domain.Trip;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.ServiceException;
import service.TripObserver;
import service.TripServices;
import validator.ValidationException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class MainController implements TripObserver {
    private static final Logger LOGGER = LogManager.getLogger(MainController.class.getName());

    private Account account;
    ObservableList<Trip> modelTrips = FXCollections.observableArrayList();
    ObservableList<Trip> modelSearchTrips = FXCollections.observableArrayList();
    @FXML
    TableView<Trip> tripsTable;
    @FXML
    TableColumn<Trip, String> landmarkColumn;
    @FXML
    TableColumn<Trip, String> companyColumn;
    @FXML
    TableColumn<Trip, LocalDateTime> departureColumn;
    @FXML
    TableColumn<Trip, Integer> priceColumn;
    @FXML
    TableColumn<Trip, Integer> placesColumn;

    @FXML
    TableView<Trip> searchTripsTable;
    @FXML
    TableColumn<Trip, String> searchCompanyColumn;
    @FXML
    TableColumn<Trip, LocalDateTime> searchDepartureColumn;
    @FXML
    TableColumn<Trip, Integer> searchPriceColumn;
    @FXML
    TableColumn<Trip, Integer> searchPlacesColumn;

    @FXML
    private TextField landmarkText;
    @FXML
    private TextField startHourText;
    @FXML
    private TextField endHourText;
    @FXML
    private Button searchButton;
    @FXML
    private TextField clientNameText;
    @FXML
    private TextField phoneText;
    @FXML
    private TextField numOfTicketsText;
    @FXML
    private Button bookButton;
    @FXML
    private Button logoutButton;
    private Stage stage;
    private TripServices tripServices;

    public void setService(Account account, TripServices tripServices, Stage stage) {
        this.account = account;
        this.tripServices = tripServices;
        this.stage = stage;
        List<Trip> trips = tripServices.getAllTrips();
        Platform.runLater(() -> {
            modelTrips.setAll(trips);
        });
    }


    @FXML
    public void initialize() {
        landmarkColumn.setCellValueFactory(new PropertyValueFactory<Trip, String>("landmark"));
        companyColumn.setCellValueFactory(new PropertyValueFactory<Trip, String>("companyName"));
        departureColumn.setCellValueFactory(new PropertyValueFactory<Trip, LocalDateTime>("departureTime"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<Trip, Integer>("price"));
        placesColumn.setCellValueFactory(new PropertyValueFactory<Trip, Integer>("availablePlaces"));
        searchCompanyColumn.setCellValueFactory(new PropertyValueFactory<Trip, String>("companyName"));
        searchDepartureColumn.setCellValueFactory(new PropertyValueFactory<Trip, LocalDateTime>("departureTime"));
        searchPriceColumn.setCellValueFactory(new PropertyValueFactory<Trip, Integer>("price"));
        searchPlacesColumn.setCellValueFactory(new PropertyValueFactory<Trip, Integer>("availablePlaces"));
        searchTripsTable.setItems(modelSearchTrips);
        tripsTable.setItems(modelTrips);
        tripsTable.setRowFactory(row -> getTableRow());
        searchTripsTable.setRowFactory(row -> getTableRow());

    }

    private TableRow<Trip> getTableRow() {
        return new TableRow<Trip>() {
            @Override
            public void updateItem(Trip item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setStyle("");
                } else {
                    if (item.getAvailablePlaces() == 0) {
//                        for (int i = 0; i < getChildren().size(); i++) {
//                            ((Labeled) getChildren().get(i)).setTextFill(Color.RED);
//                        }
                        setStyle("-fx-background-color: #F07470");
                    }
                }
            }
        };
    }

    @FXML
    public void handleSearchTrip() {
        LOGGER.info("handle search trips");
        String landmark = landmarkText.getText();
        String startHour = startHourText.getText();
        String endHour = endHourText.getText();
        if (landmark.isEmpty()) {
            AlertMessage.showMessage("Landmark must be entered", Alert.AlertType.ERROR);
            return;
        }
        if (startHour.isEmpty() || endHour.isEmpty()) {
            AlertMessage.showMessage("Hours must be entered", Alert.AlertType.ERROR);
            return;
        }
        try {
            int start = Integer.parseInt(startHour);
            int end = Integer.parseInt(endHour);
            if ((start >= end) || (start < 0) || (start > 23) || (end < 0) || (end > 24)) {
                AlertMessage.showMessage("Invalid interval", Alert.AlertType.ERROR);
                return;
            }
            List<Trip> trips = tripServices.getTripsByLandmarkDepartureHour(landmark, start, end);
            Platform.runLater(() -> {
                modelSearchTrips.setAll(trips);
            });
        } catch (NumberFormatException ne) {
            AlertMessage.showMessage("Invalid hour", Alert.AlertType.ERROR);
        } catch (Exception e) {
            LOGGER.warn("handle search trips failed " + e);
            e.printStackTrace();
        }
    }

    @FXML
    public void handleBooking() {
        LOGGER.info("handle add booking");
        String client = clientNameText.getText();
        String phone = phoneText.getText();
        String tickets = numOfTicketsText.getText();
        Trip trip = tripsTable.getSelectionModel().getSelectedItem();
        if (trip == null) {
            AlertMessage.showMessage("A trip must be selected", Alert.AlertType.ERROR);
            return;
        }
        if (client.isEmpty()) {
            AlertMessage.showMessage("Client name must be entered", Alert.AlertType.ERROR);
            return;
        }
        if (phone.isEmpty()) {
            AlertMessage.showMessage("Phone number must be entered", Alert.AlertType.ERROR);
            return;
        }
        if (tickets.isEmpty()) {
            AlertMessage.showMessage("Number of tickets must be entered", Alert.AlertType.ERROR);
            return;
        }
        try {
            int numTickets = Integer.parseInt(tickets);
            tripServices.addBooking(client, phone, numTickets, trip, account);
            clearFields();
            AlertMessage.showMessage("Booking saved", Alert.AlertType.INFORMATION);
        } catch (NumberFormatException ne) {
            AlertMessage.showMessage("Invalid number of tickets", Alert.AlertType.ERROR);
        } catch (ValidationException | ServiceException ve) {
            AlertMessage.showMessage(ve.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            LOGGER.warn("handle add booking failed " + e);
            e.printStackTrace();
        }
    }


    private void clearFields() {
        clientNameText.clear();
        phoneText.clear();
        numOfTicketsText.clear();
    }

    @FXML
    public void handleLogout() {
        tripServices.logOut();
        tripServices.removeTripObserver(this);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/loginView.fxml"));
        AnchorPane root = null;
        try {
            root = loader.load();
            Stage primaryStage = new Stage();
            LoginController loginController = loader.getController();
            loginController.setService(tripServices, primaryStage);
            Scene scene = new Scene(root, 600, 400);
            primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    tripServices.logOut();
                }
            });
            primaryStage.setTitle("Login");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.close();
    }

    @Override
    public void bookingInserted(Booking booking) {
        Platform.runLater(() -> {
            LOGGER.info("booking inserted " + booking + " updating table views");
            updateTableRow(booking, modelTrips);
            updateTableRow(booking, modelSearchTrips);
        });
    }

    private void updateTableRow(Booking booking, ObservableList<Trip> trips) {
        for (int i = 0; i < trips.size(); i++) {
            Trip t = trips.get(i);
            if (t.getId().equals(booking.getTripId())) {
                t.setAvailablePlaces(t.getAvailablePlaces() - booking.getNumTickets());
                trips.set(i, t);
                break;
            }
        }
    }
}
