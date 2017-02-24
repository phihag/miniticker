package de.phihag.miniticker;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

public class Renderer {
	private Mustache matchTemplate;
	private Mustache overviewTemplate;
	
	private Mustache makeTemplate(String fn) {
		InputStream is = this.getClass().getResourceAsStream(fn);
		String templateStr;
		try {
			templateStr = IOUtils.toString(is, "UTF-8");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		MustacheFactory mf = new DefaultMustacheFactory();
	    Mustache template = mf.compile(new StringReader(templateStr), fn);
	    return template;
	}
	
	public Renderer() {
		matchTemplate = makeTemplate("MatchTemplate.html");
		overviewTemplate = makeTemplate("OverviewTemplate.html");
	}
	
	public String renderMatch(Event e, Match m) {
		Map<String,String> ctx = new HashMap<>();
		ctx.put("presses_json", m.presses_json);
		ctx.put("match_name", m.setup.match_name);
		
		for (int i = 0;i < m.setup.teams.length;i++) {
			Player[] players = m.setup.teams[i].players;
			assert(players.length <= 2);
			if (players.length == 1) {
				ctx.put("team" + i + "players", players[0].name);
			} else if (players.length == 2) {
				ctx.put("team" + i + "players", players[0].name + "/" + players[1].name);
			}
		}

		if (m.network_score != null) {
			for (int game_idx = 0;game_idx < m.network_score.length;game_idx++) {
				int[] setscore = m.network_score[game_idx];
				for (int team_idx = 0;team_idx <= 1;team_idx++) {
					ctx.put("team" + team_idx + "game" + game_idx, String.valueOf(setscore[team_idx]));
				}
			}
		}
		
		StringWriter sw = new StringWriter();
		matchTemplate.execute(sw, ctx);
		return sw.toString();
	}
	
	public String renderOverview(Event e) {
		Map<String, String> ctx = new HashMap<>();
		if ((e.date != null) && ! "".equals(e.date)) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
			Date d;
			try {
				d = sdf.parse(e.date);
			} catch (ParseException pe) {
				throw new RuntimeException(pe);
			}
			ctx.put("date_str", DateUtil.longGermanString(d));
		}
		
		int[] mscores = e.matchScore(); 
		for (int team_idx = 0;team_idx <= 1;team_idx++) {
			if (e.team_names != null) {
				ctx.put("team" + team_idx + "name", e.team_names[team_idx]);
			}
			
			ctx.put("team" + team_idx + "score", String.valueOf(mscores[team_idx]));
		}
	
		ctx.put("starttime", e.starttime);
		ctx.put("location", e.location);
		
		StringWriter sw = new StringWriter();
		overviewTemplate.execute(sw, ctx);
		return sw.toString();
	}
}
