CREATE TABLE `Teams`(
   Abbrev VARCHAR(3) NOT NULL,
   Name VARCHAR(255) NOT NULL,
   City VARCHAR(30),
   State VARCHAR(2),
   PRIMARY KEY(Abbrev)
);

CREATE TABLE `Players`(
   Player VARCHAR(255) NOT NULL,
   Pos VARCHAR(6),
   Age INT,
   Team VARCHAR(3),
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
   PRIMARY KEY(Player, Team, Year),
   FOREIGN KEY(Team) REFERENCES `Teams`(Abbrev)
);

CREATE TABLE `MVP`(
   Player VARCHAR(255) NOT NULL,
   Team VARCHAR(3) NOT NULL,
   Year INT NOT NULL,
   PRIMARY KEY(Player, Year),
   FOREIGN KEY(Player, Team, Year) REFERENCES `Players`(Player, Team, Year),
   FOREIGN KEY(Team) REFERENCES `Teams`(Abbrev)
);

CREATE TABLE `Champions`(
   Team VARCHAR(3) NOT NULL,
   Year INT NOT NULL,
   PRIMARY KEY(Team, Year),
   FOREIGN KEY(Team) REFERENCES `Teams`(Abbrev)
);
