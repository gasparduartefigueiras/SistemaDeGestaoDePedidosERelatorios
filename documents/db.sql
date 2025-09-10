-- CLIENT
CREATE TABLE Client (
                        Id        UNIQUEIDENTIFIER NOT NULL PRIMARY KEY,
                        Name      NVARCHAR(150)    NOT NULL,
                        Email     NVARCHAR(254)    NOT NULL UNIQUE,
                        CreatedAt DATETIME2        NOT NULL
);

-- ORDERS (FK por Email para Client)
CREATE TABLE Orders (
                        Id                 UNIQUEIDENTIFIER NOT NULL PRIMARY KEY,
                        CustomerName       NVARCHAR(150)    NOT NULL,
                        CustomerEmail      NVARCHAR(254)    NOT NULL,
                        OrderValue         DECIMAL(12,2)    NOT NULL,
                        CreationDate       DATETIME2        NOT NULL,
                        Status             NVARCHAR(20)     NOT NULL, -- PENDING | APPROVED | REJECTED
                        ValidationMessage  NVARCHAR(500)    NULL,
                        CONSTRAINT FK_Order_ClientEmail FOREIGN KEY (CustomerEmail) REFERENCES Client(Email)
);

-- ORDER STATUS HISTORY (UUID como PK; liga à Order por OrderId)
CREATE TABLE OrderStatusHistory (
                                    Id            UNIQUEIDENTIFIER NOT NULL PRIMARY KEY,
                                    OrderId       UNIQUEIDENTIFIER NOT NULL,
                                    InitialStatus NVARCHAR(20)     NOT NULL,
                                    FinalStatus   NVARCHAR(20)     NOT NULL,
                                    Reason        NVARCHAR(500)    NULL,
                                    ChangedBy     NVARCHAR(100)    NOT NULL,
                                    ChangedAt     DATETIME2        NOT NULL,
                                    CONSTRAINT FK_History_Order FOREIGN KEY (OrderId) REFERENCES Orders(Id)
);

-- ERROR LOG (OrderId opcional)
CREATE TABLE ErrorLog (
                          Id          UNIQUEIDENTIFIER NOT NULL PRIMARY KEY,
                          Level       NVARCHAR(10)     NOT NULL,          -- ERROR | WARN | INFO
                          Message     NVARCHAR(2000)   NOT NULL,
                          LoggedAt    DATETIME2        NOT NULL,
                          OrderId     UNIQUEIDENTIFIER NULL,
                          CONSTRAINT FK_Error_Order FOREIGN KEY (OrderId) REFERENCES Orders(Id)
);

-- ÍNDICES
CREATE INDEX IX_Orders_Status_Created   ON Orders(Status, CreationDate DESC);
CREATE INDEX IX_OSH_Order_ChangedAt     ON OrderStatusHistory(OrderId, ChangedAt DESC);
CREATE INDEX IX_ErrorLog_LoggedAt       ON ErrorLog(LoggedAt DESC);

-- CHECKS (opcionais)
ALTER TABLE Orders ADD CONSTRAINT CK_Orders_Status
    CHECK (Status IN ('PENDING','APPROVED','REJECTED'));

ALTER TABLE OrderStatusHistory ADD CONSTRAINT CK_OSH_Status
    CHECK (InitialStatus IN ('PENDING','APPROVED','REJECTED')
        AND FinalStatus   IN ('PENDING','APPROVED','REJECTED'));

ALTER TABLE ErrorLog ADD CONSTRAINT CK_ErrorLog_Level
    CHECK (Level IN ('ERROR','WARN','INFO'));