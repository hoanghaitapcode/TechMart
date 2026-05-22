# 🏗️ ARCHITECTURE DESIGN
## TechMart E-Commerce Ecosystem
**Phiên bản:** 1.0  
**Ngày tạo:** 2026-04-27

---

## 1. KIẾN TRÚC TỔNG THỂ (System Architecture)

```mermaid
graph TB
    subgraph Frontend
        FE[ReactJS - Vite<br/>TailwindCSS + Shadcn/UI]
    end

    subgraph Backend ["Spring Boot Application"]
        direction TB
        CTRL[Controller Layer<br/>REST API Endpoints]
        SVC[Service Layer<br/>Business Logic]
        REPO[Repository Layer<br/>Data Access - JPA]
    end

    subgraph Infrastructure
        DB[(PostgreSQL<br/>Supabase Cloud)]
        CACHE[(Redis<br/>Cache Layer)]
    end

    FE -->|HTTP/JSON| CTRL
    CTRL --> SVC
    SVC --> REPO
    REPO --> DB
    SVC -.->|Optional| CACHE
```

---

## 2. KIẾN TRÚC 3 LỚP CHI TIẾT (Layered Architecture)

```mermaid
graph LR
    subgraph Controller ["🎯 Controller Layer"]
        direction TB
        C1[CategoryController]
        C2[ProductController]
        C3[OrderController]
        C4[WalletController]
        C5[AuthController]
        C6[AdminController]
        C7[ImageController]
        C8[ProductVariantController]
    end

    subgraph Service ["🧠 Service Layer"]
        direction TB
        S1[CategoryService]
        S2[ProductService]
        S3[OrderService]
        S4[WalletService]
        S5[AuthService]
        S6[AdminService]
        S7[ImageService]
        S8[ProductVariantService]
    end

    subgraph Repository ["📦 Repository Layer"]
        direction TB
        R1[CategoryRepository]
        R2[ProductRepository]
        R3[OrderRepository / OrderItemRepository]
        R4[WalletRepository / TransactionRepository]
        R5[UserRepository]
        R6[ProductImageRepository]
        R7[ProductVariantRepository / OptionRepository]
    end

    C1 --> S1 --> R1
    C2 --> S2 --> R2
    C3 --> S3 --> R3
    C4 --> S4 --> R4
    C5 --> S5 --> R5
    C6 --> S6
    C7 --> S7 --> R6
    C8 --> S8 --> R7

    S3 -.->|Gọi trừ tiền| S4
    S3 -.->|Gọi kiểm tra tồn kho| S2
```

---

Cap nhat Product Media & Variant:

- `ImageController`/`ImageService` quan ly upload, delete, set primary image cho ProductImage.
- `ProductVariantController`/`ProductVariantService` quan ly option groups, option values va variants.
- `OrderService` va `CartService` can doc variant de tinh gia, ton kho va snapshot selected options khi product co variants.
- `ProductService` chi nen quan ly product cha; khong nhan `imageUrl` trong `ProductRequest`.

## 3. CẤU TRÚC PACKAGE (Project Structure)

```
src/main/java/com/springboot/techmart/
│
├── TechMartBackendApplication.java          # Điểm khởi chạy ứng dụng
│
├── config/                                   # Cấu hình hệ thống
│   ├── SecurityConfig.java                   #   Cấu hình Spring Security
│   ├── JwtConfig.java                        #   Cấu hình JWT properties
│   └── CorsConfig.java                       #   Cho phép Frontend gọi API cross-origin
│
├── controller/                               # REST API Endpoints
│   ├── AuthController.java                   #   POST /auth/register, /auth/login
│   ├── CategoryController.java               #   CRUD /categories
│   ├── ProductController.java                #   CRUD /products + /products/search
│   ├── OrderController.java                  #   POST /orders/checkout, GET /orders/my-orders
│   ├── WalletController.java                 #   GET /wallet/balance, POST /wallet/deposit
│   └── AdminController.java                  #   GET /admin/stats, /admin/users
│
├── service/                                  # Giao diện Service (Interface)
│   ├── AuthService.java
│   ├── CategoryService.java
│   ├── ProductService.java
│   ├── OrderService.java
│   ├── WalletService.java
│   └── AdminService.java
│
├── service/impl/                             # Cài đặt Service (Implementation)
│   ├── AuthServiceImpl.java
│   ├── CategoryServiceImpl.java
│   ├── ProductServiceImpl.java
│   ├── OrderServiceImpl.java                 #   Chứa logic Checkout phức tạp nhất
│   ├── WalletServiceImpl.java
│   └── AdminServiceImpl.java
│
├── repository/                               # Data Access Layer (JPA)
│   ├── UserRepository.java
│   ├── CategoryRepository.java
│   ├── ProductRepository.java
│   ├── OrderRepository.java
│   ├── OrderItemRepository.java
│   ├── WalletRepository.java
│   └── TransactionRepository.java
│
├── entity/                                   # JPA Entities (Mapping Database)
│   ├── BaseEntity.java                       #   Lớp cha: id, createdAt, updatedAt
│   ├── User.java
│   ├── Wallet.java
│   ├── Category.java
│   ├── Product.java
│   ├── Order.java
│   ├── OrderItem.java
│   ├── Transaction.java
│   ├── Role.java                             #   Enum: ADMIN, VENDOR, CUSTOMER
│   ├── Status.java                           #   Enum: PENDING, PAID, SHIPPING, ...
│   └── Type.java                             #   Enum: DEPOSIT, WITHDRAW, PAYMENT, REFUND
│
├── dto/                                      # Data Transfer Objects
│   ├── request/                              #   Dữ liệu Client gửi lên
│   │   ├── CategoryRequest.java
│   │   ├── ProductRequest.java
│   │   ├── OrderRequest.java                 #   Chứa List<OrderItemRequest>
│   │   ├── LoginRequest.java
│   │   ├── RegisterRequest.java
│   │   └── WalletDepositRequest.java
│   │
│   └── response/                             #   Dữ liệu Server trả về
│       ├── CategoryResponse.java
│       ├── ProductResponse.java
│       ├── OrderResponse.java
│       ├── UserResponse.java
│       ├── WalletResponse.java
│       ├── TransactionResponse.java
│       └── AuthResponse.java                 #   Chứa JWT token
│
├── exception/                                # Xử lý ngoại lệ toàn cục
│   ├── GlobalExceptionHandler.java           #   @ControllerAdvice - Bắt mọi Exception
│   ├── ResourceNotFoundException.java        #   404 - Không tìm thấy
│   ├── BadRequestException.java              #   400 - Dữ liệu không hợp lệ
│   ├── InsufficientBalanceException.java     #   400 - Số dư không đủ
│   └── ConflictException.java                #   409 - Optimistic Lock conflict
│
└── security/                                 # Bảo mật (Triển khai cuối cùng)
    ├── JwtProvider.java                      #   Tạo và xác thực JWT Token
    ├── JwtAuthenticationFilter.java          #   Filter kiểm tra Token trong Header
    └── CustomUserDetailsService.java         #   Load User từ DB cho Spring Security
```

---

### 3.1 Package bo sung cho Product Media & Variant

Khi triển khai product media và variant, package structure cần bổ sung:

| Layer | Thành phần mới | Trách nhiệm |
|:------|:---------------|:------------|
| Controller | `ImageController` | Upload/delete/set primary product images |
| Controller | `ProductVariantController` | Quản lý option groups, option values, variants |
| Service | `ImageService`, `ImageServiceImpl` | Validate file, upload Cloudinary, lưu ProductImage |
| Service | `ProductVariantService`, `ProductVariantServiceImpl` | Validate SKU, tổ hợp option, giá/tồn kho variant |
| Repository | `ProductImageRepository` | Truy vấn ảnh theo product, primary image |
| Repository | `ProductVariantRepository` | Truy vấn variant theo product/SKU |
| Repository | `ProductOptionGroupRepository` | Truy vấn nhóm option |
| Repository | `ProductOptionValueRepository` | Truy vấn option values |
| Entity | `ProductImage` | Gallery/thumbnail cho Product |
| Entity | `ProductOptionGroup`, `ProductOptionValue` | Cấu hình option |
| Entity | `ProductVariant` | SKU bán được, có giá/tồn kho riêng |

---

## 4. LUỒNG XỬ LÝ REQUEST (Request Pipeline)

```mermaid
sequenceDiagram
    participant Client as 🖥️ Client (React/Postman)
    participant Filter as 🔐 JWT Filter
    participant Controller as 🎯 Controller
    participant Service as 🧠 Service
    participant Repo as 📦 Repository
    participant DB as 🗄️ Database

    Client->>Filter: HTTP Request + JWT Token
    Filter->>Filter: Xác thực Token
    alt Token hợp lệ
        Filter->>Controller: Cho phép đi tiếp
        Controller->>Controller: Kiểm tra @Valid (DTO Validation)
        Controller->>Service: Gọi hàm nghiệp vụ
        Service->>Repo: Truy vấn dữ liệu
        Repo->>DB: SQL Query
        DB-->>Repo: Kết quả
        Repo-->>Service: Entity
        Service->>Service: Xử lý logic, chuyển Entity → Response DTO
        Service-->>Controller: Response DTO
        Controller-->>Client: HTTP 200 + JSON
    else Token không hợp lệ
        Filter-->>Client: HTTP 401 Unauthorized
    end
```

---

## 5. NGUYÊN TẮC THIẾT KẾ

### 5.1 Quy tắc phụ thuộc giữa các Layer

```
Controller → Service → Repository → Entity
     ↑                                  ↑
     |                                  |
    DTO                            Database
```

**Quy tắc sắt đá:**
1. **Controller CHỈ ĐƯỢC gọi Service.** Không bao giờ gọi thẳng Repository.
2. **Service CHỈ ĐƯỢC gọi Repository.** Không bao giờ trả Entity ra ngoài, luôn chuyển sang DTO.
3. **Repository CHỈ NÓI CHUYỆN với Database.** Không chứa business logic.
4. **Entity KHÔNG BAO GIỜ lộ ra ngoài Controller.** Mọi dữ liệu vào/ra đều qua DTO.

### 5.2 Tại sao Service dùng Interface + Impl?
- **Interface (CategoryService):** Khai báo "hợp đồng" — Service này phải có những hàm gì. Code trong Controller chỉ phụ thuộc vào Interface, không biết bên trong Implementation viết gì.
- **Impl (CategoryServiceImpl):** Cài đặt cụ thể cho hợp đồng đó.

Lợi ích: Sau này nếu cần thay đổi logic hoàn toàn (ví dụ chuyển từ thanh toán nội bộ sang Momo), bạn chỉ cần viết một `WalletServiceMomoImpl` mới mà KHÔNG CẦN SỬA Controller. Đây là nguyên tắc **Dependency Inversion** trong SOLID.
