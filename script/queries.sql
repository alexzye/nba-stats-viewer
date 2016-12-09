SELECT p.Player, SUM(PTS) AS total 
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
SELECT Player, SUM(PTS) as total
FROM Players
WHERE Player IN (SELECT Player FROM MVP)
GROUP BY Player
HAVING SUM(PTS) >=
(
    select MAX(points) from 
    (
        SELECT SUM(PTS) AS points
        FROM Players
        WHERE Player IN 
        (
            SELECT Player FROM MVP
        )
        GROUP BY Player
    ) a
);
SELECT t.Name, SUM(FGM)/SUM(FGA) as total 
FROM Players p, Teams t
WHERE p.Team = t.Abbrev
AND t.Abbrev != "TOT"
GROUP BY Team
HAVING SUM(FGM)/SUM(FGA) <=
(
    select MIN(percent) FROM
    (
        SELECT Team, SUM(FGM)/SUM(FGA) AS percent 
        FROM Players
        GROUP BY Team
    ) a
) + 0.01
ORDER BY total ASC
LIMIT 1;
SELECT t.Name, SUM(FGM)/SUM(FGA) as total 
FROM Players p, Teams t
WHERE p.Team = t.Abbrev
AND t.Abbrev != "TOT"
GROUP BY Team
HAVING SUM(FGM)/SUM(FGA) >=
(
    select MAX(percent) FROM
    (
        SELECT Team, SUM(FGM)/SUM(FGA) AS percent 
        FROM Players
        GROUP BY Team
    ) a
) - 0.01
ORDER BY total DESC
LIMIT 1;
SELECT Teams.Name, MAX(a.Teamcount) AS total
FROM Champions, Teams,
    (
        SELECT Count(Champions.Team) AS Teamcount, Champions.Team AS T
        FROM Champions, Teams   
        WHERE Teams.`Abbrev` = `Champions`.`Team`
        GROUP BY T
        ORDER BY Teamcount DESC
    ) a
WHERE Champions.Team = a.T and Teams.`Abbrev` = a.T;
SELECT Player, SUM(FTM)/SUM(FTA) as total 
from Players p
WHERE FTA > 0
AND FTM > 0 
GROUP BY Player
HAVING SUM(FTM)/SUM(FTA) <=
(
    select MIN(ft) FROM (select Player, SUM(FTM)/SUM(FTA) as ft from Players
    WHERE FTA > 0
    AND FTM > 0
    GROUP BY Player) a
) + 0.01
ORDER BY total ASC
LIMIT 1;