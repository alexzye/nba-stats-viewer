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