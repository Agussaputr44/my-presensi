# My-Presensi: Sistem Presensi Terintegrasi

![Laravel](https://img.shields.io/badge/Laravel-FF2D20?style=for-the-badge&logo=laravel&logoColor=white)
![Filament](https://img.shields.io/badge/Filament-FFA116?style=for-the-badge&logo=laravel&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![License](https://img.shields.io/badge/License-MIT-green.svg?style=for-the-badge)

## 📌 Deskripsi Proyek
**My-Presensi** adalah solusi manajemen kehadiran karyawan/mahasiswa yang terdiri dari dua komponen utama:
1.  **Dashboard Admin**: Platform berbasis web untuk mengelola data master, lokasi presensi, dan pemantauan statistik kehadiran.
2.  **Aplikasi Mobile**: Aplikasi Android untuk pengguna melakukan presensi secara mandiri dengan verifikasi lokasi (Geolocation).

## ✨ Fitur Utama
### 🖥️ Dashboard Admin (Web)
- **Manajemen Lokasi**: Pengaturan titik koordinat presensi dengan radius tertentu.
- **Manajemen Pengguna**: Pengelolaan data user (admin & pegawai).
- **Statistik & Overview**: Widget grafik untuk memantau tren kehadiran secara real-time.
- **Rekap Presensi**: Export data kehadiran ke format Excel/CSV.
- **Automated Check**: Fitur command otomatis untuk memeriksa status kehadiran harian.

### 📱 Aplikasi Mobile (Android)
- **Presensi Geolocation**: Melakukan presensi masuk/pulang hanya jika berada di radius lokasi yang ditentukan.
- **Histori Kehadiran**: Melihat riwayat presensi pribadi secara mendetail.
- **Manajemen Profil**: Mengelola data pribadi dan keamanan akun.
- **Keamanan Perangkat**: Pencatatan nama perangkat untuk validasi presensi.

## 🛠️ Teknologi yang Digunakan
### Backend & Admin (mypresensi-admin)
- **Framework**: [Laravel 11](https://laravel.com/)
- **Admin Panel**: [Filament PHP](https://filamentphp.com/) (TALL Stack)
- **Database**: MySQL / MariaDB
- **API**: RESTful API dengan Laravel Sanctum untuk autentikasi mobile.

### Mobile (mypresensi-mobile)
- **Bahasa**: Kotlin
- **UI Framework**: Jetpack Compose
- **Networking**: Retrofit & OkHttp
- **Local Storage**: SharedPreferences / DataStore

## 📋 Prasyarat Instalasi
- **Server**: PHP >= 8.2, Composer, MySQL Server.
- **Mobile Development**: Android Studio (Koala atau versi terbaru), JDK 17+.

## ⚙️ Langkah Instalasi

### 1. Setup Admin & API
```bash
cd mypresensi-admin
composer install
cp .env.example .env
php artisan key:generate
# Sesuaikan database di file .env
php artisan migrate --seed
php artisan serve

```

### 2. Setup Mobile App

1. Buka folder `mypresensi-mobile` menggunakan Android Studio.
2. Buka file `app/src/main/java/com/example/mypresensi_mobile/core/constant/ApiConfig.kt`.
3. Ubah `BASE_URL` sesuai dengan alamat IP server API Anda (contoh: `http://192.168.1.5:8000/api/`).
4. Build dan Run aplikasi ke emulator atau perangkat fisik.

## 📂 Struktur Proyek

```text
my-presensi/
├── mypresensi-admin/       # Source code Laravel & Filament
│   ├── app/Filament/       # Konfigurasi resource & widget admin
│   ├── app/Http/Controllers/API/ # Endpoint untuk mobile
│   └── routes/api.php      # Definisi rute API
├── mypresensi-mobile/      # Source code Android (Kotlin)
│   ├── app/src/main/java/  # Logika utama (Compose Screens, Retrofit)
│   └── app/src/main/res/   # Asset UI (Layout, Drawable)
└── README.md

```

## 🚀 Contoh Penggunaan

1. Admin login ke dashboard web untuk menambahkan daftar **Locations**.
2. Pengguna mendaftar melalui aplikasi mobile atau ditambahkan oleh Admin.
3. Pengguna melakukan **Login** di aplikasi mobile.
4. Pada menu **Beranda**, pengguna menekan tombol presensi. Sistem akan memvalidasi koordinat GPS sebelum data dikirim ke server.
5. Admin dapat mengekspor laporan bulanan melalui menu **Attendances Summary**.

## 🤝 Kontribusi

Kontribusi terbuka untuk pengembangan lebih lanjut:

1. Fork repositori ini.
2. Buat branch fitur (`git checkout -b fitur/NamaFitur`).
3. Commit perubahan (`git commit -m 'Menambahkan Fitur X'`).
4. Push ke branch (`git push origin fitur/NamaFitur`).
5. Buat Pull Request.

## 📄 Lisensi

Proyek ini dilisensikan di bawah **MIT License**. Lihat file [LICENSE](https://www.google.com/search?q=LICENSE) untuk informasi lebih lanjut.

---

**Developed by [Agus Saputra**](https://www.google.com/search?q=https://github.com/Agussaputr44)

```

