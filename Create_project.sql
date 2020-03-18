Create database Project
go
Use Project
go
Create table Store
(
	S_id char(5),
	S_name nvarchar(50),
	S_ad nvarchar(150),
	S_phone varchar(15),
	S_wifi varchar(20),
	primary key (S_id)
)
go
Insert into Store values ('S0001','Ocha House',N'106 Ông Ích Khiêm, TP Đà Nẵng',0329998124,'ochahouseoik')
go
Create table Employees
(
	E_id char(5),
	E_name nvarchar(50),
	E_dateOfBirth date,
	E_phone varchar(15),
	E_ad nvarchar(50),
	E_identityCard varchar(10),
	E_position varchar(5),
	S_id CHAR(5),
	primary key(E_id),
	foreign key (S_id) references Store
)
go
Insert into Employees values('QL000',N'Phạm Hùng Mạnh','1999-07-11',0329998124,
								N'71 Ngũ Hành Sơn, TP Đà Nẵng','231303093','QL','S0001'),
							('TN001',N'Phạm Hùng Mạnh','1999-07-11',0329998124,
								N'71 Ngũ Hành Sơn, TP Đà Nẵng','231303093','TN','S0001'),
								('PV001',N'Phan Minh Nhân','1999-07-11',054323525,
								N'71 Ngũ Hành Sơn, TP Đà Nẵng','231123493','PV','S0001')
go
Create table Account
(
	A_account varchar(10),
	A_password varchar(10),
	E_id char(5),
	primary key(A_account),
	foreign key (E_id) references Employees
)
go
Insert into Account values('admin','admin','QL000'),
							('0329998124','admin','TN001'),
							('231123493','admin','PV001')
go
Create table Items
(
	I_id  char(5),
	I_name nvarchar(50),
	I_price numeric(15),
	primary key(I_id)
)
go
Insert into Items values ('I0000','',0),
					('I0001',N'Việt Quất Đá Xay',38000),
					('I0002',N'Trà Sữa Truyền Thống',19000),
					('I0003',N'Trà Sữa Olong',19000)					
go
Create table Bills
(
	Bi_id char(5),
	Bi_date date,
	Bi_timeIn time,
	Bi_timeOut time,
	Bi_moneyTotal numeric(15),
	Bi_payments numeric(15),
	Bi_extraMoney numeric(15),
	E_id char(5),
	primary key(Bi_id),
	foreign key (E_id) references Employees
)
go
Insert into Bills values('BI001','2019-09-18','17:39','17:40',76000,100000,24000,'TN001')
go
Create table BillsDetail
(
	Bi_id char(5),
	I_id  char(5),
	I_amount int, 
	I_price numeric(15),
	I_money numeric(15),
	primary key(Bi_id,I_id),
	foreign key (Bi_id) references Bills, 
	foreign key (I_id) references Items
)
go
Insert into BillsDetail values('BI001','I0001',1,38000,38000),
							('BI001','I0002',1,19000,19000),
							('BI001','I0003',1,19000,19000)
go
-- Create index
CREATE INDEX ixAccount
ON Account (A_account);
go
CREATE INDEX ixEmployees
ON Employees (E_id);
go
--Create function
Create function newBillsID()
Returns varchar(5)
AS
BEGIN
	Declare @billsIDMax varchar(5), @IDMax int
	Select @billsIDMax = max(Bi_id)
	From Bills
	Set @IDMax = right(@billsIDMax,LEN(@billsIDMax)-2)+1
	Return 'BI'+   REPLICATE(0,len(@billsIDMax)-len(@IDMax) - 2) + cast((@IDMax) as varchar(5))
END
go
Create function newEmployeesID()
Returns varchar(5)
AS
BEGIN
	Declare @iDMax varchar(5), @IDNew int, @position varchar(5)

	Select top 1 @iDMax = E_id, @position = E_position
	From Employees
	Order by E_id

	Set @IDNew = right(@iDMax, len(@iDMax)-len(@position)) + 1
	Return @position +   REPLICATE(0,len(@iDMax)-len(@IDNew) - len(@position)) + cast(@IDNew as varchar(5))
END
go
Create function newItemsID()
Returns varchar(5)
AS
BEGIN
	Declare @iDMax varchar(5), @IDNew int

	Select @iDMax = max(I_id)
	From Items

	Set @IDNew = right(@iDMax, len(@iDMax)-1) + 1
	Return 'I' +   REPLICATE(0,len(@iDMax)-len(@IDNew) - 1) + cast(@IDNew as varchar(5))
END
go
Create function checkEmployessID(
	@employeesID varchar(5)
)
Returns bit
AS
BEGIN
	Declare @res bit
	if(@employeesID in (Select E_id From Employees) )
		set @res = 1;
	else
		set @res = 0
	return @res
END
go
Create function checkAmount(
	@amount varchar(5)
)
Returns bit
AS
BEGIN
	Declare @res bit
	if(@amount > 0 )
		set @res = 1;
	else
		set @res = 0
	return @res
END
go
-- Create trigger
Create TRIGGER trigUpdateBillsDetail
ON BillsDetail
FOR Insert, Update
AS
BEGIN
	Declare @price numeric(15), @money numeric(15), @iID  varchar(5), @bID varchar(5), @amount int
	Select @bID = Bi_id ,@iID = I_id, @amount = I_amount from inserted
	Select @price = I_price from Items where I_id = @iID
	Set @money = @price * @amount
	Update BillsDetail set I_price = @price, I_money = @money where I_id = @iID and Bi_id = @bID 
END
go



