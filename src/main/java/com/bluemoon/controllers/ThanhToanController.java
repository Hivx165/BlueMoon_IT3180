package com.bluemoon.controllers;

import com.bluemoon.dao.PhiSinhHoatDAO;
import com.bluemoon.models.PhiSinhHoatModel;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.text.NumberFormat;
import java.util.Locale;

public class ThanhToanController {

    @FXML private Label lblMaHoKhau;
    @FXML private Label lblThoiGian;
    @FXML private Label lblDien;
    @FXML private Label lblNuoc;
    @FXML private Label lblTongTien;
    @FXML private ComboBox<String> cbbPhuongThuc;

    // Các nút để khóa khi đang loading
    @FXML private Button btnConfirm;
    @FXML private Button btnCancel;

    // Hộp thoại loading
    @FXML private VBox loadingBox;

    private PhiSinhHoatModel currentBill;
    private boolean isPaid = false;

    @FXML
    public void initialize() {
        // CHỈ CÒN 2 PHƯƠNG THỨC ONLINE
        cbbPhuongThuc.getItems().addAll("Chuyển khoản ngân hàng (QR Code)", "Ví điện tử / Thẻ quốc tế");
        cbbPhuongThuc.getSelectionModel().selectFirst();
    }

    public void setBillData(PhiSinhHoatModel bill) {
        this.currentBill = bill;
        lblMaHoKhau.setText(bill.getMaHoKhau());
        lblThoiGian.setText(bill.getThang() + "/" + bill.getNam());

        NumberFormat vnFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        lblDien.setText(vnFormat.format(bill.getTienDien()) + " đ");
        lblNuoc.setText(vnFormat.format(bill.getTienNuoc()) + " đ");
        lblTongTien.setText(vnFormat.format(bill.getTongTien()) + " đ");
    }

    @FXML
    private void handleConfirm() {
        if (currentBill == null) return;

        // 1. Hiển thị Loading và Khóa nút
        loadingBox.setVisible(true);
        btnConfirm.setDisable(true);
        btnCancel.setDisable(true);

        // 2. Tạo một Task chạy ngầm (Giả lập độ trễ mạng)
        Task<Boolean> paymentTask = new Task<>() {
            @Override
            protected Boolean call() throws Exception {
                // Giả lập thời gian chờ kết nối ngân hàng (3 giây)
                Thread.sleep(3000);

                // Gọi Database thực tế
                return PhiSinhHoatDAO.pay(currentBill.getId());
            }
        };

        // 3. Xử lý khi Task hoàn thành
        paymentTask.setOnSucceeded(e -> {
            loadingBox.setVisible(false); // Ẩn loading

            if (paymentTask.getValue()) {
                isPaid = true;
                showSuccessAlert();
                closeWindow();
            } else {
                showErrorAlert();
                // Mở lại nút để thử lại
                btnConfirm.setDisable(false);
                btnCancel.setDisable(false);
            }
        });

        // Xử lý khi Task bị lỗi
        paymentTask.setOnFailed(e -> {
            loadingBox.setVisible(false);
            btnConfirm.setDisable(false);
            btnCancel.setDisable(false);
            showAlert(Alert.AlertType.ERROR, "Lỗi mạng", "Không thể kết nối đến máy chủ.");
        });

        // 4. Bắt đầu chạy luồng
        new Thread(paymentTask).start();
    }

    private void showSuccessAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Giao dịch thành công");
        alert.setHeaderText("Thanh toán hoàn tất!");
        alert.setContentText("Cảm ơn bạn đã thanh toán hóa đơn tháng " + currentBill.getThang() + "/" + currentBill.getNam());
        alert.showAndWait();
    }

    private void showErrorAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Giao dịch thất bại");
        alert.setHeaderText("Lỗi thanh toán");
        alert.setContentText("Không thể cập nhật dữ liệu. Vui lòng thử lại sau.");
        alert.showAndWait();
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) lblMaHoKhau.getScene().getWindow();
        stage.close();
    }

    public boolean isPaymentSuccess() {
        return isPaid;
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}