# 📅 DEVELOPMENT ROADMAP
## TechMart E-Commerce Ecosystem
**Phiên bản:** 1.0  
**Ngày tạo:** 2026-04-27

---

## TỔNG QUAN LỘ TRÌNH

| Giai đoạn | Thời gian | Nội dung | Trạng thái |
|:----------|:----------|:---------|:-----------|
| Phase 0 | Tuần 0 | Thiết lập dự án, cấu hình, Entity | ✅ Hoàn thành |
| Phase 1 | Tuần 1 | CRUD Category + Product | 🔄 Đang làm |
| Phase 2 | Tuần 2 | Wallet + Transaction | ⬜ Chưa bắt đầu |
| Phase 3 | Tuần 3 | Order + Checkout Logic | ⬜ Chưa bắt đầu |
| Phase 4 | Tuần 4 | Search + Filtering | ⬜ Chưa bắt đầu |
| Phase 5 | Tuần 5 | JWT Security + Role-based Access | ⬜ Chưa bắt đầu |
| Phase 6 | Tuần 6 | Admin Dashboard APIs + Testing | ⬜ Chưa bắt đầu |

---

## PHASE 0: Thiết lập dự án ✅

### Đã hoàn thành:
- [x] Khởi tạo Spring Boot project với Maven.
- [x] Cấu hình PostgreSQL (Supabase) trong `application.yml`.
- [x] Cấu hình HikariCP Connection Pool.
- [x] Thiết kế Entity: `BaseEntity`, `User`, `Wallet`, `Category`, `Product`, `Order`, `OrderItem`, `Transaction`.
- [x] Định nghĩa Enums: `Role`, `Status`, `Type`.
- [x] Cấu hình `SecurityConfig` (permitAll) để dễ test.

---

## PHASE 1: CRUD Category + Product 🔄

### Mục tiêu học:
- Hiểu mô hình Controller → Service → Repository.
- Làm quen với Spring Data JPA (save, findById, findAll, deleteById).
- Hiểu cách dùng DTO Request/Response.
- Hiểu Validation (`@Valid`, `@NotBlank`).

### Checklist:
- [x] `CategoryRepository` + `CategoryService` + `CategoryController`.
- [ ] `ProductRepository` — Kế thừa JpaRepository.
- [ ] `ProductRequest` DTO — (name, description, price, stockQuantity, categoryId, imageUrl).
- [ ] `ProductResponse` DTO — (id, name, description, price, stockQuantity, categoryName, vendorName, imageUrl, createdAt).
- [ ] `ProductService` Interface + `ProductServiceImpl`.
- [ ] `ProductController` — Các endpoint CRUD.
- [ ] Test toàn bộ bằng Postman.

### Kiến thức cần nắm:
- `JpaRepository` và các hàm tự sinh (findBy..., existsBy...).
- Cách dùng `@RequestBody`, `@PathVariable`, `@RequestParam`.
- Cách dùng `ResponseEntity` và HTTP Status Code.

---

## PHASE 2: Wallet + Transaction

### Mục tiêu học:
- Hiểu `@Transactional` — tất cả hoặc hủy tất cả.
- Hiểu `@Version` (Optimistic Locking).
- Ghi log giao dịch vào bảng Transaction.

### Checklist:
- [ ] `WalletRepository` + `TransactionRepository`.
- [ ] `WalletService` — Logic Deposit, xem số dư.
- [ ] `WalletController`.
- [ ] `TransactionResponse` DTO.
- [ ] Test nạp tiền và kiểm tra số dư.
- [ ] Test đồng thời 2 request nạp tiền (xem Optimistic Lock có hoạt động không).

### Kiến thức cần nắm:
- `@Transactional` annotation.
- `@Version` và cơ chế Optimistic Locking.
- `OptimisticLockingFailureException`.

---

## PHASE 3: Order + Checkout

### Mục tiêu học:
- Ghép nhiều Service lại để tạo luồng Checkout hoàn chỉnh.
- Xử lý Rollback khi lỗi giữa chừng.
- Hiểu `CascadeType.ALL` và quan hệ `@OneToMany`.

### Checklist:
- [ ] `OrderRepository` + `OrderItemRepository`.
- [ ] `CheckoutRequest` DTO — (items: [{productId, quantity}]).
- [ ] `OrderResponse` DTO — (gồm danh sách OrderItem).
- [ ] `OrderService.checkout()` — **Hàm quan trọng nhất dự án:**
  - Kiểm tra số dư.
  - Kiểm tra tồn kho (tất cả sản phẩm).
  - Trừ tiền, trừ tồn kho, tạo đơn, ghi log. Tất cả bọc trong @Transactional.
- [ ] `OrderController`.
- [ ] API xem danh sách đơn hàng.
- [ ] API hủy đơn hàng (REFUND + Cộng lại tồn kho).

### Kiến thức cần nắm:
- Thiết kế Service gọi Service khác.
- Atomic Transaction (Rollback khi lỗi).
- Snapshot giá (`priceAtPurchase`).

---

## PHASE 4: Search + Filtering

### Mục tiêu học:
- Viết API tìm kiếm linh hoạt với nhiều điều kiện.
- Phân trang với `Pageable`.

### Checklist:
- [ ] Tạo `ProductSearchCriteria` DTO (keyword, categoryId, minPrice, maxPrice, page, size, sort).
- [ ] Triển khai `JpaSpecificationExecutor<Product>` trong `ProductRepository`.
- [ ] Viết `ProductSpecification` — build các điều kiện lọc động.
- [ ] Tích hợp phân trang (`Page<Product>`, `Pageable`).
- [ ] Test tìm kiếm với nhiều tổ hợp filter.

### Kiến thức cần nắm:
- JPA `Specification` và `CriteriaBuilder`.
- `Pageable` và `Page<T>`.
- `@ModelAttribute`.

---

## PHASE 5: JWT Security

### Mục tiêu học:
- Hiểu flow xác thực Stateless (JWT).
- Phân quyền dựa trên Role.

### Checklist:
- [ ] Thêm dependency `jjwt` vào `pom.xml`.
- [ ] Viết `JwtProvider` (tạo token, parse token, lấy userId từ token).
- [ ] Viết `JwtAuthenticationFilter` (đọc header "Authorization: Bearer <token>").
- [ ] Viết `CustomUserDetailsService` (load User từ DB).
- [ ] Cập nhật `SecurityConfig` — phân quyền từng endpoint theo Role.
- [ ] Viết `AuthController` (Register, Login).
- [ ] Mã hóa password bằng `BCryptPasswordEncoder`.
- [ ] Test đăng nhập và gọi API có token.

### Kiến thức cần nắm:
- Cấu trúc JWT (Header.Payload.Signature).
- `OncePerRequestFilter`.
- `SecurityFilterChain`, `@PreAuthorize`.

---

## PHASE 6: Admin + Testing

### Mục tiêu học:
- Viết query thống kê bằng `@Query` (Native SQL).
- Viết Unit Test với JUnit 5 + Mockito.

### Checklist:
- [ ] API thống kê doanh thu (SUM, GROUP BY).
- [ ] API quản lý người dùng.
- [ ] `GlobalExceptionHandler` — Xử lý ngoại lệ chuyên nghiệp.
- [ ] Viết Unit Test cho `CategoryService`.
- [ ] Viết Unit Test cho `OrderService.checkout()`.
- [ ] Viết Integration Test cho luồng checkout end-to-end.
