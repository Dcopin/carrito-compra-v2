-- phpMyAdmin SQL Dump
-- version 4.6.6deb5ubuntu0.5
-- https://www.phpmyadmin.net/
--
-- Servidor: localhost:3306
-- Tiempo de generación: 20-06-2021 a las 18:31:09
-- Versión del servidor: 5.7.33-0ubuntu0.18.04.1
-- Versión de PHP: 7.2.24-0ubuntu0.18.04.7

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
--

CREATE DATABASE ccjava CHARACTER SET utf8 COLLATE utf8_spanish_ci;

USE ccjava;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `producto`
--

CREATE TABLE `producto` (
  `idProducto` int(11) NOT NULL,
  `nombre` varchar(255) COLLATE utf8_spanish_ci DEFAULT NULL,
  `codigo` varchar(255) COLLATE utf8_spanish_ci NOT NULL,
  `imagen` text COLLATE utf8_spanish_ci,
  `precio` double(10,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

--
-- Volcado de datos para la tabla `producto`
--

INSERT INTO `producto` (`idProducto`, `nombre`, `codigo`, `imagen`, `precio`) VALUES
(1, 'FinePix Pro2 3D Camera', '3DcAM01', 'images/product/camera.jpg', 671.21),
(2, 'EXP Portable Hard Drive', 'USB02', 'images/product/external-hard-drive.jpg', 215.10),
(3, 'Luxury Ultra thin Wrist Watch', 'wristWear03', 'images/product/watch.jpg', 2121.86),
(4, 'XP 1155 Intel Core Laptop', 'LPN45', 'images/product/laptop.jpg', 10232.35);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `shopping_cart`
--

CREATE TABLE `shopping_cart` (
  `idShoppingCart` int(11) NOT NULL,
  `idProducto` int(11) DEFAULT NULL,
  `codUsuario` varchar(128) COLLATE utf8_spanish_ci DEFAULT NULL,
  `cantidad` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

--
-- Volcado de datos para la tabla `shopping_cart`
--

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `producto`
--
ALTER TABLE `producto`
  ADD PRIMARY KEY (`idProducto`);

--
-- Indices de la tabla `shopping_cart`
--
ALTER TABLE `shopping_cart`
  ADD PRIMARY KEY (`idShoppingCart`),
  ADD KEY `cc_profk_1` (`idProducto`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `producto`
--
ALTER TABLE `producto`
  MODIFY `idProducto` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
--
-- AUTO_INCREMENT de la tabla `shopping_cart`
--
ALTER TABLE `shopping_cart`
  MODIFY `idShoppingCart` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=0;
--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `shopping_cart`
--
ALTER TABLE `shopping_cart`
  ADD CONSTRAINT `cc_profk_1` FOREIGN KEY (`idProducto`) REFERENCES `producto` (`idProducto`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
