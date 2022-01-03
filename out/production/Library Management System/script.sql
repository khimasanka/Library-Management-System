DROP DATABASE IF EXISTS Library;
CREATE DATABASE IF NOT EXISTS Library;
SHOW DATABASES ;
USE Library;
#========================

DROP TABLE IF EXISTS Users;
CREATE TABLE IF NOT EXISTS Users(
    userId VARCHAR(6),
    userFullName VARCHAR(25) ,
    userName VARCHAR(10),
    userPassword VARCHAR(15),
    userMobile INT(10),
    userAddress VARCHAR(50),
    role VARCHAR(20),
    CONSTRAINT PRIMARY KEY (userId,userName)
);

#======================

DROP TABLE IF EXISTS item;
CREATE TABLE IF NOT EXISTS item(
    itemId VARCHAR(10),
    itemName VARCHAR(30),
    itemType VARCHAR(30),
    qty INT(10),
    CONSTRAINT PRIMARY KEY (itemId)
);
SHOW TABLES ;
DESCRIBE item;
#========================

DROP TABLE IF EXISTS book;
CREATE TABLE IF NOT EXISTS book(
    bookId VARCHAR(10),
    bookName VARCHAR(50),
    bookType VARCHAR(50),
    language VARCHAR(15),
    authorName VARCHAR(50),
    count INT,
     CONSTRAINT PRIMARY KEY (bookId)
);
SHOW TABLES ;
DESCRIBE book;
#=========================
DROP TABLE IF EXISTS author;
CREATE TABLE IF NOT EXISTS author(
    authorId VARCHAR(10),
    authorName VARCHAR(50),
    CONSTRAINT PRIMARY KEY (authorId)
);
#========================
DROP TABLE IF EXISTS bookCase;
CREATE TABLE IF NOT EXISTS bookCase(
     bookCaseNumber VARCHAR(10),
     bookType VARCHAR(50),
     bookLanguage VARCHAR(15),
     CONSTRAINT PRIMARY KEY (bookCaseNumber)
);
#========================
DROP TABLE IF EXISTS member;
CREATE TABLE IF NOT EXISTS member(
    memberId VARCHAR(10),
    memName VARCHAR(50),
    address VARCHAR(50),
    NIC VARCHAR(12),
    mobile INT(10),
    CONSTRAINT PRIMARY KEY (memberId)
);

#===========================

DROP TABLE IF EXISTS borrow;
CREATE TABLE IF NOT EXISTS borrow(
    borrowId VARCHAR(10),
    memberId VARCHAR(10),
    memName VARCHAR(50),
    borrowDate DATE,
    borrowTime VARCHAR(8),
    reserveDate VARCHAR(11) NOT NULL DEFAULT 'Borrowed',
    CONSTRAINT PRIMARY KEY (borrowId),
    CONSTRAINT FOREIGN KEY (memberId)REFERENCES member(memberId) ON DELETE CASCADE ON UPDATE CASCADE
);
#===========================

DROP TABLE IF EXISTS borrowDetails;
CREATE TABLE IF NOT EXISTS borrowDetails(
    borrowId VARCHAR(10),
    bookId VARCHAR(10),
    bookName VARCHAR(50),
    borrowDate DATE,
    CONSTRAINT PRIMARY KEY (borrowId,bookId),
    CONSTRAINT FOREIGN KEY (borrowId)REFERENCES borrow (borrowId) ON DELETE CASCADE ON UPDATE CASCADE ,
    CONSTRAINT FOREIGN KEY (bookId) REFERENCES book(bookId)ON UPDATE CASCADE ON DELETE CASCADE
);

#==========================

DROP TABLE IF EXISTS donate;
CREATE TABLE IF NOT EXISTS donate(
     donateId VARCHAR(10),
     personName VARCHAR(50),
     donateDate DATE,
     CONSTRAINT PRIMARY KEY (donateId)
);
#===========================

DROP TABLE IF EXISTS donatedBooks;
CREATE TABLE IF NOT EXISTS donatedBooks(
    donateId varchar(10),
    bookName varchar(50),
    qty int
);


DROP TABLE IF EXISTS reserveBook;
CREATE TABLE IF NOT EXISTS reserveBook(
    borrowId VARCHAR(10),
    reserveDate DATE,
    CONSTRAINT PRIMARY KEY (borrowid),
    CONSTRAINT FOREIGN KEY (borrowId) REFERENCES borrow(borrowId) ON UPDATE CASCADE ON DELETE CASCADE
);


select b.borrowId,b.memberId ,b.memName ,b.borrowDate ,b.reserveDate, bD.bookId  From borrow as b inner join borrowDetails as bD on b.borrowId = bD.borrowId;

SELECT b.borrowId,b.memberId , b.memName ,bd.bookId, bd.bookName FROM borrow AS b INNER JOIN borrowDetails AS bD ON b.borrowId  = bD.borrowId;

select count(bookId) from book;

select bookId , count from book where bookId='?';

