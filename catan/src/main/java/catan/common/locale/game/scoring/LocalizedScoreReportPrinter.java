package catan.common.locale.game.scoring;

import catan.common.config.ConfigSource;
import catan.common.crypto.Username;
import catan.common.game.scoring.reporting.scores.ScoreReport;
import catan.common.locale.LocalizedPrinter;

/**
 * Created by greg on 5/28/16.
 * A printer responsible for printing ScoreReport objects.
 */
public class LocalizedScoreReportPrinter extends LocalizedPrinter<ScoreReport> {

    private final LocalizedPlayerScorePrinter playerScorePrinter;

    public LocalizedScoreReportPrinter(ConfigSource locale) {
        super(locale);
        playerScorePrinter = new LocalizedPlayerScorePrinter(locale);
    }

    @Override
    public String getLocalization(ScoreReport instance) {
        StringBuilder output = new StringBuilder();
        for (Username username : instance) {
            if (output.length() != 0)
                output.append('\n');
            output.append(playerScorePrinter.getLocalization(instance.getPlayerReport(username)));
        }
        return output.toString();
    }
}
