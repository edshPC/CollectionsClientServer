package edsh.helpers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.NONE)
public class SQLRequests {

    public static final String tablesCreation = """
CREATE TABLE IF NOT EXISTS Users (
  login varchar(16) primary key not null,
  passwordHash text not null
);

CREATE TABLE IF NOT EXISTS Events (
    id bigint primary key generated by default as identity check ( id > 0 ),
    name text not null,
    date date not null,
    minAge bigint not null,
    ticketsCount bigint not null check ( ticketsCount > 0 ),
    eventType text not null
);

CREATE TABLE IF NOT EXISTS Tickets (
    id bigint primary key generated by default as identity check ( id > 0 ),
    name text not null,
    x float4 not null check ( x <= 542 ),
    y int not null check ( y <= 203 ),
    creationDate timestamptz not null default now(),
    price bigint not null check ( price > 0 ),
    comment text,
    type text not null,
    event bigint not null references Events on delete cascade,
    owner varchar references Users
);
""";

    public static final String getAllTickets = """
SELECT * FROM tickets
    JOIN events on events.id = tickets.event
""";

//    public static final String updateEvent = """
//INSERT INTO events (id, name, date, minage, ticketscount, eventtype) VALUES
//    (?, ?, ?, ?, ?, ?) ON CONFLICT (id) DO UPDATE
//        SET name = excluded.name,
//            date = excluded.date,
//            minage = excluded.minage,
//            ticketscount = excluded.ticketscount,
//            eventtype = excluded.eventtype;
//""";
//    public static final String updateTicket = """
//INSERT INTO tickets (id, name, x, y, creationdate, price, comment, type, event) VALUES
//    (?, ?, ?, ?, ?, ?, ?, ?, ?) ON CONFLICT (id) DO UPDATE
//        SET name = excluded.name,
//            x = excluded.x,
//            y = excluded.y,
//            creationdate = excluded.creationdate,
//            price = excluded.price,
//            comment = excluded.comment,
//            type = excluded.type,
//            event = excluded.event;
//""";

    public static final String updateEvent = """
UPDATE events
        SET name = ?,
            date = ?,
            minage = ?,
            ticketscount = ?,
            eventtype = ?
        WHERE id = ?;
""";
    public static final String updateTicket = """
UPDATE tickets
        SET name = ?,
            x = ?,
            y = ?,
            creationdate = ?,
            price = ?,
            comment = ?,
            type = ?,
            event = ?
        WHERE id = ?;
""";

    public static final String addEvent = """
INSERT INTO events (name, date, minage, ticketscount, eventtype) VALUES
    (?, ?, ?, ?, ?) RETURNING id;
""";
    public static final String addTicket = """
INSERT INTO tickets (name, x, y, creationdate, price, comment, type, event, owner) VALUES
    (?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id;
""";

    public static final String hasTickets = "SELECT exists(SELECT 1 FROM tickets);";
    public static final String hasTicket = "SELECT exists(SELECT 1 FROM tickets WHERE id = ?);";
    public static final String hasLogin = "SELECT exists(SELECT 1 FROM users WHERE login = ?);";

    public static final String removeTicket = "DELETE FROM events WHERE id = (SELECT event FROM tickets WHERE id = ? LIMIT 1);";
    public static final String getAllIds = "SELECT id FROM tickets;";

    public static final String addUser = "INSERT INTO users (login, passwordhash) VALUES (?, ?);";
    public static final String getOwner = "SELECT owner FROM tickets WHERE id = ? LIMIT 1";
    public static final String getPassHash = "SELECT passwordhash FROM users WHERE login = ? LIMIT 1;";

    public static final String getOldestTicketCreationTime  = "SELECT min(creationdate) FROM tickets;";

}
