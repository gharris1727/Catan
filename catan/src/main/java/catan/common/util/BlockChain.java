package catan.common.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.Map.Entry;

/**
 * Created by greg on 7/4/17.
 * Implementation of a MessageDigest-based blockchain value store.
 */
public class BlockChain<T> implements Iterable<Entry<BigInteger, T>> {

    private final MessageDigest digester;
    private final Map<BigInteger, BlockchainNode> nodes;
    private final Set<BigInteger> unlinkedIDs;

    public BlockChain(String hashType) throws NoSuchAlgorithmException {
        digester = MessageDigest.getInstance(hashType);
        nodes = new HashMap<>();
        unlinkedIDs = new HashSet<>();
    }

    public BigInteger addBlock(BigInteger parent, T value) {
        if (parent == null) {
            parent = new BigInteger("0");
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
        return (node != null) ? node.value : null;
    }

    public BigInteger getParent(BigInteger key) {
        BlockchainNode node = nodes.get(key);
        return (node != null) ? node.parent : null;
    }

    public Collection<BigInteger> getChildren(BigInteger key) {
        BlockchainNode node = nodes.get(key);
        return (node != null) ? Collections.unmodifiableCollection(node.childNodes) : Collections.emptySet();
    }

    public BigInteger resolveGenesis() {
        Iterator<BigInteger> it = unlinkedIDs.iterator();
        while (it.hasNext()) {
            BlockchainNode node = nodes.get(it.next());
            if (node.linkParent()) {
                it.remove();
            }
        }
        return (unlinkedIDs.size() == 1) ? unlinkedIDs.iterator().next() : null;
    }

    @Override
    public Iterator<Entry<BigInteger, T>> iterator() {
        return new Iterator<Entry<BigInteger, T>> () {

            private final Iterator<BigInteger> it = nodes.keySet().iterator();

            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public Entry<BigInteger, T> next() {
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
            throw new RuntimeException(e);
        }
    }

    private final class BlockchainEntry implements Entry<BigInteger, T> {

        private final BigInteger key;

        private BlockchainEntry(BigInteger key) {
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
        public T setValue(T v) {
            throw new UnsupportedOperationException();
        }
    }

    private final class BlockchainNode {
        private final BigInteger parent;
        private final BigInteger identity;
        private final T value;

        private BlockchainNode parentNode;
        private final Set<BigInteger> childNodes;

        private BlockchainNode(BigInteger parent, T value) {
            digester.reset();
            byte[] p = parent.toByteArray();
            digester.update(p);
            this.parent = parent;
            identity = new BigInteger(digester.digest(serialize(value)));
            this.value = value;
            parentNode = null;
            childNodes = new HashSet<>();
        }

        private boolean linkParent() {
            BlockchainNode parentNode = nodes.get(parent);

            if ((this.parentNode == null) && (parentNode != null)) {
                this.parentNode = parentNode;
                this.parentNode.childNodes.add(identity);
            }

            return this.parentNode != null;
        }
    }
}
