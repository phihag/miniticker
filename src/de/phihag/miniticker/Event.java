package de.phihag.miniticker;

import de.phihag.miniticker.http.SetPlayersHandler.SetPlayersRequest;
import de.phihag.miniticker.http.SetScoreHandler.SetScoreRequest;

public class Event {
	public String[] team_names;
	public String id;
	public String league_key;
	public Match[] matches;
	public Player[][] all_players;
	public Player[][] backup_players;
	public Player[][] present_players;
	public String location;
	public String date;
	public String starttime;
	public String notes;
	public String protest;
	public String umpires;
	public boolean team_competition;
	public Court[] courts;
	
	public void select(Event e) {
		this.team_names = e.team_names;
		this.id = e.id;
		this.league_key = e.league_key;
		this.matches = e.matches;
		this.location = e.location;
		this.starttime = e.starttime;
		this.date = e.date;
		this.all_players = e.all_players;
		this.backup_players = e.backup_players;
		this.present_players = e.present_players;
		this.notes = e.notes;
		this.protest = e.protest;
		this.umpires = e.umpires;
		this.team_competition = e.team_competition;
		this.courts = e.courts;
	}

	public void setPlayers(SetPlayersRequest spr) {
		this.backup_players = spr.backup_players;
		this.present_players = spr.present_players;

		for (Match m : matches) {
			String match_id = m.setup.match_id;
			Team[] t = spr.teams_by_match.get(match_id);
			if (t == null) {
				continue;
			}
			m.setup.teams = t;
		}
	}

	public void setScore(SetScoreRequest ssr) {
		for (Match m : matches) {
			if (m.setup.match_id.equals(ssr.match_id)) {
				m.presses_json = ssr.presses_json;
				m.network_score = ssr.network_score;
			}
		}
		
		for (Court c : courts) {
			if (c.court_id.equals(ssr.court_id)) {
				c.match_id = ssr.match_id;
			}
		}
	}
}
