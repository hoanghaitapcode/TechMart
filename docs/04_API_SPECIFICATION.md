# 🌐 API SPECIFICATION
## TechMart E-Commerce Ecosystem
**Phiên bản:** 1.0  
**Ngày tạo:** 2026-04-27  
**Base URL:** `http://localhost:8081/api`

---

## 1. QUY ƯỚC CHUNG

### 1.1 Response Format
Tất cả API trả về JSON. Khi thành công, trả trực tiếp data. Khi lỗi, trả theo cấu trúc:
```json
{
    "status": 400,
    "error": "Bad Request",
    "message": "Tên danh mục đã tồn tại",
    "timestamp": "2026-04-27T10:30:00"
}
```

### 1.2 HTTP Status Codes
| Code | Ý nghĩa | Khi nào trả? |
|:-----|:--------|:-------------|
| 200 | OK | GET, PUT thành công |
| 201 | Created | POST tạo mới thành công |
| 204 | No Content | DELETE thành công |
| 400 | Bad Request | Validation lỗi, logic nghiệp vụ lỗi |
| 401 | Unauthorized | Chưa đăng nhập / Token hết hạn |
| 403 | Forbidden | Không có quyền truy cập |
| 404 | Not Found | Không tìm thấy tài nguyên |
| 409 | Conflict | Optimistic Lock conflict |
| 500 | Internal Server Error | Lỗi hệ thống |

### 1.3 Phân trang (Pagination)
Các API danh sách hỗ trợ tham số:
- `page` (int, default: 0): Trang hiện tại (0-indexed).
- `size` (int, default: 10): Số phần tử mỗi trang.
- `sort` (string): Trường sắp xếp (ví dụ: `price,asc` hoặc `createdAt,desc`).

---

## 2. AUTHENTICATION APIs

### 2.1 Đăng ký tài khoản
```
POST /api/auth/register
```
**Request Body:**
```json
{
    "username": "nguyenvana",
    "email": "vana@gmail.com",
    "password": "matkhau123",
    "role": "CUSTOMER"
}
```
**Response (201 Created):**
```json
{
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "username": "nguyenvana",
    "email": "vana@gmail.com",
    "role": "CUSTOMER",
    "createdAt": "2026-04-27T10:30:00"
}
```

### 2.2 Đăng nhập
```
POST /api/auth/login
```
**Request Body:**
```json
{
    "username": "nguyenvana",
    "password": "matkhau123"
}
```
**Response (200 OK):**
```json
{
    "accessToken": "eyJhbGciOiJIUzI1NiIs...",
    "tokenType": "Bearer",
    "expiresIn": 3600
}
```

---

## 3. CATEGORY APIs

### 3.1 Lấy tất cả danh mục
```
GET /api/categories
```
**Auth:** Không yêu cầu  
**Response (200 OK):**
```json
[
    {
        "id": "uuid-01",
        "name": "Laptop Gaming",
        "description": "Các dòng laptop hiệu năng cao",
        "createdAt": "2026-04-27T10:00:00",
        "updatedAt": "2026-04-27T10:00:00"
    }
]
```

### 3.2 Lấy chi tiết danh mục
```
GET /api/categories/{id}
```

### 3.3 Tạo danh mục mới
```
POST /api/categories
```
**Auth:** ADMIN only  
**Request Body:**
```json
{
    "name": "Phụ kiện máy tính",
    "description": "Chuột, bàn phím, tai nghe"
}
```

### 3.4 Cập nhật danh mục
```
PUT /api/categories/{id}
```
**Auth:** ADMIN only

### 3.5 Xóa danh mục
```
DELETE /api/categories/{id}
```
**Auth:** ADMIN only  
**Response:** 204 No Content

---

## 4. PRODUCT APIs

### 4.1 Lấy danh sách sản phẩm (có phân trang)
```
GET /api/products?page=0&size=10&sort=price,asc
```
**Auth:** Không yêu cầu  
**Response (200 OK):**
```json
{
    "content": [
        {
            "id": "uuid-p1",
            "name": "Laptop ASUS ROG Strix G15",
            "description": "Laptop gaming i7-12700H, RTX 4060",
            "price": 32990000,
            "stockQuantity": 15,
            "categoryName": "Laptop Gaming",
            "vendorName": "TechShop VN",
            "thumbnailUrl": "https://...",
            "images": [
                {
                    "id": "uuid-img-01",
                    "imageUrl": "https://...",
                    "isPrimary": true,
                    "sortOrder": 0
                }
            ],
            "variants": [
                {
                    "id": "uuid-variant-01",
                    "sku": "ROG-G15-BLACK-I7",
                    "price": 32990000,
                    "stockQuantity": 15
                }
            ],
            "createdAt": "2026-04-27T10:00:00"
        }
    ],
    "page": 0,
    "size": 10,
    "totalElements": 50,
    "totalPages": 5
}
```

### 4.2 Tìm kiếm sản phẩm
```
GET /api/products/search?keyword=laptop&categoryId=uuid-01&minPrice=10000000&maxPrice=50000000&sort=price,asc&page=0&size=10
```
**Auth:** Không yêu cầu  
**Tham số (tất cả đều optional):**
| Param | Kiểu | Mô tả |
|:------|:-----|:------|
| keyword | String | Tìm theo tên sản phẩm (LIKE %keyword%) |
| categoryId | UUID | Lọc theo danh mục |
| minPrice | Decimal | Giá tối thiểu |
| maxPrice | Decimal | Giá tối đa |
| sort | String | Sắp xếp: `price,asc` / `createdAt,desc` |
| page | int | Trang (default: 0) |
| size | int | Kích thước trang (default: 10) |

### 4.3 Tạo sản phẩm mới
```
POST /api/products
```
**Auth:** VENDOR, ADMIN  
**Request Body:**
```json
{
    "name": "Laptop ASUS ROG Strix G15",
    "description": "Laptop gaming i7-12700H, RTX 4060",
    "price": 32990000,
    "stockQuantity": 15,
    "categoryId": "uuid-cat-01"
}
```

> Ảnh sản phẩm không còn truyền bằng `imageUrl` trong `ProductRequest`. Product được tạo trước, sau đó upload ảnh qua Product Image API.

### 4.4 Cập nhật sản phẩm
```
PUT /api/products/{id}
```
**Auth:** Vendor sở hữu sản phẩm, ADMIN

### 4.5 Xóa sản phẩm
```
DELETE /api/products/{id}
```
**Auth:** Vendor sở hữu sản phẩm, ADMIN

### 4.6 Upload ảnh sản phẩm
```
POST /api/products/{productId}/images
Content-Type: multipart/form-data
```
**Auth:** Vendor sở hữu sản phẩm, ADMIN

**Form-data:**
| Field | Kiểu | Bắt buộc | Mô tả |
|:------|:-----|:---------|:------|
| file | MultipartFile | Có | Ảnh jpg/png/webp |
| altText | String | Không | Text mô tả ảnh |
| sortOrder | Integer | Không | Thứ tự hiển thị trong slider |
| isPrimary | Boolean | Không | Đặt làm ảnh thumbnail |

**Response (201 Created):**
```json
{
    "id": "uuid-img-01",
    "imageUrl": "https://res.cloudinary.com/.../iphone-black.jpg",
    "altText": "iPhone màu đen",
    "sortOrder": 0,
    "isPrimary": true
}
```

### 4.7 Quản lý option group
```
POST /api/products/{productId}/option-groups
```
**Auth:** Vendor sở hữu sản phẩm, ADMIN

**Request Body:**
```json
{
    "name": "Color",
    "sortOrder": 0
}
```

```
POST /api/products/{productId}/option-groups/{groupId}/values
```
**Request Body:**
```json
{
    "value": "Black",
    "sortOrder": 0
}
```

### 4.8 Quản lý product variants
```
POST /api/products/{productId}/variants
```
**Auth:** Vendor sở hữu sản phẩm, ADMIN

**Request Body:**
```json
{
    "sku": "IPHONE15-BLACK-128",
    "price": 22990000,
    "stockQuantity": 20,
    "imageId": "uuid-img-01",
    "optionValueIds": [
        "uuid-option-black",
        "uuid-option-128gb"
    ]
}
```

**Response (201 Created):**
```json
{
    "id": "uuid-variant-01",
    "sku": "IPHONE15-BLACK-128",
    "price": 22990000,
    "stockQuantity": 20,
    "image": {
        "id": "uuid-img-01",
        "imageUrl": "https://res.cloudinary.com/.../iphone-black.jpg"
    },
    "options": [
        { "groupName": "Color", "value": "Black" },
        { "groupName": "Storage", "value": "128GB" }
    ]
}
```

Business rules:

- SKU unique trong phạm vi product.
- Một variant không được chọn 2 value trong cùng 1 option group.
- Không cho tạo 2 variants có cùng tổ hợp option values.
- Nếu product có variants, Cart API phải gửi `variantId`.

---

## 5. WALLET APIs

### 5.1 Xem số dư ví
```
GET /api/wallet/balance
```
**Auth:** Đã đăng nhập (lấy user từ JWT token)  
**Response (200 OK):**
```json
{
    "userId": "uuid-user-01",
    "balance": 5000000.0,
    "updatedAt": "2026-04-27T10:00:00"
}
```

### 5.2 Nạp tiền
```
POST /api/wallet/deposit
```
**Auth:** CUSTOMER, VENDOR  
**Request Body:**
```json
{
    "amount": 1000000
}
```
**Response (200 OK):**
```json
{
    "walletId": "uuid-wallet-01",
    "transactionType": "DEPOSIT",
    "amount": 1000000,
    "newBalance": 6000000.0,
    "description": "Nạp tiền vào ví",
    "createdAt": "2026-04-27T10:05:00"
}
```

### 5.3 Rút tiền (Vendor)
```
POST /api/wallet/withdraw
```
**Auth:** VENDOR only  
**Request Body:**
```json
{
    "amount": 500000
}
```

### 5.4 Xem lịch sử giao dịch
```
GET /api/wallet/transactions?page=0&size=20
```
**Auth:** Đã đăng nhập (chỉ xem giao dịch của ví chính mình)

---

## 6. ORDER APIs

### 6.1 Đặt hàng (Checkout)
```
POST /api/orders/checkout
```
**Auth:** CUSTOMER  
**Request Body:**
```json
{
    "items": [
        {
            "productId": "uuid-product-01",
            "variantId": "uuid-variant-01",
            "quantity": 2
        },
        {
            "productId": "uuid-product-02",
            "quantity": 1
        }
    ]
}
```
**Response (201 Created):**
```json
{
    "orderId": "uuid-order-01",
    "status": "PENDING",
    "totalAmount": 75980000,
    "items": [
        {
            "productName": "Laptop ASUS ROG Strix G15",
            "variantSku": "ROG-G15-BLACK-I7",
            "selectedOptions": "Color: Black, CPU: i7",
            "thumbnailUrl": "https://res.cloudinary.com/.../rog-black.jpg",
            "quantity": 2,
            "priceAtPurchase": 32990000,
            "subtotal": 65980000
        },
        {
            "productName": "Chuột Logitech G502",
            "quantity": 1,
            "priceAtPurchase": 10000000,
            "subtotal": 10000000
        }
    ],
    "walletBalanceAfter": 4020000,
    "createdAt": "2026-04-27T10:10:00"
}
```

> Nếu product có variants, request checkout/cart phải truyền `variantId`. Nếu product không có variants, `variantId` có thể bỏ trống và hệ thống dùng giá/tồn kho trên Product.

### 6.2 Xem đơn hàng của tôi
```
GET /api/orders/my-orders?page=0&size=10
```
**Auth:** CUSTOMER

### 6.3 Xem chi tiết đơn hàng
```
GET /api/orders/{id}
```
**Auth:** Customer sở hữu đơn, Vendor có sản phẩm trong đơn, Admin

### 6.4 Cập nhật trạng thái đơn hàng
```
PUT /api/orders/{id}/status
```
**Auth:** VENDOR, ADMIN  
**Request Body:**
```json
{
    "status": "SHIPPING"
}
```

### 6.5 Hủy đơn hàng
```
PUT /api/orders/{id}/cancel
```
**Auth:** CUSTOMER (chỉ khi status = PENDING)

---

## 7. ADMIN APIs

### 7.1 Thống kê doanh thu
```
GET /api/admin/stats?period=monthly&from=2026-01-01&to=2026-04-30
```
**Auth:** ADMIN only  
**Response (200 OK):**
```json
{
    "totalRevenue": 150000000,
    "totalOrders": 320,
    "totalUsers": 150,
    "revenueByMonth": [
        { "month": "2026-01", "revenue": 30000000 },
        { "month": "2026-02", "revenue": 35000000 },
        { "month": "2026-03", "revenue": 40000000 },
        { "month": "2026-04", "revenue": 45000000 }
    ]
}
```

### 7.2 Quản lý người dùng
```
GET /api/admin/users?page=0&size=20&role=VENDOR
```
**Auth:** ADMIN only
