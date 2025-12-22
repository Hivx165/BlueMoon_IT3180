package com.bluemoon.controllers;

import com.bluemoon.models.PhiCoDinhModel; // Sử dụng Model phí cố định
import com.bluemoon.services.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class PhiDichVuController {

    // Khai báo các thành phần giao diện
    @FXML private TableView<PhiCoDinhModel> tablePhiDichVu;
    @FXML private TableColumn<PhiCoDinhModel, String> colMaHoKhau;
    @FXML private TableColumn<PhiCoDinhModel, Float> colDonGia;      // Giá phí (vnđ/m2 hoặc trọn gói)
    @FXML private TableColumn<PhiCoDinhModel, Float> colTongTien;    // Tiền phải nộp mỗi tháng
    @FXML private TableColumn<PhiCoDinhModel, Integer> colNam;

    // Ví dụ hiển thị trạng thái thu phí của quý 1 (3 tháng đầu)
    @FXML private TableColumn<PhiCoDinhModel, Float> colThang1;
    @FXML private TableColumn<PhiCoDinhModel, Float> colThang2;
    @FXML private TableColumn<PhiCoDinhModel, Float> colThang3;
    
    @FXML private TextField tfSearch; // Ô tìm kiếm

    private ObservableList<PhiCoDinhModel> listPhiDichVu = FXCollections.observableArrayList();

    public void initialize() {
        // 1. Cấu hình cột (phải khớp tên biến trong PhiCoDinhModel)
        colMaHoKhau.setCellValueFactory(new PropertyValueFactory<>("maHoKhau"));
        colDonGia.setCellValueFactory(new PropertyValueFactory<>("giaPhi"));
        colTongTien.setCellValueFactory(new PropertyValueFactory<>("tienNopMoiThang"));
        colNam.setCellValueFactory(new PropertyValueFactory<>("nam"));
        
        // Hiển thị số tiền đã đóng của các tháng (nếu 0 là chưa đóng, >0 là đã đóng)
        colThang1.setCellValueFactory(new PropertyValueFactory<>("thang1"));
        colThang2.setCellValueFactory(new PropertyValueFactory<>("thang2"));
        colThang3.setCellValueFactory(new PropertyValueFactory<>("thang3"));

        // 2. Tải dữ liệu
        loadData();
    }

    private void loadData() {
        listPhiDichVu.clear();
        // Giả sử tên bảng trong CSDL là 'phicodinh'
        String sql = "SELECT * FROM phicodinh";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                PhiCoDinhModel phi = new PhiCoDinhModel();
                phi.setId(rs.getInt("ID"));
                phi.setMaHoKhau(rs.getString("MaHoKhau"));
                phi.setGiaPhi(rs.getFloat("GiaPhi"));
                phi.setTienNopMoiThang(rs.getFloat("TienNopMoiThang"));
                phi.setNam(rs.getInt("Nam"));

                // Lấy dữ liệu các tháng
                phi.setThang1(rs.getFloat("Thang1"));
                phi.setThang2(rs.getFloat("Thang2"));
                phi.setThang3(rs.getFloat("Thang3"));
                // ... (Bạn có thể set tiếp Thang4 -> Thang12 nếu cần)

                listPhiDichVu.add(phi);
            }
            tablePhiDichVu.setItems(listPhiDichVu);

        } catch (Exception e) {
            e.printStackTrace();
            ControllerUtil.showError("Lỗi hệ thống", "Không thể tải danh sách phí dịch vụ: " + e.getMessage());
        }
    }

    // Chức năng tìm kiếm đơn giản theo Mã Hộ Khẩu
    @FXML
    private void handleSearch() {
        String keyword = tfSearch.getText().toLowerCase();
        ObservableList<PhiCoDinhModel> filteredList = FXCollections.observableArrayList();

        for (PhiCoDinhModel item : listPhiDichVu) {
            if (item.getMaHoKhau().toLowerCase().contains(keyword)) {
                filteredList.add(item);
            }
        }
        tablePhiDichVu.setItems(filteredList);
    }
    
    // Nút làm mới dữ liệu
    @FXML
    private void handleRefresh() {
        loadData();
    }
}