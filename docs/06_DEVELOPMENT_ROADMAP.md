# 📅 DEVELOPMENT ROADMAP — TechMart Backend
**Phiên bản:** 2.0 (Cập nhật 28/04/2026)  
**Triết lý:** Mỗi Phase học một nhóm kỹ năng cốt lõi. Phase sau XÂY TRÊN NỀN Phase trước.

---

## TỔNG QUAN TIẾN ĐỘ

| Phase | Tên gọi | Kiến thức cốt lõi | Trạng thái |
|:------|:--------|:-------------------|:-----------|
| 0 | Nền móng | Entity, JPA Config, Project Structure | ✅ Hoàn thành |
| 1 | CRUD cơ bản | Controller → Service → Repository, DTO, Validation | ✅ Hoàn thành |
| 2 | Exception Handling & API Docs | @RestControllerAdvice, Swagger | ✅ Hoàn thành |
| 3 | Wallet & Transaction | @Transactional, @Version, Atomic Operations | ⬜ Tiếp theo |
| 4 | Order & Checkout | Service gọi Service, Rollback, CascadeType | ⬜ |
| 5 | Search & Pagination | JPA Specification, Pageable, @ModelAttribute | ⬜ |
| 6 | Authentication | JWT, Spring Security, BCrypt | ⬜ |
| 7 | Authorization & Admin | @PreAuthorize, Role-based Access, Statistics Query | ⬜ |
| 8 | Testing & Polish | JUnit 5, Mockito, Integration Test | ⬜ |

---

## ✅ PHASE 0: Nền móng (ĐÃ HOÀN THÀNH)

### Những gì đã làm:
- [x] Khởi tạo Spring Boot project (Maven, Java 21)
- [x] Cấu hình PostgreSQL (Supabase) trong `application.yml`
- [x] Tạo `BaseEntity` (id UUID, createdAt, updatedAt, **isDeleted**)
- [x] Tạo tất cả Entity: `User`, `Wallet`, `Category`, `Product`, `Order`, `OrderItem`, `Transaction`
- [x] Định nghĩa Enum: `Role`, `Status`, `Type`
- [x] Cấu hình `SecurityConfig` (permitAll tạm thời)

### Kiến thức đã học:
- `@Entity`, `@Table`, `@Column`, `@Id`, `@GeneratedValue(UUID)`
- `@MappedSuperclass` — Kế thừa Entity
- `@ManyToOne`, `@OneToMany`, `@OneToOne` — Quan hệ giữa các bảng
- `@Version` — Optimistic Locking
- `@CreationTimestamp`, `@UpdateTimestamp` — Tự động gán thời gian
- `ddl-auto: update` — JPA tự tạo bảng từ Entity

---

## ✅ PHASE 1: CRUD cơ bản (ĐÃ HOÀN THÀNH)

### Những gì đã làm:
- [x] CRUD Category (Controller + Service + Repository + DTO)
- [x] CRUD Product (Controller + Service + Repository + DTO)
- [x] Soft Delete Pattern (BaseEntity + @SQLDelete + @SQLRestriction)

### Kiến thức đã học:
- **Kiến trúc 3 lớp:** Controller → Service (Interface + Impl) → Repository
- **DTO Pattern:** Tách biệt Request (đầu vào) và Response (đầu ra)
- **JPA Repository:** `save()`, `findById()`, `findAll()`, `deleteById()`, `existsByName()`
- **Controller Annotations:** `@RestController`, `@RequestMapping`, `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping`
- **Nhận dữ liệu:** `@RequestBody`, `@PathVariable`, `@RequestParam`
- **Validation:** `@Valid`, `@NotBlank`, `@NotNull`, `@DecimalMin`, `@Min`
- **ResponseEntity:** Kiểm soát HTTP Status Code (200, 201, 204)
- **Soft Delete:** `@SQLDelete`, `@SQLRestriction` — Xóa mềm thay vì xóa cứng
- **Lombok:** `@Getter`, `@Setter`, `@RequiredArgsConstructor`, `@Builder`

---

## ✅ PHASE 2: Exception Handling & API Documentation (ĐÃ HOÀN THÀNH)

### Những gì đã làm:
- [x] Tạo Custom Exception: `ResourceNotFoundException`, `BadRequestException`
- [x] Tạo `GlobalExceptionHandler` (@RestControllerAdvice)
- [x] Cấu hình Swagger/OpenAPI (`OpenAPIConfig`)
- [x] Cấu hình CORS (`CorsConfig`)

### Kiến thức đã học:
- **@RestControllerAdvice:** Bắt mọi Exception ở một chỗ duy nhất
- **@ExceptionHandler:** Map từng loại Exception → HTTP Status Code
- **Custom Exception:** Tạo RuntimeException tùy chỉnh với message rõ ràng
- **Swagger (@Tag, @Operation):** Tự sinh tài liệu API tương tác
- **CORS:** Cho phép Frontend (khác domain) gọi API Backend

---

## ⬜ PHASE 3: Wallet & Transaction (LÀM TIẾP THEO)

### Mục tiêu:
Xây dựng hệ thống Ví điện tử — Nạp tiền, Xem số dư, Lịch sử giao dịch.  
**Phase này dạy bạn cách xử lý tiền bạc một cách an toàn tuyệt đối.**

### Checklist:
- [ ] `WalletRepository` + `TransactionRepository`
- [ ] `WalletResponse` DTO (userId, balance, updatedAt)
- [ ] `TransactionResponse` DTO (amount, type, description, createdAt)
- [ ] `WalletDepositRequest` DTO (amount — có @DecimalMin validation)
- [ ] `WalletService` Interface + `WalletServiceImpl`:
  - [ ] `getBalance()` — Xem số dư ví của user
  - [ ] `deposit()` — Nạp tiền (cộng tiền vào balance + ghi Transaction log)
  - [ ] `getTransactionHistory()` — Xem lịch sử giao dịch
- [ ] `WalletController` — 3 endpoint trên
- [ ] Test trường hợp: Nạp số tiền âm, nạp 0 đồng, nạp bình thường
- [ ] Test đồng thời 2 request nạp tiền (kiểm tra Optimistic Lock)

### 📚 Kiến thức cần học:

#### 1. `@Transactional` — "Tất cả hoặc hủy tất cả"
```java
@Transactional
public WalletResponse deposit(UUID userId, BigDecimal amount) {
    Wallet wallet = walletRepository.findByUserId(userId)...;
    wallet.setBalance(wallet.getBalance() + amount);  // Bước 1: Cộng tiền
    walletRepository.save(wallet);                      // Bước 2: Lưu ví
    
    Transaction tx = new Transaction();
    tx.setWallet(wallet);
    tx.setAmount(amount);
    tx.setType(Type.DEPOSIT);
    transactionRepository.save(tx);                     // Bước 3: Ghi log

    return WalletResponse.fromEntity(wallet);
}
// Nếu Bước 3 lỗi → Bước 1 và 2 TỰ ĐỘNG ROLLBACK (tiền không cộng)
// Không có @Transactional → Bước 1+2 đã lưu, Bước 3 lỗi → Tiền cộng rồi nhưng KHÔNG có log → SAI
```

> **Học gì:** Hiểu khái niệm Transaction trong Database (ACID), cách Spring quản lý Transaction thông qua Proxy, khi nào cần dùng `@Transactional(readOnly = true)` để tối ưu hiệu năng đọc.

#### 2. `@Version` — Optimistic Locking (Chống Double Spending)
```
User A gửi request nạp 100k (ví đang có 500k, version=1)
User B gửi request nạp 200k (ví đang có 500k, version=1)
→ Nếu KHÔNG có @Version: cả 2 đều thấy 500k → cả 2 đều lưu → Kết quả có thể là 600k hoặc 700k (SAI)
→ Nếu CÓ @Version: A lưu thành công (version=2), B thấy version đã thay đổi → throw OptimisticLockException → Retry
```

> **Học gì:** Khái niệm Race Condition, Optimistic vs Pessimistic Locking, cách xử lý `OptimisticLockingFailureException` (retry hoặc báo lỗi).

#### 3. BigDecimal — Xử lý tiền chính xác
```java
// ❌ Double: 0.1 + 0.2 = 0.30000000000000004 (SAI)
// ✅ BigDecimal: 0.1 + 0.2 = 0.3 (ĐÚNG)
BigDecimal balance = wallet.getBalance();
BigDecimal newBalance = balance.add(amount); // KHÔNG dùng + mà dùng .add()
```

> **Học gì:** Tại sao KHÔNG BAO GIỜ dùng `Double` cho tiền, các phép toán BigDecimal (`.add()`, `.subtract()`, `.compareTo()`).

---

## ⬜ PHASE 4: Order & Checkout

### Mục tiêu:
Xây dựng luồng đặt hàng hoàn chỉnh — **Hàm phức tạp nhất dự án**.  
Một hàm `checkout()` phải phối hợp 3 bảng (Wallet, Product, Order) trong cùng 1 Transaction.

### Checklist:
- [ ] `OrderRepository` + `OrderItemRepository`
- [ ] `CheckoutRequest` DTO — chứa `List<OrderItemRequest>` (productId, quantity)
- [ ] `OrderResponse` DTO — chứa danh sách items, totalAmount, status
- [ ] `OrderService.checkout()`:
  - [ ] Kiểm tra tồn kho từng sản phẩm
  - [ ] Tính tổng tiền (price × quantity, dùng `priceAtPurchase` — snapshot giá)
  - [ ] Kiểm tra số dư ví
  - [ ] Trừ tiền, trừ tồn kho, tạo đơn hàng + OrderItems, ghi Transaction log
  - [ ] Tất cả bọc trong `@Transactional`
- [ ] API xem danh sách đơn hàng của tôi
- [ ] API xem chi tiết đơn hàng
- [ ] API hủy đơn hàng (REFUND tiền + cộng lại tồn kho)

### 📚 Kiến thức cần học:

#### 1. Service gọi Service — Thiết kế phụ thuộc
```java
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ProductService productService;   // Gọi service Product
    private final WalletService walletService;     // Gọi service Wallet
    // ...
}
```
> **Học gì:** Khi nào Service A nên gọi Service B (qua Interface) vs gọi trực tiếp Repository B. Nguyên tắc: Nếu cần dùng đến **business logic** (kiểm tra tồn kho, kiểm tra số dư) → gọi Service. Nếu chỉ cần **đọc raw data** → gọi Repository.

#### 2. Snapshot Price — Tại sao cần `priceAtPurchase`
```java
orderItem.setPriceAtPurchase(product.getPrice());
// Lưu giá tại thời điểm mua, KHÔNG dùng product.getPrice() khi xem lại đơn hàng
// Vì sau này Admin có thể thay đổi giá sản phẩm
```
> **Học gì:** Khái niệm Immutable Data trong E-commerce, tại sao đơn hàng KHÔNG BAO GIỜ JOIN trực tiếp bảng Product để lấy giá.

#### 3. CascadeType.ALL — Lưu cha tự động lưu con
```java
// Khi save(order) → JPA tự động save tất cả orderItems bên trong
@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
private List<OrderItem> items;
```
> **Học gì:** Các loại CascadeType (ALL, PERSIST, MERGE, REMOVE), orphanRemoval, `@Transactional` rollback khi lỗi giữa chừng.

#### 4. Luồng hủy đơn hàng (Reverse Transaction)
> **Học gì:** Cách thiết kế API idempotent (gọi nhiều lần cho cùng kết quả), cách kiểm tra trạng thái trước khi cho hủy (chỉ hủy khi PENDING).

---

## ⬜ PHASE 5: Search & Pagination

### Mục tiêu:
Xây dựng API tìm kiếm sản phẩm linh hoạt với nhiều bộ lọc (keyword, category, khoảng giá) + phân trang.

### Checklist:
- [ ] `ProductSearchCriteria` DTO (keyword, categoryId, minPrice, maxPrice)
- [ ] `ProductRepository extends JpaSpecificationExecutor<Product>`
- [ ] `ProductSpecification` — Build điều kiện lọc động
- [ ] Tích hợp `Pageable` + `Page<T>` vào response
- [ ] `PageResponse<T>` DTO chung (content, page, size, totalElements, totalPages)
- [ ] Cập nhật `ProductController` — endpoint `/search` dùng `@ModelAttribute`
- [ ] Test: tìm theo tên, lọc theo giá, lọc theo danh mục, kết hợp nhiều filter

### 📚 Kiến thức cần học:

#### 1. JPA Specification — Bộ lọc động
```java
// Thay vì viết 20 hàm: findByName(), findByPrice(), findByNameAndPrice()...
// Ta viết 1 Specification có thể kết hợp linh hoạt

public static Specification<Product> hasKeyword(String keyword) {
    return (root, query, cb) ->
        keyword == null ? null : cb.like(cb.lower(root.get("name")), "%" + keyword.toLowerCase() + "%");
}

public static Specification<Product> hasPriceBetween(BigDecimal min, BigDecimal max) {
    return (root, query, cb) -> {
        if (min != null && max != null) return cb.between(root.get("price"), min, max);
        if (min != null) return cb.greaterThanOrEqualTo(root.get("price"), min);
        if (max != null) return cb.lessThanOrEqualTo(root.get("price"), max);
        return null;
    };
}

// Kết hợp: Specification.where(hasKeyword("laptop")).and(hasPriceBetween(10M, 50M))
```
> **Học gì:** Criteria API, CriteriaBuilder, Root, Predicate — các thành phần cốt lõi của JPA dynamic query.

#### 2. Pageable & Page — Phân trang
```java
// Controller nhận: ?page=0&size=10&sort=price,asc
// Spring tự bind thành Pageable object
public Page<Product> search(Specification<Product> spec, Pageable pageable) {
    return productRepository.findAll(spec, pageable);
}
```
> **Học gì:** `PageRequest.of()`, `Sort.by()`, cấu trúc `Page<T>` (content, totalElements, totalPages).

#### 3. @ModelAttribute — Gom tham số URL vào Object
```java
// Thay vì 7 cái @RequestParam riêng lẻ
@GetMapping("/search")
public ResponseEntity<?> search(@ModelAttribute ProductSearchCriteria criteria, Pageable pageable) {
    // criteria.getKeyword(), criteria.getMinPrice()... được tự động bind
}
```
> **Học gì:** Sự khác biệt giữa `@RequestBody` (JSON body) và `@ModelAttribute` (Query String parameters).

---

## ⬜ PHASE 6: Authentication (Xác thực)

### Mục tiêu:
Triển khai hệ thống đăng ký/đăng nhập bằng JWT. Sau Phase này, mọi API đều cần token để gọi.

### Checklist:
- [ ] Thêm dependency `jjwt` vào `pom.xml`
- [ ] `UserRepository` — `findByUsername()`, `existsByUsername()`, `existsByEmail()`
- [ ] `AuthService` — Register + Login
- [ ] `JwtProvider` — Tạo token, parse token, validate token
- [ ] `JwtAuthenticationFilter extends OncePerRequestFilter`
- [ ] `CustomUserDetailsService implements UserDetailsService`
- [ ] `BCryptPasswordEncoder` — Mã hóa password
- [ ] `AuthController` — `POST /auth/register`, `POST /auth/login`
- [ ] Cập nhật `SecurityConfig` — Gắn JwtFilter vào filter chain
- [ ] Test: Đăng ký → Đăng nhập → Dùng token gọi API

### 📚 Kiến thức cần học:

#### 1. JWT — JSON Web Token
```
eyJhbGciOiJIUzI1NiJ9.          ← Header (thuật toán mã hóa)
eyJ1c2VySWQiOiIxMjMifQ.        ← Payload (dữ liệu: userId, role, exp)
SflKxwRJSMeKKF2QT4fwpMeJf36POk ← Signature (chữ ký bảo mật)
```
> **Học gì:** Cấu trúc JWT 3 phần, Stateless Authentication (server không lưu session), Access Token vs Refresh Token, Token Expiration.

#### 2. Spring Security Filter Chain
```
Request → [JwtAuthenticationFilter] → [SecurityConfig] → Controller
                    ↓
           Đọc header "Authorization: Bearer <token>"
           Parse token → Lấy userId → Load User từ DB
           Gắn User vào SecurityContext
```
> **Học gì:** `OncePerRequestFilter`, `SecurityContextHolder`, `UsernamePasswordAuthenticationToken`, luồng filter chain hoạt động ra sao.

#### 3. BCrypt — Mã hóa password
```java
// Mã hóa khi đăng ký
String encoded = passwordEncoder.encode("matkhau123");
// → "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy"

// So sánh khi đăng nhập
passwordEncoder.matches("matkhau123", encoded); // → true
```
> **Học gì:** Tại sao KHÔNG BAO GIỜ lưu plaintext password, Hashing vs Encryption, Salt là gì.

---

## ⬜ PHASE 7: Authorization & Admin (Phân quyền)

### Mục tiêu:
Phân quyền API theo Role (ADMIN, VENDOR, CUSTOMER) + Xây dựng API thống kê cho Admin.

### Checklist:
- [ ] Cập nhật `SecurityConfig` — Phân quyền từng nhóm endpoint
- [ ] Dùng `@PreAuthorize("hasRole('ADMIN')")` cho các API Admin
- [ ] Kiểm tra quyền sở hữu: Vendor chỉ sửa/xóa sản phẩm của mình
- [ ] `SecurityUtils.getCurrentUserId()` — Lấy user hiện tại từ JWT
- [ ] Cập nhật `ProductService` — Gán vendorId từ JWT thay vì từ request
- [ ] `AdminService` — API thống kê doanh thu (SUM, GROUP BY)
- [ ] `AdminController` — GET /admin/stats
- [ ] Test phân quyền: Customer không thể gọi API Admin, Vendor A không thể sửa Product của Vendor B

### 📚 Kiến thức cần học:

#### 1. Method-level Security
```java
@PreAuthorize("hasRole('ADMIN')")
@GetMapping("/admin/stats")
public ResponseEntity<?> getStats() { ... }

@PreAuthorize("hasAnyRole('VENDOR', 'ADMIN')")
@PostMapping("/products")
public ResponseEntity<?> createProduct() { ... }
```
> **Học gì:** `@EnableMethodSecurity`, `@PreAuthorize`, `@PostAuthorize`, SpEL (Spring Expression Language).

#### 2. Ownership Check — Kiểm tra quyền sở hữu
```java
public void updateProduct(UUID productId, ProductRequest request) {
    Product product = productRepository.findById(productId)...;
    UUID currentUserId = SecurityUtils.getCurrentUserId();
    
    if (!product.getVendor().getId().equals(currentUserId)) {
        throw new ForbiddenException("Bạn không có quyền sửa sản phẩm này");
    }
    // ... tiếp tục update
}
```
> **Học gì:** IDOR (Insecure Direct Object Reference), Resource-level authorization.

#### 3. Native SQL cho Thống kê
```java
@Query(value = """
    SELECT DATE_TRUNC('month', o.created_at) AS month,
           SUM(o.total_amount) AS revenue
    FROM orders o
    WHERE o.status = 'DELIVERED'
    GROUP BY month
    ORDER BY month
    """, nativeQuery = true)
List<Object[]> getMonthlyRevenue();
```
> **Học gì:** `@Query` (JPQL vs Native SQL), khi nào dùng Native Query, cách map kết quả về DTO.

---

## ⬜ PHASE 8: Testing & Polish

### Mục tiêu:
Viết test tự động để đảm bảo code chạy đúng, refactor code cho sạch.

### Checklist:
- [ ] Unit Test cho `CategoryServiceImpl` (Mock Repository)
- [ ] Unit Test cho `WalletServiceImpl` (Test deposit, test insufficient balance)
- [ ] Unit Test cho `OrderServiceImpl.checkout()` (Test thành công + rollback)
- [ ] Integration Test: Luồng đăng nhập → tạo sản phẩm → checkout end-to-end
- [ ] Refactor: Tạo package `mapper/` nếu cần
- [ ] Review toàn bộ code, bổ sung comment, dọn dẹp import thừa

### 📚 Kiến thức cần học:

#### 1. JUnit 5 + Mockito
```java
@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    void createCategory_WhenNameExists_ShouldThrowException() {
        when(categoryRepository.existsByName("Laptop")).thenReturn(true);

        assertThrows(BadRequestException.class, () -> {
            categoryService.createCategory(new CategoryRequest("Laptop", "..."));
        });
    }
}
```
> **Học gì:** `@Mock` vs `@InjectMocks`, `when().thenReturn()`, `verify()`, `assertThrows()`, Given-When-Then pattern.

#### 2. @SpringBootTest — Integration Test
```java
@SpringBootTest
@AutoConfigureMockMvc
class OrderIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void fullCheckoutFlow() throws Exception {
        // 1. Tạo user + nạp tiền
        // 2. Tạo product
        // 3. Gọi checkout
        // 4. Kiểm tra: số dư giảm, tồn kho giảm, đơn hàng được tạo
    }
}
```
> **Học gì:** `MockMvc`, `@Transactional` trong test (auto rollback), `@TestMethodOrder`, Test Container cho PostgreSQL.

---

## 📊 BẢN ĐỒ KIẾN THỨC TỔNG THỂ

```
Phase 0-1: CRUD & JPA Basics
    │
    ├── Entity, Repository, DTO, Validation
    ├── Controller ↔ Service ↔ Repository
    └── Soft Delete Pattern
    
Phase 2: Error Handling
    │
    └── @RestControllerAdvice, Custom Exception, Swagger
    
Phase 3: Transaction Safety ★★★
    │
    ├── @Transactional (ACID)
    ├── @Version (Optimistic Locking)
    └── BigDecimal (Money handling)
    
Phase 4: Complex Business Logic ★★★★★
    │
    ├── Service orchestration (Service gọi Service)
    ├── Atomic multi-table operations
    ├── Snapshot data (priceAtPurchase)
    └── Reverse operations (Cancel/Refund)
    
Phase 5: Dynamic Query
    │
    ├── JPA Specification + CriteriaBuilder
    ├── Pageable + Page<T>
    └── @ModelAttribute
    
Phase 6-7: Security ★★★★
    │
    ├── JWT (Stateless Auth)
    ├── Spring Security Filter Chain
    ├── BCrypt Password Encoding
    ├── Role-based Authorization
    └── Ownership Check (IDOR prevention)
    
Phase 8: Quality Assurance
    │
    ├── Unit Test (JUnit 5 + Mockito)
    └── Integration Test (MockMvc)
```
