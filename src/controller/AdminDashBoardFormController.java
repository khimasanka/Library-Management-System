package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTabPane;
import db.DbConnection;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import util.Login;
import util.ValidationUtil;
import view.tm.*;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.regex.Pattern;

public class AdminDashBoardFormController {

    public AnchorPane loadWindowContext;
    public Label lblTitle;
    public StackPane dashBoardFullContext;
    public StackPane stackContext;
    public Label lblDate;
    public Label lblTime;
    public TextField txtBookName;
    public TextField txtBooksQuantity;
    public TextField txtBookCategory;
    public TextField txtBookLanguage;
    public TextField txtBooksAuthorName;
    public Label lblBookId;
    public TableView<BooksTM> tblBooks;
    public TableColumn colBookId;
    public TableColumn colBookName;
    public TableColumn colBookAuthor;
    public TableColumn colBookLanguage;
    public TableColumn colCategory;
    public TableColumn colBookQty;
    public TextField txtBookSearch;
    public JFXButton btnClearField;
    public JFXTabPane tabPaneContext;
    public TableView<ItemTM> tblItem;
    public TableColumn colItemId;
    public TableColumn colItemName;
    public TableColumn colItemType;
    public TableColumn colItemQuantity;
    public Label lblItemId;
    public TextField txtItemType;
    public TextField txtItemQuantity;
    public TextField txtItemName;
    public TextField txtItemSearch;
    public TextField txtBookCaseSearch;
    public TextField txtAuthorSearch;
    public Label lblBookCaseId;
    public TextField txtCaseBookLanguage;
    public TextField txtCaseBookType;
    public TableView<BookCaseTM> tblBookCase;
    public TableColumn colBookCaseId;
    public TableColumn colBookCaseType;
    public TableColumn colBookCaseLanguage;
    public Label lblAuthorId;
    public TextField txtAuthorName;
    public TableView<AuthorTM> tblAuthor;
    public TableColumn colAuthorId;
    public TableColumn colAuthorName;
    public JFXButton btnDonation;
    public VBox vboxContext;
    public Label lblUserOrAdmin;
    public JFXButton btnRemoveBook;
    public JFXButton btnRemoveItem;
    public JFXButton btnSaveBookDetails;
    public JFXButton btnItemSave;
    public JFXButton btnBookCaseSave;
    public JFXButton btnSaveAuthor;
    public JFXButton btnUpDateAuthor;
    public JFXButton btnUpdateBookCase;
    public JFXButton btnUpdateItem;
    public JFXButton btnUpdateBook;
    public JFXButton btnRemoveBookCase;
    public JFXButton btnRemoveAuthor;

    ObservableList<BooksTM> booksObList = FXCollections.observableArrayList();
    ObservableList<ItemTM>itemObList;
    ObservableList<BookCaseTM>caseTMS  ;
    ObservableList<AuthorTM>authorTMS ;

    LinkedHashMap<TextField,Pattern> bookMap = new LinkedHashMap<>();
    LinkedHashMap<TextField,Pattern> itemMap = new LinkedHashMap<>();
    LinkedHashMap<TextField,Pattern> bookCaseMap = new LinkedHashMap<>();
    LinkedHashMap<TextField,Pattern> authorMap = new LinkedHashMap<>();

    ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
    ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

    public void initialize() throws SQLException, ClassNotFoundException {
        loadDateAndTime();
        loadAllBooks();
        setBookCaseTableData();
        tblAuthor.getItems().setAll(loadAllAuthors());
        tblBookCase.getItems().setAll(loadAllBookCases());
        tblItem.getItems().setAll(loadAllItems());
        setAuthors();
        searchBarActivate();
        setItemsToTable();
        setItemId();

        bookValidations();
        btnSaveBookDetails.setDisable(true);
        btnUpdateBook.setDisable(true);

        itemValidations();
        btnItemSave.setDisable(true);
        btnUpdateItem.setDisable(true);

        bookCaseValidate();
        btnBookCaseSave.setDisable(true);
        btnUpdateBookCase.setDisable(true);
        btnRemoveBookCase.setDisable(true);

        authorValidate();
        btnSaveAuthor.setDisable(true);
        btnUpDateAuthor.setDisable(true);
        btnRemoveAuthor.setDisable(true);

        if(Login.login.equals("User")){
            lblUserOrAdmin.setText("User");
            btnRemoveBook.setDisable(true);
            btnRemoveItem.setDisable(true);
        }else {
            lblUserOrAdmin.setText("Admin");
        }

        txtAuthorSearch.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                searchAuthor(newValue);
            }
        });

        txtBookSearch.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                bookSearchDataToTable(newValue);
            }
        });

        txtBookCaseSearch.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                searchBookCase(newValue);
            }
        });
     }

    public void loadDateAndTime() {
        Date date = new Date();
        SimpleDateFormat f= new SimpleDateFormat("yyyy-MM-dd");
        lblDate.setText(f.format(date));

        final Thread thread = new Thread(()->{
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm aa ");
            while (true){
                try{
                    Thread.sleep(1000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                final String times=simpleDateFormat.format(new Date());
                Platform.runLater(()->{
                    lblTime.setText(times);
                });
            }
        });
        thread.start();

    }

    private void loadAllBooks() throws SQLException, ClassNotFoundException {
        setBooksToTable(new BooksController().getAllBooks());
        colBookId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colBookName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colBookLanguage.setCellValueFactory(new PropertyValueFactory<>("language"));
        colBookAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        colBookQty.setCellValueFactory(new PropertyValueFactory<>("qty"));

    }

    private void setBooksToTable(ArrayList<Books> books){
        books.forEach(e->{
            booksObList.add(new BooksTM(
               e.getId(),e.getBookName(),e.getBookType(),e.getLanguage(),e.getAuthorName(),e.getCount()));
        });
        tblBooks.setItems(booksObList);
    }

    public void bookSearchOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        bookSearchDataToTable(txtBookSearch.getText());
    }

    private void bookSearchDataToTable(String value){
        try {
            List<Books> books = BooksController.searchBookForAll(value);
            if(books==null){
                new Alert(Alert.AlertType.WARNING,"Empty Result Set").show();
            }else {

                ObservableList<BooksTM> tableData = FXCollections.observableArrayList();
                for (Books book : books) {
                    tableData.add(new BooksTM(
                            book.getId(),
                            book.getBookName(),
                            book.getBookType(),
                            book.getLanguage(),
                            book.getAuthorName(),
                            book.getCount()
                    ));
                    lblBookId.setText(book.getId());
                    txtBookName.setText(book.getBookName());
                    txtBooksAuthorName.setText(book.getAuthorName());
                    txtBookCategory.setText(book.getBookType());
                    txtBookLanguage.setText(book.getLanguage());
                    txtBooksQuantity.setText(String.valueOf(book.getCount()));
                    btnUpdateBook.setDisable(false);
                }

                tblBooks.getItems().setAll(tableData);
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    public void updateBookDetail(MouseEvent mouseEvent) throws SQLException, ClassNotFoundException, IOException {
        Books b1 = new Books(
                lblBookId.getText(),
                txtBookName.getText(),
                txtBookCategory.getText(),
                txtBookLanguage.getText(),
                txtBooksAuthorName.getText(),
                Integer.parseInt(txtBooksQuantity.getText())
        );

        if(new BooksController().updateBooks(b1)){
            new Alert(Alert.AlertType.CONFIRMATION,"Updated..").show();
            refreshBookTable();
        }else{
            new Alert(Alert.AlertType.WARNING,"Try Again..").show();
        }
    }

    public void removeBook(MouseEvent mouseEvent) throws SQLException, ClassNotFoundException, IOException {
        if(new BooksController().deleteBooks(lblBookId.getText())){
            refreshBookTable();
        }else {

        }
    }

    public void bookFieldClearOnAction(MouseEvent mouseEvent)  {
        String id = tblBooks.getSelectionModel().getSelectedItem().getId();
        boolean isDeleted;
        try {
            if (new BooksController().deleteBooks(id)) isDeleted = true;
            else isDeleted = false;

            if(isDeleted) {
                refreshBookTable();
            }
        } catch (SQLException | NullPointerException | ClassNotFoundException | IOException throwables) {
            throwables.printStackTrace();
        }
    }

    public void BooksSaveOnAction(MouseEvent mouseEvent) throws SQLException, ClassNotFoundException, IOException {
        Books b1 = new Books(
                lblBookId.getText(),
                txtBookName.getText(),
                txtBookCategory.getText(),
                txtBookLanguage.getText(),
                txtBooksAuthorName.getText(),
                Integer.parseInt(txtBooksQuantity.getText())
        );
        if (new BooksController().saveBooks(b1)) {
            new Alert(Alert.AlertType.CONFIRMATION, "Successful").show();
            refreshBookTable();
        }else {
            new Alert(Alert.AlertType.WARNING, "Try Again").show();
        }
    }

    //================================== Stages ===========================================
    public void logOutOnAction(MouseEvent mouseEvent) throws IOException {
        Alert alert =new Alert(Alert.AlertType.CONFIRMATION,"Are You sure you want to Logout..?",yes,no);
        alert.setTitle("Logout");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.orElse(no)==yes){
            Stage window = (Stage) dashBoardFullContext.getScene().getWindow();
            window.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/LoginForm.fxml"))));
        }else {
        }
    }

    private void setItemId(){
        try {
            lblItemId.setText(ItemController.getItemId());
            lblBookCaseId.setText(BookCaseController.getBookCaseID());
            lblAuthorId.setText(AuthorController.getAuthorId());
            lblBookId.setText(BooksController.getBookId());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void memberWindowOnAction(MouseEvent mouseEvent) throws IOException {
        lblTitle.setText("Members");
        txtBookSearch.setVisible(false);

        URL resource = getClass().getResource("../view/MembersForm.fxml");
        Parent load = FXMLLoader.load(resource);
        stackContext.getChildren().add(load);
    }

    public void donationsWindowOnAction(MouseEvent mouseEvent) throws IOException {
        lblTitle.setText("Donations");
        txtBookSearch.setVisible(false);

        URL resource = getClass().getResource("../view/DonationsForm.fxml");
        Parent load = FXMLLoader.load(resource);
        stackContext.getChildren().add(load);
    }

    public void settingsWindowOnAction(MouseEvent mouseEvent) throws IOException {
        lblTitle.setText("Settings");

        URL resource = getClass().getResource("../view/SettingsForm.fxml");
        Parent load = FXMLLoader.load(resource);
        stackContext.getChildren().add(load);
    }

    public void borrowBooksWindow(MouseEvent mouseEvent) throws IOException {
        lblTitle.setText("Borrow Books");
        txtBookSearch.setVisible(false);

        URL resource = getClass().getResource("../view/BorrowBooksForm.fxml");
        Parent load = FXMLLoader.load(resource);
        stackContext.getChildren().add(load);

    }

    public void booksWindowOnAction(MouseEvent mouseEvent) throws IOException {
        refreshBookTable();
    }

    public void refreshBookTable() throws IOException {
        Stage window = (Stage) dashBoardFullContext.getScene().getWindow();
        window.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/AdminDashBoardForm.fxml"))));
    }

    public void reportsWindowOnAction(MouseEvent mouseEvent) throws IOException {

    }

    public void reserveBooksWindowOnAction(MouseEvent mouseEvent) throws IOException {
        lblTitle.setText("Reserve Book");

        URL resource = getClass().getResource("../view/ReserveBooksForm.fxml");
        Parent load = FXMLLoader.load(resource);
        stackContext.getChildren().add(load);
    }

    //=======================================================================================

    public void searchBarActivate(){
        txtItemSearch.setVisible(false);
        txtBookCaseSearch.setVisible(false);
        txtAuthorSearch.setVisible(false);
        tabPaneContext.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals(1)) {
                lblTitle.setText("Items");
                txtBookSearch.setVisible(false);
                txtItemSearch.setVisible(true);
                txtBookCaseSearch.setVisible(false);
                txtAuthorSearch.setVisible(false);
            }else if(newValue.equals(2)) {
                lblTitle.setText("Book-Cases");
                txtItemSearch.setVisible(false);
                txtBookSearch.setVisible(false);
                txtBookCaseSearch.setVisible(true);
                txtAuthorSearch.setVisible(false);
            }else if(newValue.equals(3)) {
                lblTitle.setText("Authors");
                txtAuthorSearch.setVisible(true);
                txtItemSearch.setVisible(false);
                txtBookCaseSearch.setVisible(false);
                txtBookSearch.setVisible(false);
            }else {
                lblTitle.setText("Books");
                txtItemSearch.setVisible(false);
                txtBookSearch.setVisible(true);
                txtBookCaseSearch.setVisible(false);
                txtAuthorSearch.setVisible(false);
            }

        });
    }

    public void clearItemFields(ActionEvent actionEvent) {
        String id = tblItem.getSelectionModel().getSelectedItem().getId();
        boolean isDeleted = false;
        try {
            isDeleted = new ItemController().deleteItem(id);
            if(isDeleted) {
                tblItem.getItems().setAll(loadAllItems());
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }

    }

    public void removeItemOnAction(ActionEvent actionEvent) {
        try {
            if(new ItemController().deleteItem(lblItemId.getText())){
                tblItem.getItems().setAll(loadAllItems());
                clearItemFields();
            }else {

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void updateItemOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        Item i1 = new Item(
                lblItemId.getText(),
                txtItemName.getText(),
                txtItemType.getText(),
                Integer.parseInt(txtItemQuantity.getText())
        );

        if(new ItemController().updateItem(i1)){
            new Alert(Alert.AlertType.CONFIRMATION,"Updated..").show();
            tblItem.getItems().setAll(loadAllItems());
            clearItemFields();
        }else{
            new Alert(Alert.AlertType.WARNING,"Try Again..").show();
        }
    }

    public void saveItemOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException  {
        Item i1 = new Item(
                lblItemId.getText(),
                txtItemName.getText(),
                txtItemType.getText(),
                Integer.parseInt(txtItemQuantity.getText())
        );
        if (new ItemController().saveItem(i1)) {
            new Alert(Alert.AlertType.CONFIRMATION, "Successful").show();
            setItemId();
            tblItem.getItems().setAll(loadAllItems());
            clearItemFields();
            btnItemSave.setDisable(true);
        }else {
            new Alert(Alert.AlertType.WARNING, "Try Again").show();
        }
    }

    private void setItemsToTable() throws SQLException, ClassNotFoundException {
        colItemId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colItemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        colItemType.setCellValueFactory(new PropertyValueFactory<>("itemType"));
        colItemQuantity.setCellValueFactory(new PropertyValueFactory<>("qty"));

    }

    void clearItemFields(){
        txtItemName.clear();
        txtItemType.clear();
        txtItemQuantity.clear();
        txtItemSearch.clear();
    }

    public ObservableList<ItemTM> loadAllItems() throws SQLException, ClassNotFoundException {
        List<Item> items = new ItemController().getAllItems();

         itemObList=FXCollections.observableArrayList();
        for (Item i1: items) {
            itemObList.add(new ItemTM(
                    i1.getItemId(),
                    i1.getItemName(),
                    i1.getItemType(),
                    i1.getQty()
            ));
        }
        return itemObList;
    }

    public void itemSearchOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        Item i1 = new ItemController().searchItem(txtItemSearch.getText());
        if (i1 == null) {
            new Alert(Alert.AlertType.WARNING,"Empty Result Set..").show();
        }else{
            lblItemId.setText(i1.getItemId());
            txtItemName.setText(i1.getItemName());
            txtItemType.setText(i1.getItemType());
            txtItemQuantity.setText(String.valueOf(i1.getQty()));
            btnRemoveItem.setDisable(false);
            btnUpdateItem.setDisable(false);
            btnItemSave.setDisable(true);
        }
    }

    public void bookCaseSearchOnAction(ActionEvent actionEvent)  {
        searchBookCase(txtBookCaseSearch.getText());
    }

    private void searchBookCase(String value) {
        try {
            List <BookCase> cases = BookCaseController.searchBookCase(value);
            ObservableList<BookCaseTM> tableData = FXCollections.observableArrayList();

                for (BookCase bookCase : cases) {
                    tableData.add(new BookCaseTM(
                            bookCase.getCaseId(),
                            bookCase.getBookType(),
                            bookCase.getBookLanguage()
                    ));
                    lblBookCaseId.setText(bookCase.getCaseId());
                    txtCaseBookType.setText(bookCase.getBookType());
                    txtCaseBookLanguage.setText(bookCase.getBookLanguage());
                    btnRemoveBookCase.setDisable(false);
                    btnUpdateBookCase.setDisable(false);
                }
            tblBookCase.getItems().setAll(tableData);
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    public void authorSearchOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        searchAuthor(txtAuthorSearch.getText());
    }

    private void searchAuthor(String value){
        try {
            List <Authors> authors = AuthorController.searchAuthor(value);
            ObservableList<AuthorTM> tableData = FXCollections.observableArrayList();
            if(authors==null){
                new Alert(Alert.AlertType.WARNING,"Empty Result Set").show();
            }else {
                for (Authors author : authors) {
                    tableData.add(new AuthorTM(
                            author.getAuthorId(),
                            author.getName()
                    ));
                    lblAuthorId.setText(author.getAuthorId());
                    txtAuthorName.setText(author.getName());
                    btnRemoveAuthor.setDisable(false);
                    btnUpDateAuthor.setDisable(false);
                }
            }
            tblAuthor.getItems().setAll(tableData);
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    public void removeBookCaseOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if( new BookCaseController().deleteCase(lblBookCaseId.getText())){
            tblBookCase.getItems().setAll(loadAllBookCases());
        }else {

        }
    }

    public void updateBookCaseOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        BookCase b1 = new BookCase(
                lblBookCaseId.getText(),
                txtCaseBookType.getText(),
                txtCaseBookLanguage.getText()
        );
        if(new BookCaseController().updateCase(b1)){
            new Alert(Alert.AlertType.CONFIRMATION,"Updated..").show();
            tblBookCase.getItems().setAll(loadAllBookCases());
        }else{
            new Alert(Alert.AlertType.WARNING,"Try Again..").show();
        }
    }

    public void saveBookCaseOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        BookCase i1 = new BookCase(
                lblBookCaseId.getText(),
                txtCaseBookType.getText(),
                txtCaseBookLanguage.getText()
        );
        if (new BookCaseController().saveBookCase(i1)) {
            new Alert(Alert.AlertType.CONFIRMATION, "Successful").show();
            setItemId();
            tblBookCase.getItems().setAll(loadAllBookCases());
            btnBookCaseSave.setDisable(true);
        }else {
            new Alert(Alert.AlertType.WARNING, "Try Again").show();
        }
    }

    public ObservableList<BookCaseTM> loadAllBookCases() throws SQLException, ClassNotFoundException {
        List<BookCase> bookCases = new BookCaseController().getAllItems();

        caseTMS =FXCollections.observableArrayList();
        for (BookCase bookCase: bookCases) {
            caseTMS.add(new BookCaseTM(
                    bookCase.getCaseId(),
                    bookCase.getBookType(),
                    bookCase.getBookLanguage()
            ));
        }
        return caseTMS;
    }

    private void setBookCaseTableData(){
        colBookCaseId.setCellValueFactory(new PropertyValueFactory<>("caseId"));
        colBookCaseType.setCellValueFactory(new PropertyValueFactory<>("bookType"));
        colBookCaseLanguage.setCellValueFactory(new PropertyValueFactory<>("language"));

    }

    public void removeAuthorOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if(new AuthorController().deleteAuthor(lblAuthorId.getText())){
            tblAuthor.getItems().setAll(loadAllAuthors());
            txtAuthorName.clear();
        }else {

        }
    }

    public void updateAuthorOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        Authors b1 = new Authors(
                lblAuthorId.getText(),
                txtAuthorName.getText()
        );
        if(new AuthorController().updateAuthor(b1)){
            new Alert(Alert.AlertType.CONFIRMATION,"Updated..").show();
            tblAuthor.getItems().setAll(loadAllAuthors());
            txtAuthorName.clear();
            txtAuthorSearch.clear();
        }else{
            new Alert(Alert.AlertType.WARNING,"Try Again..").show();
        }
    }

    public void saveAuthorOnAction(ActionEvent actionEvent) {
        Authors a1 = new Authors(
                lblAuthorId.getText(),
                txtAuthorName.getText()

        );
        try {
            if (new AuthorController().saveAuthor(a1)) {
                new Alert(Alert.AlertType.CONFIRMATION, "Successful").show();
                tblAuthor.getItems().setAll(loadAllAuthors());
                txtAuthorName.clear();
                btnSaveAuthor.setDisable(true);
                setItemId();
            }else {
                new Alert(Alert.AlertType.WARNING, "Try Again").show();
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    public ObservableList<AuthorTM> loadAllAuthors() throws SQLException, ClassNotFoundException {
        List<Authors> allAuthors = new AuthorController().getAllAuthors();

         authorTMS =FXCollections.observableArrayList();
        for (Authors authors: allAuthors) {
            authorTMS.add(new AuthorTM(
                    authors.getAuthorId(),
                    authors.getName()

            ));
        }
        return authorTMS;
    }

    void setAuthors(){
        colAuthorId.setCellValueFactory(new PropertyValueFactory<>("authorId"));
        colAuthorName.setCellValueFactory(new PropertyValueFactory<>("name"));
    }

    public void authorClearField(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        String id = tblAuthor.getSelectionModel().getSelectedItem().getAuthorId();
        boolean isDeleted = new AuthorController().deleteAuthor(id);
        if(isDeleted) {
            tblAuthor.getItems().setAll(loadAllAuthors());
        }
    }

    public void bookCaseFieldClear(ActionEvent actionEvent)  {
        String id = tblBookCase.getSelectionModel().getSelectedItem().getCaseId();
        boolean isDeleted = false;
        try {
            isDeleted = new BookCaseController().deleteCase(id);

        if(isDeleted) {
            tblBookCase.getItems().setAll(loadAllBookCases());
        }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    private void bookValidations() {
        bookMap.put(txtBookName, Pattern.compile("^[A-z\\d][\\dA-z:\\/\"'.]+([\\ A-z][A-z\\d\\:@#/]+)*$"));
        bookMap.put(txtBooksAuthorName,Pattern.compile("^[A-Za-z][A-Za-z\\'\\-.]+([\\ A-Za-z][A-Za-z\\'\\-.]+)*"));
        bookMap.put(txtBookLanguage,Pattern.compile("^[A-z]{3,12}$"));
        bookMap.put(txtBookCategory,Pattern.compile("^[A-z/.,& ]{3,35}$"));
        bookMap.put(txtBooksQuantity,Pattern.compile("^[0-9]{1,3}$"));
    }

    public void bookSaveFieldFocus(KeyEvent keyEvent) {
        Object response = ValidationUtil.textFieldValidate(bookMap,btnSaveBookDetails);
        if (keyEvent.getCode()== KeyCode.ENTER){
            if (response instanceof TextField){
                TextField errorText = (TextField) response;
                errorText.requestFocus();
            }else if(response instanceof Boolean){
            }
        }

    }

    public void itemValidations(){
        itemMap.put(txtItemName, Pattern.compile("^[A-z ]{3,15}$"));
        itemMap.put(txtItemType,Pattern.compile("^[A-z0-9.'*(),/ ]{3,35}$"));
        itemMap.put(txtItemQuantity,Pattern.compile("^[0-9]{1,3}$"));
    }

    public void itemFieldFocus(KeyEvent keyEvent) {
        Object response = ValidationUtil.textFieldValidate(itemMap,btnItemSave);
        if (keyEvent.getCode()== KeyCode.ENTER){
            if (response instanceof TextField){
                TextField errorText = (TextField) response;
                errorText.requestFocus();
            }else if(response instanceof Boolean){

            }
        }
    }

    public void bookCaseValidate(){
        bookCaseMap.put(txtCaseBookType,Pattern.compile("^[A-z/.,& ]{3,35}$"));
        bookCaseMap.put(txtCaseBookLanguage,Pattern.compile("^[A-z]{3,12}$"));
    }

    public void bookTypeFieldValidate(KeyEvent keyEvent) {
        Object response = ValidationUtil.textFieldValidate(bookCaseMap,btnBookCaseSave);
        if (keyEvent.getCode()== KeyCode.ENTER){
            if (response instanceof TextField){
                TextField errorText = (TextField) response;
                errorText.requestFocus();
            }else if(response instanceof Boolean){

            }
        }
    }

    public void authorValidate(){
        authorMap.put(txtAuthorName,Pattern.compile("^[A-Za-z][A-Za-z\\'\\-.]+([\\ A-Za-z][A-Za-z\\'\\-.]+)*"));
    }

    public void authorNameFieldValidate(KeyEvent keyEvent) {
        Object response = ValidationUtil.textFieldValidate(authorMap,btnSaveAuthor);
        if (keyEvent.getCode()== KeyCode.ENTER){
            if (response instanceof TextField){
                TextField errorText = (TextField) response;
                errorText.requestFocus();
            }else if(response instanceof Boolean){

            }
        }
    }

    public void printItemListOnAction(ActionEvent actionEvent) {
        try {
            JasperDesign design = JRXmlLoader.load(this.getClass().getResourceAsStream("/view/reports/ItemListPrint.jrxml"));
            JasperReport report = JasperCompileManager.compileReport(design);
            JasperPrint jasperPrint = JasperFillManager.fillReport(report, null, DbConnection.getInstance().getConnection());
            JasperViewer.viewReport(jasperPrint,false);
        } catch (JRException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

