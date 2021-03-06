package catan.common.locale.game.scoring;

import catan.common.config.ConfigSource;
import catan.common.game.scoring.reporting.team.TeamScoreReport;
import catan.common.game.teams.TeamColor;
import catan.common.locale.LocalizedEnumPrinter;
import catan.common.locale.LocalizedPrinter;

/**
 * Created by greg on 5/28/16.
 * Object responsible for printing a team score report.
 */
public class LocalizedTeamScorePrinter extends LocalizedPrinter<TeamScoreReport> {

    private final LocalizedEnumPrinter teamColorPrinter;
    private final LocalizedScoreReportPrinter scoreReportPrinter;

    public LocalizedTeamScorePrinter(ConfigSource locale) {
        super(locale);
        teamColorPrinter = new LocalizedEnumPrinter(locale.narrow("game.teamcolor"));
        scoreReportPrinter = new LocalizedScoreReportPrinter(locale);
    }

    @Override
    public String getLocalization(TeamScoreReport instance) {
        StringBuilder output = new StringBuilder();
        output.append("Team Report");
        for (TeamColor teamColor : instance) {
            output.append('\n');
            output.append(teamColorPrinter.getLocalization(teamColor));
            output.append(":\n");
            output.append(scoreReportPrinter.getLocalization(instance.getScoreReport(teamColor)));
        }
        return output.toString();
    }
}
