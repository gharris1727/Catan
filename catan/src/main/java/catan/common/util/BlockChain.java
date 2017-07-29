package catan.common.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * Created by greg on 7/4/17.
 * Implementation of a MessageDigest-based blockchain value store.
 */
public class BlockChain<T> implements Iterable<Map.Entry<BigInteger, T>> {

    private final MessageDigest digester;
    private final HashMap<BigInteger, BlockchainNode> nodes;
    private final HashSet<BigInteger> unlinkedIDs;

    public BlockChain(String hashType) throws NoSuchAlgorithmException {
        this.digester = MessageDigest.getInstance(hashType);
        this.nodes = new HashMap<>();
        this.unlinkedIDs = new HashSet<>();
    }

    public BigInteger addBlock(BigInteger parent, T value) {
        if (parent == null) {
            parent = new BigInteger("0", 16);
        }
        BlockchainNode node = new BlockchainNode(parent, value);
        if (!nodes.containsKey(node.identity)) {
            nodes.put(node.identity, node);
            if (!node.linkParent()) {
                unlinkedIDs.add(node.identity);
            }
        }
        return node.identity;
    }

    public T get(BigInteger key) {
        BlockchainNode node = nodes.get(key);
        return node != null ? node.value : null;
    }

    public BigInteger getParent(BigInteger key) {
        BlockchainNode node = nodes.get(key);
        return node != null ? node.parent : null;
    }

    public Iterator<BigInteger> getChildren(BigInteger key) {
        BlockchainNode node = nodes.get(key);
        return node != null ? node.childNodes.iterator() : Collections.emptyIterator();
    }

    public BigInteger resolveGenesis() {
        Iterator<BigInteger> it = unlinkedIDs.iterator();
        while (it.hasNext()) {
            BlockchainNode node = nodes.get(it.next());
            if (node.linkParent()) {
                it.remove();
            }
        }
        return unlinkedIDs.size() == 1 ? unlinkedIDs.iterator().next() : null;
    }

    @Override
    public Iterator<Map.Entry<BigInteger, T>> iterator() {
        return new Iterator<Map.Entry<BigInteger, T>> () {

            private Iterator<BigInteger> it = nodes.keySet().iterator();

            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public Map.Entry<BigInteger, T> next() {
                return new BlockchainEntry(it.next());
            }
        };
    }

    private static byte[] serialize(Object object) {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        try (ObjectOutputStream objectStream = new ObjectOutputStream(byteStream)) {
            objectStream.writeObject(object);
            return byteStream.toByteArray();
        } catch (IOException e) {
            throw new Error(e);
        }
    }

    private class BlockchainEntry implements Map.Entry<BigInteger, T> {

        private final BigInteger key;

        public BlockchainEntry(BigInteger key) {
            this.key = key;
        }

        @Override
        public BigInteger getKey() {
            return key;
        }

        @Override
        public T getValue() {
            return nodes.get(key).value;
        }

        @Override
        public T setValue(T o) {
            throw new UnsupportedOperationException();
        }
    }

    private class BlockchainNode {
        private final BigInteger parent;
        private final BigInteger identity;
        private final T value;

        private BlockchainNode parentNode;
        private final HashSet<BigInteger> childNodes;

        private BlockchainNode(BigInteger parent, T value) {
            digester.reset();
            byte[] p = parent.toByteArray();
            digester.update(p);
            this.parent = parent;
            this.identity = new BigInteger(digester.digest(serialize(value)));
            this.value = value;
            this.parentNode = null;
            this.childNodes = new HashSet<>();
        }

        private boolean linkParent() {
            BlockchainNode parentNode = nodes.get(parent);

            if (this.parentNode == null && parentNode != null) {
                this.parentNode = parentNode;
                this.parentNode.childNodes.add(identity);
            }

            return this.parentNode != null;
        }
    }
}
