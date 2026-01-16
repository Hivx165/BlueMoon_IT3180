package com.bluemoon.controllers;

import com.bluemoon.dao.TamVangDAO;
import com.bluemoon.models.TamVangModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;

public class TamVangController {

    @FXML private TableView<TamVangModel> tableTamVang;
    @FXML private TableColumn<TamVangModel, String> columnSoCCCD;
    @FXML private TableColumn<TamVangModel, String> columnHoTen;
    @FXML private TableColumn<TamVangModel, String> columnTuNgay;
    @FXML private TableColumn<TamVangModel, String> columnDenNgay;
    @FXML private TableColumn<TamVangModel, String> columnLyDo;

    @FXML private TextField txtSoCCCD;
    @FXML private TextField txtHoTen;
    @FXML private DatePicker dateTuNgay;
    @FXML private DatePicker dateDenNgay;
    @FXML private TextArea txtLyDo;

    private ObservableList<TamVangModel> tamVangList;

    @FXML
    public void initialize() {
        columnSoCCCD.setCellValueFactory(new PropertyValueFactory<>("soCCCD"));
        columnHoTen.setCellValueFactory(new PropertyValueFactory<>("hoTen"));
        columnTuNgay.setCellValueFactory(new PropertyValueFactory<>("tuNgay"));
        columnDenNgay.setCellValueFactory(new PropertyValueFactory<>("denNgay"));
        columnLyDo.setCellValueFactory(new PropertyValueFactory<>("lyDo"));

        loadDataToTable();
    }

    private void loadDataToTable() {
        try {
            tamVangList = FXCollections.observableArrayList(TamVangDAO.getAllTamVang());
            tableTamVang.setItems(tamVangList);
        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Lỗi", "Không thể tải dữ liệu", e.getMessage());
        }
    }

    // --- TÌM KIẾM ---
    @FXML
    private void handleSearch() {
        String cccd = txtSoCCCD.getText().trim();
        if (cccd.isEmpty()) {
            showAlert(AlertType.WARNING, "Cảnh báo", "Nhập CCCD để tìm kiếm.", null);
            return;
        }

        String name = TamVangDAO.getHoTenByCCCD(cccd);
        if (name != null) {
            txtHoTen.setText(name);
            txtHoTen.setEditable(false); // Khóa sửa tên
            showAlert(AlertType.INFORMATION, "Tìm thấy", "Cư dân: " + name, null);
        } else {
            txtHoTen.clear();
            txtHoTen.setEditable(true);
            showAlert(AlertType.ERROR, "Không tìm thấy", "Không tìm thấy cư dân này.", null);
        }
    }

    @FXML
    private void handleAdd() {
        String soCCCD = txtSoCCCD.getText();
        String hoTen = txtHoTen.getText();
        String tuNgay = (dateTuNgay.getValue() != null) ? dateTuNgay.getValue().toString() : "";
        String denNgay = (dateDenNgay.getValue() != null) ? dateDenNgay.getValue().toString() : "";
        String lyDo = txtLyDo.getText();

        if (soCCCD.isEmpty() || hoTen.isEmpty() || tuNgay.isEmpty() || denNgay.isEmpty() || lyDo.isEmpty()) {
            showAlert(AlertType.WARNING, "Lỗi", "Vui lòng nhập đủ thông tin.", null);
            return;
        }

        TamVangModel model = new TamVangModel(soCCCD, hoTen, dateTuNgay.getValue(), dateDenNgay.getValue(), lyDo);

        // Gọi UPDATE
        boolean success = TamVangDAO.updateTamVang(model);

        if (success) {
            showAlert(AlertType.INFORMATION, "Thành công", "Đã cập nhật Tạm vắng!", null);
            loadDataToTable();
            handleReset();
        } else {
            showAlert(AlertType.ERROR, "Thất bại", "Lỗi cập nhật. Kiểm tra lại CCCD.", null);
        }
    }

    @FXML
    private void handleDelete() {
        TamVangModel selected = tableTamVang.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(AlertType.WARNING, "Chọn dòng", "Vui lòng chọn dòng cần xóa.", null);
            return;
        }
        if (TamVangDAO.deleteTamVangByCCCD(selected.getSoCCCD())) {
            showAlert(AlertType.INFORMATION, "Thành công", "Đã xóa tạm vắng.", null);
            loadDataToTable();
        } else {
            showAlert(AlertType.ERROR, "Lỗi", "Xóa thất bại.", null);
        }
    }

    @FXML
    private void handleReset() {
        txtSoCCCD.clear();
        txtHoTen.clear();
        txtHoTen.setEditable(true);
        dateTuNgay.setValue(null);
        dateDenNgay.setValue(null);
        txtLyDo.clear();
    }

    private void showAlert(AlertType alertType, String title, String content, String details) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(content);
        if (details != null) {
            TextArea textArea = new TextArea(details);
            textArea.setEditable(false);
            textArea.setWrapText(true);
            alert.getDialogPane().setExpandableContent(textArea);
        }
        alert.showAndWait();
    }
}