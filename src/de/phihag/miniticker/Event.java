package de.phihag.miniticker;

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
	}
}
