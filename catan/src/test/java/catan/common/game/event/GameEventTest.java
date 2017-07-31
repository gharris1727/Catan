package catan.common.game.event;

import catan.common.crypto.Username;
import catan.common.event.ExternalEvent;
import catan.common.game.GameTestUtils;
import catan.common.game.board.hexarray.Coordinate;
import catan.common.game.gameplay.trade.Trade;
import catan.common.game.util.EnumAccumulator;
import catan.common.game.util.GameResource;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static catan.common.game.event.GameEventType.*;

/**
 * Created by greg on 7/23/17.
 * Class to test serialization and deserialization of GameEvents.
 */
public class GameEventTest {

    private final Username greg = new Username("Greg");
    private final Username bob = new Username("Bob");

    private static void assertCorrectlySerialized(GameEvent event) throws IOException {
        byte[] serialized = event.serialize();
        GameEvent deserialized = (GameEvent) ExternalEvent.deserialize(serialized);
        byte[] serializedTwice = deserialized.serialize();
        GameEvent deserializedTwice = (GameEvent) ExternalEvent.deserialize(serializedTwice);
        byte[] serializedThrice = deserializedTwice.serialize();
        GameEvent deserializedThrice = (GameEvent) ExternalEvent.deserialize(serializedThrice);
        Assert.assertEquals(deserializedTwice, deserializedThrice);
        Assert.assertArrayEquals(serializedTwice, serializedThrice);
        Assert.assertEquals(deserialized, deserializedTwice);
        Assert.assertArrayEquals(serialized, serializedTwice);
        Assert.assertEquals(event, deserialized);
    }
    @Test
    public void testStartSerialization() throws IOException {
        GameEvent event = new GameEvent(null, Start, GameTestUtils.createSettings(124102134L, Arrays.asList(greg, bob)));
        assertCorrectlySerialized(event);
    }

    @Test
    public void testTurn_AdvanceSerialization() throws IOException {
        GameEvent event = new GameEvent(bob, Turn_Advance, null);
        assertCorrectlySerialized(event);
    }
    @Test
    public void testPlayer_Move_RobberSerialization() throws IOException {
        GameEvent event = new GameEvent(greg, Player_Move_Robber, new Coordinate(23,3));
        assertCorrectlySerialized(event);
    }
    @Test
    public void testBuild_SettlementSerialization() throws IOException {
        GameEvent event = new GameEvent(greg, Build_Settlement, new Coordinate(23,3));
        assertCorrectlySerialized(event);
    }
    @Test
    public void testBuild_CitySerialization() throws IOException {
        GameEvent event = new GameEvent(greg, Build_City, new Coordinate(23,3));
        assertCorrectlySerialized(event);
    }
    @Test
    public void testBuild_RoadSerialization() throws IOException {
        GameEvent event = new GameEvent(greg, Build_Road, new Coordinate(23,3));
        assertCorrectlySerialized(event);
    }
    @Test
    public void testBuy_DevelopmentSerialization() throws IOException {
        GameEvent event = new GameEvent(bob, Buy_Development, null);
        assertCorrectlySerialized(event);
    }
    @Test
    public void testOffer_TradeSerialization() throws IOException {
        Trade trade = new Trade(GameResource.Brick, GameResource.Grain, 3);
        GameEvent event = new GameEvent(bob, Offer_Trade, trade);
        assertCorrectlySerialized(event);
    }
    @Test
    public void testCancel_TradeSerialization() throws IOException {
        GameEvent event = new GameEvent(bob, Cancel_Trade, null);
        assertCorrectlySerialized(event);
    }
    @Test
    public void testMake_TradeSerialization() throws IOException {
        Trade trade = new Trade(GameResource.Brick, GameResource.Grain, 3);
        GameEvent event = new GameEvent(bob, Make_Trade, trade);
        assertCorrectlySerialized(event);
    }
    @Test
    public void testDiscard_ResourcesSerialization() throws IOException {
        GameEvent event = new GameEvent(greg, Discard_Resources, new EnumAccumulator<>(GameResource.class));
        assertCorrectlySerialized(event);
    }
    @Test
    public void testSteal_ResourcesSerialization() throws IOException {
        GameEvent event = new GameEvent(greg, Steal_Resources, new Coordinate(23,3));
        assertCorrectlySerialized(event);
    }
    @Test
    public void testPlay_RoadBuildingSerialization() throws IOException {
        GameEvent event = new GameEvent(bob, Play_RoadBuilding, null);
        assertCorrectlySerialized(event);
    }
    @Test
    public void testPlay_YearOfPlentySerialization() throws IOException {
        GameEvent event = new GameEvent(greg, Discard_Resources, new EnumAccumulator<>(GameResource.class));
        assertCorrectlySerialized(event);
    }
    @Test
    public void testPlay_MonopolySerialization() throws IOException {
        GameEvent event = new GameEvent(bob, Play_Monopoly, GameResource.Brick);
        assertCorrectlySerialized(event);
    }
}
