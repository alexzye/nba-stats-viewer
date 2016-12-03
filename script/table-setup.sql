/*
   Creates the NBA datatables.

   Authors:
   Alex Ye, aye01
   Robert Franklin Mathews IV, rfmathew
   Esteban Ray Ramos, eramos04
*/

CREATE TABLE `Teams`(
   TeamId INT NOT NULL,
   Name VARCHAR(255) NOT NULL,
   Code VARCHAR(3) NOT NULL,
   City VARCHAR(100) NOT NULL,
   State VARCHAR(2) NOT NULL,
   PRIMARY KEY(TeamId),
   UNIQUE(Name, Year)
);

CREATE TABLE `Players`(
   Player VARCHAR(255) NOT NULL,
   Pos VARCHAR(2),
   Age INT,
   Team INT,
   GP INT,
   GS INT,
   MIN INT,
   FGM INT,
   FGA INT,
   3PM INT,
   3PA INT,
   2PM INT,
   2PA INT,
   FTM INT,
   FTA INT,
   ORB INT,
   DRB INT,
   AST INT,
   STL INT,
   BLK INT,
   TOV INT,
   PF INT,
   PTS INT,
   Year INT NOT NULL,
   PRIMARY KEY(Player, Year)
);

CREATE TABLE `Coaches`(
   CoachId INT NOT NULL, -- This is no longer an identifier for a Coach
   Team INT NOT NULL,
   Year INT NOT NULL,
   FOREIGN KEY(CoachId) REFERENCES `People`(Id),
   PRIMARY KEY(CoachId),
   UNIQUE(CouchId, Team, Year)
);

CREATE TABLE `Playoffs`(
   Date DATE NOT NULL,
   Visitor INT NOT NULL,
   vScore INT NOT NULL,
   Home INT NOT NULL,
   hScore INT NOT NULL,
   GameNumber INT NOT NULL,
   YEAR INT NOT NULL,
   FOREIGN KEY(Home) REFERENCES `Teams`(TeamId),
   FOREIGN KEY(Visitor) REFERENCES `Teams`(TeamId),
   PRIMARY KEY(Home, Visitor, GameNumber, YEAR)
);

CREATE TABLE `MVP`(
   Player INT NOT NULL,
   Year INT NOT NULL,
   PRIMARY KEY(Player, Year),
   FOREIGN KEY(Player) REFERENCES `People`(Id)
);

CREATE TABLE `Champions`(
   Team INT NOT NULL,
   Year INT NOT NULL,
   PRIMARY KEY(Team, Year),
   FOREIGN KEY(Team) REFERENCES `Teams`(TeamId)
);
