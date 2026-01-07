-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jan 07, 2026 at 01:54 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `qlnhatro`
--
CREATE DATABASE IF NOT EXISTS `qlnhatro` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `qlnhatro`;

-- --------------------------------------------------------

--
-- Table structure for table `chitietphuphi`
--

DROP TABLE IF EXISTS `chitietphuphi`;
CREATE TABLE `chitietphuphi` (
  `MaPhong` bigint(20) NOT NULL,
  `MaPP` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `giadiennuoc`
--

DROP TABLE IF EXISTS `giadiennuoc`;
CREATE TABLE `giadiennuoc` (
  `MaGiaDN` bigint(20) NOT NULL,
  `GiaDien` decimal(12,2) NOT NULL,
  `GiaNuoc` decimal(12,2) NOT NULL,
  `TrangThaiXoa` bit(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `hoadon`
--

DROP TABLE IF EXISTS `hoadon`;
CREATE TABLE `hoadon` (
  `MaHoaDon` bigint(20) NOT NULL,
  `NgayTT` datetime NOT NULL,
  `SoTienTT` decimal(12,2) NOT NULL,
  `TongTienPP` decimal(12,2) NOT NULL,
  `LoaiTT` varchar(50) NOT NULL,
  `GhiChu` text DEFAULT NULL,
  `MaHD` bigint(20) NOT NULL,
  `MaPT` bigint(20) NOT NULL,
  `TrangThaiXoa` bit(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `hopdong`
--

DROP TABLE IF EXISTS `hopdong`;
CREATE TABLE `hopdong` (
  `MaHD` bigint(20) NOT NULL,
  `NgayLapHD` date NOT NULL,
  `NgayKT` date NOT NULL,
  `GiaThue` decimal(12,2) NOT NULL,
  `SoNguoiO` int(11) NOT NULL,
  `TrangThaiHD` varchar(50) NOT NULL,
  `GhiChu` text DEFAULT NULL,
  `MaKH` bigint(20) NOT NULL,
  `MaPhong` bigint(20) NOT NULL,
  `TrangThaiXoa` bit(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `khachhang`
--

DROP TABLE IF EXISTS `khachhang`;
CREATE TABLE `khachhang` (
  `MaKH` bigint(20) NOT NULL,
  `TenKH` varchar(50) NOT NULL,
  `DiaChi` varchar(150) DEFAULT NULL,
  `GioiTinh` bit(1) DEFAULT NULL,
  `NgaySinh` date DEFAULT NULL,
  `SDT` varchar(12) NOT NULL,
  `Gmail` varchar(100) DEFAULT NULL,
  `SoCCCD` char(12) NOT NULL,
  `MaKHChinh` bigint(20) DEFAULT NULL,
  `MaPhong` bigint(20) DEFAULT NULL,
  `TrangThaiXoa` bit(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `nhatro`
--

DROP TABLE IF EXISTS `nhatro`;
CREATE TABLE `nhatro` (
  `MaNT` bigint(20) NOT NULL,
  `TenNT` varchar(50) NOT NULL,
  `SLPhong` int(11) NOT NULL,
  `DiaChi` varchar(200) NOT NULL,
  `TrangThaiNT` varchar(50) NOT NULL,
  `GhiChu` text DEFAULT NULL,
  `TrangThaiXoa` bit(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `nhatro`
--

INSERT INTO `nhatro` (`MaNT`, `TenNT`, `SLPhong`, `DiaChi`, `TrangThaiNT`, `GhiChu`, `TrangThaiXoa`) VALUES
(1, 'Nguyễn Hồng', 0, 'Ninh Kiều, Cần Thơ', 'Còn Phòng', NULL, b'0'),
(2, 'Nhà Trọ Sinh Viên Hoàng Nam', 0, '495A/NC, Nguyễn Văn Trường, Cần Thơ, Việt Nam', 'Còn Phòng', NULL, b'0');

-- --------------------------------------------------------

--
-- Table structure for table `phieudiennuoc`
--

DROP TABLE IF EXISTS `phieudiennuoc`;
CREATE TABLE `phieudiennuoc` (
  `MaDN` bigint(20) NOT NULL,
  `ThangNam` date NOT NULL,
  `ChiSoDienCu` float NOT NULL,
  `ChiSoDienMoi` float NOT NULL,
  `ChiSoNuocCu` float NOT NULL,
  `ChiSoNuocMoi` float NOT NULL,
  `TienDien` decimal(12,2) NOT NULL,
  `TienNuoc` decimal(12,2) NOT NULL,
  `GiaDienTaiThoiDiem` decimal(12,2) NOT NULL,
  `GiaNuocTaiThoiDiem` decimal(12,2) NOT NULL,
  `TongTien` decimal(12,2) NOT NULL,
  `TrangThaiDN` varchar(50) NOT NULL,
  `MaPhong` bigint(20) NOT NULL,
  `MaHoaDon` bigint(20) DEFAULT NULL,
  `MaGiaDN` bigint(20) DEFAULT NULL,
  `TrangThaiXoa` bit(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `phong`
--

DROP TABLE IF EXISTS `phong`;
CREATE TABLE `phong` (
  `MaPhong` bigint(20) NOT NULL,
  `SoPhong` int(11) NOT NULL,
  `Gia` decimal(12,2) NOT NULL,
  `SLNguoiMax` int(11) NOT NULL,
  `PhuThu` decimal(12,2) NOT NULL,
  `TrangThaiPhong` varchar(50) NOT NULL,
  `BaoTri` bit(1) NOT NULL,
  `GhiChu` text DEFAULT NULL,
  `MaHD` bigint(20) DEFAULT NULL,
  `MaNT` bigint(20) NOT NULL,
  `TrangThaiXoa` bit(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Triggers `phong`
--
DROP TRIGGER IF EXISTS `trg_phong_after_delete`;
DELIMITER $$
CREATE TRIGGER `trg_phong_after_delete` AFTER DELETE ON `phong` FOR EACH ROW BEGIN
    UPDATE NhaTro
    SET SLPhong = SLPhong - 1
    WHERE MaNT = OLD.MaNT;
END
$$
DELIMITER ;
DROP TRIGGER IF EXISTS `trg_phong_after_insert`;
DELIMITER $$
CREATE TRIGGER `trg_phong_after_insert` AFTER INSERT ON `phong` FOR EACH ROW BEGIN
    UPDATE NhaTro
    SET SLPhong = SLPhong + 1
    WHERE MaNT = NEW.MaNT;
END
$$
DELIMITER ;
DROP TRIGGER IF EXISTS `trg_phong_after_update`;
DELIMITER $$
CREATE TRIGGER `trg_phong_after_update` AFTER UPDATE ON `phong` FOR EACH ROW BEGIN
    IF OLD.MaNT <> NEW.MaNT THEN
        UPDATE NhaTro SET SLPhong = SLPhong - 1 WHERE MaNT = OLD.MaNT;
        UPDATE NhaTro SET SLPhong = SLPhong + 1 WHERE MaNT = NEW.MaNT;
    END IF;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `phuphi`
--

DROP TABLE IF EXISTS `phuphi`;
CREATE TABLE `phuphi` (
  `MaPP` bigint(20) NOT NULL,
  `TenPP` varchar(50) NOT NULL,
  `Gia` decimal(12,2) NOT NULL,
  `TrangThaiXoa` bit(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `ptthanhtoan`
--

DROP TABLE IF EXISTS `ptthanhtoan`;
CREATE TABLE `ptthanhtoan` (
  `MaPT` bigint(20) NOT NULL,
  `TenPT` varchar(50) NOT NULL,
  `TrangThaiXoa` bit(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `chitietphuphi`
--
ALTER TABLE `chitietphuphi`
  ADD PRIMARY KEY (`MaPhong`,`MaPP`),
  ADD KEY `FK_ChiTietPhuPhi_PhuPhi` (`MaPP`);

--
-- Indexes for table `giadiennuoc`
--
ALTER TABLE `giadiennuoc`
  ADD PRIMARY KEY (`MaGiaDN`);

--
-- Indexes for table `hoadon`
--
ALTER TABLE `hoadon`
  ADD PRIMARY KEY (`MaHoaDon`),
  ADD KEY `FK_HoaDon_HopDong` (`MaHD`),
  ADD KEY `FK_HoaDon_PTThanhToan` (`MaPT`);

--
-- Indexes for table `hopdong`
--
ALTER TABLE `hopdong`
  ADD PRIMARY KEY (`MaHD`),
  ADD KEY `FK_HopDong_KhachHang` (`MaKH`),
  ADD KEY `FK_HopDong_Phong` (`MaPhong`);

--
-- Indexes for table `khachhang`
--
ALTER TABLE `khachhang`
  ADD PRIMARY KEY (`MaKH`),
  ADD KEY `FK_KhachHang_KhachHangChinh` (`MaKHChinh`),
  ADD KEY `FK_KhachHang_Phong` (`MaPhong`);

--
-- Indexes for table `nhatro`
--
ALTER TABLE `nhatro`
  ADD PRIMARY KEY (`MaNT`);

--
-- Indexes for table `phieudiennuoc`
--
ALTER TABLE `phieudiennuoc`
  ADD PRIMARY KEY (`MaDN`),
  ADD KEY `FK_PhieuDienNuoc_Phong` (`MaPhong`),
  ADD KEY `FK_PhieuDienNuoc_HoaDon` (`MaHoaDon`),
  ADD KEY `FK_PhieuDienNuoc_GiaDienNuoc` (`MaGiaDN`);

--
-- Indexes for table `phong`
--
ALTER TABLE `phong`
  ADD PRIMARY KEY (`MaPhong`),
  ADD KEY `FK_Phong_HopDong` (`MaHD`),
  ADD KEY `FK_Phong_NhaTro` (`MaNT`);

--
-- Indexes for table `phuphi`
--
ALTER TABLE `phuphi`
  ADD PRIMARY KEY (`MaPP`);

--
-- Indexes for table `ptthanhtoan`
--
ALTER TABLE `ptthanhtoan`
  ADD PRIMARY KEY (`MaPT`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `giadiennuoc`
--
ALTER TABLE `giadiennuoc`
  MODIFY `MaGiaDN` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `hoadon`
--
ALTER TABLE `hoadon`
  MODIFY `MaHoaDon` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `hopdong`
--
ALTER TABLE `hopdong`
  MODIFY `MaHD` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `khachhang`
--
ALTER TABLE `khachhang`
  MODIFY `MaKH` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `nhatro`
--
ALTER TABLE `nhatro`
  MODIFY `MaNT` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `phieudiennuoc`
--
ALTER TABLE `phieudiennuoc`
  MODIFY `MaDN` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `phong`
--
ALTER TABLE `phong`
  MODIFY `MaPhong` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `phuphi`
--
ALTER TABLE `phuphi`
  MODIFY `MaPP` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `ptthanhtoan`
--
ALTER TABLE `ptthanhtoan`
  MODIFY `MaPT` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `chitietphuphi`
--
ALTER TABLE `chitietphuphi`
  ADD CONSTRAINT `FK_ChiTietPhuPhi_Phong` FOREIGN KEY (`MaPhong`) REFERENCES `phong` (`MaPhong`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `FK_ChiTietPhuPhi_PhuPhi` FOREIGN KEY (`MaPP`) REFERENCES `phuphi` (`MaPP`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `hoadon`
--
ALTER TABLE `hoadon`
  ADD CONSTRAINT `FK_HoaDon_HopDong` FOREIGN KEY (`MaHD`) REFERENCES `hopdong` (`MaHD`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `FK_HoaDon_PTThanhToan` FOREIGN KEY (`MaPT`) REFERENCES `ptthanhtoan` (`MaPT`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `hopdong`
--
ALTER TABLE `hopdong`
  ADD CONSTRAINT `FK_HopDong_KhachHang` FOREIGN KEY (`MaKH`) REFERENCES `khachhang` (`MaKH`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `FK_HopDong_Phong` FOREIGN KEY (`MaPhong`) REFERENCES `phong` (`MaPhong`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `khachhang`
--
ALTER TABLE `khachhang`
  ADD CONSTRAINT `FK_KhachHang_KhachHangChinh` FOREIGN KEY (`MaKHChinh`) REFERENCES `khachhang` (`MaKH`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `FK_KhachHang_Phong` FOREIGN KEY (`MaPhong`) REFERENCES `phong` (`MaPhong`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `phieudiennuoc`
--
ALTER TABLE `phieudiennuoc`
  ADD CONSTRAINT `FK_PhieuDienNuoc_GiaDienNuoc` FOREIGN KEY (`MaGiaDN`) REFERENCES `giadiennuoc` (`MaGiaDN`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `FK_PhieuDienNuoc_HoaDon` FOREIGN KEY (`MaHoaDon`) REFERENCES `hoadon` (`MaHoaDon`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `FK_PhieuDienNuoc_Phong` FOREIGN KEY (`MaPhong`) REFERENCES `phong` (`MaPhong`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `phong`
--
ALTER TABLE `phong`
  ADD CONSTRAINT `FK_Phong_HopDong` FOREIGN KEY (`MaHD`) REFERENCES `hopdong` (`MaHD`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `FK_Phong_NhaTro` FOREIGN KEY (`MaNT`) REFERENCES `nhatro` (`MaNT`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
