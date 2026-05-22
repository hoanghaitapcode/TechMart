# 📋 SOFTWARE REQUIREMENT SPECIFICATION (SRS)
## TechMart E-Commerce Ecosystem
**Phiên bản:** 1.0  
**Ngày tạo:** 2026-04-27  
**Trạng thái:** Draft

---

## 1. GIỚI THIỆU (Introduction)

### 1.1 Mục đích tài liệu
Tài liệu này mô tả chi tiết các yêu cầu chức năng và phi chức năng của hệ thống TechMart — một sàn thương mại điện tử tích hợp ví điện tử nội bộ. Tài liệu phục vụ cho nhóm phát triển Backend (Spring Boot) và Frontend (ReactJS).

### 1.2 Phạm vi dự án
TechMart là hệ thống E-commerce cho phép:
- **Khách hàng (Customer):** Duyệt sản phẩm, đặt hàng, thanh toán bằng ví nội bộ.
- **Người bán (Vendor):** Đăng bán sản phẩm, quản lý đơn hàng, rút tiền từ ví.
- **Quản trị viên (Admin):** Quản lý toàn bộ hệ thống, duyệt yêu cầu, xem báo cáo tài chính.

### 1.3 Đối tượng người dùng (Actors)

| Actor | Mô tả | Vai trò hệ thống |
|:------|:-------|:------------------|
| **Customer** | Người mua hàng trên sàn | Duyệt sản phẩm, quản lý giỏ hàng, thanh toán, xem lịch sử đơn hàng |
| **Vendor** | Chủ cửa hàng trên sàn | Đăng/quản lý sản phẩm, xem đơn hàng của shop, rút tiền |
| **Admin** | Quản trị viên hệ thống | Quản lý người dùng, danh mục, duyệt yêu cầu rút tiền, xem báo cáo |

---

## 2. YÊU CẦU CHỨC NĂNG (Functional Requirements)

### 2.1 Module Xác thực & Phân quyền (Authentication & Authorization)

| ID | Yêu cầu | Mô tả chi tiết | Độ ưu tiên |
|:---|:---------|:----------------|:-----------|
| FR-AUTH-01 | Đăng ký tài khoản | Người dùng đăng ký với username, email, password. Hệ thống mã hóa password và tự động tạo Wallet (số dư = 0). | Cao |
| FR-AUTH-02 | Đăng nhập | Xác thực bằng username/password. Trả về JWT Token. | Cao |
| FR-AUTH-03 | Phân quyền theo Role | Hệ thống phân quyền 3 cấp: ADMIN > VENDOR > CUSTOMER. Mỗi API endpoint được giới hạn theo Role. | Cao |
| FR-AUTH-04 | Đăng xuất | Hủy phiên đăng nhập (Invalidate token). | Trung bình |

---

### 2.2 Module Quản lý Danh mục (Category Management)

| ID | Yêu cầu | Mô tả chi tiết | Độ ưu tiên |
|:---|:---------|:----------------|:-----------|
| FR-CAT-01 | Xem danh sách danh mục | Trả về toàn bộ danh mục sản phẩm. Không cần đăng nhập. | Cao |
| FR-CAT-02 | Xem chi tiết danh mục | Trả về thông tin 1 danh mục theo ID. | Cao |
| FR-CAT-03 | Tạo danh mục mới | Chỉ ADMIN. Tên danh mục không được trùng. | Cao |
| FR-CAT-04 | Cập nhật danh mục | Chỉ ADMIN. Sửa tên hoặc mô tả danh mục. | Trung bình |
| FR-CAT-05 | Xóa danh mục | Chỉ ADMIN. Không cho xóa nếu danh mục đang chứa sản phẩm. | Trung bình |

---

### 2.3 Module Quản lý Sản phẩm (Product Management)

| ID | Yêu cầu | Mô tả chi tiết | Độ ưu tiên |
|:---|:---------|:----------------|:-----------|
| FR-PRD-01 | Xem danh sách sản phẩm | Trả về danh sách sản phẩm. Hỗ trợ phân trang. Không cần đăng nhập. | Cao |
| FR-PRD-02 | Xem chi tiết sản phẩm | Trả về thông tin đầy đủ của 1 sản phẩm (kèm info danh mục, vendor). | Cao |
| FR-PRD-03 | Tìm kiếm & lọc sản phẩm | Hỗ trợ lọc theo: keyword, danh mục, khoảng giá, sắp xếp (giá, mới nhất). | Cao |
| FR-PRD-04 | Tạo sản phẩm mới | Chỉ VENDOR/ADMIN. Gồm: tên, mô tả, giá, số lượng tồn kho, ảnh, danh mục. | Cao |
| FR-PRD-05 | Cập nhật sản phẩm | Chỉ chủ sản phẩm (Vendor sở hữu) hoặc ADMIN. | Cao |
| FR-PRD-06 | Xóa sản phẩm | Chỉ chủ sản phẩm hoặc ADMIN. Không cho xóa nếu sản phẩm đang nằm trong đơn hàng chưa hoàn thành. | Trung bình |

---

### 2.3.1 Cap nhat Product Media & Variant Catalog

Product khong con chi co 1 truong anh dang string. Huong moi tach product thanh product cha, gallery anh va cac bien the ban duoc.

| ID | Yeu cau | Mo ta chi tiet | Do uu tien |
|:---|:--------|:---------------|:-----------|
| FR-PRD-07 | Quan ly gallery anh san pham | Vendor/Admin upload nhieu anh cho 1 product, dat anh primary, sap xep thu tu slider va xoa anh khong dung. | Cao |
| FR-PRD-08 | Quan ly option group | Vendor/Admin tao nhom lua chon cho product nhu Color, Storage, Size. | Cao |
| FR-PRD-09 | Quan ly option value | Vendor/Admin them gia tri cho tung option group nhu Black, Blue, 128GB, XL. | Cao |
| FR-PRD-10 | Quan ly product variant | Vendor/Admin tao SKU rieng theo to hop option values; moi variant co gia, ton kho va anh dai dien tuy chon. | Cao |
| FR-PRD-11 | Chon variant khi mua hang | Neu product co variants, Customer phai chon variant truoc khi them vao gio hoac checkout. | Cao |
| FR-PRD-12 | Snapshot variant trong don hang | OrderItem luu lai SKU, selectedOptions, gia va thumbnail tai thoi diem mua de lich su don hang khong bi thay doi khi Vendor sua product. | Cao |

Quy uoc nghiep vu:

- `ProductRequest` khong nhan `imageUrl`; anh duoc upload qua API rieng sau khi tao product.
- `Product.price` va `Product.stockQuantity` chi la fallback cho product khong co variants.
- Neu product co variants, cart/checkout dung `ProductVariant.price` va `ProductVariant.stockQuantity`.

---

### 2.4 Module Ví điện tử (Wallet & Transactions)

| ID | Yêu cầu | Mô tả chi tiết | Độ ưu tiên |
|:---|:---------|:----------------|:-----------|
| FR-WAL-01 | Xem số dư ví | Người dùng đã đăng nhập xem số dư ví của chính mình. | Cao |
| FR-WAL-02 | Nạp tiền vào ví | Người dùng nạp tiền vào ví (giả lập, không tích hợp cổng thanh toán thật). Ghi log vào bảng Transaction (type = DEPOSIT). | Cao |
| FR-WAL-03 | Rút tiền từ ví | Chỉ VENDOR. Gửi yêu cầu rút tiền. Admin duyệt mới trừ tiền. Ghi log Transaction (type = WITHDRAW). | Trung bình |
| FR-WAL-04 | Xem lịch sử giao dịch | Xem danh sách lịch sử nạp/rút/thanh toán/hoàn tiền của ví. Hỗ trợ phân trang. | Cao |

---

### 2.5 Module Đơn hàng (Order Management)

| ID | Yêu cầu | Mô tả chi tiết | Độ ưu tiên |
|:---|:---------|:----------------|:-----------|
| FR-ORD-01 | Đặt hàng (Checkout) | Customer chọn sản phẩm và số lượng → Hệ thống thực hiện **Atomic Transaction**: (1) Kiểm tra số dư ví, (2) Kiểm tra tồn kho, (3) Trừ tiền ví, (4) Trừ tồn kho, (5) Tạo đơn hàng, (6) Ghi log Transaction. Nếu bất kỳ bước nào lỗi → Rollback toàn bộ. | Cao |
| FR-ORD-02 | Xem đơn hàng của tôi | Customer xem danh sách đơn hàng đã đặt. | Cao |
| FR-ORD-03 | Xem chi tiết đơn hàng | Xem thông tin chi tiết 1 đơn hàng (sản phẩm, số lượng, tổng tiền, trạng thái). | Cao |
| FR-ORD-04 | Cập nhật trạng thái đơn hàng | Vendor/Admin cập nhật Status: PENDING → PAID → SHIPPING → DELIVERED. | Trung bình |
| FR-ORD-05 | Hủy đơn hàng | Customer hủy khi đơn còn ở trạng thái PENDING. Hệ thống hoàn tiền vào ví (REFUND) và cộng lại tồn kho. | Trung bình |

---

### 2.6 Module Quản trị & Thống kê (Admin Dashboard)

| ID | Yêu cầu | Mô tả chi tiết | Độ ưu tiên |
|:---|:---------|:----------------|:-----------|
| FR-ADM-01 | Thống kê doanh thu | ADMIN xem tổng doanh thu theo ngày/tuần/tháng. | Trung bình |
| FR-ADM-02 | Quản lý người dùng | ADMIN xem, khóa/mở khóa tài khoản người dùng. | Trung bình |
| FR-ADM-03 | Duyệt yêu cầu rút tiền | ADMIN duyệt hoặc từ chối yêu cầu rút tiền của Vendor. | Trung bình |

---

## 3. YÊU CẦU PHI CHỨC NĂNG (Non-Functional Requirements)

| ID | Loại | Yêu cầu | Mô tả |
|:---|:-----|:---------|:------|
| NFR-01 | Hiệu năng | Thời gian phản hồi API | API CRUD phải phản hồi trong vòng < 500ms. API Search < 1000ms. |
| NFR-02 | Bảo mật | Mã hóa mật khẩu | Password phải được mã hóa bằng BCrypt trước khi lưu DB. |
| NFR-03 | Bảo mật | Stateless Authentication | Xác thực bằng JWT, không dùng Session trên server. |
| NFR-04 | Toàn vẹn dữ liệu | Concurrency Control | Sử dụng Optimistic Locking (`@Version`) cho bảng Wallet và Product để chống race condition (overselling). |
| NFR-05 | Toàn vẹn dữ liệu | Atomic Transactions | Luồng checkout phải được bọc trong `@Transactional`. Bất kỳ lỗi nào → rollback toàn bộ. |
| NFR-06 | Khả dụng | Database Connection Pool | Sử dụng HikariCP với max-pool-size = 10. |
| NFR-07 | Khả mở rộng | Kiến trúc Controller-Service-Repository | Source code tuân thủ Clean Architecture. Mỗi layer chỉ phụ thuộc layer bên dưới. |

---

## 4. RÀNG BUỘC HỆ THỐNG (System Constraints)

| Ràng buộc | Chi tiết |
|:----------|:---------|
| Ngôn ngữ Backend | Java 21 |
| Framework Backend | Spring Boot 4.x |
| Database | PostgreSQL (Hosted trên Supabase) |
| Frontend | ReactJS (Vite) + TailwindCSS + Shadcn/UI |
| IDE | IntelliJ IDEA |
| Version Control | Git |
