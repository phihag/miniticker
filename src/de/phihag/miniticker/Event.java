package de.phihag.miniticker;

import de.phihag.miniticker.http.SetPlayersHandler.SetPlayersRequest;
import de.phihag.miniticker.http.SetScoreHandler.SetScoreRequest;
import de.phihag.miniticker.sftp.SFTPChangeListener;

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
	private transient SFTPChangeListener cl;

	public void load(Event loaded) {
		this.team_names = loaded.team_names;
		this.id = loaded.id;
		this.league_key = loaded.league_key;
		this.matches = loaded.matches;
		this.location = loaded.location;
		this.starttime = loaded.starttime;
		this.date = loaded.date;
		this.all_players = loaded.all_players;
		this.backup_players = loaded.backup_players;
		this.present_players = loaded.present_players;
		this.notes = loaded.notes;
		this.protest = loaded.protest;
		this.umpires = loaded.umpires;
		this.team_competition = loaded.team_competition;
		this.courts = loaded.courts;
	}

	public void select(Event e) {
		load(e);
		cl.updateIndex(this);
		for (Match m: matches) {
			cl.updateMatch(this, m);
		}
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
			cl.updateMatch(this, m);
		}
		cl.updateIndex(this);
	}

	public void setScore(SetScoreRequest ssr) {
		for (Match m : matches) {
			if (m.setup.match_id.equals(ssr.match_id)) {
				m.presses_json = ssr.presses_json;
				m.network_score = ssr.network_score;
				cl.updateMatch(this, m);
			}
		}
		
		for (Court c : courts) {
			if (c.court_id.equals(ssr.court_id)) {
				c.match_id = ssr.match_id;
			}
		}
	}

	public void setChangeListener(SFTPChangeListener cl) {
		this.cl = cl;
	}
	
	public int[] matchScore() {
		int[] res = {0, 0};
		for (Match m: matches) {
			int winner = m.winner();
			if (winner != 0) {
				res[winner - 1]++;
			}
		}
		return res;
	}
}
