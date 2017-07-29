package catan.common.util;

import catan.junit.UnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

/**
 * Created by greg on 7/9/17.
 * Unit tests for the BlockChain class.
 */
@Category(UnitTests.class)
public class BlockChainTest {

    private Random random = new Random();

    @Test
    public void testConstructor() throws NoSuchAlgorithmException {
        BlockChain<String> bc = new BlockChain<>("SHA");
        Assert.assertFalse(bc.iterator().hasNext());
    }

    @Test
    public void testAddBlockNoParent() throws NoSuchAlgorithmException {
        BlockChain<String> bc = new BlockChain<>("SHA");
        BigInteger key = bc.addBlock(null, "Hello");
        Assert.assertEquals("Hello", bc.get(key));
    }

    @Test
    public void testAddBlockIsolated() throws NoSuchAlgorithmException {
        BlockChain<String> bc = new BlockChain<>("SHA");
        BigInteger parent = new BigInteger(160, random);
        BigInteger child = bc.addBlock(parent, "Hello");
        Assert.assertEquals(parent, bc.getParent(child));
    }

    @Test
    public void testAddBlockLinked() throws NoSuchAlgorithmException {
        BlockChain<String> bc = new BlockChain<>("SHA");
        BigInteger parent = bc.addBlock(null, "Parent");
        BigInteger child = bc.addBlock(parent, "Child");
        Assert.assertTrue(bc.getChildren(parent).hasNext());
        Assert.assertEquals(child, bc.getChildren(parent).next());
    }

    @Test
    public void testAddBlockDuplicate() throws NoSuchAlgorithmException {
        BlockChain<String> bc = new BlockChain<>("SHA");
        BigInteger parent = bc.addBlock(null, "Parent");
        BigInteger childA = bc.addBlock(parent, "ChildA");
        Assert.assertEquals(childA, bc.addBlock(parent, "ChildA"));
    }

    @Test
    public void testNoChildren() throws NoSuchAlgorithmException {
        BlockChain<String> bc = new BlockChain<>("SHA");
        BigInteger parent = bc.addBlock(null, "Parent");
        Assert.assertFalse(bc.getChildren(parent).hasNext());
        Assert.assertFalse(bc.getChildren(new BigInteger(160, random)).hasNext());
    }

    @Test
    public void testMultipleChildren() throws NoSuchAlgorithmException {
        BlockChain<String> bc = new BlockChain<>("SHA");
        BigInteger parent = bc.addBlock(null, "Parent");
        BigInteger childA = bc.addBlock(parent, "ChildA");
        BigInteger childB = bc.addBlock(parent, "ChildB");
        Iterator<BigInteger> children = bc.getChildren(parent);
        Assert.assertTrue(children.hasNext());
        BigInteger first = children.next();
        Assert.assertTrue(children.hasNext());
        BigInteger second = children.next();
        Assert.assertTrue(
                (first.equals(childA) && second.equals(childB)) ||
                (first.equals(childB) && second.equals(childA))
        );
    }

    @Test
    public void testGetFail() throws NoSuchAlgorithmException {
        BlockChain<String> bc = new BlockChain<>("SHA");
        Assert.assertNull(bc.get(new BigInteger(160, random)));
    }

    @Test
    public void testGetParentFail() throws NoSuchAlgorithmException {
        BlockChain<String> bc = new BlockChain<>("SHA");
        Assert.assertNull(bc.get(new BigInteger(160, random)));
    }

    @Test
    public void testResolveGenesis() throws NoSuchAlgorithmException {
        BlockChain<String> bc = new BlockChain<>("SHA");
        BigInteger parent = bc.addBlock(null, "Parent");
        BigInteger child = bc.addBlock(parent, "Child");

        bc = new BlockChain<>("SHA");
        Assert.assertEquals(child, bc.addBlock(parent, "Child"));
        Assert.assertEquals(parent, bc.addBlock(null, "Parent"));

        Assert.assertEquals(parent, bc.resolveGenesis());
    }

    @Test
    public void testResolveGenesisFail() throws NoSuchAlgorithmException {
        BlockChain<String> bc = new BlockChain<>("SHA");
        bc.addBlock(new BigInteger(160, random), "ChildA");
        bc.addBlock(new BigInteger(160, random), "ChildB");
        Assert.assertNull(bc.resolveGenesis());
    }

    @Test
    public void testIterator() throws NoSuchAlgorithmException {
        BlockChain<String> bc = new BlockChain<>("SHA");
        BigInteger parent = bc.addBlock(null, "Parent");
        for (Map.Entry<BigInteger, String> e : bc) {
            Assert.assertEquals(parent, e.getKey());
            Assert.assertEquals("Parent", e.getValue());
        }
    }
}
