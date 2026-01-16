package com.bluemoon.controllers;

import com.bluemoon.dao.TamTruDAO;
import com.bluemoon.models.TamTruModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;

public class TamTruController {

    @FXML private TableView<TamTruModel> tableTamTru;
    @FXML private TableColumn<TamTruModel, String> columnSoCCCD;
    @FXML private TableColumn<TamTruModel, String> columnHoTen;
    @FXML private TableColumn<TamTruModel, String> columnDiaChiTamTru;
    @FXML private TableColumn<TamTruModel, String> columnTuNgay;
    @FXML private TableColumn<TamTruModel, String> columnDenNgay;
    @FXML private TableColumn<TamTruModel, String> columnGhiChu;

    @FXML private TextField txtSoCCCD;
    @FXML private TextField txtHoTen;
    @FXML private TextField txtDiaChiTamTru;
    @FXML private DatePicker dateTuNgay;
    @FXML private DatePicker dateDenNgay;
    @FXML private TextArea txtGhiChu;

    private ObservableList<TamTruModel> tamTruList;

    @FXML
    public void initialize() {
        columnSoCCCD.setCellValueFactory(new PropertyValueFactory<>("soCCCD"));
        columnHoTen.setCellValueFactory(new PropertyValueFactory<>("hoTen"));
        columnDiaChiTamTru.setCellValueFactory(new PropertyValueFactory<>("diaChiTamTru"));
        columnTuNgay.setCellValueFactory(new PropertyValueFactory<>("tuNgay"));
        columnDenNgay.setCellValueFactory(new PropertyValueFactory<>("denNgay"));
        columnGhiChu.setCellValueFactory(new PropertyValueFactory<>("ghiChu"));

        loadDataToTable();
    }

    private void loadDataToTable() {
        try {
            tamTruList = FXCollections.observableArrayList(TamTruDAO.getAll());
            tableTamTru.setItems(tamTruList);
        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Lỗi", "Không thể tải dữ liệu", e.getMessage());
        }
    }

    // --- CHỨC NĂNG TÌM KIẾM CƯ DÂN ---
    @FXML
    private void handleSearch() {
        String cccd = txtSoCCCD.getText().trim();
        if (cccd.isEmpty()) {
            showAlert(AlertType.WARNING, "Cảnh báo", "Vui lòng nhập số CCCD để tìm.", null);
            return;
        }

        TamTruModel found = TamTruDAO.findByCCCD(cccd);
        if (found != null) {
            // Tìm thấy: Điền thông tin vào form
            txtHoTen.setText(found.getHoTen());
            if (found.getDiaChiTamTru() != null) txtDiaChiTamTru.setText(found.getDiaChiTamTru());

            // Khóa ô tên để người dùng không sửa (đảm bảo đúng người)
            txtHoTen.setEditable(false);

            showAlert(AlertType.INFORMATION, "Tìm thấy", "Đã tìm thấy cư dân: " + found.getHoTen(), null);
        } else {
            // Không tìm thấy
            txtHoTen.clear();
            txtHoTen.setEditable(true);
            showAlert(AlertType.ERROR, "Không tìm thấy", "Không có cư dân nào với số CCCD này trong hệ thống.", "Vui lòng kiểm tra lại hoặc thêm cư dân này vào Nhân khẩu trước.");
        }
    }

    @FXML
    private void handleAdd() {
        String soCCCD = txtSoCCCD.getText();
        String hoTen = txtHoTen.getText();
        String diaChiTamTru = txtDiaChiTamTru.getText();
        String tuNgay = (dateTuNgay.getValue() != null) ? dateTuNgay.getValue().toString() : "";
        String denNgay = (dateDenNgay.getValue() != null) ? dateDenNgay.getValue().toString() : "";
        String ghiChu = txtGhiChu.getText();

        if (soCCCD.isEmpty() || hoTen.isEmpty() || diaChiTamTru.isEmpty() || tuNgay.isEmpty() || denNgay.isEmpty()) {
            showAlert(AlertType.WARNING, "Lỗi nhập liệu", "Vui lòng điền đầy đủ thông tin.", null);
            return;
        }

        // --- SỬA LỖI TẠI ĐÂY ---
        // Bỏ tham số 'null' thừa ở vị trí thứ 3. Chỉ truyền 6 tham số đúng với Model.
        TamTruModel model = new TamTruModel(soCCCD, hoTen, diaChiTamTru, dateTuNgay.getValue(), dateDenNgay.getValue(), ghiChu);

        // Gọi hàm UPDATE thay vì Insert
        boolean success = TamTruDAO.updateTamTru(model);

        if (success) {
            showAlert(AlertType.INFORMATION, "Thành công", "Đã cập nhật trạng thái Tạm trú!", null);
            loadDataToTable();
            handleReset();
        } else {
            showAlert(AlertType.ERROR, "Thất bại", "Không thể cập nhật. Hãy chắc chắn cư dân đã có trong hệ thống.", null);
        }
    }

    @FXML
    private void handleDelete() {
        TamTruModel selected = tableTamTru.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(AlertType.WARNING, "Chưa chọn dòng", "Vui lòng chọn người cần xóa.", null);
            return;
        }
        if (TamTruDAO.deleteBySoCCCD(selected.getSoCCCD())) {
            showAlert(AlertType.INFORMATION, "Thành công", "Đã xóa tạm trú.", null);
            loadDataToTable();
        } else {
            showAlert(AlertType.ERROR, "Lỗi", "Xóa thất bại.", null);
        }
    }

    @FXML
    private void handleReset() {
        txtSoCCCD.clear();
        txtHoTen.clear();
        txtHoTen.setEditable(true); // Mở khóa lại ô tên
        txtDiaChiTamTru.clear();
        dateTuNgay.setValue(null);
        dateDenNgay.setValue(null);
        txtGhiChu.clear();
    }

    private void showAlert(AlertType alertType, String title, String content, String details) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(content);
        if (details != null && !details.isEmpty()) {
            TextArea textArea = new TextArea(details);
            textArea.setEditable(false);
            textArea.setWrapText(true);
            alert.getDialogPane().setExpandableContent(textArea);
        }
        alert.showAndWait();
    }
}