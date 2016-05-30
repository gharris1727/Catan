package com.gregswebserver.catan.common.locale.game;

import com.gregswebserver.catan.common.config.ConfigSource;
import com.gregswebserver.catan.common.game.teams.TeamColor;
import com.gregswebserver.catan.common.locale.LocalizedPrinter;

/**
 * Created by greg on 5/30/16.
 * Printer for localized names of team colors.
 */
public class LocalizedTeamColorPrinter extends LocalizedPrinter<TeamColor> {

    public LocalizedTeamColorPrinter(ConfigSource locale) {
        super(locale);
    }

    @Override
    public String getLocalization(TeamColor instance) {
        return getLocalization("game.teamcolor." + instance.name);
    }
}
