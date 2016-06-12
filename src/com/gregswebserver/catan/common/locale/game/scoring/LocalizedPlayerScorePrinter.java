package com.gregswebserver.catan.common.locale.game.scoring;

import com.gregswebserver.catan.common.config.ConfigSource;
import com.gregswebserver.catan.common.game.scoring.reporting.player.PlayerScoreReport;
import com.gregswebserver.catan.common.locale.LocalizedPrinter;

/**
 * Created by greg on 5/28/16.
 * A LocalizedPrinter responsible for printing
 */
public class LocalizedPlayerScorePrinter extends LocalizedPrinter<PlayerScoreReport> {

    public LocalizedPlayerScorePrinter(ConfigSource locale) {
        super(locale);
    }

    @Override
    public String getLocalization(PlayerScoreReport instance) {
        StringBuilder output = new StringBuilder();
        for (String category : instance) {
            if (instance.getPoints(category) > 0) {
                if (output.length() != 0)
                    output.append('\n');
                output.append(getLocalization(category));
                output.append(" \t");
                output.append(instance.getPoints(category));
            }
        }
        return output.toString();
    }
}
