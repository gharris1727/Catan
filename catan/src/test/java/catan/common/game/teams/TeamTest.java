package catan.common.game.teams;

import catan.common.event.EventConsumerException;
import catan.junit.UnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import static catan.common.game.teams.TeamEventType.*;

/**
 * Created by greg on 7/1/17.
 * Unit tests for the Team class
 */
@Category(UnitTests.class)
public class TeamTest {

    private Team team1;
    private Team team2;

    private void assertEquals() {
        TeamEqualityTester.INSTANCE.assertEquals(team1, team2);
    }

    private void testFail(TeamEventType type) {
        assert team1.test(new TeamEvent(TeamColor.None, type, null)) != null;
    }

    private void testSuccess(TeamEventType type) {
        assert team1.test(new TeamEvent(TeamColor.None, type, null)) == null;
    }

    private void executeSuccess(TeamEventType type) throws EventConsumerException {
        team1.execute(new TeamEvent(TeamColor.None, type, null));
    }

    private void executeFail(TeamEventType type) {
        try {
            executeSuccess(type);
            Assert.fail();
        } catch (EventConsumerException ignored) {
            assertEquals();
        }
    }

    private void executeBoth(TeamEventType type) throws EventConsumerException {
        executeSuccess(type);
        team2.execute(new TeamEvent(TeamColor.None, type, null));
        assertEquals();
    }

    @Before
    public void setupTeams() {
        team1 = new Team(TeamColor.None);
        team2 = new Team(TeamColor.None);
        assertEquals();
    }

    @Test
    public void testActivateRobberTest() throws EventConsumerException {
        testSuccess(Activate_Robber);
        assertEquals();

        executeBoth(Activate_Robber);

        testFail(Activate_Robber);
        assertEquals();
    }

    @Test
    public void testActivateRobberExecute() throws EventConsumerException {
        executeBoth(Activate_Robber);

        executeFail(Activate_Robber);
    }

    @Test
    public void testActivateRobberUndo() throws EventConsumerException {
        executeSuccess(Activate_Robber);

        team1.undo();
        assertEquals();
    }

    @Test
    public void testUseRobberTest() throws EventConsumerException {
        testFail(Use_Robber);
        assertEquals();

        executeBoth(Activate_Robber);

        testSuccess(Use_Robber);
        assertEquals();
    }

    @Test
    public void testUseRobberExecute() throws EventConsumerException {
        executeFail(Use_Robber);

        executeBoth(Activate_Robber);
        executeBoth(Use_Robber);

        executeFail(Use_Robber);
    }

    @Test
    public void testUseRobberUndo() throws EventConsumerException {
        executeBoth(Activate_Robber);

        executeSuccess(Use_Robber);
        team1.undo();
        assertEquals();
    }

    @Test
    public void testStealResourcesTest() throws EventConsumerException {
        testFail(Steal_Resources);
        assertEquals();

        executeBoth(Activate_Robber);

        testFail(Steal_Resources);
        assertEquals();

        executeBoth(Use_Robber);

        testSuccess(Steal_Resources);
        assertEquals();
    }

    @Test
    public void testStealResourcesExecute() throws EventConsumerException {
        executeFail(Steal_Resources);
        assertEquals();

        executeBoth(Activate_Robber);

        executeFail(Steal_Resources);
        assertEquals();

        executeBoth(Use_Robber);

        executeBoth(Steal_Resources);

        executeFail(Steal_Resources);
        assertEquals();
    }

    @Test
    public void testStealResourcesUndo() throws EventConsumerException {
        executeBoth(Activate_Robber);
        executeBoth(Use_Robber);

        executeSuccess(Steal_Resources);
        team1.undo();
        assertEquals();
    }

    @Test
    public void testBuildFirstOutpostTest() throws EventConsumerException {
        testSuccess(Build_First_Outpost);
        assertEquals();

        executeBoth(Build_First_Outpost);

        testFail(Build_First_Outpost);
        assertEquals();
    }

    @Test
    public void testBuildFirstOutpostExecute() throws EventConsumerException {
        executeBoth(Build_First_Outpost);

        executeFail(Build_First_Outpost);
        assertEquals();
    }

    @Test
    public void testBuildFirstOutpostUndo() throws EventConsumerException {
        executeSuccess(Build_First_Outpost);
        team1.undo();
        assertEquals();
    }

    @Test
    public void testBuildSecondOutpostTest() throws EventConsumerException {
        testFail(Build_Second_Outpost);
        assertEquals();

        executeBoth(Build_First_Outpost);

        testFail(Build_Second_Outpost);
        assertEquals();

        executeBoth(Build_Free_Road);

        testFail(Build_Second_Outpost);
        assertEquals();

        executeBoth(Finish_Setup_Turn);

        testSuccess(Build_Second_Outpost);
        assertEquals();

        executeBoth(Build_Second_Outpost);

        testFail(Build_Second_Outpost);
        assertEquals();
    }

    @Test
    public void testBuildSecondOutpostExecute() throws EventConsumerException {
        executeFail(Build_Second_Outpost);
        assertEquals();

        executeBoth(Build_First_Outpost);

        executeFail(Build_Second_Outpost);
        assertEquals();

        executeBoth(Build_Free_Road);

        executeFail(Build_Second_Outpost);
        assertEquals();

        executeBoth(Finish_Setup_Turn);

        executeBoth(Build_Second_Outpost);

        executeFail(Build_Second_Outpost);
        assertEquals();
    }

    @Test
    public void testBuildSecondOutpostUndo() throws EventConsumerException {
        executeBoth(Build_First_Outpost);
        executeBoth(Build_Free_Road);
        executeBoth(Finish_Setup_Turn);

        executeSuccess(Build_Second_Outpost);
        team1.undo();
        assertEquals();
    }

    @Test
    public void testActivateRoadbuildingTest() throws EventConsumerException {
        executeBoth(Build_First_Outpost);
        executeBoth(Build_Free_Road);
        executeBoth(Finish_Setup_Turn);
        executeBoth(Build_Second_Outpost);
        executeBoth(Build_Free_Road);
        executeBoth(Finish_Setup_Turn);

        testSuccess(Activate_RoadBuilding);
    }

    @Test
    public void testActivateRoadbuildingExecute() throws EventConsumerException {
        executeBoth(Build_First_Outpost);
        executeBoth(Build_Free_Road);
        executeBoth(Finish_Setup_Turn);
        executeBoth(Build_Second_Outpost);
        executeBoth(Build_Free_Road);
        executeBoth(Finish_Setup_Turn);

        executeBoth(Activate_RoadBuilding);
        executeBoth(Build_Free_Road);
        executeBoth(Build_Free_Road);
        assertEquals();

        executeFail(Build_Free_Road);
        assertEquals();
    }

    @Test
    public void testActivateRoadbuildingUndo() throws EventConsumerException {
        executeBoth(Build_First_Outpost);
        executeBoth(Build_Free_Road);
        executeBoth(Finish_Setup_Turn);
        executeBoth(Build_Second_Outpost);
        executeBoth(Build_Free_Road);
        executeBoth(Finish_Setup_Turn);

        executeSuccess(Activate_RoadBuilding);
        team1.undo();
        assertEquals();
    }

    /*
    @Test
    public void testBuildFreeRoadTest() throws EventConsumerException {

    }

    @Test
    public void testBuildFreeRoadExecute() throws EventConsumerException {

    }

    @Test
    public void testBuildFreeRoadUndo() throws EventConsumerException {

    }

    @Test
    public void testFinishSetupTurnTest() throws EventConsumerException {

    }

    @Test
    public void testFinishSetupTurnExecute() throws EventConsumerException {

    }

    @Test
    public void testFinishSetupTurnUndo() throws EventConsumerException {

    }

    @Test
    public void testFinishTurnTest() throws EventConsumerException {

    }

    @Test
    public void testFinishTurnExecute() throws EventConsumerException {

    }

    @Test
    public void testFinishTurnUndo() throws EventConsumerException {

    }
    */
}
