package com.gregswebserver.catan.common.game.scoring.achievement;

import java.util.*;

/**
 * Created by greg on 5/26/16.
 * A collections of keys with associated
 */
public class Scoreboard<K,V extends Comparable<V>,O extends Comparable<O>> implements Iterable<K> {

    private final Comparator<V> vComparator;
    private final Comparator<O> oComparator;
    private final Map<K, Entry> keyMap;
    private final Map<V, Entry> valueMap;
    private final SortedSet<Entry> entries;

    public Scoreboard(Comparator<V> vComparator, Comparator<O> oComparator) {
        this.vComparator = vComparator;
        this.oComparator = oComparator;
        keyMap = new HashMap<>();
        valueMap = new HashMap<>();
        entries = new TreeSet<>(new CompositeComparator());
    }

    public Scoreboard(Comparator<V> vComparator) {
        this(vComparator, Comparator.naturalOrder());
    }

    public Scoreboard() {
        this(Comparator.naturalOrder(), Comparator.naturalOrder());
    }

    public void add(K key, V value, O order) {
        add(new Entry(key, value, order));
    }

    private void add(Entry e) {
        keyMap.put(e.key, e);
        valueMap.put(e.value, e);
        entries.add(e);
    }

    private void remove(Entry e) {
        keyMap.remove(e.key);
        valueMap.remove(e.value);
        entries.remove(e);
    }

    public V get(K key) {
        return keyMap.get(key).value;
    }

    public V removeKey(K key) {
        if (keyMap.containsKey(key))
            return null;
        Entry e = keyMap.get(key);
        remove(e);
        return e.value;
    }

    public K removeValue(V value) {
        if (valueMap.containsKey(value))
            return null;
        Entry e = valueMap.get(value);
        remove(e);
        return e.key;
    }

    @Override
    public Iterator<K> iterator() {
        return new Iterator<K>() {
            private final Iterator<Entry> treeIterator = entries.iterator();

            @Override
            public boolean hasNext() {
                return treeIterator.hasNext();
            }

            @Override
            public K next() {
                return treeIterator.next().key;
            }
        };
    }

    private class Entry {

        private final K key;
        private final V value;
        private final O order;

        private Entry(K key, V value, O order) {
            this.key = key;
            this.value = value;
            this.order = order;
        }
    }

    private class CompositeComparator implements Comparator<Entry> {
        @Override
        public int compare(Entry a, Entry b) {
            int vCompare = vComparator.compare(a.value,b.value);
            return ((vCompare != 0) ? vCompare : oComparator.compare(a.order, b.order));
        }
    }

}
