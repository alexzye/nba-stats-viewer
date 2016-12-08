-- Get All MVP's who were also on Championship teams and the year

Select MVP.Player, Teams.`Name`, MVP.year
From Champions, MVP, Teams
Where Champions.Team = MVP.Team 
	AND Champions.Year = MVP.Year
	AND Teams.`Abbrev` = MVP.`Team`
	Order by Year DESC; 
	
-- For each year, list the player name and team of the oldest player.

Select Players.`Year`, Players.`Player`, Teams.`Name`
From Players, Teams, 
	(Select MAX(Players.`Age`) as Oldest
		From Players
		Group by Year) a
Where Players.`Team` = Teams.`Abbrev` AND Players.Age = a.Oldest
		Group by Year;

-- For each team, return the name of the player who made the most 3 pointers
Select Teams.Name, Players.`Player`
From Players, Teams,
	(Select MAX(Players.`3PM`) as MAX3, Players.`Team` as PT
		From Players, Teams
		Where Players.`Team` = Teams.`Abbrev`
		Group by Players.`Team`) a
Where Players.Team = Teams.`Abbrev` AND Players.`3PM` = a.MAX3
	AND a.PT = Players.`Team`
	Group by Players.`Team`;
	

-- Find the team that has won the most Championships
Select Teams.Name, MAX(a.Teamcount) as "# Championships Won"
From Champions, Teams,
	(Select Count(Champions.Team) as Teamcount, Champions.Team as T
			From Champions, Teams	
			Where Teams.`Abbrev` = `Champions`.`Team`
   			Group by T
			Order by Teamcount DESC) a
Where Champions.Team = a.T and Teams.`Abbrev` = a.T;

-- Find the Players who did not play during their season, report player name, team name and year
Select Players.`Player`, Teams.`Name`, Players.Year
From Players, Teams
Where Players.`Team` = Teams.`Abbrev` AND Players.`MIN` = 0
Order By Year;

		


SELECT p.Player, SUM(PTS) AS TotalPts 
FROM `Players` p, `Teams` t
WHERE p.Team = t.Abbrev
GROUP BY p.Player
HAVING SUM(p.PTS) >= 
(
    SELECT MAX(pt) FROM 
    (
        SELECT SUM(p1.PTS) AS pt 
        FROM `Players` p1
        GROUP BY(p1.Player)
    ) a
);