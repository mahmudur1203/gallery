package com.myapp.gallery.testing.data

import com.myapp.gallery.data.local.mediastore.model.MediaFile
import com.myapp.gallery.domain.model.Media

val timeStamp = System.currentTimeMillis()

val mediaListTestData = mutableListOf(
    Media(1, "https://picsum.photos/id/100/200/300", "Image 1", 100, timeStamp, false, null),
    Media(2, "", "Image 2", 200, timeStamp, false, null),
    Media(3, "", "Image 3", 300, timeStamp, false, null),
    Media(4, "", "Image 4", 400, timeStamp, false, null),
    Media(5, "", "Image 5", 500, timeStamp, false, null),
    Media(6, "", "Image 6", 600, timeStamp, false, null),
    Media(7, "", "Image 7", 700, timeStamp, false, null),
    Media(8, "", "Image 8", 800, timeStamp, false, null),
    Media(9, "", "Image 9", 900, timeStamp, false, null),
    Media(10, "", "Image 10", 1000, timeStamp, false, null),
    Media(11, "", "Image 11", 1100, timeStamp, false, null),
    Media(12, "", "Image 12", 1200, timeStamp, false, null),
    Media(13, "", "Image 13", 1300, timeStamp, false, null),
    Media(14, "", "Image 14", 1400, timeStamp, false, null),
    Media(15, "", "Image 15", 1500, timeStamp, false, null),
    Media(16, "", "Image 16", 1600, timeStamp, false, null),
    Media(17, "", "Image 17", 1700, timeStamp, false, null),
    Media(18, "", "Image 18", 1800, timeStamp, false, null),
    Media(19, "", "Image 19", 1900, timeStamp, false, null),
    Media(20, "", "Image 20", 2000, timeStamp, false, null),
    Media(21, "", "Image 21", 2100, timeStamp, false, null),
    Media(22, "", "Image 22", 2200, timeStamp, false, null),
    Media(23, "", "Image 23", 2300, timeStamp, false, null),
    Media(24, "", "Image 24", 2400, timeStamp, false, null),
    Media(25, "", "Image 25", 2500, timeStamp, false, null),
    Media(26, "", "Image 26", 2600, timeStamp, false, null),
    Media(27, "", "Image 27", 2700, timeStamp, false, null),
    Media(28, "", "Image 28", 2800, timeStamp, false, null),
    Media(29, "", "Image 29", 2900, timeStamp, false, null),
    Media(30, "", "Image 30", 3000, timeStamp, false, null),
    Media(31, "", "Image 31", 3100, timeStamp, false, null),
    Media(32, "", "Image 32", 3200, timeStamp, false, null),
    Media(33, "", "Image 33", 3300, timeStamp, false, null),
    Media(34, "", "Image 34", 3400, timeStamp, false, null),
    Media(35, "", "Image 35", 3500, timeStamp, false, null),
    Media(36, "", "Image 36", 3600, timeStamp, false, null),
    Media(37, "", "Image 37", 3700, timeStamp, false, null),
    Media(38, "", "Image 38", 3800, timeStamp, false, null),
    Media(39, "", "Image 39", 3900, timeStamp, false, null),
    Media(40, "", "Image 40", 4000, timeStamp, false, null),
    Media(41, "", "Image 41", 4100, timeStamp, false, null),
    Media(42, "", "Image 42", 4200, timeStamp, false, null),
    Media(43, "", "Image 43", 4300, timeStamp, false, null),
    Media(44, "", "Image 44", 4400, timeStamp, false, null),
    Media(45, "", "Image 45", 4500, timeStamp, false, null),
    Media(46, "", "Image 46", 4600, timeStamp, false, null),
    Media(47, "", "Image 47", 4700, timeStamp, false, null),
    Media(48, "", "Image 48", 4800, timeStamp, false, null),
    Media(49, "", "Image 49", 4900, timeStamp, false, null),
    Media(50, "", "Image 50", 5000, timeStamp, false, null),
    Media(51, "", "Image 51", 5100, timeStamp, false, null),
    Media(52, "", "Image 52", 5200, timeStamp, false, null),
    Media(53, "", "Image 53", 5300, timeStamp, false, null),
    Media(54, "", "Image 54", 5400, timeStamp, false, null),
    Media(55, "", "Image 55", 5500, timeStamp, false, null),
    Media(56, "", "Image 56", 5600, timeStamp, false, null),
    Media(57, "", "Image 57", 5700, timeStamp, false, null),
    Media(58, "", "Image 58", 5800, timeStamp, false, null),
    Media(59, "", "Image 59", 5900, timeStamp, false, null),
    Media(60, "", "Image 60", 6000, timeStamp, false, null),
    Media(61, "", "Image 61", 6100, timeStamp, false, null),
    Media(62, "", "Image 62", 6200, timeStamp, false, null),
    Media(63, "", "Image 63", 6300, timeStamp, false, null),
    Media(64, "", "Image 64", 6400, timeStamp, false, null),
    Media(65, "", "Image 65", 6500, timeStamp, false, null),
    Media(66, "", "Image 66", 6600, timeStamp, false, null),
    Media(67, "", "Image 67", 6700, timeStamp, false, null),
    Media(68, "", "Image 68", 6800, timeStamp, false, null),
    Media(69, "", "Image 69", 6900, timeStamp, false, null),
    Media(70, "", "Image 70", 7000, timeStamp, false, null),
)

val mediaFileTestData = mutableListOf(
    MediaFile(
        1,
        "https://picsum.photos/id/100/200/300",
        "Image 1",
        100,
        timeStamp + 1,
        false,
        null
    ),
    MediaFile(
        2,
        "https://picsum.photos/id/200/200/300",
        "Image 2",
        200,
        timeStamp + 2,
        false,
        null
    ),
    MediaFile(
        3,
        "https://picsum.photos/id/300/200/300",
        "Image 3",
        300,
        timeStamp + 3,
        false,
        null
    ),
    MediaFile(4, "", "Image 4", 300, timeStamp + 4, false, null),
    MediaFile(5, "", "Image 5", 300, timeStamp + 5, false, null),
    MediaFile(6, "", "Image 6", 300, timeStamp + 6, false, null),
    MediaFile(7, "", "Image 7", 300, timeStamp + 7, false, null),
    MediaFile(8, "", "Image 8", 300, timeStamp + 8, false, null),
    MediaFile(9, "", "Image 9", 300, timeStamp + 9, false, null),
    MediaFile(10, "", "Image 10", 300, timeStamp + 10, false, null),
    MediaFile(11, "", "Image 11", 300, timeStamp + 11, false, null),
    MediaFile(12, "", "Image 12", 300, timeStamp + 12, false, null),
    MediaFile(13, "", "Image 13", 300, timeStamp + 13, false, null),
    MediaFile(14, "", "Image 14", 300, timeStamp + 14, false, null),
    MediaFile(15, "", "Image 15", 300, timeStamp + 15, false, null),
    MediaFile(16, "", "Image 16", 300, timeStamp + 16, false, null),
    MediaFile(17, "", "Image 17", 300, timeStamp + 17, false, null),
    MediaFile(18, "", "Image 18", 300, timeStamp + 18, false, null),
    MediaFile(19, "", "Image 19", 300, timeStamp + 19, false, null),
    MediaFile(20, "", "Image 20", 300, timeStamp + 20, false, null),
    MediaFile(21, "", "Image 21", 300, timeStamp + 21, false, null),
    MediaFile(22, "", "Image 22", 300, timeStamp + 22, false, null),
    MediaFile(23, "", "Image 23", 300, timeStamp + 23, false, null),
    MediaFile(24, "", "Image 24", 300, timeStamp + 24, false, null),
    MediaFile(25, "", "Image 25", 300, timeStamp + 25, false, null),
    MediaFile(26, "", "Image 26", 300, timeStamp + 26, false, null),
    MediaFile(27, "", "Image 27", 300, timeStamp + 27, false, null),
    MediaFile(28, "", "Image 28", 300, timeStamp + 28, false, null),
    MediaFile(29, "", "Image 29", 300, timeStamp + 29, false, null),
    MediaFile(30, "", "Image 30", 300, timeStamp + 30, false, null),
    MediaFile(31, "", "Image 31", 300, timeStamp + 31, false, null),
    MediaFile(32, "", "Image 32", 300, timeStamp + 32, false, null),
    MediaFile(33, "", "Image 33", 300, timeStamp + 33, false, null),
    MediaFile(34, "", "Image 34", 300, timeStamp + 34, false, null),
    MediaFile(35, "", "Image 35", 300, timeStamp + 35, false, null),
    MediaFile(36, "", "Image 36", 300, timeStamp + 36, false, null),
    MediaFile(37, "", "Image 37", 300, timeStamp + 37, false, null),
    MediaFile(38, "", "Image 38", 300, timeStamp + 38, false, null),
    MediaFile(39, "", "Image 39", 300, timeStamp + 39, false, null),
    MediaFile(40, "", "Image 40", 300, timeStamp + 40, false, null),
    MediaFile(41, "", "Image 41", 300, timeStamp + 41, false, null),
    MediaFile(42, "", "Image 42", 300, timeStamp + 42, false, null),
    MediaFile(43, "", "Image 43", 300, timeStamp + 43, false, null),
    MediaFile(44, "", "Image 44", 300, timeStamp + 44, false, null),
    MediaFile(45, "", "Image 45", 300, timeStamp + 45, false, null),
    MediaFile(46, "", "Image 46", 300, timeStamp + 46, false, null),
    MediaFile(47, "", "Image 47", 300, timeStamp + 47, false, null),
    MediaFile(48, "", "Image 48", 300, timeStamp + 48, false, null),
    MediaFile(49, "", "Image 49", 300, timeStamp + 49, false, null),
    MediaFile(50, "", "Image 50", 300, timeStamp + 50, false, null),
    MediaFile(51, "", "Image 51", 300, timeStamp + 51, false, null),
    MediaFile(52, "", "Image 52", 300, timeStamp + 52, false, null),
    MediaFile(53, "", "Image 53", 300, timeStamp + 53, false, null),
    MediaFile(54, "", "Image 54", 300, timeStamp + 54, false, null),
    MediaFile(55, "", "Image 55", 300, timeStamp + 55, false, null),
    MediaFile(56, "", "Image 56", 300, timeStamp + 56, false, null),
    MediaFile(57, "", "Image 57", 300, timeStamp + 57, false, null),
    MediaFile(58, "", "Image 58", 300, timeStamp + 58, false, null),
    MediaFile(59, "", "Image 59", 300, timeStamp + 59, false, null),
    MediaFile(60, "", "Image 60", 300, timeStamp + 60, false, null),
    MediaFile(61, "", "Image 61", 300, timeStamp + 61, false, null),
    MediaFile(62, "", "Image 62", 300, timeStamp + 62, false, null),
    MediaFile(63, "", "Image 63", 300, timeStamp + 63, false, null),
    MediaFile(64, "", "Image 64", 300, timeStamp + 64, false, null),
    MediaFile(65, "", "Image 65", 300, timeStamp + 65, false, null),
    MediaFile(66, "", "Image 66", 300, timeStamp + 66, false, null),
    MediaFile(67, "", "Image 67", 300, timeStamp + 67, false, null),
    MediaFile(68, "", "Image 68", 300, timeStamp + 68, false, null),
    MediaFile(69, "", "Image 69", 300, timeStamp + 69, false, null),
    MediaFile(70, "", "Image 70", 300, timeStamp + 70, false, null),
    MediaFile(71, "", "Image 71", 300, timeStamp + 71, false, null),
    MediaFile(72, "", "Image 72", 300, timeStamp + 72, false, null),
    MediaFile(73, "", "Image 73", 300, timeStamp + 73, false, null),
    MediaFile(74, "", "Image 74", 300, timeStamp + 74, false, null),
    MediaFile(75, "", "Image 75", 300, timeStamp + 75, false, null),
    MediaFile(76, "", "Image 76", 300, timeStamp + 76, false, null),
    MediaFile(77, "", "Image 77", 300, timeStamp + 77, false, null),
    MediaFile(78, "", "Image 78", 300, timeStamp + 78, false, null),
    MediaFile(79, "", "Image 79", 300, timeStamp + 79, false, null),
    MediaFile(
        80,
        "https://picsum.photos/id/8000/200/300",
        "Image 80",
        8000,
        timeStamp + 80,
        false,
        null
    )
)