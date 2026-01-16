package com.bluemoon.controllers;

import com.bluemoon.dao.PhiSinhHoatDAO;
import com.bluemoon.models.PhiSinhHoatModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class PhiSinhHoatController {

    // ... (Các khai báo TableView, TableColumn giữ nguyên) ...
    @FXML private TableView<PhiSinhHoatModel> tablePhi;
    @FXML private TableColumn<PhiSinhHoatModel, String> colMaHoKhau;
    @FXML private TableColumn<PhiSinhHoatModel, Integer> colThang;
    @FXML private TableColumn<PhiSinhHoatModel, Integer> colNam;
    @FXML private TableColumn<PhiSinhHoatModel, Double> colDien;
    @FXML private TableColumn<PhiSinhHoatModel, Double> colNuoc;
    @FXML private TableColumn<PhiSinhHoatModel, Double> colTong;

    // Các TextField nhập liệu...
    @FXML private TextField txtMaHoKhau;
    @FXML private TextField txtThang;
    @FXML private TextField txtNam;
    @FXML private TextField txtTienDien;
    @FXML private TextField txtTienNuoc;

    private ObservableList<PhiSinhHoatModel> listPhi;

    @FXML
    public void initialize() {
        // Setup cột
        colMaHoKhau.setCellValueFactory(new PropertyValueFactory<>("maHoKhau"));
        colThang.setCellValueFactory(new PropertyValueFactory<>("thang"));
        colNam.setCellValueFactory(new PropertyValueFactory<>("nam"));
        colDien.setCellValueFactory(new PropertyValueFactory<>("tienDien"));
        colNuoc.setCellValueFactory(new PropertyValueFactory<>("tienNuoc"));
        colTong.setCellValueFactory(new PropertyValueFactory<>("tongTien"));

        loadData();
    }

    private void loadData() {
        // Gọi hàm getUnpaid để chỉ hiện những hóa đơn chưa đóng
        listPhi = FXCollections.observableArrayList(PhiSinhHoatDAO.getUnpaid());
        tablePhi.setItems(listPhi);
    }

    // --- HÀM THANH TOÁN MỚI (Mở cửa sổ) ---
    @FXML
    private void handlePay() {
        PhiSinhHoatModel selected = tablePhi.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(AlertType.WARNING, "Chọn dòng", "Vui lòng chọn hóa đơn để thanh toán.");
            return;
        }

        try {
            // Load giao diện ThanhToan.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/bluemoon/views/ThanhToan.fxml"));
            Parent root = loader.load();

            // Lấy Controller của cửa sổ thanh toán để truyền dữ liệu
            ThanhToanController controller = loader.getController();
            controller.setBillData(selected);

            // Tạo Stage mới (Cửa sổ con)
            Stage stage = new Stage();
            stage.setTitle("Thanh toán hóa đơn");
            stage.setScene(new Scene(root));

            // Chặn không cho bấm vào cửa sổ chính khi cửa sổ này đang mở (Modal)
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait(); // Chờ đến khi cửa sổ đóng lại

            // Sau khi cửa sổ đóng, kiểm tra xem đã thanh toán thành công chưa
            if (controller.isPaymentSuccess()) {
                // Nếu thành công, load lại bảng -> Hóa đơn sẽ biến mất vì status đã đổi
                loadData();
            }

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Lỗi", "Không thể mở cửa sổ thanh toán.");
        }
    }

    // --- Các hàm Thêm, Xóa giữ nguyên logic nhưng gọi loadData() ---

    @FXML
    private void handleAdd() {
        try {
            String maHK = txtMaHoKhau.getText();
            int thang = Integer.parseInt(txtThang.getText());
            int nam = Integer.parseInt(txtNam.getText());
            double dien = Double.parseDouble(txtTienDien.getText());
            double nuoc = Double.parseDouble(txtTienNuoc.getText());
            double tong = dien + nuoc;

            PhiSinhHoatModel model = new PhiSinhHoatModel(0, maHK, thang, nam, dien, nuoc, tong, 0);

            if (PhiSinhHoatDAO.insert(model)) {
                showAlert(AlertType.INFORMATION, "Thành công", "Đã tạo hóa đơn.");
                loadData(); // Load lại để hiện hóa đơn mới
                clearInput();
            } else {
                showAlert(AlertType.ERROR, "Lỗi", "Mã Hộ Khẩu không tồn tại!");
            }
        } catch (NumberFormatException e) {
            showAlert(AlertType.WARNING, "Lỗi", "Vui lòng nhập số hợp lệ.");
        }
    }

    @FXML
    private void handleDelete() {
        PhiSinhHoatModel selected = tablePhi.getSelectionModel().getSelectedItem();
        if (selected != null) {
            if (PhiSinhHoatDAO.delete(selected.getId())) {
                showAlert(AlertType.INFORMATION, "Xóa", "Đã xóa hóa đơn.");
                loadData();
            }
        }
    }

    private void clearInput() {
        txtMaHoKhau.clear(); txtThang.clear(); txtNam.clear();
        txtTienDien.clear(); txtTienNuoc.clear();
    }

    private void showAlert(AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}