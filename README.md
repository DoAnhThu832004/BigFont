# Big Font (Modern Edition) 📱🔍

Ứng dụng hỗ trợ thay đổi kích thước chữ toàn hệ thống Android, tạo cỡ chữ tùy chỉnh và tích hợp kính lúp soi chữ với công nghệ **Jetpack Compose**, **Clean Architecture** và **Material 3**.

---

## 📖 Giới thiệu

**Big Font** được thiết kế đặc biệt nhằm hỗ trợ người lớn tuổi, người có thị lực kém hoặc bất kỳ ai muốn đọc văn bản trên điện thoại một cách thoải mái, rõ ràng mà không bị mỏi mắt.

### Tính năng chính:
- 🔤 **Đổi cỡ chữ toàn hệ thống**: Phóng to toàn bộ chữ trên điện thoại từ **100% đến 240%+** chỉ với một thao tác chạm.
- ✏️ **Tùy chỉnh cỡ chữ theo ý muốn (Custom Size)**: Kéo thanh trượt trực quan từ **70% đến 280%** với bản xem trước (Live Preview) theo thời gian thực. Các cỡ chữ tùy chỉnh được tự động lưu vào cơ sở dữ liệu Room để tái sử dụng.
- 🔍 **Kính lúp soi chữ (Magnifier)**: Sử dụng máy ảnh với thanh trượt phóng đại từ **1.0x đến 5.0x** cùng đèn flash trợ sáng, hỗ trợ đọc sách báo, bao bì thuốc, hóa đơn trong bóng tối.
- 🧩 **Widget màn hình chính (AppWidget)**: Tích hợp Jetpack Glance cho phép điều chỉnh cỡ chữ nhanh chóng ngay từ màn hình chính điện thoại.
- ⚙️ **Màn hình Cài đặt đầy đủ**: Đánh giá 5 sao, chia sẻ ứng dụng, gửi phản hồi kèm cấu hình thiết bị và chính sách bảo mật.
- 🛡️ **Giao diện chuẩn chống vỡ (Layout Safe)**: Khóa cố định mật độ hiển thị giao diện của ứng dụng (`LocalDensity`), đảm bảo các nút bấm và bố cục không bị phình to vỡ nát khi người dùng kích hoạt font cỡ lớn toàn máy.

---

## 🏗️ Kiến trúc & Công nghệ (Tech Stack)

Dự án áp dụng mô hình **Clean Architecture kết hợp MVI/MVVM**:

```
app/src/main/java/com/thupo/bigfont/
├── data/                      # Tầng Data: Giao tiếp với Android SDK và Lưu trữ
│   ├── local/                 # Room Database cho các cỡ font tự tạo
│   │   ├── dao/               # CustomFontDao (Room DAO)
│   │   ├── entity/            # CustomFontEntity (Room Entity)
│   │   └── AppDatabase.kt     # RoomDatabase
│   ├── pref/                  # SharedPreferences & DataStore
│   └── repository/            # FontScaleRepositoryImpl, UserPreferencesRepositoryImpl
│
├── domain/                    # Tầng Domain: Nghiệp vụ cốt lõi (Business Logic)
│   ├── model/                 # FontScaleItem
│   ├── repository/            # FontScaleRepository, UserPreferencesRepository
│   └── usecase/               # UseCases độc lập:
│       ├── ApplyFontScaleUseCase.kt
│       ├── CheckWritePermissionUseCase.kt
│       ├── GetFontScalesUseCase.kt
│       ├── SaveCustomFontUseCase.kt
│       ├── DeleteCustomFontUseCase.kt
│       ├── PrepareInitialDataUseCase.kt
│       └── CompleteIntroUseCase.kt
│
├── presentation/              # Tầng Presentation: UI & Quản lý State
│   ├── components/            # UI Components dùng chung (FontScaleCard, QuickActions,...)
│   ├── navigation/            # AppNavHost, Screen (Navigation Compose)
│   ├── screen/                # Các màn hình ứng dụng:
│   │   ├── splash/            # SplashScreen & SplashViewModel
│   │   ├── intro/             # IntroScreen & IntroViewModel (Onboarding)
│   │   ├── home/              # HomeScreen & HomeViewModel (Danh sách font, xin quyền)
│   │   ├── custom/            # CustomSizeScreen & CustomSizeViewModel (Slider font)
│   │   ├── setting/           # SettingScreen (Đánh giá, chia sẻ, phản hồi)
│   │   └── magnifier/         # MagnifierScreen (CameraX Kính lúp & đèn pin)
│   └── widget/                # Glance AppWidget cho màn hình chính
│
├── di/                        # Dependency Injection với Dagger Hilt
│   ├── DatabaseModule.kt      # Cung cấp Room Database & DAO
│   └── RepositoryModule.kt    # Binds Repository Interfaces sang Implementation
│
└── ui/theme/                  # Material3 Design System (Color, Typography, Theme)
```

### Thư viện & Công cụ chính:
- **UI Toolkit**: Jetpack Compose (BOM 2026.02.01), Material 3, Compose Navigation.
- **Dependency Injection**: Dagger Hilt `2.60.1`.
- **Database**: Android Jetpack Room `2.6.1` với bộ sinh mã **KSP Kotlin Code Generation** (`room.generateKotlin = true`).
- **Camera & Hardware**: AndroidX CameraX `1.4.1` (Camera2, Lifecycle, View).
- **Home Widget**: Jetpack Glance Material3 `1.1.1`.
- **Bất đồng bộ**: Kotlin Coroutines & Reactive Flow `1.9.0`.

---

## 🔐 Quyền hạn hệ thống (Permissions)

Để thực thi việc thay đổi cỡ chữ và vận hành kính lúp, ứng dụng cần các quyền sau:

1. `android.permission.WRITE_SETTINGS`: 
   - Quyền cấp hệ thống (Protected Permission) cho phép ứng dụng ghi đè cấu hình `Settings.System.FONT_SCALE`.
   - Ứng dụng tự động kiểm tra và hướng dẫn người dùng tới trang cấp quyền hệ thống nếu chưa được cho phép.
2. `android.permission.CAMERA`: 
   - Sử dụng cho tính năng Kính lúp phóng to tài liệu thực tế.

---

## 🚀 Hướng dẫn Cài đặt & Biên dịch (Build Guide)

### Yêu cầu môi trường:
- **Android Studio**: Ladybug / Koala hoặc mới hơn.
- **JDK**: Java 17 hoặc 21.
- **Android SDK**: `minSdk 26` (Android 8.0), `compileSdk 36`, `targetSdk 36`.

### Lệnh biên dịch bằng dòng lệnh:

```bash
# Di chuyển vào thư mục dự án
cd C:\Users\ASUS\AndroidStudioProjects\bigfont

# Dọn dẹp và biên dịch APK Debug
./gradlew clean assembleDebug

# Kiểm tra code Kotlin
./gradlew compileDebugKotlin
```

File APK sau khi build nằm tại:
`app/build/outputs/apk/debug/app-debug.apk`

---

## 🔄 Luồng điều hướng (Navigation Flow)

$$\text{Splash} \longrightarrow \begin{cases} \text{Intro (Lần đầu mở)} \longrightarrow \text{Home} \\ \text{Home (Các lần tiếp theo)} \end{cases} \rightleftarrows \begin{cases} \text{CustomSize (Tùy chỉnh cỡ chữ)} \\ \text{Setting (Cài đặt & Góp ý)} \\ \text{Magnifier (Kính lúp Camera)} \end{cases}$$

---

## 📝 Bản quyền & Đóng góp
Dự án được xây dựng và tối ưu lại dựa trên tiêu chuẩn phát triển Android hiện đại.
Mọi đóng góp và báo lỗi vui lòng liên hệ qua tính năng **Phản hồi** trong mục Cài đặt của ứng dụng.
