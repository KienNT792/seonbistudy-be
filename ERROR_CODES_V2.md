# Error Codes Documentation - Seonbi Study System

## 📋 Tổng quan

Hệ thống Seonbi Study sử dụng mã lỗi có cấu trúc để dễ dàng xác định và xử lý các lỗi. 

## 🔢 Cấu trúc mã lỗi

```
[HỆ LỖI]-[SỐ]
    │      │
    │      └─ Mã số cụ thể (1001-9999)
    └──────── Hệ lỗi (CMN, AUTH, USR, CRS, etc.)
```

### Các hệ lỗi (Error Systems):

| Prefix | Hệ thống | Mô tả |
|--------|----------|-------|
| **CMN** | Common | Lỗi chung cho toàn hệ thống (validation, resource, system) |
| **AUTH** | Authentication | Lỗi xác thực và phân quyền |
| **USR** | User | Lỗi quản lý người dùng *(future)* |
| **CRS** | Course | Lỗi quản lý khóa học *(future)* |
| **LSN** | Lesson | Lỗi quản lý bài học *(future)* |
| **EXM** | Exam | Lỗi thi và bài kiểm tra *(future)* |

---

## 📚 COMMON ERRORS (CMN-xxxx)

Các lỗi chung được sử dụng xuyên suốt toàn bộ hệ thống.

### Validation & Input Errors (CMN-1xxx)

| Mã lỗi | HTTP Status | Thông báo | Sử dụng |
|--------|-------------|-----------|---------|
| **CMN-1001** | 400 Bad Request | Validation failed | Lỗi validation chung (kèm theo chi tiết) |
| **CMN-1002** | 400 Bad Request | Required field is missing | Thiếu trường bắt buộc |
| **CMN-1003** | 400 Bad Request | Invalid input format | Định dạng input không hợp lệ |
| **CMN-1004** | 400 Bad Request | Invalid parameter provided | Parameter không hợp lệ |
| **CMN-1005** | 400 Bad Request | Invalid request body | Request body không hợp lệ |

**Ví dụ response CMN-1001:**
```json
{
  "errorCode": "CMN-1001",
  "message": "Validation failed",
  "status": 400,
  "timestamp": "2025-10-27T10:30:00",
  "path": "/api/auth/register",
  "validationErrors": [
    {
      "field": "username",
      "message": "Username is required"
    },
    {
      "field": "email",
      "message": "Email should be valid"
    }
  ]
}
```

### Resource Errors (CMN-2xxx)

| Mã lỗi | HTTP Status | Thông báo | Sử dụng |
|--------|-------------|-----------|---------|
| **CMN-2001** | 404 Not Found | Resource not found | Không tìm thấy resource |
| **CMN-2002** | 409 Conflict | Resource already exists | Resource đã tồn tại |
| **CMN-2003** | 409 Conflict | Resource conflict occurred | Xung đột resource |

### System Errors (CMN-9xxx)

| Mã lỗi | HTTP Status | Thông báo | Sử dụng |
|--------|-------------|-----------|---------|
| **CMN-9001** | 500 Internal Server Error | Internal server error | Lỗi server chung |
| **CMN-9002** | 503 Service Unavailable | Service is temporarily unavailable | Service không khả dụng |
| **CMN-9003** | 500 Internal Server Error | Database error occurred | Lỗi database |
| **CMN-9004** | 502 Bad Gateway | External service error | Lỗi external service |

---

## 🔐 AUTHENTICATION ERRORS (AUTH-xxxx)

Các lỗi liên quan đến xác thực và phân quyền.

### Credential Errors (AUTH-1xxx)

| Mã lỗi | HTTP Status | Thông báo | Mô tả |
|--------|-------------|-----------|-------|
| **AUTH-1001** | 401 Unauthorized | Invalid username or password | Sai tên đăng nhập hoặc mật khẩu |
| **AUTH-1002** | 404 Not Found | User not found | Không tìm thấy người dùng |
| **AUTH-1003** | 403 Forbidden | Account is disabled | Tài khoản đã bị vô hiệu hóa |
| **AUTH-1004** | 403 Forbidden | Account is locked | Tài khoản đã bị khóa |
| **AUTH-1005** | 400 Bad Request | Username is already taken | Username đã tồn tại |
| **AUTH-1006** | 400 Bad Request | Email is already in use | Email đã được sử dụng |

**Ví dụ response:**
```json
{
  "errorCode": "AUTH-1001",
  "message": "Invalid username or password",
  "status": 401,
  "timestamp": "2025-10-27T10:30:00",
  "path": "/api/auth/login"
}
```

### Token Errors (AUTH-2xxx)

| Mã lỗi | HTTP Status | Thông báo | Mô tả |
|--------|-------------|-----------|-------|
| **AUTH-2001** | 401 Unauthorized | Invalid or malformed token | Token không hợp lệ |
| **AUTH-2002** | 401 Unauthorized | Token has expired | Token đã hết hạn |
| **AUTH-2003** | 401 Unauthorized | Authentication token is missing | Thiếu token |
| **AUTH-2004** | 401 Unauthorized | Token has been revoked | Token đã bị thu hồi |

### Password Errors (AUTH-3xxx)

| Mã lỗi | HTTP Status | Thông báo | Mô tả |
|--------|-------------|-----------|-------|
| **AUTH-3001** | 400 Bad Request | Password does not meet security requirements | Password không đủ mạnh |
| **AUTH-3002** | 400 Bad Request | Password confirmation does not match | Password confirm không khớp |
| **AUTH-3003** | 400 Bad Request | Old password is incorrect | Password cũ không đúng |

### OAuth2 Errors (AUTH-4xxx)

| Mã lỗi | HTTP Status | Thông báo | Mô tả |
|--------|-------------|-----------|-------|
| **AUTH-4001** | 400 Bad Request | OAuth2 provider authentication failed | Xác thực OAuth2 thất bại |
| **AUTH-4002** | 400 Bad Request | Email from OAuth2 provider is not verified | Email OAuth2 chưa xác thực |
| **AUTH-4003** | 500 Internal Server Error | Failed to retrieve user info from OAuth2 provider | Không lấy được thông tin user |
| **AUTH-4004** | 400 Bad Request | OAuth2 state parameter mismatch | State parameter không khớp |

### Permission/Authorization Errors (AUTH-5xxx)

| Mã lỗi | HTTP Status | Thông báo | Mô tả |
|--------|-------------|-----------|-------|
| **AUTH-5001** | 403 Forbidden | Access denied | Không có quyền truy cập |
| **AUTH-5002** | 403 Forbidden | Insufficient permissions to perform this action | Không đủ quyền |
| **AUTH-5003** | 403 Forbidden | This role is not allowed to perform this action | Role không được phép |

### Registration Errors (AUTH-6xxx)

| Mã lỗi | HTTP Status | Thông báo | Mô tả |
|--------|-------------|-----------|-------|
| **AUTH-6001** | 400 Bad Request | Username must be at least 3 characters | Username quá ngắn |
| **AUTH-6002** | 400 Bad Request | Username contains invalid characters | Username có ký tự không hợp lệ |
| **AUTH-6003** | 400 Bad Request | Invalid email format | Email không hợp lệ |
| **AUTH-6004** | 400 Bad Request | Invalid role specified | Role không hợp lệ |

---

## 🔧 Cách sử dụng trong code

### 1. Sử dụng SeonbiException

```java
// Auth errors
throw new SeonbiException(ErrorCode.AUTH_USERNAME_EXISTS);
throw new SeonbiException(ErrorCode.AUTH_INVALID_CREDENTIALS);

// Common errors
throw new SeonbiException(ErrorCode.CMN_RESOURCE_NOT_FOUND);
throw new SeonbiException(ErrorCode.CMN_VALIDATION_FAILED);

// Với custom message
throw new SeonbiException(ErrorCode.AUTH_USER_NOT_FOUND, "User ID: " + userId);

// Với cause
throw new SeonbiException(ErrorCode.CMN_DATABASE_ERROR, e);
```

### 2. Trong Service Layer

```java
@Service
public class AuthService {
    
    public void register(RegisterRequest request) {
        // Common validation error
        if (request.getUsername() == null) {
            throw new SeonbiException(ErrorCode.CMN_REQUIRED_FIELD_MISSING, "Username is required");
        }
        
        // Auth-specific error
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new SeonbiException(ErrorCode.AUTH_USERNAME_EXISTS);
        }
        
        // Password validation
        if (request.getPassword().length() < 6) {
            throw new SeonbiException(ErrorCode.AUTH_PASSWORD_TOO_WEAK);
        }
    }
}
```

### 3. Response tự động được xử lý bởi GlobalExceptionHandler

```json
{
  "errorCode": "AUTH-1005",
  "message": "Username is already taken",
  "status": 400,
  "timestamp": "2025-10-27T10:30:00",
  "path": "/api/auth/register"
}
```

---

## 🎯 Best Practices

### 1. Chọn đúng hệ lỗi

```java
// ✅ Tốt - Sử dụng CMN cho lỗi validation chung
throw new SeonbiException(ErrorCode.CMN_INVALID_INPUT_FORMAT);

// ✅ Tốt - Sử dụng AUTH cho lỗi auth cụ thể
throw new SeonbiException(ErrorCode.AUTH_TOKEN_EXPIRED);

// ❌ Tránh - Không dùng auth error cho lỗi chung
throw new SeonbiException(ErrorCode.AUTH_VALIDATION_ERROR); // Deprecated
```

### 2. Sử dụng CMN cho các lỗi xuất hiện ở nhiều module

```java
// ✅ Tốt - CMN cho resource not found (dùng ở mọi module)
public Course getCourse(Long id) {
    return courseRepository.findById(id)
        .orElseThrow(() -> new SeonbiException(ErrorCode.CMN_RESOURCE_NOT_FOUND));
}

public User getUser(Long id) {
    return userRepository.findById(id)
        .orElseThrow(() -> new SeonbiException(ErrorCode.CMN_RESOURCE_NOT_FOUND));
}
```

### 3. Tạo error code mới cho module cụ thể

```java
// Future: Course module
public enum ErrorCode {
    // Course Errors (CRS-xxxx)
    CRS_COURSE_NOT_FOUND("CRS-1001", "Course not found", HttpStatus.NOT_FOUND),
    CRS_ALREADY_ENROLLED("CRS-1002", "Already enrolled in this course", HttpStatus.CONFLICT),
    CRS_CAPACITY_FULL("CRS-1003", "Course capacity is full", HttpStatus.CONFLICT),
    
    // Lesson Errors (LSN-xxxx)
    LSN_LESSON_NOT_FOUND("LSN-1001", "Lesson not found", HttpStatus.NOT_FOUND),
    LSN_NOT_COMPLETED("LSN-1002", "Previous lesson not completed", HttpStatus.FORBIDDEN),
}
```

---

## 📊 Error Response Structure

Tất cả errors đều có cấu trúc chuẩn:

```json
{
  "errorCode": "CMN-1001",              // Mã lỗi
  "message": "Validation failed",        // Thông báo
  "status": 400,                         // HTTP status
  "timestamp": "2025-10-27T10:30:00",   // Timestamp
  "path": "/api/auth/register",         // Endpoint
  "validationErrors": [                  // Optional: Chi tiết validation
    {
      "field": "username",
      "message": "Username is required"
    }
  ]
}
```

---

## 🧪 Testing

### Test với cURL

```bash
# Test CMN-1001: Validation failed
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{}'

# Test AUTH-1005: Username exists
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username":"admin",
    "password":"password",
    "email":"new@email.com",
    "fullName":"Test User"
  }'

# Test AUTH-1001: Invalid credentials
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"wrong","password":"wrong"}'

# Test AUTH-2002: Token expired
curl -X GET http://localhost:8080/api/me \
  -H "Authorization: Bearer expired_token"
```

---

## 📝 Quy tắc đặt tên Error Code

### 1. Common Errors (CMN-xxxx)
- **1xxx**: Validation & Input errors
- **2xxx**: Resource errors  
- **9xxx**: System errors

### 2. Authentication Errors (AUTH-xxxx)
- **1xxx**: Credentials & Account errors
- **2xxx**: Token errors
- **3xxx**: Password errors
- **4xxx**: OAuth2 errors
- **5xxx**: Permission errors
- **6xxx**: Registration errors

### 3. Future Modules
```
USR-xxxx: User Management
CRS-xxxx: Course Management
LSN-xxxx: Lesson Management
EXM-xxxx: Exam Management
PAY-xxxx: Payment
NOT-xxxx: Notification
```

---

## 🚀 Migration từ error codes cũ

| Error Code cũ | Error Code mới | Lý do |
|---------------|----------------|-------|
| AUTH-5001 (Validation error) | CMN-1001 | Validation là lỗi chung |
| AUTH-5002 (Required field missing) | CMN-1002 | Dùng chung cho tất cả modules |
| AUTH-5003 (Invalid input) | CMN-1003 | Dùng chung |
| AUTH-9001 (Internal error) | CMN-9001 | System error chung |
| AUTH-9003 (Database error) | CMN-9003 | Database error chung |

---

## 📖 Summary

- **CMN-xxxx**: Lỗi chung cho toàn hệ thống (validation, resource, system)
- **AUTH-xxxx**: Lỗi authentication & authorization cụ thể
- **SeonbiException**: Exception class chung thay thế AuthException
- Format: `[HỆ]-[SỐ]` thay vì `AUTH-[SỐ]` cho tất cả

Thiết kế này giúp hệ thống dễ mở rộng và maintain hơn khi thêm các modules mới! 🎉
