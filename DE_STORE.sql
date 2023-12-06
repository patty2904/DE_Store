CREATE DATABASE DE_STORE; 
USE DE_STORE;

CREATE TABLE Product(
    ProductID INT AUTO_INCREMENT PRIMARY KEY,
    ProductName VARCHAR(255) NOT NULL,
    ProductPrice NUMERIC(10, 2) NOT NULL,
    ProductOffer ENUM(
        'BUY_ONE_GET_ONE_FREE',
        'THREE_FOR_TWO',
        'FREE_DELIVERY',
        'NONE'
    ) DEFAULT 'NONE'
);

CREATE TABLE Customer(
    CustomerID INT AUTO_INCREMENT PRIMARY KEY,
    CustomerName VARCHAR(255) NOT NULL,
    CustomerLoyaltyCard ENUM('RED', 'BLUE', 'PURPLE', 'NONE') DEFAULT 'NONE'
);

CREATE TABLE Store(
    StoreID INT AUTO_INCREMENT PRIMARY KEY,
    StoreName VARCHAR(255) NOT NULL
);

CREATE TABLE StoreProduct (
    StoreID INT,
    ProductID INT,
    Quantity INT NOT NULL,
    FOREIGN KEY (StoreID) REFERENCES Store(StoreID),
    FOREIGN KEY (ProductID) REFERENCES Product(ProductID),
    PRIMARY KEY (StoreID, ProductID)
);

CREATE TABLE Purchase(
    PurchaseID INT AUTO_INCREMENT PRIMARY KEY,
    PurchaseCustomerID INT NOT NULL,
    PurchaseProductID INT NOT NULL,
    PurchaseStoreID INT NOT NULL,
    PurchaseQuantity INT NOT NULL,
    PurchaseTotal NUMERIC(10, 2) NOT NULL,
    PurchasePaymentMethod ENUM('CREDIT_CARD', 'DEBIT_CARD', 'FINANCE') NOT NULL,
    FOREIGN KEY(PurchaseCustomerID) REFERENCES Customer(CustomerID),
    FOREIGN KEY(PurchaseProductID) REFERENCES Product(ProductID),
    FOREIGN KEY(PurchaseStoreID) REFERENCES Store(StoreID)
);

CREATE TABLE Finance(
    FinanceID INT AUTO_INCREMENT PRIMARY KEY,
    FinanceCustomerID INT NOT NULL,
    FinancePurchaseID INT NOT NULL,
    FinanceAmount NUMERIC(10, 2) NOT NULL,
    FinanceDuration INT NOT NULL,
    FOREIGN KEY(FinanceCustomerID) REFERENCES Customer(CustomerID),
    FOREIGN KEY(FinancePurchaseID) REFERENCES Purchase(PurchaseID)
);

INSERT INTO `Customer` (`CustomerID`, `CustomerName`, `CustomerLoyaltyCard`) VALUES
(1, 'Taylor Swift', 'NONE'),
(2, 'Beyonce', 'RED'),
(3, 'Rihanna', 'BLUE'),
(4, 'Ariana Grande', 'BLUE'),
(5, 'Justin Bieber', 'RED');

INSERT INTO `Product` (`ProductID`, `ProductName`, `ProductPrice`, `ProductOffer`) VALUES
(1, '\'Speak Now\' Cardigan', 70.00, 'NONE'),
(2, 'Light Yellow 1989 Photo Crewneck', 59.89, 'THREE_FOR_TWO'),
(3, 'Welcome to New York V-Neck Pullover', 58.89, 'THREE_FOR_TWO'),
(4, '1989 Seagull Crewneck', 74.89, 'FREE_DELIVERY'),
(5, 'The Story of Us Purple Crop Hoodie', 75.00, 'BUY_ONE_GET_ONE_FREE'),
(6, 'Taylor Swift The Eras Tour Heart Photo Hoodie', 70.00, 'FREE_DELIVERY'),
(7, '1989 (Taylor\'s Version) CD', 12.89, 'FREE_DELIVERY'),
(8, '1989 (Taylor\'s Version) Vinyl', 34.89, 'FREE_DELIVERY'),
(9, 'Speak Now 3LP Orchid Marbled Vinyl', 39.99, 'THREE_FOR_TWO'),
(10, 'Midnights: Jade Green Edition Vinyl', 32.99, 'THREE_FOR_TWO'),
(11, 'Midnights: Mahogany Edition CD', 12.99, 'BUY_ONE_GET_ONE_FREE'),
(12, 'Lover: Colour Vinyl 2LP', 35.99, 'THREE_FOR_TWO'),
(13, '1989 Viewfinder', 19.89, 'THREE_FOR_TWO'),
(14, 'Fearless Ball Ornament', 15.00, 'FREE_DELIVERY'),
(15, '\'Lover\' Snowglobe', 50.00, 'THREE_FOR_TWO');

INSERT INTO `Store` (`StoreID`, `StoreName`) VALUES
(1, 'Central Store'),
(2, 'Edinburgh Store'),
(3, 'London Store'),
(4, 'Paris Store');

INSERT INTO `StoreProduct` (`StoreID`, `ProductID`, `Quantity`) VALUES
(1, 1, 100),
(1, 2, 150),
(1, 3, 200),
(1, 4, 250),
(1, 5, 300),
(1, 6, 350),
(1, 7, 400),
(1, 8, 450),
(1, 9, 500),
(1, 10, 550),
(1, 11, 600),
(1, 12, 650),
(1, 13, 700),
(1, 14, 750),
(1, 15, 800),
(2, 1, 7),
(2, 2, 7),
(2, 3, 9),
(2, 4, 6),
(2, 5, 8),
(2, 6, 5),
(2, 7, 5),
(2, 8, 5),
(2, 9, 5),
(2, 10, 5),
(2, 11, 5),
(2, 12, 5),
(2, 13, 5),
(2, 14, 5),
(2, 15, 5),
(3, 1, 5),
(3, 2, 6),
(3, 3, 8),
(3, 4, 5),
(3, 5, 7),
(3, 6, 5),
(3, 7, 5),
(3, 8, 5),
(3, 9, 5),
(3, 10, 5),
(3, 11, 5),
(3, 12, 5),
(3, 13, 5),
(3, 14, 5),
(3, 15, 5),
(4, 1, 5),
(4, 2, 5),
(4, 3, 5),
(4, 4, 5),
(4, 5, 5),
(4, 6, 5),
(4, 7, 5),
(4, 8, 5),
(4, 9, 5),
(4, 10, 5),
(4, 11, 5),
(4, 12, 5),
(4, 13, 5),
(4, 14, 5),
(4, 15, 5);

ALTER TABLE Purchase MODIFY PurchaseStoreID INT NULL;

