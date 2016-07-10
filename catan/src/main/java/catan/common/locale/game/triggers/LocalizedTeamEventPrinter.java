package catan.common.locale.game.triggers;

import catan.common.config.ConfigSource;
import catan.common.game.teams.TeamEvent;
import catan.common.locale.LocalizedEnumPrinter;
import catan.common.locale.LocalizedPrinter;

/**
 * Created by greg on 6/11/16.
 * LocalizedPrinter for events affecting the teams.
 */
public class LocalizedTeamEventPrinter extends LocalizedPrinter<TeamEvent> {

    private final LocalizedEnumPrinter teamPrinter;
    private final LocalizedEnumPrinter typePrinter;

    public LocalizedTeamEventPrinter(ConfigSource locale) {
        super(locale);
        teamPrinter = new LocalizedEnumPrinter(locale.narrow("game.teamcolor"));
        typePrinter = new LocalizedEnumPrinter(locale.narrow("game.team"));
    }

    @Override
    public String getLocalization(TeamEvent instance) {
        switch (instance.getType()) {
            case Use_Robber:
            case Build_First_Outpost:
            case Build_Second_Outpost:
            case Build_Free_Road:
            case Steal_Resources:
            case Activate_RoadBuilding:
                return teamPrinter.getLocalization(instance.getOrigin()) + " " +
                    typePrinter.getLocalization(instance.getType());
            case Activate_Robber:
            case Finish_Setup_Turn:
            case Finish_Turn:
                return null;
        }
        return null;
    }
}
