CREATE TABLE `Teams`(
   TeamId INT NOT NULL,
   Name VARCHAR(255) NOT NULL,
   City VARCHAR(100) NOT NULL,
   State VARCHAR(2) NOT NULL,
   Year INT NOT NULL,
   PRIMARY KEY(TeamId),
   UNIQUE(Name, Year)
);

CREATE TABLE `People`(
   Id INT NOT NULL,
   First VARCHAR(100) NOT NULL,
   Middle VARCHAR(100) NOT NULL,
   Last VARCHAR(255) NOT NULL,
   PRIMARY KEY(Id)
);

CREATE TABLE `Players`(
   PlayerId INT NOT NULL,
   Team INT NOT NULL,
   Year INT NOT NULL,
   Height FLOAT,
   Weight FLOAT,
   GP FLOAT,
   MIN FLOAT,
   PTS FLOAT,
   FGM FLOAT,
   FGA FLOAT,
   3PM FLOAT,
   3PA FLOAT,
   PlusMinus FLOAT,
   Rookie BOOLEAN,
   Jersey INT,
   FOREIGN KEY(PlayerId) REFERENCES `People`(Id),
   PRIMARY KEY(PlayerId, Team, Year)
);

CREATE TABLE `Coaches`(
   CoachId INT NOT NULL,
   Team INT NOT NULL,
   Year INT NOT NULL,
   FOREIGN KEY(CoachId) REFERENCES `People`(Id),
   PRIMARY KEY(CoachId),
   UNIQUE(CouchId, Team, Year)
);

CREATE TABLE `Playoffs`(
   Home INT NOT NULL,
   Away INT NOT NULL,
   GameNumber INT NOT NULL,
   Round INT NOT NULL,
   Winner ENUM("HOME", "AWAY"),
   Year INT NOT NULL,
   FOREIGN KEY(Home) REFERENCES `Teams`(TeamId),
   Primary Key(Home, Away, GameNumber, Round, Year)
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
