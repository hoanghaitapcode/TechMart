```md
# PROJECT PROPOSAL: TECHMART E-COMMERCE ECOSYSTEM
## Hệ thống sàn thương mại điện tử tích hợp lõi tài chính

---

## 1. TỔNG QUAN DỰ ÁN (PROJECT OVERVIEW)

TechMart không chỉ là một ứng dụng mua bán trực tuyến thông thường mà là một hệ thống tích hợp giữa **E-commerce** và **E-wallet**.  

Dự án tập trung giải quyết các bài toán về:
- Hiệu năng cao  
- Tính toàn vẹn dữ liệu giao dịch  
- Khả năng mở rộng hệ thống  

**Lĩnh vực:** E-commerce & Fintech  
**Đối tượng sử dụng:**
- Khách hàng (Tenant)  
- Người bán (Landlord/Vendor)  
- Quản trị viên (Admin)  

**Mục tiêu cốt lõi:**
- Xây dựng hệ thống giao dịch an toàn  
- Xử lý tranh chấp hàng hóa (Concurrency)  
- Xây dựng bộ lọc tìm kiếm linh hoạt (Dynamic Filtering)  

---

## 2. KIẾN TRÚC KỸ THUẬT (TECHNICAL STACK)

Áp dụng **Clean Architecture** để đảm bảo khả năng bảo trì và mở rộng.

### 2.1 Backend (Java Ecosystem)

- **Framework:** Spring Boot 3.x (Java 17/21)  
- **Security:** Spring Security + Stateless JWT Authentication  
- **Data Access:**  
  - Spring Data JPA (Hibernate) cho CRUD  
  - JdbcTemplate / Native Query cho báo cáo tài chính  
- **Database:**  
  - PostgreSQL (Primary DB)  
  - Redis (Caching sản phẩm hot, danh mục)  

**Design Principles:**
- SOLID  
- Interface-driven design  
- DTO / Entity separation  

---

### 2.2 Frontend (Modern Web)

- **Framework:** ReactJS (Vite)  
- **State Management:** TanStack Query (React Query)  
- **UI/UX:** TailwindCSS + Shadcn/UI  

---

## 3. CÁC TÍNH NĂNG TRỌNG TÂM (CORE FEATURES)

### 3.1 Hệ thống Giao dịch & Ví điện tử (Financial Core)

- **Internal Wallet:**  
  Mỗi user có một ví nội bộ để thanh toán  

- **Atomic Transactions:**  
  Luồng xử lý:
```

Trừ tiền ví → Trừ tồn kho → Tạo đơn hàng

```
Nếu lỗi ở bất kỳ bước nào → rollback toàn bộ  

- **Concurrency Handling:**  
- Sử dụng Optimistic Locking (`@Version`)  
- Ngăn chặn overselling khi nhiều request đồng thời  

---

### 3.2 Bộ lọc tìm kiếm thông minh (Search Engine)

- **Reflective Dynamic Search:**  
- Sử dụng Java Reflection + JPA Specification  
- Hỗ trợ filter động:
  - Giá  
  - Loại  
  - Đánh giá  
  - Khoảng thời gian  
- Không cần viết lại query cho từng entity  

---

### 3.3 Quản trị & Thống kê (Admin Dashboard)

- **Financial Reporting:**  
- Biểu đồ doanh thu, dòng tiền  
- Real-time analytics  
- Thư viện: Recharts / Chart.js  

- **Resource Management:**  
- Quản lý sản phẩm  
- Duyệt yêu cầu rút tiền của Vendor  

---

## 4. CẤU TRÚC DỰ ÁN (PROJECT STRUCTURE)

```

techmart-root/
├── techmart-backend/
│   ├── config/             # Security, Redis, Swagger configs
│   ├── controller/         # Entry points (Rest APIs)
│   ├── service/            # Business Logic (Interface & Impl)
│   ├── repository/         # Data access layer
│   ├── entity/             # JPA Entities
│   ├── dto/                # Request/Response objects
│   └── exception/          # Global Exception Handling
│
└── techmart-frontend/
├── src/components/     # UI reusable components
├── src/hooks/          # Custom hooks (API calls)
└── src/pages/          # Main views (Home, Checkout, Dashboard)

```

---

## 5. LỘ TRÌNH PHÁT TRIỂN (ROADMAP)

### Giai đoạn 1 (Tuần 1-2)
- Thiết kế Database (chuẩn hóa)  
- Triển khai Authentication (JWT)  

### Giai đoạn 2 (Tuần 3)
- Xây dựng core E-commerce:
  - Product  
  - Category  
  - Shopping Cart  

### Giai đoạn 3 (Tuần 4)
- Triển khai:
  - Module ví điện tử  
  - Transaction handling  
  - Concurrency control  

### Giai đoạn 4 (Tuần 5)
- Phát triển Frontend Dashboard  
- Tích hợp API  

### Giai đoạn 5 (Tuần 6)
- Unit Test (JUnit 5 / Mockito)  
- Tối ưu query  
- Deploy lên Cloud  

---
```

---
