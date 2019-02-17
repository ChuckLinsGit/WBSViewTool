/*
 Navicat MySQL Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 50719
 Source Host           : localhost:3306
 Source Schema         : wbstooldatabase

 Target Server Type    : MySQL
 Target Server Version : 50719
 File Encoding         : 65001

 Date: 27/11/2018 15:56:47
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for headerpointers
-- ----------------------------
DROP TABLE IF EXISTS `headerpointers`;
CREATE TABLE `headerpointers`  (
  `number` int(20) NULL DEFAULT NULL,
  `headNumber` int(20) NULL DEFAULT NULL,
  INDEX `FK_head_pointer`(`headNumber`) USING BTREE,
  INDEX `FK_hnumber_pointer`(`number`) USING BTREE,
  CONSTRAINT `FK_head_pointer` FOREIGN KEY (`headNumber`) REFERENCES `structurepointer` (`number`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_hnumber_pointer` FOREIGN KEY (`number`) REFERENCES `structurepointer` (`number`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of headerpointers
-- ----------------------------
INSERT INTO `headerpointers` VALUES (1, 1);
INSERT INTO `headerpointers` VALUES (2, 1);
INSERT INTO `headerpointers` VALUES (3, 2);
INSERT INTO `headerpointers` VALUES (4, 3);
INSERT INTO `headerpointers` VALUES (5, 3);
INSERT INTO `headerpointers` VALUES (5, 4);
INSERT INTO `headerpointers` VALUES (6, 5);
INSERT INTO `headerpointers` VALUES (7, 5);
INSERT INTO `headerpointers` VALUES (8, 5);
INSERT INTO `headerpointers` VALUES (9, 6);
INSERT INTO `headerpointers` VALUES (9, 7);
INSERT INTO `headerpointers` VALUES (9, 8);
INSERT INTO `headerpointers` VALUES (10, 9);
INSERT INTO `headerpointers` VALUES (11, 9);
INSERT INTO `headerpointers` VALUES (12, 10);
INSERT INTO `headerpointers` VALUES (12, 11);
INSERT INTO `headerpointers` VALUES (13, 12);
INSERT INTO `headerpointers` VALUES (14, 13);
INSERT INTO `headerpointers` VALUES (15, 14);

-- ----------------------------
-- Table structure for nextpointers
-- ----------------------------
DROP TABLE IF EXISTS `nextpointers`;
CREATE TABLE `nextpointers`  (
  `number` int(20) NULL DEFAULT NULL,
  `nextnumber` int(20) NULL DEFAULT NULL,
  INDEX `FK_nnumber_pointer`(`number`) USING BTREE,
  INDEX `FK_next_pointer`(`nextnumber`) USING BTREE,
  CONSTRAINT `FK_next_pointer` FOREIGN KEY (`nextnumber`) REFERENCES `structurepointer` (`number`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_nnumber_pointer` FOREIGN KEY (`number`) REFERENCES `structurepointer` (`number`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of nextpointers
-- ----------------------------
INSERT INTO `nextpointers` VALUES (1, 2);
INSERT INTO `nextpointers` VALUES (2, 3);
INSERT INTO `nextpointers` VALUES (3, 4);
INSERT INTO `nextpointers` VALUES (3, 5);
INSERT INTO `nextpointers` VALUES (4, 5);
INSERT INTO `nextpointers` VALUES (5, 6);
INSERT INTO `nextpointers` VALUES (5, 7);
INSERT INTO `nextpointers` VALUES (5, 8);
INSERT INTO `nextpointers` VALUES (6, 9);
INSERT INTO `nextpointers` VALUES (7, 9);
INSERT INTO `nextpointers` VALUES (8, 9);
INSERT INTO `nextpointers` VALUES (9, 10);
INSERT INTO `nextpointers` VALUES (9, 11);
INSERT INTO `nextpointers` VALUES (10, 12);
INSERT INTO `nextpointers` VALUES (11, 12);
INSERT INTO `nextpointers` VALUES (12, 13);
INSERT INTO `nextpointers` VALUES (13, 14);
INSERT INTO `nextpointers` VALUES (14, 15);
INSERT INTO `nextpointers` VALUES (15, 15);

-- ----------------------------
-- Table structure for projectstructure
-- ----------------------------
DROP TABLE IF EXISTS `projectstructure`;
CREATE TABLE `projectstructure`  (
  `projectName` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `projectBeginDate` double(10, 0) NOT NULL,
  `projectEndDate` double(10, 0) NULL DEFAULT NULL,
  `projectPointerQty` int(8) NULL DEFAULT NULL,
  PRIMARY KEY (`projectName`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of projectstructure
-- ----------------------------
INSERT INTO `projectstructure` VALUES ('商业企划', 0, 22, 15);

-- ----------------------------
-- Table structure for structurepointer
-- ----------------------------
DROP TABLE IF EXISTS `structurepointer`;
CREATE TABLE `structurepointer`  (
  `number` int(20) NOT NULL,
  `days` int(8) NOT NULL,
  `projectName` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `content` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `earlistBegin` double(10, 0) NULL DEFAULT NULL,
  `earlistEnd` double(10, 0) NULL DEFAULT NULL,
  `latestBegin` double(10, 0) NULL DEFAULT NULL,
  `latestEnd` double(10, 0) NULL DEFAULT NULL,
  `isCriticial` bit(1) NULL DEFAULT NULL,
  PRIMARY KEY (`number`) USING BTREE,
  INDEX `FK_projectpointer_name`(`projectName`) USING BTREE,
  CONSTRAINT `FK_projectpointer_name` FOREIGN KEY (`projectName`) REFERENCES `projectstructure` (`projectName`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of structurepointer
-- ----------------------------
INSERT INTO `structurepointer` VALUES (1, 1, '商业企划', '组成团队', 0, 1, 0, 1, b'1');
INSERT INTO `structurepointer` VALUES (2, 2, '商业企划', '讨论新商机', 1, 3, 1, 3, b'1');
INSERT INTO `structurepointer` VALUES (3, 1, '商业企划', '初步了解市场', 3, 4, 3, 4, b'1');
INSERT INTO `structurepointer` VALUES (4, 2, '商业企划', '研究产品发展计划', 4, 6, 4, 6, b'1');
INSERT INTO `structurepointer` VALUES (5, 1, '商业企划', '初步形成产品方案', 6, 7, 6, 7, b'1');
INSERT INTO `structurepointer` VALUES (6, 3, '商业企划', '提出产品研发流程计划', 7, 10, 8, 11, b'0');
INSERT INTO `structurepointer` VALUES (7, 1, '商业企划', '建立成本和盈利模型', 7, 8, 10, 11, b'0');
INSERT INTO `structurepointer` VALUES (8, 4, '商业企划', '分析风险，研究应对方法', 7, 11, 7, 11, b'1');
INSERT INTO `structurepointer` VALUES (9, 2, '商业企划', '研究产品可行性', 11, 13, 11, 13, b'1');
INSERT INTO `structurepointer` VALUES (10, 1, '商业企划', '制定组织结构方案', 13, 14, 14, 15, b'0');
INSERT INTO `structurepointer` VALUES (11, 2, '商业企划', '制定公司战略', 13, 15, 13, 15, b'1');
INSERT INTO `structurepointer` VALUES (12, 4, '商业企划', '形成并完善商业企划书', 15, 19, 15, 19, b'1');
INSERT INTO `structurepointer` VALUES (13, 1, '商业企划', '通过招标审核，获得启动资金', 19, 20, 19, 20, b'1');
INSERT INTO `structurepointer` VALUES (14, 1, '商业企划', '成立公司', 20, 21, 20, 21, b'1');
INSERT INTO `structurepointer` VALUES (15, 1, '商业企划', '阶段总结', 21, 22, 21, 22, b'1');

SET FOREIGN_KEY_CHECKS = 1;
