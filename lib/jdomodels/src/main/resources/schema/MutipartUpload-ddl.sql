CREATE TABLE IF NOT EXISTS `MULTIPART_UPLOAD` (
  `ID` bigint(20) NOT NULL,
  `REQUEST_HASH` char(100) NOT NULL,
  `ETAG` char(36) NOT NULL,
  `REQUEST_BLOB` BLOB NOT NULL,
  `STARTED_BY` bigint(20) NOT NULL,
  `STARTED_ON` TIMESTAMP NOT NULL,
  `UPDATED_ON` TIMESTAMP NOT NULL,
  `FILE_HANDLE_ID` bigint(20) DEFAULT NULL,
  `STATE` ENUM('UPLOADING','COMPLETED') NOT NULL,
  `UPLOAD_TOKEN` varchar(200) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL, 
  `S3_BUCKET` varchar(100) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `S3_KEY` varchar(700) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `NUMBER_OF_PARTS` int NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY (`STARTED_BY`,`REQUEST_HASH`),
  CONSTRAINT `MUTI_FILE_HAN_BY_FK` FOREIGN KEY (`FILE_HANDLE_ID`) REFERENCES `FILES` (`ID`) ON DELETE CASCADE,
  CONSTRAINT `MUTI_STARTED_BY_FK` FOREIGN KEY (`STARTED_BY`) REFERENCES `JDOUSERGROUP` (`ID`) ON DELETE CASCADE
)