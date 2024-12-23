# Aplikasi Pengingat Jadwal

## Deskripsi
Aplikasi Pengingat Jadwal adalah aplikasi desktop berbasis Java yang memungkinkan pengguna untuk mengelola jadwal harian mereka dengan mudah. Aplikasi ini mencakup fungsionalitas CRUD, exception handling, dan integrasi dengan pustaka eksternal untuk meningkatkan fitur-fiturnya. Aplikasi ini memiliki antarmuka pengguna modern dan intuitif yang dibuat dengan Java Swing sesuai dengan spesifikasi desain yang diberikan.

---

## Fitur

1. **Manajemen Jadwal Harian**:
   - Tambah, lihat, perbarui, dan hapus jadwal.
   - Kategorisasi jadwal berdasarkan tingkat prioritas (Tinggi, Sedang, Rendah).
   - Lacak status tugas (Belum Dimulai, Sedang Berlangsung, Selesai).

2. **Lampiran**:
   - Unggah file atau gambar sebagai lampiran untuk setiap jadwal.

3. **Antarmuka Pengguna yang Mudah Digunakan**:
   - Dirancang dengan prinsip UI modern.
   - Skema warna dan penggunaan font yang konsisten.

4. **Penanganan Pengecualian**:
   - Menangani input yang tidak valid dan file yang hilang dengan baik.

5. **Integrasi API**:
   - Menggunakan pustaka `jcalendar` untuk pengelolaan tanggal.

6. **Penyimpanan Data yang Persisten**:
   - Menyimpan data pengguna dan jadwal dalam file teks untuk akses dan pengelolaan yang mudah.

---

## Struktur File

### Direktori Proyek:

- **assets/**:
  Berisi aset UI seperti gambar (misalnya, `back_arrow.png`, `Group 12.png`).

- **data/**:
  Menyimpan data jadwal dan pengguna dalam file teks (misalnya, `arbin.txt`, `ega.txt`, `faiz.txt`, `users.txt`).

- **lib/**:
  Termasuk pustaka eksternal (misalnya, `jcalendar-1.4.jar`).

- **resources/**:
  Menyimpan file sumber daya yang digunakan oleh aplikasi.

- **src/main/java/controller/**:
  Berisi kelas Java untuk mengelola logika aplikasi, seperti:
    - `DateController.java`
    - `UserController.java`

- **src/main/java/ui/**:
  Berisi kelas Java untuk antarmuka pengguna grafis:
    - `AddSchedulePage.java`
    - `DetailSchedulePage.java`
    - `MainPage.java`
    - `OnboardingPage.java`

- **tests/**:
  Tempat untuk skrip pengujian.

- **README.md**:
  Dokumentasi untuk proyek.

---

## Pustaka yang Digunakan

1. **jCalendar 1.4**:
   Menyediakan fungsionalitas pemilihan tanggal untuk jadwal.

---

## Cara Menjalankan

1. **Persyaratan**:
   - Java Development Kit (JDK) 8 atau lebih tinggi.
   - Integrated Development Environment (IDE) seperti IntelliJ IDEA atau Eclipse.

2. **Pengaturan**:
   - Kloning repositori dari GitHub.
   - Tambahkan `jcalendar-1.4.jar` ke jalur build proyek.

3. **Jalankan**:
   - Buka file `MainPage.java` di IDE Anda.
   - Jalankan metode `main` untuk meluncurkan aplikasi.

---

## Pengembangan Masa Depan

1. Tambahkan dukungan untuk jadwal berulang.
2. Implementasikan opsi penyaringan dan pencarian yang lebih canggih.
4. Integrasi dengan penyimpanan cloud untuk unggahan lampiran.

---

## Kontribusi
Pull request sangat diterima! Untuk perubahan besar, silakan buka isu terlebih dahulu untuk mendiskusikan apa yang ingin Anda ubah.

---

## Lisensi
Proyek ini dilisensikan di bawah Lisensi MIT.

