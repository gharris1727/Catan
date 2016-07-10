package catan.client.ui.game;

import catan.client.graphics.masks.RenderMask;
import catan.client.graphics.screen.GraphicObject;
import catan.client.graphics.ui.ConfigurableScreenRegion;
import catan.client.graphics.ui.EdgedTiledBackground;
import catan.client.graphics.ui.TiledBackground;
import catan.client.graphics.ui.UIConfig;
import catan.common.game.gamestate.DevelopmentCard;
import catan.common.game.util.EnumCounter;
import catan.common.game.util.GameResource;
import catan.common.resources.GraphicSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by greg on 6/4/16.
 * A list of cards that displays each card individually, overlapping if necessary.
 */
public class CardList extends ConfigurableScreenRegion {

    //Instance information
    private final EnumCounter<GameResource> resources;
    private final EnumCounter<DevelopmentCard> development;
    private final List<CardIcon> icons;

    //Configuration dependencies
    private GraphicSet resourceIcons;
    private GraphicSet developmentIcons;

    public CardList(String name, int priority, String configKey, EnumCounter<GameResource> resources, EnumCounter<DevelopmentCard> development) {
        super(name, priority, configKey);
        this.resources = resources;
        this.development = development;
        icons = new ArrayList<>();
        enableTransparency();
    }

    @Override
    public void loadConfig(UIConfig config) {
        resourceIcons = new GraphicSet(config.getLayout(), "resource.icons", null);
        developmentIcons = new GraphicSet(config.getLayout(), "development.icons", null);
    }

    @Override
    protected void resizeContents(RenderMask mask) {
    }

    @Override
    protected void renderContents() {
        assertRenderable();
        clear();
        icons.clear();
        if (resources != null) {
            for (GameResource resource : resources) {
                for (int i = 0; i < resources.get(resource); i++) {
                    GameResourceIcon icon = new GameResourceIcon(icons.size(), resource);
                    icon.setConfig(getConfig());
                    icons.add(icon);
                    add(icon);
                }
            }
        }
        if (development != null) {
            for (DevelopmentCard card : development) {
                for (int i = 0; i < development.get(card); i++) {
                    DevelopmentCardIcon icon = new DevelopmentCardIcon(icons.size(), card);
                    icon.setConfig(getConfig());
                    icons.add(icon);
                    add(icon);
                }
            }
        }
        if (icons.size() == 1) {
            center(icons.get(0));
        } else if (icons.size() > 1){
            int width = getMask().getWidth() - resourceIcons.getMask().getWidth();
            double step = ((double) width) / (icons.size() - 1);
            double x = 0;
            for (CardIcon icon : icons) {
                center(icon).x = (int) x;
                x += step;
            }
        }
    }

    private abstract class CardIcon extends ConfigurableScreenRegion {

        private CardIcon(String name, int priority, String configKey) {
            super(name, priority, configKey);
        }
    }

    private class GameResourceIcon extends CardIcon {

        //Instance information
        private final GameResource gameResource;

        //Sub-regions
        private final TiledBackground background;
        private final GraphicObject icon;

        private GameResourceIcon(int priority, GameResource gameResource) {
            super("GameResourceIcon", priority, "resource");
            this.gameResource = gameResource;
            //Create sub-regions
            enableTransparency();
            background = new EdgedTiledBackground();
            icon = new GraphicObject("GameResourceIconImage", 1, null);
            //Add everything to the screen.
            add(background).setClickable(this);
            add(icon).setClickable(this);
        }

        @Override
        protected void resizeContents(RenderMask mask) {
            background.setMask(mask);
        }

        @Override
        protected void renderContents() {
            setMask(resourceIcons.getMask());
            assertRenderable();
            icon.setGraphic(resourceIcons.getGraphic(gameResource.ordinal()));
        }
    }

    private class DevelopmentCardIcon extends CardIcon {

        //Instance information
        private final DevelopmentCard developmentCard;

        //Sub-regions
        private final TiledBackground background;
        private final GraphicObject icon;

        private DevelopmentCardIcon(int priority, DevelopmentCard developmentCard) {
            super("DevelopmentCardIcon", priority, "development");
            this.developmentCard = developmentCard;
            //Create sub-regions
            enableTransparency();
            background = new EdgedTiledBackground();
            icon = new GraphicObject("DevelopmentCardIconImage", 1, null);
            //Add everything to the screen.
            add(background).setClickable(this);
            add(icon).setClickable(this);
        }

        @Override
        protected void resizeContents(RenderMask mask) {
            background.setMask(mask);
        }

        @Override
        protected void renderContents() {
            setMask(resourceIcons.getMask());
            icon.setGraphic(developmentIcons.getGraphic(developmentCard.ordinal()));
        }
    }
}
