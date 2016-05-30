package com.gregswebserver.catan.common.locale.game;

import com.gregswebserver.catan.common.config.ConfigSource;
import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.game.scoring.reporting.scores.ScoreReport;
import com.gregswebserver.catan.common.locale.LocalizedPrinter;

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
